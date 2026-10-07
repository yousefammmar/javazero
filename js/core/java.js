/* Teaching-subset Java runner: checks common syntax mistakes, transpiles to JS, runs it.
   Supports classes, fields, constructors, inheritance, interfaces, static members, loops, arrays,
   String/Math basics, ArrayList. Not a JVM: formatting of doubles (9 vs 9.0) is approximate. */
const Java = (() => {
  const TYPE = '(?:int|long|short|byte|double|float|boolean|char|String|var|void|[A-Z]\\w*(?:<[^>]*>)?)(?:\\[\\])*';
  const MODS = '(?:(?:public|private|protected|static|final|abstract|default|synchronized)\\s+)*';
  const INT = new RegExp(`^${MODS}(${TYPE})\\s+(\\w+)\\s*\\(([^)]*)\\)\\s*(?:throws [\\w, ]+)?\\s*(\\{|;)?(.*)$`);
  const FIELD = new RegExp(`^(${MODS})(${TYPE})\\s+(\\w+)\\s*(?:=\\s*(.*))?;\\s*$`);
  const CLASS = /^(?:(?:public|private|protected|static|final|abstract)\s+)*(class|interface)\s+(\w+)(?:\s+extends\s+(\w+))?(?:\s+implements\s+([\w,\s]+?))?\s*\{(.*)$/;
  const DEFAULTS = { int: '0', long: '0', short: '0', byte: '0', double: '0', float: '0', boolean: 'false', char: "'\\0'" };
  const strip = p => p.replace(new RegExp(`(?:final\\s+)?${TYPE}\\s+(\\w+)`, 'g'), '$1');
  const count = (s, c) => s.split(c).length - 1;

  const prelude = `
String.prototype.equals=function(o){return String(this)===String(o)};
String.prototype.hashCode=function(){let h=0;for(const c of String(this))h=(Math.imul(31,h)+c.charCodeAt(0))|0;return h};
String.prototype.isEmpty=function(){return this.length===0};
class ArrayList extends Array{add(x){this.push(x);return true}get(i){return this[i]}size(){return this.length}}
const Integer={parseInt:s=>{if(!/^\\s*-?\\d+\\s*$/.test(s))throw new Error('NumberFormatException: For input string: "'+s+'"');return parseInt(s,10)},MAX_VALUE:2147483647,MIN_VALUE:-2147483648};
const String_valueOf=x=>String(x);
const __div=(a,b)=>{if(b===0)throw new Error('ArithmeticException: / by zero');return Math.trunc(a/b)};`;

  function check(src) {
    const errs = [], lines = src.split('\n');
    let depth = 0;
    lines.forEach((raw, i) => {
      const l = raw.replace(/"(?:[^"\\]|\\.)*"/g, '""').replace(/\/\/.*$/, '').trim();
      depth += count(l, '{') - count(l, '}');
      const bare = /^System\.out\.print(ln)?\(/.test(l) || (/^(final\s+)?(int|long|double|float|boolean|char|String|var)\b[^()]*=/.test(l));
      if (l && bare && !/[;{},(]$/.test(l)) errs.push({ line: i + 1, msg: "';' expected" });
    });
    if (depth > 0) errs.push({ line: lines.length, msg: 'reached end of file while parsing (missing "}")' });
    if (depth < 0) errs.push({ line: lines.length, msg: 'class, interface, or enum expected (extra "}")' });
    if (!/static\s+void\s+main\s*\(/.test(src)) errs.push({ line: 1, msg: 'no main method found: add public static void main(String[] args)' });
    return errs;
  }

  function transpile(src) {
    const strs = [];
    let s = src.replace(/\/\*[\s\S]*?\*\//g, '').replace(/"(?:[^"\\]|\\.)*"/g, m => `\u0000${strs.push(m) - 1}\u0000`)
      .replace(/\/\/.*$/gm, '').replace(/^\s*(import|package)\b.*$/gm, '').replace(/@Override/g, '');
    const lines = s.split('\n').flatMap(l => { const m = l.trim().match(CLASS); return m && m[5].replace(/[\s}]/g, '') ? [l.slice(0, l.indexOf('{') + 1), ...m[5].replace(/;/g, ';\n').replace(/\}/g, '\n}\n').split('\n')].filter(x => x.trim()) : [l]; });

    // pass 1: collect class shapes (fields, methods, parents, interfaces)
    const classes = {}, stack0 = [];
    lines.forEach(raw => {
      const l = raw.trim(), top = stack0[stack0.length - 1];
      const m = l.match(CLASS);
      if (m) { classes[m[2]] = { name: m[2], parent: m[3], impl: (m[4] || '').split(',').map(x => x.trim()).filter(Boolean), fields: {}, methods: {}, arity: {} }; stack0.push({ k: 'class', c: classes[m[2]] }); }
      else if (top && top.k === 'class') {
        let f = l.match(FIELD), mm = !f && l.match(INT);
        if (f && !/\b(return|new)\b.*\(/.test(f[0].split('=')[0])) top.c.fields[f[3]] = /static/.test(f[1]);
        else if (mm && mm[2] !== top.c.name) { top.c.methods[mm[2]] = /static/.test(l.split('(')[0]); (Object.hasOwn(top.c.arity, mm[2]) ? top.c.arity[mm[2]] : (top.c.arity[mm[2]] = new Set())).add(strip(mm[3]).split(',').filter(x => x.trim()).length); }
        if (count(l, '{') - count(l, '}') > 0) stack0.push({ k: 'block' });
      } else if (top) { const n = count(l, '{') - count(l, '}'); for (let i = 0; i < n; i++) stack0.push({ k: 'block' }); }
      const n = count(l, '{') - count(l, '}');
      if (!m && n < 0) for (let i = 0; i < -n; i++) stack0.pop();
      else if (m) { const nn = n - 1; for (let i = 0; i < -nn; i++) stack0.pop(); }
    });
    const chain = (c, key) => { const o = {}; for (let k = c; k; k = classes[k.parent]) Object.assign(o, { ...k[key], ...o }); return o; };
    const intVars = new Set([...src.matchAll(/\b(?:int|long|short|byte)\s+(\w+)\s*[=;,)]/g)].map(m => m[1]));

    // pass 2: rewrite
    const out = [], stack = [], impls = [];
    let mainClass = null, locals = new Set(), isStatic = false;
    const qualify = (t, cls) => {
      if (!cls) return t;
      const fields = chain(cls, 'fields'), methods = chain(cls, 'methods');
      return t.replace(/(?<![\w.$\u0000])([A-Za-z_]\w*)(?![\w\u0000])/g, (w, n, off, all) => {
        if (locals.has(n)) return w;
        const next = all.slice(off + w.length).trimStart()[0];
        if (Object.hasOwn(fields, n) && next !== '(') { const st = fields[n]; return st ? `${owner(cls, 'fields', n)}.${n}` : (isStatic ? w : `this.${n}`); }
        if (Object.hasOwn(methods, n) && next === '(') { const st = methods[n]; return st ? `${owner(cls, 'methods', n)}.${n}` : (isStatic ? w : `this.${n}`); }
        return w;
      });
    };
    const owner = (c, key, n) => { for (let k = c; k; k = classes[k.parent]) if (Object.hasOwn(k[key], n)) return k.name; return c.name; };
    const body = t => {
      t = t.replace(/\(\s*int\s*\)\s*(\w+(?:\.\w+)*|\([^()]*\))/g, 'Math.trunc($1)').replace(/\(\s*double\s*\)\s*/g, '')
        .replace(/\bfor\s*\(\s*(?:final\s+)?TYPE\s+(\w+)\s*:\s*/.source ? new RegExp(`\\bfor\\s*\\(\\s*(?:final\\s+)?${TYPE}\\s+(\\w+)\\s*:\\s*`, 'g') : '', 'for (const $1 of ')
        .replace(new RegExp(`\\bfor\\s*\\(\\s*(?:int|long|double)\\s+`, 'g'), 'for (let ')
        .replace(new RegExp(`^(\\s*)(?:final\\s+)?${TYPE}\\s+(\\w+)\\s*=\\s*\\{(.*)\\}\\s*;`), '$1let $2 = [$3];')
        .replace(new RegExp(`(^\\s*|[{;]\\s*)(?:final\\s+)?${TYPE}\\s+(\\w+)\\s*(=|;)`, 'g'), '$1let $2 $3')
        .replace(/\bnew\s+(?:int|long|double|boolean|char|byte|short|float)\[(\w+)\]/g, 'new Array($1).fill(0)')
        .replace(/\bnew\s+String\[(\w+)\]/g, 'new Array($1).fill(null)')
        .replace(/(\w)<[\w\s,?<>]*>(?=\s*\()/g, '$1')
        .replace(/\.length\(\)/g, '.length').replace(/\.replace\(/g, '.replaceAll(')
        .replace(/System\.out\.println\(/g, '__p(').replace(/System\.out\.print\(/g, '__w(')
        .replace(/\bString\.valueOf\(/g, 'String(').replace(/(\d)[Lf]\b/g, '$1')
        .replace(/\bcatch\s*\(\s*[\w.|\s]+\s+(\w+)\s*\)/g, 'catch ($1)').replace(/\bwhile\s*\((.*)\)\s*\{/, 'while (__t() && ($1)) {')
        .replace(/(?<![\w.])(\w+)\s*\/\s*(\w+)(?![\w.])/g, (m, a, b) => (intVars.has(a) || /^\d+$/.test(a)) && (intVars.has(b) || /^\d+$/.test(b)) ? `__div(${a}, ${b})` : m);
      return t;
    };
    lines.forEach(raw => {
      let l = raw.trim();
      if (!l) return;
      const top = stack[stack.length - 1], cls = top && top.c;
      let pushKind = null, t = l;
      const m = l.match(CLASS);
      if (m) {
        const c = classes[m[2]];
        c.impl.forEach(i => impls.push(`__impl(${c.name},${i})`));
        const disp = Object.keys(c.arity).filter(k => c.arity[k].size > 1).map(k => `${c.methods[k] ? 'static ' : ''}${k}(...a) { return this['__${k}_' + a.length](...a); }`).join(' ');
        t = `class ${c.name}${c.parent ? ' extends ' + c.parent : ''} { ${disp}${m[5]}`; pushKind = { k: 'class', c };
      } else if (top && top.k === 'class') {
        const f = l.match(FIELD), ctor = l.match(new RegExp(`^${MODS}(${cls.name})\\s*\\(([^)]*)\\)\\s*\\{(.*)$`)), me = !f && l.match(INT);
        if (f) { const def = f[4] != null ? body(f[4].replace(/^\{(.*)\}$/, '[$1]')) : (DEFAULTS[f[2]] || 'null'); t = `${/static/.test(f[1]) ? 'static ' : ''}${f[3]} = ${qualifyDef(def)};`; }
        else if (ctor) {
          locals = new Set(strip(ctor[2]).split(',').map(x => x.trim()).filter(Boolean)); isStatic = false;
          const rest = ctor[3], needSuper = cls.parent && !/\bsuper\s*\(/.test(rest);
          t = `constructor(${strip(ctor[2])}) {${needSuper ? ' super();' : ''}${body(qualify(rest, cls))}`; pushKind = { k: 'method', c: cls };
        } else if (me) {
          isStatic = /\bstatic\b/.test(l.split('(')[0]);
          locals = new Set(strip(me[3]).split(',').map(x => x.trim()).filter(Boolean));
          const rest = me[5] || '';
          if (me[2] === 'main' && !mainClass) mainClass = cls.name;
          const nm = Object.hasOwn(cls.arity, me[2]) && cls.arity[me[2]].size > 1 ? `__${me[2]}_${strip(me[3]).split(',').filter(x => x.trim()).length}` : me[2];
          t = me[4] === ';' ? `${me[2]}() {}${rest}` : `${isStatic ? 'static ' : ''}${nm}(${strip(me[3])}) {${body(qualify(rest, cls))}`; if (me[4] === '{') pushKind = { k: 'method', c: cls };
        } else t = body(qualify(l, cls));
      } else if (top) {
        [...l.matchAll(new RegExp(`(?:^|[({;])\\s*(?:final\\s+)?${TYPE}\\s+(\\w+)\\s*[=;:]`, 'g'))].forEach(x => locals.add(x[1]));
        [...l.matchAll(/\bfor\s*\(\s*(?:final\s+)?\w[\w<>\[\]]*\s+(\w+)\s*[:=]/g)].forEach(x => locals.add(x[1]));
        [...l.matchAll(/\bcatch\s*\(\s*[\w.|\s]+\s+(\w+)\s*\)/g)].forEach(x => locals.add(x[1]));
        t = body(qualify(l, cls)); pushKind = { k: 'block', c: cls };
      } else t = l;
      out.push(t);
      const n = count(l, '{') - count(l, '}') - (m && m[5] ? count(m[5], '{') - count(m[5], '}') : 0) + (m && m[5] ? count(m[5], '{') - count(m[5], '}') : 0);
      const net = count(l, '{') - count(l, '}');
      if (net > 0 && pushKind) for (let i = 0; i < net; i++) stack.push(i === 0 ? pushKind : { k: 'block', c: cls });
      else if (net > 0) for (let i = 0; i < net; i++) stack.push({ k: 'block', c: cls });
      else for (let i = 0; i < -net; i++) stack.pop();
      void n;
    });
    function qualifyDef(d) { return d; }
    let js = out.join('\n').replace(/\u0000(\d+)\u0000/g, (_, i) => strs[i]);
    if (!mainClass) mainClass = Object.keys(classes)[0];
    return `${js}\n${impls.join(';')};\n${mainClass}.main([]);`;
  }

  function run(src) {
    const errs = check(src);
    if (errs.length) return { out: [], err: { kind: 'compile', list: errs } };
    const out = [];
    let cur = '', ticks = 0;
    const p = x => { out.push(cur + fmt(x)); cur = ''; }, w = x => { cur += fmt(x); };
    const fmt = x => x === null || x === undefined ? 'null' : Array.isArray(x) ? `[${x.join(', ')}]` : String(x);
    const tick = () => { if (++ticks > 300000) throw new Error('Time limit exceeded (possible infinite loop)'); return true; };
    const impl = (c, i) => { if (!i) return; for (const k of Object.getOwnPropertyNames(i.prototype)) if (k !== 'constructor' && !(k in c.prototype)) c.prototype[k] = i.prototype[k]; };
    let js;
    try { js = transpile(src); } catch (e) { return { out, err: { kind: 'compile', list: [{ line: 1, msg: 'could not parse this program: ' + e.message }] } }; }
    try {
      new Function('__p', '__w', '__t', '__impl', prelude + '\n' + js)(p, w, tick, impl);
      if (cur) out.push(cur);
    } catch (e) {
      if (cur) out.push(cur);
      const msg = String(e.message);
      let kind, text;
      if (e instanceof ReferenceError) { kind = 'compile'; text = `cannot find symbol: ${msg.split(' ')[0]}`; return { out, err: { kind, list: [{ line: 0, msg: text }] } }; }
      if (e instanceof TypeError && /null|undefined/.test(msg)) text = 'java.lang.NullPointerException';
      else if (e instanceof RangeError) text = 'java.lang.StackOverflowError';
      else if (/^(NumberFormatException|ArithmeticException)/.test(msg)) text = 'java.lang.' + msg;
      else text = 'java.lang.RuntimeException: ' + msg;
      return { out, err: { kind: 'runtime', text } };
    }
    return { out, err: null };
  }
  return { run, check, transpile };
})();
