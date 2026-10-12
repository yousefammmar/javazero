/* Single lesson viewer with runnable example. */
lesson: {
  const flat = MODULES.flatMap(m => m.lessons), id = new URLSearchParams(location.search).get('id') || flat[0].id;
  const i = flat.findIndex(l => l.id === id), root = $('#root');
  if (i < 0) {
    document.title = 'Lesson not found – JavaZero';
    root.innerHTML = `<div class="qres"><div class="ico big2">${ic('search')}</div><h1>Lesson not found</h1><p class="mut">There is no lesson called <code>${esc(id)}</code>. The link may be out of date.</p><div class="actions c"><a class="btn b1" href="lessons.html">${ic('book')} Browse all lessons</a><a class="btn b2 m0" href="index.html">${ic('dashboard')} Dashboard</a></div></div>`;
    break lesson;
  }
  const l = flat[i], m = MODULES.find(x => x.id === l.mod);
  document.title = `${l.t} – JavaZero`;
  // per-lesson search snippet and canonical URL (the static tags in lesson.html are generic)
  const desc = document.querySelector('meta[name="description"]');
  if (desc) desc.setAttribute('content', `${l.t}: ${l.sum} A runnable Java example with key points, in the ${m.name} module.`);
  const canon = document.createElement('link'); canon.rel = 'canonical'; canon.href = `https://yousefammmar.github.io/javazero/lesson.html?id=${l.id}`; document.head.appendChild(canon);
  const render = () => {
    const done = Progress.has(l.id), prev = flat[i - 1], next = flat[i + 1];
    root.innerHTML = `<nav class="bc" aria-label="Breadcrumb"><a href="lessons.html">Lessons</a>${ic('chevr')}<span>${m.name}</span></nav>
      <div class="lh"><span class="tag">${m.tag} • Lesson ${l.n} of ${m.lessons.length}</span><h1>${l.t}</h1><p class="lead2">${l.sum}</p></div>
      <div class="lesson-grid"><div>
        <div class="codecard"><div class="dots"><i style="background:#ff5f57"></i><i style="background:#febc2e"></i><i style="background:#28c840"></i>&nbsp;Main.java<button class="rb" id="run">${ic('play')} RUN</button></div><pre>${hl(l.code)}</pre>
          <div class="console"><small>CONSOLE</small><div id="out" aria-live="polite">Press RUN to execute this example.</div></div></div>
        <div class="actions"><a class="btn b2 m0" href="playground.html" id="pg">${ic('terminal')} Edit in playground</a>
          <button class="btn ${done ? 'b3' : 'b1'}" id="done">${ic('checkc')} ${done ? 'Completed' : 'Mark complete'}</button></div></div>
        <aside class="kp"><h2>${ic('bulb')} Key points</h2><ul>${l.pts.map(p => `<li>${p}</li>`).join('')}</ul>
          <a class="w3" href="${W3}${W3_LESSON[l.id] || ''}" target="_blank" rel="noopener">${ic('upright')} Read more on W3Schools</a>
          <div class="step-note"><b>Try it:</b> change a value in the playground and predict the output before you run it.</div></aside></div>
      <div class="pn">${prev ? `<a href="lesson.html?id=${prev.id}">${ic('left')}<span><small>Previous</small>${prev.t}</span></a>` : '<span></span>'}${next ? `<a class="nx" href="lesson.html?id=${next.id}"><span><small>Next</small>${next.t}</span>${ic('right')}</a>` : `<a class="nx" href="quiz.html"><span><small>Finished</small>Take the daily quiz</span>${ic('right')}</a>`}</div>`;
    $('#run').onclick = () => {
      const r = Java.run(l.code);
      $('#out').innerHTML = r.err ? `<span class="e">${esc(r.err.list ? r.err.list[0].msg : r.err.text)}</span>` : r.out.map(esc).join('<br>');
      Store.set('ran', Store.get('ran', 0) + 1); Activity.touch();
    };
    $('#pg').onclick = () => Store.set('draft', l.code);
    $('#done').onclick = () => { Progress.toggle(l.id); render(); refreshAside(); };
  };
  render();
}
