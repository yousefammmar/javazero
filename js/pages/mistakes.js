/* Mistake Lab: buggy vs corrected code for the classic beginner errors. */
{
  const root = $('#root'), seen = new Set(Store.get('seenMistakes', []));
  root.innerHTML = `<div class="mh"><div><h2>Mistake Lab</h2><p>Every Java developer makes these. Flip each card to see the buggy code and the fix.</p></div><span class="cnt" id="cnt"></span></div>
    <div class="mods mist">${MISTAKES.map((m, n) => `<article class="card" id="${m.id}" data-id="${m.id}"><div class="ch"><div class="ico red-ico">${ic('bug')}</div><span class="bd red-bd">Pitfall #${String(n + 1).padStart(2, '0')}</span><span class="m seen">${ic('checkc')}</span></div>
      <h3>${m.t}</h3><p>${m.why}</p><div class="box"><div class="bug"><button class="a on" data-m="bad">Buggy Code</button><button class="b" data-m="good">Corrected Fix</button></div><pre class="code bugcode red">${esc(m.bad)}</pre></div></article>`).join('')}</div>`;
  const count = () => { $('#cnt').textContent = `${seen.size} / ${MISTAKES.length} explored`; $$('.mist .card').forEach(c => c.classList.toggle('seen-on', seen.has(c.dataset.id))); };
  const mark = id => { seen.add(id); Store.set('seenMistakes', [...seen]); count(); };
  root.onclick = e => {
    const b = e.target.closest('[data-m]'); if (!b) return;
    const card = b.closest('.card'), m = MISTAKES.find(x => x.id === card.dataset.id), bad = b.dataset.m === 'bad';
    card.querySelectorAll('[data-m]').forEach(x => x.classList.toggle('on', x === b));
    const pre = card.querySelector('pre'); pre.textContent = bad ? m.bad : m.good; pre.className = `code bugcode ${bad ? 'red' : 'grn'}`; mark(m.id);
  };
  count();
  if (location.hash) { const t = document.getElementById(location.hash.slice(1)); if (t) { t.scrollIntoView(); t.classList.add('flash'); } }
}
