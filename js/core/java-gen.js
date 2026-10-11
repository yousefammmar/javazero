/* JavaZero compiler, stage 2a: class table, overload groups, class / constructor / method generation.
   Statements, expressions and flow analysis are added to Gen.prototype by java-gen-expr.js and java-flow.js. */
const JavaGen = (() => {
  const RT = typeof JavaRT !== 'undefined' ? JavaRT : require('./java-rt.js');

  const PRIM = new Set(['int', 'long', 'short', 'byte', 'double', 'float', 'boolean', 'char']);
  const NUMP = new Set(['int', 'long', 'short', 'byte', 'double', 'float', 'char']);
  const BOX = { Integer: 'int', Long: 'long', Short: 'short', Byte: 'byte', Double: 'double', Float: 'float', Boolean: 'boolean', Character: 'char' };
  const RANK = { byte: 1, short: 2, char: 2, int: 3, long: 4, float: 5, double: 6 };
  const JS_RESERVED = new Set('arguments await async break case catch class const continue debugger default delete do else enum eval export extends false finally for function if implements import in instanceof interface let new null of package private protected public return static super switch this throw true try typeof var void while with yield undefined NaN Infinity Object Array String Number Boolean Math Date Symbol Map Set Error Function JSON Promise Proxy Reflect BigInt RegExp console window document globalThis'.split(' '));
  const ARITH = new Set(['+', '-', '*', '/', '%']);
  // abstract methods of well-known library interfaces: [name, arity]
  const LIB_ABSTRACT = { Comparable: [['compareTo', 1]], Runnable: [['run', 0]], Comparator: [['compare', 2]], Iterator: [['hasNext', 0], ['next', 0]], Iterable: [['iterator', 0]], Supplier: [['get', 0]], Function: [['apply', 1]], Consumer: [['accept', 1]], Predicate: [['test', 1]], Callable: [['call', 0]], BiFunction: [['apply', 2]], AutoCloseable: [['close', 0]], Closeable: [['close', 0]] };
  const CHECKED_BASES = new Set(['Exception', 'IOException', 'FileNotFoundException', 'InterruptedException', 'CloneNotSupportedException', 'TimeoutException', 'ReflectiveOperationException', 'Throwable']);

  let LIBL = null;
  const libL = () => { if (!LIBL) LIBL = RT.create({ tick: () => true }); return LIBL; };

  /* Return types of well-known library methods, used for type inference and diagnostics. */
  const STR_RET = { length: 'int', charAt: 'char', substring: 'String', indexOf: 'int', lastIndexOf: 'int', equals: 'boolean', equalsIgnoreCase: 'boolean', isEmpty: 'boolean', isBlank: 'boolean', contains: 'boolean', startsWith: 'boolean', endsWith: 'boolean', toUpperCase: 'String', toLowerCase: 'String', trim: 'String', strip: 'String', replace: 'String', replaceAll: 'String', replaceFirst: 'String', concat: 'String', repeat: 'String', intern: 'String', compareTo: 'int', compareToIgnoreCase: 'int', hashCode: 'int', toCharArray: 'char[]', split: 'String[]', matches: 'boolean', toString: 'String', formatted: 'String', codePointAt: 'int', stripLeading: 'String', stripTrailing: 'String' };
  const STATIC_RET = {
    'Math.sqrt': 'double', 'Math.pow': 'double', 'Math.floor': 'double', 'Math.ceil': 'double', 'Math.random': 'double', 'Math.sin': 'double', 'Math.cos': 'double', 'Math.tan': 'double', 'Math.log': 'double', 'Math.log10': 'double', 'Math.exp': 'double', 'Math.cbrt': 'double', 'Math.hypot': 'double', 'Math.atan': 'double', 'Math.atan2': 'double', 'Math.toRadians': 'double', 'Math.toDegrees': 'double', 'Math.signum': 'double', 'Math.rint': 'double',
    'Integer.parseInt': 'int', 'Integer.valueOf': 'Integer', 'Integer.compare': 'int', 'Integer.max': 'int', 'Integer.min': 'int', 'Integer.sum': 'int', 'Integer.toString': 'String', 'Integer.toBinaryString': 'String', 'Integer.toHexString': 'String', 'Integer.bitCount': 'int', 'Integer.signum': 'int',
    'Long.parseLong': 'long', 'Long.valueOf': 'Long', 'Double.parseDouble': 'double', 'Double.valueOf': 'Double', 'Double.compare': 'int', 'Double.toString': 'String', 'Double.isNaN': 'boolean', 'Boolean.parseBoolean': 'boolean', 'Float.parseFloat': 'float',
    'Character.isDigit': 'boolean', 'Character.isLetter': 'boolean', 'Character.isLetterOrDigit': 'boolean', 'Character.isUpperCase': 'boolean', 'Character.isLowerCase': 'boolean', 'Character.isWhitespace': 'boolean', 'Character.isAlphabetic': 'boolean', 'Character.getNumericValue': 'int', 'Character.toString': 'String',
    'String.valueOf': 'String', 'String.format': 'String', 'String.join': 'String', 'System.currentTimeMillis': 'long', 'System.nanoTime': 'long', 'System.lineSeparator': 'String', 'Objects.equals': 'boolean', 'Objects.hash': 'int', 'Arrays.toString': 'String', 'Arrays.deepToString': 'String', 'Arrays.equals': 'boolean',
  };
  const STATIC_FIELD = { 'Integer.MAX_VALUE': 'int', 'Integer.MIN_VALUE': 'int', 'Long.MAX_VALUE': 'long', 'Long.MIN_VALUE': 'long', 'Double.MAX_VALUE': 'double', 'Double.MIN_VALUE': 'double', 'Math.PI': 'double', 'Math.E': 'double', 'Short.MAX_VALUE': 'short', 'Byte.MAX_VALUE': 'byte', 'Double.POSITIVE_INFINITY': 'double', 'Double.NEGATIVE_INFINITY': 'double', 'Double.NaN': 'double', 'Character.MAX_VALUE': 'char' };
  const OBJ_RET = {
    Scanner: { nextInt: 'int', nextLine: 'String', next: 'String', nextDouble: 'double', nextLong: 'long', nextBoolean: 'boolean', hasNext: 'boolean', hasNextInt: 'boolean', hasNextLine: 'boolean', hasNextDouble: 'boolean' },
    StringBuilder: { length: 'int', toString: 'String', charAt: 'char', indexOf: 'int', isEmpty: 'boolean' }, StringBuffer: { length: 'int', toString: 'String', charAt: 'char' },
    Random: { nextInt: 'int', nextDouble: 'double', nextBoolean: 'boolean', nextLong: 'long' },
  };
  for (const c of ['List', 'ArrayList', 'LinkedList', 'Set', 'HashSet', 'TreeSet', 'LinkedHashSet', 'Map', 'HashMap', 'TreeMap', 'LinkedHashMap', 'Queue', 'Deque', 'ArrayDeque', 'Stack', 'PriorityQueue', 'Collection', 'Vector']) OBJ_RET[c] = { size: 'int', isEmpty: 'boolean', contains: 'boolean', containsKey: 'boolean', containsValue: 'boolean', indexOf: 'int', toString: 'String', hashCode: 'int' };

  const isNumT = t => !!t && (NUMP.has(t) || (BOX[t] && BOX[t] !== 'boolean'));
  const unbox = t => (t && BOX[t]) || t;
  const isIntLike = t => { t = unbox(t); return t === 'int' || t === 'short' || t === 'byte' || t === 'char'; };
  const binNum = (a, b) => { a = unbox(a); b = unbox(b); if (!NUMP.has(a) || !NUMP.has(b)) return null; if (a === 'double' || b === 'double') return 'double'; if (a === 'float' || b === 'float') return 'float'; if (a === 'long' || b === 'long') return 'long'; return 'int'; };
  const isRefT = t => !!t && !PRIM.has(t);
  const esc = s => JSON.stringify(s);
  const mangle = k => k.replace(/\[\]/g, 'A').replace(/[^\w]/g, '_') || 'v';

  /* ======================================================================== */
  class Gen {
    constructor(unit) {
      this.unit = unit; this.errs = []; this.classes = new Map(); this.all = []; this.consts = new Map(); this.constLines = [];
      this.anonN = 0; this.tmpN = 0; this.imports = unit.imports || []; this.localClassN = 0;
      this.sigs = new Map(); this.enumConsts = new Map(); this.freshN = 0;
    }
    err(line, msg) { if (!this.errs.some(e => e.line === line && e.msg === msg)) this.errs.push({ line, msg }); }

    /* ---------- constants hoisting ---------- */
    konst(key, expr) { let n = this.consts.get(key); if (!n) { n = '$k' + this.consts.size; this.consts.set(key, n); this.constLines.push(`const ${n} = ${expr};`); } return n; }

    /* ---------- class table ---------- */
    collect(decl, outer, opts = {}) {
      const name = decl.name;
      const js = opts.js || (outer && !opts.local ? outer.js + '$' + name : name);
      const info = {
        name, js, decl, kind: decl.kind, outer, fields: new Map(), methods: new Map(), ctors: [], tparams: new Set((decl.tparams || []).map(t => t.name)),
        isStatic: !outer || opts.local || opts.anon ? !(opts.local || opts.anon) : (decl.kind !== 'class' || decl.mods.has('static') || outer.kind === 'interface'),
        abstract: decl.mods.has('abstract') || decl.kind === 'interface', anon: !!opts.anon, local: !!opts.local, parent: null, parentLib: null, ifaces: [], libIfaces: [], nested: [], id: this.all.length,
        throwsCache: null, defaults: [],
      };
      info.inner = !!outer && !info.isStatic && !opts.local && !opts.anon;
      this.all.push(info);
      if (!opts.anon && !opts.local) {
        if (this.classes.has(name) && !outer) this.err(decl.ln, `duplicate class: ${name}`);
        if (!this.classes.has(name) || !outer) this.classes.set(name, info);
        else if (!this.classes.has(name)) this.classes.set(name, info);
      }
      if (outer) outer.nested.push(info);
      // members
      const members = opts.members || decl.members;
      for (const m of members) {
        if (m.k === 'field') {
          for (const d of m.decls) {
            const ft = { ...m.type, dims: m.type.dims + d.dims };
            if (info.fields.has(d.name)) this.err(d.ln, `variable ${d.name} is already defined in class ${name}`);
            info.fields.set(d.name, { name: d.name, tnode: ft, static: m.mods.has('static') || decl.kind === 'interface', final: m.mods.has('final') || decl.kind === 'interface', init: d.init, mods: m.mods, owner: info, ln: d.ln });
          }
        } else if (m.k === 'method') {
          const mi = { name: m.name, decl: m, params: m.params || [], static: m.mods.has('static'), abstract: !m.body, mods: m.mods, owner: info, ln: m.ln, ret: m.ret, throws: m.throws || [] };
          if (m.isCtor) info.ctors.push(mi);
          else { if (!info.methods.has(m.name)) info.methods.set(m.name, []); info.methods.get(m.name).push(mi); }
        } else if (m.k === 'class') this.collect(m, info, {});
      }
      if (decl.kind === 'record') {
        for (const c of decl.recordComps) info.fields.set(c.name, { name: c.name, tnode: c.type, static: false, final: true, init: null, mods: new Set(['private', 'final']), owner: info, ln: decl.ln, comp: true });
      }
      if (decl.kind === 'enum') for (const c of decl.enumConsts) { this.enumConsts.set(c.name, info); info.fields.set(c.name, { name: c.name, tnode: { n: name, args: [], dims: 0 }, static: true, final: true, enumConst: true, owner: info, mods: new Set(['public', 'static', 'final']), ln: c.ln }); }
      return info;
    }
    findClass(name, from) {
      if (!name) return null;
      if (name.includes('.')) {
        const parts = name.split('.'); let c = this.classes.get(parts[0]);
        if (!c) { if (/^[a-z]/.test(parts[0])) return this.findClass(parts[parts.length - 1], from); return null; }
        for (let i = 1; i < parts.length && c; i++) c = c.nested.find(n => n.name === parts[i]) || null;
        return c;
      }
      if (this.anonInfos && this.anonInfos.has(name)) return this.anonInfos.get(name);
      // local / anonymous scopes are consulted first
      for (let s = this.scope; s; s = s.parent) if (s.classes && s.classes.has(name)) return s.classes.get(name);
      // nested classes visible from the current class chain
      for (let c = from || this.cur; c; c = c.outer) {
        if (c.name === name && !c.anon) return c;
        const n = c.nested.find(x => x.name === name); if (n) return n;
        for (let p = c.parent; p; p = p.parent) { const n2 = p.nested.find(x => x.name === name); if (n2) return n2; }
      }
      return this.classes.get(name) || null;
    }
    resolveSupers(info) {
      const d = info.decl;
      const prev = this.cur; this.cur = info.outer || info;
      const resolve = (t) => {
        const c = this.findClass(t.n, info.outer || info);
        if (c) return { info: c };
        const lib = this.libName(t.n);
        if (lib) return { lib };
        this.err(d.ln, `cannot find symbol: class ${t.n}`);
        return null;
      };
      if (d.ext) { const r = resolve(d.ext); if (r) { if (r.info) { info.parent = r.info; if (r.info.kind === 'interface') this.err(d.ln, 'no interface expected here'); else if (r.info.decl.mods.has('final')) this.err(d.ln, `cannot inherit from final ${r.info.name}`); } else { info.parentLib = r.lib; } } }
      if (d.kind === 'enum') info.parentLib = 'Enum'; else if (d.kind === 'record') info.parentLib = 'Record';
      for (const t of d.impl) { const r = resolve(t); if (!r) continue; if (r.info) { info.ifaces.push(r.info); if (r.info.kind !== 'interface' && d.kind !== 'interface') this.err(d.ln, 'interface expected here'); } else info.libIfaces.push(r.lib); }
      this.cur = prev;
    }
    libName(n) {
      // returns library class simple name if it exists AND is importable; null otherwise (errors reported by caller)
      const L = libL().L;
      if (n.includes('.')) { const parts = n.split('.'); if (/^[a-z]/.test(parts[0])) return L[parts[parts.length - 1]] ? parts[parts.length - 1] : null; if (L[n]) return n; return null; }
      const c = L[n]; if (!c) return null;
      if (!this.importable(c, n)) return null;
      return n;
    }
    importable(c, n) {
      const fq = c.$jfq || ''; if (!fq || /^java\.lang\.[A-Z]/.test(fq) || fq.indexOf('$') >= 0) return true;
      if (/^java\.lang\.[a-z]/.test(fq)) return true;
      const pkg = fq.replace(/\.[^.]+$/, '');
      return this.imports.some(i => i === fq || i === pkg + '.*' || i === fq.replace(/\.[^.]+$/, '') + '.*' || i === fq.split('$')[0] || (i.endsWith('.*') && fq.startsWith(i.slice(0, -1)) && fq.slice(i.length - 1).indexOf('.') < 0) || (i.endsWith('.' + n)));
    }
    isSub(a, b) { // user class a is subtype of user class b
      if (a === b) return true;
      if (a.parent && this.isSub(a.parent, b)) return true;
      return a.ifaces.some(i => this.isSub(i, b));
    }
    superChain(info) { const out = []; for (let c = info; c; c = c.parent) out.push(c); return out; }
    allIfaces(info, acc = new Set()) { for (let c = info; c; c = c.parent) c.ifaces.forEach(i => { if (!acc.has(i)) { acc.add(i); this.allIfaces(i, acc); } }); return acc; }
    libSuper(info) { for (let c = info; c; c = c.parent) if (c.parentLib) return c.parentLib; return null; }
    isThrowable(info) { for (let c = info; c; c = c.parent) if (c.parentLib && libL().L[c.parentLib] && (libL().L[c.parentLib].prototype instanceof libL().L.Throwable || libL().L[c.parentLib] === libL().L.Throwable)) return true; return false; }
    isUncheckedLib(n) { const L = libL().L; const c = L[n]; return !!c && (c === L.RuntimeException || c.prototype instanceof L.RuntimeException || c === L.Error || c.prototype instanceof L.Error); }
    isChecked(typeName) {
      const info = this.findClass(typeName);
      if (info) { const lib = this.libSuper(info); return lib ? !this.isUncheckedLib(lib) : false; }
      const L = libL().L; const c = L[typeName]; if (!c || !(c === L.Throwable || c.prototype instanceof L.Throwable)) return false;
      return !this.isUncheckedLib(typeName);
    }

    /* ---------- types ---------- */
    T(t) {
      if (!t) return null;
      let n = t.n;
      if (n === 'var') return null;
      if (this.tparamsIn(n)) return null;
      if (n.includes('.')) { const last = n.split('.').pop(); if (this.findClass(n)) n = this.findClass(n).name; else if (/^[a-z]/.test(n)) n = last; }
      return n + '[]'.repeat(t.dims || 0);
    }
    tparamsIn(n) { for (let c = this.cur; c; c = c.outer) if (c.tparams.has(n)) return true; return !!(this.mtparams && this.mtparams.has(n)); }
    elemOf(t) { return t && t.endsWith('[]') ? t.slice(0, -2) : null; }
    /* Check that a declared type exists. */
    checkType(t, line) {
      if (!t) return;
      for (const a of t.args || []) if (PRIM.has(a.n) && !a.dims) this.err(line, 'unexpected type');
      const n = t.n;
      if (n === '?') return;
      if (PRIM.has(n) || n === 'var' || n === 'void') { (t.args || []).forEach(a => this.checkType(a, line)); return; }
      if (this.tparamsIn(n)) return;
      if (n === 'Object' || n === 'String') return;
      if (this.findClass(n)) { (t.args || []).forEach(a => this.checkType(a, line)); return; }
      const L = libL().L;
      let base = n; if (n.includes('.')) { const parts = n.split('.'); if (/^[a-z]/.test(parts[0])) base = parts[parts.length - 1]; else base = parts[0] === 'Map' || parts[0] === 'AbstractMap' ? n : parts[0]; }
      if (!(L[base] || L[n])) { this.err(line, `cannot find symbol: class ${n.split('.')[0]}`); return; }
      const c = L[base] || L[n];
      const top = L[n.split('.')[0]] || c;
      if (!/^[a-z]/.test(n) && !this.importable(top, n.split('.')[0])) this.err(line, `cannot find symbol: class ${n.split('.')[0]}`);
      (t.args || []).forEach(a => this.checkType(a, line));
    }

    /* ---------- signature groups (for overloading) ---------- */
    sigKey(params, tp) { return params.map(p => { let n = p.type.n; if (tp && tp.has(n)) n = 'Object'; return n + '[]'.repeat(p.type.dims) + (p.varargs ? '...' : ''); }).join(','); }
    scanSigs(info) {
      for (const [name, ms] of info.methods) for (const m of ms) {
        if (m.abstract) continue;
        const tp = new Set([...info.tparams, ...(m.decl.tparams || []).map(t => t.name)]);
        for (let c = info.outer; c; c = c.outer) c.tparams.forEach(x => tp.add(x));
        m.sig = this.sigKey(m.params, tp);
        if (!this.sigs.has(name)) this.sigs.set(name, new Set());
        this.sigs.get(name).add(m.sig);
      }
      for (const ms of info.methods.values()) for (const m of ms) if (m.abstract && m.sig === undefined) { const tp = new Set([...info.tparams, ...(m.decl.tparams || []).map(t => t.name)]); m.sig = this.sigKey(m.params, tp); }
    }
    overloaded(name) { const s = this.sigs.get(name); return !!s && s.size > 1; }
    jsMethod(m) { return this.overloaded(m.name) ? m.name + '$' + mangle(m.sig) : m.name; }

    /* ---------- AST pre-scan for anonymous / local classes and lambdas (so their methods count in overload groups) ---------- */
    prescan(node, owner) {
      if (!node || typeof node !== 'object') return;
      if (Array.isArray(node)) { node.forEach(n => this.prescan(n, owner)); return; }
      if (node.k === 'new' && node.body) {
        const sigs = new Map();
        for (const m of node.body) if (m.k === 'method' && m.body) { const k = this.sigKey(m.params || [], null); if (!this.sigs.has(m.name)) this.sigs.set(m.name, new Set()); this.sigs.get(m.name).add(k); }
      }
      if (node.k === 'localclass') {
        for (const m of node.decl.members) if (m.k === 'method' && m.body) { const k = this.sigKey(m.params || [], new Set((node.decl.tparams || []).map(t => t.name))); if (!this.sigs.has(m.name)) this.sigs.set(m.name, new Set()); this.sigs.get(m.name).add(k); }
      }
      if (node.k === 'class') for (const c of node.enumConsts || []) if (c.body) for (const m of c.body) if (m.k === 'method' && m.body) { const k = this.sigKey(m.params || [], null); if (!this.sigs.has(m.name)) this.sigs.set(m.name, new Set()); this.sigs.get(m.name).add(k); }
      for (const key of Object.keys(node)) { if (key === 'ln' || key === 'type' || key === 'mods') continue; const v = node[key]; if (v && typeof v === 'object') this.prescan(v, owner); }
    }

    /* ======================================================================
       Program generation
       ====================================================================== */
    generate() {
      const unit = this.unit;
      for (const d of unit.types) this.collect(d, null);
      const tops = this.all.slice();
      tops.forEach(i => this.resolveSupers(i));
      // cyclic inheritance: depth-first search over extends/implements edges; report and cut the back edge
      {
        const state = new Map();
        const visit = c => {
          state.set(c, 1);
          if (c.parent) { if (state.get(c.parent) === 1) { this.err(c.parent.decl.ln, `cyclic inheritance involving ${c.parent.name}`); c.parent = null; } else if (!state.get(c.parent)) visit(c.parent); }
          c.ifaces = c.ifaces.filter(i => { if (state.get(i) === 1) { this.err(i.decl.ln, `cyclic inheritance involving ${i.name}`); return false; } if (!state.get(i)) visit(i); return true; });
          state.set(c, 2);
        };
        this.all.forEach(c => { if (!state.get(c)) visit(c); });
      }
      this.hiddenFields = new Set();
      for (const c of this.all) for (let a = c.parent; a; a = a.parent) for (const [n, f] of c.fields) if (!f.static && a.fields.has(n) && !a.fields.get(n).static) this.hiddenFields.add(n);
      this.all.slice().forEach(i => this.scanSigs(i));
      this.prescan(unit.types, null);
      const mainInfo = this.findMain();
      const defs = [], posts = [];
      const done = new Set();
      const emit = info => {
        if (done.has(info)) return; done.add(info);
        if (info.parent) emit(info.parent);
        for (const i of info.ifaces) emit(i);
        const r = this.genClass(info, false);
        defs.push(r.def); posts.push(r.post);
      };
      for (const i of this.all.slice()) { if (!i.anon && !i.local) emit(i); }
      // semantic checks that need whole-program knowledge
      for (const i of this.all) if (!i.anon && !i.local) { this.checkAbstractImpl(i); this.checkOverrides(i); }
      if (!mainInfo && !this.errs.length) this.err(1, 'no main method found: add public static void main(String[] args)');
      if (this.errs.length) { this.errs.sort((a, b) => a.line - b.line); throw { errors: this.errs }; }
      const lines = [];
      lines.push("'use strict';");
      lines.push(`const {${HELPERS.join(',')}} = $rt;`);
      lines.push('const $L = $rt.L, $JObject = $rt.L.Object;');
      lines.push(...this.constLines);
      lines.push(...defs, ...posts);
      lines.push(`return ${mainInfo ? mainInfo.js : 'null'};`);
      return { js: lines.join('\n'), main: mainInfo };
    }
    findMain() {
      const has = i => (i.methods.get('main') || []).some(m => m.static && m.params.length <= 1);
      const top = this.all.filter(i => !i.anon && !i.local && has(i));
      return top.find(i => i.name === 'Main') || top[0] || null;
    }
    checkAbstractImpl(info) {
      if (info.abstract || info.kind === 'interface') return;
      if (info.kind === 'enum' && info.decl.enumConsts.some(c => c.body)) return;
      const missing = this.missingAbstract(info);
      if (missing) this.err(info.decl.ln, `${info.name} is not abstract and does not override abstract method ${missing.name}(${missing.params}) in ${missing.owner}`);
    }
    /* javac's override rules: access, return type, static/instance, throws, final, @Override, interface bodies */
    checkOverrides(info) {
      const rank = m => (m.mods.has('public') || m.owner.kind === 'interface' ? 3 : m.mods.has('protected') ? 2 : m.mods.has('private') ? 0 : 1);
      const accName = r => ['private', 'package-private', 'protected', 'public'][r] || 'package-private';
      const sameSig = (a, b) => { const x = this.ptypes(a), y = this.ptypes(b); return x.length === y.length && x.every((t, i) => t === y[i] || t === 'Object' || y[i] === 'Object' || t === null || y[i] === null); };
      const supers = []; for (let c = info.parent; c; c = c.parent) supers.push(c); for (const i of this.allIfaces(info)) supers.push(i);
      for (const [name, ms] of info.methods) for (const m of ms) {
        if (m.static && info.kind === 'interface') continue;
        const hasOverride = m.mods.has('@Override');
        if (info.kind === 'interface' && m.decl.body && !m.mods.has('default') && !m.mods.has('static') && !m.mods.has('private')) this.err(m.ln, 'interface abstract methods cannot have body');
        let found = null;
        for (const sc of supers) { for (const sm of sc.methods.get(name) || []) { if (sm.mods.has('private') || sm.params.length !== m.params.length) continue; if (!sameSig(m, sm)) continue; found = sm; this.checkOneOverride(info, m, sm, rank, accName); } }
        if (hasOverride && !found && !m.static) {
          const objM = (name === 'toString' && !m.params.length) || (name === 'hashCode' && !m.params.length) || (name === 'equals' && m.params.length === 1) || name === 'clone' || name === 'finalize';
          const rec = info.kind === 'record' && !m.params.length && info.fields.has(name);
          if (!objM && !rec && !this.hasLibParent(info)) this.err(m.decl.startLn || m.ln, 'method does not override or implement a method from a supertype');
        } else if (hasOverride && m.static) this.err(m.decl.startLn || m.ln, 'method does not override or implement a method from a supertype');
      }
    }
    checkOneOverride(info, m, sm, rank, accName) {
      const sig = x => `${x.name}(${x.params.map(p => this.T(p.type)).join(',')})`;
      const kind = sm.owner.kind === 'interface' && info.kind !== 'interface' ? 'implement' : 'override';
      const head = `${sig(m)} in ${info.name} cannot ${m.static && !sm.static ? 'hide' : kind} ${sig(sm)} in ${sm.owner.name}`;
      if (sm.static !== m.static) { this.err(m.ln, head + (sm.static ? '; overridden method is static' : '; overriding method is static')); return; }
      if (m.static) return;
      if (sm.mods.has('final')) { this.err(m.ln, head + '; overridden method is final'); return; }
      if (rank(m) < rank(sm)) { this.err(m.ln, head + `; attempting to assign weaker access privileges; was ${accName(rank(sm))}`); return; }
      const rt = this.retType(m), st = this.retType(sm);
      if (rt && st && rt !== st) {
        const ok = !PRIM.has(rt) && !PRIM.has(st) && rt !== 'void' && st !== 'void' && (st === 'Object' || (this.findClass(rt) && this.findClass(st) && this.isSub(this.findClass(rt), this.findClass(st))));
        if (!ok) { this.err(m.ln, head + `; return type ${rt} is not compatible with ${st}`); return; }
      }
      for (const t of m.throws) if (this.isChecked(t.n) && !sm.throws.some(x => this.exSub(t.n, x.n))) { this.err(m.ln, head + `; overridden method does not throw ${t.n}`); return; }
    }
    missingAbstract(info) {
      // concrete implementations visible through the class chain + default methods of interfaces
      const concrete = (name, arity) => {
        for (let c = info; c; c = c.parent) { if ((c.methods.get(name) || []).some(m => !m.abstract && m.params.length === arity)) return true; if (c.kind === 'record' && arity === 0 && c.fields.has(name) && c.fields.get(name).comp) return true; }
        for (const i of this.allIfaces(info)) if ((i.methods.get(name) || []).some(m => !m.abstract && m.params.length === arity)) return true;
        return false;
      };
      const libSuper = this.libSuper(info);
      for (let c = info; c; c = c.parent) {
        for (const ms of c.methods.values()) for (const m of ms) if (m.abstract && (c.kind === 'class' || c.kind === 'enum') && !concrete(m.name, m.params.length)) return { name: m.name, params: m.params.map(p => this.T(p.type)).join(','), owner: c.name };
      }
      for (const i of this.allIfaces(info)) for (const ms of i.methods.values()) for (const m of ms) if (m.abstract && !m.static && !concrete(m.name, m.params.length)) { if (this.isObjectMethod(m)) continue; return { name: m.name, params: m.params.map(p => this.T(p.type)).join(','), owner: i.name }; }
      // library interfaces
      const libIfs = []; for (let c = info; c; c = c.parent) libIfs.push(...c.libIfaces);
      for (const i of this.allIfaces(info)) libIfs.push(...i.libIfaces);
      for (const li of libIfs) for (const [n, a] of LIB_ABSTRACT[li] || []) {
        if (concrete(n, a)) continue;
        if (libSuper && libL().L[libSuper] && libL().L[libSuper].prototype[n]) continue;
        const pt = { compareTo: 'T', compare: 'T,T', accept: 'T', test: 'T', apply: a === 2 ? 'T,U' : 'T' }[n] || '';
        return { name: n, params: pt, owner: li };
      }
      return null;
    }
    isObjectMethod(m) { return (m.name === 'equals' && m.params.length === 1) || (m.name === 'hashCode' && !m.params.length) || (m.name === 'toString' && !m.params.length); }

    /* ======================================================================
       Class generation
       ====================================================================== */
    parentRef(info) {
      if (info.parent) return info.parent.js;
      if (info.parentLib) return `$L[${esc(info.parentLib)}]`;
      if (info.kind === 'enum') return '$L.Enum';
      if (info.kind === 'record') return '$L.Record';
      return '$JObject';
    }
    ifaceRefs(info) { return [...info.ifaces.map(i => i.js), ...info.libIfaces.map(n => `$L[${esc(n)}]`)]; }
    needsInit(info) {
      if (info._needsInit !== undefined) return info._needsInit;
      let need = info.kind === 'enum' || info.decl.members.some(m => m.k === 'init' && m.static);
      for (const f of info.fields.values()) if (f.static && f.init && !f.enumConst && !this.isConstField(f)) need = true;
      if (info.parent && this.needsInit(info.parent)) need = true;
      return info._needsInit = need;
    }
    isLitInit(e) { return e.k === 'lit' || (e.k === 'unary' && e.op === '-' && e.e.k === 'lit') || (e.k === 'lit' && e.neg); }
    defaultOf(t) {
      switch (t) { case 'int': case 'short': case 'byte': return '0'; case 'long': return '0n'; case 'boolean': return 'false'; case 'double': return this.konst('d0', '$D(0)'); case 'float': return this.konst('f0', '$F(0)'); case 'char': return this.konst('c0', '$Ch(0)'); default: return 'null'; }
    }
    /* Generates `class X extends P {...}` plus post-definition statements. `expr` => class expression for anonymous classes. */
    genClass(info, asExpr, anonArgs) {
      const prevCur = this.cur, prevScope = this.scope, prevCtx = this.ctx, prevMt = this.mtparams;
      this.cur = info; this.mtparams = null;
      if (info.local || info.anon) { /* keep lexical scope */ } else this.scope = null;
      const d = info.decl, body = [], post = [];
      const isIface = info.kind === 'interface';
      // validate declared types of fields / method signatures
      for (const f of info.fields.values()) if (!f.comp && !f.enumConst) this.checkType(f.tnode, f.ln);
      // constructor: field defaults
      const inst = [...info.fields.values()].filter(f => !f.static);
      if (!isIface) {
        this.ctx = this.newCtx(info, false, null);
        const defs = inst.map(f => `this.${this.fj(f)} = ${this.isConstField(f) ? this.initExpr(f.init, this.T(f.tnode), f.ln) : this.defaultOf(this.T(f.tnode))};`).join(' ');
        body.push(`constructor() { super(); ${defs} }`);
      }
      // instance initializers
      const fiParts = [];
      const fiName = '$fi$' + info.js;
      if (!isIface) {
        this.ctx = this.newCtx(info, false, null);
        for (const m of d.members) {
          if (m.k === 'field' && !m.mods.has('static')) for (const dc of m.decls) if (dc.init) {
            const f = info.fields.get(dc.name);
            const ft = this.T(f.tnode);
            const e = this.initExpr(dc.init, ft, dc.ln);
            fiParts.push(`this.${this.fj(f)} = ${e};`);
          } else if (false) { /* none */ }
          if (m.k === 'init' && !m.static) fiParts.push(this.genBlockBody(m.body));
        }
        if (this.ctx.tmps.length) fiParts.unshift(`let ${this.ctx.tmps.join(',')};`);
        if (this.ctx.captured) fiParts.unshift(`const $o${info.id} = this;`);
        if (info.kind === 'record') { /* assigned by canonical ctor */ }
        if (fiParts.length) body.push(`${fiName}() { ${fiParts.join(' ')} }`);
      }
      const hasFi = fiParts.length > 0;
      // constructors
      if (!isIface) body.push(...this.genCtors(info, hasFi ? fiName : null, anonArgs));
      // methods
      const statics = [], defaultsBody = [];
      const ovl = new Map();
      for (const [name, ms] of info.methods) {
        for (const m of ms) {
          if (m.abstract) { if (!(info.kind === 'interface' || m.mods.has('abstract') || m.mods.has('native'))) this.err(m.ln, 'missing method body, or declare abstract'); continue; }
          if (m.mods.has('abstract') && m.decl.body) this.err(m.ln, 'abstract methods cannot have a body');
          const code = this.genMethod(info, m);
          const jn = this.jsMethod(m);
          if (isIface && !m.static) defaultsBody.push(`${jn}${code}`);
          else body.push(`${m.static ? 'static ' : ''}${jn}${code}`);
          if (this.overloaded(name)) { if (!ovl.has(name)) ovl.set(name, []); ovl.get(name).push(m); }
        }
        // abstract-method checks on classes
      }
      if (!isIface && !info.abstract && !info.anon) { /* abstract methods declared in a concrete class */
        for (const ms of info.methods.values()) for (const m of ms) if (m.abstract && m.mods.has('abstract') && info.kind === 'class') this.err(info.decl.ln, `${info.name} is not abstract and does not override abstract method ${m.name}(${m.params.map(p => this.T(p.type)).join(',')}) in ${info.name}`);
      }
      // dispatchers for overloaded names
      const regI = [], regS = [];
      for (const [name, ms] of ovl) {
        const inst = ms.filter(m => !m.static), st = ms.filter(m => m.static);
        const entry = m => `[${esc(this.jsMethod(m))}, ${JSON.stringify(m.params.map(p => (this.tparamsFor(info, m).has(p.type.n) ? 'Object' : p.type.n) + '[]'.repeat(p.type.dims)))}, ${m.params.some(p => p.varargs)}]`;
        if (inst.length) { if (isIface) defaultsBody.push(`${name}(...a) { return $ovlm(this, ${esc(name)}, a, false); }`); else body.push(`${name}(...a) { return $ovlm(this, ${esc(name)}, a, false); }`); regI.push(`${esc(name)}: [${inst.map(entry).join(', ')}]`); }
        if (st.length) { body.push(`static ${name}(...a) { return $ovlm(${info.js}, ${esc(name)}, a, true); }`); regS.push(`${esc(name)}: [${st.map(entry).join(', ')}]`); }
      }
      // class text
      const ext = isIface ? '$JObject' : this.parentRef(info);
      let clsName = info.js;
      let text;
      if (asExpr) text = `(class ${/^[A-Za-z_$][\w$]*$/.test(info.js) ? info.js : ''} extends ${ext} {\n${body.join('\n')}\n})`;
      else text = `class ${clsName} extends ${ext} {\n${body.join('\n')}\n}`;
      const ref = asExpr ? '$C' : clsName;
      const ps = [];
      if (regI.length) ps.push(`${ref}.$ms = {${regI.join(', ')}};`);
      if (regS.length) ps.push(`${ref}.$mss = {${regS.join(', ')}};`);
      if (defaultsBody.length) ps.push(`${ref}.$defaults = {${defaultsBody.join(',\n')}};`);
      const binOf = c => c.anon ? c.name : c.local ? `${(() => { let t = c; while (t.outer) t = t.outer; return t.name; })()}$1${c.name}` : c.outer ? `${binOf(c.outer)}$${c.name}` : c.name;
      const canonOf = c => (c.outer && !c.anon && !c.local ? canonOf(c.outer) + '.' : '') + c.name;
      ps.push(`$defc(${ref}, ${esc(info.anon ? '' : info.name)}, [${this.ifaceRefs(info).join(', ')}], {iface: ${isIface}, bin: ${esc(binOf(info))}, canon: ${esc(canonOf(info))}});`);
      if (info._ctorsReg) ps.push(`${ref}.$ctors = ${JSON.stringify(info._ctorsReg)};`);
      // static members
      const sinit = [];
      this.ctx = this.newCtx(info, true, null); this.ctx.isClinit = true;
      const isEnum = info.kind === 'enum';
      if (isEnum) {
        const consts = d.enumConsts;
        consts.forEach((c, idx) => {
          let args = c.args.map(a => this.expr(a).c);
          let cls = ref;
          if (c.body) {
            const sub = this.collect({ ...d, k: 'class', kind: 'class', name: info.name, mods: new Set(), members: [], enumConsts: [], tparams: [], impl: [], ext: null }, info, { anon: true, js: info.js + '$' + c.name, members: c.body });
            sub.parent = info; sub.decl = { ...sub.decl, members: c.body }; sub.isStatic = true; sub.enumBody = true; sub.name = info.name;
            this.scanSigs(sub);
            const r = this.genClass(sub, true, true);
            cls = `(() => { const $C = ${r.def}; ${r.post} return $C; })()`;
          }
          sinit.push(`${ref}.$${c.name} = $ec(new (${cls})(), ${esc(c.name)}, ${idx}).$init(${args.join(', ')});`);
        });
        sinit.push(`$enumInit(${ref}, [${consts.map(c => `${ref}.$${c.name}`).join(', ')}]);`);
      }
      for (const m of d.members) {
        if (m.k === 'field' && (m.mods.has('static') || isIface)) for (const dc of m.decls) {
          const f = info.fields.get(dc.name), ft = this.T(f.tnode);
          if (dc.init) {
            if (this.isConstField(f) && !isEnum) { ps.push(`${ref}.$${dc.name} = ${this.initExpr(dc.init, ft, dc.ln)};`); }
            else { ps.push(`${ref}.$${dc.name} = ${this.defaultOf(ft)};`); sinit.push(`${ref}.$${dc.name} = ${this.initExpr(dc.init, ft, dc.ln)};`); }
          } else ps.push(`${ref}.$${dc.name} = ${this.defaultOf(ft)};`);
        } else if (m.k === 'init' && m.static) sinit.push(this.genBlockBody(m.body));
      }
      if (info.kind === 'record') {
        const comps = d.recordComps;
        for (const c of comps) if (![...(info.methods.get(c.name) || [])].some(m => m.params.length === 0)) post.push(`${ref}.prototype.${c.name} = function() { return this.$${c.name}; };`);
        if (!(info.methods.get('toString') || []).some(m => !m.params.length)) post.push(`${ref}.prototype.toString = function() { return ${esc(info.name)} + '[' + ${comps.length ? comps.map(c => `${esc(c.name + '=')} + $str(this.$${c.name})`).join(" + ', ' + ") : "''"} + ']'; };`);
        if (!(info.methods.get('equals') || []).some(m => m.params.length === 1)) post.push(`${ref}.prototype.equals = function(o) { return o instanceof ${ref} && ${comps.length ? comps.map(c => `$equals(this.$${c.name}, o.$${c.name})`).join(' && ') : 'true'}; };`);
        if (!(info.methods.get('hashCode') || []).some(m => !m.params.length)) post.push(`${ref}.prototype.hashCode = function() { let h = 0; ${comps.map(c => `h = (Math.imul(31, h) + $hash(this.$${c.name})) | 0;`).join(' ')} return h; };`);
      }
      if (sinit.length || this.needsInit(info)) {
        const pinit = info.parent && this.needsInit(info.parent) ? `$g(${info.parent.js}); ` : '';
        const tm = this.ctx.tmps.length ? `let ${this.ctx.tmps.join(',')}; ` : '';
        ps.push(`${ref}.$si = function() { ${ref}.$d = true; ${pinit}${tm}${sinit.join('\n')} return ${ref}; };`);
      }
      this.cur = prevCur; this.scope = prevScope; this.ctx = prevCtx; this.mtparams = prevMt;
      return { def: text, post: ps.concat(post).join('\n') };
    }
    tparamsFor(info, m) { const s = new Set(info.tparams); (m.decl.tparams || []).forEach(t => s.add(t.name)); for (let c = info.outer; c; c = c.outer) c.tparams.forEach(x => s.add(x)); return s; }
    newCtx(cls, isStatic, method) { return { cls, isStatic, method, tmps: [], ret: null, inLambda: 0, labels: [], captured: false, parentCtx: this.ctx || null, loops: 0, switches: 0 }; }
    tmp() { const n = '$t' + (this.tmpN++); this.ctx.tmps.push(n); return n; }
    initExpr(e, t, line) {
      if (e.k === 'arrlit') return this.arrayLit(e, t, line);
      const r = this.expr(e);
      return this.convert(r, t, line, e);
    }
    genBlockBody(blk) {
      const prevScope = this.scope; this.scope = this.pushScope();
      const s = blk.body.map(st => this.stmt(st)).join('\n');
      this.scope = prevScope;
      return `{ ${s} }`;
    }

    /* ---------- constructors ---------- */
    genCtors(info, fiName, anonArgs) {
      const out = [];
      const ctors = info.ctors;
      const prefix = '$init';
      if (info.anon && !info.enumBody) {
        // anonymous classes forward constructor arguments to the superclass
        const sup = info.parent ? `${info.parent.js}.prototype.$init.call(this, ...a);` : info.parentLib ? `$L[${esc(info.parentLib)}].prototype.$init.call(this, ...a);` : '';
        out.push(`$init(...a) { ${sup} ${fiName ? `this.${fiName}();` : ''} return this; }`);
        return out;
      }
      if (info.enumBody) { out.push(`$init(...a) { ${info.parent.js}.prototype.$init.call(this, ...a); ${fiName ? `this.${fiName}();` : ''} return this; }`); return out; }
      if (info.kind === 'record') {
        const comps = info.decl.recordComps;
        const canon = ctors.find(c => !c.decl.compact && c.params.length === comps.length);
        const compact = ctors.find(c => c.decl.compact);
        if (!canon) {
          const params = comps.map(c => c.name).join(', ');
          let pre = '';
          if (compact) {
            this.ctx = this.newCtx(info, false, compact); this.scope = this.pushScope();
            comps.forEach(c => this.declare(c.name, { t: this.T(c.type), js: this.jv(c.name) }, info.decl.ln));
            const b = compact.decl.body.body.map(s => this.stmt(s)).join('\n');
            pre = (this.ctx.tmps.length ? `let ${this.ctx.tmps.join(',')};` : '') + b; this.scope = this.scope.parent;
          }
          const co = comps.map(c => { const t = this.T(c.type); const cv = this.coerceCode(this.jv(c.name), null, t); return cv === this.jv(c.name) ? '' : `${this.jv(c.name)} = ${cv};`; }).join(' ');
          const assign = comps.map(c => `this.$${c.name} = ${this.jv(c.name)};`).join(' ');
          const others = ctors.filter(c => c !== compact);
          const own = `$init$canon(${comps.map(c => this.jv(c.name)).join(', ')}) { ${co} ${pre} ${assign} ${fiName ? `this.${fiName}();` : ''} return this; }`;
          if (!others.length) { out.push(own.replace('$init$canon', '$init')); return out; }
          out.push(own);
          out.push(...this.ctorList(info, others, fiName, true));
          out.push(`$init(...a) { return $ovlc(${info.js}, this, a); }`);
          out.push(`static $ctors_${info.js}() {}`);
          post_ctors: { /* registry emitted by caller through $ctors */ }
          info._ctorsReg = [['$init$canon', comps.map(c => this.T(c.type))], ...others.map(c => ['$init$' + mangle(this.sigKey(c.params, info.tparams)), c.params.map(p => this.T(p.type))])];
          return out;
        }
      }
      if (!ctors.length) {
        let sup = '';
        if (info.parent) {
          const pc = info.parent.ctors;
          if (pc.length && !pc.some(c => c.params.length === 0 || c.params.every(p => p.varargs)) && !info.parent.anon) this.err(info.decl.ln, `constructor ${info.parent.name} in class ${info.parent.name} cannot be applied to given types;`);
          sup = `${info.parent.js}.prototype.$init.call(this);`;
        } else if (info.parentLib) sup = `$L[${esc(info.parentLib)}].prototype.$init.call(this);`;
        out.push(`$init() { ${sup} ${fiName ? `this.${fiName}();` : ''} return this; }`);
        return out;
      }
      if (ctors.length === 1) {
        out.push(this.genCtor(info, ctors[0], fiName, '$init'));
        return out;
      }
      out.push(...this.ctorList(info, ctors, fiName, false));
      out.push(`$init(...a) { return $ovlc(${info.js}, this, a); }`);
      return out;
    }
    ctorList(info, ctors, fiName) {
      const out = [];
      info._ctorsReg = info._ctorsReg || [];
      ctors.forEach(c => {
        const k = this.sigKey(c.params, info.tparams);
        // duplicate constructors
        const nm = '$init$' + mangle(k);
        if (out.some(o => o.startsWith(nm + '('))) this.err(c.ln, `constructor ${info.name}(${k}) is already defined in class ${info.name}`);
        out.push(this.genCtor(info, c, fiName, nm));
        if (!info._ctorsReg.some(r => r[0] === nm)) info._ctorsReg.push([nm, c.params.map(p => (info.tparams.has(p.type.n) ? 'Object' : p.type.n) + '[]'.repeat(p.type.dims)), c.params.some(p => p.varargs)]);
      });
      return out;
    }
    genCtor(info, c, fiName, jsName) {
      const prevScope = this.scope, prevCtx = this.ctx, prevMt = this.mtparams;
      this.ctx = this.newCtx(info, false, c); this.ctx.isCtor = true; this.ctx.ret = 'void';
      this.mtparams = new Set((c.decl.tparams || []).map(t => t.name));
      this.scope = this.pushScope(true);
      const pro = this.params(c.params, c.ln);
      const stmts = c.decl.body.body.slice();
      let first = stmts[0], explicit = null;
      if (first && first.k === 'expr' && first.e.k === 'ctorcall') { explicit = first.e; stmts.shift(); }
      let head = '';
      if (explicit) {
        const args = explicit.args.map(a => this.expr(a));
        if (explicit.which === 'this') {
          head = `${info.js}.prototype.$init.call(this${args.map(a => ', ' + a.c).join('')});`;
          this.checkCtorCall(info, args, explicit.ln);
        } else {
          head = this.superInit(info, args, explicit.ln);
          head += fiName ? ` this.${fiName}();` : '';
        }
      } else {
        head = this.superInit(info, [], c.ln, true);
        head += fiName ? ` this.${fiName}();` : '';
      }
      if (info.kind === 'record' && !c.decl.compact) { /* explicit canonical ctor must assign fields itself */ }
      for (const s of stmts) if (s.k === 'expr' && s.e.k === 'ctorcall') this.err(s.ln, `call to ${s.e.which} must be first statement in constructor`);
      const body = this.blockStmts(stmts);
      const tm = this.ctx.tmps.length ? `let ${this.ctx.tmps.join(',')}; ` : '';
      const cap = this.ctx.captured ? 'const $o' + info.id + ' = this; ' : '';
      this.analyzeBody(c.decl.body, c.params, c.throws, true, c.ln);
      this.scope = prevScope; this.ctx = prevCtx; this.mtparams = prevMt;
      return `${jsName}(${pro.names}) { ${cap}${tm}${pro.pre}${head} ${body} return this; }`;
    }
    superInit(info, args, line, implicit) {
      if (info.parent) {
        const pc = info.parent.ctors;
        if (pc.length) this.checkCtorCall(info.parent, args, line, implicit);
        else if (args.length) this.err(line, `constructor ${info.parent.name} in class ${info.parent.name} cannot be applied to given types;`);
        return `${info.parent.js}.prototype.$init.call(this${args.map(a => ', ' + a.c).join('')});`;
      }
      if (info.parentLib) return `$L[${esc(info.parentLib)}].prototype.$init.call(this${args.map(a => ', ' + a.c).join('')});`;
      if (info.kind === 'enum') return '';
      return '';
    }
    checkCtorCall(target, args, line, implicit) {
      const cands = this.ctorCands(target).filter(c => c.params.some(p => p.varargs) ? args.length >= c.params.length - 1 : c.params.length === args.length);
      if (!cands.length) {
        const req = this.ctorCands(target).map(c => c.params.map(p => this.T(p.type)).join(',')).join(' | ') || 'no arguments';
        this.err(line, `constructor ${target.name} in class ${target.name} cannot be applied to given types;` + ` required: ${req}; found: ${args.map(a => a.t || 'Object').join(',') || 'no arguments'}`);
      } else if (cands.length === 1) this.checkArgTypes(cands[0], args, line, 'constructor');
    }

    /* ---------- methods ---------- */
    params(params, line) {
      const names = [], pre = [];
      for (const p of params) {
        this.checkType(p.type, line);
        const tb = this.T(p.type); const t = tb === null ? null : tb + (p.varargs ? '[]' : '');
        const js = this.jv(p.name);
        this.declare(p.name, { t, js, param: true, targs: this.targsOf(p.type), finalParam: false }, line);
        if (p.varargs) {
          const et = this.T({ ...p.type, dims: p.type.dims });
          names.push('...$va');
          pre.push(`let ${js} = ($va.length === 1 && ($va[0] === null || Array.isArray($va[0]) && (${PRIM.has(et) ? `$va[0].$t === ${esc(et)}` : `!PRIM_ARR.test($va[0].$t || '')`}))) ? $va[0] : $arr(${esc(et)}, $va.map($co[${esc(et)}] || (x => x)));`.replace('PRIM_ARR', '/^(int|double|char|long|boolean|byte|short|float)$/'));
        } else {
          names.push(js);
          const c = this.coerceCode(js, null, t, true);
          if (c !== js) pre.push(`${js} = ${c};`);
        }
      }
      return { names: names.join(', '), pre: pre.join(' ') };
    }
    genMethod(info, m) {
      const prevScope = this.scope, prevCtx = this.ctx, prevMt = this.mtparams;
      this.ctx = this.newCtx(info, m.static, m); this.mtparams = new Set((m.decl.tparams || []).map(t => t.name));
      this.scope = this.pushScope(true);
      this.checkType(m.ret, m.ln);
      this.ctx.ret = this.T(m.ret);
      const pro = this.params(m.params, m.ln);
      const body = this.blockStmts(m.decl.body.body);
      // flow analysis: reachability, definite assignment, effectively-final captures, checked exceptions
      const completes = this.analyzeBody(m.decl.body, m.params, m.throws, this.ctx.ret === 'void', m.ln);
      if (this.ctx.ret !== 'void' && completes) this.err(m.decl.body.end || m.ln, 'missing return statement');
      const tm = this.ctx.tmps.length ? `let ${this.ctx.tmps.join(',')}; ` : '';
      const cap = this.ctx.captured ? `const $o${info.id} = this; ` : '';
      this.scope = prevScope; this.ctx = prevCtx; this.mtparams = prevMt;
      return `(${pro.names}) { ${cap}${tm}${pro.pre}\n${body}\n}`;
    }
    blockStmts(stmts) { return stmts.map(s => this.stmt(s)).join('\n'); }

    /* ---------- scopes ---------- */
    pushScope(boundary) { const s = { vars: new Map(), parent: this.scope, boundary: !!boundary, classes: null }; return s; }
    declare(name, v, line) {
      if (this.scope) {
        for (let s = this.scope; s; s = s.parent) { if (s.vars.has(name) && !v.allowShadow) { const kind = this.ctx && this.ctx.method ? (this.ctx.isCtor ? `constructor ${this.ctx.cls.name}(${this.ctx.method.params.map(p => this.T(p.type)).join(',')})` : `method ${this.ctx.method.name}(${this.ctx.method.params.map(p => this.T(p.type)).join(',')})`) : 'method'; this.err(line, `variable ${name} is already defined in ${kind}`); break; } if (s.boundary) break; }
        this.scope.vars.set(name, v);
      }
    }
    lookupVar(name) { for (let s = this.scope; s; s = s.parent) if (s.vars.has(name)) return { v: s.vars.get(name), scope: s }; return null; }
    jv(n) { return JS_RESERVED.has(n) || n.startsWith('$') ? n + '$' : n; }

    /* ---------- member lookup ---------- */
    findField(info, name) {
      for (let c = info; c; c = c.parent) {
        if (c.fields.has(name)) return c.fields.get(name);
        for (const i of this.allIfaces(c)) if (i.fields.has(name)) return i.fields.get(name);
      }
      return null;
    }
    findMethods(info, name) {
      const out = [];
      const seen = new Set();
      const visit = c => { if (!c || seen.has(c)) return; seen.add(c); (c.methods.get(name) || []).forEach(m => out.push(m)); visit(c.parent); c.ifaces.forEach(visit); };
      visit(info);
      return out;
    }
    hasLibParent(info, seen = new Set()) { for (let c = info; c; c = c.parent) { if (c.parentLib || c.libIfaces.length) return true; if (seen.has(c)) continue; seen.add(c); if (c.ifaces.some(i => this.hasLibParent(i, seen))) return true; } return false; }

    /* Walk lexical class contexts looking for a member; returns {kind, member, level: cls ctx where found} */
    lookupMemberLexical(name, isMethod) {
      for (let c = this.cur; c; c = c.outer) {
        if (isMethod) { const ms = this.findMethods(c, name); if (ms.length) return { cls: c, members: ms }; if (this.hasLibParent(c) && (c === this.cur)) { /* may be inherited from the library */ } }
        else { const f = this.findField(c, name); if (f) return { cls: c, member: f }; }
        if (c.local || c.anon) { /* lexical capture continues into enclosing class via outer */ }
      }
      return null;
    }
    /* expression for "the instance of class `target` as seen from the current position" */
    instRef(target) {
      let c = this.cur, e = 'this';
      if (this.ctx && this.ctx.isStatic) return null;
      let guard = 0;
      while (c && guard++ < 50) {
        if (c === target || this.isSub(c, target)) return e;
        if (c.anon || c.local) { // lexical capture of the enclosing method's `this`
          const enc = c.outer; if (!enc) return null;
          if (c.staticCtx) return null;
          e = `$o${enc.id}`; this.markCapture(enc); c = enc; continue;
        }
        if (c.inner) { e = e + '.$outer'; c = c.outer; continue; }
        return null;
      }
      return null;
    }
    fj(f) { return (!f.static && this.hiddenFields && this.hiddenFields.has(f.name)) ? `$${f.name}$${f.owner.js}` : `$${f.name}`; }
    markCapture(enc) {
      for (let x = this.ctx; x; x = x.parentCtx) if (x.cls === enc && !x.isClinit) { x.captured = true; break; }
    }
  }

  const HELPERS = ['$D', '$F', '$Ch', '$add', '$sub', '$mul', '$div', '$mod', '$cat', '$neg', '$pos', '$bnot', '$and', '$or', '$xor', '$shl', '$shr', '$ushr', '$eq', '$ne', '$toInt', '$toLong', '$toDouble', '$toFloat', '$toChar', '$toByte', '$toShort', '$like', '$co', '$incv', '$arr', '$newArr', '$ai', '$as', '$alen', '$inv', '$chk', '$lam', '$mref', '$inst', '$cast', '$defc', '$str', '$equals', '$hash', '$iter', '$sw', '$tick', '$new', '$newInner', '$wrap', '$classOf', '$tryClose', '$ovlm', '$ovlc', '$ovlsup', '$ec', '$enumInit', '$fmt', '$boxEq', '$len', '$equalsNN', '$throw', '$g', '$castArr', '$lamI', '$lt', '$gt', '$le', '$ge'];

  return { Gen, HELPERS, HELPERS_LIST: HELPERS, libL, PRIM, NUMP, BOX, RANK, JS_RESERVED, esc, mangle, isNumT, unbox, isIntLike, binNum, isRefT, OBJ_RET, STR_RET, STATIC_RET, STATIC_FIELD, ARITH, CHECKED_BASES };
})();
