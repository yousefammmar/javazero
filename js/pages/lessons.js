/* Lessons landing page: choose a level. Each card shows the level, what it covers and your progress. */
{
  const root = $('#root');
  const render = () => {
    const ov = Progress.overall(), pct = Math.round(ov.n / ov.total * 100);
    root.innerHTML = `<div class="mh"><div><h1>Lessons</h1><p>${ov.total} interactive lessons in three levels. Pick the one that matches where you are today.</p></div><span class="cnt">${ov.n} / ${ov.total} done • ${pct}%</span></div>
      <div class="levels">${LEVELS.map((lv, i) => {
        const s = levelStats(lv), preview = lv.groups.flatMap(g => g.topics).slice(0, 8);
        return `<article class="level lv${i + 1}">
          <div class="lv-top"><span class="lv-ico">${ic(lv.icon)}</span><span class="lv-step">Level ${i + 1} of ${LEVELS.length}</span></div>
          <h2><a href="${lv.page}">${lv.name}</a></h2>
          <p class="lv-tag">${lv.tagline} • ${lv.audience}</p>
          <p class="lv-desc">${lv.desc}</p>
          <dl class="lv-stats"><div><dt>Lessons</dt><dd>${s.lessons}</dd></div><div><dt>Topics</dt><dd>${s.topics}</dd></div><div><dt>Plan</dt><dd>${lv.weeks.replace('Weeks ', 'Wk ')}</dd></div></dl>
          <div class="lv-prog"><div class="row"><span class="mut">Your progress</span><b>${s.pct}%</b></div>${pctBar(s.pct, 'var(--coral)')}<small>${s.done} of ${s.lessons} lessons done</small></div>
          <ul class="chips2" aria-label="Some topics in ${lv.name}">${preview.map(t => `<li>${t[0].replace(/^Java /, '')}</li>`).join('')}<li class="more-chip">+ more</li></ul>
          <a class="btn b1 lv-go" href="${lv.page}">Open ${lv.name} ${ic('right')}</a>
        </article>`;
      }).join('')}</div>
      <p class="lv-note">Not sure where to start? If you have never written code, begin with <a href="basics.html">Basics</a>. Topic lists follow the structure of the <a href="https://www.w3schools.com/java/" target="_blank" rel="noopener">W3Schools Java tutorial</a>; the lessons and examples are original.</p>`;
  };
  render();
}
