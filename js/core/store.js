/* localStorage-backed progress. Starts empty: nothing is checked until the learner checks it. */
const $ = s => document.querySelector(s), $$ = s => [...document.querySelectorAll(s)];
const Store = {
  get(k, d) { try { const v = localStorage.getItem('jz:' + k); return v == null ? d : JSON.parse(v); } catch { return d; } },
  set(k, v) { try { localStorage.setItem('jz:' + k, JSON.stringify(v)); } catch {} },
  del() { try { Object.keys(localStorage).filter(k => k.startsWith('jz:')).forEach(k => localStorage.removeItem(k)); } catch {} },
};
// one-time cleanup of the old pre-checked demo data
if (Store.get('v', 0) < 2) { try { localStorage.removeItem('jz:done'); localStorage.removeItem('jz:ran'); } catch {} Store.set('v', 2); }
/* Activity streak: a day counts when the learner completes a lesson, runs code or finishes a quiz. */
const Activity = {
  key: d => `${d.getFullYear()}-${String(d.getMonth() + 1).padStart(2, '0')}-${String(d.getDate()).padStart(2, '0')}`,
  days() { return new Set(Store.get('days', [])); },
  touch() { const d = this.days(); d.add(this.key(new Date())); Store.set('days', [...d].sort().slice(-90)); },
  streak() { const d = this.days(); let n = 0; const t = new Date(); if (!d.has(this.key(t))) t.setDate(t.getDate() - 1); while (d.has(this.key(t))) { n++; t.setDate(t.getDate() - 1); } return n; },
  week() { const d = this.days(), t = new Date(), dow = (t.getDay() + 6) % 7, out = []; for (let i = 0; i < 7; i++) { const x = new Date(t); x.setDate(t.getDate() - dow + i); out.push({ on: d.has(this.key(x)), future: x > t && this.key(x) !== this.key(t) }); } return out; },
};
const Progress = {
  done() { return new Set(Store.get('done', [])); },
  has(id) { return this.done().has(id); },
  toggle(id) { const d = this.done(); if (d.has(id)) d.delete(id); else { d.add(id); Activity.touch(); } Store.set('done', [...d]); },
  forModule(m) { const d = this.done(), n = m.lessons.filter(l => d.has(l.id)).length; return { n, total: m.lessons.length, pct: Math.round(n / m.lessons.length * 100) }; },
  overall() { const d = this.done(), all = MODULES.flatMap(m => m.lessons); return { n: all.filter(l => d.has(l.id)).length, total: all.length }; },
  next() { const d = this.done(); return MODULES.flatMap(m => m.lessons).find(l => !d.has(l.id)); },
};
