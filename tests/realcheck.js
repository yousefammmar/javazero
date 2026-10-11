#!/usr/bin/env node
/* For corpus authors: compiles + runs program(s) on the REAL JDK twice and checks they are deterministic.
   Usage: node tests/realcheck.js file.java [...]   (add --err for programs that are SUPPOSED to fail to compile) */
const fs = require('fs'), path = require('path'), os = require('os'), { spawnSync } = require('child_process');
const args = process.argv.slice(2), expectErr = args.includes('--err');
let bad = 0;
for (const f of args.filter(a => !a.startsWith('--'))) {
  const d = fs.mkdtempSync(path.join(os.tmpdir(), 'rc-'));
  fs.copyFileSync(f, path.join(d, 'Main.java'));
  const c = spawnSync('javac', ['-d', d, '-Xlint:none', 'Main.java'], { cwd: d, encoding: 'utf8' });
  const clean = s => (s || '').split('\n').filter(l => !l.startsWith('Picked up')).join('\n');
  if (c.status !== 0) {
    if (expectErr) { const m = clean(c.stderr).match(/Main\.java:(\d+): error: (.*)/); console.log(`OK   ${f}: javac error line ${m && m[1]}: ${m && m[2]}  (errors total: ${(clean(c.stderr).match(/error:/g) || []).length})`); }
    else { bad++; console.log(`BAD  ${f}: does not compile\n${clean(c.stderr).slice(0, 400)}`); }
    continue;
  }
  if (expectErr) { bad++; console.log(`BAD  ${f}: expected a compile error but it compiled`); continue; }
  const inFile = f.replace(/\.java$/, '.in'); const input = fs.existsSync(inFile) ? fs.readFileSync(inFile, 'utf8') : '';
  const run = () => spawnSync('java', ['-Xss64m', '-cp', d, 'Main'], { cwd: d, encoding: 'utf8', input, timeout: 15000 });
  const a = run(), b = run();
  if (a.stdout !== b.stdout || clean(a.stderr).split('\n')[0] !== clean(b.stderr).split('\n')[0]) { bad++; console.log(`BAD  ${f}: NON-DETERMINISTIC output between two runs`); continue; }
  const ex = clean(a.stderr).match(/^Exception in thread "main" (.*)$/m);
  console.log(`OK   ${f}: exit ${a.status}${ex ? ' uncaught ' + ex[1].slice(0, 80) : ''}, ${a.stdout.split('\n').length - 1} lines of output`);
  fs.rmSync(d, { recursive: true, force: true });
}
process.exit(bad ? 1 : 0);
