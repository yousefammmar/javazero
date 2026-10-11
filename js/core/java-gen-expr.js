/* JavaZero compiler, stage 2b: statements and expressions (type inference, javac-style diagnostics, JS emission). */
(() => {
  const { Gen, PRIM, NUMP, BOX, RANK, esc, mangle, isNumT, unbox, isIntLike, binNum, OBJ_RET, STR_RET, STATIC_RET, STATIC_FIELD, ARITH, libL } = JavaGen;
  const proto = Gen.prototype;
  const BOXED_OBJ = new Set(['String', 'Integer', 'Long', 'Double', 'Float', 'Character', 'Boolean', 'Short', 'Byte', 'Number', 'Object', 'Comparable', 'CharSequence', 'T', 'Iterable']);
  const IFACE_CHECK_MAP = { List: 'ArrayList', Map: 'HashMap', Set: 'HashSet', Queue: 'LinkedList', Deque: 'LinkedList', Collection: 'ArrayList', SortedMap: 'TreeMap', NavigableMap: 'TreeMap', SortedSet: 'TreeSet', NavigableSet: 'TreeSet' };
  const lit = (t, c) => ({ c, t });
  const isBool = t => t === 'boolean' || t === 'Boolean';
  const argStr = args => args.map(a => a.t || 'Object').join(',');
  const jsName = s => (JavaGen.JS_RESERVED.has(s) ? s + '$' : s);

  Object.assign(proto, {
    /* ==================================================================
       conversions & assignment checks
       ================================================================== */
    checkAssign(from, to, line, node) {
      if (!from || !to || from === to) return;
      const fu = unbox(from), tu = unbox(to);
      const bad = () => this.err(line, `incompatible types: ${from} cannot be converted to ${to}`);
      const lossy = () => this.err(line, `incompatible types: possible lossy conversion from ${from} to ${to}`);
      if (from === 'null') { if (PRIM.has(to)) this.err(line, `incompatible types: <null> cannot be converted to ${to}`); return; }
      if (from === 'Object' && to !== 'Object') { if (!(PRIM.has(to) && false)) { bad(); return; } }
      if (from === 'void') { this.err(line, `incompatible types: void cannot be converted to ${to}`); return; }
      if (PRIM.has(to)) {
        if (PRIM.has(from) || BOX[from]) {
          if (tu === 'boolean' || fu === 'boolean') { if (tu !== fu) bad(); return; }
          // numeric
          if (BOX[from] && !PRIM.has(from)) { if (RANK[fu] > RANK[tu] || (fu === 'char' && (tu === 'short' || tu === 'byte')) || (tu === 'char' && fu !== 'char')) lossy(); return; }
          if (tu === 'char') { if (fu === 'int' || fu === 'short' || fu === 'byte') return; if (RANK[fu] > RANK[tu]) lossy(); else if (fu !== 'char') lossy(); return; }
          if (fu === 'char' && (tu === 'short' || tu === 'byte')) return;
          if (RANK[fu] > RANK[tu]) { if (fu === 'int' && (tu === 'short' || tu === 'byte')) return; lossy(); }
          return;
        }
        if (from === 'String') { bad(); return; }
        if (from.endsWith('[]') || this.findClass(from)) { bad(); return; }
        if (from === 'Object' || from === 'Number' || from === 'Comparable') { this.err(line, `incompatible types: ${from} cannot be converted to ${to}`); return; }
        return;
      }
      if (BOX[to]) {
        if (PRIM.has(from)) { if (fu !== tu && !(fu === 'int' && (tu === 'short' || tu === 'byte' || tu === 'char'))) bad(); return; }
        if (BOX[from]) { if (fu !== tu) bad(); return; }
        if (from === 'String' || from.endsWith('[]') || this.findClass(from)) bad();
        return;
      }
      if (to === 'String') {
        if (PRIM.has(from) || BOX[from] || from.endsWith('[]')) { bad(); return; }
        const fc = this.findClass(from); if (fc) bad();
        return;
      }
      if (to.endsWith('[]')) {
        if (!from.endsWith('[]')) { if (from !== 'null') bad(); return; }
        if (from.endsWith('[]')) { const fe = from.slice(0, -2), te = to.slice(0, -2); if ((PRIM.has(fe) || PRIM.has(te)) && fe !== te) bad(); }
        return;
      }
      const tc = this.findClass(to);
      if (tc) {
        if (PRIM.has(from) || from === 'String' || BOX[from]) { bad(); return; }
        const fc = this.findClass(from);
        if (fc) { if (!this.isSub(fc, tc)) bad(); return; }
        if (from.endsWith('[]')) bad();
        return;
      }
      // to is a library reference type
      if (PRIM.has(from) && to !== 'Object' && to !== 'Number' && to !== 'Comparable' && !BOXED_OBJ.has(to)) {
        const L = libL().L; if (L[to] && !L[to].$prim) bad();
        return;
      }
      if (from === 'String' && L_isCollection(to)) bad();
      const fc = this.findClass(from);
      if (fc && libL().L[to] && libL().L[to].$isIface !== undefined && !BOXED_OBJ.has(to)) {
        // user class assigned to a library interface/class: must implement/extend it
        if (!this.userImplementsLib(fc, to)) this.err(line, `incompatible types: ${from} cannot be converted to ${to}`);
      }
    },
    userImplementsLib(info, libName) {
      const L = libL().L, target = L[libName];
      const visit = c => {
        if (!c) return false;
        if (c.parentLib && (L[c.parentLib] === target || (L[c.parentLib].$supers && L[c.parentLib].$supers.has(target)))) return true;
        if (c.libIfaces.some(n => L[n] === target || (L[n].$supers && L[n].$supers.has(target)))) return true;
        if (c.ifaces.some(visit)) return true;
        return visit(c.parent);
      };
      return visit(info);
    },
    guardArr(r, node, what) {
      const k = node.k;
      if (['this', 'new', 'newarr', 'lit', 'arrlit', 'qthis'].includes(k) || /^\$/.test(r.c) && false) return r.c;
      return `(${r.c} ?? $np(${esc(what + this.nullWhy(node, r))}))`;
    },
    constInt(e) {
      switch (e.k) {
        case 'lit': return e.t === 'int' ? Number(e.v) : e.t === 'char' ? e.v : undefined;
        case 'paren': return this.constInt(e.e);
        case 'unary': { const v = this.constInt(e.e); if (v === undefined) return undefined; return e.op === '-' ? -v | 0 : e.op === '+' ? v : e.op === '~' ? ~v : undefined; }
        case 'bin': {
          const a = this.constInt(e.l), b = this.constInt(e.r); if (a === undefined || b === undefined) return undefined;
          switch (e.op) { case '+': return (a + b) | 0; case '-': return (a - b) | 0; case '*': return Math.imul(a, b); case '/': return b ? (a / b) | 0 : undefined; case '%': return b ? (a % b) | 0 : undefined; case '<<': return a << b; case '>>': return a >> b; case '>>>': return (a >>> b) | 0; case '&': return a & b; case '|': return a | b; case '^': return a ^ b; }
          return undefined;
        }
        case 'cast': { const v = this.constInt(e.e); if (v === undefined) return undefined; const t = e.type.n; return t === 'int' ? v | 0 : t === 'char' ? v & 0xffff : t === 'short' ? (v << 16) >> 16 : t === 'byte' ? (v << 24) >> 24 : undefined; }
        case 'name': { const lv = this.lookupVar(e.id); if (lv) return lv.v.constVal; const r = this.lookupMemberLexical(e.id, false); if (r && r.member.final && r.member.init && r.member.init.k !== 'arrlit' && !r.member.init.k !== 'x') return r.member.init.k === 'lit' ? this.constInt(r.member.init) : undefined; return undefined; }
      }
      return undefined;
    },
    narrowCheck(r, to, line, node) {
      const range = { byte: [-128, 127], short: [-32768, 32767], char: [0, 65535] }[to];
      const v = this.constInt(node);
      const lossy = () => this.err(line, `incompatible types: possible lossy conversion from ${r.t} to ${to}`);
      if (v !== undefined) { if (v < range[0] || v > range[1]) lossy(); return; }
      if (!(r.t === to || (r.t === 'byte' && to === 'short'))) lossy();
    },
    convert(r, to, line, node) {
      if (!to) return r.c;
      if (r.t === '$lambda') {
        const ci = this.findClass(to);
        if (ci && ci.kind === 'interface') return `$lamI(${r.c}, ${ci.js})`;
        if (PRIM.has(to) || to === 'String' || BOX[to] || to.endsWith('[]') || to === 'Object' || (ci && ci.kind !== 'interface')) this.err(line, `incompatible types: ${to} is not a functional interface`);
        return r.c;
      }
      if (node && node.k === 'cond' && r.branches && to && (PRIM.has(to) || to === 'String' || BOX[to])) {
        let bad = false;
        for (const b of r.branches) if (b.t) { const saved = this.errs; this.errs = []; this.checkAssign(b.t, to, line, null); if (this.errs.length) bad = true; this.errs = saved; }
        if (bad) { this.err(line, 'incompatible types: bad type in conditional expression'); return r.c; }
      }
      if (node && (to === 'byte' || to === 'short' || to === 'char') && PRIM.has(r.t) && isIntLike(r.t)) this.narrowCheck(r, to, line, node);
      else this.checkAssign(r.t, to, line, node);
      return this.coerceCode(r.c, r.t, to, false, node);
    },
    coerceCode(code, from, to, isParam, node) {
      if (!to || from === to) return code;
      switch (to) {
        case 'double': return from === 'double' ? code : `$toDouble(${code})`;
        case 'float': return from === 'float' ? code : `$toFloat(${code})`;
        case 'long': return from === 'long' ? code : (from === 'int' || from === 'short' || from === 'byte') ? `BigInt(${code})` : `$toLong(${code})`;
        case 'int': case 'short': case 'byte':
          if (from === 'int' || from === 'short' || from === 'byte') return code;
          if (from === 'char') return `${code}.c`;
          if (isParam) return code;
          return from ? `$toInt(${code})` : `$co.int(${code})`;
        case 'char':
          if (from === 'char') return code;
          if (/^-?\d+$/.test(code)) return this.konst('c' + code, `$Ch(${code})`);
          return from === 'int' || from === 'short' || from === 'byte' ? `$toChar(${code})` : (isParam ? code : `$co.char(${code})`);
        default: return code;
      }
    },
    arrayLit(e, t, line) {
      if (!t || !t.endsWith('[]')) { if (t) this.err(line, `illegal initializer for ${t}`); return `$arr('Object', [${e.elems.map(x => this.expr(x).c).join(', ')}])`; }
      const et = t.slice(0, -2);
      const items = e.elems.map(x => x.k === 'arrlit' ? this.arrayLit(x, et, x.ln) : this.convert(this.expr(x), et, x.ln, x));
      return `$arr(${esc(et)}, [${items.join(', ')}])`;
    },
    /* ==================================================================
       statements
       ================================================================== */
    stmt(s) {
      switch (s.k) {
        case 'block': return this.withScope(() => '{\n' + s.body.map(x => this.stmt(x)).join('\n') + '\n}');
        case 'empty': return ';';
        case 'local': return this.localDecl(s);
        case 'expr': {
          if (s.e.k === 'ctorcall') { this.err(s.ln, `call to ${s.e.which} must be first statement in constructor`); return ';'; }
          return this.expr(s.e, true).c + ';';
        }
        case 'if': {
          const c = this.cond(s.c);
          const a = this.sub(s.a), b = s.b ? this.sub(s.b) : null;
          return `if (${c}) ${a}${b !== null ? ' else ' + b : ''}`;
        }
        case 'while': { const c = this.cond(s.c); this.ctx.loops++; const b = this.sub(s.body); this.ctx.loops--; return `while (${c}) { $tick(); ${b} }`; }
        case 'do': { this.ctx.loops++; const b = this.sub(s.body); this.ctx.loops--; const c = this.cond(s.c); return `do { $tick(); ${b} } while (${c});`; }
        case 'for': return this.withScope(() => {
          const init = s.init.length && s.init[0].k === 'local' ? this.localDecl(s.init[0], true) : s.init.map(i => this.expr(i.e, true).c).join(', ');
          const c = s.c ? this.cond(s.c) : '';
          const upd = s.update.map(u => this.expr(u, true).c).join(', ');
          this.ctx.loops++; const b = this.sub(s.body); this.ctx.loops--;
          return `for (${init}; ${c}; ${upd}) { $tick(); ${b} }`;
        });
        case 'foreach': return this.withScope(() => {
          const it = this.expr(s.iter);
          let vt = s.type.n === 'var' ? (it.t && it.t.endsWith('[]') ? it.t.slice(0, -2) : null) : this.T(s.type);
          if (s.type.n !== 'var') this.checkType(s.type, s.ln);
          if (it.t && PRIM.has(it.t)) this.err(s.ln, 'for-each not applicable to expression type\n  required: array or java.lang.Iterable\n  found:    ' + it.t);
          else if (it.t === 'String') this.err(s.ln, 'for-each not applicable to expression type\n  required: array or java.lang.Iterable\n  found:    String');
          else if (it.t && it.t.endsWith('[]') && vt) this.checkAssign(it.t.slice(0, -2), vt, s.ln);
          else if (it.targs && it.targs[0] && JavaGen.helpers.ITER_TYPES.has(it.t)) { if (s.type.n === 'var') vt = it.targs[0].t; else if (vt) this.checkAssign(it.targs[0].t, vt, s.ln); }
          const js = jsName(s.name);
          this.declare(s.name, { t: vt, js, targs: it.targs && it.targs[0] ? it.targs[0].targs : null }, s.ln);
          this.ctx.loops++; const b = this.sub(s.body); this.ctx.loops--;
          const conv = vt && (vt === 'double' || vt === 'long' || vt === 'float') && it.t && it.t.endsWith('[]') && it.t.slice(0, -2) !== vt;
          return `for (let ${js}${conv ? '$' : ''} of $iter(${it.c})) { $tick(); ${conv ? `let ${js} = ${this.coerceCode(js + '$', null, vt)};` : ''} ${b} }`;
        });
        case 'return': return this.returnStmt(s);
        case 'break':
          if (!s.label && !this.ctx.loops && !this.ctx.switches) this.err(s.ln, 'break outside switch or loop');
          return `break${s.label ? ' ' + s.label : ''};`;
        case 'continue':
          if (!this.ctx.loops) this.err(s.ln, 'continue outside of loop');
          return `continue${s.label ? ' ' + s.label : ''};`;
        case 'throw': {
          const e = this.expr(s.e);
          if (e.t && (PRIM.has(e.t) || e.t === 'String' || BOX[e.t])) this.err(s.ln, `incompatible types: ${e.t} cannot be converted to Throwable`);
          else if (e.t) { const ci = this.findClass(e.t); if (ci && !this.isThrowable(ci)) this.err(s.ln, `incompatible types: ${e.t} cannot be converted to Throwable`); }
          return `throw ${e.c};`;
        }
        case 'try': return this.tryStmt(s);
        case 'switch': return this.switchStmt(s, false);
        case 'labeled': return `${s.label}: ${this.stmt(s.body)}`;
        case 'localclass': return this.localClass(s);
        case 'yield': return `return ${this.expr(s.e).c};`;
        case 'assert': return ';';
        case 'exprval': return this.expr(s.e, true).c + ';';
      }
      this.err(s.ln, 'unsupported statement: ' + s.k);
      return ';';
    },
    sub(s) { if (s.k === 'block') return this.stmt(s); return this.withScope(() => (s.k === 'local' ? (this.err(s.ln, 'variable declaration not allowed here'), '') : '') + this.stmt(s)); },
    withScope(fn) { const prev = this.scope; this.scope = this.pushScope(); try { return fn(); } finally { this.scope = prev; } },
    cond(e) {
      const r = this.expr(e);
      if (r.t && !isBool(r.t)) this.err(e.ln, `incompatible types: ${r.t} cannot be converted to boolean`);
      return r.c;
    },
    localDecl(s, inFor) {
      const parts = [];
      const isVar = s.type.n === 'var';
      if (!isVar) this.checkType(s.type, s.ln);
      for (const d of s.decls) {
        if (isVar && !d.init) this.err(d.ln, `cannot infer type for local variable ${d.name}`);
        const dt = isVar ? null : this.T({ ...s.type, dims: s.type.dims + d.dims });
        const js = jsName(d.name);
        if (d.init) {
          let code, vt = dt;
          if (d.init.k === 'arrlit') { if (isVar) this.err(d.ln, "cannot infer type for local variable " + d.name); code = this.arrayLit(d.init, dt, d.ln); }
          else {
            const r = this.expr(d.init); this.lastExprResult = r;
            if (isVar) { if (r.t === 'null') this.err(d.ln, `cannot infer type for local variable ${d.name}`); vt = r.t === 'null' ? null : r.t; code = r.c; }
            else code = this.convert(r, dt, d.ln, d.init);
            if (r.t === 'void') this.err(d.ln, "illegal initializer for " + (dt || 'var'));
          }
          const rr = d.init.k === 'arrlit' ? {} : this.lastExprResult || {};
          this.declare(d.name, { t: vt, js, final: s.final, assigned: true, kind: 'local', ln: d.ln, fresh: vt === 'String' ? rr.fresh : 0, constInit: vt === 'String' && !!rr.isConst, constVal: s.final && d.init.k !== 'arrlit' && isIntLike(vt) ? this.constInt(d.init) : undefined, targs: isVar ? (rr.targs || null) : this.targsOf(s.type) }, d.ln);
          parts.push(`${js} = ${code}`);
        } else {
          this.declare(d.name, { t: dt, js, final: s.final, assigned: false, kind: 'local', ln: d.ln, needsDA: true, targs: this.targsOf(s.type) }, d.ln);
          parts.push(js);
        }
      }
      return `let ${parts.join(', ')}${inFor ? '' : ';'}`;
    },
    returnStmt(s) {
      const ctx = this.ctx;
      if (ctx.inLambda) {
        if (!s.e) return 'return;';
        return `return ${this.expr(s.e).c};`;
      }
      if (ctx.cls && ctx.isClinit) { this.err(s.ln, 'return outside method'); return ';'; }
      const rt = ctx.ret;
      if (!s.e) {
        if (rt && rt !== 'void' && !ctx.isCtor) this.err(s.ln, 'incompatible types: missing return value');
        return ctx.isCtor ? 'return this;' : 'return;';
      }
      if (rt === 'void' || ctx.isCtor) { this.expr(s.e); this.err(s.ln, 'incompatible types: unexpected return value'); return 'return;'; }
      if (rt === null) return `return ${this.expr(s.e).c};`;
      if (s.e.k === 'arrlit') this.err(s.ln, 'illegal start of expression');
      const r = this.expr(s.e);
      return `return ${this.convert(r, rt, s.ln, s.e)};`;
    },
    tryStmt(s) {
      const L = libL().L;
      let body;
      if (s.res.length) {
        // try-with-resources: close in reverse order, attach close failures as suppressed exceptions
        body = this.withScope(() => {
          const open = [];
          const gen = i => {
            if (i === s.res.length) return this.stmt(s.block);
            const r = s.res[i];
            let js, pre;
            if (r.type) { const t = this.T(r.type); const init = this.convert(this.expr(r.init), t, r.ln, r.init); js = jsName(r.name); this.declare(r.name, { t, js, kind: 'local', assigned: true, final: true }, r.ln); pre = `let ${js} = ${init};`; }
            else { js = this.tmp(); pre = `${js} = ${this.expr(r.expr).c};`; }
            const pv = '$p' + (this.tmpN++);
            return `{ ${pre} let ${pv} = null; try ${gen(i + 1)} catch ($x) { ${pv} = $wrap($x); throw ${pv}; } finally { $tryClose(${js}, ${pv}); } }`;
          };
          return gen(0);
        });
      } else body = this.stmt(s.block);
      if (!s.catches.length && !s.fin) return body;
      let out = `try ${body}`;
      if (s.catches.length) {
        const ev = '$e' + (this.tmpN++);
        const arms = []; const seen = [];
        for (const c of s.catches) {
          const tests = c.types.map(t => {
            this.checkType(t, c.ln);
            const ci = this.findClass(t.n);
            for (const p of seen) if (this.exSub(t.n, p.n)) this.err(c.ln, `exception ${t.n} has already been caught`);
            if (ci) { if (!this.isThrowable(ci)) this.err(c.ln, `incompatible types: ${t.n} cannot be converted to Throwable`); return `$inst(${ev}, ${ci.js})`; }
            const lib = this.libName(t.n);
            if (lib && L[lib] && (L[lib] === L.Throwable || L[lib].prototype instanceof L.Throwable)) return `$inst(${ev}, $L[${esc(lib)}])`;
            if (lib) this.err(c.ln, `incompatible types: ${t.n} cannot be converted to Throwable`);
            return 'false';
          });
          c.types.forEach(t => seen.push(t));
          const prevScope = this.scope; this.scope = this.pushScope();
          const ct = c.types.length === 1 ? c.types[0].n : 'Throwable';
          const js = jsName(c.name);
          this.declare(c.name, { t: ct, js, kind: 'local', assigned: true }, c.ln);
          const bd = this.stmt(c.body);
          this.scope = prevScope;
          arms.push(`if (${tests.join(' || ')}) { let ${js} = ${ev}; ${bd} }`);
        }
        out += ` catch (${ev}) { ${ev} = $wrap(${ev}); ${arms.join(' else ')} else throw ${ev}; }`;
      }
      if (s.fin) out += ` finally ${this.stmt(s.fin)}`;
      return out;
    },
    exSub(a, b) { // exception class a is a subclass of b
      if (a === b) return true;
      const L = libL().L, ia = this.findClass(a);
      if (ia) { for (let c = ia; c; c = c.parent) { if (c.name === b) return true; if (c.parentLib) return this.exSub(c.parentLib, b); } return false; }
      const ca = L[a], cb = this.findClass(b) ? null : L[b];
      return !!(ca && cb && ca.prototype instanceof cb);
    },
    localClass(s) {
      const d = s.decl;
      if (d.kind === 'record' || d.kind === 'enum' || d.kind === 'interface') { /* implicitly static: still emitted inline */ }
      const info = this.collect(d, this.cur, { local: true, js: d.name + '$L' + (this.localClassN++) });
      info.staticCtx = this.ctx.isStatic;
      info.outer = this.cur;
      if (!this.scope.classes) this.scope.classes = new Map();
      this.scope.classes.set(d.name, info);
      this.resolveSupers(info); this.scanSigs(info);
      info.nested.forEach(n => { this.resolveSupers(n); this.scanSigs(n); });
      const r = this.genClass(info, false);
      this.checkAbstractImpl(info); this.checkOverrides(info);
      let extra = '';
      for (const n of info.nested) { const rn = this.genClass(n, false); extra += rn.def + '\n' + rn.post + '\n'; }
      return `${r.def}\n${r.post}\n${extra}`;
    },
    /* ---------- switch ---------- */
    switchStmt(s, isExpr) {
      const sel = this.expr(s.sel);
      const hasPattern = s.cases.some(c => c.labels && c.labels.some(l => l.k === 'typepat' || l.k === 'recpat' || (l.k === 'lit' && l.t === 'null')));
      const arrowForm = s.cases.some(c => c.arrow);
      if (s.cases.some(c => c.arrow) && s.cases.some(c => !c.arrow)) this.err(s.ln, 'different case kinds used in the switch');
      this.ctx.switches++;
      const selT = sel.t;
      if (selT && (selT === 'long' || selT === 'double' || selT === 'float' || selT === 'boolean') && !hasPattern) this.err(s.sel.ln, `constant label of type ${selT} is not allowed here`.replace(/^.*$/, `incompatible types: ${selT} cannot be converted to int`));
      let out, resT = undefined;
      const seenLabels = new Set(); let hasDefault = false;
      const arms = [];
      const enumInfo = selT ? this.findClass(selT) : null;
      const label = l => {
        if (l.k === 'lit') {
          let key;
          if (l.t === 'String') { key = 's:' + l.v; if (selT && selT !== 'String' && !hasPattern) this.err(l.ln, `constant label of type String is not compatible with ${selT}`); if (seenLabels.has(key)) this.err(l.ln, 'duplicate case label'); seenLabels.add(key); return esc(l.v); }
          if (l.t === 'char') { key = 'n:' + l.v; if (seenLabels.has(key)) this.err(l.ln, 'duplicate case label'); seenLabels.add(key); if (selT === 'String') this.err(l.ln, `constant label of type char is not compatible with String`); return String(l.v); }
          if (l.t === 'int') { key = 'n:' + Number(l.v); if (seenLabels.has(key)) this.err(l.ln, 'duplicate case label'); seenLabels.add(key); if (selT === 'String') this.err(l.ln, `constant label of type int is not compatible with String`); return String(Number(l.v)); }
          if (l.t === 'null') return 'null';
          this.err(l.ln, 'constant expression required'); return '0';
        }
        if (l.k === 'name' || l.k === 'field') {
          const nm = l.k === 'name' ? l.id : l.name;
          // enum constant?
          let ec = null;
          if (enumInfo && enumInfo.kind === 'enum' && enumInfo.fields.get(nm) && enumInfo.fields.get(nm).enumConst) ec = enumInfo;
          else if (l.k === 'field') { const ci = this.exprClass(l.obj); if (ci && ci.kind === 'enum') ec = ci; }
          else if (!this.lookupVar(nm) && !this.lookupMemberLexical(nm, false)) ec = this.enumConsts.get(nm) || null;
          if (ec) { if (seenLabels.has('e:' + nm)) this.err(l.ln, 'duplicate case label'); seenLabels.add('e:' + nm); return esc(nm); }
          const r = this.expr(l);
          if (r.t === 'char') return `${r.c}.c`;
          return r.c;
        }
        const r = this.expr(l);
        if (r.t === 'char') return `${r.c}.c`;
        return r.c;
      };
      const armTypes = [];
      if (hasPattern) {
        // if-chain implementation; pattern variables live in hoisted temporaries
        const sv = '$s' + (this.tmpN++);
        this.ctx.tmps.push(sv);
        const chain = [];
        let defBody = null;
        for (const c of s.cases) {
          const prevScope = this.scope; this.scope = this.pushScope();
          if (c.labels === null || c.isDefault) { defBody = this.caseBody(c, isExpr, armTypes); this.scope = prevScope; if (!c.labels || !c.labels.length) continue; }
          const tests = [];
          for (const l of c.labels || []) {
            if (l.k === 'typepat' || l.k === 'recpat') tests.push(this.patExpr(l, sv));
            else if (l.k === 'lit' && l.t === 'null') tests.push(`${sv} === null`);
            else tests.push(`$eq(${sv}, ${label(l)})`);
          }
          if (!c.labels) continue;
          let test = tests.join(' || ');
          if (c.guard) test = `(${test}) && (${this.expr(c.guard).c})`;
          const body = this.caseBody(c, isExpr, armTypes);
          this.scope = prevScope;
          chain.push(`if (${test}) { ${body} }`);
        }
        const code = `${sv} = ${sel.c}; ${chain.join(' else ')}${defBody !== null ? (chain.length ? ' else ' : '') + `{ ${defBody} }` : (isExpr ? (chain.length ? ' else ' : '') + `{ throw $wrap(new Error('MatchException')); }` : '')}`;
        out = isExpr ? `(() => { ${code} })()` : `{ ${code} }`;
      } else {
        const sw = `switch ($sw(${sel.c})) {`;
        const cs = [];
        for (const c of s.cases) {
          const prevScope = this.scope; this.scope = this.pushScope();
          let head;
          if (c.labels === null) { if (hasDefault) this.err(c.ln, 'duplicate default label'); hasDefault = true; head = 'default:'; }
          else { head = c.labels.map(l => `case ${label(l)}:`).join(' '); if (c.isDefault) { head += ' default:'; hasDefault = true; } }
          const body = this.caseBody(c, isExpr, armTypes);
          this.scope = prevScope;
          cs.push(`${head} { ${body} }`);
          if (!arrowForm) cs[cs.length - 1] = `${head}\n${body}`;
        }
        let tail = '';
        if (isExpr && !hasDefault) tail = ` default: throw $wrap(new Error('MatchException'));`;
        if (isExpr && !hasDefault && enumInfo && enumInfo.kind === 'enum') { /* exhaustive over enum */ }
        // a non-arrow switch needs its own block scope for `let` declarations in cases
        out = isExpr ? `(() => { ${sw}\n${cs.join('\n')}${tail}\n} })()` : `${sw}\n${cs.join('\n')}\n}`;
      }
      this.ctx.switches--;
      if (isExpr) {
        if (!hasDefault && !hasPattern && !(enumInfo && enumInfo.kind === 'enum') && selT !== 'boolean') this.err(s.ln, 'the switch expression does not cover all possible input values');
        let t = null;
        const ts = armTypes.filter(x => x !== undefined);
        if (ts.length && ts.every(x => x === ts[0])) t = ts[0];
        else if (ts.length && ts.every(x => isNumT(x))) { t = ts.reduce((a, b) => binNum(a, b)); if (t === 'double' || t === 'long' || t === 'float') out = this.coerceCode(out, null, t); }
        return { c: out, t };
      }
      return out;
    },
    caseBody(c, isExpr, armTypes) {
      if (c.arrow) {
        const b = c.body[0];
        if (b.k === 'exprval') {
          if (isExpr) { const r = this.expr(b.e); armTypes.push(r.t); return `return ${r.c};`; }
          return `${this.expr(b.e, true).c}; break;`;
        }
        if (b.k === 'block') { const code = this.stmt(b); return isExpr ? code : `${code} break;`; }
        return this.stmt(b) + (isExpr ? '' : ' break;');
      }
      return c.body.map(x => this.stmt(x)).join('\n');
    },
    /* ==================================================================
       expressions
       ================================================================== */
    exprClass(e) {
      // returns the user class a name / field chain refers to (without evaluating), or null
      if (e.k === 'name') { if (this.lookupVar(e.id) || this.lookupMemberLexical(e.id, false)) return null; return this.findClass(e.id); }
      if (e.k === 'field') { const o = this.exprClass(e.obj); if (o) return o.nested.find(n => n.name === e.name) || null; if (e.obj.k === 'name' && /^[a-z]/.test(e.obj.id) && !this.lookupVar(e.obj.id)) return this.findClass(e.name); return null; }
      return null;
    },
    libClassOf(e) {
      // library class a name/field chain refers to, or null. Reports non-imported classes.
      const L = libL().L;
      if (e.k === 'name') {
        if (this.lookupVar(e.id) || this.lookupMemberLexical(e.id, false) || this.findClass(e.id)) return null;
        const c = L[e.id];
        if (c && this.libName(e.id)) return { name: e.id, cls: c };
        return null;
      }
      if (e.k === 'field') {
        // Map.Entry, java.util.List ...
        const chain = []; let x = e;
        while (x.k === 'field') { chain.unshift(x.name); x = x.obj; }
        if (x.k !== 'name') return null;
        if (this.lookupVar(x.id) || this.lookupMemberLexical(x.id, false)) return null;
        if (/^[a-z]/.test(x.id)) { // package-qualified
          const last = chain[chain.length - 1]; if (L[last] && !this.findClass(last)) return { name: last, cls: L[last] };
          return null;
        }
        const dotted = [x.id, ...chain].join('.');
        if (L[dotted]) return { name: dotted, cls: L[dotted] };
      }
      return null;
    },
    targsOf(tn) { if (!tn || !tn.args || !tn.args.length) return null; return tn.args.map(a => (a.n === '?' || this.tparamsIn(a.n) || !this.T(a) ? null : { t: this.T(a), targs: this.targsOf(a) })); },
    fieldTargs(f) { const tn = f.tnode; if (!tn.args || !tn.args.length) return null; if (f.owner.tparams.size || (f.owner.outer && this.ownerTparam(f.owner, tn.n))) { const own = new Set(f.owner.tparams); return tn.args.map(a => (a.n === '?' || own.has(a.n) || !this.T(a) ? null : { t: this.T(a), targs: this.targsOf(a) })); } return this.targsOf(tn); },
    isConstField(f) { return !!(f.final && f.init && f.init.k === 'lit' && f.tnode.dims === 0 && (PRIM.has(f.tnode.n) || f.tnode.n === 'String')); },
    staticRef(info) { return info === this.cur || !this.needsInit(info) ? info.js : `$g(${info.js})`; },
    fieldType(f) { const tn = f.tnode; if (f.owner.tparams.has(tn.n)) return null; if (f.owner.outer && this.ownerTparam(f.owner, tn.n)) return null; return this.T(tn); },
    ownerTparam(o, n) { for (let c = o; c; c = c.outer) if (c.tparams.has(n)) return true; return false; },
    retType(m) {
      if (!m.ret) return null;
      const n = m.ret.n; if (n === 'void') return 'void';
      const tp = this.tparamsFor(m.owner, m); if (tp.has(n)) return null;
      return this.findClass(m.ret.n) || PRIM.has(n) || n === 'String' || libL().L[n] ? n + '[]'.repeat(m.ret.dims) : null;
    },
    expr(e, discard) {
      switch (e.k) {
        case 'lit': return this.literal(e);
        case 'name': return this.nameExpr(e);
        case 'paren': { const r = this.expr(e.e, discard); return { c: `(${r.c})`, t: r.t, v: r.v, fresh: r.fresh, isConst: r.isConst }; }
        case 'this': {
          if (this.ctx.isStatic) { this.err(e.ln, 'non-static variable this cannot be referenced from a static context'); return lit(this.cur.name, 'this'); }
          return lit(this.cur.name, 'this');
        }
        case 'qthis': {
          const ci = this.exprClass(e.cls);
          if (!ci) { this.err(e.ln, 'not an enclosing class'); return lit(null, 'this'); }
          const r = this.instRef(ci);
          if (!r) { this.err(e.ln, `not an enclosing class: ${ci.name}`); return lit(null, 'this'); }
          return lit(ci.name, r);
        }
        case 'field': return this.fieldExpr(e);
        case 'index': {
          const a = this.expr(e.arr), i = this.expr(e.idx);
          if (a.t && !a.t.endsWith('[]')) this.err(e.ln, `array required, but ${a.t} found`);
          if (i.t && !isIntLike(i.t) && i.t !== 'Integer') this.err(e.ln, i.t === 'long' || i.t === 'double' || i.t === 'float' ? `incompatible types: possible lossy conversion from ${i.t} to int` : `incompatible types: ${i.t} cannot be converted to int`);
          { const et = a.t && a.t.endsWith('[]') ? a.t.slice(0, -2) : null; const ac = this.guardArr(a, e.arr, `Cannot load from ${et && PRIM.has(et) ? et : 'object'} array`); return { c: `$ai(${ac}, ${i.t === 'char' ? i.c + '.c' : i.c})`, t: et, targs: null }; }
        }
        case 'call': return this.callExpr(e);
        case 'new': return this.newExpr(e);
        case 'newarr': return this.newArray(e);
        case 'arrlit': this.err(e.ln, 'illegal start of expression'); return lit(null, 'null');
        case 'unary': return this.unary(e);
        case 'incdec': return this.incdec(e, discard);
        case 'bin': return this.binary(e);
        case 'assign': return this.assign(e, discard);
        case 'cond': return this.ternary(e);
        case 'cast': return this.castExpr(e);
        case 'instanceof': return this.instanceOf(e);
        case 'lambda': return this.lambda(e);
        case 'mref': return this.methodRef(e);
        case 'switchexpr': return this.switchStmt(e, true);
        case 'classlit': { const ci = this.exprClass(e.e); return lit('Class', ci ? `$classOf(${ci.js})` : `$classOf($L[${esc(e.e.id || 'Object')}] || $JObject)`); }
        case 'ctorcall': this.err(e.ln, `call to ${e.which} must be first statement in constructor`); return lit(null, 'null');
        case 'super': this.err(e.ln, "'.' expected"); return lit(null, 'null');
      }
      this.err(e.ln, 'unsupported expression: ' + e.k);
      return lit(null, 'null');
    },
    literal(e) {
      switch (e.t) {
        case 'int': {
          let v = e.v;
          const raw = e.raw || '';
          const isDec = !/^0[xXbB0-7]/.test(raw) || raw === '0';
          if (e.neg) { if (v < -2147483648n) this.err(e.ln, 'integer number too large'); return lit('int', v < 0n ? `(${v})` : String(v)); }
          if (isDec ? v > 2147483647n : v > 4294967295n) { this.err(e.ln, `integer number too large${raw ? ': ' + raw : ''}`); return lit('int', '0'); }
          if (!isDec && v > 2147483647n) v = BigInt.asIntN(32, v);
          return lit('int', String(v));
        }
        case 'long': {
          let v = e.v; const isDec = !/^0[xXbB0-7]/.test(e.raw || '') || e.raw === '0';
          if (!isDec && !e.neg && v > 9223372036854775807n && v <= 18446744073709551615n) v = BigInt.asIntN(64, v);
          if (v > 9223372036854775807n || v < -9223372036854775808n) this.err(e.ln, 'integer number too large');
          return lit('long', v < 0n ? `(${v}n)` : `${v}n`);
        }
        case 'double': return lit('double', this.konst('d' + e.v, `$D(${Object.is(e.v, -0) ? '-0' : e.v})`));
        case 'float': return lit('float', this.konst('f' + e.v, `$F(${e.v})`));
        case 'char': return lit('char', this.konst('c' + e.v, `$Ch(${e.v})`));
        case 'String': return { c: esc(e.v), t: 'String', isConst: true };
        case 'boolean': return lit('boolean', String(e.v));
        case 'null': return lit('null', 'null');
      }
      return lit(null, 'null');
    },
    nameExpr(e) {
      const lv = this.lookupVar(e.id);
      if (lv) { this.noteUse(e, lv); return { c: lv.v.js, t: lv.v.t, v: lv.v, fresh: lv.v.fresh, isConst: !!(lv.v.final && lv.v.constInit), targs: lv.v.targs }; }
      const r = this.lookupMemberLexical(e.id, false);
      if (r) {
        const f = r.member;
        if (f.static) { const rr = lit(this.fieldType(f), `${this.isConstField(f) ? f.owner.js : this.staticRef(f.owner)}.$${e.id}`); rr.isConst = this.isConstField(f); return rr; }
        const ref = this.instRef(r.cls);
        if (!ref) { this.err(e.ln, `non-static variable ${e.id} cannot be referenced from a static context`); return lit(this.fieldType(f), 'null'); }
        return { c: `${ref}.${this.fj(f)}`, t: this.fieldType(f), field: f, targs: this.fieldTargs(f) };
      }
      if (this.hasLibParent(this.cur)) { return lit(null, `this.$${e.id}`); }
      this.err(e.ln, `cannot find symbol: variable ${e.id}`);
      return lit(null, 'null');
    },
    noteUse(e, lv) { if (this.daUse) this.daUse(e, lv); },
    rootUnresolved(e) {
      let x = e; while (x.k === 'field') x = x.obj;
      if (x.k !== 'name') return null;
      if (this.lookupVar(x.id) || this.lookupMemberLexical(x.id, false) || this.findClass(x.id) || libL().L[x.id]) return null;
      return x;
    },
    fieldExpr(e) {
      const nm = e.name;
      if (e.obj.k === 'field') { const rt = this.rootUnresolved(e.obj); if (rt) { this.err(rt.ln, `package ${rt.id} does not exist`); return lit(null, 'null'); } }
      // class-qualified static access
      const uc = this.exprClass(e.obj);
      if (uc) {
        if (nm === 'length' && false) return lit('int', '0');
        const f = this.findField(uc, nm);
        if (f) {
          if (!f.static) { this.err(e.ln, `non-static variable ${nm} cannot be referenced from a static context`); return lit(this.fieldType(f), 'null'); }
          this.checkAccess(f, uc, e.ln, nm);
          { const rr = lit(this.fieldType(f), `${this.isConstField(f) ? f.owner.js : this.staticRef(f.owner)}.$${nm}`); rr.isConst = this.isConstField(f); return rr; }
        }
        const nested = uc.nested.find(n => n.name === nm);
        if (nested) return lit(null, nested.js);
        this.err(e.ln, `cannot find symbol: variable ${nm}`);
        return lit(null, 'null');
      }
      const lc = this.libClassOf(e.obj);
      if (lc) {
        const key = lc.name + '.' + nm;
        if (!(nm in lc.cls)) { this.err(e.ln, `cannot find symbol: variable ${nm}`); return lit(null, 'null'); }
        let t = STATIC_FIELD[key] || null;
        if (key === 'System.out' || key === 'System.err') t = 'PrintStream'; else if (key === 'System.in') t = 'InputStream';
        return lit(t, `$L[${esc(lc.name)}].${nm}`);
      }
      if (e.obj.k === 'name' && !this.lookupVar(e.obj.id) && !this.lookupMemberLexical(e.obj.id, false) && !this.findClass(e.obj.id) && !libL().L[e.obj.id]) {
        this.err(e.obj.ln, `cannot find symbol: variable ${e.obj.id}`);
        return lit(null, 'null');
      }
      if (e.obj.k === 'name' && /^[A-Z]/.test(e.obj.id) && libL().L[e.obj.id] && !this.libName(e.obj.id) && !this.lookupVar(e.obj.id) && !this.findClass(e.obj.id)) {
        this.err(e.obj.ln, `cannot find symbol: variable ${e.obj.id}`);
        return lit(null, 'null');
      }
      if (e.obj.k === 'super') {
        const f = this.cur.parent && this.findField(this.cur.parent, nm);
        if (f) return lit(this.fieldType(f), `this.${this.fj(f)}`);
        this.err(e.ln, `cannot find symbol: variable ${nm}`); return lit(null, 'null');
      }
      const o = this.expr(e.obj);
      if (o.t && o.t.endsWith('[]')) {
        if (nm === 'length') return lit('int', `$alen(${this.guardArr(o, e.obj, 'Cannot read the array length')})`);
        this.err(e.ln, `cannot find symbol: variable ${nm}`); return lit(null, 'null');
      }
      if (o.t && PRIM.has(o.t)) { this.err(e.ln, `${o.t} cannot be dereferenced`); return lit(null, 'null'); }
      if (o.t === 'String') { this.err(e.ln, `cannot find symbol: variable ${nm}`); return lit(null, 'null'); }
      const oc = o.t ? this.findClass(o.t) : null;
      if (oc) {
        const f = this.findField(oc, nm);
        if (!f) {
          if (nm === 'length' && false) return lit('int', '0');
          if (!this.hasLibParent(oc)) { this.err(e.ln, `cannot find symbol: variable ${nm}`); return lit(null, 'null'); }
          return lit(null, `${o.c}.$${nm}`);
        }
        this.checkAccess(f, oc, e.ln, nm);
        if (f.static) return lit(this.fieldType(f), `${this.isConstField(f) ? f.owner.js : this.staticRef(f.owner)}.$${nm}`);
        return { c: `${this.guardArr(o, e.obj, `Cannot read field "${nm}"`)}.${this.fj(f)}`, t: this.fieldType(f), field: f, objc: o.c, targs: this.fieldTargs(f) };
      }
      if (o.t && libL().L[o.t] && !this.findClass(o.t)) { this.err(e.ln, `cannot find symbol: variable ${nm}`); return lit(null, 'null'); }
      if (nm === 'length') return lit('int', `$len(${o.c})`);
      if (o.t && BOX[o.t]) { this.err(e.ln, `cannot find symbol: variable ${nm}`); return lit(null, 'null'); }
      return lit(null, `${o.c}.$${nm}`);
    },
    checkAccess(member, ownerInfo, line, nm, isMethod) {
      if (!member.mods || !member.mods.has('private')) return;
      const top = c => { while (c.outer) c = c.outer; return c; };
      if (top(member.owner) !== top(this.cur)) this.err(line, `${nm}${isMethod ? '(' + member.params.map(p => this.T(p.type)).join(',') + ')' : ''} has private access in ${member.owner.name}`);
    },

    /* ---------- operators ---------- */
    unary(e) {
      const r = this.expr(e.e);
      const t = r.t;
      switch (e.op) {
        case '!':
          if (t && !isBool(t)) this.err(e.ln, `bad operand type ${t} for unary operator '!'`);
          return lit('boolean', `!${r.c}`);
        case '-':
          if (t && !isNumT(t)) this.err(e.ln, `bad operand type ${t} for unary operator '-'`);
          return lit(isIntLike(t) ? 'int' : unbox(t), isIntLike(t) && t !== 'char' ? `(-${r.c} | 0)` : `$neg(${r.c})`);
        case '+': if (t && !isNumT(t)) this.err(e.ln, `bad operand type ${t} for unary operator '+'`); return lit(isIntLike(t) ? 'int' : unbox(t), `$pos(${r.c})`);
        case '~': if (t && !(isIntLike(t) || unbox(t) === 'long')) this.err(e.ln, `bad operand type ${t} for unary operator '~'`); return lit(isIntLike(t) ? 'int' : unbox(t), `$bnot(${r.c})`);
      }
      return r;
    },
    binary(e) {
      const op = e.op;
      if (op === '&&' || op === '||') {
        const l = this.expr(e.l), r = this.expr(e.r);
        if ((l.t && !isBool(l.t)) || (r.t && !isBool(r.t))) this.err(e.ln, `bad operand types for binary operator '${op}'`);
        return lit('boolean', `(${l.c} ${op} ${r.c})`);
      }
      const l = this.expr(e.l), r = this.expr(e.r);
      return this.binop(op, l, r, e.ln);
    },
    binop(op, l, r, line) {
      const lt = l.t, rt = r.t;
      const bad = () => this.err(line, `bad operand types for binary operator '${op}'`);
      const lu = unbox(lt), ru = unbox(rt);
      switch (op) {
        case '+': {
          if (lt === 'String' || rt === 'String') {
            if (lt === 'void' || rt === 'void') this.err(line, "'void' type not allowed here");
            { const cst = !!(l.isConst && r.isConst); return { c: `$cat(${l.c}, ${r.c})`, t: 'String', isConst: cst, fresh: cst ? 0 : ++this.freshN }; }
          }
          if (lt && rt) {
            if (!(isNumT(lt) && isNumT(rt))) { if (PRIM.has(lt) || PRIM.has(rt) || BOX[lt] || BOX[rt]) { bad(); return lit(null, `$add(${l.c}, ${r.c})`); } }
            const t = binNum(lt, rt);
            if (t === 'int' && isIntLike(lt) && isIntLike(rt) && lt !== 'char' && rt !== 'char') return lit('int', `(${l.c} + ${r.c} | 0)`);
            return lit(t, `$add(${l.c}, ${r.c})`);
          }
          return lit(null, `$add(${l.c}, ${r.c})`);
        }
        case '-': case '*': case '/': case '%': {
          if (lt && rt && !(isNumT(lt) && isNumT(rt))) { bad(); return lit(null, `$sub(${l.c}, ${r.c})`); }
          const t = lt && rt ? binNum(lt, rt) : null;
          const fn = { '-': '$sub', '*': '$mul', '/': '$div', '%': '$mod' }[op];
          if (t === 'int' && lt !== 'char' && rt !== 'char' && isIntLike(lt) && isIntLike(rt)) {
            if (op === '-') return lit('int', `(${l.c} - ${r.c} | 0)`);
            if (op === '*') return lit('int', `Math.imul(${l.c}, ${r.c})`);
          }
          if ((op === '/' || op === '%') && t === 'int' && /^\d+$/.test(r.c) && Number(r.c) === 0) { /* runtime error like Java */ }
          return lit(t, `${fn}(${l.c}, ${r.c})`);
        }
        case '<': case '>': case '<=': case '>=': {
          if (lt && rt && !(isNumT(lt) && isNumT(rt))) { bad(); }
          if (BOX[lt] || BOX[rt] || !lt || !rt) { const fn = { '<': '$lt', '>': '$gt', '<=': '$le', '>=': '$ge' }[op]; return lit('boolean', `${fn}(${l.c}, ${r.c})`); }
          return lit('boolean', `(${l.c} ${op} ${r.c})`);
        }
        case '==': case '!=': {
          const js = op === '==' ? '===' : '!==';
          if (lt === 'null' || rt === 'null') {
            if ((lt && PRIM.has(lt)) || (rt && PRIM.has(rt))) { this.err(line, `bad operand types for binary operator '${op}'`); }
            return lit('boolean', `(${l.c} ${op === '==' ? '==' : '!='} null)`);
          }
          if (lt === 'String' && rt === 'String' && ((l.fresh || r.fresh) && l.fresh !== r.fresh)) return lit('boolean', `(${l.c}, ${r.c}, ${op === '==' ? 'false' : 'true'})`);
          if (lt && rt) {
            const lp = PRIM.has(lt), rp = PRIM.has(rt);
            const ln_ = isNumT(lt), rn = isNumT(rt);
            if ((lp || rp) && ((lt === 'boolean' || lu === 'boolean') !== (rt === 'boolean' || ru === 'boolean'))) { this.err(line, `bad operand types for binary operator '${op}'`); return lit('boolean', 'false'); }
            if ((lp && !rp && !BOX[rt] && rt !== 'Object' && rt !== 'Number' && rt !== 'Comparable') || (rp && !lp && !BOX[lt] && lt !== 'Object' && lt !== 'Number' && lt !== 'Comparable')) { this.err(line, `bad operand types for binary operator '${op}'`); return lit('boolean', 'false'); }
            if (!lp && !rp) {
              // two reference types: must be castable
              const lc = this.findClass(lt), rc = this.findClass(rt);
              if ((lt === 'String' && (rc || BOX[rt])) || (rt === 'String' && (lc || BOX[lt])) || (BOX[lt] && BOX[rt] && lt !== rt) || (lt === 'String' && BOX[rt]) || (rt === 'String' && BOX[lt])) { this.err(line, `incomparable types: ${lt} and ${rt}`); return lit('boolean', 'false'); }
              if (lc && rc && lc.kind !== 'interface' && rc.kind !== 'interface' && !this.isSub(lc, rc) && !this.isSub(rc, lc)) { this.err(line, `incomparable types: ${lt} and ${rt}`); return lit('boolean', 'false'); }
              if (BOX[lt] && BOX[rt] && lt === rt && lt !== 'Boolean') return lit('boolean', `${op === '!=' ? '!' : ''}$boxEq(${l.c}, ${r.c})`);
              return lit('boolean', `(${l.c} ${js} ${r.c})`);
            }
            if ((lt === 'int' || lt === 'boolean') && (rt === 'int' || rt === 'boolean') && lt === rt) return lit('boolean', `(${l.c} ${js} ${r.c})`);
            if (lt === 'char' && rt === 'char') return lit('boolean', `(${l.c} ${js} ${r.c})`);
          }
          return lit('boolean', `${op === '!=' ? '$ne' : '$eq'}(${l.c}, ${r.c})`);
        }
        case '&': case '|': case '^': {
          if (lt && rt) {
            if (isBool(lt) && isBool(rt)) return lit('boolean', op === '^' ? `(${l.c} !== ${r.c})` : `!!(${l.c} ${op} ${r.c})`);
            if (!((isIntLike(lt) || lu === 'long') && (isIntLike(rt) || ru === 'long'))) { bad(); return lit(null, `$and(${l.c}, ${r.c})`); }
            if (isIntLike(lt) && isIntLike(rt) && lt !== 'char' && rt !== 'char') return lit('int', `(${l.c} ${op} ${r.c})`);
          }
          const fn = { '&': '$and', '|': '$or', '^': '$xor' }[op];
          return lit(lt && rt ? binNum(lt, rt) : (lt === 'boolean' || rt === 'boolean' ? 'boolean' : null), `${fn}(${l.c}, ${r.c})`);
        }
        case '<<': case '>>': case '>>>': {
          if (lt && rt && !((isIntLike(lt) || lu === 'long') && (isIntLike(rt) || ru === 'long'))) bad();
          const fn = { '<<': '$shl', '>>': '$shr', '>>>': '$ushr' }[op];
          return lit(lu === 'long' ? 'long' : 'int', `${fn}(${l.c}, ${r.c})`);
        }
      }
      return lit(null, 'null');
    },
    ternary(e) {
      const c = this.cond(e.c);
      let a = this.expr(e.a), b = this.expr(e.b);
      let t = null;
      if (a.t && b.t) {
        if (a.t === b.t) t = a.t;
        else if ((a.t === 'char' && b.t === 'int' && e.b.k === 'lit' && Number(e.b.v) >= 0 && Number(e.b.v) < 65536) || (b.t === 'char' && a.t === 'int' && e.a.k === 'lit' && Number(e.a.v) >= 0 && Number(e.a.v) < 65536)) { t = 'char'; if (a.t === 'int') a = { c: this.coerceCode(a.c, 'int', 'char'), t }; else b = { c: this.coerceCode(b.c, 'int', 'char'), t }; }
        else if (isNumT(a.t) && isNumT(b.t)) {
          t = binNum(a.t, b.t);
          if (isIntLike(a.t) && isIntLike(b.t)) t = a.t === 'char' || b.t === 'char' ? (a.t === b.t ? 'char' : 'int') : 'int';
          a = { c: this.coerceCode(a.c, a.t, t === 'char' ? a.t : t), t }; b = { c: this.coerceCode(b.c, b.t, t === 'char' ? b.t : t), t };
        } else if (a.t === 'null') t = b.t; else if (b.t === 'null') t = a.t;
        else if (this.findClass(a.t) && this.findClass(b.t)) { const ca = this.findClass(a.t), cb = this.findClass(b.t); t = this.isSub(ca, cb) ? b.t : this.isSub(cb, ca) ? a.t : null; }
      }
      const fr = t === 'String' ? (a.fresh || b.fresh || 0) : 0;
      return { c: `(${c} ? ${a.c} : ${b.c})`, t, fresh: fr ? ++this.freshN : 0, branches: [a, b] };
    },
    castExpr(e) {
      const r = this.expr(e.e);
      this.checkType(e.type, e.ln);
      const t = this.T(e.type);
      const from = r.t;
      if (t === null) return lit(null, r.c);
      if (PRIM.has(t)) {
        if (from && !(PRIM.has(from) || BOX[from] || from === 'Object' || from === 'Number' || from === 'Comparable')) { this.err(e.ln, `incompatible types: ${from} cannot be converted to ${t}`); return lit(t, 'null'); }
        if (from && (unbox(from) === 'boolean') !== (t === 'boolean') && (PRIM.has(from) || BOX[from])) { this.err(e.ln, `incompatible types: ${from} cannot be converted to ${t}`); return lit(t, 'null'); }
        const fu = unbox(from);
        switch (t) {
          case 'int': return lit('int', fu === 'int' && from === 'int' ? r.c : fu === 'short' || fu === 'byte' ? r.c : fu === 'char' ? `${r.c}.c` : `$toInt(${r.c})`);
          case 'long': return lit('long', from === 'long' ? r.c : `$toLong(${r.c})`);
          case 'double': return lit('double', from === 'double' ? r.c : `$toDouble(${r.c})`);
          case 'float': return lit('float', from === 'float' ? r.c : `$toFloat(${r.c})`);
          case 'char': return lit('char', from === 'char' ? r.c : `$toChar(${r.c})`);
          case 'byte': return lit('byte', `$toByte(${r.c})`);
          case 'short': return lit('short', `$toShort(${r.c})`);
          case 'boolean': return lit('boolean', r.c);
        }
      }
      // reference casts
      const ci = this.findClass(t);
      if (from && (PRIM.has(from)) && !BOX[t] && t !== 'Object' && t !== 'Number' && t !== 'Comparable') { this.err(e.ln, `incompatible types: ${from} cannot be converted to ${t}`); return lit(t, r.c); }
      if (from === 'String' && (ci || BOX[t])) { this.err(e.ln, `incompatible types: String cannot be converted to ${t}`); return lit(t, r.c); }
      if (ci && from) {
        const fc = this.findClass(from);
        if (fc && fc.kind !== 'interface' && ci.kind !== 'interface' && !this.isSub(fc, ci) && !this.isSub(ci, fc)) { this.err(e.ln, `incompatible types: ${from} cannot be converted to ${t}`); return lit(t, r.c); }
        if (BOX[from]) { this.err(e.ln, `incompatible types: ${from} cannot be converted to ${t}`); return lit(t, r.c); }
      }
      if (ci) return lit(t, `$cast(${r.c}, ${ci.js})`);
      if (t.endsWith('[]')) return lit(t, `$castArr(${r.c}, ${esc(t.slice(0, -2))})`);
      const L = libL().L;
      if (L[t] && !this.tparamsIn(t) && this.libName(t)) {
        if (BOX[t] && from && PRIM.has(from)) return lit(t, r.c);
        return lit(t, `$cast(${r.c}, $L[${esc(t)}])`);
      }
      return lit(this.tparamsIn(t) ? null : t, r.c);
    },
    instanceOf(e) {
      const r = this.expr(e.e);
      if (e.pat) { const tv = this.tmp(); return lit('boolean', `((${tv} = ${r.c}), ${this.patExpr(e.pat, tv)})`); }
      this.checkType(e.type, e.ln);
      const t = this.T(e.type);
      if (r.t && PRIM.has(r.t)) { this.err(e.ln, `unexpected type\n  required: reference\n  found:    ${r.t}`); return lit('boolean', 'false'); }
      const ci = this.findClass(t);
      if (ci && r.t) {
        const fc = this.findClass(r.t);
        if (fc && fc.kind !== 'interface' && ci.kind !== 'interface' && !this.isSub(fc, ci) && !this.isSub(ci, fc)) this.err(e.ln, `incompatible types: ${r.t} cannot be converted to ${t}`);
      }
      if (r.t === 'String' && ci) this.err(e.ln, `incompatible types: String cannot be converted to ${t}`);
      let ref;
      if (t.endsWith('[]')) ref = null;
      else if (ci) ref = ci.js;
      else ref = `$L[${esc(t)}]`;
      const bv = e.bind ? this.bindVar(e) : null;
      let test;
      if (ref === null) { const tv = bv || this.tmp(); test = `(Array.isArray(${tv} = ${r.c}) && ${tv}.$t === ${esc(t.slice(0, -2))})`; }
      else test = `$inst(${bv ? `(${bv} = ${r.c})` : r.c}, ${ref})`;
      if (e.bind) { const v = this.lookupVar(e.bind); if (v) v.v.t = t; }
      return lit('boolean', `(${test})`);
    },
    /* boolean JS expression testing value `v` against a pattern; bindings are assigned to hoisted temporaries */
    patExpr(p, v) {
      if (p.k === 'typepat') {
        const isVar = p.type.n === 'var', prim = PRIM.has(p.type.n) && p.type.dims === 0;
        if (!isVar && !prim) this.checkType(p.type, p.ln);
        const t = isVar ? null : this.T(p.type);
        const js = jsName(p.name) + '$' + (this.tmpN++);
        this.ctx.tmps.push(js);
        if (this.scope) this.scope.vars.set(p.name, { t, js, kind: 'local', assigned: true, binding: true });
        let test = 'true';
        if (!isVar && !prim) { const ci = this.findClass(p.type.n); test = ci ? `$inst(${v}, ${ci.js})` : t.endsWith('[]') ? `(Array.isArray(${v}) && ${v}.$t === ${esc(t.slice(0, -2))})` : `$inst(${v}, $L[${esc(t)}])`; }
        return `(${test} && ((${js} = ${v}), true))`;
      }
      // record deconstruction pattern
      this.checkType(p.type, p.ln);
      const ci = this.findClass(p.type.n);
      if (!ci || ci.kind !== 'record') { this.err(p.ln, `deconstruction patterns can only be applied to records, ${p.type.n} is not a record`); return 'false'; }
      const comps = ci.decl.recordComps;
      if (comps.length !== p.subs.length) this.err(p.ln, 'incorrect number of nested patterns');
      const parts = [`$inst(${v}, ${ci.js})`];
      comps.forEach((c, i) => {
        if (!p.subs[i]) return;
        const tmp = this.tmp();
        parts.push(`((${tmp} = ${v}.${c.name}()), true)`);
        parts.push(this.patExpr(p.subs[i], tmp));
      });
      if (p.name) { const js = jsName(p.name) + '$' + (this.tmpN++); this.ctx.tmps.push(js); if (this.scope) this.scope.vars.set(p.name, { t: ci.name, js, kind: 'local', assigned: true, binding: true }); parts.push(`((${js} = ${v}), true)`); }
      return `(${parts.join(' && ')})`;
    },
    bindVar(e) {
      const t = this.T(e.type);
      const n = e.bind;
      const js = jsName(n) + '$' + (this.tmpN++);
      this.ctx.tmps.push(js);
      if (this.scope) this.scope.vars.set(n, { t, js, kind: 'local', assigned: true, binding: true });
      return js;
    },

    /* ---------- assignment ---------- */
    once(code) {
      if (/^[\w$.]+$/.test(code)) return [code, code];
      const t = this.tmp();
      return [`(${t} = ${code})`, t];
    },
    lvalue(t) {
      // returns {get, set(v), type, final, ...}
      switch (t.k) {
        case 'paren': return this.lvalue(t.e);
        case 'name': {
          const lv = this.lookupVar(t.id);
          if (lv) {
            const v = lv.v;
            return { type: v.t, get: v.js, set: x => `(${v.js} = ${x})`, kind: 'local', v, name: t.id, plain: true };
          }
          const r = this.lookupMemberLexical(t.id, false);
          if (r) {
            const f = r.member, ft = this.fieldType(f);
            let base;
            if (f.static) base = `${this.staticRef(f.owner)}.$${t.id}`;
            else { const ref = this.instRef(r.cls); if (!ref) { this.err(t.ln, `non-static variable ${t.id} cannot be referenced from a static context`); return { type: ft, get: 'null', set: x => x, kind: 'bad' }; } base = `${ref}.${this.fj(f)}`; }
            return { type: ft, get: base, set: x => `(${base} = ${x})`, kind: 'field', field: f, name: t.id };
          }
          if (this.hasLibParent(this.cur)) return { type: null, get: `this.$${t.id}`, set: x => `(this.$${t.id} = ${x})`, kind: 'field' };
          this.err(t.ln, `cannot find symbol: variable ${t.id}`);
          return { type: null, get: 'null', set: x => x, kind: 'bad' };
        }
        case 'field': {
          const r = this.fieldExpr(t);
          if (t.obj.k !== 'this' && !this.exprClass(t.obj) && t.name === 'length' && r.t === 'int' && r.c.startsWith('$alen')) { this.err(t.ln, 'cannot assign a value to final variable length'); return { type: 'int', get: r.c, set: x => x, kind: 'bad' }; }
          const f = r.field;
          if (r.objc !== undefined) {
            const [o1, o2] = this.once(r.objc);
            const fjn = f ? this.fj(f) : `$${t.name}`;
            const nm = `${o2}.${fjn}`;
            return { type: r.t, get: nm, first: `${o1}.${fjn}`, set: x => `(${o1}.${fjn} = ${x})`, setRepeat: x => `(${nm} = ${x})`, kind: 'field', field: f, name: t.name, objc: o1 };
          }
          return { type: r.t, get: r.c, set: x => `(${r.c} = ${x})`, kind: 'field', field: r.field, name: t.name, staticField: true };
        }
        case 'index': {
          const a = this.expr(t.arr), i = this.expr(t.idx);
          if (a.t && !a.t.endsWith('[]')) this.err(t.ln, `array required, but ${a.t} found`);
          const [a1, a2] = this.once(a.c), [i1, i2] = this.once(i.t === 'char' ? i.c + '.c' : i.c);
          const et = a.t && a.t.endsWith('[]') ? a.t.slice(0, -2) : null;
          return { type: et, get: `$ai(${a2}, ${i2})`, first: `$ai(${a1}, ${i1})`, set: x => `$as(${a1}, ${i1}, ${x})`, setRepeat: x => `$as(${a2}, ${i2}, ${x})`, kind: 'index' };
        }
      }
      this.err(t.ln, 'unexpected type\n  required: variable\n  found:    value');
      return { type: null, get: 'null', set: x => x, kind: 'bad' };
    },
    checkFinalAssign(lv, line, isInit) {
      if (lv.kind === 'local' && lv.v && lv.v.final && !lv.v.needsDA && (lv.v.assigned || lv.v.param)) this.err(line, `cannot assign a value to final variable ${lv.name}`);
      if (lv.kind === 'local' && lv.v && lv.v.param && lv.v.finalParam) this.err(line, `final parameter ${lv.name} may not be assigned`);
      if (lv.kind === 'field' && lv.field && lv.field.final && !lv.field.enumConst) {
        const inCtorOrInit = this.ctx.isCtor || (this.ctx.cls === lv.field.owner && !this.ctx.method) ;
        const sameClass = this.ctx.cls === lv.field.owner;
        if (!(sameClass && (this.ctx.isCtor || this.ctx.isClinit || !this.ctx.method) && (lv.field.init === null || lv.field.init === undefined))) this.err(line, `cannot assign a value to final variable ${lv.name}`);
      }
      if (lv.kind === 'local' && lv.v) lv.v.assigned = true;
      if (lv.kind === 'local' && lv.v && this.daAssign) this.daAssign(lv);
    },
    assign(e, discard) {
      const lv = this.lvalue(e.target);
      const op = e.op;
      if (op === '=') {
        let rv;
        if (e.v.k === 'arrlit') rv = { c: this.arrayLit(e.v, lv.type, e.ln), t: lv.type };
        else rv = this.expr(e.v);
        this.checkFinalAssign(lv, e.ln);
        if (lv.kind === 'bad') return lit(null, rv.c);
        if (lv.kind === 'local' && lv.v) lv.v.fresh = lv.type === 'String' ? rv.fresh : 0;
        if (rv.t === 'void') { this.err(e.ln, 'incompatible types: void cannot be converted to ' + (lv.type || 'Object')); }
        let code;
        if (lv.type) code = this.convert(rv, lv.type, e.ln, e.v);
        else if (lv.kind === 'index' || lv.kind === 'field') code = `$like(${lv.get}, ${rv.c})`;
        else code = rv.c;
        if (lv.kind === 'index') return lit(lv.type, lv.set(code));
        return lit(lv.type, lv.set(code));
      }
      const bop = op.slice(0, -1);
      const rv = this.expr(e.v);
      this.checkFinalAssign(lv, e.ln);
      if (lv.kind === 'bad') return lit(null, rv.c);
      const cur = { c: lv.first && lv.kind !== 'local' ? lv.first : lv.get, t: lv.type };
      // the first evaluation of the target must come first; later uses use the repeat form
      const rc = lv.kind === 'local' ? lv.get : lv.get;
      const bin = this.binop(bop, { c: lv.get, t: lv.type }, rv, e.ln);
      // narrowing assignment conversion back to the target type
      let code = bin.c;
      if (lv.type === 'String') { /* concatenation */ }
      else if (lv.type && PRIM.has(lv.type)) code = this.castCode(bin.c, bin.t, lv.type);
      else if (lv.type && BOX[lv.type]) code = this.castCode(bin.c, bin.t, BOX[lv.type]);
      else if (!lv.type) code = lv.kind === 'local' ? `$like(${lv.get}, ${bin.c})` : `$like(${lv.get}, ${bin.c})`;
      if (lv.first) return lit(lv.type, (lv.setRepeat ? this.reorder(lv, code) : lv.set(code)));
      return lit(lv.type, lv.set(code));
    },
    reorder(lv, code) {
      // `once` placed the object/array evaluation in lv.first; make sure it is evaluated exactly once, before the right side
      return lv.set(code);
    },
    castCode(code, from, to) {
      const fu = from;
      switch (to) {
        case 'int': return fu === 'int' ? code : fu === 'char' ? `${code}.c` : `$toInt(${code})`;
        case 'long': return fu === 'long' ? code : `$toLong(${code})`;
        case 'double': return fu === 'double' ? code : `$toDouble(${code})`;
        case 'float': return fu === 'float' ? code : `$toFloat(${code})`;
        case 'char': return fu === 'char' ? code : `$toChar(${code})`;
        case 'short': return `$toShort(${code})`;
        case 'byte': return `$toByte(${code})`;
      }
      return code;
    },
    incdec(e, discard) {
      const lv = this.lvalue(e.e);
      if (lv.type && !isNumT(lv.type)) this.err(e.ln, `bad operand type ${lv.type} for unary operator '${e.op}'`);
      this.checkFinalAssign(lv, e.ln);
      if (lv.kind === 'bad') return lit(null, 'null');
      const d = e.op === '++' ? 1 : -1;
      const intFast = lv.type === 'int';
      const narrow = lv.type === 'byte' ? '$toByte' : lv.type === 'short' ? '$toShort' : null;
      const newv = narrow ? `${narrow}(${lv.get} + ${d})` : intFast ? `(${lv.get} ${d > 0 ? '+' : '-'} 1 | 0)` : `$incv(${lv.get}, ${d})`;
      const t = lv.type;
      if (discard || e.prefix) {
        const code = lv.first ? lv.set(newv) : lv.set(newv);
        return lit(t, code);
      }
      // postfix value needed: return old value
      const tm = this.tmp();
      const first = lv.first || lv.get;
      const setv = lv.first ? (lv.setRepeat ? lv.setRepeat : lv.set) : lv.set;
      const upd = narrow ? `${narrow}(${tm} + ${d})` : intFast ? `(${tm} + ${d > 0 ? 1 : -1} | 0)` : `$incv(${tm}, ${d})`;
      return lit(t, `(${tm} = ${first}, ${setv(upd)}, ${tm})`);
    },
    newArray(e) {
      this.checkType(e.elem, e.ln);
      const base = this.T(e.elem);
      if (e.init) { const t = base + '[]'.repeat(e.extra); return lit(t, this.arrayLit(e.init, t, e.ln)); }
      const dims = e.dims.map(d => { const r = this.expr(d); if (r.t && !isIntLike(r.t) && r.t !== 'Integer') this.err(d.ln, r.t === 'long' || r.t === 'double' || r.t === 'float' ? `incompatible types: possible lossy conversion from ${r.t} to int` : `incompatible types: ${r.t} cannot be converted to int`); return r.t === 'char' ? r.c + '.c' : r.c; });
      return lit(base + '[]'.repeat(e.dims.length + e.extra), `$newArr(${esc(base)}, [${dims.join(', ')}], ${e.extra})`);
    },
    lambda(e) {
      const prevScope = this.scope, prevTmps = this.ctx.tmps, prevRet = this.ctx.ret, prevLoops = this.ctx.loops, prevSw = this.ctx.switches;
      this.scope = this.pushScope();
      this.ctx.tmps = []; this.ctx.inLambda++; this.ctx.loops = 0; this.ctx.switches = 0;
      const names = e.params.map(p => {
        const js = jsName(p.name);
        if (p.type) this.checkType(p.type, e.ln);
        this.declare(p.name, { t: p.type && p.type.n !== 'var' ? this.T(p.type) : null, js, kind: 'local', assigned: true, param: true, lambdaParam: true }, e.ln);
        return js;
      });
      let body;
      if (e.body.k === 'block') {
        const code = e.body.body.map(s => this.stmt(s)).join('\n');
        body = `{ ${this.ctx.tmps.length ? `let ${this.ctx.tmps.join(',')};` : ''} ${code} }`;
      } else {
        const r = this.expr(e.body, false);
        body = this.ctx.tmps.length ? `{ let ${this.ctx.tmps.join(',')}; return ${r.c}; }` : `(${r.c})`;
      }
      this.ctx.inLambda--; this.ctx.tmps = prevTmps; this.ctx.ret = prevRet; this.ctx.loops = prevLoops; this.ctx.switches = prevSw;
      this.scope = prevScope;
      return lit('$lambda', `$lam((${names.join(', ')}) => ${body})`);
    },
    methodRef(e) {
      const nm = e.name;
      if (e.type) { // int[]::new
        return lit(null, `$lam(n => $newArr(${esc(this.T({ ...e.type, dims: 0 }))}, [n], ${Math.max(0, e.type.dims - 1)}))`);
      }
      const ci = this.exprClass(e.obj);
      if (nm === 'new') {
        if (ci) return lit(null, `$lam((...a) => $new(${ci.js}, ...a))`);
        const lc = this.libClassOf(e.obj);
        if (lc) return lit(null, `$lam((...a) => $new($L[${esc(lc.name)}], ...a))`);
        this.err(e.ln, `cannot find symbol: class ${e.obj.id || '?'}`);
        return lit(null, 'null');
      }
      if (ci) {
        const ms = this.findMethods(ci, nm);
        if (!ms.length && !this.hasLibParent(ci)) { this.err(e.ln, `invalid method reference\n  cannot find symbol: method ${nm}`); return lit(null, 'null'); }
        return lit(null, `$mref(${this.staticRef(ci)}, ${esc(nm)}, 'type')`);
      }
      const lc = this.libClassOf(e.obj);
      if (lc) {
        if (!(nm in lc.cls) && !(lc.cls.prototype && nm in lc.cls.prototype) && !['String', 'Object', 'Integer', 'Double', 'Long', 'Character', 'Boolean', 'Comparable', 'Number'].includes(lc.name) && !L_hasStrM(nm)) { this.err(e.ln, `invalid method reference\n  cannot find symbol: method ${nm}`); }
        return lit(null, `$mref($L[${esc(lc.name)}], ${esc(nm)}, 'type')`);
      }
      if (e.obj.k === 'super') return lit(null, `$lam((...a) => super.${nm}(...a))`);
      const r = this.expr(e.obj);
      return lit(null, `$mref(${r.c}, ${esc(nm)}, 'bound')`);
    },
  });

  const L_hasStrM = nm => true;
  const L_isCollection = to => !!libL().L[to] && !!libL().L[to].$supers && libL().L[to].$supers.has(libL().L.Collection);
  const ITER_TYPES = new Set(['List', 'ArrayList', 'LinkedList', 'Vector', 'Stack', 'Queue', 'Deque', 'ArrayDeque', 'PriorityQueue', 'Set', 'HashSet', 'TreeSet', 'LinkedHashSet', 'Collection', 'SortedSet', 'NavigableSet', 'Iterable']);
  JavaGen.helpers = { lit, isBool, argStr, jsName, BOXED_OBJ, IFACE_CHECK_MAP, ITER_TYPES };
})();
