/* Module sections with checkable lessons. Shared by the level pages. */
const moduleListHTML = mods => `<div class="stack-list">${mods.map(m => {
  const p = Progress.forModule(m);
  return `<section class="mod" id="m-${m.id}"><header><div class="ico">${ic(m.icon)}</div><div><h3>${m.name}</h3><p>${m.desc}</p></div><div class="mp"><b>${p.pct}%</b><span>${p.n}/${p.total}</span>${pctBar(p.pct, 'var(--coral)')}</div></header>
    <ol>${m.lessons.map(l => `<li class="${Progress.has(l.id) ? 'done' : ''}"><button class="tick" data-id="${l.id}" role="checkbox" aria-checked="${Progress.has(l.id)}" aria-label="Mark ${l.t} complete">${ic('check')}</button>
      <a href="lesson.html?id=${l.id}"><span class="n">${String(l.n).padStart(2, '0')}</span><span class="t"><b>${l.t}</b><small>${l.sum}</small></span>${ic('chevr')}</a></li>`).join('')}</ol></section>`;
}).join('')}</div>`;
