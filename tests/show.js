#!/usr/bin/env node
/* Shows real-JDK vs JavaZero output for one program:  node tests/show.js path/to/File.java */
const fs = require('fs'), { load } = require('./harness.js');
const f = process.argv[2]; const src = fs.readFileSync(f, 'utf8');
const exp = JSON.parse(fs.readFileSync(f.replace(/\.java$/, '.expected.json'), 'utf8'));
const inF = f.replace(/\.java$/, '.in');
const stdin = fs.existsSync(inF) ? fs.readFileSync(inF, 'utf8').replace(/\n$/, '').split('\n') : [];
const r = load().Java.run(src, { stdin, debug: true });
if (r.err && r.err.js) console.log('JS STACK:\n' + r.err.js.split('\n').slice(0, 14).join('\n'));
const a = (exp.compile ? ['COMPILE ' + JSON.stringify(exp.compile)] : exp.stdout.replace(/\n$/, '').split('\n').concat(exp.exception ? ['!! ' + exp.exception] : []));
const b = r.err && r.err.kind === 'compile' ? ['COMPILE ' + JSON.stringify(r.err.list)] : r.out.concat(r.err ? ['!! ' + r.err.text] : []);
const n = Math.max(a.length, b.length), w = 70;
console.log('JAVA'.padEnd(w) + ' | OURS');
for (let i = 0; i < n; i++) { const x = a[i] === undefined ? '' : a[i], y = b[i] === undefined ? '' : b[i]; console.log((x === y ? ' ' : '*') + x.slice(0, w - 1).padEnd(w - 1) + ' | ' + y.slice(0, 120)); }
