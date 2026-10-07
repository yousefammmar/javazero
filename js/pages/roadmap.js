/* Roadmap: the Learn / See / Try / Break / Build mastery path applied to every module. */
{
  const STAGES = [['book', 'Learn', 'Read a short lesson with one idea.', 'var(--mint)', '#14573c'], ['eye', 'See', 'Watch the code run and inspect memory.', 'var(--lav)', '#5a3fb5'], ['pencil', 'Try', 'Change it yourself in the playground.', '#ffe3df', '#b8372b'], ['bug', 'Break', 'Cause the classic mistake on purpose.', '#ffd9d9', 'var(--red)'], ['blocks', 'Build', 'Combine files into a real program.', 'var(--mint)', '#14573c']];
  const ov = Progress.overall(), pct = Math.round(ov.n / ov.total * 100);
  $('#root').innerHTML = `<div class="mh"><div><h2>Your Roadmap</h2><p>From first variable to multi-file programs. Every module follows the same five-step loop.</p></div><span class="cnt">${pct}% of the path</span></div>
    <div class="loop">${STAGES.map(([i, t, d, bg, fg], n) => `<div style="background:${bg};color:${fg}"><span class="sn">${n + 1}</span>${ic(i)}<b>${t}</b><small>${d}</small></div>`).join('')}</div>
    <ol class="road">${MODULES.map((m, n) => {
      const p = Progress.forModule(m), st = p.n === p.total ? 'Completed' : p.n ? 'In progress' : 'Not started', cls = p.n === p.total ? 'ok' : p.n ? 'cur' : '';
      const next = m.lessons.find(l => !Progress.has(l.id)) || m.lessons[0];
      return `<li class="${cls}"><span class="node">${p.n === p.total ? ic('check') : n + 1}</span><div class="rc"><div class="rt"><h3>${m.name}</h3><span class="bd">${st}</span></div><p>${m.desc}</p>${pctBar(p.pct, 'var(--coral)')}
        <div class="rf"><span>${p.n} of ${p.total} lessons • ${m.min} min</span><a class="mini c" href="lesson.html?id=${next.id}">${p.n === p.total ? 'Review' : p.n ? 'Resume' : 'Start'}</a></div></div></li>`;
    }).join('')}
      <li class="end"><span class="node">${ic('trophy')}</span><div class="rc"><div class="rt"><h3>Capstone: Build a multi-file program</h3><span class="bd">Final stage</span></div><p>Put it together: split a program across Main, Student and Course and watch objects live in heap memory.</p><div class="rf"><span>Break things first in the Mistake Lab</span><span><a class="mini" href="mistakes.html">Mistake Lab</a> <a class="mini c" href="tools.html#ide">Open the IDE</a></span></div></div></li></ol>`;
}
