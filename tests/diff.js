#!/usr/bin/env node
/* Differential tester: runs every program under tests/programs on the REAL JDK and on the in-browser JavaZero compiler and compares.
   Usage: node tests/diff.js [dir-or-file ...] [--regen] [-v]      Expected results are cached next to each program in <name>.expected.json */
const fs = require('fs'), path = require('path'), os = require('os'), { spawnSync } = require('child_process');
const { load } = require('./harness.js');
const args = process.argv.slice(2), regen = args.includes('--regen'), verbose = args.includes('-v');
const targets = args.filter(a => !a.startsWith('-'));
if (!targets.length) targets.push(path.join(__dirname, 'programs'));
const files = [];
const walk = p => { const st = fs.statSync(p); if (st.isDirectory()) fs.readdirSync(p).sort().forEach(f => walk(path.join(p, f))); else if (p.endsWith('.java')) files.push(p); };
targets.forEach(walk);
const ctx = load();
const clean = s => s.split('\n').filter(l => !l.startsWith('Picked up')).join('\n');
function real(file) {
  const exp = file.replace(/\.java$/, '.expected.json');
  if (!regen && fs.existsSync(exp)) return JSON.parse(fs.readFileSync(exp, 'utf8'));
  const d = fs.mkdtempSync(path.join(os.tmpdir(), 'jz-'));
  fs.copyFileSync(file, path.join(d, 'Main.java'));
  const inFile = file.replace(/\.java$/, '.in');
  const input = fs.existsSync(inFile) ? fs.readFileSync(inFile, 'utf8') : '';
  const c = spawnSync('javac', ['-d', d, '-Xlint:none', 'Main.java'], { cwd: d, encoding: 'utf8' });
  let res;
  if (c.status !== 0) {
    const m = clean(c.stderr).match(/Main\.java:(\d+): error: (.*)/);
    res = { compile: m ? { line: +m[1], msg: m[2] } : { line: 0, msg: clean(c.stderr).slice(0, 200) } };
  } else {
    const r = spawnSync('java', ['-Xss64m', '-cp', d, 'Main'], { cwd: d, encoding: 'utf8', input, timeout: 15000, maxBuffer: 50 << 20 });
    const err = clean(r.stderr || '').trim();
    const m = err.match(/^Exception in thread "main" (.*)$/m);
    res = { stdout: r.stdout, exception: m ? m[1] : null, exit: r.status, timeout: r.error && r.error.code === 'ETIMEDOUT' };
  }
  fs.writeFileSync(exp, JSON.stringify(res, null, 1));
  fs.rmSync(d, { recursive: true, force: true });
  return res;
}
// the sandbox JVM runs with an ASCII stdout charset and prints '?' for every non-ASCII code point
const norm = s => s.replace(/\r/g, '').replace(/[^\x00-\x7f]/gu, '?').replace(/\s+$/, '');
let pass = 0, fail = 0; const failures = [];
for (const f of files) {
  const src = fs.readFileSync(f, 'utf8');
  const exp = real(f);
  const inFile = f.replace(/\.java$/, '.in');
  const stdin = fs.existsSync(inFile) ? fs.readFileSync(inFile, 'utf8').replace(/\n$/, '').split('\n') : [];
  let r;
  const t0 = Date.now();
  try { r = ctx.Java.run(src, { stdin, timeLimit: 20000, echo: false }); } catch (e) { r = { out: [], err: { kind: 'crash', text: e.stack } }; }
  const ms = Date.now() - t0;
  let why = null;
  if (exp.compile) {
    if (!r.err || r.err.kind !== 'compile') why = `expected compile error line ${exp.compile.line}: ${exp.compile.msg}\n    but got: ${r.err ? JSON.stringify(r.err).slice(0, 200) : 'a successful run: ' + JSON.stringify(r.out.slice(0, 3))}`;
    else {
      const e0 = r.err.list[0];
      const okMsg = e0.msg.startsWith(exp.compile.msg) || exp.compile.msg.startsWith(e0.msg) || e0.msg.replace(/:.*$/, '') === exp.compile.msg.replace(/:.*$/, '');
      if (e0.line !== exp.compile.line || !okMsg) why = `compile error mismatch\n    javac: line ${exp.compile.line}: ${exp.compile.msg}\n    ours:  line ${e0.line}: ${e0.msg}`;
    }
  } else {
    if (r.err && r.err.kind === 'compile') why = `unexpected compile error: ${JSON.stringify(r.err.list.slice(0, 2))}`;
    else if (r.needInput) why = 'wanted more input than provided';
    else {
      const got = norm(r.out.join('\n'));
      if (exp.exception) {
        if (!r.err || r.err.kind !== 'runtime') why = `expected exception ${exp.exception} but ran fine`;
        else if (r.err.text.split('\n')[0] !== exp.exception.split('\n')[0]) why = `exception mismatch\n    java: ${exp.exception}\n    ours: ${r.err.text.split('\n')[0]}`;
      } else if (r.err) why = `unexpected runtime error: ${JSON.stringify(r.err).slice(0, 300)}`;
      if (!why && got !== norm(exp.stdout)) {
        const a = got.split('\n'), b = norm(exp.stdout).split('\n'); let i = 0; while (i < a.length && i < b.length && a[i] === b[i]) i++;
        why = `stdout differs at line ${i + 1}\n    java: ${JSON.stringify(b[i])}\n    ours: ${JSON.stringify(a[i])}`;
      }
    }
  }
  if (why) { fail++; failures.push([f, why]); console.log(`FAIL ${path.relative(__dirname, f)}\n  ${why}`); }
  else { pass++; if (verbose) console.log(`ok   ${path.relative(__dirname, f)} (${ms}ms)`); }
}
console.log(`\n${pass} passed, ${fail} failed, ${files.length} total`);
process.exit(fail ? 1 : 0);
