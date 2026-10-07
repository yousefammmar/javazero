/* localStorage-backed progress. Starts empty: nothing is checked until the learner checks it. */
const $ = s => document.querySelector(s), $$ = s => [...document.querySelectorAll(s)];
const Store = {
  get(k, d) { try { const v = localStorage.getItem('jz:' + k); return v == null ? d : JSON.parse(v); } catch { return d; } },
  set(k, v) { try { localStorage.setItem('jz:' + k, JSON.stringify(v)); } catch {} },
  del() { try { Object.keys(localStorage).filter(k => k.startsWith('jz:')).forEach(k => localStorage.removeItem(k)); } catch {} },
};
// one-time cleanup of the old pre-checked demo data
if (Store.get('v', 0) < 2) { try { localStorage.removeItem('jz:done'); localStorage.removeItem('jz:ran'); } catch {} Store.set('v', 2); }
const Progress = {
  done() { return new Set(Store.get('done', [])); },
  has(id) { return this.done().has(id); },
  toggle(id) { const d = this.done(); d.has(id) ? d.delete(id) : d.add(id); Store.set('done', [...d]); },
  forModule(m) { const d = this.done(), n = m.lessons.filter(l => d.has(l.id)).length; return { n, total: m.lessons.length, pct: Math.round(n / m.lessons.length * 100) }; },
  overall() { const d = this.done(), all = MODULES.flatMap(m => m.lessons); return { n: all.filter(l => d.has(l.id)).length, total: all.length }; },
  next() { const d = this.done(); return MODULES.flatMap(m => m.lessons).find(l => !d.has(l.id)); },
};
