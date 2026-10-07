/* Daily quiz: one question at a time with instant feedback. */
{
  const root = $('#root'); let i = 0, score = 0, picked = null;
  const draw = () => {
    if (i >= QUIZ.length) {
      const best = Math.max(Store.get('quizBest', 0), score); Store.set('quizBest', best);
      root.innerHTML = `<div class="qres"><div class="ico big2">${ic('trophy')}</div><h2>${score} / ${QUIZ.length}</h2><p>${score === QUIZ.length ? 'Perfect. Nothing got past you.' : score >= 8 ? 'Excellent work.' : score >= 5 ? 'Solid. Review the lessons you missed.' : 'Good start. Revisit the lessons and try again.'}</p><p class="mut">Best score: <b>${best}</b></p>
        <div class="actions c"><button class="btn b1" id="again">${ic('reset')} Try again</button><a class="btn b2 m0" href="lessons.html">${ic('book')} Back to lessons</a></div></div>`;
      $('#again').onclick = () => { i = 0; score = 0; picked = null; draw(); }; return;
    }
    const q = QUIZ[i];
    root.innerHTML = `<div class="mh"><div><h2>Daily Practice Quiz</h2><p>Question ${i + 1} of ${QUIZ.length}</p></div><span class="cnt">Score ${score}</span></div>${pctBar((i / QUIZ.length) * 100, 'var(--coral)')}
      <div class="qcard"><h3>${esc(q.q)}</h3><div class="opts" role="radiogroup">${q.o.map((o, n) => `<button class="opt" data-n="${n}" role="radio"><span class="k">${n + 1}</span>${esc(o)}</button>`).join('')}</div>
        <div class="fb" id="fb" aria-live="polite"></div><div class="actions"><button class="btn b1" id="nxt" hidden>${i + 1 === QUIZ.length ? 'See result' : 'Next question'} ${ic('right')}</button></div></div>`;
    picked = null;
    $$('.opt').forEach(b => b.onclick = () => pick(+b.dataset.n));
    $('#nxt').onclick = () => { i++; draw(); };
  };
  const pick = n => {
    if (picked !== null) return; picked = n; const q = QUIZ[i];
    $$('.opt').forEach(b => { const k = +b.dataset.n; b.disabled = true; if (k === q.a) b.classList.add('right'); else if (k === n) b.classList.add('wrong'); });
    if (n === q.a) score++;
    $('#fb').innerHTML = `<b class="${n === q.a ? 'grn' : 'red'}">${n === q.a ? 'Correct.' : 'Not quite.'}</b> ${esc(q.why)}`; $('#nxt').hidden = false; $('#nxt').focus();
  };
  addEventListener('keydown', e => { if (/^[1-4]$/.test(e.key) && i < QUIZ.length) pick(+e.key - 1); });
  draw();
}
