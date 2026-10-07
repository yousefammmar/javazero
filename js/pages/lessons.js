/* Lessons index: every module with checkable lessons. */
{
  const root = $('#root'), FILTERS = [['all', 'All Modules'], ['fund', 'Fundamentals'], ['logic', 'Logic & Scope'], ['oop', 'Object-Oriented Java'], ['inh', 'Inheritance & Interfaces']];
  let filter = 'all';
  const render = () => {
    const ov = Progress.overall(), pct = Math.round(ov.n / ov.total * 100);
    root.innerHTML = `<div class="mh"><div><h2>Lessons</h2><p>${ov.total} lessons across ${MODULES.length} modules. Tick a lesson when you finish it.</p></div><span class="cnt">${ov.n} / ${ov.total} done • ${pct}%</span></div>
      <div class="filters">${FILTERS.map(([k, t]) => `<button data-f="${k}" class="${k === filter ? 'on' : ''}">${t}</button>`).join('')}</div>
      <div class="stack-list">${MODULES.filter(m => filter === 'all' || m.track === filter).map(m => {
        const p = Progress.forModule(m);
        return `<section class="mod"><header><div class="ico">${ic(m.icon)}</div><div><h3>${m.name}</h3><p>${m.desc}</p></div><div class="mp"><b>${p.pct}%</b><span>${p.n}/${p.total}</span>${pctBar(p.pct, 'var(--coral)')}</div></header>
          <ol>${m.lessons.map(l => `<li class="${Progress.has(l.id) ? 'done' : ''}"><button class="tick" data-id="${l.id}" role="checkbox" aria-checked="${Progress.has(l.id)}" aria-label="Mark ${l.t} complete">${ic('check')}</button>
            <a href="lesson.html?id=${l.id}"><span class="n">${String(l.n).padStart(2, '0')}</span><span class="t"><b>${l.t}</b><small>${l.sum}</small></span>${ic('chevr')}</a></li>`).join('')}</ol></section>`;
      }).join('')}</div>`;
  };
  root.onclick = e => {
    const f = e.target.closest('[data-f]'), t = e.target.closest('.tick');
    if (f) { filter = f.dataset.f; render(); } else if (t) { Progress.toggle(t.dataset.id); render(); }
  };
  render();
}
