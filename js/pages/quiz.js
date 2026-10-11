/* Daily quiz: one question at a time with instant feedback. Two sets: core (10) and intermediate (8). */
{
  const root = $('#root');
  const SETS = { core: { name: 'Core', qs: QUIZ, key: 'quizBest' }, mid: { name: 'Intermediate', qs: QUIZ_MID, key: 'quizBestMid' } };
  let set = 'core', i = 0, score = 0, picked = null;
  const cur = () => SETS[set].qs;
  const tabs = () => `<div class="filters" role="tablist" aria-label="Quiz set">${Object.entries(SETS).map(([k, s]) => `<button role="tab" aria-selected="${k === set}" data-set="${k}" class="${k === set ? 'on' : ''}">${s.name} (${s.qs.length})</button>`).join('')}</div>`;
  const draw = () => {
    const QS = cur();
    if (i >= QS.length) {
      const key = SETS[set].key, best = Math.max(Store.get(key, 0), score); Store.set(key, best); Activity.touch();
      const top = QS.length;
      root.innerHTML = `${tabs()}<div class="qres"><div class="ico big2">${ic('trophy')}</div><h1>${score} / ${top}</h1><p>${score === top ? 'Perfect. Nothing got past you.' : score >= top * 0.8 ? 'Excellent work.' : score >= top / 2 ? 'Solid. Review the lessons you missed.' : 'Good start. Revisit the lessons and try again.'}</p><p class="mut">Best score: <b>${best}</b></p>
        <div class="actions c"><button class="btn b1" id="again">${ic('reset')} Try again</button><a class="btn b2 m0" href="lessons.html">${ic('book')} Back to lessons</a></div></div>`;
      $('#again').onclick = () => { i = 0; score = 0; picked = null; draw(); }; return;
    }
    const q = QS[i];
    root.innerHTML = `${tabs()}<div class="mh"><div><h1>Daily Practice Quiz</h1><p>${SETS[set].name} set • Question ${i + 1} of ${QS.length}</p></div><span class="cnt">Score ${score}</span></div>${pctBar((i / QS.length) * 100, 'var(--coral)')}
      <div class="qcard"><h2>${esc(q.q)}</h2><div class="opts" role="radiogroup">${q.o.map((o, n) => `<button class="opt" data-n="${n}" role="radio"><span class="kn">${n + 1}</span>${esc(o)}</button>`).join('')}</div>
        <div class="fb" id="fb" aria-live="polite"></div><div class="actions"><button class="btn b1" id="nxt" hidden>${i + 1 === QS.length ? 'See result' : 'Next question'} ${ic('right')}</button></div></div>`;
    picked = null;
    $$('.opt').forEach(b => b.onclick = () => pick(+b.dataset.n));
    $('#nxt').onclick = () => { i++; draw(); };
  };
  const pick = n => {
    if (picked !== null) return; picked = n; const q = cur()[i];
    $$('.opt').forEach(b => { const k = +b.dataset.n; b.disabled = true; if (k === q.a) b.classList.add('right'); else if (k === n) b.classList.add('wrong'); });
    if (n === q.a) score++;
    $('#fb').innerHTML = `<b class="${n === q.a ? 'grn' : 'red'}">${n === q.a ? 'Correct.' : 'Not quite.'}</b> ${esc(q.why)}`; $('#nxt').hidden = false; $('#nxt').focus();
  };
  root.onclick = e => { const t = e.target.closest('[data-set]'); if (t && t.dataset.set !== set) { set = t.dataset.set; i = 0; score = 0; picked = null; draw(); } };
  addEventListener('keydown', e => { if (/^[1-4]$/.test(e.key) && i < cur().length && picked === null && $('.opt')) pick(+e.key - 1); });
  draw();
}
