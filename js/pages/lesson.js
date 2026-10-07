/* Single lesson viewer with runnable example. */
{
  const flat = MODULES.flatMap(m => m.lessons), id = new URLSearchParams(location.search).get('id') || flat[0].id;
  const i = Math.max(0, flat.findIndex(l => l.id === id)), l = flat[i], m = MODULES.find(x => x.id === l.mod), root = $('#root');
  document.title = `${l.t} – JavaZero`;
  const render = () => {
    const done = Progress.has(l.id), prev = flat[i - 1], next = flat[i + 1];
    root.innerHTML = `<nav class="bc" aria-label="Breadcrumb"><a href="lessons.html">Lessons</a>${ic('chevr')}<span>${m.name}</span></nav>
      <div class="lh"><span class="tag">${m.tag} • Lesson ${l.n} of ${m.lessons.length}</span><h2>${l.t}</h2><p class="lead2">${l.sum}</p></div>
      <div class="lesson-grid"><div>
        <div class="codecard"><div class="dots"><i style="background:#ff5f57"></i><i style="background:#febc2e"></i><i style="background:#28c840"></i>&nbsp;Main.java<button class="rb" id="run">${ic('play')} RUN</button></div><pre>${hl(l.code)}</pre>
          <div class="console"><small>CONSOLE</small><div id="out" aria-live="polite">Press RUN to execute this example.</div></div></div>
        <div class="actions"><a class="btn b2 m0" href="playground.html" id="pg">${ic('terminal')} Edit in playground</a>
          <button class="btn ${done ? 'b3' : 'b1'}" id="done">${ic('checkc')} ${done ? 'Completed' : 'Mark complete'}</button></div></div>
        <aside class="kp"><h4>${ic('bulb')} Key points</h4><ul>${l.pts.map(p => `<li>${p}</li>`).join('')}</ul>
          <div class="step-note"><b>Try it:</b> change a value in the playground and predict the output before you run it.</div></aside></div>
      <div class="pn">${prev ? `<a href="lesson.html?id=${prev.id}">${ic('left')}<span><small>Previous</small>${prev.t}</span></a>` : '<span></span>'}${next ? `<a class="nx" href="lesson.html?id=${next.id}"><span><small>Next</small>${next.t}</span>${ic('right')}</a>` : `<a class="nx" href="quiz.html"><span><small>Finished</small>Take the daily quiz</span>${ic('right')}</a>`}</div>`;
    $('#run').onclick = () => {
      const r = Java.run(l.code);
      $('#out').innerHTML = r.err ? `<span class="e">${esc(r.err.list ? r.err.list[0].msg : r.err.text)}</span>` : r.out.map(esc).join('<br>');
      Store.set('ran', Store.get('ran', 1) + 1);
    };
    $('#pg').onclick = () => Store.set('draft', l.code);
    $('#done').onclick = () => { Progress.toggle(l.id); render(); };
  };
  render();
}
