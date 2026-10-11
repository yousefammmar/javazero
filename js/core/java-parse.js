/* JavaZero compiler, stage 1: lexer + recursive-descent parser for a large subset of Java 17.
   parse(src) returns an AST or throws {line, msg} shaped like a javac error. */
const JavaParser = (() => {
  const KEYWORDS = new Set(('abstract assert boolean break byte case catch char class const continue default do double else enum extends final finally float for goto if implements import instanceof int interface long native new package private protected public return short static strictfp super switch synchronized this throw throws transient try void volatile while true false null').split(' '));
  const PRIMS = new Set(['boolean', 'byte', 'short', 'int', 'long', 'float', 'double', 'char']);
  const MODS = new Set(['public', 'private', 'protected', 'static', 'final', 'abstract', 'synchronized', 'native', 'transient', 'volatile', 'strictfp', 'default', 'sealed', 'non-sealed']);
  const OPS = ['>>>=', '<<=', '...', '->', '::', '++', '--', '&&', '||', '==', '!=', '<=', '>=', '+=', '-=', '*=', '/=', '%=', '&=', '|=', '^=', '<<', '(', ')', '{', '}', '[', ']', ';', ',', '.', '@', '=', '>', '<', '!', '~', '?', ':', '+', '-', '*', '/', '&', '|', '^', '%'];
  const err = (line, msg) => { throw { line, msg }; };

  /* ---------- lexer ---------- */
  function lex(src) {
    const toks = [];
    let i = 0, ln = 1;
    const n = src.length;
    const esc = (q) => {
      // at src[i] === '\\'
      const c = src[i + 1];
      i += 2;
      switch (c) {
        case 'n': return '\n'; case 't': return '\t'; case 'r': return '\r'; case 'b': return '\b'; case 'f': return '\f'; case 's': return ' ';
        case '0': case '1': case '2': case '3': case '4': case '5': case '6': case '7': {
          let o = c; while (o.length < 3 && /[0-7]/.test(src[i] || '') && parseInt(o + src[i], 8) < 256) o += src[i++];
          return String.fromCharCode(parseInt(o, 8));
        }
        case 'u': { while (src[i] === 'u') i++; const h = src.slice(i, i + 4); if (!/^[0-9a-fA-F]{4}$/.test(h)) err(ln, 'illegal unicode escape'); i += 4; return String.fromCharCode(parseInt(h, 16)); }
        case '\\': case '\'': case '"': return c;
        default: err(ln, 'illegal escape character');
      }
    };
    while (i < n) {
      const c = src[i];
      if (c === '\n') { ln++; i++; continue; }
      if (c === ' ' || c === '\t' || c === '\r' || c === '\f') { i++; continue; }
      if (c === '/' && src[i + 1] === '/') { while (i < n && src[i] !== '\n') i++; continue; }
      if (c === '/' && src[i + 1] === '*') {
        const start = ln; i += 2;
        while (i < n && !(src[i] === '*' && src[i + 1] === '/')) { if (src[i] === '\n') ln++; i++; }
        if (i >= n) err(start, 'unclosed comment');
        i += 2; continue;
      }
      const sp = i > 0 && /\s/.test(src[i - 1]);
      if (/[A-Za-z_$]/.test(c) || c > '\x7f' && /\p{L}/u.test(c)) {
        let j = i + 1; while (j < n && (/[\w$]/.test(src[j]) || src[j] > '\x7f' && /\p{L}/u.test(src[j]))) j++;
        const w = src.slice(i, j); i = j;
        toks.push({ t: KEYWORDS.has(w) ? 'kw' : 'id', v: w, ln, sp });
        continue;
      }
      if (/[0-9]/.test(c) || (c === '.' && /[0-9]/.test(src[i + 1] || ''))) {
        let j = i, kind = 'int', text;
        if (c === '0' && /[xX]/.test(src[i + 1] || '')) {
          j = i + 2; while (/[0-9a-fA-F_]/.test(src[j] || '')) j++;
          let hf = /^[.pP]/.test(src[j] || '') && (src[j] !== '.' || /[0-9a-fA-F.pP]/.test(src[j + 1] || ''));
          if (hf) { // hexadecimal floating point literal 0x1.8p3
            const mant = src.slice(i + 2, j).replace(/_/g, ''); let frac = '';
            if (src[j] === '.') { j++; const k = j; while (/[0-9a-fA-F_]/.test(src[j] || '')) j++; frac = src.slice(k, j).replace(/_/g, ''); }
            if (!/[pP]/.test(src[j] || '')) err(ln, 'malformed floating-point literal');
            j++; let sg = 1; if (src[j] === '+' || src[j] === '-') { if (src[j] === '-') sg = -1; j++; }
            const k2 = j; while (/[0-9]/.test(src[j] || '')) j++;
            const ex = sg * parseInt(src.slice(k2, j), 10);
            const val = (parseInt(mant || '0', 16) + (frac ? parseInt(frac, 16) / Math.pow(16, frac.length) : 0)) * Math.pow(2, ex);
            let kd = 'double'; if (/[fF]/.test(src[j] || '')) { kd = 'float'; j++; } else if (/[dD]/.test(src[j] || '')) j++;
            toks.push({ t: 'num', kind: kd, v: val, ln, sp, raw: src.slice(i, j) }); i = j; continue;
          }
          text = src.slice(i, j).replace(/_/g, '');
        }
        else if (c === '0' && /[bB]/.test(src[i + 1] || '')) { j = i + 2; while (/[01_]/.test(src[j] || '')) j++; text = src.slice(i, j).replace(/_/g, ''); }
        else {
          while (/[0-9_]/.test(src[j] || '')) j++;
          if (src[j] === '.' && /[0-9]/.test(src[j + 1] || '')) { kind = 'double'; j++; while (/[0-9_]/.test(src[j] || '')) j++; }
          else if (src[j] === '.' && !/[A-Za-z_.]/.test(src[j + 1] || '')) { kind = 'double'; j++; }
          if (/[eE]/.test(src[j] || '') && /[0-9+-]/.test(src[j + 1] || '')) { kind = 'double'; j += 2; while (/[0-9]/.test(src[j] || '')) j++; }
          text = src.slice(i, j).replace(/_/g, '');
        }
        const suf = src[j] || '';
        let val;
        if (/[lL]/.test(suf)) { kind = 'long'; j++; }
        else if (/[fF]/.test(suf) && !/^0[xX]/.test(text)) { kind = 'float'; j++; }
        else if (/[dD]/.test(suf) && !/^0[xX]/.test(text)) { kind = 'double'; j++; }
        if (kind === 'int' || kind === 'long') {
          let b;
          if (/^0[xX]/.test(text)) b = BigInt(text);
          else if (/^0[bB]/.test(text)) b = BigInt(text);
          else if (/^0[0-7]+$/.test(text)) b = BigInt('0o' + text.slice(1));
          else b = BigInt(text);
          val = b;
        } else val = parseFloat(text);
        toks.push({ t: 'num', kind, v: val, ln, sp, raw: text });
        i = j; continue;
      }
      if (c === '"') {
        if (src.startsWith('"""', i)) {
          const start = ln; i += 3;
          while (i < n && src[i] !== '\n') i++;
          i++; ln++;
          let raw = '';
          while (i < n && !src.startsWith('"""', i)) { if (src[i] === '\\') { raw += '\\' + src[i + 1]; i += 2; continue; } if (src[i] === '\n') ln++; raw += src[i++]; }
          if (i >= n) err(start, 'unclosed text block');
          i += 3;
          const lines = raw.split('\n'), last = lines[lines.length - 1];
          const ind = Math.min(...lines.filter((l, k) => l.trim() || k === lines.length - 1).map(l => l.match(/^\s*/)[0].length));
          let s = lines.map(l => l.slice(Math.min(ind, l.length)).replace(/\s+$/, '')).join('\n');
          if (!/^\s*$/.test(last)) { /* closing delimiter on same line as text: keep as is */ }
          s = s.replace(/\\\n/g, '').replace(/\\([0-3][0-7]{0,2}|[4-7][0-7]?|u+[0-9a-fA-F]{4}|.)/g, (m, ch) => /^[0-7]/.test(ch) ? String.fromCharCode(parseInt(ch, 8)) : /^u/.test(ch) ? String.fromCharCode(parseInt(ch.replace(/^u+/, ''), 16)) : ({ n: '\n', t: '\t', r: '\r', b: '\b', f: '\f', '"': '"', "'": "'", '\\': '\\', s: ' ' }[ch] ?? ch));
          toks.push({ t: 'str', v: s, ln: start, sp });
          continue;
        }
        let s = ''; i++;
        for (;;) {
          if (i >= n || src[i] === '\n') err(ln, 'unclosed string literal');
          if (src[i] === '"') { i++; break; }
          if (src[i] === '\\') s += esc('"'); else s += src[i++];
        }
        toks.push({ t: 'str', v: s, ln, sp });
        continue;
      }
      if (c === "'") {
        i++; let s;
        if (src[i] === "'") err(ln, 'empty character literal');
        if (src[i] === '\\') s = esc("'"); else s = src[i++];
        if (src[i] !== "'") err(ln, 'unclosed character literal');
        i++;
        toks.push({ t: 'chr', v: s.charCodeAt(0), ln, sp });
        continue;
      }
      let op = null;
      for (const o of OPS) if (src.startsWith(o, i)) { op = o; break; }
      if (!op) err(ln, `illegal character: '${c}'`);
      i += op.length;
      toks.push({ t: 'op', v: op, ln, sp });
    }
    toks.push({ t: 'eof', v: '<EOF>', ln, sp: true });
    return toks;
  }

  /* ---------- parser ---------- */
  function parse(src) {
    const T = lex(src);
    let p = 0;
    const peek = (k = 0) => T[Math.min(p + k, T.length - 1)];
    const prevLn = () => (p > 0 ? T[p - 1].ln : 1);
    const is = (v, k = 0) => { const t = peek(k); return (t.t === 'op' || t.t === 'kw') && t.v === v; };
    const isId = (k = 0) => peek(k).t === 'id';
    const isIdv = (v, k = 0) => peek(k).t === 'id' && peek(k).v === v;
    const eat = v => { if (is(v)) { p++; return true; } return false; };
    const fail = (msg, ln) => err(ln ?? peek().ln, msg);
    const eofCheck = () => { if (peek().t === 'eof') fail('reached end of file while parsing', peek().ln); };
    const expect = v => {
      if (is(v)) return T[p++];
      if (peek().t === 'eof') fail('reached end of file while parsing', prevLn());
      if (v === ';' || v === ')' || v === ']') fail(`'${v}' expected`, prevLn());
      fail(`'${v}' expected`, peek().ln);
    };
    const ident = () => {
      const t = peek();
      if (t.t === 'id') { p++; return t.v; }
      if (t.t === 'eof') fail('reached end of file while parsing', prevLn());
      fail('<identifier> expected', t.t === 'kw' || t.t === 'op' ? (is(';') || is(')') || is('=') ? prevLn() : t.ln) : t.ln);
    };

    /* types */
    const parseTypeArgs = () => {
      // at '<'
      expect('<');
      const args = [];
      if (is('>')) { p++; return args; } // diamond
      for (;;) {
        if (is('?')) {
          p++;
          if (is('extends') || is('super')) { p++; parseType(); }
          args.push({ n: '?', args: [], dims: 0 });
        } else args.push(parseType());
        if (!eat(',')) break;
      }
      if (!is('>')) fail("'>' expected");
      p++;
      return args;
    };
    const parseType = (allowVoid) => {
      const t = peek();
      let n, args = [];
      if (t.t === 'kw' && (PRIMS.has(t.v) || (allowVoid && t.v === 'void'))) { p++; n = t.v; }
      else if (t.t === 'id') {
        p++; n = t.v;
        if (is('<')) args = parseTypeArgs();
        while (is('.') && peek(1).t === 'id') { p++; n += '.' + peek().v; p++; if (is('<')) args = parseTypeArgs(); }
      } else if (t.t === 'eof') fail('reached end of file while parsing', prevLn());
      else fail(t.t === 'kw' || t.t === 'op' ? (t.v === 'void' ? "'void' type not allowed here" : '<identifier> expected') : 'illegal start of type', t.ln);
      let dims = 0;
      while (is('[') && is(']', 1)) { p += 2; dims++; }
      return { n, args, dims };
    };
    const tryParse = fn => { const save = p; try { return fn(); } catch (e) { if (e && e.msg !== undefined) { p = save; return null; } throw e; } };

    /* modifiers & annotations */
    const skipAnno = (collect) => {
      while (is('@') && !is('interface', 1)) {
        p++; const an = ident(); if (collect) collect.add('@' + an); while (eat('.')) ident();
        if (is('(')) { let d = 0; do { if (is('(')) d++; if (is(')')) d--; p++; eofCheck(); } while (d > 0); }
      }
    };
    const parseMods = () => {
      const m = new Set();
      for (;;) {
        skipAnno(m);
        const t = peek();
        if ((t.t === 'kw' || t.t === 'id') && MODS.has(t.v)) {
          if (t.v === 'default' && (is(':', 1) || is('->', 1))) break;
          if (t.v === 'sealed' || t.v === 'non-sealed') { if (!(peek(1).t === 'kw' || peek(1).t === 'id')) break; }
          if (t.t === 'id' && t.v !== 'sealed') break;
          m.add(t.v); p++;
        } else if (t.t === 'id' && t.v === 'non' && is('-', 1) && isIdv('sealed', 2)) { p += 3; }
        else break;
      }
      return m;
    };

    /* expressions */
    const isLambdaAhead = () => {
      if (isId() && is('->', 1)) return true;
      if (!is('(')) return false;
      let d = 0, k = 0;
      for (;;) {
        const t = peek(k);
        if (t.t === 'eof') return false;
        if (t.t === 'op' && t.v === '(') d++;
        if (t.t === 'op' && t.v === ')') { d--; if (d === 0) break; }
        k++;
      }
      return is('->', k + 1);
    };
    const parseLambda = () => {
      const ln = peek().ln, params = [];
      if (isId()) params.push({ name: ident(), type: null });
      else {
        expect('(');
        if (!is(')')) for (;;) {
          eat('final');
          if (isId() && (is(',', 1) || is(')', 1))) params.push({ name: ident(), type: null });
          else { const ty = parseType(); params.push({ name: ident(), type: ty }); }
          if (!eat(',')) break;
        }
        expect(')');
      }
      expect('->');
      let body;
      if (is('{')) body = parseBlock(); else body = parseExpr();
      return { k: 'lambda', params, body, ln };
    };
    const ASSIGN = new Set(['=', '+=', '-=', '*=', '/=', '%=', '&=', '|=', '^=', '<<=', '>>>=']);
    const gtAssign = () => {
      // handles >>= and >>>= which the lexer emits as '>' '>=' / '>' '>' '>='
      if (is('>') && !peek(1).sp && is('>=', 1)) return '>>=';
      if (is('>') && !peek(1).sp && is('>', 1) && !peek(2).sp && is('>=', 2)) return '>>>=';
      return null;
    };
    function parseExpr(noLambda) {
      if (!noLambda && isLambdaAhead()) return parseLambda();
      const ln = peek().ln;
      const lhs = parseTernary();
      const g = gtAssign();
      let op = g || (peek().t === 'op' && ASSIGN.has(peek().v) ? peek().v : null);
      if (op) {
        if (!['name', 'field', 'index'].includes(lhs.k)) fail('unexpected type\n  required: variable\n  found:    value', ln);
        p += g ? (g === '>>=' ? 2 : 3) : 1;
        const v = parseExpr(noLambda);
        return { k: 'assign', op, target: lhs, v, ln };
      }
      return lhs;
    }
    function parseTernary() {
      const c = parseBinary(1);
      if (is('?')) {
        const ln = peek().ln; p++;
        const a = parseExpr();
        expect(':');
        const b = isLambdaAhead() ? parseLambda() : parseTernary();
        return { k: 'cond', c, a, b, ln };
      }
      return c;
    }
    const PREC = { '||': 1, '&&': 2, '|': 3, '^': 4, '&': 5, '==': 6, '!=': 6, '<': 7, '>': 7, '<=': 7, '>=': 7, 'instanceof': 7, '<<': 8, '>>': 8, '>>>': 8, '+': 9, '-': 9, '*': 10, '/': 10, '%': 10 };
    const binOp = () => {
      const t = peek();
      if (t.t === 'kw' && t.v === 'instanceof') return ['instanceof', 1];
      if (t.t !== 'op') return null;
      if (t.v === '>') {
        if (!peek(1).sp && is('>', 1)) {
          if (!peek(2).sp && is('>', 2)) { if (!peek(3).sp && is('>=', 3)) return null; return ['>>>', 3]; }
          if (!peek(2).sp && is('>=', 2)) return null; // >>>=
          return ['>>', 2];
        }
        if (!peek(1).sp && is('>=', 1)) return null; // >>=
        return ['>', 1];
      }
      if (PREC[t.v]) return [t.v, 1];
      return null;
    };
    function parseBinary(minPrec) {
      let l = parseUnary();
      for (;;) {
        const bo = binOp();
        if (!bo) break;
        const [op, w] = bo, pr = PREC[op];
        if (pr < minPrec) break;
        const ln = peek().ln;
        p += w;
        if (op === 'instanceof') {
          eat('final');
          const type = parseType();
          let bind = null, pat = null;
          if (is('(')) pat = parseRecPatRest(type);
          else if (isId() && !(is('->', 1))) bind = ident();
          l = { k: 'instanceof', e: l, type, bind, pat, ln };
          continue;
        }
        const r = parseBinary(pr + 1);
        l = { k: 'bin', op, l, r, ln };
      }
      return l;
    }
    const startsOperand = t => t.t === 'id' || t.t === 'str' || t.t === 'chr' || t.t === 'num' || (t.t === 'kw' && ['this', 'super', 'new', 'true', 'false', 'null', 'switch'].includes(t.v) || (t.t === 'kw' && PRIMS.has(t.v))) || (t.t === 'op' && ['(', '!', '~'].includes(t.v));
    function parseUnary() {
      const t = peek(), ln = t.ln;
      if (t.t === 'op') {
        if (t.v === '+' || t.v === '-') {
          p++;
          if (t.v === '-' && peek().t === 'num' && (peek().kind === 'int' || peek().kind === 'long') && !is('.', 1)) {
            const nt = peek(); p++;
            return parsePostfix({ k: 'lit', t: nt.kind, v: -nt.v, ln, neg: true });
          }
          return { k: 'unary', op: t.v, e: parseUnary(), ln };
        }
        if (t.v === '!' || t.v === '~') { p++; return { k: 'unary', op: t.v, e: parseUnary(), ln }; }
        if (t.v === '++' || t.v === '--') { p++; const e = parseUnary(); return { k: 'incdec', op: t.v, prefix: true, e, ln }; }
        if (t.v === '(') {
          // cast?
          const save = p;
          p++;
          if (peek().t === 'kw' && PRIMS.has(peek().v)) {
            const ty = tryParse(() => { const ty = parseType(); expect(')'); return ty; });
            if (ty) return { k: 'cast', type: ty, e: parseUnary(), ln };
            p = save;
          } else if (peek().t === 'id') {
            const ty = tryParse(() => { const ty = parseType(); while (eat('&')) parseType(); expect(')'); return ty; });
            if (ty && startsOperand(peek()) && !(peek().t === 'op' && peek().v === '(' && false)) {
              const e = isLambdaAhead() ? parseLambda() : parseUnary();
              return { k: 'cast', type: ty, e, ln };
            }
            p = save;
          } else p = save;
        }
      }
      return parsePostfix(parsePrimary());
    }
    function parseArgs() {
      expect('(');
      const a = [];
      if (!is(')')) for (;;) { a.push(parseExpr()); if (!eat(',')) break; }
      if (!is(')')) { if (peek().t === 'eof') fail('reached end of file while parsing', prevLn()); fail("')' or ',' expected", prevLn()); }
      p++;
      return a;
    }
    function parseArrayInit() {
      const ln = peek().ln;
      expect('{');
      const elems = [];
      while (!is('}')) {
        elems.push(is('{') ? parseArrayInit() : parseExpr());
        if (!eat(',')) break;
      }
      expect('}');
      return { k: 'arrlit', elems, ln };
    }
    function parseNew(outer) {
      const ln = peek().ln;
      expect('new');
      const t = peek();
      let n, args = [];
      if (t.t === 'kw' && PRIMS.has(t.v)) { p++; n = t.v; }
      else {
        n = ident();
        if (is('<')) args = parseTypeArgs();
        while (is('.') && peek(1).t === 'id') { p++; n += '.' + ident(); if (is('<')) args = parseTypeArgs(); }
      }
      if (is('[')) {
        const dims = []; let extra = 0;
        while (is('[')) {
          p++;
          if (is(']')) { p++; extra++; } else { if (extra) fail("']' expected"); dims.push(parseExpr()); expect(']'); }
        }
        let init = null;
        if (is('{')) { if (dims.length) fail('array creation with both dimension expression and initialization is illegal'); init = parseArrayInit(); }
        else if (!dims.length) fail('array dimension missing');
        return { k: 'newarr', elem: { n, args, dims: 0 }, dims, extra, init, ln };
      }
      if (!is('(')) { if (PRIMS.has(n)) fail("'[' expected"); fail("'(' or '[' expected"); }
      const cargs = parseArgs();
      let body = null;
      if (is('{')) body = parseClassBody({ name: '', kind: 'class' }, ln);
      return { k: 'new', type: { n, args, dims: 0 }, args: cargs, body, outer: outer || null, ln };
    }
    function parsePrimary() {
      const t = peek(), ln = t.ln;
      switch (t.t) {
        case 'num': p++; return { k: 'lit', t: t.kind, v: t.v, ln, raw: t.raw };
        case 'str': p++; return { k: 'lit', t: 'String', v: t.v, ln };
        case 'chr': p++; return { k: 'lit', t: 'char', v: t.v, ln };
        case 'id': {
          p++;
          if (is('(')) return { k: 'call', obj: null, name: t.v, args: parseArgs(), ln };
          return { k: 'name', id: t.v, ln };
        }
        case 'kw':
          switch (t.v) {
            case 'true': case 'false': p++; return { k: 'lit', t: 'boolean', v: t.v === 'true', ln };
            case 'null': p++; return { k: 'lit', t: 'null', v: null, ln };
            case 'this':
              p++;
              if (is('(')) return { k: 'ctorcall', which: 'this', args: parseArgs(), ln };
              return { k: 'this', ln };
            case 'super':
              p++;
              if (is('(')) return { k: 'ctorcall', which: 'super', args: parseArgs(), ln };
              return { k: 'super', ln };
            case 'new': return parseNew();
            case 'switch': return parseSwitch(true);
            default:
              if (PRIMS.has(t.v) || t.v === 'void') {
                // int.class / int[]::new
                const ty = parseType(true);
                if (is('::')) { p++; expect('new'); return { k: 'mref', type: ty, name: 'new', ln }; }
                if (is('.') && isIdv('class', 1) || is('.') && is('class', 1)) { p += 2; return { k: 'lit', t: 'String', v: ty.n, ln }; }
                fail("'.class' expected", ln);
              }
          }
          if (t.v === 'else') fail("illegal start of expression", ln);
          fail('illegal start of expression', ln);
          break;
        case 'op':
          if (t.v === '(') { p++; const e = parseExpr(); expect(')'); return { k: 'paren', e, ln }; }
          if (t.v === '{') fail('illegal start of expression', ln);
          fail('illegal start of expression', ln);
          break;
        case 'eof': fail('reached end of file while parsing', prevLn());
      }
      fail('illegal start of expression', ln);
    }
    function parsePostfix(e) {
      for (;;) {
        const t = peek(), ln = t.ln;
        if (t.t !== 'op') break;
        if (t.v === '.') {
          p++;
          if (is('new')) { e = parseNew(e); continue; }
          if (is('this')) { p++; e = { k: 'qthis', cls: e, ln }; continue; }
          if (is('class')) { p++; e = { k: 'classlit', e, ln }; continue; }
          if (is('<')) parseTypeArgs();
          const name = (is('super') ? (p++, 'super') : ident());
          if (is('(')) e = { k: 'call', obj: e, name, args: parseArgs(), ln };
          else e = { k: 'field', obj: e, name, ln };
        } else if (t.v === '[') {
          // generic-array type in expression position like String[]::new
          if (is(']', 1)) { // Type[]::new or Type[].class
            let dims = 0; while (is('[') && is(']', 1)) { p += 2; dims++; }
            if (is('::')) { p++; expect('new'); e = { k: 'mref', type: { n: e.k === 'name' ? e.id : '?', args: [], dims }, name: 'new', ln }; continue; }
            fail('illegal start of expression', ln);
          }
          p++; const idx = parseExpr(); expect(']');
          e = { k: 'index', arr: e, idx, ln };
        } else if (t.v === '++' || t.v === '--') { p++; e = { k: 'incdec', op: t.v, prefix: false, e, ln }; }
        else if (t.v === '::') { p++; const name = eat('new') ? 'new' : ident(); e = { k: 'mref', obj: e, name, ln }; }
        else if (t.v === '<' && e.k === 'name' && /^[A-Z]/.test(e.id)) {
          // generic type used with a method reference: List<String>::new
          const r = tryParse(() => { parseTypeArgs(); if (!is('::')) fail('x'); return true; });
          if (!r) break;
        } else break;
      }
      return e;
    }

    /* patterns: `Type name`, `var name`, `Type(pattern, ...) [name]` */
    function parseRecPatRest(type) {
      const ln = peek().ln;
      expect('(');
      const subs = [];
      if (!is(')')) for (;;) { subs.push(parsePattern()); if (!eat(',')) break; }
      expect(')');
      let name = null;
      if (isId() && !isIdv('when')) name = ident();
      return { k: 'recpat', type, subs, name, ln };
    }
    function parsePattern() {
      const ln = peek().ln;
      eat('final');
      const type = parseType();
      if (is('(')) return parseRecPatRest(type);
      return { k: 'typepat', type, name: ident(), ln };
    }

    /* switch (statement and expression forms) */
    function parseSwitch(isExpr) {
      const ln = peek().ln;
      expect('switch');
      expect('(');
      const sel = parseExpr();
      expect(')');
      expect('{');
      const cases = [];
      while (!is('}')) {
        eofCheck();
        const c = { labels: null, arrow: false, body: [], ln: peek().ln, guard: null };
        if (eat('default')) { c.labels = null; c.isDefault = true; }
        else {
          if (!is('case')) fail("case, default, or '}' expected");
          p++;
          c.labels = [];
          for (;;) {
            if (is('null')) { p++; c.labels.push({ k: 'lit', t: 'null', v: null, ln }); }
            else if (is('default')) { p++; c.isDefault = true; }
            else {
              // type pattern: Type ident
              const pat = tryParse(() => parsePattern());
              c.labels.push(pat || parseTernary());
            }
            if (!eat(',')) break;
          }
          if (isIdv('when')) { p++; c.guard = parseExpr(true); }
        }
        if (eat('->')) {
          c.arrow = true;
          if (is('{')) c.body = [parseBlock()];
          else if (is('throw')) c.body = [parseStatement()];
          else { const e = parseExpr(); expect(';'); c.body = [{ k: 'exprval', e, ln: c.ln }]; }
        } else {
          if (!eat(':')) fail("':' or '->' expected", prevLn());
          while (!is('case') && !is('default') && !is('}')) { eofCheck(); c.body.push(parseStatement()); }
          if (is('default') && (is('->', 1) === false && !is(':', 1))) fail("':' or '->' expected");
        }
        cases.push(c);
      }
      expect('}');
      return { k: isExpr ? 'switchexpr' : 'switch', sel, cases, ln };
    }

    /* statements */
    function parseBlock() {
      const ln = peek().ln;
      expect('{');
      const body = [];
      while (!is('}')) { if (peek().t === 'eof') fail('reached end of file while parsing', prevLn()); body.push(parseStatement()); }
      const end = peek().ln;
      p++;
      return { k: 'block', body, ln, end };
    }
    function parseVarDecls(type, mods, ln) {
      const decls = [];
      for (;;) {
        const nln = peek().ln;
        const name = ident();
        let dims = 0;
        while (is('[') && is(']', 1)) { p += 2; dims++; }
        let init = null;
        if (eat('=')) init = is('{') ? parseArrayInit() : parseExpr();
        decls.push({ name, dims, init, ln: nln });
        if (!eat(',')) break;
      }
      return { k: 'local', type, decls, final: mods && mods.has('final'), ln };
    }
    const looksLikeDecl = () => {
      const save = p;
      const ty = tryParse(() => parseType());
      const ok = !!ty && isId() && (is('=', 1) || is(';', 1) || is(',', 1) || is('[', 1) || is(':', 1));
      p = save;
      return ok;
    };
    function parseStatement() {
      const t = peek(), ln = t.ln;
      if (t.t === 'op') {
        if (t.v === '{') return parseBlock();
        if (t.v === ';') { p++; return { k: 'empty', ln }; }
        if (t.v === '@') { skipAnno(); return parseStatement(); }
      }
      if (t.t === 'eof') fail('reached end of file while parsing', prevLn());
      if (t.t === 'kw') {
        switch (t.v) {
          case 'if': {
            p++; expect('('); const c = parseExpr(); expect(')');
            const a = parseStatement(); let b = null;
            if (eat('else')) b = parseStatement();
            return { k: 'if', c, a, b, ln };
          }
          case 'while': { p++; expect('('); const c = parseExpr(); expect(')'); return { k: 'while', c, body: parseStatement(), ln }; }
          case 'do': { p++; const body = parseStatement(); if (!is('while')) fail("'while' expected", peek().ln); p++; expect('('); const c = parseExpr(); expect(')'); expect(';'); return { k: 'do', c, body, ln }; }
          case 'for': {
            p++; expect('(');
            // foreach?
            const save = p;
            const fe = tryParse(() => { const mods = parseMods(); const ty = parseType(); const nm = ident(); if (!is(':')) fail('x'); p++; return { ty, nm }; });
            if (fe) { const iter = parseExpr(); expect(')'); return { k: 'foreach', type: fe.ty, name: fe.nm, iter, body: parseStatement(), ln }; }
            p = save;
            let init = [];
            if (!is(';')) {
              const mods = parseMods();
              if (mods.size || (peek().t === 'kw' && PRIMS.has(peek().v)) || (isIdv('var') && isId(1)) || looksLikeDecl()) {
                const ty = parseType(); init = [parseVarDecls(ty, mods, ln)];
              } else for (;;) { init.push({ k: 'expr', e: parseExpr(), ln }); if (!eat(',')) break; }
            }
            expect(';');
            const c = is(';') ? null : parseExpr();
            expect(';');
            const update = [];
            if (!is(')')) for (;;) { update.push(parseExpr()); if (!eat(',')) break; }
            expect(')');
            return { k: 'for', init, c, update, body: parseStatement(), ln };
          }
          case 'return': { p++; let e = null; if (!is(';')) e = parseExpr(); expect(';'); return { k: 'return', e, ln }; }
          case 'break': { p++; let label = null; if (isId()) label = ident(); expect(';'); return { k: 'break', label, ln }; }
          case 'continue': { p++; let label = null; if (isId()) label = ident(); expect(';'); return { k: 'continue', label, ln }; }
          case 'throw': { p++; const e = parseExpr(); expect(';'); return { k: 'throw', e, ln }; }
          case 'try': return parseTry();
          case 'switch': {
            // a switch expression used as a statement start is a statement
            const s = parseSwitch(false); return s;
          }
          case 'synchronized': { p++; expect('('); parseExpr(); expect(')'); return parseBlock(); }
          case 'assert': { p++; const c = parseExpr(); let m = null; if (eat(':')) m = parseExpr(); expect(';'); return { k: 'assert', c, m, ln }; }
          case 'else': fail("'else' without 'if'", ln); break;
          case 'class': case 'interface': case 'enum': case 'abstract': case 'static': case 'final': case 'private': case 'public': case 'protected': {
            const mods = parseMods();
            if (is('class') || is('interface') || is('enum') || isIdv('record')) { const d = parseTypeDecl(mods); return { k: 'localclass', decl: d, ln }; }
            if (mods.has('static') || mods.has('public') || mods.has('private') || mods.has('protected')) fail('illegal start of expression', ln);
            if (mods.has('final') || mods.size) { const ty = parseType(); const d = parseVarDecls(ty, mods, ln); expect(';'); return d; }
            break;
          }
          case 'case': case 'default': fail('orphaned ' + t.v, ln); break;
          case 'catch': fail("'catch' without 'try'", ln); break;
          case 'finally': fail("'finally' without 'try'", ln); break;
          default:
            if (PRIMS.has(t.v)) { const ty = parseType(); if (!isId()) { if (is('.') || is('[') ) fail('not a statement', ln); fail(peek().t === 'eof' ? 'reached end of file while parsing' : 'not a statement', peek().t === 'eof' ? prevLn() : ln); } const d = parseVarDecls(ty, null, ln); expect(';'); return d; }
            if (t.v === 'void') fail('illegal start of expression', ln);
        }
      }
      if (t.t === 'id') {
        if (is(':', 1) && !is('::', 1)) { const label = ident(); p++; return { k: 'labeled', label, body: parseStatement(), ln }; }
        if (t.v === 'yield' && !is('=', 1) && !is('.', 1) && !is('[', 1) && !is('++', 1) && !is('--', 1)) { p++; const e = parseExpr(); expect(';'); return { k: 'yield', e, ln }; }
        if (t.v === 'record' && isId(1) && (is('(', 2) || is('<', 2))) { const d = parseTypeDecl(new Set()); return { k: 'localclass', decl: d, ln }; }
        if (t.v === 'var' && isId(1)) { p++; const d = parseVarDecls({ n: 'var', args: [], dims: 0 }, null, ln); expect(';'); return d; }
        // local variable declaration?
        const save = p;
        const ty = tryParse(() => parseType());
        if (ty && isId() && (is('=', 1) || is(';', 1) || is(',', 1) || is('[', 1) || is(':', 1))) {
          const d = parseVarDecls(ty, null, ln); expect(';'); return d;
        }
        if (ty && isId() && peek(1).t !== 'op') fail("';' expected", prevLn() === peek().ln ? peek().ln : prevLn());
        p = save;
      }
      // expression statement
      const e = parseExpr();
      if (!['assign', 'incdec', 'call', 'new', 'ctorcall'].includes(e.k) && !(e.k === 'paren' && false)) {
        if (e.k === 'name' && peek().t === 'id') fail("';' expected", prevLn());
        fail('not a statement', ln);
      }
      expect(';');
      return { k: 'expr', e, ln };
    }
    function parseTry() {
      const ln = peek().ln;
      expect('try');
      const res = [];
      if (eat('(')) {
        while (!is(')')) {
          const mods = parseMods();
          const rl = peek().ln;
          if (isId() && !isId(1) && !is('<', 1) && !is('.', 1) && !is('[', 1)) { res.push({ expr: parseExpr(), ln: rl }); }
          else { const ty = parseType(); const name = ident(); expect('='); const init = parseExpr(); res.push({ type: ty, name, init, ln: rl }); }
          if (!eat(';')) break;
        }
        expect(')');
      }
      const block = parseBlock();
      const catches = [];
      while (is('catch')) {
        const cl = peek().ln; p++; expect('(');
        parseMods();
        const types = [parseType()];
        while (eat('|')) types.push(parseType());
        const name = ident();
        expect(')');
        catches.push({ types, name, body: parseBlock(), ln: cl });
      }
      let fin = null;
      if (eat('finally')) fin = parseBlock();
      if (!catches.length && !fin && !res.length) fail("'try' without 'catch', 'finally' or resource declarations", ln);
      return { k: 'try', res, block, catches, fin, ln };
    }

    /* declarations */
    function parseTypeParams() {
      const out = [];
      if (!is('<')) return out;
      p++;
      for (;;) {
        skipAnno();
        const name = ident(); let bound = null;
        if (eat('extends')) { bound = parseType(); while (eat('&')) parseType(); }
        out.push({ name, bound });
        if (!eat(',')) break;
      }
      if (!is('>')) fail("'>' expected");
      p++;
      return out;
    }
    function parseParams() {
      expect('(');
      const ps = [];
      if (!is(')')) for (;;) {
        parseMods();
        const ty = parseType();
        let varargs = false;
        if (eat('...')) varargs = true;
        const name = ident();
        let dims = 0; while (is('[') && is(']', 1)) { p += 2; dims++; }
        ps.push({ type: dims ? { ...ty, dims: ty.dims + dims } : ty, name, varargs });
        if (!eat(',')) break;
      }
      expect(')');
      return ps;
    }
    function parseClassBody(decl, ln) {
      expect('{');
      const members = [];
      while (!is('}')) {
        if (peek().t === 'eof') fail('reached end of file while parsing', prevLn());
        if (eat(';')) continue;
        members.push(...parseMember(decl));
      }
      p++;
      return members;
    }
    function parseMember(decl) {
      const mln = peek().ln;
      const mods = parseMods();
      if (is('{')) return [{ k: 'init', static: mods.has('static'), body: parseBlock(), ln: mln }];
      if (is('class') || is('interface') || is('enum') || (is('@') && is('interface', 1)) || (isIdv('record') && isId(1) && (is('(', 2) || is('<', 2)))) return [parseTypeDecl(mods)];
      const tparams = parseTypeParams();
      // constructor?
      if (isId() && is('(', 1) && peek().v === decl.name) {
        const name = ident(); const params = parseParams();
        const throws = [];
        if (eat('throws')) { do throws.push(parseType()); while (eat(',')); }
        const body = parseBlock();
        return [{ k: 'method', mods, tparams, ret: null, name: '<init>', params, throws, body, isCtor: true, ln: mln }];
      }
      // compact canonical record constructor
      if (decl.kind === 'record' && isId() && peek().v === decl.name && is('{', 1)) {
        p++; const body = parseBlock();
        return [{ k: 'method', mods, tparams, ret: null, name: '<init>', params: null, throws: [], body, isCtor: true, compact: true, ln: mln }];
      }
      if (isId() && is('(', 1)) fail('invalid method declaration; return type required', peek().ln);
      const ty = parseType(true);
      const nln = peek().ln;
      const name = ident();
      if (is('(')) {
        const params = parseParams();
        let dims = 0; while (is('[') && is(']', 1)) { p += 2; dims++; }
        const throws = [];
        if (eat('throws')) { do throws.push(parseType()); while (eat(',')); }
        let body = null;
        if (is('{')) body = parseBlock();
        else if (is(';')) p++;
        else if (eat('default')) { parseExpr(); expect(';'); }
        else fail(peek().t === 'eof' ? 'reached end of file while parsing' : "'{' or ';' expected", peek().t === 'eof' ? prevLn() : prevLn());
        if (decl.kind === 'interface') {
          if (!body && !mods.has('static')) mods.add('abstract');
        }
        return [{ k: 'method', mods, tparams, ret: dims ? { ...ty, dims: ty.dims + dims } : ty, name, params, throws, body, isCtor: false, ln: nln, startLn: mln }];
      }
      // field(s)
      if (ty.n === 'void') fail("'(' expected", prevLn());
      p--;
      const d = parseVarDecls(ty, mods, mln);
      expect(';');
      if (decl.kind === 'interface') { mods.add('static'); mods.add('final'); }
      return [{ k: 'field', mods, type: ty, decls: d.decls, ln: mln }];
    }
    function parseTypeDecl(mods) {
      const ln = peek().ln;
      let kind;
      if (eat('class')) kind = 'class'; else if (eat('interface')) kind = 'interface'; else if (eat('enum')) kind = 'enum';
      else if (isIdv('record')) { p++; kind = 'record'; }
      else if (is('@') && is('interface', 1)) { p += 2; kind = 'interface'; }
      else fail('class, interface, enum, or record expected', ln);
      const name = ident();
      const tparams = parseTypeParams();
      const d = { k: 'class', kind, name, mods, tparams, ext: null, impl: [], members: [], enumConsts: [], recordComps: null, ln };
      if (kind === 'record') d.recordComps = parseParams();
      if (eat('extends')) {
        if (kind === 'interface') { do d.impl.push(parseType()); while (eat(',')); } else d.ext = parseType();
      }
      if (eat('implements')) { do d.impl.push(parseType()); while (eat(',')); }
      if (isIdv('permits')) { p++; do parseType(); while (eat(',')); }
      if (kind === 'enum') {
        expect('{');
        while (!is(';') && !is('}')) {
          skipAnno();
          const cln = peek().ln, cname = ident();
          let args = [], body = null;
          if (is('(')) args = parseArgs();
          if (is('{')) body = parseClassBody({ name: '', kind: 'class' }, cln);
          d.enumConsts.push({ name: cname, args, body, ln: cln });
          if (!eat(',')) break;
        }
        if (eat(';')) {
          while (!is('}')) { if (peek().t === 'eof') fail('reached end of file while parsing', prevLn()); if (eat(';')) continue; d.members.push(...parseMember(d)); }
        }
        expect('}');
      } else {
        if (!is('{')) { if (peek().t === 'eof') fail('reached end of file while parsing', prevLn()); fail("'{' expected", peek().ln); }
        d.members = parseClassBody(d, ln);
      }
      return d;
    }
    function parseUnit() {
      const unit = { types: [], imports: [] };
      skipAnno();
      if (is('package')) { while (!is(';')) { eofCheck(); p++; } p++; }
      while (is('import')) {
        p++; let s = ''; eat('static');
        while (!is(';')) { eofCheck(); s += peek().v; p++; }
        p++; unit.imports.push(s);
      }
      while (peek().t !== 'eof') {
        if (eat(';')) continue;
        const mods = parseMods();
        if (!(is('class') || is('interface') || is('enum') || (is('@') && is('interface', 1)) || isIdv('record'))) {
          if (peek().t === 'id' || peek().t === 'kw' && PRIMS.has(peek().v) || peek().t === 'kw' && peek().v === 'void') fail('class, interface, enum, or record expected', peek().ln);
          fail('class, interface, enum, or record expected', peek().ln);
        }
        unit.types.push(parseTypeDecl(mods));
      }
      return unit;
    }
    return parseUnit();
  }
  return { parse, lex, PRIMS };
})();
if (typeof module !== 'undefined') module.exports = JavaParser;
