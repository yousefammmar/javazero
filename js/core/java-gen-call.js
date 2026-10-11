/* JavaZero compiler, stage 2c: method calls, object creation, overload resolution. */
(() => {
  const { Gen, PRIM, BOX, esc, isNumT, unbox, isIntLike, binNum, OBJ_RET, STR_RET, STATIC_RET, libL } = JavaGen;
  const { lit, BOXED_OBJ, IFACE_CHECK_MAP, jsName } = JavaGen.helpers;
  const proto = Gen.prototype;
  const OBJECT_METHODS = { toString: 'String', equals: 'boolean', hashCode: 'int', getClass: 'Class', notify: 'void', notifyAll: 'void', wait: 'void' };
  const argT = a => a.t || 'Object';
  const W = s => new Set(s.split(' '));
  const COLL_M = 'add addAll clear contains containsAll isEmpty iterator remove removeAll removeIf retainAll size stream parallelStream toArray forEach equals hashCode toString spliterator getClass';
  const LIST_M = COLL_M + ' get set indexOf lastIndexOf listIterator replaceAll sort subList getFirst getLast removeFirst removeLast addFirst addLast reversed';
  const QUEUE_M = COLL_M + ' element offer peek poll';
  const DEQUE_M = QUEUE_M + ' addFirst addLast descendingIterator getFirst getLast offerFirst offerLast peekFirst peekLast pollFirst pollLast pop push removeFirst removeLast removeFirstOccurrence removeLastOccurrence reversed';
  const MAP_M = 'clear compute computeIfAbsent computeIfPresent containsKey containsValue entrySet equals forEach get getOrDefault hashCode isEmpty keySet merge put putAll putIfAbsent remove replace replaceAll size values toString getClass';
  const REAL = {
    List: W(LIST_M), ArrayList: W(LIST_M + ' clone ensureCapacity trimToSize'), LinkedList: W(LIST_M + ' ' + DEQUE_M + ' clone'), Collection: W(COLL_M), Queue: W(QUEUE_M), Deque: W(DEQUE_M), ArrayDeque: W(DEQUE_M + ' clone'),
    Set: W(COLL_M), HashSet: W(COLL_M + ' clone'), LinkedHashSet: W(COLL_M + ' clone getFirst getLast removeFirst removeLast addFirst addLast reversed'), Map: W(MAP_M), HashMap: W(MAP_M + ' clone'), LinkedHashMap: W(MAP_M + ' clone'),
    Stack: W(LIST_M + ' push pop peek empty search addElement elementAt firstElement lastElement removeAllElements insertElementAt removeElement clone capacity'),
  };
  const ELEM_FIRST = W('getFirst getLast removeFirst removeLast pop peek poll element first last pollFirst pollLast peekFirst peekLast floor ceiling higher lower');
  const ELEM_ADD = W('add addFirst addLast offer push offerFirst offerLast');
  const LISTS = W('List ArrayList LinkedList Vector Stack Queue Deque ArrayDeque PriorityQueue Set HashSet TreeSet LinkedHashSet Collection SortedSet NavigableSet');
  const MAPS = W('Map HashMap TreeMap LinkedHashMap SortedMap NavigableMap');

  Object.assign(proto, {
    pickMethod(cands, args, line, name, ownerCls, kind) {
      const n = args.length;
      const fits = m => m.params.some(p => p.varargs) ? n >= m.params.length - 1 : m.params.length === n;
      let ok = cands.filter(fits);
      if (!ok.length) {
        const owner = ownerCls ? ownerCls.name : '';
        if (cands.length === 1) {
          const m = cands[0];
          this.err(line, `${kind || 'method'} ${name} in ${ownerCls && ownerCls.kind === 'interface' ? 'interface' : ownerCls && ownerCls.kind === 'enum' ? 'enum' : 'class'} ${m.owner.name} cannot be applied to given types;` + ` required: ${m.params.map(p => this.T(p.type)).join(',') || 'no arguments'}; found: ${args.map(argT).join(',') || 'no arguments'}; reason: actual and formal argument lists differ in length`);
        } else this.err(line, `no suitable ${kind || 'method'} found for ${name}(${args.map(argT).join(',')})`);
        return null;
      }
      if (ok.length > 1) {
        // prefer candidates whose parameter types accept the static argument types
        const narrowed = ok.filter(m => this.argsCompatible(m, args));
        if (narrowed.length) ok = narrowed;
        // collapse overrides (same signature declared in different classes): keep the nearest
        const seen = new Set(); ok = ok.filter(m => { const k = m.sig; if (seen.has(k)) return false; seen.add(k); return true; });
      }
      if (ok.length === 1) { this.checkArgTypes(ok[0], args, line, kind); return ok[0]; }
      // several overloads remain: runtime dispatch decides; check that at least one accepts the args
      const compat = ok.filter(m => this.argsCompatible(m, args));
      if (!compat.length && args.every(a => a.t)) { this.err(line, `no suitable ${kind || 'method'} found for ${name}(${args.map(argT).join(',')})`); return null; }
      return ok;
    },
    argsCompatible(m, args) {
      const tp = this.tparamsFor(m.owner, m);
      for (let i = 0; i < args.length; i++) {
        const p = m.params[Math.min(i, m.params.length - 1)];
        if (!p) return false;
        let pt = this.T(p.type); if (!pt || tp.has(p.type.n)) continue;
        if (p.varargs && i >= m.params.length - 1) { if (args.length === m.params.length && args[i].t && args[i].t.endsWith('[]')) { /* array passed directly */ } else pt = pt; }
        const at = args[i].t; if (!at || at === '$lambda') continue;
        if (!this.compatible(at, p.varargs && i >= m.params.length - 1 && !(args[i].t || '').endsWith('[]') ? pt : pt)) { if (p.varargs && at.endsWith('[]')) continue; return false; }
      }
      return true;
    },
    compatible(from, to) {
      if (from === to || !from || !to) return true;
      const errs = this.errs.length;
      const saved = this.errs; this.errs = [];
      this.checkAssign(from, to, 0, null);
      const bad = this.errs.length > 0; this.errs = saved;
      return !bad;
    },
    checkArgTypes(m, args, line, kind) {
      if (m.params.some(p => p.varargs)) return;
      const tp = this.tparamsFor(m.owner, m);
      for (let i = 0; i < args.length; i++) {
        const p = m.params[i]; const pt = this.T(p.type);
        if (!pt || tp.has(p.type.n) || !args[i].t || args[i].t === '$lambda') continue;
        const before = this.errs.length;
        this.checkAssign(args[i].t, pt, line);
        if (this.errs.length > before) {
          // javac reports the whole call as not applicable for a single candidate
          const e = this.errs.pop();
          this.err(line, e.msg);
          return;
        }
      }
    },
    /* ---- javac-style compile-time overload resolution (phases: strict, boxing; then most specific) ---- */
    ctorCands(info) {
      if (info.kind !== 'record') return info.ctors;
      const comps = info.decl.recordComps;
      const explicit = info.ctors.filter(c => !c.decl.compact);
      const canon = { name: '<init>', decl: { tparams: [] }, params: comps.map(c => ({ type: c.type, name: c.name })), owner: info, static: false, abstract: false, mods: new Set(), ln: info.decl.ln, throws: [], sig: this.sigKey(comps.map(c => ({ type: c.type })), info.tparams) };
      return explicit.some(c => c.params.length === comps.length) ? explicit : [...explicit, canon];
    },
    ptypes(m) { const tp = this.tparamsFor(m.owner, m); return m.params.map(p => (tp.has(p.type.n) ? 'Object' : this.T(p.type)) + (p.varargs ? '[]' : '')); },
    widens(a, b) { const W = { byte: ['short', 'int', 'long', 'float', 'double'], short: ['int', 'long', 'float', 'double'], char: ['int', 'long', 'float', 'double'], int: ['long', 'float', 'double'], long: ['float', 'double'], float: ['double'] }; return a === b || (W[a] || []).includes(b); },
    strictSub(from, to) {
      if (from === to) return true;
      if (from === 'null') return !PRIM.has(to);
      if (PRIM.has(from)) return PRIM.has(to) && this.widens(from, to);
      if (PRIM.has(to)) return false;
      if (to === 'Object') return true;
      if (from.endsWith('[]')) { if (to.endsWith('[]')) { const a = from.slice(0, -2), b = to.slice(0, -2); return PRIM.has(a) || PRIM.has(b) ? a === b : this.strictSub(a, b); } return to === 'Cloneable' || to === 'Serializable'; }
      if (to.endsWith('[]')) return false;
      const fc = this.findClass(from), tc = this.findClass(to), L = libL().L;
      if (fc && tc) return this.isSub(fc, tc);
      if (fc) return !!L[to] && this.userImplementsLib(fc, to);
      if (tc) return false;
      if (from === 'String') return ['CharSequence', 'Comparable', 'Serializable'].includes(to);
      if (BOX[from]) return to === 'Comparable' || to === 'Serializable' || (to === 'Number' && BOX[from] !== 'boolean' && BOX[from] !== 'char');
      return !!(L[from] && L[to] && (L[from] === L[to] || (L[from].$supers && L[from].$supers.has(L[to]))));
    },
    looseSub(from, to) {
      if (this.strictSub(from, to)) return true;
      if (PRIM.has(from) && !PRIM.has(to)) { const bx = Object.keys(BOX).find(k => BOX[k] === from); return this.strictSub(bx, to); }
      if (!PRIM.has(from) && PRIM.has(to) && BOX[from]) return this.widens(BOX[from], to);
      return false;
    },
    resolveOverload(cands, args) {
      if (args.some(a => !a.t || a.t === '$lambda')) return null;
      const phases = [(a, p) => this.strictSub(a, p), (a, p) => this.looseSub(a, p)];
      for (const ok of phases) {
        const app = cands.filter(m => !m.params.some(p => p.varargs) && m.params.length === args.length && this.ptypes(m).every((pt, i) => ok(args[i].t, pt)));
        if (!app.length) continue;
        if (app.length === 1) return app[0];
        const best = app.filter(m1 => app.every(m2 => m1 === m2 || this.ptypes(m1).every((p1, i) => this.strictSub(p1, this.ptypes(m2)[i]))));
        return best.length === 1 ? best[0] : null;
      }
      // phase 3: variable arity
      const va = cands.filter(m => { const pt = this.ptypes(m), n = pt.length; if (!m.params[n - 1].varargs || args.length < n - 1) return false; const el = pt[n - 1].slice(0, -2); return pt.slice(0, n - 1).every((p, i) => this.looseSub(args[i].t, p)) && args.slice(n - 1).every(a => this.looseSub(a.t, el)); });
      return va.length ? va[0] : null;
    },
    finalPick(pick, args) { if (!pick) return null; if (!Array.isArray(pick)) return pick; return pick.length === 1 ? pick[0] : this.resolveOverload(pick, args); },
    /* JS method name to call for a resolved user method (mangled when the name is overloaded anywhere) */
    callName(pick, args) {
      const cands = Array.isArray(pick) ? pick : [pick];
      const name = cands[0].name;
      if (!this.overloaded(name)) return name;
      const m = cands.length === 1 ? cands[0] : this.resolveOverload(cands, args);
      if (m && !m.abstract && !m.params.some(p => this.tparamsFor(m.owner, m).has(p.type.n))) return this.jsMethod(m);
      return name;
    },
    ctorName(info, pick, args) {
      if (info.ctors.length < 2 || info.kind === 'record') return '$init';
      const cands = Array.isArray(pick) ? pick : pick ? [pick] : [];
      const m = cands.length === 1 ? cands[0] : this.resolveOverload(cands.length ? cands : info.ctors, args);
      if (m && !m.params.some(p => info.tparams.has(p.type.n))) return '$init$' + JavaGen.mangle(this.sigKey(m.params, info.tparams));
      return '$init';
    },
    retOf(pick) {
      if (Array.isArray(pick)) { const ts = [...new Set(pick.map(m => this.retType(m)))]; return ts.length === 1 ? ts[0] : null; }
      return this.retType(pick);
    },
    paramList(m) { return m.params.map(p => this.T(p.type)).join(','); },
    methodDesc(t, name, args) {
      const owner = this.qualName(t || 'Object');
      const ci = t ? this.findClass(t) : null;
      let params;
      if (ci) { const m = this.findMethods(ci, name).find(x => x.params.length === args.length); if (m) params = m.params.map(p => this.T(p.type) || 'Object').join(', '); }
      if (params === undefined && t === 'String') { const S = { charAt: 'int', equals: 'Object', contains: 'CharSequence', indexOf: 'String', lastIndexOf: 'String', startsWith: 'String', endsWith: 'String', split: 'String', compareTo: 'String', compareToIgnoreCase: 'String', equalsIgnoreCase: 'String', concat: 'String', replace: 'CharSequence, CharSequence', replaceAll: 'String, String', matches: 'String', repeat: 'int', substring: args.length === 1 ? 'int' : 'int, int' }; params = S[name]; }
      if (params === undefined) params = args.map(a => (a.t && PRIM.has(a.t) ? a.t : 'Object')).join(', ');
      return `${owner}.${name}(${params})`;
    },
    guardNull(recv, e, args) {
      const k = e.obj.k;
      if (['this', 'super', 'new', 'newarr', 'lit', 'qthis', 'classlit', 'lambda', 'mref', 'switchexpr'].includes(k)) return recv.c;
      if (recv.c === 'null' || /^"/.test(recv.c)) return recv.c;
      if (k === 'field') { const base = e.obj.obj; if (this.libClassOf(base) || this.exprClass(base)) return recv.c; }
      const msg = `Cannot invoke "${this.methodDesc(recv.t, e.name, args)}"` + this.nullWhy(e.obj, recv);
      return `(${recv.c} ?? $np(${esc(msg)}))`;
    },
    argCode(args) { return args.map(a => a.c).join(', '); },
    /* varargs packaging at the call site (javac does this); `pick` is the resolved method(s) */
    packArgs(pick, args) {
      const m = this.finalPick(pick, args);
      if (!m || !m.params.some(p => p.varargs)) return this.argCode(args);
      const n = m.params.length, fixed = n - 1, last = m.params[n - 1];
      const et = this.T(last.type);
      if (args.length === n && args[n - 1].t && (args[n - 1].t === 'null' || args[n - 1].t.endsWith('[]'))) return this.argCode(args);
      const head = args.slice(0, fixed).map(a => a.c);
      const rest = args.slice(fixed).map(a => (et ? this.coerceCode(a.c, a.t, et) : a.c));
      return [...head, `$arr(${esc(et || 'Object')}, [${rest.join(', ')}])`].join(', ');
    },

    /* ================= method calls ================= */
    callExpr(e) {
      const name = e.name;
      const args = e.args.map(a => this.expr(a));
      const ac = this.argCode(args);
      // ---- unqualified ----
      if (!e.obj) {
        const r = this.lookupMemberLexical(name, true);
        if (r) {
          const pick = this.pickMethod(r.members, args, e.ln, name, r.cls);
          if (!pick) return lit(null, `null`);
          const p0 = Array.isArray(pick) ? pick[0] : pick;
          const allStatic = (Array.isArray(pick) ? pick : [pick]).every(m => m.static);
          const t = this.retOf(pick);
          const cn = this.callName(pick, args); const ac = this.packArgs(pick, args);
          if (allStatic) { const rr = lit(t, `${this.staticRef(p0.owner)}.${cn}(${ac})`); rr.callDesc = `${this.binName(p0.owner)}.${name}(${p0.params.map(q => this.T(q.type) || 'Object').join(', ')})`; return rr; }
          const ref = this.instRef(r.cls);
          if (!ref) { this.err(e.ln, `non-static method ${name}(${this.paramList(p0)}) cannot be referenced from a static context`); return lit(t, 'null'); }
          { const rr = lit(t, `${ref}.${cn}(${ac})`); rr.callDesc = `${this.binName(p0.owner)}.${name}(${p0.params.map(q => this.T(q.type) || 'Object').join(', ')})`; return rr; }
        }
        const en = (() => { for (let c = this.cur; c; c = c.outer) if (c.kind === 'enum') return c; return null; })();
        if (en && (name === 'values' || name === 'valueOf')) return lit(name === 'values' ? en.name + '[]' : en.name, `${this.staticRef(en)}.${name}(${ac})`);
        if (this.hasLibParent(this.cur) || OBJECT_METHODS[name]) {
          if (this.ctx.isStatic && !OBJECT_METHODS[name]) { this.err(e.ln, `cannot find symbol: method ${name}(${args.map(argT).join(',')})`); return lit(null, 'null'); }
          if (this.ctx.isStatic) { this.err(e.ln, `non-static method ${name}() cannot be referenced from a static context`); return lit(null, 'null'); }
          return lit(OBJECT_METHODS[name] || (this.cur.kind === 'enum' ? { name: 'String', ordinal: 'int', compareTo: 'int' }[name] : null) || null, `this.${name}(${ac})`);
        }
        this.err(e.ln, `cannot find symbol: method ${name}(${args.map(argT).join(',')})`);
        return lit(null, 'null');
      }
      // ---- super.m() ----
      if (e.obj.k === 'super') {
        if (this.ctx.isStatic) { this.err(e.ln, 'non-static variable super cannot be referenced from a static context'); return lit(null, 'null'); }
        const par = this.cur.parent;
        let cands = par ? this.findMethods(par, name) : [];
        for (const i of this.cur.ifaces) cands = cands.concat(this.findMethods(i, name).filter(m => !m.abstract));
        if (cands.length) {
          const pick = this.pickMethod(cands, args, e.ln, name, par || this.cur);
          if (!pick) return lit(null, 'null');
          const p0 = Array.isArray(pick) ? pick[0] : pick;
          if (p0.abstract) { this.err(e.ln, `abstract method ${name}(${this.paramList(p0)}) in ${p0.owner.name} cannot be accessed directly`); }
          if (this.overloaded(name)) { const cn = this.callName(pick, args); if (cn !== name) return lit(this.retOf(pick), `super.${cn}(${ac})`); return lit(this.retOf(pick), `$ovlsup(${(par || p0.owner).js}, this, ${esc(name)}, [${ac}])`); }
          return lit(this.retOf(pick), `super.${name}(${ac})`);
        }
        if (par && !this.hasLibParent(par) && !OBJECT_METHODS[name]) { this.err(e.ln, `cannot find symbol: method ${name}(${args.map(argT).join(',')})`); return lit(null, 'null'); }
        return lit(OBJECT_METHODS[name] || null, `super.${name}(${ac})`);
      }
      // ---- Iface.super.m() ----
      if (e.obj.k === 'field' && e.obj.name === 'super') {
        const ic = this.exprClass(e.obj.obj);
        if (ic) return lit(this.retOf(this.findMethods(ic, name)), `${ic.js}.$defaults.${this.overloaded(name) ? name + '$' + JavaGen.mangle((this.findMethods(ic, name)[0] || {}).sig || '') : name}.call(this${args.length ? ', ' : ''}${ac})`);
      }
      // ---- Class.staticMethod() ----
      const uc = this.exprClass(e.obj);
      if (uc) {
        if (uc.kind === 'enum' && (name === 'values' || name === 'valueOf') && !this.findMethods(uc, name).length) {
          return lit(name === 'values' ? uc.name + '[]' : uc.name, `${this.staticRef(uc)}.${name}(${ac})`);
        }
        const cands = this.findMethods(uc, name);
        if (!cands.length) {
          if (this.hasLibParent(uc) && uc.parentLib) { return lit(null, `${this.staticRef(uc)}.${name}(${ac})`); }
          this.err(e.ln, `cannot find symbol: method ${name}(${args.map(argT).join(',')})`);
          return lit(null, 'null');
        }
        const pick = this.pickMethod(cands, args, e.ln, name, uc);
        if (!pick) return lit(null, 'null');
        const p0 = Array.isArray(pick) ? pick[0] : pick;
        if (!(Array.isArray(pick) ? pick : [pick]).every(m => m.static)) {
          // calling an instance method through the class name is only valid for Outer.this-style code
          if (!(this.instRef(uc))) { this.err(e.ln, `non-static method ${name}(${this.paramList(p0)}) cannot be referenced from a static context`); return lit(this.retOf(pick), 'null'); }
          return lit(this.retOf(pick), `${this.instRef(uc)}.${this.callName(pick, args)}(${ac})`);
        }
        this.checkAccess(p0, uc, e.ln, name, true);
        return lit(this.retOf(pick), `${this.staticRef(p0.owner)}.${this.callName(pick, args)}(${this.packArgs(pick, args)})`);
      }
      const lc = this.libClassOf(e.obj);
      if (lc) return this.libStatic(e, lc, args, ac);
      if (e.obj.k === 'name' && !this.lookupVar(e.obj.id) && !this.lookupMemberLexical(e.obj.id, false) && !this.findClass(e.obj.id)) {
        this.err(e.obj.ln, `cannot find symbol: variable ${e.obj.id}`);
        return lit(null, 'null');
      }
      // ---- expression.method() ----
      if (e.obj.k === 'field') { const rt = this.rootUnresolved(e.obj); if (rt) { this.err(rt.ln, `package ${rt.id} does not exist`); return lit(null, 'null'); } }
      const recv = this.expr(e.obj);
      return this.instanceCall(e, recv, args, ac);
    },
    libStatic(e, lc, args, ac) {
      const name = e.name, cls = lc.cls, cn = lc.name;
      const known = typeof cls[name] === 'function';
      if (!known) {
        // maybe an inherited static (e.g. Integer.toString) or an instance method called wrongly
        this.err(e.ln, `cannot find symbol: method ${name}(${args.map(argT).join(',')})`);
        return lit(null, 'null');
      }
      let t = STATIC_RET[cn + '.' + name] || null;
      const a0 = args[0] && args[0].t, a1 = args[1] && args[1].t;
      if (cn === 'Math') {
        if (name === 'abs' || name === 'negateExact' || name === 'incrementExact' || name === 'decrementExact') t = a0 ? (isIntLike(a0) ? 'int' : unbox(a0)) : null;
        else if (name === 'max' || name === 'min' || name === 'addExact' || name === 'subtractExact' || name === 'multiplyExact' || name === 'floorMod' || name === 'floorDiv') t = a0 && a1 ? binNum(a0, a1) : null;
        else if (name === 'round') t = a0 === 'float' ? 'int' : 'long';
        else if (name === 'toIntExact') t = 'int';
      }
      if (cn === 'Character' && (name === 'toUpperCase' || name === 'toLowerCase')) t = a0 === 'char' ? 'char' : 'int';
      if (cn === 'Character' && name === 'valueOf') t = 'Character';
      if (cn === 'Integer' && name === 'valueOf') t = 'Integer';
      if (cn === 'Double' && name === 'valueOf') t = 'Double';
      if (cn === 'Math' && (name === 'sqrt' || name === 'pow') && args.length && args.some(a => a.t === 'String' || a.t === 'boolean')) this.err(e.ln, `incompatible types: ${args.find(a => a.t === 'String' || a.t === 'boolean').t} cannot be converted to double`);
      // simple argument type checks for the most common numeric statics
      if (cn === 'Math' && ['sqrt', 'pow', 'floor', 'ceil', 'sin', 'cos', 'abs', 'max', 'min', 'round'].includes(name)) for (const a of args) if (a.t === 'String' || a.t === 'boolean') this.err(e.ln, `incompatible types: ${a.t} cannot be converted to ${name === 'round' ? 'double' : 'double'}`);
      if ((cn === 'Integer' && name === 'parseInt') || (cn === 'Double' && name === 'parseDouble')) { if (a0 && a0 !== 'String' && a0 !== 'null') this.err(e.ln, `incompatible types: ${a0} cannot be converted to String`); }
      let ref = /^[A-Za-z_]\w*$/.test(cn) ? `$L.${cn}` : `$L[${esc(cn)}]`;
      return lit(t, `${ref}.${name}(${ac})`);
    },
    isDirectType(t) {
      if (!t) return false;
      if (t.endsWith('[]')) return true;
      if (this.findClass(t)) return true;
      if (BOXED_OBJ.has(t) || PRIM.has(t)) return false;
      const L = libL().L;
      return !!L[t] && t !== 'Object';
    },
    instanceCall(e, recv, args, ac) {
      const name = e.name, t = recv.t;
      const rc = this.guardNull(recv, e, args);
      if (t && PRIM.has(t)) { this.err(e.ln, `${t} cannot be dereferenced`); return lit(null, 'null'); }
      if (t === 'void') { this.err(e.ln, 'void cannot be dereferenced'); return lit(null, 'null'); }
      // arrays
      if (t && t.endsWith('[]')) {
        if (name === 'length') { this.err(e.ln, `cannot find symbol: method length()`); return lit('int', '0'); }
        if (name === 'clone') return lit(t, `$inv(${rc}, 'clone')`);
        if (!['equals', 'hashCode', 'toString', 'getClass'].includes(name)) { this.err(e.ln, `cannot find symbol: method ${name}(${args.map(argT).join(',')})`); return lit(null, 'null'); }
        return lit(OBJECT_METHODS[name], `$inv(${rc}, ${esc(name)}${args.length ? ', ' + ac : ''})`);
      }
      const L = libL().L;
      // user class receiver
      const ci = t ? this.findClass(t) : null;
      if (ci) {
        const cands = this.findMethods(ci, name);
        if (!cands.length) {
          if (OBJECT_METHODS[name]) return lit(OBJECT_METHODS[name], `${rc}.${name}(${ac})`);
          if (this.hasLibParent(ci) || ci.kind === 'enum' || ci.kind === 'record') {
            let rt = ci.kind === 'enum' ? { name: 'String', ordinal: 'int', compareTo: 'int', getDeclaringClass: 'Class' }[name] : null;
            if (ci.kind === 'record' && !args.length && ci.fields.has(name) && ci.fields.get(name).comp) rt = this.fieldType(ci.fields.get(name));
            return lit(rt || null, `${rc}.${name}(${ac})`);
          }
          this.err(e.ln, `cannot find symbol: method ${name}(${args.map(argT).join(',')})`);
          return lit(null, 'null');
        }
        const pick = this.pickMethod(cands, args, e.ln, name, ci);
        if (!pick) return lit(null, 'null');
        const p0 = Array.isArray(pick) ? pick[0] : pick;
        this.checkAccess(p0, ci, e.ln, name, true);
        const pac = this.packArgs(pick, args);
        if ((Array.isArray(pick) ? pick : [pick]).every(m => m.static)) return lit(this.retOf(pick), `${this.staticRef(p0.owner)}.${this.callName(pick, args)}(${pac})`);
        { const rr = lit(this.retOf(pick), `${rc}.${this.callName(pick, args)}(${pac})`); rr.callDesc = `${this.binName(p0.owner)}.${name}(${p0.params.map(q => this.T(q.type) || 'Object').join(', ')})`; return rr; }
      }
      // String
      if (t === 'String') {
        const sm = libL().$STRM;
        if (!(name in sm)) { if (OBJECT_METHODS[name] && !['notify', 'wait', 'notifyAll'].includes(name)) { /* ok */ } else { this.err(e.ln, `cannot find symbol: method ${name}(${args.map(argT).join(',')})`); return lit(null, 'null'); } }
        this.checkStringArgs(e, name, args);
        const freshS = ['concat', 'repeat', 'toUpperCase', 'toLowerCase', 'replace', 'replaceAll'].includes(name) && !(recv.isConst && name !== 'concat' && false) ? ++this.freshN : 0;
        return { c: `$inv(${rc}, ${esc(name)}${args.length ? ', ' + ac : ''})`, t: STR_RET[name] || OBJECT_METHODS[name] || null, fresh: freshS };
      }
      if (t && BOX[t] && !(name in { intValue: 1, doubleValue: 1, longValue: 1, floatValue: 1, shortValue: 1, byteValue: 1, compareTo: 1, equals: 1, hashCode: 1, toString: 1, charValue: 1, booleanValue: 1, isNaN: 1, isInfinite: 1, getClass: 1, compare: 1 })) {
        this.err(e.ln, `cannot find symbol: method ${name}(${args.map(argT).join(',')})`); return lit(null, 'null');
      }
      let rtype = null;
      if (t) {
        const tab = OBJ_RET[t];
        if (tab && tab[name]) rtype = tab[name];
        if (BOX[t]) rtype = { intValue: 'int', doubleValue: 'double', longValue: 'long', floatValue: 'float', compareTo: 'int', equals: 'boolean', hashCode: 'int', toString: 'String', charValue: 'char', booleanValue: 'boolean', isNaN: 'boolean' }[name] || null;
        if (OBJECT_METHODS[name] && !rtype) rtype = OBJECT_METHODS[name];
        // existence check for concrete library classes
        const chk = L[IFACE_CHECK_MAP[t] || t];
        if (chk && typeof chk === 'function' && !BOXED_OBJ.has(t) && !chk.$prim && chk.prototype && !(L[t].$isIface && !IFACE_CHECK_MAP[t])) {
          const proto_ = chk.prototype;
          if (!(name in proto_) && !OBJECT_METHODS[name] && !name.endsWith('$idx')) {
            const isFnIface = L[t] && L[t].$isIface;
            if (!isFnIface || IFACE_CHECK_MAP[t]) { this.err(e.ln, `cannot find symbol: method ${name}(${args.map(argT).join(',')})`); return lit(null, 'null'); }
          }
        }
        if (t === 'Object' && !OBJECT_METHODS[name]) { this.err(e.ln, `cannot find symbol: method ${name}(${args.map(argT).join(',')})`); return lit(null, 'null'); }
      }
      if (t && REAL[t] && !REAL[t].has(name) && !name.endsWith('$idx')) { this.err(e.ln, `cannot find symbol: method ${name}(${args.map(argT).join(',')})`); return lit(null, 'null'); }
      let gen = null;
      if (recv.targs && t) gen = this.genericRefine(t, name, args, recv.targs, e.ln);
      if (gen && gen.t !== undefined) { rtype = gen.t; }
      if (t && /^Double(Unary|Binary)Operator$|^Double(Predicate|Function|Supplier)$/.test(t)) { args.forEach(a => { if (a.t !== 'double') a.c = `$toDouble(${a.c})`; }); }
      if (t && /^Long(Unary|Binary)Operator$|^Long(Predicate|Function)$/.test(t)) { args.forEach(a => { if (a.t !== 'long') a.c = `$toLong(${a.c})`; }); }
      const ac2 = args.map(a => a.c).join(', ');
      let mname = name;
      if (name === 'remove' && args.length === 1 && args[0].t && ['int', 'short', 'byte', 'char'].includes(args[0].t) && !(t && this.findClass(t))) {
        return lit(null, `$inv(${rc}, 'remove$idx', ${args[0].t === 'char' ? args[0].c + '.c' : args[0].c})`);
      }
      const rtargs = gen && gen.targs !== undefined ? gen.targs : (recv.targs && MAPS.has(t) && name === 'keySet' ? [recv.targs[0]] : recv.targs && MAPS.has(t) && name === 'values' ? [recv.targs[1]] : null);
      if (recv.targs && MAPS.has(t) && name === 'keySet') rtype = 'Set'; else if (recv.targs && MAPS.has(t) && name === 'values') rtype = 'Collection';
      const fresh = rtype === 'String' && ((t === 'Scanner' && (name === 'next' || name === 'nextLine')) || (t === 'BufferedReader' && name === 'readLine') || ((t === 'StringBuilder' || t === 'StringBuffer') && name === 'toString')) ? ++this.freshN : 0;
      if (this.isDirectType(t) && !(t === 'Class')) return { c: `${rc}.${name}(${ac2})`, t: rtype, fresh, targs: rtargs };
      return { c: `$inv(${rc}, ${esc(name)}${args.length ? ', ' + ac2 : ''})`, t: rtype, fresh, targs: rtargs };
    },
    /* element-type awareness for generic collections: List<E>.get -> E, Map<K,V>.get -> V, add(E) argument checks */
    genericRefine(t, name, args, ta, line) {
      const chk = (a, want) => { if (want && a.t && a.t !== '$lambda') this.checkAssign(a.t, want.t, line); };
      if (LISTS.has(t) && ta[0]) {
        const E = ta[0];
        if (ELEM_ADD.has(name) && args.length === 1) chk(args[0], E);
        else if (name === 'add' && args.length === 2) chk(args[1], E);
        else if (name === 'set' && args.length === 2) { chk(args[1], E); return { t: E.t, targs: E.targs }; }
        if (ELEM_FIRST.has(name) && args.length === 0) return { t: E.t, targs: E.targs };
        if (name === 'get' && args.length === 1 && !/^(Set|HashSet|TreeSet|LinkedHashSet|Queue|Collection)$/.test(t)) return { t: E.t, targs: E.targs };
        if (name === 'remove' && args.length === 1 && args[0].t === 'int' && /^(List|ArrayList|LinkedList|Vector|Stack)$/.test(t)) return { t: E.t, targs: E.targs };
      }
      if (MAPS.has(t) && ta[0] && ta[1]) {
        const [K, V] = ta;
        if ((name === 'put' || name === 'putIfAbsent') && args.length === 2) { chk(args[0], K); chk(args[1], V); return { t: V.t, targs: V.targs }; }
        if ((name === 'get' || name === 'remove') && args.length === 1) return { t: V.t, targs: V.targs };
        if (name === 'getOrDefault' && args.length === 2) { chk(args[1], V); return { t: V.t, targs: V.targs }; }
        if (/^(firstKey|lastKey|floorKey|ceilingKey|higherKey|lowerKey)$/.test(name)) return { t: K.t, targs: K.targs };
      }
      return null;
    },
    checkStringArgs(e, name, args) {
      const chk = (i, want) => { const a = args[i]; if (a && a.t && a.t !== want && a.t !== 'null' && !(want === 'CharSequence' && a.t === 'String')) { if (want === 'int' && isIntLike(a.t)) return; this.err(e.ln, `incompatible types: ${a.t} cannot be converted to ${want}`); } };
      if (name === 'charAt' || name === 'substring') { args.forEach((a, i) => { if (a.t && !isIntLike(a.t) && a.t !== 'Integer') this.err(e.ln, a.t === 'String' || a.t === 'boolean' ? `incompatible types: ${a.t} cannot be converted to int` : `incompatible types: possible lossy conversion from ${a.t} to int`); }); }
      if (name === 'equals' && args.length !== 1) this.err(e.ln, `method equals in class String cannot be applied to given types;`);
      if (name === 'length' && args.length) this.err(e.ln, `method length in class String cannot be applied to given types;`);
    },

    /* ================= object creation ================= */
    newExpr(e) {
      const args = e.args.map(a => this.expr(a));
      const ac = this.argCode(args);
      const tn = e.type.n;
      this.checkType(e.type, e.ln);
      // ---- anonymous class ----
      if (e.body) return this.anonClass(e, args, ac);
      const ci = this.findClass(tn);
      if (ci) {
        if (ci.kind === 'enum') { this.err(e.ln, 'enum classes may not be instantiated'); return lit(ci.name, 'null'); }
        if (ci.abstract) { this.err(e.ln, `${ci.name} is abstract; cannot be instantiated`); return lit(ci.name, 'null'); }
        let cpick = null;
        if (ci.kind === 'record') {
          const comps = ci.decl.recordComps;
          const explicit = ci.ctors.filter(c => !c.decl.compact);
          const canon = { name: '<init>', decl: { tparams: [] }, params: comps.map(c => ({ type: c.type, name: c.name })), owner: ci, static: false, abstract: false, mods: new Set(), ln: ci.decl.ln, throws: [], sig: this.sigKey(comps.map(c => ({ type: c.type })), ci.tparams) };
          const all = explicit.some(c => c.params.length === comps.length) ? explicit : [...explicit, canon];
          this.pickMethod(all, args, e.ln, ci.name, ci, 'constructor');
        } else if (ci.ctors.length) {
          cpick = this.pickMethod(ci.ctors, args, e.ln, ci.name, ci, 'constructor');
          if (cpick && !Array.isArray(cpick)) this.checkAccess(cpick, ci, e.ln, ci.name, true);
        } else if (args.length) {
          this.err(e.ln, `constructor ${ci.name} in class ${ci.name} cannot be applied to given types; required: no arguments; found: ${args.map(argT).join(',')}; reason: actual and formal argument lists differ in length`);
        }
        if (ci.kind === 'record' && ci.ctors.length) { /* explicit constructors: overload set includes the canonical one */ }
        if (ci.inner) {
          let outer;
          if (e.outer) outer = this.expr(e.outer).c;
          else { outer = this.instRef(ci.outer); }
          if (!outer) { this.err(e.ln, 'non-static variable this cannot be referenced from a static context'); return lit(ci.name, 'null'); }
          return lit(ci.name, `$newInner(${outer}, ${ci.js}${args.length ? ', ' + ac : ''})`);
        }
        const initN = this.ctorName(ci, cpick, args);
        if (cpick && !Array.isArray(cpick)) { const pa = this.packArgs(cpick, args); return ci.local ? lit(ci.name, `new ${ci.js}().${initN}(${pa})`) : lit(ci.name, `new (${this.staticRef(ci)})().${initN}(${pa})`); }
        if (ci.local) return lit(ci.name, `new ${ci.js}().${initN}(${ac})`);
        return lit(ci.name, `new (${this.staticRef(ci)})().${initN}(${ac})`);
      }
      const lib = this.libName(tn);
      const L = libL().L;
      if (!lib) { this.err(e.ln, `cannot find symbol: class ${tn.split('.')[0]}`); return lit(null, 'null'); }
      const cls = L[lib];
      if (cls.$isIface || /^(AbstractMap|AbstractCollection|AbstractSet|Number|Enum|Collector)$/.test(lib)) {
        if (cls.$isIface) { this.err(e.ln, `${lib} is abstract; cannot be instantiated`); return lit(lib, 'null'); }
      }
      if (lib === 'String') return { c: `$L.String.$make(${ac})`, t: 'String', fresh: ++this.freshN };
      if (BOX[lib] && args.length === 1) return lit(lib, `$L[${esc(lib)}].valueOf(${ac})`);
      if (lib === 'Object') return lit('Object', 'new $JObject()');
      if (['Scanner'].includes(lib) && args.length && args[0].t && args[0].t !== 'InputStream' && args[0].t !== 'String') { /* Scanner(System.in) typical */ }
      if (L.Throwable === cls || cls.prototype instanceof L.Throwable) { /* ok */ }
      return lit(lib, `$new($L[${esc(lib)}]${args.length ? ', ' + ac : ''})`);
    },
    anonClass(e, args, ac) {
      const tn = e.type.n;
      const baseUser = this.findClass(tn);
      const L = libL().L;
      const n = ++this.anonN;
      const top = (() => { let c = this.cur; while (c.outer) c = c.outer; return c.name; })();
      const decl = { k: 'class', kind: 'class', name: `${top}$${n}`, mods: new Set(), tparams: [], ext: null, impl: [], members: e.body, enumConsts: [], ln: e.ln };
      const info = this.collect(decl, this.cur, { anon: true, js: `$Anon${n}`, members: e.body });
      info.outer = this.cur; info.staticCtx = this.ctx.isStatic; info.isStatic = false; info.inner = false;
      let bt;
      if (baseUser) {
        if (baseUser.kind === 'interface') { info.ifaces.push(baseUser); if (args.length) this.err(e.ln, 'anonymous class implements interface; cannot have arguments'); } else info.parent = baseUser;
        bt = baseUser.name;
        if (baseUser.kind === 'class' && baseUser.decl.mods.has('final')) this.err(e.ln, `cannot inherit from final ${baseUser.name}`);
      } else {
        const lib = this.libName(tn);
        if (!lib) { this.err(e.ln, `cannot find symbol: class ${tn}`); return lit(null, 'null'); }
        if (L[lib].$isIface) info.libIfaces.push(lib); else info.parentLib = lib;
        bt = lib;
      }
      this.scanSigs(info);
      info.nested.forEach(x => { this.resolveSupers(x); this.scanSigs(x); });
      // anonymous classes forward constructor arguments to a user superclass
      if (baseUser && baseUser.kind !== 'interface' && baseUser.ctors.length) this.pickMethod(baseUser.ctors, args, e.ln, baseUser.name, baseUser, 'constructor');
      const r = this.genClass(info, true, true);
      this.checkAbstractImpl(info); this.checkOverrides(info);
      let nestedCode = '';
      for (const nn of info.nested) { const rn = this.genClass(nn, false); nestedCode += rn.def + '\n' + rn.post + '\n'; }
      this.anonInfos = this.anonInfos || new Map(); this.anonInfos.set(info.name, info);
      return lit(info.name, `(() => { const $C = ${r.def}; ${r.post} ${nestedCode} return new $C().$init(${ac}); })()`);
    },
  });
})();
