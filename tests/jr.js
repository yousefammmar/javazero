#!/usr/bin/env node
/* Ask the real JDK: node tests/jr.js 'statements...' [imports]   (wraps in a main method; prints stdout then stderr first line) */
const fs = require('fs'), os = require('os'), path = require('path'), { spawnSync } = require('child_process');
const body = process.argv[2], extra = process.argv[3] || '';
const d = fs.mkdtempSync(path.join(os.tmpdir(), 'jr-'));
fs.writeFileSync(path.join(d, 'Main.java'), `import java.util.*; import java.util.function.*; import java.util.stream.*;\n${extra}\npublic class Main { public static void main(String[] args) throws Exception {\n${body}\n} }`);
const c = spawnSync('javac', ['-d', d, 'Main.java'], { cwd: d, encoding: 'utf8' });
if (c.status) { console.log((c.stderr || '').split('\n').filter(l => !l.startsWith('Picked')).join('\n')); process.exit(1); }
const r = spawnSync('java', ['-cp', d, 'Main'], { encoding: 'utf8' });
process.stdout.write(r.stdout); const e = (r.stderr || '').split('\n').filter(l => !l.startsWith('Picked up') && l.trim() && !/^\s+at /.test(l)).join('\n'); if (e) console.log('STDERR: ' + e);
