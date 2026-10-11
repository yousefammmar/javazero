/* App shell: header, slide-out section menu, right-hand progress panel and command palette around each page's <main>. */
const NAV = [
  ['index.html', 'dashboard', 'Dashboard'], ['lessons.html', 'book', 'Lessons'], ['roadmap.html', 'branch', 'Roadmap'],
  ['playground.html', 'terminal', 'Playground'], ['tools.html', 'wrench', 'Tools'], ['mistakes.html', 'bug', 'Mistake Lab'], ['quiz.html', 'quiz', 'Daily Quiz'], ['resources.html', 'link', 'Resources'],
];
(() => {
  const main = $('#main'), file = location.pathname.split('/').pop() || 'index.html', page = file.replace('lesson.html', 'lessons.html');
  // which level the visitor is in: a level page, or the level that owns the lesson being read
  const lvPage = LEVELS.find(l => l.page === file), lvLesson = file === 'lesson.html' ? levelOfLesson(new URLSearchParams(location.search).get('id')) : null, curLevel = (lvPage || lvLesson || {}).id;
  const inLessons = file === 'lessons.html' || file === 'lesson.html' || !!lvPage;
  const title = main.dataset.title || 'Dashboard';
  const noAside = main.dataset.aside === 'off';
  const asideInner = () => {
    const next = Progress.next(), streak = Activity.streak(), week = Activity.week();
    const bars = LEVELS.map((l, i) => ({ l, s: levelStats(l), c: ['var(--coral)', 'var(--purple)', 'var(--green)'][i] })).filter(x => x.s.lessons);
    const earned = BADGES.filter(b => b.test()), tip = TIPS[new Date().getDate() % TIPS.length];
    const shown = [...earned, ...BADGES.filter(b => !b.test())].slice(0, 3);
    return `
      <div class="side"><h2><span class="l">ACTIVE STREAK</span><b class="red">${ic('flame')} ${streak} ${streak === 1 ? 'Day' : 'Days'}</b></h2>
        <div class="days">${'MTWTFSS'.split('').map((d, i) => `<div>${d}<i class="${week[i].on ? 'd' : ''}">${week[i].on ? ic('check') : ''}</i></div>`).join('')}</div></div>
      <div class="side">${bars.map((x, i) => `<h2${i ? ' style="margin-top:14px"' : ''}><a class="lvlink" href="${x.l.page}">${x.l.name}</a> <span class="bd">${x.s.pct}%</span></h2><div class="bar thick"><i style="width:${x.s.pct}%;background:${x.c}"></i></div>`).join('')}
        <div class="nm"><b>Next Milestone:</b><br><span>${next ? next.t : 'All lessons complete'}</span></div>
        <a class="btn b1 full" href="${next ? 'lesson.html?id=' + next.id : 'quiz.html'}">${ic('play')} Continue Lesson</a></div>
      <div class="side"><h2>Earned Badges <span class="cnt-s">${earned.length} / ${BADGES.length}</span></h2>
        <div class="bdg">${shown.map(b => `<div class="${b.test() ? '' : 'lock'}" title="${b.hint}"><i>${ic(b.icon)}</i>${b.t}</div>`).join('')}</div>
        <a class="more" href="settings.html#badges">View all badges ${ic('chevr')}</a></div>
      <div class="tip"><b>${ic('bulb')} ${tip[0]}</b>${tip[1]}</div>
    `;
  };
  // re-render the sidebar after progress changes (lesson ticks)
  window.refreshAside = () => { const a = $('aside.rail'); if (a) a.innerHTML = asideInner(); };

  const app = document.createElement('div'); app.className = 'app';
  app.innerHTML = `
  <a class="skip" href="#main">Skip to main content</a>
  <header class="top">
    <button class="menu" id="menuBtn" aria-label="Open sections menu" aria-expanded="false" aria-controls="drawer">${ic('sidebar')}</button>
    <a class="logo" href="index.html" aria-label="JavaZero home">JO</a>
    <div class="crumb"><b>JAVA ZERO</b> / ${title.toUpperCase()}</div>
    <span class="pill"><i class="dot"></i>Java 21 LTS Standard HotSpot</span>
    <button class="search" id="openPalette" aria-label="Quick find">${ic('search')}<span>Quick find classes, errors, syntax…</span><kbd>⌘K</kbd></button>
  </header>
  <div class="grid ${noAside ? 'no-aside' : ''}">
    <div class="slot"></div>
    ${noAside ? '' : `<aside class="rail" aria-label="Your progress">${asideInner()}</aside>`}
  </div>`;
  // slide-out section menu: hidden by default, so no section icons or names show until the toggle is pressed
  const drawerHTML = `<div class="scrim" id="scrim" hidden></div>
  <nav class="drawer" id="drawer" aria-label="Sections" aria-hidden="true">
    <div class="drawer-h"><b>Sections</b><button class="drawer-x" id="drawerX" aria-label="Close sections menu" tabindex="-1">${ic('x')}</button></div>
    <ul>${NAV.map(([h, i, t]) => {
      const on = h === 'lessons.html' ? file === 'lessons.html' : h === page;
      const parent = h === 'lessons.html' && inLessons && !on ? ' in' : '';
      const sub = h === 'lessons.html' ? `<ul class="sub" aria-label="Lesson levels">${LEVELS.map(l => `<li><a href="${l.page}" class="${l.id === curLevel ? 'on' : ''}" ${l.id === curLevel ? (lvPage ? 'aria-current="page"' : 'aria-current="true"') : ''} tabindex="-1">${ic(l.icon)}<span>${l.name}</span></a></li>`).join('')}</ul>` : '';
      return `<li><a href="${h}" class="${on ? 'on' : ''}${parent}" ${on ? 'aria-current="page"' : ''} tabindex="-1">${ic(i)}<span>${t}</span></a>${sub}</li>`;
    }).join('')}</ul>
    <ul class="drawer-f"><li><a href="settings.html" class="${page === 'settings.html' ? 'on' : ''}" ${page === 'settings.html' ? 'aria-current="page"' : ''} tabindex="-1">${ic('sliders')}<span>Settings</span></a></li>
      <li><button id="navKb" tabindex="-1">${ic('keyboard')}<span>Quick find</span><kbd>⌘K</kbd></button></li></ul>
  </nav>`;
  app.insertAdjacentHTML('beforeend', drawerHTML);
  app.insertAdjacentHTML('beforeend', `<footer class="sig"><a class="avatar" href="https://yousefammmar.github.io/profile" target="_blank" rel="noopener" aria-label="Yousef Odeh - open portfolio" title="Yousef Odeh - portfolio"><img src="img/avatar.png" width="72" height="72" alt=""></a><span>Designed &amp; built by</span><b class="sign">Yousef Odeh</b>${ic('code')}</footer>`);
  main.tabIndex = -1;
  app.querySelector('.slot').replaceWith(main);
  document.body.append(app);

  // command palette
  const items = [
    ...NAV.map(([h, i, t]) => ({ t, s: 'Page', i, h, k: '' })),
    ...LEVELS.map(l => ({ t: `${l.name} lessons`, s: 'Level', i: l.icon, h: l.page, k: l.desc })),
    ...LEVELS.flatMap(l => l.groups.flatMap(g => g.topics.filter(t => t[2]).map(t => ({ t: t[0], s: `${l.name} topic`, i: 'book', h: `lesson.html?id=${t[2]}`, k: t[3] || g.name })))),
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
  $('#openPalette').onclick = open;
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
  // drawer open/close
  const drawer = $('#drawer'), scrim = $('#scrim'), menuBtn = $('#menuBtn');
  const focusables = () => [...drawer.querySelectorAll('a,button')];
  const setDrawer = on => {
    drawer.classList.toggle('open', on); scrim.hidden = !on; menuBtn.setAttribute('aria-expanded', on); drawer.setAttribute('aria-hidden', !on);
    focusables().forEach(el => el.tabIndex = on ? 0 : -1);
    if (on) (drawer.querySelector('a.on') || focusables()[0]).focus(); else if (drawer.contains(document.activeElement)) menuBtn.focus();
  };
  menuBtn.onclick = () => setDrawer(!drawer.classList.contains('open'));
  $('#drawerX').onclick = scrim.onclick = () => setDrawer(false);
  $('#navKb').onclick = () => { setDrawer(false); open(); };
  drawer.addEventListener('keydown', e => {
    if (e.key !== 'Tab') return;
    const f = focusables(), first = f[0], last = f[f.length - 1];
    if (e.shiftKey && document.activeElement === first) { last.focus(); e.preventDefault(); } else if (!e.shiftKey && document.activeElement === last) { first.focus(); e.preventDefault(); }
  });
  addEventListener('keydown', e => { if (e.key === 'Escape' && drawer.classList.contains('open')) setDrawer(false); });
})();
