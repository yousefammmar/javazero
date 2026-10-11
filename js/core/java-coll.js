/* JavaZero compiler, stage 3b: java.util collections, streams, Optional, Arrays/Collections helpers, functional interfaces. */
JavaRT.extensions.push((rt, L, H) => {
  const { mk, throwEx, str, hash, equals, compare, fnOf, keyCmp, defLib, iface, arr, identityHash } = H;
  const JObject = L.Object, D = rt.D, Ch = rt.Ch;
  const toNum = H.toNum; const { f64, u32, dstr } = H;
  const isNullish = x => x === null || x === undefined;
  const CME = () => throwEx('ConcurrentModificationException');
  const NSE = (m) => throwEx('NoSuchElementException', m);
  const UOE = () => throwEx('UnsupportedOperationException');

  /* ---------- functional interface markers ---------- */
  ['Function', 'BiFunction', 'Supplier', 'Consumer', 'BiConsumer', 'Predicate', 'BiPredicate', 'UnaryOperator', 'BinaryOperator', 'IntBinaryOperator', 'IntUnaryOperator', 'IntPredicate', 'IntFunction',
    'IntConsumer', 'IntSupplier', 'ToIntFunction', 'ToDoubleFunction', 'ToLongFunction', 'ToIntBiFunction', 'DoubleUnaryOperator', 'DoubleBinaryOperator', 'BooleanSupplier', 'DoubleSupplier', 'DoubleFunction',
    'Callable', 'ObjIntConsumer', 'LongUnaryOperator', 'LongBinaryOperator', 'IntToDoubleFunction', 'DoublePredicate', 'LongFunction', 'LongPredicate'].forEach(n => iface(n, 'java.util.function.' + n));
  L.Callable.$jfq = 'java.util.concurrent.Callable';
  L.Function.identity = L.UnaryOperator.identity = () => rt.$lam(x => x);
  L.Predicate.not = p => rt.$lam((...a) => !fnOf(p)(...a));
  L.Predicate.isEqual = t => rt.$lam(x => equals(t, x));
  L.BinaryOperator.minBy = c => rt.$lam((a, b) => fnOf(c, 'compare')(a, b) <= 0 ? a : b);
  L.BinaryOperator.maxBy = c => rt.$lam((a, b) => fnOf(c, 'compare')(a, b) >= 0 ? a : b);
  const lamDefaults = {};
  ['andThen', 'compose', 'negate', 'and', 'or'].forEach(k => { lamDefaults[k] = function (...a) { return rt.$lam((...x) => this.apply ? this.apply(...x) : this.accept ? this.accept(...x) : this.test(...x))[k](...a); }; });
  [L.Function, L.Predicate, L.Consumer, L.BiFunction, L.BiPredicate].forEach(c => { c.$defaults = lamDefaults; });
  const Comparator = iface('Comparator', 'java.util.Comparator');
  const asFn = o => (typeof o === 'function' ? o : (...a) => o.compare(...a));
  Comparator.$defaults = {
    reversed() { return rt.$lam(asFn(this))['reversed'](); },
    thenComparing(k, c) { return rt.$lam(asFn(this)).thenComparing(k, c); },
    thenComparingInt(k) { return rt.$lam(asFn(this)).thenComparing(k); }, thenComparingDouble(k) { return rt.$lam(asFn(this)).thenComparing(k); }, thenComparingLong(k) { return rt.$lam(asFn(this)).thenComparing(k); },
  };
  const cmpFn = c => c === null || c === undefined ? compare : (typeof c === 'function' ? c : (a, b) => c.compare(a, b));
  H.cmpFn = cmpFn;
  Comparator.naturalOrder = () => rt.$lam((a, b) => compare(a, b));
  Comparator.reverseOrder = () => rt.$lam((a, b) => compare(b, a));
  Comparator.comparing = (k, c) => rt.$lam(keyCmp(k, c));
  Comparator.comparingInt = Comparator.comparingDouble = Comparator.comparingLong = k => rt.$lam(keyCmp(k));
  Comparator.nullsFirst = c => rt.$lam((a, b) => isNullish(a) ? (isNullish(b) ? 0 : -1) : isNullish(b) ? 1 : cmpFn(c)(a, b));
  Comparator.nullsLast = c => rt.$lam((a, b) => isNullish(a) ? (isNullish(b) ? 0 : 1) : isNullish(b) ? -1 : cmpFn(c)(a, b));
  L.Map_Entry_comparingByKey = null;
  const Iterator = iface('Iterator', 'java.util.Iterator');
  Iterator.$defaults = { forEachRemaining(f) { const g = fnOf(f, 'accept'); while (this.hasNext()) g(this.next()); }, remove() { UOE(); } };

  /* ---------- Optional ---------- */
  class Optional extends JObject {
    constructor() { super(); this.v = null; this.kind = ''; }
    static of(v) { if (isNullish(v)) throw mk('NullPointerException'); const o = new Optional(); o.v = v; return o; }
    static ofNullable(v) { const o = new Optional(); o.v = isNullish(v) ? null : v; return o; }
    static empty() { return new Optional(); }
    isPresent() { return this.v !== null; } isEmpty() { return this.v === null; }
    get() { if (this.v === null) NSE('No value present'); return this.v; }
    getAsInt() { return this.get(); } getAsDouble() { return this.get(); } getAsLong() { return this.get(); } orElseThrow(s) { if (this.v === null) { if (s) throw fnOf(s)(); NSE('No value present'); } return this.v; }
    orElse(d) { return this.v !== null ? this.v : d; } orElseGet(s) { return this.v !== null ? this.v : fnOf(s)(); }
    ifPresent(f) { if (this.v !== null) fnOf(f, 'accept')(this.v); } ifPresentOrElse(f, g) { if (this.v !== null) fnOf(f, 'accept')(this.v); else fnOf(g)(); }
    map(f) { return this.v === null ? this : Optional.ofNullable(fnOf(f, 'apply')(this.v)); } flatMap(f) { return this.v === null ? this : fnOf(f, 'apply')(this.v); }
    filter(p) { return this.v === null || fnOf(p, 'test')(this.v) ? this : new Optional(); } or(s) { return this.v !== null ? this : fnOf(s)(); }
    stream() { return Stream.fromArray(this.v === null ? [] : [this.v]); }
    equals(o) { return o instanceof Optional && equals(this.v, o.v); } hashCode() { return hash(this.v); }
    toString() { const n = this.kind ? 'Optional' + this.kind : 'Optional'; return this.v === null ? n + '.empty' : n + '[' + str(this.v) + ']'; }
  }
  defLib(Optional, 'Optional', 'java.util.Optional');
  const mkOpt = (v, kind) => { const o = new Optional(); o.v = v; o.kind = kind; return o; };
  L.OptionalInt = class OptionalInt extends Optional { static of(v) { return mkOpt(v, 'Int'); } static empty() { return mkOpt(null, 'Int'); } }; defLib(L.OptionalInt, 'OptionalInt', 'java.util.OptionalInt');
  L.OptionalDouble = class OptionalDouble extends Optional { static of(v) { return mkOpt(v, 'Double'); } static empty() { return mkOpt(null, 'Double'); } }; defLib(L.OptionalDouble, 'OptionalDouble', 'java.util.OptionalDouble');
  L.OptionalLong = class OptionalLong extends Optional { static of(v) { return mkOpt(v, 'Long'); } static empty() { return mkOpt(null, 'Long'); } }; defLib(L.OptionalLong, 'OptionalLong', 'java.util.OptionalLong');

  /* ---------- Map.Entry ---------- */
  class Entry extends JObject {
    constructor(k, v, h) { super(); this.k = k; this.v = v; this.h = h; this.nx = null; this.bf = null; this.af = null; }
    $init(k, v) { this.k = k; this.v = v; return this; }
    getKey() { return this.k; } getValue() { return this.v; } setValue(v) { if (this.ro) UOE(); const o = this.v; this.v = v; return o; }
    toString() { return str(this.k) + '=' + str(this.v); } equals(o) { return o instanceof Entry && equals(this.k, o.k) && equals(this.v, o.v); } hashCode() { return hash(this.k) ^ hash(this.v); }
    static comparingByKey(c) { const f = cmpFn(c); return rt.$lam((a, b) => f(a.k, b.k)); }
    static comparingByValue(c) { const f = cmpFn(c); return rt.$lam((a, b) => f(a.v, b.v)); }
  }
  defLib(Entry, 'Entry', 'java.util.Map.Entry'); L['Map.Entry'] = Entry; L.SimpleEntry = Entry; L['AbstractMap.SimpleEntry'] = Entry; L['AbstractMap.SimpleImmutableEntry'] = Entry;

  /* ---------- Collection base ---------- */
  iface('Collection', 'java.util.Collection'); L.Collection.$supers.add(L.Iterable);
  class Coll extends JObject {
    $arr() { throw new Error('abstract'); }
    $mc() { return 0; }
    size() { return this.$arr().length; } isEmpty() { return this.size() === 0; }
    contains(o) { return this.$arr().some(x => equals(o, x)); }
    containsAll(c) { return Array.from(rt.$iter(c)).every(x => this.contains(x)); }
    toString() { return '[' + this.$arr().map(x => x === this ? '(this Collection)' : str(x)).join(', ') + ']'; }
    forEach(f) { const g = fnOf(f, 'accept'); const e = this.$mc(); for (const x of this.$arr()) { g(x); if (this.$mc() !== e) CME(); } }
    stream() { return Stream.fromArray(this.$arr().slice()); } parallelStream() { return this.stream(); }
    toArray(a) { const r = this.$arr().slice(); r.$t = a && a.$t ? a.$t : 'Object'; return r; }
    iterator() { return new SnapIter(this); }
    *[Symbol.iterator]() { const e = this.$mc(); const a = this.$arr(); for (let i = 0; i < a.length; i++) { if (this.$mc() !== e) CME(); yield a[i]; } }
    removeIf(p) { const f = fnOf(p, 'test'); let ch = false; for (const x of this.$arr().slice()) if (f(x)) { this.remove(x); ch = true; } return ch; }
    removeAll(c) { let ch = false; const o = Array.from(rt.$iter(c)); for (const x of this.$arr().slice()) if (o.some(y => equals(x, y))) { this.remove(x); ch = true; } return ch; }
    retainAll(c) { let ch = false; const o = Array.from(rt.$iter(c)); for (const x of this.$arr().slice()) if (!o.some(y => equals(x, y))) { this.remove(x); ch = true; } return ch; }
    addAll(c) { let ch = false; for (const x of Array.from(rt.$iter(c))) ch = this.add(x) || ch; return ch; }
    hashCode() { return this.$arr().reduce((h, x) => (h + hash(x)) | 0, 0); }
  }
  defLib(Coll, 'AbstractCollection', 'java.util.AbstractCollection', [L.Collection, L.Iterable]);
  class SnapIter extends JObject {
    constructor(c) { super(); this.c = c; this.a = c.$arr().slice(); this.i = 0; this.last = undefined; this.e = c.$mc(); }
    hasNext() { return this.i < this.a.length; }
    next() { if (this.c.$mc() !== this.e) CME(); if (this.i >= this.a.length) NSE(); return this.last = this.a[this.i++]; }
    remove() { if (this.last === undefined) throwEx('IllegalStateException'); if (this.c.$mc() !== this.e) CME(); this.c.$removeEl(this.last); this.last = undefined; this.e = this.c.$mc(); }
    forEachRemaining(f) { const g = fnOf(f); while (this.hasNext()) g(this.next()); }
  }
  defLib(SnapIter, 'Iterator', 'java.util.Iterator', [Iterator]);
  Coll.prototype.$removeEl = function (x) { this.remove(x); };

  /* ---------- sequences: ArrayList / LinkedList / ArrayDeque / Stack / immutable ---------- */
  const idx = (i, n) => throwEx('IndexOutOfBoundsException', `Index ${i} out of bounds for length ${n}`);
  const aidx = (i, n) => throwEx('ArrayIndexOutOfBoundsException', `Index ${i} out of bounds for length ${n}`);
  iface('List', 'java.util.List'); L.List.$supers.add(L.Collection); L.List.$supers.add(L.Iterable);
  iface('Queue', 'java.util.Queue'); L.Queue.$supers.add(L.Collection);
  iface('Deque', 'java.util.Deque'); L.Deque.$supers.add(L.Queue); L.Deque.$supers.add(L.Collection);
  iface('Set', 'java.util.Set'); L.Set.$supers.add(L.Collection);
  iface('SortedSet', 'java.util.SortedSet'); iface('NavigableSet', 'java.util.NavigableSet'); L.SortedSet.$supers.add(L.Set); L.NavigableSet.$supers.add(L.SortedSet); L.NavigableSet.$supers.add(L.Set);
  iface('Map', 'java.util.Map'); iface('SortedMap', 'java.util.SortedMap'); iface('NavigableMap', 'java.util.NavigableMap'); L.SortedMap.$supers.add(L.Map); L.NavigableMap.$supers.add(L.SortedMap); L.NavigableMap.$supers.add(L.Map);
  iface('RandomAccess', 'java.util.RandomAccess'); iface('SequencedCollection', 'java.util.SequencedCollection');
  class ArrSeq extends Coll {
    constructor() { super(); this.a = []; this.mc = 0; this.ro = 0; /* 0 mutable, 1 fixed-size, 2 read-only */ }
    $init(x) {
      if (x === undefined) return this;
      if (typeof x === 'number') { if (x < 0) throwEx('IllegalArgumentException', 'Illegal Capacity: ' + x); return this; }
      this.a = Array.from(rt.$iter(x)); return this;
    }
    $arr() { return this.a; } $mc() { return this.mc; }
    $oob(i) { idx(i, this.a.length); }
    $w() { if (this.ro) UOE(); }
    $s() { if (this.ro === 2) UOE(); }
    size() { return this.a.length; } isEmpty() { return this.a.length === 0; }
    get(i) { i = toNum(i); if (i < 0 || i >= this.a.length) this.$oob(i); return this.a[i]; }
    set(i, v) { this.$s(); i = toNum(i); if (i < 0 || i >= this.a.length) this.$oob(i); const o = this.a[i]; this.a[i] = v; return o; }
    add(i, v) {
      this.$w();
      if (arguments.length === 1) { this.$check(i); this.a.push(i); this.mc++; return true; }
      i = toNum(i); if (i < 0 || i > this.a.length) throwEx('IndexOutOfBoundsException', `Index: ${i}, Size: ${this.a.length}`);
      this.$check(v); this.a.splice(i, 0, v); this.mc++;
    }
    $check() {}
    addAll(i, c) {
      this.$w();
      if (arguments.length === 1) { const items = Array.from(rt.$iter(i)); items.forEach(x => this.$check(x)); this.a.push(...items); this.mc++; return items.length > 0; }
      i = toNum(i); if (i < 0 || i > this.a.length) throwEx('IndexOutOfBoundsException', `Index: ${i}, Size: ${this.a.length}`);
      const items = Array.from(rt.$iter(c)); this.a.splice(i, 0, ...items); this.mc++; return items.length > 0;
    }
    remove$idx(i) { this.$w(); i = toNum(i); if (i < 0 || i >= this.a.length) this.$oob(i); this.mc++; return this.a.splice(i, 1)[0]; }
    remove(o) {
      if (arguments.length === 0) return this.removeFirst();
      this.$w(); const i = this.a.findIndex(x => equals(o, x)); if (i < 0) return false; this.a.splice(i, 1); this.mc++; return true;
    }
    $removeEl(x) { this.remove(x); }
    removeAt(i) { return this.remove$idx(i); }
    indexOf(o) { return this.a.findIndex(x => equals(o, x)); }
    lastIndexOf(o) { for (let i = this.a.length - 1; i >= 0; i--) if (equals(o, this.a[i])) return i; return -1; }
    contains(o) { return this.indexOf(o) >= 0; }
    clear() { this.$w(); this.a.length = 0; this.mc++; }
    sort(c) { this.$s(); const f = cmpFn(c); const e = this.mc; this.a.sort(f); if (this.mc !== e) CME(); this.mc++; }
    replaceAll(f) { this.$s(); const g = fnOf(f, 'apply'); this.a = this.a.map(x => g(x)); }
    removeIf(p) { this.$w(); const f = fnOf(p, 'test'); const n = this.a.length; this.a = this.a.filter(x => !f(x)); if (this.a.length !== n) { this.mc++; return true; } return false; }
    removeAll(c) { this.$w(); const o = Array.from(rt.$iter(c)); const n = this.a.length; this.a = this.a.filter(x => !o.some(y => equals(x, y))); if (n !== this.a.length) this.mc++; return n !== this.a.length; }
    retainAll(c) { this.$w(); const o = Array.from(rt.$iter(c)); const n = this.a.length; this.a = this.a.filter(x => o.some(y => equals(x, y))); if (n !== this.a.length) this.mc++; return n !== this.a.length; }
    subList(a, b) { a = toNum(a); b = toNum(b); if (a < 0) throwEx('IndexOutOfBoundsException', `fromIndex = ${a}`); if (b > this.a.length) throwEx('IndexOutOfBoundsException', `toIndex = ${b}`); if (a > b) throwEx('IllegalArgumentException', `fromIndex(${a}) > toIndex(${b})`); return new SubList(this, a, b - a); }
    iterator() { return new ListIter(this, 0); } listIterator(i) { i = i === undefined ? 0 : toNum(i); if (i < 0 || i > this.a.length) throwEx('IndexOutOfBoundsException', `Index: ${i}, Size: ${this.a.length}`); return new ListIter(this, i); }
    *[Symbol.iterator]() { let c = 0; const e = this.mc; for (;;) { if (c === this.a.length) return; if (this.mc !== e || c > this.a.length) CME(); yield this.a[c++]; } }
    equals(o) { if (o === this) return true; if (!(o instanceof Coll) || !this.constructor.$supers.has(L.List) || !o.constructor.$supers.has(L.List)) return false; const b = o.$arr(); return this.a.length === b.length && this.a.every((x, i) => equals(x, b[i])); }
    hashCode() { return this.a.reduce((h, x) => (Math.imul(31, h) + hash(x)) | 0, 1); }
    toArray(t) { const r = this.a.slice(); r.$t = t && t.$t ? t.$t : 'Object'; return r; }
    ensureCapacity() {} trimToSize() {}
    reversed() { const l = new ArrayList(); l.a = this.a.slice().reverse(); return l; }
    /* deque & sequenced operations */
    addFirst(x) { this.$w(); this.$check(x); this.a.unshift(x); this.mc++; } addLast(x) { this.$w(); this.$check(x); this.a.push(x); this.mc++; }
    offerFirst(x) { this.addFirst(x); return true; } offerLast(x) { this.addLast(x); return true; } offer(x) { this.addLast(x); return true; }
    push(x) { this.addFirst(x); }
    getFirst() { if (!this.a.length) NSE(); return this.a[0]; } getLast() { if (!this.a.length) NSE(); return this.a[this.a.length - 1]; }
    element() { return this.getFirst(); }
    peek() { return this.a.length ? this.a[0] : null; } peekFirst() { return this.peek(); } peekLast() { return this.a.length ? this.a[this.a.length - 1] : null; }
    removeFirst() { this.$w(); if (!this.a.length) NSE(); this.mc++; return this.a.shift(); } removeLast() { this.$w(); if (!this.a.length) NSE(); this.mc++; return this.a.pop(); }
    poll() { return this.a.length ? this.removeFirst() : null; } pollFirst() { return this.poll(); } pollLast() { return this.a.length ? this.removeLast() : null; }
    pop() { return this.removeFirst(); }
    removeFirstOccurrence(o) { return this.remove(o); }
    removeLastOccurrence(o) { const i = this.lastIndexOf(o); if (i < 0) return false; this.remove$idx(i); return true; }
    descendingIterator() { return new SnapIter({ $arr: () => this.a.slice().reverse(), $mc: () => this.mc, $removeEl: x => this.remove(x) }); }
    clone() { const c = new this.constructor(); c.a = this.a.slice(); return c; }
  }
  class SubList extends Coll {
    constructor(p, off, n) { super(); this.p = p; this.off = off; this.n = n; this.mc = p.mc; }
    $chk() { if (this.p.mc !== this.mc) CME(); }
    $sync() { this.mc = this.p.mc; }
    $arr() { this.$chk(); return this.p.a.slice(this.off, this.off + this.n); } $mc() { return this.p.mc; }
    size() { this.$chk(); return this.n; } isEmpty() { return this.size() === 0; }
    get(i) { this.$chk(); i = toNum(i); if (i < 0 || i >= this.n) idx(i, this.n); return this.p.a[this.off + i]; }
    set(i, v) { this.$chk(); this.p.$s(); i = toNum(i); if (i < 0 || i >= this.n) idx(i, this.n); const o = this.p.a[this.off + i]; this.p.a[this.off + i] = v; return o; }
    add(i, v) {
      this.$chk(); this.p.$w();
      if (arguments.length === 1) { this.p.a.splice(this.off + this.n, 0, i); this.n++; this.p.mc++; this.$sync(); return true; }
      i = toNum(i); if (i < 0 || i > this.n) throwEx('IndexOutOfBoundsException', `Index: ${i}, Size: ${this.n}`);
      this.p.a.splice(this.off + i, 0, v); this.n++; this.p.mc++; this.$sync();
    }
    remove$idx(i) { this.$chk(); this.p.$w(); i = toNum(i); if (i < 0 || i >= this.n) idx(i, this.n); this.n--; this.p.mc++; const r = this.p.a.splice(this.off + i, 1)[0]; this.$sync(); return r; }
    remove(o) { this.$chk(); this.p.$w(); const i = this.p.a.slice(this.off, this.off + this.n).findIndex(x => equals(o, x)); if (i < 0) return false; this.remove$idx(i); return true; }
    $removeEl(x) { this.remove(x); }
    clear() { this.$chk(); this.p.$w(); this.p.a.splice(this.off, this.n); this.n = 0; this.p.mc++; this.$sync(); }
    indexOf(o) { return this.$arr().findIndex(x => equals(o, x)); } lastIndexOf(o) { const a = this.$arr(); for (let i = a.length - 1; i >= 0; i--) if (equals(o, a[i])) return i; return -1; }
    sort(c) { this.$chk(); const part = this.p.a.slice(this.off, this.off + this.n).sort(cmpFn(c)); part.forEach((x, i) => { this.p.a[this.off + i] = x; }); }
    replaceAll(f) { const g = fnOf(f, 'apply'); for (let i = 0; i < this.n; i++) this.p.a[this.off + i] = g(this.p.a[this.off + i]); }
    subList(a, b) { const l = new ArrayList(); l.a = this.$arr().slice(a, b); return l; }
    *[Symbol.iterator]() { const e = this.p.mc; const a = this.$arr(); for (let i = 0; i < a.length; i++) { if (this.p.mc !== e) CME(); yield a[i]; } }
    equals(o) { return o === this || (o instanceof Coll && o.constructor.$supers.has(L.List) && this.$arr().length === o.$arr().length && this.$arr().every((x, i) => equals(x, o.$arr()[i]))); }
    hashCode() { return this.$arr().reduce((h, x) => (Math.imul(31, h) + hash(x)) | 0, 1); }
  }
  defLib(SubList, 'SubList', 'java.util.ArrayList$SubList', [L.List, L.Collection, L.Iterable, L.RandomAccess]);
  class ListIter extends JObject {
    constructor(l, i) { super(); this.l = l; this.i = toNum(i); this.last = -1; this.e = l.mc; }
    hasNext() { return this.i !== this.l.a.length; }
    next() { if (this.l.mc !== this.e) CME(); if (this.i >= this.l.a.length) NSE(); this.last = this.i; return this.l.a[this.i++]; }
    hasPrevious() { return this.i > 0; } previous() { if (this.l.mc !== this.e) CME(); if (this.i <= 0) NSE(); this.last = --this.i; return this.l.a[this.i]; }
    nextIndex() { return this.i; } previousIndex() { return this.i - 1; }
    remove() { if (this.last < 0) throwEx('IllegalStateException'); if (this.l.mc !== this.e) CME(); this.l.remove$idx(this.last); this.i = this.last; this.last = -1; this.e = this.l.mc; }
    set(v) { if (this.last < 0) throwEx('IllegalStateException'); this.l.set(this.last, v); }
    add(v) { this.l.add(this.i++, v); this.last = -1; this.e = this.l.mc; }
    forEachRemaining(f) { const g = fnOf(f); while (this.hasNext()) g(this.next()); }
  }
  defLib(ListIter, 'ListIterator', 'java.util.ListIterator', [Iterator]);
  class ArrayList extends ArrSeq { }
  defLib(ArrayList, 'ArrayList', 'java.util.ArrayList', [L.List, L.Collection, L.Iterable, L.RandomAccess, L.Cloneable]);
  class LinkedList extends ArrSeq { $oob(i) { throwEx('IndexOutOfBoundsException', `Index: ${i}, Size: ${this.a.length}`); } }
  defLib(LinkedList, 'LinkedList', 'java.util.LinkedList', [L.List, L.Deque, L.Queue, L.Collection, L.Iterable, L.Cloneable]);
  class ArrayDeque extends ArrSeq { remove$idx(x) { return this.remove(x); } $check(x) { if (isNullish(x)) throw mk('NullPointerException'); } get() { UOE(); } }
  defLib(ArrayDeque, 'ArrayDeque', 'java.util.ArrayDeque', [L.Deque, L.Queue, L.Collection, L.Iterable]);
  class Vector extends ArrSeq { $oob(i) { throwEx('ArrayIndexOutOfBoundsException', `Array index out of range: ${i}`); } addElement(x) { this.add(x); } elementAt(i) { return this.get(i); } firstElement() { return this.getFirst(); } lastElement() { return this.getLast(); } removeAllElements() { this.clear(); } push(x) { this.add(x); } }
  defLib(Vector, 'Vector', 'java.util.Vector', [L.List, L.Collection, L.Iterable, L.RandomAccess]);
  class Stack extends Vector {
    push(x) { this.a.push(x); this.mc++; return x; }
    pop() { if (!this.a.length) throw mk('EmptyStackException'); this.mc++; return this.a.pop(); }
    peek() { if (!this.a.length) throw mk('EmptyStackException'); return this.a[this.a.length - 1]; }
    empty() { return this.a.length === 0; }
    search(o) { const i = this.lastIndexOf(o); return i < 0 ? -1 : this.a.length - i; }
  }
  defLib(Stack, 'Stack', 'java.util.Stack', [Vector]);
  class ImmList extends ArrSeq { $oob(i) { if (this.a.length === 1 || this.a.length === 2) throwEx('IndexOutOfBoundsException', `Index: ${i} Size: ${this.a.length}`); aidx(i, this.a.length); } $check(x) { if (this.nonull && isNullish(x)) throw mk('NullPointerException'); } indexOf(o) { if (this.nonull && isNullish(o)) throw mk('NullPointerException'); return super.indexOf(o); } contains(o) { return this.indexOf(o) >= 0; } }
  defLib(ImmList, 'ImmutableCollections.ListN', 'java.util.ImmutableCollections$ListN', [L.List, L.Collection, L.Iterable, L.RandomAccess]);
  const imm = (items, nonull) => { const l = new ImmList(); l.a = items; l.ro = 2; l.nonull = nonull; return l; };
  H.imm = imm;
  class FixedList extends ArrSeq { $oob(i) { aidx(i, this.a.length); } }
  defLib(FixedList, 'Arrays.ArrayList', 'java.util.Arrays$ArrayList', [L.List, L.Collection, L.Iterable, L.RandomAccess]);

  L.List.of = (...a) => { const items = a.length === 1 && Array.isArray(a[0]) && a[0].$t !== 'int' && !a[0].$t?.match(/^(int|double|char|long|boolean|byte|short|float)$/) ? a[0].slice() : a; if (items.some(isNullish)) throw mk('NullPointerException'); return imm(items, true); };
  L.List.copyOf = c => imm(Array.from(rt.$iter(c)), true);

  /* ---------- PriorityQueue ---------- */
  class PriorityQueue extends Coll {
    constructor() { super(); this.q = []; this.cmp = null; this.mc = 0; }
    $init(a, b) {
      let src = null;
      if (typeof a === 'function' || (a && typeof a === 'object' && typeof a.compare === 'function' && !(a instanceof Coll))) this.cmp = cmpFn(a);
      else if (b !== undefined) this.cmp = cmpFn(b);
      else if (a instanceof Coll) { src = a; if (a instanceof PriorityQueue) this.cmp = a.cmp; else if (a.cmp) this.cmp = a.cmp; }
      if (src) { this.q = src.$arr().slice(); if (!(src instanceof PriorityQueue)) for (let i = (this.q.length >>> 1) - 1; i >= 0; i--) this.siftDown(i, this.q[i]); }
      return this;
    }
    c(a, b) { return this.cmp ? this.cmp(a, b) : compare(a, b); }
    $arr() { return this.q; } $mc() { return this.mc; }
    size() { return this.q.length; }
    offer(e) { if (isNullish(e)) throw mk('NullPointerException'); this.mc++; const i = this.q.length; this.q.push(e); if (i === 0) { if (!this.cmp) compare(e, e); } else this.siftUp(i, e); return true; }
    add(e) { return this.offer(e); }
    siftUp(k, x) { const q = this.q; while (k > 0) { const p = (k - 1) >>> 1, e = q[p]; if (this.c(x, e) >= 0) break; q[k] = e; k = p; } q[k] = x; }
    siftDown(k, x) { const q = this.q, n = q.length, half = n >>> 1; while (k < half) { let ch = 2 * k + 1, c = q[ch]; const r = ch + 1; if (r < n && this.c(c, q[r]) > 0) c = q[ch = r]; if (this.c(x, c) <= 0) break; q[k] = c; k = ch; } q[k] = x; }
    poll() { if (!this.q.length) return null; this.mc++; const q = this.q, res = q[0], x = q.pop(); if (q.length) this.siftDown(0, x); return res; }
    remove(o) { if (arguments.length === 0) { if (!this.q.length) NSE(); return this.poll(); } const i = this.q.findIndex(x => equals(o, x)); if (i < 0) return false; this.removeAt(i); return true; }
    removeAt(i) { this.mc++; const q = this.q, s = q.length - 1; if (s === i) q.pop(); else { const moved = q.pop(); this.siftDown(i, moved); if (q[i] === moved) this.siftUp(i, moved); } }
    $removeEl(x) { this.remove(x); }
    peek() { return this.q.length ? this.q[0] : null; } element() { if (!this.q.length) NSE(); return this.q[0]; }
    clear() { this.q.length = 0; this.mc++; } comparator() { return this.cmp; }
    iterator() { return new SnapIter(this); }
  }
  defLib(PriorityQueue, 'PriorityQueue', 'java.util.PriorityQueue', [L.Queue, L.Collection, L.Iterable]);

  /* ---------- Maps ---------- */
  const tableSizeFor = c => { let n = 1; while (n < c) n *= 2; return n; };
  const spread = h => (h ^ (h >>> 16));
  class AbstractMap extends JObject {
    constructor() { super(); this.mc = 0; }
    $mc() { return this.mc; }
    get(k) { const e = this.$find(k); return e ? e.v : null; }
    containsKey(k) { return !!this.$find(k); }
    containsValue(v) { return this.$entries().some(e => equals(v, e.v)); }
    getOrDefault(k, d) { const e = this.$find(k); return e ? e.v : d; }
    isEmpty() { return this.size() === 0; }
    putIfAbsent(k, v) { const e = this.$find(k); if (e && !isNullish(e.v)) return e.v; this.put(k, v); return null; }
    computeIfAbsent(k, f) { const e = this.$find(k); if (e && !isNullish(e.v)) return e.v; const mc = this.mc; const v = fnOf(f, 'apply')(k); if (this.mc !== mc) CME(); if (!isNullish(v)) this.$put(k, v, true); return v; }
    computeIfPresent(k, f) { const e = this.$find(k); if (e && !isNullish(e.v)) { const v = fnOf(f, 'apply')(k, e.v); if (isNullish(v)) this.remove(k); else e.v = v; return v; } return null; }
    compute(k, f) { const e = this.$find(k); const v = fnOf(f, 'apply')(k, e ? e.v : null); if (isNullish(v)) { if (e) this.remove(k); return null; } if (e) e.v = v; else this.$put(k, v, true); return v; }
    merge(k, v, f) { const e = this.$find(k); if (!e || isNullish(e.v)) { if (e) e.v = v; else this.$put(k, v, true); return v; } const nv = fnOf(f, 'apply')(e.v, v); if (isNullish(nv)) this.remove(k); else e.v = nv; return nv; }
    putAll(m) { for (const e of m.$entries().slice()) this.put(e.k, e.v); }
    forEach(f) { const g = fnOf(f, 'accept'); const e0 = this.mc; for (const e of this.$entries()) { g(e.k, e.v); if (this.mc !== e0) CME(); } }
    replaceAll(f) { const g = fnOf(f, 'apply'); for (const e of this.$entries()) e.v = g(e.k, e.v); }
    replace(k, v) { const e = this.$find(k); if (!e) return null; const o = e.v; e.v = v; return o; }
    keySet() { return new View(this, 0); } values() { return new View(this, 1); } entrySet() { return new View(this, 2); }
    sequencedKeySet() { return this.keySet(); }
    equals(o) { if (o === this) return true; if (!(o instanceof AbstractMap) || o.size() !== this.size()) return false; return this.$entries().every(e => { const f = o.$find(e.k); return f && equals(e.v, f.v); }); }
    hashCode() { return this.$entries().reduce((h, e) => (h + (hash(e.k) ^ hash(e.v))) | 0, 0); }
    toString() { return '{' + this.$entries().map(e => (e.k === this ? '(this Map)' : str(e.k)) + '=' + (e.v === this ? '(this Map)' : str(e.v))).join(', ') + '}'; }
    clone() { const c = new this.constructor(); c.$init(this); return c; }
    static of(...a) { const m = new ImmMap(); for (let i = 0; i < a.length; i += 2) { if (isNullish(a[i]) || isNullish(a[i + 1])) throw mk('NullPointerException'); if (m.$find(a[i])) throwEx('IllegalArgumentException', 'duplicate key: ' + str(a[i])); m.$put(a[i], a[i + 1]); } m.ro = true; return m; }
    static entry(k, v) { if (isNullish(k) || isNullish(v)) throw mk('NullPointerException'); const e = new Entry(k, v); e.ro = true; return e; }
    static ofEntries(...es) { const m = new ImmMap(); es.forEach(e => m.$put(e.k, e.v)); m.ro = true; return m; }
    static copyOf(o) { const m = new ImmMap(); o.$entries().forEach(e => m.$put(e.k, e.v)); m.ro = true; return m; }
  }
  defLib(AbstractMap, 'AbstractMap', 'java.util.AbstractMap', [L.Map]);
  class View extends Coll {
    constructor(m, kind) { super(); this.m = m; this.kind = kind; }
    $arr() { const es = this.m.$entries(); return this.kind === 0 ? es.map(e => e.k) : this.kind === 1 ? es.map(e => e.v) : es.slice(); }
    $mc() { return this.m.mc; } size() { return this.m.size(); }
    contains(o) { return this.kind === 0 ? this.m.containsKey(o) : this.kind === 1 ? this.m.containsValue(o) : super.contains(o); }
    remove(o) { if (this.kind === 0) { const had = this.m.containsKey(o); if (had) this.m.remove(o); return had; } if (this.kind === 1) { const e = this.m.$entries().find(x => equals(o, x.v)); if (e) { this.m.remove(e.k); return true; } return false; } if (o instanceof Entry && this.m.containsKey(o.k)) { this.m.remove(o.k); return true; } return false; }
    $removeEl(x) { if (this.kind === 0) this.m.remove(x); else if (this.kind === 1) { const e = this.m.$entries().find(y => y.v === x); if (e) this.m.remove(e.k); } else this.m.remove(x.k); }
    add() { UOE(); } clear() { this.m.clear(); }
    removeIf(p) { const f = fnOf(p, 'test'); let ch = false; for (const e of this.m.$entries().slice()) { const x = this.kind === 0 ? e.k : this.kind === 1 ? e.v : e; if (f(x)) { this.m.remove(e.k); ch = true; } } return ch; }
    equals(o) { if (this.kind === 1) return this === o; return o instanceof Coll && o.size() === this.size() && o.containsAll(this); }
    hashCode() { return super.hashCode(); }
    first() { return this.$arr()[0]; }
  }
  defLib(View, 'AbstractSet', 'java.util.AbstractSet', [L.Set, L.Collection, L.Iterable]);

  class HashMap extends AbstractMap {
    constructor() { super(); this.tab = null; this.n = 0; this.cap = 16; this.lf = 0.75; this.lfSet = false; }
    $init(a, b) {
      if (a instanceof AbstractMap) { const s = a.size(); if (s > 0) this.cap = tableSizeFor(Math.floor(s / this.lf + 1)); this.tab = null; this.putAll(a); }
      else if (a !== undefined) { const c = toNum(a); if (c < 0) throwEx('IllegalArgumentException', 'Illegal initial capacity: ' + c); this.cap = tableSizeFor(c); if (b !== undefined) this.lf = toNum(b); if (c === 0) this.cap = 1; }
      return this;
    }
    $alloc() { if (!this.tab) { this.tab = new Array(this.cap); } }
    $find(k) { if (!this.tab) return null; const h = spread(hash(k)); let b = this.tab[h & (this.cap - 1)]; if (!b) return null; for (let i = 0; i < b.length; i++) { const e = b[i]; if (e.h === h && (e.k === k || equals(k, e.k))) return e; } return null; }
    $put(k, v, head) {
      this.$alloc();
      const h = spread(hash(k)); const ix = h & (this.cap - 1);
      let b = this.tab[ix];
      if (b) for (let i = 0; i < b.length; i++) { const e = b[i]; if (e.h === h && (e.k === k || equals(k, e.k))) { const o = e.v; e.v = v; this.$access(e); return o; } }
      const e = new Entry(k, v, h);
      if (!b) this.tab[ix] = b = [];
      if (head) b.unshift(e); else b.push(e); this.mc++; this.$link(e);
      if (++this.n > this.cap * this.lf) this.$resize();
      this.$after();
      return null;
    }
    $after() {} $link() {} $unlink() {} $access() {}
    $resize() {
      const old = this.tab; this.cap *= 2; this.tab = new Array(this.cap);
      for (const b of old) if (b) for (const e of b) { const ix = e.h & (this.cap - 1); (this.tab[ix] || (this.tab[ix] = [])).push(e); }
    }
    put(k, v) { return this.$put(k, v); }
    get(k) { const e = this.$find(k); if (e) this.$access(e); return e ? e.v : null; }
    remove(k, v) {
      if (!this.tab) return arguments.length > 1 ? false : null;
      const h = spread(hash(k)); const b = this.tab[h & (this.cap - 1)]; if (!b) return arguments.length > 1 ? false : null;
      for (let i = 0; i < b.length; i++) { const e = b[i]; if (e.h === h && (e.k === k || equals(k, e.k))) { if (arguments.length > 1 && !equals(v, e.v)) return false; b.splice(i, 1); this.n--; this.mc++; this.$unlink(e); return arguments.length > 1 ? true : e.v; } }
      return arguments.length > 1 ? false : null;
    }
    $entries() { if (!this.tab) return []; const out = []; for (const b of this.tab) if (b) for (const e of b) out.push(e); return out; }
    size() { return this.n; }
    clear() { if (this.tab) { this.tab = new Array(this.cap); } this.n = 0; this.mc++; this.$clear(); }
    $clear() {}
  }
  defLib(HashMap, 'HashMap', 'java.util.HashMap', [L.Map, L.Cloneable]);
  class LinkedHashMap extends HashMap {
    constructor() { super(); this.head = null; this.tail = null; this.acc = false; }
    $init(a, b, c) { if (c === true) this.acc = true; if (a instanceof AbstractMap) { super.$init(a); } else if (a !== undefined) super.$init(a, b); return this; }
    $link(e) { e.bf = this.tail; e.af = null; if (this.tail) this.tail.af = e; else this.head = e; this.tail = e; }
    $unlink(e) { if (e.bf) e.bf.af = e.af; else this.head = e.af; if (e.af) e.af.bf = e.bf; else this.tail = e.bf; e.bf = e.af = null; }
    $access(e) { if (this.acc && this.tail !== e) { this.$unlink(e); this.$link(e); this.mc++; } }
    $after() { if (typeof this.removeEldestEntry === 'function' && this.head && this.removeEldestEntry(this.head)) this.remove(this.head.k); }
    $entries() { const out = []; for (let e = this.head; e; e = e.af) out.push(e); return out; }
    $clear() { this.head = this.tail = null; }
    sequencedKeySet() { return this.keySet(); } firstEntry() { return this.head; } lastEntry() { return this.tail; }
  }
  defLib(LinkedHashMap, 'LinkedHashMap', 'java.util.LinkedHashMap', [HashMap, L.Map]);
  class TreeMap extends AbstractMap {
    constructor() { super(); this.es = []; this.cmp = null; }
    $init(a) { if (a instanceof AbstractMap) { if (a instanceof TreeMap) this.cmp = a.cmp; this.putAll(a); } else if (a !== undefined && a !== null) this.cmp = cmpFn(a); return this; }
    c(a, b) { return this.cmp ? this.cmp(a, b) : compare(a, b); }
    $ix(k) { let lo = 0, hi = this.es.length - 1; while (lo <= hi) { const mid = (lo + hi) >>> 1, r = this.c(this.es[mid].k, k); if (r < 0) lo = mid + 1; else if (r > 0) hi = mid - 1; else return mid; } return -(lo + 1); }
    $find(k) { if (isNullish(k) && !this.cmp) throw mk('NullPointerException'); const i = this.$ix(k); return i >= 0 ? this.es[i] : null; }
    $put(k, v) { if (isNullish(k) && !this.cmp) throw mk('NullPointerException'); if (!this.es.length) this.c(k, k); const i = this.$ix(k); if (i >= 0) { const o = this.es[i].v; this.es[i].v = v; return o; } this.es.splice(-(i + 1), 0, new Entry(k, v)); this.mc++; return null; }
    put(k, v) { return this.$put(k, v); }
    remove(k) { const i = this.$ix(k); if (i < 0) return null; this.mc++; return this.es.splice(i, 1)[0].v; }
    $entries() { return this.es; } size() { return this.es.length; } clear() { this.es = []; this.mc++; }
    firstKey() { if (!this.es.length) NSE(); return this.es[0].k; } lastKey() { if (!this.es.length) NSE(); return this.es[this.es.length - 1].k; }
    firstEntry() { return this.es.length ? this.es[0] : null; } lastEntry() { return this.es.length ? this.es[this.es.length - 1] : null; }
    pollFirstEntry() { if (!this.es.length) return null; this.mc++; return this.es.shift(); } pollLastEntry() { if (!this.es.length) return null; this.mc++; return this.es.pop(); }
    $nav(k, kind) { let i = this.$ix(k); const n = this.es.length; let r; switch (kind) { case 'floor': r = i >= 0 ? i : -(i + 1) - 1; break; case 'ceiling': r = i >= 0 ? i : -(i + 1); break; case 'lower': r = (i >= 0 ? i : -(i + 1)) - 1; break; case 'higher': r = i >= 0 ? i + 1 : -(i + 1); break; } return r >= 0 && r < n ? this.es[r] : null; }
    floorEntry(k) { return this.$nav(k, 'floor'); } ceilingEntry(k) { return this.$nav(k, 'ceiling'); } lowerEntry(k) { return this.$nav(k, 'lower'); } higherEntry(k) { return this.$nav(k, 'higher'); }
    floorKey(k) { const e = this.$nav(k, 'floor'); return e ? e.k : null; } ceilingKey(k) { const e = this.$nav(k, 'ceiling'); return e ? e.k : null; }
    lowerKey(k) { const e = this.$nav(k, 'lower'); return e ? e.k : null; } higherKey(k) { const e = this.$nav(k, 'higher'); return e ? e.k : null; }
    headMap(k, inc) { return new SubMapView(this, null, false, k, inc === true); }
    tailMap(k, inc) { return new SubMapView(this, k, inc !== false, null, false, true); }
    subMap(a, b, c, d) { if (arguments.length === 2) return new SubMapView(this, a, true, b, false, true, true); return new SubMapView(this, a, b, c, d, true, true); }
    descendingMap() { const m = new LinkedHashMap(); this.es.slice().reverse().forEach(e => m.put(e.k, e.v)); return m; }
    descendingKeySet() { return new ImmSet(this.es.map(e => e.k).reverse()); } navigableKeySet() { return this.keySet(); }
    comparator() { return this.cmp; }
  }
  defLib(TreeMap, 'TreeMap', 'java.util.TreeMap', [L.NavigableMap, L.SortedMap, L.Map]);
  class SubMapView extends TreeMap {
    constructor(par, lo, loInc, hi, hiInc, hasLo, hasHi) {
      super(); this.par = par; this.cmp = par.cmp; this.lo = lo; this.loInc = loInc; this.hi = hi; this.hiInc = hiInc;
      this.hasLo = hasLo === true || (lo !== null && hasLo === undefined && hi === null); this.hasHi = hasHi === true || (hi !== null && !(hasLo === true && hasHi === undefined));
      if (hasLo === true && hasHi === undefined) this.hasHi = false;
      if (hasLo === undefined && lo === null) this.hasLo = false;
    }
    get es() { return this.par ? this.par.es.filter(e => this.inR(e.k)) : []; } set es(v) {}
    get mc() { return this.par ? this.par.mc : 0; } set mc(v) {}
    inR(k) {
      const c = this.par;
      if (this.hasLo) { const r = c.c(k, this.lo); if (r < 0 || (r === 0 && !this.loInc)) return false; }
      if (this.hasHi) { const r = c.c(k, this.hi); if (r > 0 || (r === 0 && !this.hiInc)) return false; }
      return true;
    }
    $find(k) { return this.inR(k) ? this.par.$find(k) : null; }
    $put(k, v) { if (!this.inR(k)) throwEx('IllegalArgumentException', 'key out of range'); return this.par.$put(k, v); }
    put(k, v) { return this.$put(k, v); }
    remove(k) { return this.inR(k) ? this.par.remove(k) : null; }
    clear() { for (const e of this.es.slice()) this.par.remove(e.k); }
    $ix(k) { const es = this.es; let lo = 0, hi = es.length - 1; while (lo <= hi) { const mid = (lo + hi) >>> 1, r = this.par.c(es[mid].k, k); if (r < 0) lo = mid + 1; else if (r > 0) hi = mid - 1; else return mid; } return -(lo + 1); }
    $nav(k, kind) { const es = this.es; const i = this.$ix(k); let r; switch (kind) { case 'floor': r = i >= 0 ? i : -(i + 1) - 1; break; case 'ceiling': r = i >= 0 ? i : -(i + 1); break; case 'lower': r = (i >= 0 ? i : -(i + 1)) - 1; break; case 'higher': r = i >= 0 ? i + 1 : -(i + 1); break; } return r >= 0 && r < es.length ? es[r] : null; }
    pollFirstEntry() { const e = this.es[0]; if (!e) return null; this.par.remove(e.k); return e; } pollLastEntry() { const es = this.es; const e = es[es.length - 1]; if (!e) return null; this.par.remove(e.k); return e; }
  }
  defLib(SubMapView, 'TreeMap.SubMap', 'java.util.TreeMap$NavigableSubMap', [L.NavigableMap, L.SortedMap, L.Map]);
  class ImmMap extends HashMap {
    constructor() { super(); this.ro = false; }
    put(k, v) { if (this.ro) UOE(); return super.put(k, v); } remove(k) { if (this.ro) UOE(); return super.remove(k); } clear() { if (this.ro) UOE(); super.clear(); }
    putIfAbsent() { UOE(); } computeIfAbsent() { UOE(); } compute() { UOE(); } merge() { UOE(); } putAll() { UOE(); } replaceAll() { UOE(); }
  }
  defLib(ImmMap, 'ImmutableCollections.MapN', 'java.util.ImmutableCollections$MapN', [L.Map]);
  const mkMap = (cls) => cls;
  L.Hashtable = class Hashtable extends HashMap { }; defLib(L.Hashtable, 'Hashtable', 'java.util.Hashtable', [L.Map]);
  L.ConcurrentHashMap = class ConcurrentHashMap extends HashMap { }; defLib(L.ConcurrentHashMap, 'ConcurrentHashMap', 'java.util.concurrent.ConcurrentHashMap', [L.Map]);
  L.EnumMap = class EnumMap extends TreeMap { $init(a) { if (a instanceof AbstractMap) this.putAll(a); return this; } c(a, b) { return a.ordinal() - b.ordinal(); } }; defLib(L.EnumMap, 'EnumMap', 'java.util.EnumMap', [L.Map]);
  L.Map.of = AbstractMap.of; L.Map.entry = AbstractMap.entry; L.Map.ofEntries = AbstractMap.ofEntries; L.Map.copyOf = AbstractMap.copyOf;
  L.Map.$defaults = {};

  /* ---------- Sets (backed by maps) ---------- */
  const PRESENT = true;
  class SetBase extends Coll {
    constructor() { super(); this.m = null; }
    $init(a, b) {
      this.m = this.$mk();
      if (a === undefined) return this;
      if (typeof a === 'number') { this.m.$init(a, b); return this; }
      if (a instanceof Coll) { if (this.m instanceof HashMap && !(this.m instanceof LinkedHashMap) || this.m instanceof LinkedHashMap) { this.m.cap = tableSizeFor(Math.max(Math.floor(a.size() / 0.75) + 1, 16)); } if (a instanceof TreeSet && this.m instanceof TreeMap) this.m.cmp = a.m.cmp; for (const x of a.$arr().slice()) this.add(x); return this; }
      if (this.m instanceof TreeMap) { this.m.cmp = cmpFn(a); return this; }
      for (const x of rt.$iter(a)) this.add(x);
      return this;
    }
    $mk() { return new HashMap(); }
    $arr() { return this.m.$entries().map(e => e.k); } $mc() { return this.m.mc; } size() { return this.m.size(); }
    add(x) { if (this.m.$find(x)) return false; this.m.put(x, PRESENT); return true; }
    contains(x) { return !!this.m.$find(x); }
    remove(x) { if (!this.m.$find(x)) return false; this.m.remove(x); return true; }
    clear() { this.m.clear(); }
    equals(o) { return o === this || (o instanceof Coll && o.constructor.$supers.has(L.Set) && o.size() === this.size() && this.$arr().every(x => o.contains(x))); }
    clone() { const c = new this.constructor(); c.$init(this); return c; }
  }
  class HashSet extends SetBase { }
  defLib(HashSet, 'HashSet', 'java.util.HashSet', [L.Set, L.Collection, L.Iterable, L.Cloneable]);
  class LinkedHashSet extends SetBase { $mk() { return new LinkedHashMap(); } getFirst() { if (!this.size()) NSE(); return this.$arr()[0]; } getLast() { if (!this.size()) NSE(); return this.$arr()[this.size() - 1]; } }
  defLib(LinkedHashSet, 'LinkedHashSet', 'java.util.LinkedHashSet', [HashSet, L.Set]);
  class TreeSet extends SetBase {
    $mk() { return new TreeMap(); }
    first() { return this.m.firstKey(); } last() { return this.m.lastKey(); }
    floor(x) { return this.m.floorKey(x); } ceiling(x) { return this.m.ceilingKey(x); } lower(x) { return this.m.lowerKey(x); } higher(x) { return this.m.higherKey(x); }
    pollFirst() { const e = this.m.pollFirstEntry(); return e ? e.k : null; } pollLast() { const e = this.m.pollLastEntry(); return e ? e.k : null; }
    $view(m) { const s = new TreeSet(); s.m = m; return s; }
    headSet(k, inc) { return this.$view(this.m.headMap(k, inc)); }
    tailSet(k, inc) { return this.$view(this.m.tailMap(k, inc)); }
    subSet(a, b, c, d) { return this.$view(arguments.length === 2 ? this.m.subMap(a, b) : this.m.subMap(a, b, c, d)); }
    descendingSet() { const s = new LinkedHashSet().$init(); this.$arr().slice().reverse().forEach(x => s.add(x)); return s; }
    descendingIterator() { return this.descendingSet().iterator(); } comparator() { return this.m.cmp; }
  }
  defLib(TreeSet, 'TreeSet', 'java.util.TreeSet', [L.NavigableSet, L.SortedSet, L.Set, L.Collection, L.Iterable]);
  class ImmSet extends SetBase {
    constructor(items) { super(); this.m = new LinkedHashMap(); (items || []).forEach(x => this.m.put(x, PRESENT)); this.ro = true; }
    add() { UOE(); } remove() { UOE(); } clear() { UOE(); } addAll() { UOE(); } removeIf() { UOE(); } removeAll() { UOE(); } retainAll() { UOE(); }
  }
  defLib(ImmSet, 'ImmutableCollections.SetN', 'java.util.ImmutableCollections$SetN', [L.Set, L.Collection, L.Iterable]);
  L.Set.of = (...a) => { const s = new ImmSet(); a.forEach(x => { if (isNullish(x)) throw mk('NullPointerException'); if (s.m.$find(x)) throwEx('IllegalArgumentException', 'duplicate element: ' + str(x)); s.m.$put(x, PRESENT); }); return s; };
  L.Set.copyOf = c => new ImmSet(Array.from(new Set(Array.from(rt.$iter(c)))));
  L.ConcurrentLinkedQueue = class ConcurrentLinkedQueue extends ArrayDeque { }; defLib(L.ConcurrentLinkedQueue, 'ConcurrentLinkedQueue', 'java.util.concurrent.ConcurrentLinkedQueue', [L.Queue, L.Collection, L.Iterable]);
  L.CopyOnWriteArrayList = class CopyOnWriteArrayList extends ArrayList { }; defLib(L.CopyOnWriteArrayList, 'CopyOnWriteArrayList', 'java.util.concurrent.CopyOnWriteArrayList', [L.List, L.Collection, L.Iterable]);
  L.EnumSet = class EnumSet extends TreeSet { static noneOf() { const s = new TreeSet(); s.m = new TreeMap(); s.m.c = (a, b) => a.ordinal() - b.ordinal(); return s; } static allOf(E) { const s = L.EnumSet.noneOf(); E.values().forEach(x => s.add(x)); return s; } static of(...a) { const s = L.EnumSet.noneOf(); a.forEach(x => s.add(x)); return s; } static complementOf(o) { const s = L.EnumSet.noneOf(); const all = o.$arr().length ? o.$arr()[0].constructor.$vals || [] : []; all.forEach(x => { if (!o.contains(x)) s.add(x); }); return s; } static range(a, b) { const s = L.EnumSet.noneOf(); a.constructor.values().filter(x => x.ordinal() >= a.ordinal() && x.ordinal() <= b.ordinal()).forEach(x => s.add(x)); return s; } };
  defLib(L.EnumSet, 'EnumSet', 'java.util.EnumSet', [L.Set]);

  /* ---------- Collections / Arrays ---------- */
  class Collections extends JObject { }
  Object.assign(Collections, {
    sort: (l, c) => l.sort(c), reverse: l => { const r = l.$arr().slice().reverse(); r.forEach((x, i) => l.set(i, x)); }, swap: (l, i, j) => { const t = l.get(i); l.set(i, l.get(j)); l.set(j, t); },
    shuffle: (l, r) => { r = r || new L.Random().$init(); for (let i = l.size(); i > 1; i--) { const j = r.nextInt(i); const t = l.get(i - 1); l.set(i - 1, l.get(j)); l.set(j, t); } },
    max: (c, cmp) => { const a = Array.from(rt.$iter(c)); if (!a.length) NSE(); const f = cmpFn(cmp); return a.reduce((m, x) => f(x, m) > 0 ? x : m); },
    min: (c, cmp) => { const a = Array.from(rt.$iter(c)); if (!a.length) NSE(); const f = cmpFn(cmp); return a.reduce((m, x) => f(x, m) < 0 ? x : m); },
    unmodifiableList: l => { const r = new ImmList(); r.a = l.a; r.ro = 2; return r; }, unmodifiableCollection: l => l.constructor.$supers.has(L.List) ? Collections.unmodifiableList(l) : new ImmSet(l.$arr()), unmodifiableSet: s => new ImmSet(s.$arr()),
    unmodifiableMap: m => { const r = new ImmMap(); m.$entries().forEach(e => r.$put(e.k, e.v)); r.ro = true; return r; }, synchronizedList: l => l, synchronizedMap: m => m, synchronizedSet: s => s,
    emptyList: () => { const l = imm([]); l.$oob = i => throwEx('IndexOutOfBoundsException', `Index: ${i}`); return l; }, emptySet: () => new ImmSet([]), emptyMap: () => { const m = new ImmMap(); m.ro = true; return m; }, singletonList: x => imm([x]), singleton: x => new ImmSet([x]), singletonMap: (k, v) => { const m = new ImmMap(); m.$put(k, v); m.ro = true; return m; },
    nCopies: (n, x) => imm(new Array(toNum(n)).fill(x)), frequency: (c, o) => Array.from(rt.$iter(c)).filter(x => equals(o, x)).length,
    reverseOrder: c => c ? rt.$lam((a, b) => cmpFn(c)(b, a)) : rt.$lam((a, b) => compare(b, a)),
    binarySearch: (l, k, c) => { const f = cmpFn(c); let lo = 0, hi = l.size() - 1; while (lo <= hi) { const mid = (lo + hi) >>> 1, r = f(l.a[mid], k); if (r < 0) lo = mid + 1; else if (r > 0) hi = mid - 1; else return mid; } return -(lo + 1); },
    addAll: (c, ...xs) => { if (xs.length === 1 && Array.isArray(xs[0]) && !xs[0].$t?.match(/^(int|double|char|long|boolean|byte|short|float)$/)) xs = xs[0]; let ch = false; xs.forEach(x => { ch = c.add(x) || ch; }); return ch; },
    fill: (l, x) => { for (let i = 0; i < l.size(); i++) l.set(i, x); }, copy: (dst, src) => { if (src.size() > dst.size()) throwEx('IndexOutOfBoundsException', 'Source does not fit in dest'); src.$arr().forEach((x, i) => dst.set(i, x)); },
    replaceAll: (l, o, n) => { let ch = false; for (let i = 0; i < l.size(); i++) if (equals(l.get(i), o)) { l.set(i, n); ch = true; } return ch; },
    indexOfSubList: (l, t) => { const a = l.$arr(), b = t.$arr(); outer: for (let i = 0; i + b.length <= a.length; i++) { for (let j = 0; j < b.length; j++) if (!equals(a[i + j], b[j])) continue outer; return i; } return -1; },
    lastIndexOfSubList: (l, t) => { const a = l.$arr(), b = t.$arr(); outer: for (let i = a.length - b.length; i >= 0; i--) { for (let j = 0; j < b.length; j++) if (!equals(a[i + j], b[j])) continue outer; return i; } return -1; }, disjoint: (a, b) => !Array.from(rt.$iter(a)).some(x => b.contains(x)), emptyIterator: () => new SnapIter(imm([])),
    rotate: (l, d) => { const n = l.a.length; if (!n) return; d = ((d % n) + n) % n; l.a.unshift(...l.a.splice(n - d, d)); }, list: e => new ArrayList().$init(e),
  });
  defLib(Collections, 'Collections', 'java.util.Collections');
  const typeOfElems = a => a.$t;
  const sortArr = (a, from, to, c) => {
    if (a === null) throw mk('NullPointerException');
    if (typeof from !== 'number' || from === undefined) { c = from; from = 0; to = a.length; }
    else if (to === undefined) { to = a.length; }
    if (typeof to !== 'number') { c = to; to = a.length; }
    if (from > to) throwEx('IllegalArgumentException', `fromIndex(${from}) > toIndex(${to})`);
    if (from < 0 || to > a.length) throwEx('ArrayIndexOutOfBoundsException', `Array index out of range: ${from < 0 ? from : to}`);
    const part = a.slice(from, to).sort(cmpFn(c));
    for (let i = 0; i < part.length; i++) a[from + i] = part[i];
  };
  const fill0 = t => (t === 'int' || t === 'short' || t === 'byte') ? 0 : t === 'long' ? 0n : t === 'boolean' ? false : t === 'double' ? new D(0) : t === 'float' ? new D(0, true) : t === 'char' ? Ch.of(0) : null;
  class Arrays extends JObject { }
  Object.assign(Arrays, {
    toString: a => a === null || a === undefined ? 'null' : '[' + Array.from(a, str).join(', ') + ']',
    deepToString: a => a === null ? 'null' : '[' + Array.from(a, x => Array.isArray(x) ? Arrays.deepToString(x) : str(x)).join(', ') + ']',
    sort: sortArr, parallelSort: sortArr,
    fill: (a, ...r) => { if (r.length === 1) { const t = a.$t, co = rt.$co[t]; const v = co ? co(r[0]) : r[0]; a.fill(v); } else { const t = a.$t, co = rt.$co[t]; const v = co ? co(r[2]) : r[2]; if (r[0] < 0 || r[1] > a.length) throwEx('ArrayIndexOutOfBoundsException', `Array index out of range: ${r[1]}`); a.fill(v, r[0], r[1]); } },
    asList: (...xs) => { let a; if (xs.length === 1 && Array.isArray(xs[0]) && !/^(int|double|char|long|boolean|byte|short|float)$/.test(xs[0].$t || '')) a = xs[0]; else a = xs; const l = new FixedList(); l.a = a; l.ro = 1; return l; },
    copyOf: (a, n) => { n = toNum(n); if (n < 0) throwEx('NegativeArraySizeException', String(n)); const r = a.slice(0, n); const z = fill0(a.$t); while (r.length < n) r.push(z); r.$t = a.$t; return r; },
    copyOfRange: (a, from, to) => { if (from > to) throwEx('IllegalArgumentException', `${from} > ${to}`); if (from < 0) throwEx('ArrayIndexOutOfBoundsException', `arraycopy: source index ${from} out of bounds for ${a.$t || 'object'}[${a.length}]`); const cl = Math.min(a.length - from, to - from); if (cl < 0) throwEx('ArrayIndexOutOfBoundsException', `arraycopy: length ${cl} is negative`); const r = a.slice(from, to); const z = fill0(a.$t); while (r.length < to - from) r.push(z); r.$t = a.$t; return r; },
    equals: (a, b) => a === b || (a !== null && b !== null && a.length === b.length && a.every((x, i) => equals(x, b[i]))),
    deepEquals: (a, b) => a === b || (a !== null && b !== null && a.length === b.length && a.every((x, i) => Array.isArray(x) ? Arrays.deepEquals(x, b[i]) : equals(x, b[i]))),
    hashCode: a => a === null ? 0 : a.reduce((h, x) => (Math.imul(31, h) + hash(x)) | 0, 1), deepHashCode: a => a === null ? 0 : a.reduce((h, x) => (Math.imul(31, h) + (Array.isArray(x) ? Arrays.deepHashCode(x) : hash(x))) | 0, 1),
    stream: (a, from, to) => { const t = a.$t; const items = from === undefined ? a.slice() : a.slice(from, to); return Stream.fromArray(items, t === 'int' || t === 'short' || t === 'byte' || t === 'char' ? 'int' : t === 'double' || t === 'float' ? 'double' : t === 'long' ? 'long' : 'obj'); },
    binarySearch: (a, ...r) => { let from = 0, to = a.length, k, c; if (r.length >= 3 && typeof r[0] === 'number' && typeof r[1] === 'number') { [from, to, k, c] = r; } else { [k, c] = r; } const f = cmpFn(c); let lo = from, hi = to - 1; while (lo <= hi) { const mid = (lo + hi) >>> 1, x = f(a[mid], k); if (x < 0) lo = mid + 1; else if (x > 0) hi = mid - 1; else return mid; } return -(lo + 1); },
    compare: (a, b) => { if (a === b) return 0; if (a === null) return -1; if (b === null) return 1; const n = Math.min(a.length, b.length); for (let i = 0; i < n; i++) { const c = compare(a[i], b[i]); if (c) return c; } return a.length - b.length; },
    mismatch: (a, b) => { const n = Math.min(a.length, b.length); for (let i = 0; i < n; i++) if (!equals(a[i], b[i])) return i; return a.length === b.length ? -1 : n; },
    setAll: (a, f) => { const g = fnOf(f); for (let i = 0; i < a.length; i++) a[i] = g(i); },
  });
  defLib(Arrays, 'Arrays', 'java.util.Arrays');

  /* ---------- Streams (lazy, generator based) ---------- */
  class Stream extends JObject {
    constructor(gen, kind) { super(); this.gen = gen; this.kind = kind || 'obj'; this.used = false; }
    static fromArray(a, kind) { return new Stream(function* () { yield* a; }, kind); }
    static of(...xs) { if (xs.length === 1 && Array.isArray(xs[0]) && !/^(int|double|char|long|boolean|byte|short|float)$/.test(xs[0].$t || '')) xs = xs[0]; return Stream.fromArray(xs); }
    static empty() { return Stream.fromArray([]); }
    static iterate(seed, a, b) { return new Stream(function* () { let x = seed; if (b === undefined) { const f = fnOf(a); for (;;) { yield x; x = f(x); } } else { const p = fnOf(a), f = fnOf(b); while (p(x)) { yield x; x = f(x); } } }); }
    static generate(s) { const f = fnOf(s); return new Stream(function* () { for (;;) yield f(); }); }
    static concat(a, b) { return new Stream(function* () { yield* a.gen(); yield* b.gen(); }, a.kind); }
    $use() { if (this.used) throwEx('IllegalStateException', 'stream has already been operated upon or closed'); this.used = true; return this.gen(); }
    $next(gen, kind) { return new Stream(gen, kind || this.kind); }
    filter(p) { const f = fnOf(p, 'test'), src = this; return this.$next(function* () { for (const x of src.$use()) if (f(x)) yield x; }); }
    map(g) { const f = fnOf(g, 'apply'), src = this, k = this.kind; return this.$next(function* () { for (const x of src.$use()) yield k === 'int' ? coerceKind(f(x), 'int') : k === 'double' ? rt.$toDouble(f(x)) : k === 'long' ? rt.$toLong(f(x)) : f(x); }); }
    mapToInt(g) { const f = fnOf(g, 'applyAsInt'), src = this; return this.$next(function* () { for (const x of src.$use()) yield coerceKind(f(x), 'int'); }, 'int'); }
    mapToLong(g) { const f = fnOf(g), src = this; return this.$next(function* () { for (const x of src.$use()) yield rt.$toLong(f(x)); }, 'long'); }
    mapToDouble(g) { const f = fnOf(g), src = this; return this.$next(function* () { for (const x of src.$use()) yield rt.$toDouble(f(x)); }, 'double'); }
    mapToObj(g) { const f = fnOf(g), src = this; return this.$next(function* () { for (const x of src.$use()) yield f(x); }, 'obj'); }
    boxed() { const src = this; return this.$next(function* () { yield* src.$use(); }, 'obj'); }
    asDoubleStream() { const src = this; return this.$next(function* () { for (const x of src.$use()) yield rt.$toDouble(x); }, 'double'); }
    asLongStream() { const src = this; return this.$next(function* () { for (const x of src.$use()) yield rt.$toLong(x); }, 'long'); }
    flatMap(g) { const f = fnOf(g), src = this; return this.$next(function* () { for (const x of src.$use()) yield* f(x).$use(); }, 'obj'); }
    distinct() { const src = this; return this.$next(function* () { const seen = new HashSet().$init(); for (const x of src.$use()) if (seen.add(x)) yield x; }); }
    sorted(c) { const src = this; return this.$next(function* () { const a = Array.from(src.$use()); a.sort(cmpFn(c)); yield* a; }); }
    limit(n) { n = Number(n); const src = this; return this.$next(function* () { if (n <= 0) return; let i = 0; for (const x of src.$use()) { yield x; if (++i >= n) return; } }); }
    skip(n) { n = Number(n); const src = this; return this.$next(function* () { let i = 0; for (const x of src.$use()) if (i++ >= n) yield x; }); }
    peek(g) { const f = fnOf(g, 'accept'), src = this; return this.$next(function* () { for (const x of src.$use()) { f(x); yield x; } }); }
    takeWhile(p) { const f = fnOf(p), src = this; return this.$next(function* () { for (const x of src.$use()) { if (!f(x)) return; yield x; } }); }
    dropWhile(p) { const f = fnOf(p), src = this; return this.$next(function* () { let d = true; for (const x of src.$use()) { if (d && f(x)) continue; d = false; yield x; } }); }
    parallel() { return this; } sequential() { return this; } unordered() { return this; } onClose() { return this; } close() {}
    forEach(g) { const f = fnOf(g, 'accept'); for (const x of this.$use()) f(x); } forEachOrdered(g) { this.forEach(g); }
    collect(c, acc, comb) { if (acc !== undefined) { const r = fnOf(c)(); const f = fnOf(acc); for (const x of this.$use()) f(r, x); return r; } return c.run(this.$use()); }
    toList() { return imm(Array.from(this.$use())); }
    toArray() { const a = Array.from(this.$use()); a.$t = this.kind === 'obj' ? 'Object' : this.kind; return a; }
    count() { let n = 0n; for (const _ of this.$use()) n++; return n; }
    sum() { const k = this.kind; if (k === 'double') { let s = 0; for (const x of this.$use()) s += x.v; return new D(s); } if (k === 'long') { let s = 0n; for (const x of this.$use()) s = BigInt.asIntN(64, s + x); return s; } let s = 0; for (const x of this.$use()) s = (s + toNum(x)) | 0; return s; }
    average() { let s = 0, n = 0; for (const x of this.$use()) { s += toNum(x); n++; } return n ? mkOpt(new D(s / n), 'Double') : mkOpt(null, 'Double'); }
    $ext(c, pick) { const k = this.kind === 'int' ? 'Int' : this.kind === 'double' ? 'Double' : this.kind === 'long' ? 'Long' : ''; const f = cmpFn(c); let best, has = false; for (const x of this.$use()) { if (!has || pick(f(x, best))) { best = x; has = true; } } return has ? mkOpt(best, k) : mkOpt(null, k); }
    max(c) { return this.$ext(c, r => r > 0); } min(c) { return this.$ext(c, r => r < 0); }
    reduce(a, b, c) {
      if (b === undefined) { const f = fnOf(a); const k = this.kind === 'int' ? 'Int' : this.kind === 'double' ? 'Double' : this.kind === 'long' ? 'Long' : ''; let acc, has = false; for (const x of this.$use()) { if (!has) { acc = x; has = true; } else acc = f(acc, x); } return mkOpt(has ? acc : null, k); }
      const f = fnOf(b); let acc = a; for (const x of this.$use()) acc = f(acc, x); return acc;
    }
    anyMatch(p) { const f = fnOf(p); for (const x of this.$use()) if (f(x)) return true; return false; }
    allMatch(p) { const f = fnOf(p); for (const x of this.$use()) if (!f(x)) return false; return true; }
    noneMatch(p) { const f = fnOf(p); for (const x of this.$use()) if (f(x)) return false; return true; }
    findFirst() { for (const x of this.$use()) return mkOpt(x, this.kind === 'int' ? 'Int' : this.kind === 'double' ? 'Double' : this.kind === 'long' ? 'Long' : ''); return mkOpt(null, this.kind === 'int' ? 'Int' : this.kind === 'double' ? 'Double' : this.kind === 'long' ? 'Long' : ''); }
    findAny() { return this.findFirst(); }
    iterator() { return new SnapIter({ $arr: () => Array.from(this.$use()), $mc: () => 0, $removeEl() { } }); }
    summaryStatistics() { const a = Array.from(this.$use()); return new Stats(a, this.kind); }
    getAsInt() { return this; }
    mapMulti() { UOE(); }
  }
  defLib(Stream, 'Stream', 'java.util.stream.Stream');
  const coerceKind = (v, k) => k === 'int' ? (v instanceof Ch ? v.c : v) : v;
  class Stats extends JObject {
    constructor(a, k) { super(); this.a = a.map(toNum); this.k = k; }
    getMin() { if (!this.a.length) return this.k === 'double' ? new D(Infinity) : this.k === 'long' ? 9223372036854775807n : 2147483647; const m = Math.min(...this.a); return this.k === 'double' ? new D(m) : this.k === 'long' ? BigInt(m) : m; }
    getMax() { if (!this.a.length) return this.k === 'double' ? new D(-Infinity) : this.k === 'long' ? -9223372036854775808n : -2147483648; const m = Math.max(...this.a); return this.k === 'double' ? new D(m) : this.k === 'long' ? BigInt(m) : m; }
    getSum() { const s = this.a.reduce((x, y) => x + y, 0); return this.k === 'double' ? new D(s) : BigInt(s); }
    getAverage() { return new D(this.a.length ? this.a.reduce((x, y) => x + y, 0) / this.a.length : 0); } getCount() { return BigInt(this.a.length); }
    toString() { const n = { int: 'IntSummaryStatistics', long: 'LongSummaryStatistics', double: 'DoubleSummaryStatistics' }[this.k]; const f = x => this.k === 'double' ? H.fmt('%f', [x]) : str(x); return `${n}{count=${this.a.length}, sum=${f(this.getSum())}, min=${f(this.getMin())}, average=${H.fmt('%f', [this.getAverage()])}, max=${f(this.getMax())}}`; }
  }
  defLib(Stats, 'IntSummaryStatistics', 'java.util.IntSummaryStatistics'); L.DoubleSummaryStatistics = L.LongSummaryStatistics = Stats;
  const mkStreamCls = (name, kind, extra) => { const c = ({ [name]: class extends Stream { } })[name]; defLib(c, name, 'java.util.stream.' + name); Object.assign(c, extra); return c; };
  L.IntStream = mkStreamCls('IntStream', 'int', {
    range: (a, b) => new Stream(function* () { for (let i = a; i < b; i++) yield i; }, 'int'), rangeClosed: (a, b) => new Stream(function* () { for (let i = a; i <= b; i++) yield i; }, 'int'),
    of: (...xs) => { if (xs.length === 1 && Array.isArray(xs[0])) xs = xs[0]; return Stream.fromArray(xs.map(x => x instanceof Ch ? x.c : x), 'int'); }, empty: () => Stream.fromArray([], 'int'),
    iterate: (s, a, b) => { const st = Stream.iterate(s, a, b); st.kind = 'int'; return st; }, generate: s => { const st = Stream.generate(s); st.kind = 'int'; return st; }, concat: (a, b) => Stream.concat(a, b),
  });
  L.LongStream = mkStreamCls('LongStream', 'long', { range: (a, b) => new Stream(function* () { for (let i = BigInt(a); i < BigInt(b); i++) yield i; }, 'long'), rangeClosed: (a, b) => new Stream(function* () { for (let i = BigInt(a); i <= BigInt(b); i++) yield i; }, 'long'), of: (...xs) => Stream.fromArray(xs.map(rt.$toLong), 'long') });
  L.DoubleStream = mkStreamCls('DoubleStream', 'double', { of: (...xs) => { if (xs.length === 1 && Array.isArray(xs[0])) xs = xs[0]; return Stream.fromArray(xs.map(rt.$toDouble), 'double'); } });
  L.Stream = Stream;
  L.StreamSupport = null;

  /* Collectors */
  class Collector extends JObject { constructor(run) { super(); this.run = run; } }
  defLib(Collector, 'Collector', 'java.util.stream.Collector');
  const col = (run) => new Collector(run);
  class Collectors extends JObject { }
  const toListC = col(it => new ArrayList().$init(Array.from(it)));
  Object.assign(Collectors, {
    toList: () => toListC, toUnmodifiableList: () => col(it => imm(Array.from(it), true)), toSet: () => col(it => new HashSet().$init(Array.from(it))), toUnmodifiableSet: () => col(it => new ImmSet(Array.from(it))),
    toCollection: s => col(it => { const c = fnOf(s)(); for (const x of it) c.add(x); return c; }),
    joining: (sep, pre, suf) => col(it => (pre === undefined ? '' : str(pre)) + Array.from(it, str).join(sep === undefined ? '' : str(sep)) + (suf === undefined ? '' : str(suf))),
    counting: () => col(it => { let n = 0n; for (const _ of it) n++; return n; }),
    summingInt: f => col(it => { const g = fnOf(f); let s = 0; for (const x of it) s = (s + toNum(g(x))) | 0; return s; }),
    summingLong: f => col(it => { const g = fnOf(f); let s = 0n; for (const x of it) s += BigInt(toNum(g(x))); return s; }),
    summingDouble: f => col(it => { const g = fnOf(f); let s = 0; for (const x of it) s += toNum(g(x)); return new D(s); }),
    averagingInt: f => col(it => { const g = fnOf(f); let s = 0, n = 0; for (const x of it) { s += toNum(g(x)); n++; } return new D(n ? s / n : 0); }),
    averagingDouble: f => Collectors.averagingInt(f), averagingLong: f => Collectors.averagingInt(f),
    mapping: (f, ds) => col(it => ds.run((function* () { const g = fnOf(f); for (const x of it) yield g(x); })())),
    filtering: (p, ds) => col(it => ds.run((function* () { const g = fnOf(p); for (const x of it) if (g(x)) yield x; })())),
    minBy: c => col(it => { const f = cmpFn(c); let b, has = false; for (const x of it) if (!has || f(x, b) < 0) { b = x; has = true; } return has ? Optional.of(b) : Optional.empty(); }),
    maxBy: c => col(it => { const f = cmpFn(c); let b, has = false; for (const x of it) if (!has || f(x, b) > 0) { b = x; has = true; } return has ? Optional.of(b) : Optional.empty(); }),
    reducing: (id, a, b) => col(it => { if (b === undefined) { const f = fnOf(a); let acc = id; for (const x of it) acc = f(acc, x); return acc; } const m = fnOf(a), f = fnOf(b); let acc = id; for (const x of it) acc = f(acc, m(x)); return acc; }),
    collectingAndThen: (ds, f) => col(it => fnOf(f)(ds.run(it))),
    groupingBy: (f, a, b) => col(it => {
      const kf = fnOf(f); let mapF = () => new HashMap().$init(), ds = toListC;
      if (b !== undefined) { mapF = fnOf(a); ds = b; } else if (a !== undefined) ds = a;
      const res = mapF();
      for (const x of it) {
        const k = kf(x); if (isNullish(k)) throw mk('NullPointerException', 'element cannot be mapped to a null key');
        let e = res.$find(k);
        if (!e) { res.$put(k, null, true); e = res.$find(k); e.$items = []; }
        e.$items.push(x);
      }
      for (const e of res.$entries()) if (e.$items) { e.v = ds.run(e.$items); delete e.$items; }
      return res;
    }),
    partitioningBy: (p, ds) => col(it => { const f = fnOf(p); const t = [], fl = []; for (const x of it) (f(x) ? t : fl).push(x); const m = new LinkedHashMap().$init(); const d = ds || toListC; m.put(false, d.run(fl)); m.put(true, d.run(t)); return m; }),
    toMap: (kf, vf, mf, sup) => col(it => {
      const k = fnOf(kf), v = fnOf(vf); const m = sup ? fnOf(sup)() : new HashMap().$init();
      for (const x of it) { const key = k(x), val = v(x); if (mf) m.merge(key, val, mf); else { if (m.containsKey(key)) throwEx('IllegalStateException', `Duplicate key ${str(key)} (attempted merging values ${str(m.get(key))} and ${str(val)})`); m.put(key, val); } }
      return m;
    }),
    summarizingInt: f => col(it => { const g = fnOf(f); return new Stats(Array.from(it, g), 'int'); }), summarizingDouble: f => col(it => { const g = fnOf(f); return new Stats(Array.from(it, g), 'double'); }), summarizingLong: f => col(it => { const g = fnOf(f); return new Stats(Array.from(it, g), 'long'); }),
  });
  defLib(Collectors, 'Collectors', 'java.util.stream.Collectors');
  L.Collection.stream = null;

  class Spliterator extends JObject { constructor(it) { super(); this.it = it; } }
  defLib(Spliterator, 'Spliterator', 'java.util.Spliterator');
  class StreamSupport extends JObject { static stream(sp) { return Stream.fromArray(Array.from(rt.$iter(sp.it))); } }
  defLib(StreamSupport, 'StreamSupport', 'java.util.stream.StreamSupport');
  L.Iterable.$defaults.spliterator = function () { return new Spliterator(this); };
  /* ---------- BigInteger ---------- */
  class BigInteger extends JObject {
    constructor(v) { super(); this.v = v === undefined ? 0n : v; this.$big = true; }
    $init(s, r) { if (typeof s === 'string' || s instanceof String) { const t = str(s); if (!/^[+-]?\d+$/.test(t)) throwEx('NumberFormatException', `For input string: "${t}"`); this.v = BigInt(t); } return this; }
    static valueOf(x) { return new BigInteger(BigInt(x)); }
    add(o) { return new BigInteger(this.v + o.v); } subtract(o) { return new BigInteger(this.v - o.v); } multiply(o) { return new BigInteger(this.v * o.v); }
    divide(o) { if (o.v === 0n) throwEx('ArithmeticException', 'BigInteger divide by zero'); return new BigInteger(this.v / o.v); }
    remainder(o) { if (o.v === 0n) throwEx('ArithmeticException', 'BigInteger divide by zero'); return new BigInteger(this.v % o.v); }
    mod(o) { if (o.v <= 0n) throwEx('ArithmeticException', 'BigInteger: modulus not positive'); return new BigInteger(((this.v % o.v) + o.v) % o.v); }
    pow(n) { return new BigInteger(this.v ** BigInt(n)); } negate() { return new BigInteger(-this.v); } abs() { return new BigInteger(this.v < 0n ? -this.v : this.v); } signum() { return this.v > 0n ? 1 : this.v < 0n ? -1 : 0; }
    gcd(o) { let a = this.v < 0n ? -this.v : this.v, b = o.v < 0n ? -o.v : o.v; while (b) [a, b] = [b, a % b]; return new BigInteger(a); }
    modPow(e, m) { let r = 1n, b = ((this.v % m.v) + m.v) % m.v, x = e.v; while (x > 0n) { if (x & 1n) r = r * b % m.v; b = b * b % m.v; x >>= 1n; } return new BigInteger(r); }
    compareTo(o) { return this.v < o.v ? -1 : this.v > o.v ? 1 : 0; } equals(o) { return o instanceof BigInteger && o.v === this.v; } hashCode() { return hash(this.v); }
    max(o) { return this.v >= o.v ? this : o; } min(o) { return this.v <= o.v ? this : o; } sqrt() { let x = BigInt(Math.floor(Math.sqrt(Number(this.v)))); while (x * x > this.v) x--; while ((x + 1n) * (x + 1n) <= this.v) x++; return new BigInteger(x); }
    intValue() { return Number(BigInt.asIntN(32, this.v)); } longValue() { return BigInt.asIntN(64, this.v); } doubleValue() { return new D(Number(this.v)); } intValueExact() { if (this.v > 2147483647n || this.v < -2147483648n) throwEx('ArithmeticException', 'BigInteger out of int range'); return Number(this.v); }
    isProbablePrime() { const n = this.v; if (n < 2n) return false; for (let i = 2n; i * i <= n && i < 100000n; i++) if (n % i === 0n) return false; return true; }
    toString(r) { return r === undefined ? this.v.toString() : this.v.toString(r); } testBit(n) { return ((this.v >> BigInt(n)) & 1n) === 1n; } bitLength() { return this.v.toString(2).replace('-', '').length; }
    divideAndRemainder(o) { if (o.v === 0n) throwEx('ArithmeticException', 'BigInteger divide by zero'); return arr('BigInteger', [new BigInteger(this.v / o.v), new BigInteger(this.v % o.v)]); }
    and(o) { return new BigInteger(this.v & o.v); } or(o) { return new BigInteger(this.v | o.v); } xor(o) { return new BigInteger(this.v ^ o.v); } not() { return new BigInteger(~this.v); } andNot(o) { return new BigInteger(this.v & ~o.v); } setBit(n) { return new BigInteger(this.v | (1n << BigInt(n))); } clearBit(n) { return new BigInteger(this.v & ~(1n << BigInt(n))); } flipBit(n) { return new BigInteger(this.v ^ (1n << BigInt(n))); } bitCount() { return this.v.toString(2).replace(/[-0]/g, '').length; } modInverse(m) { let [a, b, x0, x1] = [((this.v % m.v) + m.v) % m.v, m.v, 0n, 1n]; while (a > 1n) { const q = a / b; [a, b] = [b, a % b]; [x0, x1] = [x1 - q * x0, x0]; } return new BigInteger(((x1 % m.v) + m.v) % m.v); } longValueExact() { return this.v; }
    shiftLeft(n) { return new BigInteger(this.v << BigInt(n)); } shiftRight(n) { return new BigInteger(this.v >> BigInt(n)); }
  }
  defLib(BigInteger, 'BigInteger', 'java.math.BigInteger', [L.Number, L.Comparable]);
  BigInteger.ZERO = new BigInteger(0n); BigInteger.ONE = new BigInteger(1n); BigInteger.TWO = new BigInteger(2n); BigInteger.TEN = new BigInteger(10n);

  /* ---------- BigDecimal / RoundingMode ---------- */
  class RoundingMode extends JObject { name() { return this.$ename; } ordinal() { return this.$ord; } toString() { return this.$ename; } compareTo(o) { return this.$ord - o.$ord; } }
  defLib(RoundingMode, 'RoundingMode', 'java.math.RoundingMode');
  const RMODES = ['UP', 'DOWN', 'CEILING', 'FLOOR', 'HALF_UP', 'HALF_DOWN', 'HALF_EVEN', 'UNNECESSARY'];
  RoundingMode.$vals = RMODES.map((n, i) => { const m = new RoundingMode(); m.$ename = n; m.$ord = i; RoundingMode[n] = m; return m; });
  RoundingMode.values = () => RoundingMode.$vals.slice(); RoundingMode.valueOf = n => RoundingMode.$vals.find(x => x.$ename === n);
  const mname = m => (typeof m === 'number' ? RMODES[m] : m && m.$ename) || 'HALF_UP';
  const pow10 = n => 10n ** BigInt(n);
  // divide num/den (BigInt) rounding according to mode
  const roundDiv = (num, den, mode) => {
    if (den === 0n) throwEx('ArithmeticException', num === 0n ? 'Division undefined' : 'Division by zero');
    let q = num / den; const r = num % den; if (r === 0n) return q;
    const neg = (num < 0n) !== (den < 0n); const twice = (r < 0n ? -r : r) * 2n; const ad = den < 0n ? -den : den;
    let up;
    switch (mname(mode)) {
      case 'UP': up = true; break; case 'DOWN': up = false; break; case 'CEILING': up = !neg; break; case 'FLOOR': up = neg; break;
      case 'HALF_UP': up = twice >= ad; break; case 'HALF_DOWN': up = twice > ad; break;
      case 'HALF_EVEN': up = twice > ad || (twice === ad && (q % 2n !== 0n)); break;
      default: throwEx('ArithmeticException', 'Rounding necessary');
    }
    return up ? q + (neg ? -1n : 1n) : q;
  };
  class BigDecimal extends JObject {
    constructor(u, sc) { super(); this.u = u === undefined ? 0n : u; this.sc = sc || 0; this.$big = true; }
    $init(x, a) {
      if (typeof x === 'string' || x instanceof String) {
        const t = str(x).trim(); const m = /^([+-]?)(\d*)(?:\.(\d*))?(?:[eE]([+-]?\d+))?$/.exec(t);
        if (!m || (!m[2] && !m[3])) throwEx('NumberFormatException', `Character ${t[0] || ''} is neither a decimal digit number, decimal point, nor "e" notation exponential mark.`);
        const frac = m[3] || ''; this.u = BigInt((m[2] || '0') + frac) * (m[1] === '-' ? -1n : 1n); this.sc = frac.length - (m[4] ? parseInt(m[4], 10) : 0);
      } else if (x instanceof D) { // exact binary expansion of the double
        const v = x.v; if (!isFinite(v)) throwEx('NumberFormatException', 'Infinite or NaN');
        if (v === 0) { this.u = 0n; this.sc = 0; } else {
          f64[0] = v; const hi = u32[1], lo = u32[0]; const sign = hi >>> 31 ? -1n : 1n; const ex = (hi >>> 20) & 0x7ff; let mant = (BigInt(hi & 0xfffff) << 32n) | BigInt(lo);
          let e2; if (ex === 0) e2 = -1074; else { mant |= 1n << 52n; e2 = ex - 1075; }
          while (mant % 2n === 0n && e2 < 0) { mant /= 2n; e2++; }
          if (e2 >= 0) { this.u = sign * mant * (2n ** BigInt(e2)); this.sc = 0; } else { this.u = sign * mant * (5n ** BigInt(-e2)); this.sc = -e2; }
        }
      } else if (x instanceof BigInteger) { this.u = x.v; this.sc = a === undefined ? 0 : a; }
      else this.u = BigInt(x);
      return this;
    }
    static valueOf(x, sc) { if (x instanceof D) return new BigDecimal().$init(dstr(x.v)); if (sc !== undefined) return new BigDecimal(BigInt(x), sc); return new BigDecimal(BigInt(x), 0); }
    $align(o) { const sc = Math.max(this.sc, o.sc); return [this.u * pow10(sc - this.sc), o.u * pow10(sc - o.sc), sc]; }
    add(o) { const [a, b, sc] = this.$align(o); return new BigDecimal(a + b, sc); } subtract(o) { const [a, b, sc] = this.$align(o); return new BigDecimal(a - b, sc); }
    multiply(o) { return new BigDecimal(this.u * o.u, this.sc + o.sc); }
    negate() { return new BigDecimal(-this.u, this.sc); } abs() { return this.u < 0n ? this.negate() : this; } signum() { return this.u > 0n ? 1 : this.u < 0n ? -1 : 0; }
    scale() { return this.sc; } precision() { return this.u === 0n ? 1 : (this.u < 0n ? -this.u : this.u).toString().length; } unscaledValue() { return new BigInteger(this.u); }
    setScale(n, mode) { n = toNum(n); if (n >= this.sc) return new BigDecimal(this.u * pow10(n - this.sc), n); return new BigDecimal(roundDiv(this.u, pow10(this.sc - n), mode === undefined ? 'UNNECESSARY' : mode), n); }
    divide(o, a, b) {
      if (o.u === 0n) throwEx('ArithmeticException', this.u === 0n ? 'Division undefined' : 'Division by zero');
      if (a === undefined) { // exact quotient or ArithmeticException
        const pref = this.sc - o.sc; let num = this.u, den = o.u;
        for (let extra = 0; extra < 400; extra++) { const n2 = num * pow10(extra); if (n2 % den === 0n) { const sc = pref + extra; let r = new BigDecimal(n2 / den, sc); if (r.sc < pref) r = r.setScale(pref); return r; } }
        throwEx('ArithmeticException', 'Non-terminating decimal expansion; no exact representable decimal result.');
      }
      let scale, mode;
      if (b === undefined) { scale = this.sc; mode = a; } else { scale = toNum(a); mode = b; }
      if (a && a.$mc) return this.$divMc(o, a);
      // result = this / o with the requested scale
      const shift = scale - (this.sc - o.sc);
      const num = shift >= 0 ? this.u * pow10(shift) : this.u; const den = shift >= 0 ? o.u : o.u * pow10(-shift);
      return new BigDecimal(roundDiv(num, den, mode), scale);
    }
    $divMc(o, mc) { const prec = mc.prec; let scale = prec + 3 - (this.precision() - this.sc) + (o.precision() - o.sc); let q = this.divide(o, scale + 2, 'DOWN'); return q.round(mc); }
    round(mc) { const p = this.precision(); if (!mc.prec || p <= mc.prec) return this; const drop = p - mc.prec; const r = roundDiv(this.u, pow10(drop), mc.mode); const nb = new BigDecimal(r, this.sc - drop); return nb.precision() > mc.prec ? nb.round(mc) : nb; }
    remainder(o) { const [a, b, sc] = this.$align(o); if (b === 0n) throwEx('ArithmeticException', 'Division by zero'); return new BigDecimal(a % b, sc); }
    pow(n) { return new BigDecimal(this.u ** BigInt(n), this.sc * n); }
    compareTo(o) { const [a, b] = this.$align(o); return a < b ? -1 : a > b ? 1 : 0; }
    equals(o) { return o instanceof BigDecimal && o.u === this.u && o.sc === this.sc; } hashCode() { return (Math.imul(31, hash(new BigInteger(this.u))) + this.sc) | 0; }
    max(o) { return this.compareTo(o) >= 0 ? this : o; } min(o) { return this.compareTo(o) <= 0 ? this : o; }
    stripTrailingZeros() { if (this.u === 0n) return new BigDecimal(0n, 0); let u = this.u, sc = this.sc; while (u % 10n === 0n) { u /= 10n; sc--; } return new BigDecimal(u, sc); }
    movePointLeft(n) { return new BigDecimal(this.u, this.sc + n); } movePointRight(n) { return new BigDecimal(this.u, this.sc - n); }
    doubleValue() { return new D(parseFloat(this.toString())); } floatValue() { return new D(Math.fround(parseFloat(this.toString())), true); }
    intValue() { return Number(BigInt.asIntN(32, this.$trunc())); } longValue() { return BigInt.asIntN(64, this.$trunc()); } $trunc() { return this.sc <= 0 ? this.u * pow10(-this.sc) : this.u / pow10(this.sc); }
    intValueExact() { if (this.sc > 0 && this.u % pow10(this.sc) !== 0n) throwEx('ArithmeticException', 'Rounding necessary'); const v = this.$trunc(); if (v > 2147483647n || v < -2147483648n) throwEx('ArithmeticException', 'Overflow'); return Number(v); }
    toBigInteger() { return new BigInteger(this.$trunc()); }
    toPlainString() { if (this.sc <= 0) return (this.u * pow10(-this.sc)).toString(); const neg = this.u < 0n; let d = (neg ? -this.u : this.u).toString().padStart(this.sc + 1, '0'); return (neg ? '-' : '') + d.slice(0, d.length - this.sc) + '.' + d.slice(d.length - this.sc); }
    toString() {
      const neg = this.u < 0n; const d = (neg ? -this.u : this.u).toString(); const adj = -this.sc + (d.length - 1);
      if (this.sc >= 0 && adj >= -6) return this.toPlainString();
      let m = d.length > 1 ? d[0] + '.' + d.slice(1) : d; return (neg ? '-' : '') + m + 'E' + (adj >= 0 ? '+' : '') + adj;
    }
    toEngineeringString() { return this.toString(); }
  }
  defLib(BigDecimal, 'BigDecimal', 'java.math.BigDecimal', [L.Number, L.Comparable]);
  BigDecimal.ZERO = new BigDecimal(0n, 0); BigDecimal.ONE = new BigDecimal(1n, 0); BigDecimal.TWO = new BigDecimal(2n, 0); BigDecimal.TEN = new BigDecimal(10n, 0);
  BigDecimal.ROUND_HALF_UP = 4; BigDecimal.ROUND_DOWN = 1; BigDecimal.ROUND_UP = 0; BigDecimal.ROUND_FLOOR = 3; BigDecimal.ROUND_CEILING = 2; BigDecimal.ROUND_HALF_EVEN = 6; BigDecimal.ROUND_HALF_DOWN = 5;
  class MathContext extends JObject { $init(p, m) { this.prec = toNum(p); this.mode = m === undefined ? RoundingMode.HALF_UP : m; this.$mc = true; return this; } getPrecision() { return this.prec; } }
  defLib(MathContext, 'MathContext', 'java.math.MathContext');
  MathContext.DECIMAL32 = new MathContext().$init(7, RoundingMode.HALF_EVEN); MathContext.DECIMAL64 = new MathContext().$init(16, RoundingMode.HALF_EVEN); MathContext.DECIMAL128 = new MathContext().$init(34, RoundingMode.HALF_EVEN); MathContext.UNLIMITED = new MathContext().$init(0, RoundingMode.HALF_UP);
  /* ---------- regex ---------- */
  class Pattern extends JObject {
    static compile(re, flags) { const p = new Pattern(); p.re = str(re); p.fl = flags || 0; return p; }
    static matches(re, s) { return H.jregex('^(?:' + str(re) + ')$').test(str(s)); } static quote(s) { return str(s).replace(/[\\^$.|?*+()[\]{}]/g, '\\$&'); }
    matcher(s) { const m = new Matcher(); m.p = this; m.s = str(s); m.rx = H.jregex(this.re, 'g' + (this.fl & 2 ? 'i' : '')); m.last = null; m.pos = 0; return m; }
    pattern() { return this.re; } toString() { return this.re; } split(s) { return L.String.prototype.constructor === undefined ? null : rt.$inv(str(s), 'split', this.re); }
    asPredicate() { return rt.$lam(s => this.matcher(s).find()); }
  }
  Pattern.CASE_INSENSITIVE = 2;
  defLib(Pattern, 'Pattern', 'java.util.regex.Pattern');
  class Matcher extends JObject {
    matches() { const r = H.jregex('^(?:' + this.p.re + ')$', this.p.fl & 2 ? 'i' : ''); const m = r.exec(this.s); this.last = m; return !!m; }
    lookingAt() { const r = H.jregex('^(?:' + this.p.re + ')', this.p.fl & 2 ? 'i' : ''); this.last = r.exec(this.s); return !!this.last; }
    find(from) { if (from !== undefined) this.rx.lastIndex = from; const m = this.rx.exec(this.s); this.last = m; if (m && m[0] === '') this.rx.lastIndex++; return !!m; }
    $need() { if (!this.last) throwEx('IllegalStateException', 'No match found'); return this.last; }
    group(n) { const m = this.$need(); if (n === undefined || n === 0) return m[0]; if (typeof n === 'string') { const g = m.groups && m.groups[n]; if (g === undefined && !(m.groups && n in m.groups)) throwEx('IllegalArgumentException', 'No group with name <' + n + '>'); return g === undefined ? null : g; } if (n > m.length - 1) throwEx('IndexOutOfBoundsException', 'No group ' + n); return m[n] === undefined ? null : m[n]; }
    groupCount() { return new RegExp(this.p.re + '|').exec('').length - 1; }
    start(n) { const m = this.$need(); return n ? m.index + m[0].indexOf(m[n]) : m.index; } end(n) { const m = this.$need(); return n ? m.index + m[0].indexOf(m[n]) + m[n].length : m.index + m[0].length; }
    replaceAll(r) { return rt.$inv(this.s, 'replaceAll', this.p.re, r); } reset() { this.rx.lastIndex = 0; this.last = null; return this; } hitEnd() { return this.rx.lastIndex >= this.s.length; }
    results() { const a = []; this.reset(); while (this.find()) a.push(this.last[0]); return Stream.fromArray(a); }
  }
  defLib(Matcher, 'Matcher', 'java.util.regex.Matcher'); L.MatchResult = Matcher;

  /* ---------- misc ---------- */
  class UUID extends JObject { static randomUUID() { const u = new UUID(); u.s = 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g, c => { const r = Math.random() * 16 | 0; return (c === 'x' ? r : (r & 3 | 8)).toString(16); }); return u; } toString() { return this.s; } }
  defLib(UUID, 'UUID', 'java.util.UUID');
  class Duration extends JObject { static ofMillis(n) { const d = new Duration(); d.ms = Number(n); return d; } static ofSeconds(n) { return Duration.ofMillis(Number(n) * 1000); } toMillis() { return BigInt(this.ms); } getSeconds() { return BigInt(Math.floor(this.ms / 1000)); } }
  defLib(Duration, 'Duration', 'java.time.Duration');
  class TimeUnit extends JObject { sleep() {} toMillis(n) { return BigInt(Number(n) * this.mult); } toSeconds(n) { return BigInt(Math.floor(Number(n) * this.mult / 1000)); } }
  defLib(TimeUnit, 'TimeUnit', 'java.util.concurrent.TimeUnit');
  ['NANOSECONDS:0.000001', 'MICROSECONDS:0.001', 'MILLISECONDS:1', 'SECONDS:1000', 'MINUTES:60000', 'HOURS:3600000', 'DAYS:86400000'].forEach(s => { const [n, m] = s.split(':'); const t = new TimeUnit(); t.mult = +m; t.$ename = n; TimeUnit[n] = t; });
  // simple synchronous executor so common ExecutorService demos run
  const ExecutionException = (() => { const c = class ExecutionException extends H.Exception { }; defLib(c, 'ExecutionException', 'java.util.concurrent.ExecutionException'); return c; })();
  class Future extends JObject { constructor(v, e) { super(); this.v = v; this.e = e; } get() { if (this.e) throw new ExecutionException().$init(this.e); return this.v; } isDone() { return true; } cancel() { return false; } isCancelled() { return false; } join() { return this.get(); } }
  defLib(Future, 'Future', 'java.util.concurrent.Future');
  class ExecutorService extends JObject {
    submit(t) { try { const f = fnOf(t); const r = f(); return new Future(r === undefined ? null : r); } catch (e) { const w = rt.$wrap(e); return new Future(null, w); } }
    execute(t) { fnOf(t)(); } shutdown() { this.down = true; } shutdownNow() { this.down = true; return imm([]); } awaitTermination() { return true; } isShutdown() { return !!this.down; } isTerminated() { return !!this.down; } close() {}
    invokeAll(c) { return new ArrayList().$init(Array.from(rt.$iter(c)).map(t => this.submit(t))); }
  }
  defLib(ExecutorService, 'ExecutorService', 'java.util.concurrent.ExecutorService');
  class Executors extends JObject { static newFixedThreadPool() { return new ExecutorService(); } static newCachedThreadPool() { return new ExecutorService(); } static newSingleThreadExecutor() { return new ExecutorService(); } static newVirtualThreadPerTaskExecutor() { return new ExecutorService(); } }
  defLib(Executors, 'Executors', 'java.util.concurrent.Executors');
  L.Executor = ExecutorService;
  class CompletableFuture extends Future { static supplyAsync(s) { return new CompletableFuture(fnOf(s)()); } static runAsync(r) { fnOf(r)(); return new CompletableFuture(null); } static completedFuture(v) { return new CompletableFuture(v); } thenApply(f) { return new CompletableFuture(fnOf(f)(this.v)); } thenAccept(f) { fnOf(f)(this.v); return new CompletableFuture(null); } thenCombine(o, f) { return new CompletableFuture(fnOf(f)(this.v, o.v)); } }
  defLib(CompletableFuture, 'CompletableFuture', 'java.util.concurrent.CompletableFuture');
  class ReentrantLock extends JObject { lock() {} unlock() {} tryLock() { return true; } } defLib(ReentrantLock, 'ReentrantLock', 'java.util.concurrent.locks.ReentrantLock'); L.Lock = ReentrantLock;
  class CountDownLatch extends JObject { $init(n) { this.n = n; return this; } countDown() { this.n--; } await() {} getCount() { return BigInt(this.n); } } defLib(CountDownLatch, 'CountDownLatch', 'java.util.concurrent.CountDownLatch');
  class LocalDate extends JObject { static now() { const d = new LocalDate(); d.d = new Date(); return d; } static of(y, m, dd) { const d = new LocalDate(); d.d = new Date(Date.UTC(y, m - 1, dd)); return d; } getYear() { return this.d.getUTCFullYear(); } getMonthValue() { return this.d.getUTCMonth() + 1; } getDayOfMonth() { return this.d.getUTCDate(); } toString() { return this.d.toISOString().slice(0, 10); } plusDays(n) { const x = new LocalDate(); x.d = new Date(this.d.getTime() + Number(n) * 86400000); return x; } isBefore(o) { return this.d < o.d; } isAfter(o) { return this.d > o.d; } equals(o) { return o instanceof LocalDate && +o.d === +this.d; } compareTo(o) { return this.d < o.d ? -1 : this.d > o.d ? 1 : 0; } hashCode() { return Math.floor(+this.d / 86400000); } }
  defLib(LocalDate, 'LocalDate', 'java.time.LocalDate', [L.Comparable]);
  class Instant extends JObject { static now() { const i = new Instant(); i.t = Date.now(); return i; } toEpochMilli() { return BigInt(this.t); } } defLib(Instant, 'Instant', 'java.time.Instant');
  L.Date = class JDate extends JObject { $init(t) { this.t = t === undefined ? Date.now() : Number(t); return this; } getTime() { return BigInt(this.t); } toString() { return new Date(this.t).toString(); } }; defLib(L.Date, 'Date', 'java.util.Date');

  /* ---------- Enum base ---------- */
  class JEnum extends JObject {
    name() { return this.$ename; } ordinal() { return this.$ord; } toString() { return this.$ename; }
    compareTo(o) { return this.$ord - o.$ord; } equals(o) { return this === o; } hashCode() { return identityHash(this); }
    getDeclaringClass() { return rt.$classOf(this.constructor.$enumCls || this.constructor); }
  }
  defLib(JEnum, 'Enum', 'java.lang.Enum', [L.Comparable]);
  JEnum.valueOf = (cls, n) => { if (cls === null) throw mk('NullPointerException'); return cls.c.valueOf(n); };
  rt.$enumInit = (C, consts) => {
    C.$vals = consts; C.$enumCls = C;
    consts.forEach((c, i) => { c.$ord = i; });
    C.values = () => { const a = C.$vals.slice(); a.$t = C.$jn; return a; };
    C.valueOf = n => { if (n === null || n === undefined) throw mk('NullPointerException', 'Name is null'); const c = C.$vals.find(x => x.$ename === n); if (!c) throwEx('IllegalArgumentException', `No enum constant ${C.$cn || C.$jn}.${n}`); return c; };
  };
  /* Record base */
  class JRecord extends JObject { }
  defLib(JRecord, 'Record', 'java.lang.Record');

  JObject.prototype.clone = function () {
    if (!this.constructor.$supers || !this.constructor.$supers.has(L.Cloneable)) throwEx('CloneNotSupportedException', H.className(this));
    const o = Object.create(Object.getPrototypeOf(this)); Object.assign(o, this); return o;
  };
  /* resolve a library class by (possibly dotted) simple name */
  rt.$lib = n => L[n] || L[n.replace(/^.*\./, '')];
  L.Iterator = Iterator;
  L.Entry = Entry;

  /* ---------- overload dispatch + small helpers used by generated code ---------- */
  const depthTo = (v, C) => { let d = 0; for (let c = v.constructor; c; c = Object.getPrototypeOf(c), d++) { if (c === C) return d; } return 1; };
  const score = (t, v) => {
    const isStr = typeof v === 'string' || v instanceof String;
    switch (t) {
      case 'int': return typeof v === 'number' ? 0 : v instanceof Ch ? 3 : null;
      case 'short': case 'byte': return typeof v === 'number' ? 1 : null;
      case 'long': return typeof v === 'bigint' ? 0 : typeof v === 'number' ? 2 : v instanceof Ch ? 3 : null;
      case 'float': return v instanceof D && v.f ? 0 : (typeof v === 'number' || typeof v === 'bigint') ? 3 : v instanceof Ch ? 4 : null;
      case 'double': return v instanceof D && !v.f ? 0 : v instanceof D ? 1 : typeof v === 'number' ? 3 : typeof v === 'bigint' ? 4 : v instanceof Ch ? 5 : null;
      case 'char': return v instanceof Ch ? 0 : null;
      case 'boolean': return typeof v === 'boolean' ? 0 : null;
      case 'String': return v === null ? 1 : isStr ? 0 : null;
      case 'Object': return v === null ? 9 : 8;
      case 'Integer': return typeof v === 'number' ? 4 : v === null ? 2 : null;
      case 'Long': return typeof v === 'bigint' ? 4 : v === null ? 2 : null;
      case 'Double': return v instanceof D && !v.f ? 4 : v === null ? 2 : null;
      case 'Character': return v instanceof Ch ? 4 : v === null ? 2 : null;
      case 'Boolean': return typeof v === 'boolean' ? 4 : v === null ? 2 : null;
    }
    if (t.endsWith('[]')) { if (v === null) return 1; return Array.isArray(v) && (!v.$t || v.$t === t.slice(0, -2) || !/^(int|double|char|long|boolean|byte|short|float)$/.test(t.slice(0, -2))) ? 0 : null; }
    const C = rt.$classes.get(t) || L[t];
    if (!C) return 7;
    if (v === null) return 1;
    if (!rt.$inst(v, C)) return null;
    return typeof v === 'object' ? Math.min(depthTo(v, C), 6) : 5;
  };
  const best = (cands, args) => {
    let pick = null, bs = Infinity;
    for (const c of cands) {
      const n = c.types.length;
      if (c.va ? args.length < n - 1 : args.length !== n) continue;
      let tot = c.va ? 50 : 0, ok = true;
      const fixed = c.va ? n - 1 : n;
      for (let i = 0; i < fixed; i++) { const s = score(c.types[i], args[i]); if (s === null) { ok = false; break; } tot += s; }
      if (ok && tot < bs) { bs = tot; pick = c; }
    }
    if (!pick) pick = cands.find(c => c.va ? args.length >= c.types.length - 1 : args.length === c.types.length) || null;
    return pick;
  };
  const collect = (start, name, key) => {
    const out = [], seen = new Set();
    for (let c = start; c && c !== Function.prototype; c = Object.getPrototypeOf(c)) {
      if (Object.prototype.hasOwnProperty.call(c, key) && c[key][name]) for (const e of c[key][name]) if (!seen.has(e[0])) { seen.add(e[0]); out.push({ fn: e[0], types: e[1], va: e[2] }); }
    }
    return out;
  };
  rt.$ovlm = (obj, name, args, isStatic) => {
    const start = isStatic ? obj : obj.constructor;
    const cands = collect(start, name, isStatic ? '$mss' : '$ms');
    const c = best(cands, args);
    if (!c) throwEx('RuntimeException', `no applicable overload for ${name}`);
    return isStatic ? start[c.fn](...args) : obj[c.fn](...args);
  };
  rt.$ovlsup = (P, obj, name, args) => {
    const cands = collect(P, name, '$ms');
    const c = best(cands, args);
    if (!c) throwEx('RuntimeException', `no applicable overload for ${name}`);
    return P.prototype[c.fn].apply(obj, args);
  };
  rt.$ovlc = (C, obj, args) => {
    const cands = (C.$ctors || []).map(e => ({ fn: e[0], types: e[1], va: e[2] }));
    const c = best(cands, args);
    if (!c) throwEx('RuntimeException', `no applicable constructor for ${C.$jn}`);
    return C.prototype[c.fn].apply(obj, args);
  };
  rt.$ec = (o, name, ord) => { o.$ename = name; o.$ord = ord; return o; };
  rt.$boxEq = (a, b) => {
    if (a === null || b === null || a === undefined || b === undefined) return a === b;
    if (!equals(a, b)) return false;
    if (typeof a === 'number') return a >= -128 && a <= 127;
    if (typeof a === 'bigint') return a >= -128n && a <= 127n;
    if (a instanceof Ch) return a.c <= 127;
    if (a instanceof D) return a === b;
    return true;
  };
  rt.$len = x => { if (x === null || x === undefined) throw mk('NullPointerException'); return Array.isArray(x) ? x.length : x.$length; };
  const hasOwn = Object.prototype.hasOwnProperty;
  rt.$g = c => (hasOwn.call(c, '$d') ? c : c.$si());
});
if (typeof module !== 'undefined') module.exports = JavaRT;
