/* App shell: header, nav, sidebar and command palette around each page's <main>. */
const NAV = [
  ['index.html', 'dashboard', 'Dashboard'], ['lessons.html', 'book', 'Lessons'], ['roadmap.html', 'branch', 'Roadmap'],
  ['playground.html', 'terminal', 'Playground'], ['tools.html', 'wrench', 'Tools'], ['mistakes.html', 'bug', 'Mistake Lab'], ['quiz.html', 'quiz', 'Daily Quiz'], ['resources.html', 'link', 'Resources'],
];
(() => {
  const main = $('#main'), page = (location.pathname.split('/').pop() || 'index.html').replace('lesson.html', 'lessons.html');
  const title = main.dataset.title || 'Dashboard';
  const noAside = main.dataset.aside === 'off';
  const ov = Progress.overall(), core = MODULES.slice(0, 3), coreDone = core.reduce((a, m) => a + Progress.forModule(m).n, 0), coreTot = core.reduce((a, m) => a + m.lessons.length, 0);
  const corePct = Math.round(coreDone / coreTot * 100), next = Progress.next();
  const earned = BADGES.filter(b => b.test()), tip = TIPS[new Date().getDate() % TIPS.length];
  const shown = [...earned, ...BADGES.filter(b => !b.test())].slice(0, 3);

  const app = document.createElement('div'); app.className = 'app';
  app.innerHTML = `
  <header class="top">
    <a class="logo" href="index.html" aria-label="JavaZero home">JO</a>
    <div class="crumb"><b>JAVA ZERO</b> / ${title.toUpperCase()}</div>
    <span class="pill"><i class="dot"></i>Java 21 LTS Standard HotSpot</span>
    <button class="search" id="openPalette" aria-label="Quick find">${ic('search')}<span>Quick find classes, errors, syntax…</span><kbd>⌘K</kbd></button>
  </header>
  <div class="grid ${noAside ? 'no-aside' : ''}">
    <nav aria-label="Primary">
      ${NAV.map(([h, i, t]) => `<a href="${h}" class="${h === page ? 'on' : ''}" title="${t}" aria-label="${t}" ${h === page ? 'aria-current="page"' : ''}>${ic(i)}</a>`).join('')}
      <span class="sp"></span>
      <button title="Command palette" id="navKb" aria-label="Command palette">${ic('keyboard')}</button>
      <a href="settings.html" class="${page === 'settings.html' ? 'on' : ''}" title="Settings" aria-label="Settings">${ic('sliders')}</a>
    </nav>
    <div class="slot"></div>
    ${noAside ? '' : `<aside>
      <div class="side"><h4><span class="l">ACTIVE STREAK</span><b class="red">${ic('flame')} 4 Days</b></h4>
        <div class="days">${'MTWTFSS'.split('').map((d, i) => `<div>${d}<i class="${i < 4 ? 'd' : ''}">${i < 4 ? ic('check') : ''}</i></div>`).join('')}</div></div>
      <div class="side"><h4>Java Core I <span class="bd">${corePct}%</span></h4><div class="bar thick"><i style="width:${corePct}%;background:var(--coral)"></i></div>
        <div class="nm"><b>Next Milestone:</b><br><span>${next ? next.t : 'All lessons complete'}</span></div>
        <a class="btn b1 full" href="${next ? 'lesson.html?id=' + next.id : 'quiz.html'}">${ic('play')} Continue Lesson</a></div>
      <div class="side"><h4>Earned Badges <span class="cnt-s">${earned.length} / ${BADGES.length}</span></h4>
        <div class="bdg">${shown.map(b => `<div class="${b.test() ? '' : 'lock'}" title="${b.hint}"><i>${ic(b.icon)}</i>${b.t}</div>`).join('')}</div>
        <a class="more" href="settings.html#badges">View all badges ${ic('chevr')}</a></div>
      <div class="tip"><b>${ic('bulb')} ${tip[0]}</b>${tip[1]}</div>
    </aside>`}
  </div>`;
  app.insertAdjacentHTML('beforeend', `<footer class="sig"><a class="avatar" href="https://yousefammmar.github.io/profile" target="_blank" rel="noopener" aria-label="Yousef Odeh - open portfolio" title="Yousef Odeh - portfolio"><img src="img/avatar.png" width="72" height="72" alt=""></a><span>Designed &amp; built by</span><b class="sign">Yousef Odeh</b>${ic('code')}</footer>`);
  app.querySelector('.slot').replaceWith(main);
  document.body.append(app);

  // command palette
  const items = [
    ...NAV.map(([h, i, t]) => ({ t, s: 'Page', i, h, k: '' })),
    ...MODULES.flatMap(m => m.lessons.map(l => ({ t: l.t, s: m.name, i: 'book', h: `lesson.html?id=${l.id}`, k: l.sum }))),
    ...MISTAKES.map(x => ({ t: x.t, s: 'Mistake Lab', i: 'bug', h: `mistakes.html#${x.id}`, k: x.why })),
    ...EXCEPTIONS.map(x => ({ t: x.n, s: 'Stack traces', i: 'terminal', h: 'tools.html#stack', k: x.say })),
    ...COMPILER_ERRORS.map(x => ({ t: x.e, s: 'Compiler errors', i: 'warn', h: 'tools.html#errors', k: x.say })),
    ...RESOURCES.flatMap(g => g.items.map(([t, , d]) => ({ t: `${t} (W3Schools)`, s: 'Resources', i: 'link', h: 'resources.html', k: d }))),
    { t: 'String Lab', s: 'Tools', i: 'type', h: 'tools.html#string', k: 'toUpperCase substring replace' },
    { t: 'Multi-file IDE', s: 'Tools', i: 'folder', h: 'tools.html#ide', k: 'heap stack memory' },
  ];
  const pal = document.createElement('div'); pal.className = 'pal'; pal.hidden = true;
  pal.innerHTML = `<div class="pal-box" role="dialog" aria-label="Quick find"><div class="pal-in">${ic('search')}<input placeholder="Search lessons, errors, tools, syntax…" aria-label="Search"><kbd>esc</kbd></div><ul role="listbox"></ul></div>`;
  document.body.append(pal);
  const inp = pal.querySelector('input'), ul = pal.querySelector('ul'); let sel = 0, list = [];
  const draw = () => {
    const q = inp.value.trim().toLowerCase();
    list = (q ? items.filter(x => (x.t + ' ' + x.s + ' ' + x.k).toLowerCase().includes(q)) : items.slice(0, 7)).slice(0, 9);
    sel = Math.min(sel, Math.max(0, list.length - 1));
    ul.innerHTML = list.length ? list.map((x, n) => `<li role="option" data-n="${n}" class="${n === sel ? 'on' : ''}">${ic(x.i)}<span>${x.t}</span><small>${x.s}</small></li>`).join('') : '<li class="empty">No matches</li>';
  };
  const open = () => { pal.hidden = false; inp.value = ''; sel = 0; draw(); inp.focus(); };
  const close = () => { pal.hidden = true; };
  const go = n => { if (list[n]) location.href = list[n].h; };
  $('#openPalette').onclick = $('#navKb').onclick = open;
  pal.onclick = e => { if (e.target === pal) close(); const li = e.target.closest('li[data-n]'); if (li) go(+li.dataset.n); };
  inp.oninput = () => { sel = 0; draw(); };
  inp.onkeydown = e => {
    if (e.key === 'ArrowDown') { sel = (sel + 1) % list.length; draw(); e.preventDefault(); }
    if (e.key === 'ArrowUp') { sel = (sel - 1 + list.length) % list.length; draw(); e.preventDefault(); }
    if (e.key === 'Enter') go(sel);
  };
  addEventListener('keydown', e => {
    if ((e.metaKey || e.ctrlKey) && e.key.toLowerCase() === 'k') { e.preventDefault(); pal.hidden ? open() : close(); }
    else if (e.key === 'Escape') close();
    else if (e.key === '/' && !/INPUT|TEXTAREA|SELECT/.test(document.activeElement.tagName)) { e.preventDefault(); open(); }
  });
})();
