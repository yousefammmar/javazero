/* JavaZero compiler, stage 3a: runtime for compiled programs (java.lang, numbers, strings, exceptions, formatting, console input).
   JavaRT.create(env) builds a fresh runtime per run. Collections live in java-coll.js and plug in through JavaRT.extensions. */
const JavaRT = (() => {
  const extensions = [];
  function create(env) {
    const rt = {}, L = {}, H = {};
    rt.L = L; rt.H = H;
    const out = [];
    let cur = '';
    const MAXI = 2147483647, MINI = -2147483648;
    const PRIMARR = /^(int|double|char|long|boolean|byte|short|float)$/;

    /* ---------- boxed primitive stand-ins ---------- */
    class D { constructor(v, f) { this.v = v; this.f = !!f; } valueOf() { return this.v; } toString() { return dstr(this.v, this.f); } }
    const chCache = [];
    class Ch { constructor(c) { this.c = c; } valueOf() { return this.c; } toString() { return String.fromCharCode(this.c); } static of(c) { c &= 0xFFFF; return chCache[c] || (chCache[c] = new Ch(c)); } }
    const mkD = v => new D(v), mkF = v => new D(Math.fround(v), true);
    rt.$D = mkD; rt.$F = mkF; rt.$Ch = Ch.of; rt.D = D; rt.Ch = Ch;

    function dstr(v, isF) {
      if (v !== v) return 'NaN';
      if (v === Infinity) return 'Infinity';
      if (v === -Infinity) return '-Infinity';
      if (v === 0) return (1 / v < 0) ? '-0.0' : '0.0';
      let digits = null;
      const two = (x, f, orig) => { /* a 1-digit shortest form is rendered with the closest 2-digit decimal, as Java does (4.9E-324) */ const m = x.toExponential().split('e')[0]; if (m.length === 1 || (m.length === 2 && m[0] === '-')) { const t = (f ? orig : x).toExponential(1); const r = f ? Math.fround(+t) === orig : +t === x; if (r) return +t; } return x; };
      if (isF) { for (let p = 1; p <= 9; p++) { const s = v.toPrecision(p); if (Math.fround(+s) === v) { digits = +s; break; } } if (digits === null) digits = v; } else digits = v;
      digits = two(digits, isF, v);
      const a = Math.abs(digits);
      if (a >= 1e-3 && a < 1e7) { let s = String(digits); if (s.indexOf('e') >= 0) s = digits.toFixed(20).replace(/0+$/, ''); return s.indexOf('.') < 0 ? s + '.0' : s; }
      let e = digits.toExponential();
      let [m, x] = e.split('e');
      if (m.indexOf('.') < 0) m += '.0';
      return m + 'E' + (x[0] === '+' ? x.slice(1) : x);
    }
    rt.$dstr = dstr;

    /* ---------- identity hash / class objects ---------- */
    let idSeed = 0x1b6d3586, idCount = 0;
    const idMap = new WeakMap();
    const identityHash = o => { let h = idMap.get(o); if (h === undefined) { idSeed = (Math.imul(idSeed, 1103515245) + 12345 + (++idCount)) & 0x7fffffff; h = idSeed; idMap.set(o, h); } return h; };
    const classObjs = new Map();
    class JClass {
      constructor(c) { this.c = c; }
      getSimpleName() { return this.c.$jn !== undefined ? this.c.$jn : this.c.name; }
      getName() { return this.c.$jfq || this.c.$jn || this.c.name; }
      getTypeName() { return this.getName(); }
      toString() { return (this.c.$isIface ? 'interface ' : 'class ') + this.getName(); }
      hashCode() { return identityHash(this); }
      equals(o) { return this === o; }
      isInstance(o) { return rt.$inst(o, this.c); }
      getSuperclass() { const p = Object.getPrototypeOf(this.c); return p && p !== Function.prototype && p.$jn ? classOf(p) : null; }
      getEnumConstants() { return this.c.values ? this.c.values() : null; }
    }
    const classOf = c => { let o = classObjs.get(c); if (!o) classObjs.set(c, o = new JClass(c)); return o; };
    rt.$classOf = classOf;
    const pseudoClass = (name, fq) => { const c = function () {}; c.$jn = name; c.$jfq = fq; return c; };
    const C_STRING = pseudoClass('String', 'java.lang.String'), C_INT = pseudoClass('Integer', 'java.lang.Integer'), C_LONG = pseudoClass('Long', 'java.lang.Long'),
      C_DBL = pseudoClass('Double', 'java.lang.Double'), C_FLT = pseudoClass('Float', 'java.lang.Float'), C_CH = pseudoClass('Character', 'java.lang.Character'), C_BOOL = pseudoClass('Boolean', 'java.lang.Boolean');

    class JObject {
      $init() { return this; }
      equals(o) { return this === o; }
      hashCode() { return identityHash(this); }
      toString() { return (this.constructor.$jfq || this.constructor.$jn || this.constructor.name) + '@' + (this.hashCode() >>> 0).toString(16); }
      getClass() { return classOf(this.constructor); }
      notify() {} notifyAll() {} wait() {}
    }
    JObject.$jn = 'Object'; JObject.$jfq = 'java.lang.Object'; JObject.$supers = new Set([JObject]);
    L.Object = JObject;
    const defLib = (cls, jn, fq, supers = [], opts = {}) => {
      cls.$jn = jn; cls.$jfq = fq; if (opts.iface) cls.$isIface = true;
      const set = new Set([cls]);
      let p = Object.getPrototypeOf(cls);
      while (p && p.$supers) { p.$supers.forEach(x => set.add(x)); break; }
      supers.forEach(s => { set.add(s); if (s.$supers) s.$supers.forEach(x => set.add(x)); });
      cls.$supers = set; L[jn] = cls; return cls;
    };
    H.defLib = defLib;
    defLib(JClass, 'Class', 'java.lang.Class');
    const iface = (name, fq) => { const c = class { }; c.$jn = name; c.$jfq = fq; c.$isIface = true; c.$supers = new Set([c]); L[name] = c; return c; };
    H.iface = iface;

    /* ---------- exceptions (defined early: everything below throws them) ---------- */
    const UNINIT = { uninit: true };
    class Throwable extends JObject {
      constructor() { super(); this.$msg = null; this.$cause = UNINIT; this.$supp = []; if (env.debug) this.$js = new Error('debug'); }
      $init(a, b) {
        const n = arguments.length;
        if (n === 1 && a instanceof Throwable) { this.$cause = a; this.$msg = a.toString(); }
        else if (n >= 1) { this.$msg = a === undefined ? null : a; if (n >= 2) this.$cause = b === undefined ? null : b; }
        return this;
      }
      getMessage() { return this.$msg; }
      getLocalizedMessage() { return this.getMessage(); }
      getCause() { return this.$cause === UNINIT ? null : this.$cause; }
      initCause(c) {
        if (this.$cause !== UNINIT) throw mk('IllegalStateException', "Can't overwrite cause with " + (c === null || c === undefined ? 'a null' : c.toString()), this);
        if (c === this) throw mk('IllegalArgumentException', 'Self-causation not permitted', this);
        this.$cause = c === undefined ? null : c; return this;
      }
      toString() { const n = this.constructor.$jfq || this.constructor.$jn || this.constructor.name, m = this.getLocalizedMessage(); return m != null ? n + ': ' + m : n; }
      printStackTrace() {
        errPrintln(this.toString()); errPrintln('\tat Main.main(Main.java)');
        for (let c = this.getCause(), k = 0; c && k < 20; c = c.getCause(), k++) { errPrintln('Caused by: ' + c.toString()); errPrintln('\t... 1 more'); }
      }
      getStackTrace() { return arr('StackTraceElement', []); }
      setStackTrace() {} fillInStackTrace() { return this; }
      addSuppressed(t) { if (t === this) throw mk('IllegalArgumentException', 'Self-suppression not permitted', t); if (t === null || t === undefined) throw mk('NullPointerException', 'Cannot suppress a null exception.'); this.$supp.push(t); } getSuppressed() { return arr('Throwable', this.$supp.slice()); }
    }
    defLib(Throwable, 'Throwable', 'java.lang.Throwable');
    const mkEx = (name, parent, pkg = 'java.lang') => {
      const cls = ({ [name]: class extends parent { } })[name];
      return defLib(cls, name, pkg + '.' + name);
    };
    const Exception = mkEx('Exception', Throwable), RuntimeException = mkEx('RuntimeException', Exception), JError = mkEx('Error', Throwable);
    H.Throwable = Throwable; H.Exception = Exception; H.RuntimeException = RuntimeException;
    const exs = {};
    [['IllegalArgumentException', RuntimeException], ['IllegalStateException', RuntimeException], ['ArithmeticException', RuntimeException],
      ['NullPointerException', RuntimeException], ['ClassCastException', RuntimeException], ['IndexOutOfBoundsException', RuntimeException],
      ['NegativeArraySizeException', RuntimeException], ['ArrayStoreException', RuntimeException], ['UnsupportedOperationException', RuntimeException],
      ['SecurityException', RuntimeException], ['CloneNotSupportedException', Exception], ['InterruptedException', Exception],
      ['ReflectiveOperationException', Exception], ['StackOverflowError', null], ['OutOfMemoryError', null], ['AssertionError', JError], ['VirtualMachineError', JError]].forEach(([n, p]) => {
      if (n === 'StackOverflowError' || n === 'OutOfMemoryError') return;
      exs[n] = mkEx(n, p);
    });
    exs.VirtualMachineError = L.VirtualMachineError;
    exs.StackOverflowError = mkEx('StackOverflowError', exs.VirtualMachineError); exs.OutOfMemoryError = mkEx('OutOfMemoryError', exs.VirtualMachineError);
    const intMsgCtor = (cls, prefix) => { const orig = cls.prototype.$init; cls.prototype.$init = function (a) { if (typeof a === 'number' || typeof a === 'bigint') return orig.call(this, prefix + a); return orig.apply(this, arguments); }; return cls; };
    exs.IndexOutOfBoundsException = intMsgCtor(exs.IndexOutOfBoundsException, 'Index out of range: ');
    exs.ArrayIndexOutOfBoundsException = intMsgCtor(mkEx('ArrayIndexOutOfBoundsException', exs.IndexOutOfBoundsException), 'Array index out of range: ');
    exs.StringIndexOutOfBoundsException = intMsgCtor(mkEx('StringIndexOutOfBoundsException', exs.IndexOutOfBoundsException), 'String index out of range: ');
    exs.NumberFormatException = mkEx('NumberFormatException', exs.IllegalArgumentException);
    exs.NoSuchElementException = mkEx('NoSuchElementException', RuntimeException, 'java.util');
    exs.InputMismatchException = mkEx('InputMismatchException', exs.NoSuchElementException, 'java.util');
    exs.ConcurrentModificationException = mkEx('ConcurrentModificationException', RuntimeException, 'java.util');
    exs.EmptyStackException = mkEx('EmptyStackException', RuntimeException, 'java.util');
    exs.DateTimeException = mkEx('DateTimeException', RuntimeException, 'java.time');
    exs.IOException = mkEx('IOException', Exception, 'java.io');
    exs.FileNotFoundException = mkEx('FileNotFoundException', exs.IOException, 'java.io');
    exs.UncheckedIOException = mkEx('UncheckedIOException', RuntimeException, 'java.io');
    exs.TimeoutException = mkEx('TimeoutException', Exception, 'java.util.concurrent');
    exs.IllegalFormatException = mkEx('IllegalFormatException', exs.IllegalArgumentException, 'java.util');
    exs.UnknownFormatConversionException = mkEx('UnknownFormatConversionException', exs.IllegalFormatException, 'java.util');
    exs.MissingFormatArgumentException = mkEx('MissingFormatArgumentException', exs.IllegalFormatException, 'java.util');
    exs.IllegalFormatConversionException = mkEx('IllegalFormatConversionException', exs.IllegalFormatException, 'java.util');
    exs.DuplicateFormatFlagsException = exs.IllegalFormatException;
    exs.ArithmeticException = L.ArithmeticException;
    const mk = (n, ...a) => new exs[n]().$init(...a);
    const throwEx = (n, ...a) => { throw mk(n, ...a); };
    H.mk = mk; H.throwEx = throwEx; H.exs = exs;
    class ExitSignal { constructor(code) { this.code = code; } }
    rt.ExitSignal = ExitSignal;
    class NeedInput { }
    rt.NeedInput = NeedInput;

    /* ---------- output ---------- */
    const fmtPrintable = x => str(x);
    const w = s => { cur += s; };
    const pl = s => { out.push(cur + s); cur = ''; };
    const errPrintln = s => pl(s);
    rt.$out = () => ({ lines: out, cur });
    rt.$flush = () => { if (cur) { out.push(cur); cur = ''; } return out; };
    rt.$curLine = () => cur;
    class PrintStream extends JObject {
      constructor(err) { super(); this.err = err; }
      print(x) { w(typeof x === 'object' && x && x.$t === 'char' ? x.map(c => c.toString()).join('') : str(x)); }
      println(x) { if (arguments.length === 0) pl(''); else pl(typeof x === 'object' && x && x.$t === 'char' ? x.map(c => c.toString()).join('') : str(x)); }
      printf(f, ...a) { if (f && f.$isLocale) { f = a.shift(); } w(fmt(f, a.length === 1 && Array.isArray(a[0]) && !PRIMARR.test(a[0].$t || '') ? a[0] : a)); return this; }
      format(f, ...a) { return this.printf(f, ...a); }
      flush() {} close() {} write(b) { w(String.fromCharCode(b)); }
      append(x) { w(str(x)); return this; }
    }
    defLib(PrintStream, 'PrintStream', 'java.io.PrintStream');

    /* ---------- string conversion, equality, hashing ---------- */
    function str(x) {
      if (x === null || x === undefined) return 'null';
      switch (typeof x) {
        case 'string': return x;
        case 'number': return '' + x;
        case 'boolean': return x ? 'true' : 'false';
        case 'bigint': return x.toString();
        case 'function': return 'Main$$Lambda@' + (identityHash(x) >>> 0).toString(16);
      }
      if (x instanceof String) return x.valueOf();
      if (Array.isArray(x)) { const c = { int: '[I', long: '[J', double: '[D', float: '[F', char: '[C', byte: '[B', short: '[S', boolean: '[Z' }[x.$t]; return (c || '[L' + (x.$t || 'java.lang.Object') + ';') + '@' + (identityHash(x) >>> 0).toString(16); }
      return x.toString();
    }
    rt.$str = str;
    H.f64 = null; H.u32 = null; H.dstr = null;
    const strHash = s => { let h = 0; for (let i = 0; i < s.length; i++) h = (Math.imul(31, h) + s.charCodeAt(i)) | 0; return h; };
    const f64 = new Float64Array(1), u32 = new Uint32Array(f64.buffer);
    H.f64 = f64; H.u32 = u32; H.dstr = dstr;
    const dblHash = v => { if (v !== v) v = NaN; f64[0] = v; return (u32[0] ^ u32[1]) | 0; };
    function hash(x) {
      if (x === null || x === undefined) return 0;
      switch (typeof x) {
        case 'string': return strHash(x);
        case 'number': return x | 0;
        case 'boolean': return x ? 1231 : 1237;
        case 'bigint': return Number(BigInt.asIntN(32, x ^ (x >> 32n)));
      }
      if (x instanceof String) return strHash(x.valueOf());
      if (x instanceof D) return x.f ? (() => { const b = new Float32Array(1); b[0] = x.v; return new Int32Array(b.buffer)[0]; })() : dblHash(x.v);
      if (x instanceof Ch) return x.c;
      if (typeof x.hashCode === 'function') return x.hashCode() | 0;
      return identityHash(x);
    }
    rt.$hash = hash;
    function equals(a, b) {
      if (a === b) return true;
      if (a === null || a === undefined || b === null || b === undefined) return false;
      const ta = typeof a;
      if (ta === 'string' || a instanceof String) return (typeof b === 'string' || b instanceof String) && a.valueOf() === b.valueOf();
      if (ta === 'number') return false; // a !== b above, ints identical only when ===
      if (ta === 'bigint') return typeof b === 'bigint' && a === b;
      if (ta === 'boolean') return false;
      if (a instanceof D) return b instanceof D && a.f === b.f && (a.v === b.v ? (a.v !== 0 || 1 / a.v === 1 / b.v) : (a.v !== a.v && b.v !== b.v));
      if (a instanceof Ch) return b instanceof Ch && a.c === b.c;
      if (typeof a.equals === 'function') return !!a.equals(b);
      return false;
    }
    rt.$equals = equals;
    // a.equals(b) with a possibly null (NullPointerException like Java)
    rt.$equalsNN = (a, b) => { if (a === null || a === undefined) throw mk('NullPointerException', 'Cannot invoke "Object.equals(Object)" because value is null'); return equals(a, b); };
    H.str = str; H.hash = hash; H.equals = equals; H.identityHash = identityHash;

    function compare(a, b) {
      if (a === null || b === null || a === undefined || b === undefined) throw mk('NullPointerException');
      const ta = typeof a;
      if (ta === 'string' || a instanceof String) {
        if (!(typeof b === 'string' || b instanceof String)) throw mk('ClassCastException', cceMsg(className(b), 'java.lang.String'));
        return strCmp(a.valueOf(), b.valueOf());
      }
      if (ta === 'number' || ta === 'bigint') return a < b ? -1 : a > b ? 1 : 0;
      if (ta === 'boolean') return a === b ? 0 : a ? 1 : -1;
      if (a instanceof D) return dblCmp(a.v, +b);
      if (a instanceof Ch) return a.c - b.c;
      if (typeof a.compareTo === 'function') return a.compareTo(b);
      throw mk('ClassCastException', cceMsg(className(a), 'java.lang.Comparable'));
    }
    const cceMsg = (a, b) => {
      const mod = n => /^java\./.test(n) ? `module java.base of loader 'bootstrap'` : `unnamed module of loader 'app'`;
      return `class ${a} cannot be cast to class ${b} (${mod(a) === mod(b) ? `${a} and ${b} are in ${mod(a)}` : `${a} is in ${mod(a)}; ${b} is in ${mod(b)}`})`;
    };
    const strCmp = (a, b) => { const n = Math.min(a.length, b.length); for (let i = 0; i < n; i++) { const d = a.charCodeAt(i) - b.charCodeAt(i); if (d) return d; } return a.length - b.length; };
    const dblCmp = (a, b) => a < b ? -1 : a > b ? 1 : (a === b ? (a === 0 ? (1 / a === 1 / b ? 0 : (1 / a < 0 ? -1 : 1)) : 0) : (a !== a ? (b !== b ? 0 : 1) : -1));
    rt.$compare = compare; H.compare = compare; H.strCmp = strCmp;
    H.cceMsg = cceMsg;
    const className = x => {
      if (x === null || x === undefined) return 'null';
      switch (typeof x) { case 'string': return 'java.lang.String'; case 'number': return 'java.lang.Integer'; case 'bigint': return 'java.lang.Long'; case 'boolean': return 'java.lang.Boolean'; case 'function': return 'Main$$Lambda'; }
      if (x instanceof String) return 'java.lang.String';
      if (x instanceof D) return x.f ? 'java.lang.Float' : 'java.lang.Double';
      if (x instanceof Ch) return 'java.lang.Character';
      if (Array.isArray(x)) return str(x).split('@')[0];
      return x.constructor.$jfq || x.constructor.$jn || x.constructor.name;
    };
    H.className = className;

    /* ---------- numeric tower ---------- */
    const isNumLike = x => typeof x === 'number' || typeof x === 'bigint' || x instanceof D || x instanceof Ch;
    const toNum = x => typeof x === 'number' ? x : x instanceof D ? x.v : x instanceof Ch ? x.c : typeof x === 'bigint' ? Number(x) : x === null || x === undefined ? (() => { throw mk('NullPointerException'); })() : +x;
    // rank: 1 int, 2 long, 3 float, 4 double
    const rankOf = x => typeof x === 'number' ? 1 : typeof x === 'bigint' ? 2 : x instanceof D ? (x.f ? 3 : 4) : x instanceof Ch ? 1 : 0;
    H.toNum = toNum;
    function arith(op, a, b) {
      const ra = rankOf(a), rb = rankOf(b);
      if (!ra || !rb) {
        if (a === null || a === undefined || b === null || b === undefined) throw mk('NullPointerException');
        throw mk('ClassCastException', `bad operand types for binary operator '${op}'`);
      }
      const r = Math.max(ra, rb);
      if (r === 1) {
        const x = toNum(a), y = toNum(b);
        switch (op) {
          case '+': return (x + y) | 0; case '-': return (x - y) | 0; case '*': return Math.imul(x, y);
          case '/': if (y === 0) throwEx('ArithmeticException', '/ by zero'); return (x / y) | 0;
          case '%': if (y === 0) throwEx('ArithmeticException', '/ by zero'); return (x % y) | 0;
        }
      }
      if (r === 2) {
        const x = BigInt(toNum2(a)), y = BigInt(toNum2(b));
        switch (op) {
          case '+': return BigInt.asIntN(64, x + y); case '-': return BigInt.asIntN(64, x - y); case '*': return BigInt.asIntN(64, x * y);
          case '/': if (y === 0n) throwEx('ArithmeticException', '/ by zero'); return BigInt.asIntN(64, x / y);
          case '%': if (y === 0n) throwEx('ArithmeticException', '/ by zero'); return x % y;
        }
      }
      const x = toNum(a), y = toNum(b);
      let v;
      switch (op) { case '+': v = x + y; break; case '-': v = x - y; break; case '*': v = x * y; break; case '/': v = x / y; break; case '%': v = x % y; break; }
      return r === 3 ? new D(Math.fround(v), true) : new D(v);
    }
    const toNum2 = x => typeof x === 'bigint' ? x : x instanceof Ch ? x.c : typeof x === 'number' ? x : x.v;
    const dres = (v, f) => new D(f ? Math.fround(v) : v, f);
    rt.$add = (a, b) => {
      if (typeof a === 'number') { if (typeof b === 'number') return (a + b) | 0; if (b instanceof D) return dres(a + b.v, b.f); }
      else if (a instanceof D) { if (b instanceof D) return dres(a.v + b.v, a.f && b.f); if (typeof b === 'number') return dres(a.v + b, a.f); }
      else if (typeof a === 'bigint') { if (typeof b === 'bigint') return BigInt.asIntN(64, a + b); if (typeof b === 'number') return BigInt.asIntN(64, a + BigInt(b)); }
      if (typeof a === 'string' || typeof b === 'string' || a instanceof String || b instanceof String) return str(a) + str(b);
      if (!isNumLike(a) || !isNumLike(b)) { if (a === null || b === null || a === undefined || b === undefined) throw mk('NullPointerException'); return str(a) + str(b); }
      return arith('+', a, b);
    };
    rt.$sub = (a, b) => {
      if (typeof a === 'number') { if (typeof b === 'number') return (a - b) | 0; if (b instanceof D) return dres(a - b.v, b.f); }
      else if (a instanceof D) { if (b instanceof D) return dres(a.v - b.v, a.f && b.f); if (typeof b === 'number') return dres(a.v - b, a.f); }
      else if (typeof a === 'bigint') { if (typeof b === 'bigint') return BigInt.asIntN(64, a - b); if (typeof b === 'number') return BigInt.asIntN(64, a - BigInt(b)); }
      return arith('-', a, b);
    };
    rt.$mul = (a, b) => {
      if (typeof a === 'number') { if (typeof b === 'number') return Math.imul(a, b); if (b instanceof D) return dres(a * b.v, b.f); }
      else if (a instanceof D) { if (b instanceof D) return dres(a.v * b.v, a.f && b.f); if (typeof b === 'number') return dres(a.v * b, a.f); }
      else if (typeof a === 'bigint') { if (typeof b === 'bigint') return BigInt.asIntN(64, a * b); if (typeof b === 'number') return BigInt.asIntN(64, a * BigInt(b)); }
      return arith('*', a, b);
    };
    rt.$div = (a, b) => {
      if (typeof a === 'number') { if (typeof b === 'number') { if (b === 0) throwEx('ArithmeticException', '/ by zero'); return (a / b) | 0; } if (b instanceof D) return dres(a / b.v, b.f); }
      else if (a instanceof D) { if (b instanceof D) return dres(a.v / b.v, a.f && b.f); if (typeof b === 'number') return dres(a.v / b, a.f); }
      return arith('/', a, b);
    };
    rt.$mod = (a, b) => {
      if (typeof a === 'number' && typeof b === 'number') { if (b === 0) throwEx('ArithmeticException', '/ by zero'); return (a % b) | 0; }
      if (a instanceof D) { if (b instanceof D) return dres(a.v % b.v, a.f && b.f); if (typeof b === 'number') return dres(a.v % b, a.f); }
      return arith('%', a, b);
    };
    rt.$cat = (a, b) => str(a) + str(b);
    rt.$neg = a => typeof a === 'number' ? (-a) | 0 : typeof a === 'bigint' ? BigInt.asIntN(64, -a) : a instanceof D ? new D(-a.v, a.f) : a instanceof Ch ? -a.c : (() => { throw mk('NullPointerException'); })();
    rt.$pos = a => a instanceof Ch ? a.c : a;
    rt.$bnot = a => typeof a === 'bigint' ? BigInt.asIntN(64, ~a) : ~toNum(a);
    rt.$not = a => !a;
    const bitop = (op, a, b) => {
      if (typeof a === 'boolean') return op === '&' ? a && b : op === '|' ? a || b : a !== b;
      if (typeof a === 'bigint' || typeof b === 'bigint') { const x = BigInt(toNum2(a)), y = BigInt(toNum2(b)); return BigInt.asIntN(64, op === '&' ? x & y : op === '|' ? x | y : x ^ y); }
      const x = toNum(a), y = toNum(b); return op === '&' ? x & y : op === '|' ? x | y : x ^ y;
    };
    rt.$and = (a, b) => bitop('&', a, b); rt.$or = (a, b) => bitop('|', a, b); rt.$xor = (a, b) => bitop('^', a, b);
    rt.$shl = (a, b) => typeof a === 'bigint' ? BigInt.asIntN(64, a << BigInt(Number(toNum2(b)) & 63)) : (toNum(a) << (Number(toNum2(b)) & 31));
    rt.$shr = (a, b) => typeof a === 'bigint' ? a >> BigInt(Number(toNum2(b)) & 63) : (toNum(a) >> (Number(toNum2(b)) & 31));
    rt.$ushr = (a, b) => typeof a === 'bigint' ? BigInt.asIntN(64, BigInt.asUintN(64, a) >> BigInt(Number(toNum2(b)) & 63)) : ((toNum(a) >>> (Number(toNum2(b)) & 31)) | 0);
    rt.$eq = (a, b) => {
      if (a === b) return !(a instanceof D && a.v !== a.v);
      if (a === null || b === null || a === undefined || b === undefined) return false;
      const ta = typeof a, tb = typeof b;
      if ((ta === 'number' || ta === 'bigint' || a instanceof D || a instanceof Ch) && (tb === 'number' || tb === 'bigint' || b instanceof D || b instanceof Ch)) {
        if (a instanceof D || b instanceof D) return toNum(a) === toNum(b);
        return (ta === 'bigint' || tb === 'bigint') ? BigInt(toNum2(a)) === BigInt(toNum2(b)) : toNum(a) === toNum(b);
      }
      return false;
    };
    rt.$ne = (a, b) => !rt.$eq(a, b);
    // relational operators on possibly boxed values
    rt.$lt = (a, b) => (typeof a === 'number' && typeof b === 'number') ? a < b : toCmp(a) < toCmp(b);
    rt.$gt = (a, b) => (typeof a === 'number' && typeof b === 'number') ? a > b : toCmp(a) > toCmp(b);
    rt.$le = (a, b) => (typeof a === 'number' && typeof b === 'number') ? a <= b : toCmp(a) <= toCmp(b);
    rt.$ge = (a, b) => (typeof a === 'number' && typeof b === 'number') ? a >= b : toCmp(a) >= toCmp(b);
    const toCmp = x => { if (x === null || x === undefined) throw mk('NullPointerException'); return x instanceof D ? x.v : x instanceof Ch ? x.c : x; };

    // casts / conversions
    const satInt = v => v !== v ? 0 : v >= 2147483647 ? MAXI : v <= -2147483648 ? MINI : Math.trunc(v);
    rt.$toInt = x => typeof x === 'number' ? x : x instanceof D ? satInt(x.v) : x instanceof Ch ? x.c : typeof x === 'bigint' ? Number(BigInt.asIntN(32, x)) : x === null || x === undefined ? (() => { throw mk('NullPointerException'); })() : x;
    rt.$toLong = x => typeof x === 'bigint' ? x : typeof x === 'number' ? BigInt(x) : x instanceof Ch ? BigInt(x.c) : x instanceof D ? (x.v !== x.v ? 0n : x.v >= 9.223372036854775807e18 ? 9223372036854775807n : x.v <= -9.223372036854775808e18 ? -9223372036854775808n : BigInt(Math.trunc(x.v))) : (() => { throw mk('NullPointerException'); })();
    rt.$toDouble = x => x instanceof D && !x.f ? x : x instanceof D ? new D(x.v) : new D(toNum(x));
    rt.$toFloat = x => x instanceof D && x.f ? x : new D(Math.fround(toNum(x)), true);
    rt.$toChar = x => x instanceof Ch ? x : Ch.of(typeof x === 'number' ? x : x instanceof D ? satInt(x.v) : Number(BigInt.asIntN(16, BigInt(toNum2(x)))));
    rt.$toByte = x => (rt.$toInt(x) << 24) >> 24;
    rt.$toShort = x => (rt.$toInt(x) << 16) >> 16;
    // assignment conversion by prior value kind (used for fields/array slots/compound assignment when the static type is unknown)
    rt.$like = (old, v) => {
      if (old === null || old === undefined || v === null || v === undefined) return v;
      if (old instanceof D) return v instanceof D ? (old.f && !v.f ? new D(Math.fround(v.v), true) : (!old.f && v.f ? new D(v.v) : v)) : isNumLike(v) ? (old.f ? new D(Math.fround(toNum(v)), true) : new D(toNum(v))) : v;
      if (typeof old === 'bigint') return typeof v === 'bigint' ? v : typeof v === 'number' ? BigInt(v) : v instanceof Ch ? BigInt(v.c) : v;
      if (old instanceof Ch) return typeof v === 'number' ? Ch.of(v) : v instanceof D || typeof v === 'bigint' ? rt.$toChar(v) : v;
      if (typeof old === 'number') return typeof v === 'number' ? v : v instanceof Ch ? v.c : (v instanceof D || typeof v === 'bigint') ? rt.$toInt(v) : v;
      return v;
    };
    // implicit widening for a declared primitive type
    const coerceTo = { double: rt.$toDouble, float: rt.$toFloat, long: rt.$toLong, int: x => typeof x === 'number' ? x : x instanceof Ch ? x.c : x, char: x => x instanceof Ch ? x : typeof x === 'number' ? Ch.of(x) : x, short: x => x instanceof Ch ? x.c : x, byte: x => x instanceof Ch ? x.c : x };
    rt.$co = coerceTo;
    rt.$incv = (x, d) => typeof x === 'number' ? (x + d) | 0 : x instanceof D ? new D(x.f ? Math.fround(x.v + d) : x.v + d, x.f) : typeof x === 'bigint' ? BigInt.asIntN(64, x + BigInt(d)) : x instanceof Ch ? Ch.of(x.c + d) : (() => { throw mk('NullPointerException'); })();

    /* ---------- arrays ---------- */
    const zero = { int: 0, short: 0, byte: 0, long: 0n, boolean: false };
    const zeroOf = t => t in zero ? zero[t] : t === 'double' ? new D(0) : t === 'float' ? new D(0, true) : t === 'char' ? Ch.of(0) : null;
    function arr(t, items) { items.$t = t; return items; }
    rt.$arr = arr; H.arr = arr;
    function newArr(t, dims, extra) {
      // t: element base type name; dims: array of lengths; extra: number of unspecified trailing dims
      const make = (i) => {
        const n = dims[i];
        if (n < 0) throwEx('NegativeArraySizeException', String(n));
        if (n > 50000000) throwEx('OutOfMemoryError', n >= 2147483646 ? 'Requested array size exceeds VM limit' : 'Java heap space');
        const innerT = t + '[]'.repeat(dims.length - i - 1 + extra);
        const leaf = i === dims.length - 1;
        const a = new Array(n);
        if (leaf) { if (extra) a.fill(null); else { const z = zeroOf(t); for (let k = 0; k < n; k++) a[k] = z; } }
        else for (let k = 0; k < n; k++) a[k] = make(i + 1);
        a.$t = innerT;
        return a;
      };
      return make(0);
    }
    rt.$newArr = (t, dims, extra) => newArr(t, dims.map(rt.$toInt), extra || 0);
    const oob = (i, n) => throwEx('ArrayIndexOutOfBoundsException', `Index ${i} out of bounds for length ${n}`);
    rt.$ai = (a, i) => { if (a === null || a === undefined) throw mk('NullPointerException'); if (typeof i !== 'number') i = toNum(i); if (i < 0 || i >= a.length) oob(i, a.length); return a[i]; };
    rt.$as = (a, i, v) => {
      if (a === null || a === undefined) throw mk('NullPointerException');
      if (typeof i !== 'number') i = toNum(i);
      if (i < 0 || i >= a.length) oob(i, a.length);
      const t = a.$t;
      if (t && v !== null && v !== undefined) {
        const c = coerceTo[t]; if (c) v = c(v);
        else if (t !== 'Object' && !t.endsWith('[]')) { const pt = primTests[t]; const C = pt ? null : (rt.$classes.get(t) || L[t]); if ((pt && !pt(v)) || (C && !rt.$inst(v, C))) throwEx('ArrayStoreException', className(v)); }
      }
      return a[i] = v;
    };
    rt.$alen = a => { if (a === null || a === undefined) throw mk('NullPointerException'); return a.length; };

    /* ---------- boxed method tables (receivers that are JS primitives) ---------- */
    const idxErr = (i, n) => throwEx('StringIndexOutOfBoundsException', `Index ${i} out of bounds for length ${n}`);
    const subErr = (b, e, n) => throwEx('StringIndexOutOfBoundsException', `Range [${b}, ${e}) out of bounds for length ${n}`);
    const jregex = (re, flags = '') => { let r = re.replace(/\\p\{Alpha\}/g, '[A-Za-z]').replace(/\\p\{Punct\}/g, '[!-\\/:-@\\[-`{-~]').replace(/\\p\{Upper\}/g, '[A-Z]').replace(/\\p\{Lower\}/g, '[a-z]').replace(/\\p\{Digit\}/g, '\\d').replace(/\\p\{Alnum\}/g, '[A-Za-z0-9]').replace(/\(\?i\)/g, () => { flags += 'i'; return ''; }).replace(/\\h/g, '[ \\t]').replace(/\*\+/g, '*').replace(/\+\+/g, '+'); try { return new RegExp(r, flags); } catch (e) { throwEx('IllegalArgumentException', 'Invalid regex: ' + re); } };
    H.jregex = jregex;
    // Java replacement syntax -> JS: $0 is the whole match, \$ a literal dollar, ${name} a named group
    const javaRepl = r => r.replace(/\\(.)|\$(\d)|\$\{(\w+)\}|\$/g, (m, esc, d, nm) => esc !== undefined ? (esc === '$' ? '$$' : esc) : d !== undefined ? (d === '0' ? '$&' : '$' + d) : nm !== undefined ? '$<' + nm + '>' : (() => { throwEx('IllegalArgumentException', 'Illegal group reference'); })());
    const STRM = {
      length: s => s.length,
      charAt: (s, i) => { i = toNum(i); if (i < 0 || i >= s.length) throwEx('StringIndexOutOfBoundsException', `Index ${i} out of bounds for length ${s.length}`); return Ch.of(s.charCodeAt(i)); },
      codePointAt: (s, i) => { i = toNum(i); if (i < 0 || i >= s.length) idxErr(i, s.length); return s.codePointAt(i); },
      isEmpty: s => s.length === 0,
      isBlank: s => s.trim().length === 0,
      substring: (s, b, e) => { b = toNum(b); const n = s.length; if (e === undefined) { if (b < 0 || b > n) subErr(b, n, n); return s.substring(b); } e = toNum(e); if (b < 0 || e > n || b > e) subErr(b, e, n); return s.substring(b, e); },
      subSequence: (s, b, e) => STRM.substring(s, b, e),
      indexOf: (s, x, from) => { const q = x instanceof Ch ? String.fromCharCode(x.c) : typeof x === 'number' ? String.fromCodePoint(x) : str(x); return s.indexOf(q, from === undefined ? 0 : toNum(from)); },
      lastIndexOf: (s, x, from) => { const q = x instanceof Ch ? String.fromCharCode(x.c) : typeof x === 'number' ? String.fromCodePoint(x) : str(x); return from === undefined ? s.lastIndexOf(q) : s.lastIndexOf(q, toNum(from)); },
      contains: (s, x) => s.includes(str(x)),
      startsWith: (s, x, off) => s.startsWith(str(x), off === undefined ? 0 : toNum(off)),
      endsWith: (s, x) => s.endsWith(str(x)),
      equals: (s, o) => (typeof o === 'string' || o instanceof String) && s === o.valueOf(),
      equalsIgnoreCase: (s, o) => o !== null && o !== undefined && s.toLowerCase() === str(o).toLowerCase(),
      contentEquals: (s, o) => s === str(o),
      compareTo: (s, o) => { if (o === null || o === undefined) throw mk('NullPointerException'); return strCmp(s, str(o)); },
      compareToIgnoreCase: (s, o) => strCmp(s.toLowerCase(), str(o).toLowerCase()),
      hashCode: s => strHash(s),
      toString: s => s, intern: s => s, strip: s => s.trim(), trim: s => s.replace(/^[\x00-\x20]+|[\x00-\x20]+$/g, ''),
      stripLeading: s => s.trimStart(), stripTrailing: s => s.trimEnd(),
      toUpperCase: s => s.toUpperCase(), toLowerCase: s => s.toLowerCase(),
      concat: (s, o) => s + o,
      replace: (s, a, b) => { a = str(a); b = str(b); if (a === '') return b + Array.from(s).join(b) + (s === '' ? '' : b); return s.split(a).join(b); },
      replaceAll: (s, re, rep) => s.replace(jregex(re, 'g'), javaRepl(str(rep))),
      replaceFirst: (s, re, rep) => s.replace(jregex(re), javaRepl(str(rep))),
      matches: (s, re) => jregex('^(?:' + re + ')$').test(s),
      split: (s, re, limit) => {
        limit = limit === undefined ? 0 : toNum(limit);
        let parts;
        if (s === '') return arr('String', ['']);
        if (re === '') { const cs = Array.from(s); if (limit < 0) cs.push(''); else if (limit > 0 && cs.length > limit) { const h = cs.slice(0, limit - 1); h.push(cs.slice(limit - 1).join('')); return arr('String', h); } return arr('String', cs); }
        const r = jregex(re.length === 1 && !/[\\^$.|?*+()[\]{}]/.test(re) ? re.replace(/[\\^$.|?*+()[\]{}]/g, '\\$&') : re, 'g');
        parts = []; let last = 0, m;
        while ((m = r.exec(s)) !== null) {
          if (m[0] === '') { if (m.index === 0) { r.lastIndex++; continue; } }
          if (m.index === 0 && m[0] !== '' && last === 0 && m.index === 0) { /* leading empty string is kept for non-zero-width match */ }
          if (limit > 0 && parts.length === limit - 1) break;
          parts.push(s.substring(last, m.index)); last = m.index + m[0].length;
          if (m.index >= s.length) break;
          if (m[0] === '') r.lastIndex++;
        }
        parts.push(s.substring(last));
        if (limit === 0) while (parts.length > 1 && parts[parts.length - 1] === '') parts.pop();
        if (limit === 0 && parts.length === 1 && parts[0] === '' && s !== '') return arr('String', []);
        return arr('String', parts);
      },
      toCharArray: s => arr('char', Array.from(s, c => Ch.of(c.charCodeAt(0)))),
      chars: s => L.IntStream ? L.IntStream.of(...Array.from(s, c => c.charCodeAt(0))) : null,
      repeat: (s, n) => { n = toNum(n); if (n < 0) throwEx('IllegalArgumentException', 'count is negative: ' + n); return s.repeat(n); },
      formatted: (s, ...a) => fmt(s, a),
      indent: (s, n) => { n = toNum(n); if (s === '') return ''; return s.split(/\r?\n/).filter((l, i, a) => !(i === a.length - 1 && l === '')).map(l => n >= 0 ? ' '.repeat(n) + l : l.replace(new RegExp('^\\s{0,' + (-n) + '}'), '')).join('\n') + '\n'; },
      stripIndent: s => { const ls = s.split('\n'); const ind = Math.min(...ls.filter((l, i) => l.trim() || i === ls.length - 1).map(l => l.match(/^\s*/)[0].length)); return ls.map(l => l.slice(Math.min(ind, l.length)).replace(/\s+$/, '')).join('\n'); },
      translateEscapes: s => s.replace(/\\([ntrbfs"'\\])/g, (m, c) => ({ n: '\n', t: '\t', r: '\r', b: '\b', f: '\f', s: ' ', '"': '"', "'": "'", '\\': '\\' }[c])),
      codePoints: s => L.IntStream ? L.IntStream.of(...Array.from(s, c => c.codePointAt(0))) : null,
      lines: s => L.Stream ? L.Stream.fromArray(s.split(/\r?\n/)) : null,
      getClass: s => classOf(C_STRING),
      charValue: s => s,
      describeConstable: s => s,
    };
    const NUMM = {
      intValue: x => rt.$toInt(x), longValue: x => BigInt(x), doubleValue: x => new D(x), floatValue: x => new D(Math.fround(x), true), shortValue: x => (x << 16) >> 16, byteValue: x => (x << 24) >> 24,
      compareTo: (x, o) => { if (o === null || o === undefined) throw mk('NullPointerException'); return x < o ? -1 : x > o ? 1 : 0; }, compare: (x, o) => NUMM.compareTo(x, o),
      equals: (x, o) => x === o, hashCode: x => x, toString: x => '' + x, getClass: x => classOf(C_INT), isNaN: () => false, isInfinite: () => false,
    };
    const LONGM = {
      intValue: x => Number(BigInt.asIntN(32, x)), longValue: x => x, doubleValue: x => new D(Number(x)), floatValue: x => new D(Math.fround(Number(x)), true),
      compareTo: (x, o) => x < o ? -1 : x > o ? 1 : 0, equals: (x, o) => typeof o === 'bigint' && x === o, hashCode: x => hash(x), toString: x => x.toString(), getClass: x => classOf(C_LONG),
    };
    const BOOLM = { booleanValue: x => x, equals: (x, o) => x === o, hashCode: x => hash(x), toString: x => '' + x, compareTo: (x, o) => x === o ? 0 : x ? 1 : -1, getClass: x => classOf(C_BOOL) };
    const ARRM = { clone: a => { const c = a.slice(); c.$t = a.$t; return c; }, equals: (a, o) => a === o, hashCode: a => identityHash(a), toString: a => str(a), getClass: a => ({ getSimpleName: () => (a.$t || 'Object') + '[]', getName: () => str(a).split('@')[0] }) };
    D.prototype.intValue = function () { return satInt(this.v); };
    D.prototype.longValue = function () { return rt.$toLong(this); };
    D.prototype.doubleValue = function () { return new D(this.v); };
    D.prototype.floatValue = function () { return new D(Math.fround(this.v), true); };
    D.prototype.isNaN = function () { return this.v !== this.v; };
    D.prototype.isInfinite = function () { return this.v === Infinity || this.v === -Infinity; };
    D.prototype.compareTo = function (o) { return dblCmp(this.v, toNum(o)); };
    D.prototype.equals = function (o) { return equals(this, o); };
    D.prototype.hashCode = function () { return hash(this); };
    D.prototype.getClass = function () { return classOf(this.f ? C_FLT : C_DBL); };
    Ch.prototype.charValue = function () { return this; };
    Ch.prototype.compareTo = function (o) { return this.c - o.c; };
    Ch.prototype.equals = function (o) { return o instanceof Ch && o.c === this.c; };
    Ch.prototype.hashCode = function () { return this.c; };
    Ch.prototype.getClass = function () { return classOf(C_CH); };
    function inv(o, n, ...a) {
      switch (typeof o) {
        case 'string': { const f = STRM[n]; if (f) return f(o, ...a); break; }
        case 'number': { const f = NUMM[n] || NUMM[n.replace(/\$idx$/, '')]; if (f) return f(o, ...a); break; }
        case 'bigint': { const f = LONGM[n]; if (f) return f(o, ...a); break; }
        case 'boolean': { const f = BOOLM[n]; if (f) return f(o, ...a); break; }
        case 'undefined': throw mk('NullPointerException');
        case 'object':
          if (o === null) throw mk('NullPointerException', `Cannot invoke "${n}()" because value is null`);
          if (o instanceof String) { const f = STRM[n]; if (f) return f(o.valueOf(), ...a); }
          else if (Array.isArray(o)) { const f = ARRM[n]; if (f) return f(o, ...a); }
          break;
      }
      let f = o[n];
      if (typeof f !== 'function' && n.endsWith('$idx')) f = o[n.slice(0, -4)];
      if (typeof f !== 'function') throw mk('RuntimeException', `no method ${n} on ${className(o)}`);
      return f.apply(o, a);
    }
    rt.$inv = inv; rt.$STRM = STRM; rt.$classes = new Map();
    rt.$chk = o => { if (o === null || o === undefined) throw mk('NullPointerException'); return o; };

    /* ---------- lambdas ---------- */
    const lamTarget = Object.create(Function.prototype);
    const lamExtra = {
      reversed() { const f = this; return rt.$lam((a, b) => f(b, a)); },
      thenComparing(k, c) { const f = this; const g = keyCmp(k, c); return rt.$lam((a, b) => { const r = f(a, b); return r !== 0 ? r : g(a, b); }); },
      thenComparingInt(k) { return this.thenComparing(k); }, thenComparingDouble(k) { return this.thenComparing(k); }, thenComparingLong(k) { return this.thenComparing(k); },
      andThen(g) { const f = this; return rt.$lam((...a) => { const r = f(...a); const gg = fnOf(g); return r === undefined ? gg(...a) : gg(r); }); },
      compose(g) { const f = this; return rt.$lam(x => f(fnOf(g)(x))); },
      negate() { const f = this; return rt.$lam((...a) => !f(...a)); },
      and(g) { const f = this; return rt.$lam((...a) => f(...a) && fnOf(g)(...a)); },
      or(g) { const f = this; return rt.$lam((...a) => f(...a) || fnOf(g)(...a)); },
      equals(o) { return this === o; }, hashCode() { return identityHash(this); }, toString() { return str(this); }, getClass() { return classOf(JObject); },
    };
    const lamProto = new Proxy(lamTarget, {
      get(t, k, recv) {
        if (typeof k === 'symbol') return Reflect.get(t, k, recv);
        if (Object.prototype.hasOwnProperty.call(lamExtra, k)) return lamExtra[k];
        if (k === 'length' || k === 'name' || k === 'caller' || k === 'arguments') return Reflect.get(t, k, recv);
        return (...a) => recv(...a);
      },
    });
    const callThrough = f => ({ value: (...a) => f(...a), configurable: true, writable: true });
    rt.$lam = f => { const n = f.length; Object.setPrototypeOf(f, lamProto); Object.defineProperty(f, 'name', callThrough(f)); Object.defineProperty(f, 'length', callThrough(f)); f.$ar = n; return f; };
    // normalise "functional thing" (lambda, or an object implementing the interface method) to a JS function
    function fnOf(f, m) {
      if (typeof f === 'function') return f;
      if (f === null || f === undefined) throw mk('NullPointerException');
      if (m && typeof f[m] === 'function') return (...a) => f[m](...a);
      for (const k of ['apply', 'accept', 'test', 'get', 'run', 'compare', 'call', 'applyAsInt', 'applyAsDouble', 'applyAsLong']) if (typeof f[k] === 'function') return (...a) => f[k](...a);
      throw mk('ClassCastException', 'not a functional interface instance');
    }
    H.fnOf = fnOf;
    function keyCmp(k, c) {
      if (c) { const kf = fnOf(k, 'apply'), cf = fnOf(c, 'compare'); return (a, b) => cf(kf(a), kf(b)); }
      const kf = fnOf(k);
      if (typeof k === 'function' && (k.$ar !== undefined ? k.$ar : k.length) === 2) return k; // already a comparator
      if (k && typeof k !== 'function' && typeof k.compare === 'function') return (a, b) => k.compare(a, b);
      return (a, b) => compare(kf(a), kf(b));
    }
    H.keyCmp = keyCmp;
    // a lambda converted to a user functional interface also gets that interface's default methods
    const lamProtos = new Map();
    rt.$lamI = (f, C) => {
      let pr = lamProtos.get(C);
      if (!pr) {
        const defs = C.$defaults || {};
        pr = new Proxy(lamTarget, { get(t, k, recv) { if (typeof k === 'symbol') return Reflect.get(t, k, recv); if (Object.prototype.hasOwnProperty.call(defs, k)) return defs[k]; if (Object.prototype.hasOwnProperty.call(lamExtra, k)) return lamExtra[k]; if (k === 'length' || k === 'name') return Reflect.get(t, k, recv); return (...a) => recv(...a); } });
        lamProtos.set(C, pr);
      }
      const n = f.length; Object.setPrototypeOf(f, pr); Object.defineProperty(f, 'name', callThrough(f)); Object.defineProperty(f, 'length', callThrough(f)); f.$ar = n; return f;
    };
    rt.$mref = (recv, name, kind) => {
      // kind: 'bound' (obj::method), 'type' (Type::method -> static or unbound instance), 'new'
      if (kind === 'bound') return rt.$lam((...a) => inv(recv, name, ...a));
      // recv is a library class or user class
      const hasStatic = () => { for (let c = recv; c && c !== Function.prototype; c = Object.getPrototypeOf(c)) if (Object.prototype.hasOwnProperty.call(c, name)) return typeof c[name] === 'function'; return false; };
      return rt.$lam((...a) => {
        if (hasStatic()) return recv[name](...a);
        return inv(a[0], name, ...a.slice(1));
      });
    };

    /* ---------- type tests / casts ---------- */
    const primTests = {
      String: x => typeof x === 'string' || x instanceof String, Integer: x => typeof x === 'number', Long: x => typeof x === 'bigint', Double: x => x instanceof D && !x.f, Float: x => x instanceof D && x.f,
      Character: x => x instanceof Ch, Boolean: x => typeof x === 'boolean', Short: x => false, Byte: x => false,
      Number: x => typeof x === 'number' || typeof x === 'bigint' || x instanceof D, CharSequence: x => typeof x === 'string' || x instanceof String || (x && x.constructor && x.constructor.$jn === 'StringBuilder'),
      Comparable: x => typeof x === 'string' || typeof x === 'number' || typeof x === 'bigint' || typeof x === 'boolean' || x instanceof String || x instanceof D || x instanceof Ch || (x && typeof x.compareTo === 'function'),
      Object: x => x !== null && x !== undefined,
    };
    rt.$inst = (x, T) => {
      if (x === null || x === undefined) return false;
      const pt = T && T.$jn && primTests[T.$jn];
      if (T === L.Object) return true;
      if (pt && T.$prim) return pt(x);
      if (typeof x === 'function') return !!(T && T.$isIface);
      if (typeof x !== 'object') return false;
      if (x instanceof T) return true;
      const s = x.constructor && x.constructor.$supers;
      if (s && s.has(T)) return true;
      if (Array.isArray(x)) return false;
      if (x instanceof D || x instanceof Ch || x instanceof String) return !!(pt && pt(x));
      return false;
    };
    rt.$cast = (x, T) => {
      if (x === null || x === undefined || rt.$inst(x, T)) return x;
      throw mk('ClassCastException', cceMsg(className(x), T.$jfq || T.$jn || T.name));
    };
    // declare the interfaces / supers of a user class and copy default methods
    rt.$castArr = (x, elem) => {
      if (x === null || x === undefined) return x;
      const prim = /^(int|long|double|float|char|byte|short|boolean)$/;
      const sig = t => { const m = { int: 'I', long: 'J', double: 'D', float: 'F', char: 'C', byte: 'B', short: 'S', boolean: 'Z' }; let d = 0; while (t.endsWith('[]')) { d++; t = t.slice(0, -2); } return '['.repeat(d + 1) + (m[t] || 'L' + (L[t] ? L[t].$jfq : t) + ';'); };
      let ok = Array.isArray(x);
      if (ok && x.$t !== undefined) { if (prim.test(elem) || prim.test(x.$t)) ok = x.$t === elem; else ok = x.$t === elem || elem === 'Object' || (L[x.$t] && L[elem] && L[x.$t].prototype instanceof L[elem]) || !L[elem]; }
      if (!ok) { const a = Array.isArray(x) ? sig((x.$t || 'Object') + '[]') : className(x); throw mk('ClassCastException', cceMsg(a, sig(elem + '[]'))); }
      return x;
    };
    rt.$defc = (C, name, ifs, opts = {}) => {
      C.$jn = name; C.$jfq = opts.bin || name; C.$cn = opts.canon || name; if (opts.iface) C.$isIface = true; if (name) rt.$classes.set(name, C);
      const set = new Set([C]);
      const parent = Object.getPrototypeOf(C);
      if (parent && parent.$supers) parent.$supers.forEach(x => set.add(x));
      ifs.forEach(i => { set.add(i); if (i.$supers) i.$supers.forEach(x => set.add(x)); });
      C.$supers = set;
      // default methods (user interfaces and library interfaces like Comparator)
      if (opts.iface) { const merged = {}; ifs.forEach(i => Object.assign(merged, i.$defaults || {})); C.$defaults = Object.assign(merged, C.$defaults || {}); }
      ifs.forEach(i => { if (i.$defaults) for (const k of Object.keys(i.$defaults)) if (!(k in C.prototype) || C.prototype[k] === JObject.prototype[k]) Object.defineProperty(C.prototype, k, { value: i.$defaults[k], writable: true, configurable: true }); });
      if (!opts.iface) C.$abstractCheck = true;
      return C;
    };

    /* ---------- string formatting (java.util.Formatter subset) ---------- */
    function groupDigits(s) { return s.replace(/\B(?=(\d{3})+(?!\d))/g, ','); }
    // round the shortest decimal expansion of v to prec digits, half-up (as java.util.Formatter does)
    function fixedHalfUp(v, prec) {
      if (v !== v) return 'NaN'; if (v === Infinity) return 'Infinity'; if (v === -Infinity) return '-Infinity';
      const neg = v < 0 || (v === 0 && 1 / v < 0); const a = Math.abs(v);
      let s;
      if (a >= 1e21) { s = BigInt(a).toString(); }
      else {
        let [m, e] = a.toExponential().split('e'); e = +e;
        const digs = m.replace('.', ''); // digits, value = 0.d1d2... * 10^(e+1)
        const pointPos = e + 1;
        let intPart, frac;
        if (pointPos <= 0) { intPart = '0'; frac = '0'.repeat(-pointPos) + digs; }
        else if (pointPos >= digs.length) { intPart = digs + '0'.repeat(pointPos - digs.length); frac = ''; }
        else { intPart = digs.slice(0, pointPos); frac = digs.slice(pointPos); }
        if (frac.length > prec) {
          const roundUp = frac.charCodeAt(prec) >= 53;
          frac = frac.slice(0, prec);
          if (roundUp) {
            let n = BigInt(intPart + frac) + 1n; let t = n.toString().padStart(intPart.length + prec, '0');
            intPart = t.slice(0, t.length - prec) || '0'; frac = prec ? t.slice(t.length - prec) : '';
          }
        } else frac = frac.padEnd(prec, '0');
        s = prec ? intPart + '.' + frac : intPart;
        return (neg && /[1-9]/.test(s) ? '-' : neg ? '-' : '') + s;
      }
      return (neg ? '-' : '') + s + (prec ? '.' + '0'.repeat(prec) : '');
    }
    // scientific notation: shortest decimal digits of |v| rounded half-up to `prec` fraction digits -> [mantissa, exponent]
    function sciHalfUp(a, prec) {
      if (a === 0) return [(0).toFixed(prec), 0];
      let [m, e] = a.toExponential().split('e'); e = +e;
      let digs = m.replace('.', '');
      if (digs.length > prec + 1) {
        const up = digs.charCodeAt(prec + 1) >= 53; digs = digs.slice(0, prec + 1);
        if (up) { const n = (BigInt(digs) + 1n).toString(); if (n.length > digs.length) { e++; digs = n.slice(0, prec + 1); } else digs = n; }
      } else digs = digs.padEnd(prec + 1, '0');
      return [digs[0] + (prec ? '.' + digs.slice(1) : ''), e];
    }
    H.fixedHalfUp = fixedHalfUp;
    function fmt(f, args) {
      if (f === null || f === undefined) throw mk('NullPointerException');
      f = str(f);
      let ai = 0, outS = '';
      const re = /%(\d+\$)?([-#+ 0,(<]*)?(\d+)?(\.\d+)?([a-zA-Z%])/g;
      let last = 0, m;
      while ((m = re.exec(f)) !== null) {
        outS += f.slice(last, m.index); last = re.lastIndex;
        let [, argIdx, flags = '', width, prec, conv] = m;
        flags = flags || '';
        if (conv === 'n') { outS += '\n'; continue; }
        if (conv === '%') { outS += pad('%', width, flags); continue; }
        let arg;
        if (argIdx) arg = args[parseInt(argIdx) - 1]; else if (flags.includes('<')) arg = args[ai - 1]; else { if (ai >= args.length) throwEx('MissingFormatArgumentException', `Format specifier '${m[0]}'`); arg = args[ai++]; }
        const p = prec ? +prec.slice(1) : null;
        const lc = conv.toLowerCase();
        let s;
        const bad = () => throwEx('IllegalFormatConversionException', `${conv} != ${className(arg)}`);
        switch (lc) {
          case 'd': {
            if (arg === null || arg === undefined) { s = 'null'; break; }
            if (!(typeof arg === 'number' || typeof arg === 'bigint' || (arg && arg.$big))) bad();
            let neg = arg < 0; let digits = (neg ? -BigInt(arg) : BigInt(arg)).toString();
            if (flags.includes(',')) digits = groupDigits(digits);
            let sign = neg ? (flags.includes('(') ? '(' : '-') : flags.includes('+') ? '+' : flags.includes(' ') ? ' ' : '';
            let tail = neg && flags.includes('(') ? ')' : '';
            if (flags.includes('0') && width) digits = digits.padStart(+width - sign.length - tail.length, '0');
            s = sign + digits + tail; break;
          }
          case 'f': case 'e': case 'g': {
            if (arg === null || arg === undefined) { s = 'null'; break; }
            if (!(arg instanceof D)) bad();
            const v = arg.v;
            let body;
            if (lc === 'f') body = fixedHalfUp(Math.abs(v), p === null ? 6 : p);
            else if (lc === 'e') { const pp = p === null ? 6 : p; const [mm, ee] = sciHalfUp(Math.abs(v), pp); body = mm + 'e' + (ee < 0 ? '-' : '+') + String(Math.abs(ee)).padStart(2, '0'); }
            else { const pp = p === null ? 6 : p === 0 ? 1 : p; const av = Math.abs(v); const [, ee0] = sciHalfUp(av, pp - 1); if (av !== 0 && (av < 1e-4 || ee0 >= pp)) { const [mm, ee] = sciHalfUp(av, pp - 1); body = mm + 'e' + (ee < 0 ? '-' : '+') + String(Math.abs(ee)).padStart(2, '0'); } else body = fixedHalfUp(av, Math.max(0, pp - 1 - (av === 0 ? 0 : ee0))); }
            if (v !== v) { s = 'NaN'; break; }
            if (v === Infinity || v === -Infinity) { s = v < 0 ? '-Infinity' : (flags.includes('+') ? '+Infinity' : 'Infinity'); break; }
            if (flags.includes(',')) { const [ip, fp] = body.split('.'); body = groupDigits(ip) + (fp !== undefined ? '.' + fp : ''); }
            const neg = v < 0 || (v === 0 && 1 / v < 0);
            let sign = neg ? (flags.includes('(') ? '(' : '-') : flags.includes('+') ? '+' : flags.includes(' ') ? ' ' : '';
            let tail = neg && flags.includes('(') ? ')' : '';
            if (flags.includes('0') && width) body = body.padStart(+width - sign.length - tail.length, '0');
            s = sign + body + tail; break;
          }
          case 's': s = arg !== null && arg !== undefined && typeof arg.formatTo === 'function' ? str(arg) : str(arg); if (p !== null) s = s.slice(0, p); break;
          case 'c': if (arg === null || arg === undefined) { s = 'null'; break; } s = arg instanceof Ch ? arg.toString() : typeof arg === 'number' ? String.fromCodePoint(arg) : bad(); break;
          case 'b': s = arg === null || arg === undefined ? 'false' : typeof arg === 'boolean' ? String(arg) : 'true'; break;
          case 'x': case 'o': { if (arg === null || arg === undefined) { s = 'null'; break; } if (!(typeof arg === 'number' || typeof arg === 'bigint')) bad(); const base = lc === 'x' ? 16 : 8; let n = typeof arg === 'number' ? (arg < 0 ? BigInt.asUintN(32, BigInt(arg)) : BigInt(arg)) : (arg < 0n ? BigInt.asUintN(64, arg) : arg); s = n.toString(base); if (flags.includes('#')) s = (base === 16 ? '0x' : '0') + s; if (flags.includes('0') && width) s = s.padStart(+width, '0'); break; }
          case 'h': s = (hash(arg) >>> 0).toString(16); break;
          case 't': s = str(arg); break;
          default: throwEx('UnknownFormatConversionException', `Conversion = '${conv}'`);
        }
        if (conv !== lc && conv !== '%') s = s.toUpperCase();
        outS += pad(s, width, flags);
      }
      outS += f.slice(last);
      return outS;
    }
    const pad = (s, width, flags) => { if (!width || s.length >= +width) return s; return flags.includes('-') ? s.padEnd(+width) : s.padStart(+width); };
    rt.$fmt = fmt; H.fmt = fmt;

    /* ---------- java.lang statics ---------- */
    class JString extends JObject {
      static valueOf(x, off, cnt) { if (Array.isArray(x) && x.$t === 'char') return (off !== undefined ? x.slice(off, off + cnt) : x).map(c => c.toString()).join(''); return str(x); }
      static copyValueOf(x) { return JString.valueOf(x); }
      static format(f, ...a) { if (f && f.$isLocale) { f = a.shift(); } return fmt(f, a.length === 1 && Array.isArray(a[0]) && !PRIMARR.test(a[0].$t || '') ? a[0] : a); }
      static join(sep, ...parts) { const items = parts.length === 1 && parts[0] !== null && typeof parts[0] === 'object' && !(parts[0] instanceof String) ? Array.from(rt.$iter(parts[0])) : parts; return items.map(str).join(str(sep)); }
      static compare(a, b) { return strCmp(a, b); }
      static $make(...a) { // new String(...)
        if (a.length === 0) return new String('');
        const x = a[0];
        if (Array.isArray(x)) return new String(JString.valueOf(x, a[1], a[2]));
        return new String(str(x));
      }
    }
    JString.$prim = true; defLib(JString, 'String', 'java.lang.String'); JString.$prim = true;
    JString.CASE_INSENSITIVE_ORDER = rt.$lam((a, b) => strCmp(a.toLowerCase(), b.toLowerCase()));
    L.String = JString;
    const parseIntStrict = (s, radix = 10, lo = MINI, hi = MAXI) => {
      if (s === null || s === undefined) throwEx('NumberFormatException', 'Cannot parse null string');
      s = str(s);
      const digits = '0123456789abcdefghijklmnopqrstuvwxyz'.slice(0, radix);
      const re = new RegExp(`^[+-]?[${digits}]+$`, 'i');
      if (!re.test(s)) throwEx('NumberFormatException', `For input string: "${s}"` + (radix !== 10 ? ` under radix ${radix}` : ''));
      const neg = s[0] === '-'; let v = 0n; const t = s.replace(/^[+-]/, '').toLowerCase();
      for (const ch of t) v = v * BigInt(radix) + BigInt(digits.indexOf(ch));
      if (neg) v = -v;
      if (v < BigInt(lo) || v > BigInt(hi)) throwEx('NumberFormatException', `For input string: "${s}"` + (radix !== 10 ? ` under radix ${radix}` : ''));
      return v;
    };
    class JInteger extends JObject {
      static parseInt(s, r) { return Number(parseIntStrict(s, r === undefined ? 10 : r)); }
      static valueOf(x, r) { return x === null ? JInteger.parseInt(null) : typeof x === 'string' || x instanceof String ? JInteger.parseInt(x, r) : x instanceof Ch ? x.c : x; }
      static toString(x, r) { return r === undefined ? '' + x : x.toString(r); }
      static compare(a, b) { return a < b ? -1 : a > b ? 1 : 0; }
      static max(a, b) { return a > b ? a : b; } static min(a, b) { return a < b ? a : b; } static sum(a, b) { return (a + b) | 0; }
      static signum(a) { return a > 0 ? 1 : a < 0 ? -1 : 0; } static abs(a) { return Math.abs(a) | 0; }
      static toBinaryString(x) { return (x >>> 0).toString(2); } static toHexString(x) { return (x >>> 0).toString(16); } static toOctalString(x) { return (x >>> 0).toString(8); }
      static decode(s) { s = str(s); const m = /^([+-]?)(0[xX]|#|0(?=[0-7]))?(.+)$/.exec(s); if (!m) throwEx('NumberFormatException', 'Zero length string'); const radix = !m[2] ? 10 : /^0[xX]|#/.test(m[2]) ? 16 : 8; return Number(parseIntStrict((m[1] || '') + m[3], radix)); }
      static reverseBytes(x) { return ((x & 0xff) << 24) | ((x & 0xff00) << 8) | ((x >>> 8) & 0xff00) | (x >>> 24); }
      static compareUnsigned(a, b) { return JInteger.compare(a >>> 0, b >>> 0); } static remainderUnsigned(a, b) { if (b === 0) throwEx('ArithmeticException', '/ by zero'); return ((a >>> 0) % (b >>> 0)) | 0; } static toUnsignedString(x) { return String(x >>> 0); } static toUnsignedLong(x) { return BigInt(x >>> 0); } static rotateLeft(x, d) { return (x << d) | (x >>> (32 - d)); } static rotateRight(x, d) { return (x >>> d) | (x << (32 - d)); } static lowestOneBit(x) { return x & -x; }
      static bitCount(x) { let c = 0; x = x >>> 0; while (x) { c += x & 1; x >>>= 1; } return c; }
      static hashCode(x) { return x; } static reverse(x) { let r = 0; for (let i = 0; i < 32; i++) { r = (r << 1) | (x & 1); x >>>= 1; } return r; }
      static divideUnsigned(a, b) { if (b === 0) throwEx('ArithmeticException', '/ by zero'); return Math.floor((a >>> 0) / (b >>> 0)) | 0; } static parseUnsignedInt(s) { return Number(parseIntStrict(s, 10, 0, 4294967295)) | 0; }
      static numberOfTrailingZeros(x) { return x === 0 ? 32 : 31 - Math.clz32(x & -x); } static numberOfLeadingZeros(x) { return Math.clz32(x); } static highestOneBit(x) { return x === 0 ? 0 : (1 << (31 - Math.clz32(x))) | 0; }
    }
    JInteger.MAX_VALUE = MAXI; JInteger.MIN_VALUE = MINI; JInteger.MIN_RADIX = 2; JInteger.MAX_RADIX = 36; JInteger.SIZE = 32; JInteger.BYTES = 4; JInteger.$prim = true;
    defLib(JInteger, 'Integer', 'java.lang.Integer'); JInteger.$prim = true;
    const mkBoxClass = (name, extra) => { const c = ({ [name]: class extends JObject { } })[name]; c.$prim = true; defLib(c, name, 'java.lang.' + name); c.$prim = true; Object.assign(c, extra); return c; };
    mkBoxClass('Short', { BYTES: 2, SIZE: 16, toUnsignedInt: x => x & 0xffff, hashCode: x => x, MAX_VALUE: 32767, MIN_VALUE: -32768, parseShort: s => Number(parseIntStrict(s, 10, -32768, 32767)), valueOf: x => typeof x === 'string' ? Number(parseIntStrict(x, 10, -32768, 32767)) : x, toString: x => '' + x, compare: (a, b) => a - b });
    mkBoxClass('Byte', { BYTES: 1, SIZE: 8, toUnsignedInt: x => x & 0xff, hashCode: x => x, MAX_VALUE: 127, MIN_VALUE: -128, parseByte: s => Number(parseIntStrict(s, 10, -128, 127)), valueOf: x => typeof x === 'string' ? Number(parseIntStrict(x, 10, -128, 127)) : x, toString: x => '' + x, compare: (a, b) => a - b });
    mkBoxClass('Long', {
      MAX_VALUE: 9223372036854775807n, MIN_VALUE: -9223372036854775808n, SIZE: 64, BYTES: 8, rotateLeft: (x, d) => { d = ((d % 64) + 64) % 64; const u = BigInt.asUintN(64, x); return BigInt.asIntN(64, (u << BigInt(d)) | (u >> BigInt(64 - d))); }, rotateRight: (x, d) => { d = ((d % 64) + 64) % 64; const u = BigInt.asUintN(64, x); return BigInt.asIntN(64, (u >> BigInt(d)) | (u << BigInt(64 - d))); },
      parseLong: (s, r) => parseIntStrict(s, r === undefined ? 10 : r, -9223372036854775808n, 9223372036854775807n),
      valueOf: x => typeof x === 'string' || x instanceof String ? L.Long.parseLong(x) : rt.$toLong(x), toString: (x, r) => r === undefined ? x.toString() : x.toString(r),
      compare: (a, b) => a < b ? -1 : a > b ? 1 : 0, max: (a, b) => a > b ? a : b, min: (a, b) => a < b ? a : b, sum: (a, b) => BigInt.asIntN(64, a + b), signum: a => a > 0n ? 1 : a < 0n ? -1 : 0, abs: a => a < 0n ? -a : a, hashCode: x => hash(x),
      divideUnsigned: (a, b) => { if (b === 0n || b === 0) throwEx('ArithmeticException', '/ by zero'); return BigInt.asIntN(64, BigInt.asUintN(64, BigInt(a)) / BigInt.asUintN(64, BigInt(b))); }, remainderUnsigned: (a, b) => { if (b === 0n || b === 0) throwEx('ArithmeticException', '/ by zero'); return BigInt.asIntN(64, BigInt.asUintN(64, BigInt(a)) % BigInt.asUintN(64, BigInt(b))); }, compareUnsigned: (a, b) => { const x = BigInt.asUintN(64, a), y = BigInt.asUintN(64, b); return x < y ? -1 : x > y ? 1 : 0; }, toUnsignedString: x => BigInt.asUintN(64, x).toString(), toOctalString: x => BigInt.asUintN(64, x).toString(8), numberOfTrailingZeros: x => { if (x === 0n) return 64; let n = 0; while (!((x >> BigInt(n)) & 1n)) n++; return n; }, numberOfLeadingZeros: x => { const u = BigInt.asUintN(64, x); return u === 0n ? 64 : 64 - u.toString(2).length; }, reverse: x => { let r = 0n, u = BigInt.asUintN(64, x); for (let i = 0; i < 64; i++) { r = (r << 1n) | (u & 1n); u >>= 1n; } return BigInt.asIntN(64, r); }, highestOneBit: x => x === 0n ? 0n : BigInt.asIntN(64, 1n << BigInt(BigInt.asUintN(64, x).toString(2).length - 1)),
      toBinaryString: x => BigInt.asUintN(64, x).toString(2), toHexString: x => BigInt.asUintN(64, x).toString(16), bitCount: x => BigInt.asUintN(64, x).toString(2).replace(/0/g, '').length,
    });
    const parseDbl = s => {
      if (s === null || s === undefined) throw mk('NullPointerException');
      const t = str(s).trim();
      if (t === '') throwEx('NumberFormatException', 'empty String');
      if (t === 'NaN') return NaN; if (/^[+]?Infinity$/.test(t)) return Infinity; if (t === '-Infinity') return -Infinity;
      if (!/^[+-]?(\d+\.?\d*([eE][+-]?\d+)?|\.\d+([eE][+-]?\d+)?)[dDfF]?$/.test(t)) { if ((t.match(/\./g) || []).length > 1 && /^[\d.eE+-]+$/.test(t)) throwEx('NumberFormatException', 'multiple points'); throwEx('NumberFormatException', `For input string: "${str(s)}"`); }
      return parseFloat(t);
    };
    mkBoxClass('Double', { BYTES: 8, SIZE: 64,
      MAX_VALUE: new D(Number.MAX_VALUE), MIN_VALUE: new D(5e-324), MIN_NORMAL: new D(2.2250738585072014e-308), MAX_EXPONENT: 1023, MIN_EXPONENT: -1022, doubleToLongBits: x => { f64[0] = toNum(x); return BigInt.asIntN(64, (BigInt(u32[1]) << 32n) | BigInt(u32[0])); }, doubleToRawLongBits: x => { f64[0] = toNum(x); return BigInt.asIntN(64, (BigInt(u32[1]) << 32n) | BigInt(u32[0])); }, longBitsToDouble: b => { const u = BigInt.asUintN(64, b); u32[0] = Number(u & 0xffffffffn); u32[1] = Number(u >> 32n); return new D(f64[0]); }, POSITIVE_INFINITY: new D(Infinity), NEGATIVE_INFINITY: new D(-Infinity), NaN: new D(NaN),
      parseDouble: s => new D(parseDbl(s)), valueOf: x => typeof x === 'string' || x instanceof String ? new D(parseDbl(x)) : rt.$toDouble(x),
      toString: x => dstr(toNum(x)), compare: (a, b) => dblCmp(toNum(a), toNum(b)), isNaN: x => toNum(x) !== toNum(x), isInfinite: x => Math.abs(toNum(x)) === Infinity, isFinite: x => isFinite(toNum(x)),
      max: (a, b) => new D(Math.max(toNum(a), toNum(b))), min: (a, b) => new D(Math.min(toNum(a), toNum(b))), sum: (a, b) => new D(toNum(a) + toNum(b)), hashCode: x => hash(x),
    });
    mkBoxClass('Float', {
      MAX_VALUE: new D(3.4028234663852886e38, true), MIN_VALUE: new D(1.401298464324817e-45, true), POSITIVE_INFINITY: new D(Infinity, true), NEGATIVE_INFINITY: new D(-Infinity, true), NaN: new D(NaN, true), BYTES: 4, SIZE: 32,
      floatToIntBits: x => { const b = new Float32Array(1); b[0] = toNum(x); return new Int32Array(b.buffer)[0]; }, floatToRawIntBits: x => { const b = new Float32Array(1); b[0] = toNum(x); return new Int32Array(b.buffer)[0]; }, intBitsToFloat: n => { const b = new Int32Array(1); b[0] = n; return new D(new Float32Array(b.buffer)[0], true); }, MIN_NORMAL: new D(1.1754943508222875e-38, true),
      parseFloat: s => new D(Math.fround(parseDbl(s)), true), valueOf: x => typeof x === 'string' || x instanceof String ? new D(Math.fround(parseDbl(x)), true) : rt.$toFloat(x),
      toString: x => dstr(toNum(x), true), compare: (a, b) => dblCmp(toNum(a), toNum(b)), isNaN: x => toNum(x) !== toNum(x), sum: (a, b) => new D(Math.fround(toNum(a) + toNum(b)), true), max: (a, b) => new D(Math.max(toNum(a), toNum(b)), true), min: (a, b) => new D(Math.min(toNum(a), toNum(b)), true),
    });
    mkBoxClass('Boolean', {
      TRUE: true, FALSE: false, parseBoolean: s => s !== null && s !== undefined && str(s).toLowerCase() === 'true', valueOf: x => typeof x === 'boolean' ? x : (x !== null && x !== undefined && str(x).toLowerCase() === 'true'),
      toString: x => '' + x, compare: (a, b) => a === b ? 0 : a ? 1 : -1, logicalAnd: (a, b) => a && b, logicalOr: (a, b) => a || b, logicalXor: (a, b) => a !== b, hashCode: x => x ? 1231 : 1237,
    });
    const cc = x => x instanceof Ch ? x.c : x;
    mkBoxClass('Character', {
      MAX_VALUE: Ch.of(0xFFFF), MIN_VALUE: Ch.of(0), MIN_RADIX: 2, MAX_RADIX: 36, BYTES: 2, SIZE: 16, isJavaIdentifierStart: x => /[\p{L}_$]/u.test(String.fromCodePoint(cc(x))), isJavaIdentifierPart: x => /[\p{L}\p{Nd}_$]/u.test(String.fromCodePoint(cc(x))), isSurrogate: x => cc(x) >= 0xD800 && cc(x) <= 0xDFFF, isTitleCase: () => false, isDefined: () => true, charCount: x => x >= 0x10000 ? 2 : 1, isEmoji: () => false,
      isDigit: x => /\p{Nd}/u.test(String.fromCodePoint(cc(x))), isLetter: x => /\p{L}/u.test(String.fromCodePoint(cc(x))), isLetterOrDigit: x => /[\p{L}\p{Nd}]/u.test(String.fromCodePoint(cc(x))),
      isAlphabetic: x => /\p{L}/u.test(String.fromCodePoint(cc(x))), isUpperCase: x => /\p{Lu}/u.test(String.fromCodePoint(cc(x))), isLowerCase: x => /\p{Ll}/u.test(String.fromCodePoint(cc(x))),
      isWhitespace: x => /[ \t\n\r\f\v\u001c-\u001f]/.test(String.fromCodePoint(cc(x))) || (cc(x) > 127 && /\s/.test(String.fromCodePoint(cc(x))) && cc(x) !== 0xa0), isSpaceChar: x => /[   - ]/.test(String.fromCodePoint(cc(x))),
      toUpperCase: x => x instanceof Ch ? Ch.of(x.toString().toUpperCase().charCodeAt(0)) : String.fromCodePoint(x).toUpperCase().codePointAt(0),
      toLowerCase: x => x instanceof Ch ? Ch.of(x.toString().toLowerCase().charCodeAt(0)) : String.fromCodePoint(x).toLowerCase().codePointAt(0),
      toString: x => x instanceof Ch ? x.toString() : String.fromCodePoint(x), valueOf: x => x instanceof Ch ? x : Ch.of(x), compare: (a, b) => cc(a) - cc(b), hashCode: x => cc(x),
      getNumericValue: x => { const c = String.fromCodePoint(cc(x)); return /[0-9]/.test(c) ? +c : /[a-zA-Z]/.test(c) ? c.toLowerCase().charCodeAt(0) - 87 : -1; },
      digit: (x, r) => { const c = String.fromCodePoint(cc(x)).toLowerCase(); const v = /[0-9]/.test(c) ? +c : /[a-z]/.test(c) ? c.charCodeAt(0) - 87 : -1; return v < r ? v : -1; },
      forDigit: (d, r) => Ch.of(d < 10 ? 48 + d : 87 + d), isISOControl: x => cc(x) < 32 || (cc(x) >= 127 && cc(x) < 160), toChars: x => arr('char', [Ch.of(x)]), reverseBytes: x => x,
    });
    class JNumber extends JObject { } JNumber.$prim = true; defLib(JNumber, 'Number', 'java.lang.Number'); JNumber.$prim = true;
    iface('Comparable', 'java.lang.Comparable').$prim = true; iface('CharSequence', 'java.lang.CharSequence').$prim = true;
    L.Comparable.$defaults = {}; iface('Runnable', 'java.lang.Runnable'); iface('Cloneable', 'java.lang.Cloneable'); iface('AutoCloseable', 'java.lang.AutoCloseable'); iface('Closeable', 'java.io.Closeable'); iface('Serializable', 'java.io.Serializable');
    iface('Iterable', 'java.lang.Iterable');
    L.Iterable.$defaults = { forEach(f) { const g = fnOf(f, 'accept'); for (const x of rt.$iter(this)) g(x); } };
    L.Void = pseudoClass('Void', 'java.lang.Void');

    class JMath extends JObject { }
    const dd = f => (x, y) => y === undefined ? new D(f(x instanceof D ? x.v : toNum(x))) : new D(f(toNum(x), toNum(y)));
    const intOrLong = (a, b, f, fb) => (typeof a === 'bigint' || typeof b === 'bigint') ? fb(BigInt(toNum2(a)), BigInt(toNum2(b))) : (a instanceof D || b instanceof D) ? new D(f(toNum(a), toNum(b)), (a instanceof D ? a.f : true) && (b instanceof D ? b.f : true) && ((a instanceof D) || (b instanceof D))) : f(toNum(a), toNum(b));
    Object.assign(JMath, {
      PI: new D(Math.PI), E: new D(Math.E),
      abs: a => typeof a === 'number' ? (a < 0 ? (-a) | 0 : a) : typeof a === 'bigint' ? (a < 0n ? BigInt.asIntN(64, -a) : a) : new D(Math.abs(a.v), a.f),
      max: (a, b) => intOrLong(a, b, Math.max, (x, y) => x > y ? x : y), min: (a, b) => intOrLong(a, b, Math.min, (x, y) => x < y ? x : y),
      sqrt: dd(Math.sqrt), cbrt: dd(Math.cbrt), pow: (a, b) => new D(Math.pow(toNum(a), toNum(b))), exp: dd(Math.exp), log: dd(Math.log), log10: dd(Math.log10), log1p: dd(Math.log1p), expm1: dd(Math.expm1),
      sin: dd(Math.sin), cos: dd(Math.cos), tan: dd(Math.tan), asin: dd(Math.asin), acos: dd(Math.acos), atan: dd(Math.atan), atan2: dd(Math.atan2), sinh: dd(Math.sinh), cosh: dd(Math.cosh), tanh: dd(Math.tanh), hypot: dd(Math.hypot),
      floor: dd(Math.floor), ceil: dd(Math.ceil), rint: a => { const v = toNum(a); const r = Math.round(v); return new D(Math.abs(v % 1) === 0.5 ? 2 * Math.round(v / 2) : r); },
      round: a => a instanceof D && a.f ? rt.$toInt(new D(Math.floor(a.v + 0.5))) : rt.$toLong(new D(Math.floor(toNum(a) + 0.5))),
      signum: dd(Math.sign), toRadians: a => new D(toNum(a) / 180 * Math.PI), toDegrees: a => new D(toNum(a) * 180 / Math.PI), random: () => new D(Math.random()),
      floorDiv: (a, b) => { if (toNum(b) == 0) throwEx('ArithmeticException', '/ by zero'); return typeof a === 'bigint' || typeof b === 'bigint' ? (() => { const x = BigInt(toNum2(a)), y = BigInt(toNum2(b)); let q = x / y; if ((x % y !== 0n) && ((x < 0n) !== (y < 0n))) q -= 1n; return q; })() : Math.floor(a / b) | 0; },
      floorMod: (a, b) => { if (toNum(b) == 0) throwEx('ArithmeticException', '/ by zero'); if (typeof a === 'bigint' || typeof b === 'bigint') { const x = BigInt(toNum2(a)), y = BigInt(toNum2(b)); return ((x % y) + y) % y; } return ((a % b) + b) % b; },
      addExact: (a, b) => exact(a, b, '+'), subtractExact: (a, b) => exact(a, b, '-'), multiplyExact: (a, b) => exact(a, b, '*'),
      divideExact: (a, b) => { if (b === 0) throwEx('ArithmeticException', '/ by zero'); if (a === MINI && b === -1) throwEx('ArithmeticException', 'integer overflow'); return (a / b) | 0; },
      absExact: a => { if (a === MINI) throwEx('ArithmeticException', 'Overflow to represent absolute value of Integer.MIN_VALUE'); return Math.abs(a); },
      negateExact: a => exact(0, a, '-'), incrementExact: a => exact(a, 1, '+'), decrementExact: a => exact(a, 1, '-'),
      toIntExact: a => { if (a > BigInt(MAXI) || a < BigInt(MINI)) throwEx('ArithmeticException', 'integer overflow'); return Number(a); },
      clamp: (v, lo, hi) => v < lo ? lo : v > hi ? hi : v, ulp: a => new D(Math.abs(toNum(a)) * 2.220446049250313e-16),
    });
    function exact(a, b, op) {
      if (typeof a === 'bigint' || typeof b === 'bigint') { const x = BigInt(toNum2(a)), y = BigInt(toNum2(b)); const r = op === '+' ? x + y : op === '-' ? x - y : x * y; if (r > 9223372036854775807n || r < -9223372036854775808n) throwEx('ArithmeticException', 'long overflow'); return r; }
      const r = op === '+' ? a + b : op === '-' ? a - b : a * b; if (r > MAXI || r < MINI) throwEx('ArithmeticException', 'integer overflow'); return r;
    }
    defLib(JMath, 'Math', 'java.lang.Math'); L.StrictMath = JMath;

    class JSystem extends JObject { }
    JSystem.out = new PrintStream(false); JSystem.err = new PrintStream(true); JSystem.in = { $stdin: true };
    JSystem.exit = c => { throw new ExitSignal(c); };
    JSystem.currentTimeMillis = () => BigInt(Date.now());
    JSystem.nanoTime = () => BigInt(Math.round((typeof performance !== 'undefined' ? performance.now() : Date.now()) * 1e6));
    JSystem.lineSeparator = () => '\n';
    JSystem.getProperty = k => ({ 'line.separator': '\n', 'java.version': '21', 'os.name': 'Linux', 'user.name': 'learner', 'file.separator': '/' }[k] ?? null);
    JSystem.getenv = () => null; JSystem.gc = () => {};
    JSystem.identityHashCode = x => x === null ? 0 : (typeof x === 'object' || typeof x === 'function') ? identityHash(x) : hash(x);
    JSystem.arraycopy = (src, sp, dst, dp, n) => {
      if (src === null || dst === null || src === undefined || dst === undefined) throw mk('NullPointerException');
      const nm = a => (a.$t && /^(int|long|double|float|char|byte|short|boolean)$/.test(a.$t) ? a.$t : 'object array') + '[' + a.length + ']';
      if (!Array.isArray(src)) throwEx('ArrayStoreException', 'arraycopy: source type ' + className(src) + ' is not an array');
      if (!Array.isArray(dst)) throwEx('ArrayStoreException', 'arraycopy: destination type ' + className(dst) + ' is not an array');
      if (sp < 0) throwEx('ArrayIndexOutOfBoundsException', `arraycopy: source index ${sp} out of bounds for ${nm(src)}`);
      if (dp < 0) throwEx('ArrayIndexOutOfBoundsException', `arraycopy: destination index ${dp} out of bounds for ${nm(dst)}`);
      if (n < 0) throwEx('ArrayIndexOutOfBoundsException', `arraycopy: length ${n} is negative`);
      if (sp + n > src.length) throwEx('ArrayIndexOutOfBoundsException', `arraycopy: last source index ${sp + n} out of bounds for ${nm(src)}`);
      if (dp + n > dst.length) throwEx('ArrayIndexOutOfBoundsException', `arraycopy: last destination index ${dp + n} out of bounds for ${nm(dst)}`);
      const tmp = src.slice(sp, sp + n); for (let i = 0; i < n; i++) dst[dp + i] = tmp[i];
    };
    defLib(JSystem, 'System', 'java.lang.System');

    class Runtime extends JObject { static getRuntime() { return new Runtime(); } availableProcessors() { return 4; } totalMemory() { return 268435456n; } freeMemory() { return 134217728n; } maxMemory() { return 4294967296n; } gc() {} }
    defLib(Runtime, 'Runtime', 'java.lang.Runtime');

    /* ---------- StringBuilder ---------- */
    class StringBuilder extends JObject {
      constructor() { super(); this.s = ''; }
      $init(x) { if (x === undefined || typeof x === 'number') this.s = ''; else this.s = str(x); return this; }
      append(x, a, b) { if (Array.isArray(x) && x.$t === 'char') this.s += x.map(c => c.toString()).join(''); else if (a !== undefined && typeof x !== 'number') this.s += str(x).substring(a, b); else this.s += str(x); return this; }
      appendCodePoint(c) { this.s += String.fromCodePoint(c); return this; }
      insert(i, x) { i = toNum(i); if (i < 0 || i > this.s.length) subErr(i, this.s.length, this.s.length); this.s = this.s.slice(0, i) + str(x) + this.s.slice(i); return this; }
      reverse() { this.s = Array.from(this.s).reverse().join(''); return this; }
      deleteCharAt(i) { i = toNum(i); if (i < 0 || i >= this.s.length) idxErr(i, this.s.length); this.s = this.s.slice(0, i) + this.s.slice(i + 1); return this; }
      delete(a, b) { a = toNum(a); b = Math.min(toNum(b), this.s.length); if (a < 0 || a > b) subErr(a, b, this.s.length); this.s = this.s.slice(0, a) + this.s.slice(b); return this; }
      replace(a, b, x) { b = Math.min(toNum(b), this.s.length); this.s = this.s.slice(0, toNum(a)) + str(x) + this.s.slice(b); return this; }
      setCharAt(i, c) { i = toNum(i); if (i < 0 || i >= this.s.length) idxErr(i, this.s.length); this.s = this.s.slice(0, i) + String.fromCharCode(cc(c)) + this.s.slice(i + 1); }
      setLength(n) { n = toNum(n); if (n < 0) throwEx('StringIndexOutOfBoundsException', `String index out of range: ${n}`); this.s = n <= this.s.length ? this.s.slice(0, n) : this.s + '\0'.repeat(n - this.s.length); }
      charAt(i) { return STRM.charAt(this.s, i); } length() { return this.s.length; } isEmpty() { return this.s.length === 0; } capacity() { return Math.max(16, this.s.length); }
      indexOf(x, f) { return STRM.indexOf(this.s, x, f); } lastIndexOf(x, f) { return STRM.lastIndexOf(this.s, x, f); }
      substring(a, b) { return STRM.substring(this.s, a, b); } subSequence(a, b) { return STRM.substring(this.s, a, b); }
      toString() { return this.s; } compareTo(o) { return strCmp(this.s, o.s); } chars() { return STRM.chars(this.s); }
      equals(o) { return this === o; } hashCode() { return identityHash(this); } ensureCapacity() {} trimToSize() {}
    }
    defLib(StringBuilder, 'StringBuilder', 'java.lang.StringBuilder', [L.CharSequence, L.Comparable]);
    L.StringBuffer = class StringBuffer extends StringBuilder { }; defLib(L.StringBuffer, 'StringBuffer', 'java.lang.StringBuffer', [L.CharSequence]);
    class StringJoiner extends JObject {
      $init(sep, pre = '', suf = '') { this.sep = str(sep); this.pre = str(pre); this.suf = str(suf); this.parts = []; this.empty = null; return this; }
      add(x) { this.parts.push(str(x)); return this; } setEmptyValue(v) { this.empty = str(v); return this; } length() { return this.toString().length; }
      toString() { return this.parts.length === 0 && this.empty !== null ? this.empty : this.pre + this.parts.join(this.sep) + this.suf; }
    }
    defLib(StringJoiner, 'StringJoiner', 'java.util.StringJoiner');

    /* ---------- misc java.lang / java.util utilities ---------- */
    class Objects extends JObject {
      static equals(a, b) { return equals(a, b); } static hash(...a) { return a.reduce((h, x) => (Math.imul(31, h) + hash(x)) | 0, 1); } static hashCode(x) { return hash(x); }
      static toString(x, d) { return x === null && d !== undefined ? d : str(x); } static isNull(x) { return x === null || x === undefined; } static nonNull(x) { return x !== null && x !== undefined; }
      static requireNonNull(x, m) { if (x === null || x === undefined) throw mk('NullPointerException', typeof m === 'function' ? m() : m === undefined ? null : m); return x; }
      static requireNonNullElse(x, d) { if (x !== null && x !== undefined) return x; if (d === null || d === undefined) throw mk('NullPointerException', 'defaultObj'); return d; } static compare(a, b, c) { return a === b ? 0 : fnOf(c, 'compare')(a, b); }
      static checkFromToIndex(a, b, n) { if (a < 0 || a > b || b > n) throwEx('IndexOutOfBoundsException', `Range [${a}, ${b}) out of bounds for length ${n}`); return a; }
      static checkFromIndexSize(a, sz, n) { if ((n | a | sz) < 0 || sz > n - a) throwEx('IndexOutOfBoundsException', `Range [${a}, ${a} + ${sz}) out of bounds for length ${n}`); return a; }
      static checkIndex(i, n) { if (i < 0 || i >= n) throwEx('IndexOutOfBoundsException', `Index ${i} out of bounds for length ${n}`); return i; }
    }
    defLib(Objects, 'Objects', 'java.util.Objects');
    class Random extends JObject {
      $init(seed) { this.set(seed === undefined ? BigInt(Date.now()) ^ 0x5DEECE66Dn ^ BigInt(idCount++) : rt.$toLong(seed)); return this; }
      set(seed) { this.seed = (BigInt(seed) ^ 0x5DEECE66Dn) & ((1n << 48n) - 1n); this.haveG = false; }
      setSeed(s) { this.set(s); }
      next(bits) { this.seed = (this.seed * 0x5DEECE66Dn + 0xBn) & ((1n << 48n) - 1n); return Number(BigInt.asIntN(32, this.seed >> BigInt(48 - bits))); }
      nextInt(a, b) {
        if (a === undefined) return this.next(32);
        let lo = 0, bound = a;
        if (b !== undefined) { lo = a; bound = b - a; if (a >= b) throwEx('IllegalArgumentException', 'bound must be greater than origin'); }
        else if (bound <= 0) throwEx('IllegalArgumentException', 'bound must be positive');
        let r = this.next(31); const m = bound - 1;
        if ((bound & m) === 0) r = Number((BigInt(bound) * BigInt(r)) >> 31n);
        else for (let u = r; u - (r = u % bound) + m > MAXI || u - r + m < 0; u = this.next(31));
        return lo + r;
      }
      nextLong() { return BigInt.asIntN(64, (BigInt(this.next(32)) << 32n) + BigInt(this.next(32))); }
      nextDouble() { return new D((this.next(26) * 134217728 + this.next(27)) / 9007199254740992); }
      nextFloat() { return new D(Math.fround(this.next(24) / 16777216), true); }
      nextBoolean() { return this.next(1) !== 0; }
      nextBytes(a) { for (let i = 0; i < a.length;) for (let r = this.nextInt(), n = Math.min(a.length - i, 4); n-- > 0; r >>= 8) a[i++] = (r << 24) >> 24; }
      nextGaussian() {
        if (this.haveG) { this.haveG = false; return new D(this.nextG); }
        let v1, v2, s;
        do { v1 = 2 * this.nextDouble().v - 1; v2 = 2 * this.nextDouble().v - 1; s = v1 * v1 + v2 * v2; } while (s >= 1 || s === 0);
        const mul = Math.sqrt(-2 * Math.log(s) / s); this.nextG = v2 * mul; this.haveG = true; return new D(v1 * mul);
      }
      ints() { throw mk('UnsupportedOperationException', 'Random.ints is not supported'); }
    }
    defLib(Random, 'Random', 'java.util.Random');
    L.ThreadLocalRandom = class extends Random { static current() { return ThreadLocalRandomInst || (ThreadLocalRandomInst = new Random().$init()); } }; let ThreadLocalRandomInst; defLib(L.ThreadLocalRandom, 'ThreadLocalRandom', 'java.util.concurrent.ThreadLocalRandom');

    class StringTokenizer extends JObject {
      $init(s, d, ret) { s = str(s); d = d === undefined ? ' \t\n\r\f' : str(d); this.toks = []; let cur = ''; for (const ch of s) { if (d.includes(ch)) { if (cur) { this.toks.push(cur); cur = ''; } if (ret === true) this.toks.push(ch); } else cur += ch; } if (cur) this.toks.push(cur); this.i = 0; return this; }
      hasMoreTokens() { return this.i < this.toks.length; } hasMoreElements() { return this.hasMoreTokens(); }
      nextToken() { if (this.i >= this.toks.length) throwEx('NoSuchElementException'); return this.toks[this.i++]; } nextElement() { return this.nextToken(); }
      countTokens() { return this.toks.length - this.i; }
    }
    defLib(StringTokenizer, 'StringTokenizer', 'java.util.StringTokenizer');
    class Locale extends JObject { constructor(n) { super(); this.n = n; this.$isLocale = true; } toString() { return this.n; } }
    defLib(Locale, 'Locale', 'java.util.Locale'); Locale.US = new Locale('en_US'); Locale.ENGLISH = new Locale('en'); Locale.ROOT = new Locale(''); Locale.UK = new Locale('en_GB'); Locale.getDefault = () => Locale.US;

    class Thread extends JObject {
      constructor() { super(); this.tr = null; this.nm = 'Thread-' + (Thread.cnt++); this.done = false; }
      $init(a, b) { if (typeof a === 'string' || a instanceof String) this.nm = a; else if (a) { this.tr = a; if (b) this.nm = b; } return this; }
      run() { if (this.tr) { if (typeof this.tr === 'function') this.tr(); else this.tr.run(); } }
      start() { const prev = Thread.curr; Thread.curr = this; try { this.run(); } finally { Thread.curr = prev; this.done = true; } }
      join() {} interrupt() {} isInterrupted() { return false; } isAlive() { return false; } setDaemon() {} isDaemon() { return false; } setPriority() {} getPriority() { return 5; }
      getName() { return this.nm; } setName(n) { this.nm = n; } getId() { return 1n; } threadId() { return 1n; }
      static sleep() {} static yield() {} static currentThread() { return Thread.curr; } static onSpinWait() {}
    }
    Thread.cnt = 0; defLib(Thread, 'Thread', 'java.lang.Thread', [L.Runnable]); Thread.curr = new Thread(); Thread.curr.nm = 'main'; Thread.cnt = 0;

    class Atomic extends JObject {
      constructor() { super(); this.v = 0; }
      $init(v) { this.v = v === undefined ? this.zero() : v; return this; } zero() { return 0; }
      get() { return this.v; } set(v) { this.v = v; } intValue() { return rt.$toInt(this.v); } longValue() { return rt.$toLong(this.v); }
      incrementAndGet() { return this.v = rt.$incv(this.v, 1); } decrementAndGet() { return this.v = rt.$incv(this.v, -1); }
      getAndIncrement() { const o = this.v; this.v = rt.$incv(o, 1); return o; } getAndDecrement() { const o = this.v; this.v = rt.$incv(o, -1); return o; }
      addAndGet(d) { return this.v = rt.$add(this.v, d); } getAndAdd(d) { const o = this.v; this.v = rt.$add(o, d); return o; } getAndSet(v) { const o = this.v; this.v = v; return o; }
      compareAndSet(e, n) { if (rt.$eq(this.v, e) || this.v === e) { this.v = n; return true; } return false; }
      updateAndGet(f) { return this.v = fnOf(f)(this.v); } getAndUpdate(f) { const o = this.v; this.v = fnOf(f)(o); return o; } accumulateAndGet(x, f) { return this.v = fnOf(f)(this.v, x); }
      toString() { return str(this.v); }
    }
    L.AtomicInteger = class AtomicInteger extends Atomic { }; defLib(L.AtomicInteger, 'AtomicInteger', 'java.util.concurrent.atomic.AtomicInteger');
    L.AtomicLong = class AtomicLong extends Atomic { zero() { return 0n; } }; defLib(L.AtomicLong, 'AtomicLong', 'java.util.concurrent.atomic.AtomicLong');
    L.AtomicBoolean = class AtomicBoolean extends Atomic { zero() { return false; } }; defLib(L.AtomicBoolean, 'AtomicBoolean', 'java.util.concurrent.atomic.AtomicBoolean');
    L.AtomicReference = class AtomicReference extends Atomic { zero() { return null; } }; defLib(L.AtomicReference, 'AtomicReference', 'java.util.concurrent.atomic.AtomicReference');

    /* ---------- Scanner / console input ---------- */
    const q = [...(env.stdin || [])]; let buf = null;
    const need = () => { throw new NeedInput(); };
    const peekTok = () => { let b = buf, i = 0; for (;;) { if (b === null) { if (i >= q.length) return null; b = q[i++]; } const t = b.trim(); if (t) return t.split(/\s+/)[0]; b = null; } };
    const io = {
      load() { if (buf !== null) return; if (!q.length) { if (env.interactive) need(); throwEx('NoSuchElementException', 'No line found'); } buf = q.shift(); if (env.echo !== false) pl(buf); },
      nextLine() { io.load(); const r = buf; buf = null; return r; },
      next() { for (;;) { if (buf === null && !q.length && !env.interactive) throwEx('NoSuchElementException'); io.load(); buf = buf.replace(/^\s+/, ''); if (buf === '') { buf = null; continue; } const t = buf.match(/^\S+/)[0]; buf = buf.slice(t.length); return t; } },
      peekTok() { for (;;) { io.load(); const t = buf.replace(/^\s+/, ''); if (t === '') { buf = null; continue; } return t.match(/^\S+/)[0]; } },
      hasNextLine() { if (buf === null && !q.length && env.interactive) need(); return buf !== null || q.length > 0; },
      hasNext() { if (peekTok() === null && env.interactive) need(); return peekTok() !== null; },
    };
    const mismatch = t => { if (/^[+-]?\d+$/.test(t)) throwEx('InputMismatchException', `For input string: "${t}"`); throwEx('InputMismatchException'); };
    class Scanner extends JObject {
      $init(src) { this.src = src; if (typeof src === 'string' || src instanceof String) { const t = str(src); this.q = t === '' ? [] : t.split('\n'); if (t.endsWith('\n')) this.q.pop(); this.b = null; this.own = true; } return this; }
      /* a Scanner over a String keeps its own buffer */
      get io() { if (!this.own) return io; if (!this.sio) { const qq = this.q; let bb = null; const self = this; this.sio = { load() { if (bb !== null) return; if (!qq.length) throwEx('NoSuchElementException', 'No line found'); bb = qq.shift(); }, nextLine() { this.load(); const r = bb; bb = null; return r; }, next() { for (;;) { if (bb === null && !qq.length) throwEx('NoSuchElementException'); this.load(); bb = bb.replace(/^\s+/, ''); if (bb === '') { bb = null; continue; } const t = bb.match(/^\S+/)[0]; bb = bb.slice(t.length); return t; } }, peekTok() { for (;;) { this.load(); const t = bb.replace(/^\s+/, ''); if (t === '') { bb = null; continue; } return t.match(/^\S+/)[0]; } }, hasNextLine() { return bb !== null || qq.length > 0; }, hasNext() { let b = bb, i = 0; for (;;) { if (b === null) { if (i >= qq.length) return false; b = qq[i++]; } if (b.trim()) return true; b = null; } } }; } return this.sio; }
      next() { return this.io.next(); } nextLine() { return this.io.nextLine(); }
      nextInt() { const t = this.io.peekTok(); if (!/^[+-]?\d+$/.test(t) || BigInt(t) > 2147483647n || BigInt(t) < -2147483648n) mismatch(t); this.io.next(); return Number(parseIntStrict(t)); }
      nextLong() { const t = this.io.peekTok(); if (!/^[+-]?\d+$/.test(t)) mismatch(t); this.io.next(); return BigInt(t); }
      nextDouble() { const t = this.io.peekTok(); if (t === '' || isNaN(+t) || /^0x/i.test(t)) mismatch(t); this.io.next(); return new D(+t); }
      nextFloat() { const t = this.io.peekTok(); if (isNaN(+t)) mismatch(t); this.io.next(); return new D(Math.fround(+t), true); }
      nextBoolean() { const t = this.io.peekTok(); if (!/^(true|false)$/i.test(t)) mismatch(t); this.io.next(); return t.toLowerCase() === 'true'; }
      hasNext() { return this.io.hasNext(); } hasNextLine() { return this.io.hasNextLine(); }
      hasNextInt() { if (this.own) { const t = (this.q.join(' ').trim().split(/\s+/)[0]); return /^[+-]?\d+$/.test(t || ''); } const t = peekTok(); if (t === null && env.interactive) need(); return t !== null && /^[+-]?\d+$/.test(t); }
      hasNextDouble() { const t = peekTok(); if (t === null && env.interactive) need(); return t !== null && !isNaN(+t); }
      close() {}
    }
    defLib(Scanner, 'Scanner', 'java.util.Scanner', [L.AutoCloseable]);
    class InputStreamReader extends JObject { } defLib(InputStreamReader, 'InputStreamReader', 'java.io.InputStreamReader');
    class BufferedReader extends JObject { readLine() { if (!io.hasNextLine()) return null; return io.nextLine(); } read() { return -1; } ready() { return io.hasNextLine(); } close() {} lines() { const a = []; while (io.hasNextLine()) a.push(io.nextLine()); return L.Stream.fromArray(a); } }
    defLib(BufferedReader, 'BufferedReader', 'java.io.BufferedReader', [L.AutoCloseable]);

    /* ---------- iteration ---------- */
    rt.$iter = x => {
      if (x === null || x === undefined) throw mk('NullPointerException');
      if (Array.isArray(x) || typeof x[Symbol.iterator] === 'function') return x;
      if (typeof x.iterator === 'function') {
        const it = x.iterator();
        return { [Symbol.iterator]() { return { next() { return it.hasNext() ? { value: it.next(), done: false } : { value: undefined, done: true }; } }; } };
      }
      throw mk('ClassCastException', 'not iterable: ' + className(x));
    };
    rt.$sw = x => x instanceof Ch ? x.c : x instanceof String ? x.valueOf() : (x !== null && typeof x === 'object' && x.$ename !== undefined) ? x.$ename : x;
    rt.$tick = env.tick;
    rt.$new = (C, ...a) => new C().$init(...a);
    rt.$newInner = (outer, C, ...a) => { const o = new C(); o.$outer = outer; return o.$init(...a); };
    rt.$wrap = e => {
      if (e instanceof Throwable) return e;
      if (e instanceof ExitSignal || e instanceof NeedInput) throw e;
      if (e instanceof RangeError && /call stack/i.test(e.message)) return mk('StackOverflowError');
      if (e instanceof TypeError && /null|undefined|not a function|is not iterable|Cannot read|Cannot set/i.test(e.message)) { const x = mk('NullPointerException'); x.$js = e; return x; }
      if (e instanceof RangeError && /Invalid array length/i.test(e.message)) return mk('NegativeArraySizeException');
      if (e instanceof RangeError && /Invalid string length/i.test(e.message)) return mk('OutOfMemoryError', 'Java heap space');
      return e;
    };
    rt.$mkEx = mk;
    rt.$throw = e => { if (e === null || e === undefined) throw mk('NullPointerException'); throw e; };
    rt.$assert = (c, m) => { /* assertions are disabled by default in Java */ };
    rt.$tryClose = (r, primary) => { if (r === null || r === undefined) return; if (primary && primary instanceof Throwable) { try { r.close(); } catch (e) { const w = rt.$wrap(e); if (w instanceof Throwable) primary.addSuppressed(w); else throw w; } } else r.close(); };
    rt.$noSuch = (what) => { throw mk('RuntimeException', what); };
    rt.$stripNeg = null;
    rt.J = Throwable; rt.JObject = JObject;
    rt.$h = H;
    extensions.forEach(ext => ext(rt, L, H));
    return rt;
  }
  return { create, extensions };
})();
if (typeof module !== 'undefined') module.exports = JavaRT;
