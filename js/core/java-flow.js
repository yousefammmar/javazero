/* JavaZero compiler, stage 2d: flow analysis on the AST.
   Reports javac errors: missing return statement, unreachable statement, variable might not have been initialized,
   local variables referenced from a lambda / inner class must be final or effectively final, unreported exception. */
(() => {
  const { Gen, libL } = JavaGen;
  const proto = Gen.prototype;
  const ALL = null; // "vacuously everything assigned" (statement cannot complete normally)
  const UNI = { has: () => true, uni: true, [Symbol.iterator]: function* () {} }; // "vacuously everything assigned" for constant conditions, but the code stays reachable
  const inter = (a, b) => (a === ALL ? b : b === ALL ? a : a === UNI ? b : b === UNI ? a : new Set([...a].filter(x => b.has(x))));
  const add = (a, id) => (a === ALL ? ALL : a === UNI ? UNI : new Set(a).add(id));
  const isTrue = e => e && ((e.k === 'lit' && e.t === 'boolean' && e.v === true) || (e.k === 'paren' && isTrue(e.e)));
  const isFalse = e => e && ((e.k === 'lit' && e.t === 'boolean' && e.v === false) || (e.k === 'paren' && isFalse(e.e)));

  const patNames = (p, out = []) => { if (!p) return out; if (p.k === 'typepat') out.push(p.name); if (p.k === 'recpat') { p.subs.forEach(x => patNames(x, out)); if (p.name) out.push(p.name); } return out; };
  class Flow {
    constructor(g, opts) {
      this.g = g; this.scopes = []; this.targets = []; this.nextId = 1; this.depth = 0; this.captureKinds = [];
      this.vars = []; this.reported = new Set(); this.handlers = []; this.opts = opts || {};
      this.completes = true; this.lambdaDepth = 0; this.thrown = [];
    }
    err(line, msg) { this.g.err(line, msg); }
    push() { this.scopes.push(new Map()); }
    pop() { this.scopes.pop(); }
    declare(name, info) {
      const v = { id: this.nextId++, name, depth: this.depth, assigns: 0, ...info };
      this.scopes[this.scopes.length - 1].set(name, v); this.vars.push(v);
      return v;
    }
    lookup(name) { for (let i = this.scopes.length - 1; i >= 0; i--) if (this.scopes[i].has(name)) return this.scopes[i].get(name); return null; }

    /* ---------- reporting helpers ---------- */
    use(e, A) {
      const v = this.lookup(e.id);
      if (!v) return;
      if (v.tracked && A !== ALL && !A.has(v.id)) this.err(e.ln, `variable ${e.id} might not have been initialized`);
      if (v.depth < this.depth && !v.capLn) { v.capLn = e.ln; v.capKind = this.captureKinds[this.captureKinds.length - 1]; }
    }
    assignTo(name, A, ln, compound) {
      const v = this.lookup(name);
      if (!v) return A;
      if (compound && v.tracked && A !== ALL && !A.has(v.id)) this.err(ln, `variable ${name} might not have been initialized`);
      v.assigns++;
      if (v.depth < this.depth) { v.capAssign = ln; v.capKind = this.captureKinds[this.captureKinds.length - 1]; if (!v.capLn) v.capLn = ln; }
      return add(A, v.id);
    }
    finish() {
      for (const v of this.vars) {
        if (!v.capLn) continue;
        const reassigned = v.hasInit || v.param ? v.assigns > (v.hasInit ? 1 : 0) : v.assigns > 1 && v.loopAssign;
        if (v.capAssign || (reassigned && (v.hasInit || v.param))) {
          const kind = v.capKind === 'lambda' ? 'a lambda expression' : 'an inner class';
          this.err(v.capLn, `local variables referenced from ${kind} must be final or effectively final`);
        }
      }
    }

    /* ---------- expressions ---------- */
    exs(list, A) { for (const x of list) A = this.ex(x, A); return A; }
    cond(e, A) {
      if (!e) return [A, A];
      if (isTrue(e)) return [A, UNI];
      if (isFalse(e)) return [UNI, A];
      if (e.k === 'paren') return this.cond(e.e, A);
      if (e.k === 'unary' && e.op === '!') { const [t, f] = this.cond(e.e, A); return [f, t]; }
      if (e.k === 'bin' && e.op === '&&') { const [lt, lf] = this.cond(e.l, A); const [rt, rf] = this.cond(e.r, lt); return [rt, inter(lf, rf)]; }
      if (e.k === 'bin' && e.op === '||') { const [lt, lf] = this.cond(e.l, A); const [rt, rf] = this.cond(e.r, lf); return [inter(lt, rt), rf]; }
      const r = this.ex(e, A);
      return [r, r];
    }
    ex(e, A) {
      if (!e || typeof e !== 'object') return A;
      switch (e.k) {
        case 'lit': case 'this': case 'super': case 'qthis': case 'classlit': return A;
        case 'name': this.use(e, A); return A;
        case 'paren': return this.ex(e.e, A);
        case 'field': return this.ex(e.obj, A);
        case 'index': return this.ex(e.idx, this.ex(e.arr, A));
        case 'unary': return this.ex(e.e, A);
        case 'cast': return this.ex(e.e, A);
        case 'instanceof': { const r = this.ex(e.e, A); if (e.bind) this.declare(e.bind, { tracked: false, hasInit: true, assigns: 1 }); patNames(e.pat).forEach(n => this.declare(n, { tracked: false, hasInit: true, assigns: 1 })); return r; }
        case 'incdec': {
          if (e.e.k === 'name') { this.use(e.e, A); return this.assignTo(e.e.id, A, e.ln, true); }
          return this.ex(e.e, A);
        }
        case 'assign': {
          const t = e.target.k === 'paren' ? e.target.e : e.target;
          if (t.k === 'name') {
            let B = A;
            if (e.op !== '=') this.use(t, A);
            B = this.ex(e.v, A);
            const v = this.lookup(t.id);
            if (v && this.loopDepth > (v.loopDepth || 0)) v.loopAssign = true;
            return this.assignTo(t.id, B, e.ln, false);
          }
          let B = this.ex(t.k === 'field' ? t.obj : t.k === 'index' ? t.arr : t, A);
          if (t.k === 'index') B = this.ex(t.idx, B);
          return this.ex(e.v, B);
        }
        case 'bin': {
          if (e.op === '&&' || e.op === '||') { const [t, f] = this.cond(e, A); return inter(t, f); }
          return this.ex(e.r, this.ex(e.l, A));
        }
        case 'cond': {
          const [t, f] = this.cond(e.c, A);
          return inter(this.ex(e.a, t), this.ex(e.b, f));
        }
        case 'call': {
          let B = e.obj ? this.ex(e.obj, A) : A;
          B = this.exs(e.args, B);
          this.noteCall(e);
          return B;
        }
        case 'ctorcall': return this.exs(e.args, A);
        case 'new': {
          let B = e.outer ? this.ex(e.outer, A) : A;
          B = this.exs(e.args, B);
          this.noteNew(e);
          if (e.body) this.classBody(e.body, 'class', B);
          return B;
        }
        case 'newarr': { let B = this.exs(e.dims, A); if (e.init) B = this.ex(e.init, B); return B; }
        case 'arrlit': return this.exs(e.elems, A);
        case 'lambda': {
          this.push(); this.depth++; this.captureKinds.push('lambda'); this.lambdaDepth++;
          const savedTargets = this.targets, savedHandlers = this.handlers; this.targets = []; this.handlers = [{ lambda: true }];
          e.params.forEach(p => this.declare(p.name, { tracked: false, hasInit: true, param: true, assigns: 0 }));
          if (e.body.k === 'block') this.block(e.body.body, A); else this.ex(e.body, A);
          this.targets = savedTargets; this.handlers = savedHandlers;
          this.lambdaDepth--; this.captureKinds.pop(); this.depth--; this.pop();
          return A;
        }
        case 'mref': return e.obj ? this.ex(e.obj, A) : A;
        case 'switchexpr': return this.switchFlow(e, A, true);
      }
      return A;
    }
    classBody(members, kind, A) {
      this.depth++; this.captureKinds.push('inner');
      const savedTargets = this.targets, savedHandlers = this.handlers, savedL = this.lambdaDepth; this.targets = []; this.lambdaDepth = 0;
      for (const m of members) {
        if (m.k === 'method' && m.body) {
          this.handlers = [{ throws: (m.throws || []).map(t => t.n) }];
          this.push(); (m.params || []).forEach(p => this.declare(p.name, { tracked: false, hasInit: true, param: true, assigns: 0 }));
          this.block(m.body.body, A); this.pop();
        } else if (m.k === 'field') { for (const d of m.decls) if (d.init) this.ex(d.init, A); }
        else if (m.k === 'init') { this.push(); this.block(m.body.body, A); this.pop(); }
      }
      this.targets = savedTargets; this.handlers = savedHandlers; this.lambdaDepth = savedL;
      this.captureKinds.pop(); this.depth--;
    }

    /* ---------- statements ---------- */
    block(stmts, A) {
      this.push();
      let live = true;
      for (const s of stmts) {
        if (!live) { this.err(s.ln, 'unreachable statement'); break; }
        A = this.st(s, A);
        if (A === ALL) live = false;
      }
      this.pop();
      this.endLive = live;
      return A;
    }
    loopBody(body, A) { this.loopDepth = (this.loopDepth || 0) + 1; const r = this.st(body, A); this.loopDepth--; return r; }
    target(kind, label) { const t = { kind, label, breaks: [], hasBreak: false, hasContinue: false }; this.targets.push(t); return t; }
    st(s, A) {
      switch (s.k) {
        case 'block': return this.block(s.body, A);
        case 'empty': return A;
        case 'local': {
          for (const d of s.decls) {
            if (d.init) A = this.ex(d.init, A);
            const v = this.declare(d.name, { tracked: !d.init, hasInit: !!d.init, assigns: d.init ? 1 : 0, loopDepth: this.loopDepth || 0 });
            if (d.init) A = add(A, v.id);
          }
          return A;
        }
        case 'expr': return this.ex(s.e, A);
        case 'if': {
          const [t, f] = this.cond(s.c, A);
          const a = this.sub(s.a, t);
          const b = s.b ? this.sub(s.b, f) : f;
          return inter(a, b);
        }
        case 'while': {
          const [t, f] = this.cond(s.c, A);
          const tg = this.target('loop'); tg.node = s;
          this.loopBody(s.body, t);
          this.targets.pop();
          let res = isTrue(s.c) ? ALL : f;
          for (const b of tg.breaks) res = inter(res, b);
          return tg.hasBreak || !isTrue(s.c) ? (res === ALL && tg.hasBreak ? A : res) : ALL;
        }
        case 'do': {
          const tg = this.target('loop');
          const end = this.loopBody(s.body, A);
          this.targets.pop();
          const [t, f] = this.cond(s.c, end === ALL ? A : end);
          if (end === ALL && !tg.hasContinue) return tg.hasBreak ? this.breakRes(tg, ALL, A) : ALL;
          let res = isTrue(s.c) ? ALL : f;
          if (isTrue(s.c) && !tg.hasBreak) return ALL;
          return this.breakRes(tg, res, A);
        }
        case 'for': {
          this.push();
          for (const i of s.init) A = i.k === 'local' ? this.st(i, A) : this.ex(i.e, A);
          const [t, f] = s.c ? this.cond(s.c, A) : [A, ALL];
          const tg = this.target('loop');
          const end = this.loopBody(s.body, t);
          this.targets.pop();
          let B = end === ALL ? t : end; for (const u of s.update) B = this.ex(u, B);
          this.pop();
          const infinite = !s.c || isTrue(s.c);
          if (infinite && !tg.hasBreak) return ALL;
          return this.breakRes(tg, infinite ? ALL : f, A);
        }
        case 'foreach': {
          A = this.ex(s.iter, A);
          this.push();
          const v = this.declare(s.name, { tracked: false, hasInit: true, assigns: 1, loopDepth: (this.loopDepth || 0) + 1 });
          const tg = this.target('loop');
          this.loopBody(s.body, add(A, v.id));
          this.targets.pop(); this.pop();
          return this.breakRes(tg, A, A);
        }
        case 'labeled': {
          const tg = this.target('label', s.label);
          const r = this.st(s.body, A);
          this.targets.pop();
          return this.breakRes(tg, r, A);
        }
        case 'switch': return this.switchFlow(s, A, false);
        case 'return': { if (s.e) A = this.ex(s.e, A); this.noteReturn(s); return ALL; }
        case 'yield': return (this.ex(s.e, A), ALL);
        case 'throw': { A = this.ex(s.e, A); this.noteThrow(s); return ALL; }
        case 'break': {
          const tg = this.findTarget(s.label, true);
          if (tg) { tg.hasBreak = true; tg.breaks.push(A === ALL ? ALL : A); }
          return ALL;
        }
        case 'continue': { const tg = this.findTarget(s.label, false); if (tg) tg.hasContinue = true; return ALL; }
        case 'try': return this.tryFlow(s, A);
        case 'localclass': { this.push(); this.declare(s.decl.name, { tracked: false, hasInit: true, cls: true }); this.pop(); this.classBody(s.decl.members, 'class', A); return A; }
        case 'exprval': return this.ex(s.e, A);
      }
      return A;
    }
    sub(s, A) { this.push(); const r = this.st(s, A); this.pop(); return r; }
    breakRes(tg, res, A) { let r = res; for (const b of tg.breaks) r = inter(r, b); return r === ALL ? (tg.hasBreak ? A : ALL) : r; }
    findTarget(label, isBreak) {
      for (let i = this.targets.length - 1; i >= 0; i--) {
        const t = this.targets[i];
        if (label) { if (t.label === label || (t.kind !== 'label' && t.labelOf === label)) return t; continue; }
        if (t.kind === 'loop' || (isBreak && t.kind === 'switch')) return t;
      }
      return null;
    }
    switchFlow(s, A, isExpr) {
      A = this.ex(s.sel, A);
      const tg = this.target('switch');
      let hasDefault = s.cases.some(c => c.labels === null || c.isDefault);
      const enumLike = isExpr; // switch expressions must be exhaustive (checked by codegen)
      let res = ALL, last = A, anyComplete = false;
      const arrow = s.cases.some(c => c.arrow);
      for (const c of s.cases) {
        this.push();
        let start = A;
        (c.labels || []).forEach(l => patNames(l).forEach(n => this.declare(n, { tracked: false, hasInit: true, assigns: 1 })));
        for (const l of c.labels || []) if (l.k !== 'typepat' && l.k !== 'recpat' && l.k !== 'name' && l.k !== 'lit') start = this.ex(l, start);
        if (c.guard) start = this.ex(c.guard, start);
        let end = start;
        if (c.arrow) {
          const b = c.body[0];
          if (b.k === 'exprval') end = this.ex(b.e, start); else end = this.st(b, start);
          if (isExpr && b.k === 'exprval') end = ALL; // value yielded
          if (end !== ALL) { anyComplete = true; res = inter(res, end); }
        } else {
          let live = true, B = start;
          for (const x of c.body) { if (!live) { this.err(x.ln, 'unreachable statement'); break; } B = this.st(x, B); if (B === ALL) live = false; }
          last = live ? B : ALL;
        }
        this.pop();
      }
      this.targets.pop();
      let out;
      if (arrow) out = hasDefault || isExpr ? res : inter(res, A);
      else { out = hasDefault ? last : inter(last, A); }
      for (const b of tg.breaks) out = inter(out, b);
      if (arrow && !anyComplete && !tg.hasBreak && hasDefault && !isExpr) return ALL;
      if (!arrow && out === ALL && tg.hasBreak) out = A;
      if (isExpr) return A;
      return out;
    }
    tryFlow(s, A) {
      // exceptions thrown inside the body are checked against the catch clauses
      const catchTypes = s.catches.flatMap(c => c.types.map(t => t.n));
      this.handlers.push({ catches: catchTypes, thrownInBody: [] });
      const hnd = this.handlers[this.handlers.length - 1];
      this.push();
      for (const r of s.res) { if (r.type) { A = this.ex(r.init, A); const v = this.declare(r.name, { tracked: false, hasInit: true, assigns: 1 }); A = add(A, v.id); } else A = this.ex(r.expr, A); }
      const tEnd = this.st(s.block, A);
      this.pop();
      this.handlers.pop();
      // library checked exceptions: only reported when the body cannot call anything that might throw them
      const LIBCHK = new Set(['IOException', 'FileNotFoundException', 'InterruptedException', 'TimeoutException', 'CloneNotSupportedException']);
      const safe = n => {
        if (!n || typeof n !== 'object') return true;
        if (Array.isArray(n)) return n.every(safe);
        if (n.k === 'call') { const o = n.obj; const sys = o && o.k === 'field' && o.obj && o.obj.k === 'name' && o.obj.id === 'System' && (o.name === 'out' || o.name === 'err'); const math = o && o.k === 'name' && o.id === 'Math'; if (!(sys || math)) return false; }
        if (n.k === 'new' || n.k === 'throw' || n.k === 'lambda' || n.k === 'mref' || n.k === 'ctorcall' || n.k === 'localclass' || n.k === 'try' || n.k === 'switchexpr') return false;
        for (const key of Object.keys(n)) { if (key === 'ln' || key === 'mods' || key === 'type') continue; const v = n[key]; if (v && typeof v === 'object' && !safe(v)) return false; }
        return true;
      };
      const bodySafe = safe(s.block) && s.res.length === 0;
      for (const c of s.catches) for (const t of c.types) if (bodySafe && LIBCHK.has(t.n) && !this.g.findClass(t.n)) this.err(c.ln, `exception ${t.n} is never thrown in body of corresponding try statement`);
      // user-defined checked exceptions that can never be thrown in the body
      for (const c of s.catches) for (const t of c.types) {
        const ci = this.g.findClass(t.n);
        if (ci && this.g.isChecked(t.n) && !hnd.thrownInBody.some(x => this.g.exSub(x, t.n) || this.g.exSub(t.n, x))) this.err(c.ln, `exception ${t.n} is never thrown in body of corresponding try statement`);
      }
      let res = tEnd;
      for (const c of s.catches) {
        this.push();
        const v = this.declare(c.name, { tracked: false, hasInit: true, assigns: 1 });
        const cEnd = this.st(c.body, add(A, v.id));
        this.pop();
        res = inter(res, cEnd);
      }
      if (s.fin) {
        const fEnd = this.st(s.fin, A);
        if (fEnd === ALL) return ALL;
        if (res !== ALL && res !== UNI && fEnd !== ALL && fEnd !== UNI) for (const x of fEnd) res = add(res, x);
      }
      return res;
    }

    /* ---------- checked exceptions ---------- */
    handled(type) {
      for (let i = this.handlers.length - 1; i >= 0; i--) {
        const h = this.handlers[i];
        if (h.lambda) return true; // lambda bodies are not analysed for checked exceptions
        if (h.catches) { h.thrownInBody.push(type); if (h.catches.some(c => this.g.exSub(type, c))) return true; }
        if (h.throws) return h.throws.some(c => this.g.exSub(type, c));
      }
      return true;
    }
    reportUnhandled(type, ln) { if (!this.handled(type)) this.err(ln, `unreported exception ${type}; must be caught or declared to be thrown`); }
    noteThrow(s) {
      const e = s.e;
      if (e.k === 'new' && !e.body && this.g.isChecked(e.type.n)) this.reportUnhandled(e.type.n, s.ln);
      else if (e.k === 'new') { /* unchecked or anonymous */ this.handled(e.type.n); }
    }
    noteCall(e) {
      const g = this.g;
      if (e.obj && e.obj.k === 'name' && e.obj.id === 'Thread' && e.name === 'sleep' && !this.lookup('Thread')) { this.reportUnhandled('InterruptedException', e.ln); return; }
      let cands = [];
      if (!e.obj) { for (let c = g.cur; c; c = c.outer) { const ms = g.findMethods(c, e.name); if (ms.length) { cands = ms; break; } } }
      else if (e.obj.k === 'this' || e.obj.k === 'super') cands = g.findMethods(g.cur, e.name);
      else { const uc = g.exprClass(e.obj); if (uc) cands = g.findMethods(uc, e.name); else {
        // receiver is an expression: use the declared class of a simple local / field variable when we can find it
        for (const ci of g.all) for (const m of (ci.methods.get(e.name) || [])) if (m.throws.length) cands.push(m);
        const arityOk = cands.filter(m => m.params.length === e.args.length);
        // only trust this when every method of that name/arity declares the exception
        const all = [];
        for (const ci of g.all) for (const m of (ci.methods.get(e.name) || [])) if (m.params.length === e.args.length) all.push(m);
        cands = all.length && all.every(m => m.throws.length) ? all : [];
      } }
      cands = cands.filter(m => m.params.length === e.args.length || m.params.some(p => p.varargs));
      if (!cands.length || cands.some(m => !m.throws.length)) return;
      for (const t of cands[0].throws) if (g.isChecked(t.n) && cands.every(m => m.throws.some(x => x.n === t.n))) this.reportUnhandled(t.n, e.ln);
    }
    noteNew(e) {
      const g = this.g;
      const ci = g.findClass(e.type.n);
      if (!ci) return;
      const cs = ci.ctors.filter(c => c.params.length === e.args.length);
      if (!cs.length || cs.some(c => !c.throws.length)) return;
      for (const t of cs[0].throws) if (g.isChecked(t.n)) this.reportUnhandled(t.n, e.ln);
    }
    noteReturn() {}
  }

  Object.assign(proto, {
    /* analyze a method / constructor body: returns whether the body can complete normally */
    analyzeBody(body, params, throwsList, isCtorOrVoid, line) {
      const f = new Flow(this, {});
      f.handlers = [{ throws: (throwsList || []).map(t => t.n) }];
      f.push();
      (params || []).forEach(p => f.declare(p.name, { tracked: false, hasInit: true, param: true, assigns: 0 }));
      const end = f.block(body.body, new Set());
      f.pop();
      f.finish();
      return end !== ALL;
    },
  });
  Gen.Flow = Flow;
})();
