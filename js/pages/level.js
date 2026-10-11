/* One level page: what the level covers, its interactive lessons, and its W3Schools topics. */
{
  const root = $('#root'), lv = LEVELS.find(l => l.id === $('#main').dataset.level), idx = LEVELS.indexOf(lv);
  document.title = `${lv.name} lessons – JavaZero`;
  const lessonIds = new Set(MODULES.flatMap(m => m.lessons.map(l => l.id)));
  const topicHTML = ([t, path, lesson, d]) => `<li><div class="tp"><a class="tp-w3" href="${W3}${path}" target="_blank" rel="noopener"><b>${t}</b>${d ? `<small>${d}</small>` : ''}<span class="sr"> (opens W3Schools in a new tab)</span></a>
    ${lesson && lessonIds.has(lesson) ? `<a class="tp-lesson" href="lesson.html?id=${lesson}">${ic('play')} Interactive lesson</a>` : ''}</div></li>`;
  const render = () => {
    const s = levelStats(lv), next = levelModules(lv).flatMap(m => m.lessons).find(l => !Progress.has(l.id));
    root.innerHTML = `<nav class="bc" aria-label="Breadcrumb"><a href="lessons.html">Lessons</a>${ic('chevr')}<span>${lv.name}</span></nav>
      <header class="lvh lv${idx + 1}"><div><span class="tag">Level ${idx + 1} of ${LEVELS.length} • ${lv.tagline}</span><h1>${lv.name}</h1><p class="lead2">${lv.desc}</p>
        <div class="lvh-meta"><span>${ic('book')} ${s.lessons} interactive lessons in ${s.modules} modules</span><span>${ic('map')} ${s.topics} W3Schools topics</span><span>${ic('target')} ${lv.weeks}</span><span>${ic('user')} ${lv.audience}</span></div></div>
        <div class="lvh-side"><div class="row"><span class="mut">Your progress</span><b>${s.pct}%</b></div>${pctBar(s.pct, 'var(--coral)')}<small>${s.done} of ${s.lessons} lessons done</small>
          ${next ? `<a class="btn b1 full" href="lesson.html?id=${next.id}">${ic('play')} ${s.done ? 'Continue' : 'Start'}: ${next.t}</a>` : `<a class="btn b3 full" href="quiz.html">${ic('trophy')} Level complete: take the quiz</a>`}</div></header>
      <section aria-labelledby="h-lessons"><div class="mh"><div><h2 id="h-lessons">Interactive lessons</h2><p>Read, run and tick off each lesson. Every example runs in your browser.</p></div></div>${moduleListHTML(levelModules(lv))}</section>
      <section aria-labelledby="h-topics"><div class="mh"><div><h2 id="h-topics">Topics on W3Schools</h2><p>The ${lv.name} topics from the W3Schools Java tutorial. Where we wrote a matching lesson it is linked too.</p></div></div>
        <div class="tgroups">${lv.groups.map(g => `<section class="tgroup"><h3>${g.name} <span class="cnt-s">${g.topics.length} ${g.topics.length === 1 ? 'topic' : 'topics'}</span></h3><p>${g.blurb}</p>
          ${g.run === false ? `<p class="norun">${ic('info')} These need a real computer: a browser cannot read or write your files, so use W3Schools and a local JDK to try them.</p>` : ''}
          <ul>${g.topics.map(topicHTML).join('')}</ul></section>`).join('')}</div></section>
      <nav class="lvnav" aria-label="Other levels">${LEVELS.map((o, i) => i === idx ? '' : `<a href="${o.page}">${i < idx ? ic('left') : ''}<span><small>${i < idx ? 'Previous level' : 'Next level'}</small>${o.name}</span>${i > idx ? ic('right') : ''}</a>`).join('')}</nav>`;
  };
  root.onclick = e => { const t = e.target.closest('.tick'); if (t) { Progress.toggle(t.dataset.id); render(); refreshAside(); } };
  render();
  if (location.hash) { const el = document.getElementById(location.hash.slice(1)); if (el) el.scrollIntoView(); }
}
