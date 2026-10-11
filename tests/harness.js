/* Loads the JavaZero compiler files into a vm context exactly like the browser does (classic scripts sharing a global scope). */
const fs = require('fs'), vm = require('vm'), path = require('path');
const root = path.join(__dirname, '..', 'js');
const FILES = ['core/java-parse.js', 'core/java-rt.js', 'core/java-coll.js', 'core/java-gen.js', 'core/java-gen-expr.js', 'core/java-gen-call.js', 'core/java-flow.js', 'core/java.js'];
function load(extra = []) {
  const ctx = vm.createContext({ console, Date, performance: { now: () => Date.now() } });
  for (const f of [...FILES, ...extra]) vm.runInContext(fs.readFileSync(path.join(root, f), 'utf8'), ctx, { filename: f });
  vm.runInContext('this.Java = Java; this.JavaGen = JavaGen;', ctx);
  return ctx;
}
module.exports = { load, root };
