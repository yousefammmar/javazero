/* JavaZero compiler, public API.
   Pipeline: Java source -> JavaParser (AST) -> JavaGen (type checks + JavaScript) -> executed against JavaRT.
     Java.run(src, { stdin: [lines], interactive: bool })  -> { out: [lines], err: null | {kind:'compile', list:[{line,msg}]} | {kind:'runtime', text}, needInput?, prompt? }
     Java.check(src)                                        -> [{ line, msg }]   (compile errors only)
     Java.compile(src)                                      -> { js, main }       (throws { errors } on a compile error)
   Teaching-oriented subset of Java 21 (see README "The in-browser Java runner"); it is not a JVM. */
const Java = (() => {
  function compile(src) {
    let unit;
    try { unit = JavaParser.parse(src); } catch (e) { if (e && e.msg !== undefined) throw { errors: [{ line: e.line, msg: e.msg }] }; throw e; }
    return new JavaGen.Gen(unit).generate();
  }
  function check(src) {
    try { compile(src); return []; } catch (e) { return e && e.errors ? e.errors : [{ line: 0, msg: 'internal compiler error: ' + (e && e.message) }]; }
  }
  const describe = (rt, ex) => {
    let text = ex.toString();
    for (let c = ex.getCause(), n = 0; c && n < 5; c = c.getCause(), n++) text += '\nCaused by: ' + c.toString();
    return text;
  };
  function run(src, opts = {}) {
    let compiled;
    try { compiled = compile(src); } catch (e) {
      if (e && e.errors) return { out: [], err: { kind: 'compile', list: e.errors } };
      return { out: [], err: { kind: 'compile', list: [{ line: 0, msg: 'internal compiler error: ' + (e && e.message) }] } };
    }
    const deadline = Date.now() + (opts.timeLimit || 6000);
    let n = 0;
    const tick = () => { if ((++n & 4095) === 0) { if (Date.now() > deadline) throw new Error('Time limit exceeded (possible infinite loop)'); if ((n & 262143) === 0 && typeof performance !== 'undefined' && performance.memory && performance.memory.usedJSHeapSize > 1.2e9) throw new Error('Output limit exceeded: the program is using too much memory'); } return true; };
    const rt = JavaRT.create({ stdin: opts.stdin, interactive: opts.interactive, tick, debug: opts.debug, echo: opts.echo });
    let main;
    try { main = new Function('$rt', compiled.js)(rt); } catch (e) {
      return { out: [], err: { kind: 'compile', list: [{ line: 0, msg: 'internal compiler error: ' + (e && e.message) }] } };
    }
    try {
      if (main.$si && !Object.prototype.hasOwnProperty.call(main, '$d')) main.$si();
      main.main(rt.$arr('String', []));
    } catch (e0) {
      let e = e0;
      if (e instanceof rt.ExitSignal) { return { out: rt.$flush(), err: null }; }
      if (e instanceof rt.NeedInput) { const o = rt.$out(); return { out: o.lines.slice(), err: null, needInput: true, prompt: o.cur }; }
      try { e = rt.$wrap(e); } catch (x) { if (x instanceof rt.ExitSignal) return { out: rt.$flush(), err: null }; if (x instanceof rt.NeedInput) { const o = rt.$out(); return { out: o.lines.slice(), err: null, needInput: true, prompt: o.cur }; } e = x; }
      const out = rt.$flush();
      if (e instanceof rt.J) return { out, err: { kind: 'runtime', text: describe(rt, e), js: e.$js && e.$js.stack } };
      if (e && /^(Time limit|Output limit)/.test(e.message || '')) return { out, err: { kind: 'runtime', text: 'java.lang.Error: ' + e.message } };
      return { out, err: { kind: 'runtime', text: 'java.lang.InternalError: ' + (e && e.message ? e.message : e) } };
    }
    return { out: rt.$flush(), err: null };
  }
  return { run, check, compile, transpile: compile };
})();
if (typeof module !== 'undefined') module.exports = Java;
