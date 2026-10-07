/* Dashboard cards: one per curriculum module plus lab cards, with small interactive visuals. */
const card = ({ cls = '', c = '', icon, tag, meta, title, desc, body = '', foot, tagStyle = '', icoStyle = '', metaStyle = '' }) => `
<article class="card ${cls}" data-c="${c}">
  <div class="ch"><div class="ico" style="${icoStyle}">${ic(icon)}</div><span class="bd" style="${tagStyle}">${tag}</span><span class="m" style="${metaStyle}">${meta}</span></div>
  <h3>${title}</h3><p>${desc}</p>${body}<div class="foot">${foot}</div></article>`;

const VISUALS = {
  vars: () => `<div class="box"><div class="code row"><span class="red">int</span><span>age</span><span>=</span><span class="pur">21</span><span>;</span></div><span class="note">[Type: 32-bit] [Identifier] [Assign] [Literal] [Semicolon]</span></div>`,
  methods: () => `<div class="box"><div class="code">public static <span class="red">int</span> <span class="grn">add</span> ( <span class="red">int</span> a, <span class="red">int</span> b)</div>
    <div><span class="sm"><b class="red">int</b>: Return output</span><span class="sm"><b class="grn">add</b>: Subroutine identifier</span><span class="sm"><b class="pur">(a, b)</b>: Formal parameters</span></div></div>`,
  classes: () => `<div class="box"><div class="row"><b>HEAP MEMORY INSPECTOR</b><b class="grn">Address: @0x7f4b</b></div>
    <div class="code">Car myCar = <span class="red">new</span> Car(<span class="grn">"Toyota"</span>, <span class="pur">2025</span>);</div>
    <div><button class="sm grn mint" data-car="20">myCar.accelerate(+20)</button><button class="sm" data-car="-20">myCar.brake()</button></div>
    <div class="note right" data-speed>Speed: 0 mph (Parked)</div></div>`,
  inherit: () => `<div class="box"><div class="inh"><span>${ic('paw')} Animal (Superclass)</span><span class="fw4">eat(), sleep()</span></div><div class="ext">${ic('down')} EXTENDS ${ic('down')}</div><div class="inh sub"><span>${ic('subright')} Dog (Subclass)</span><span class="fw4">bark(), super.eat()</span></div></div>`,
  interfaces: () => `<div class="box"><div class="two"><div><b class="red">extends (Class)</b>IS-A relationship<code>class Dog extends Animal</code></div><div><b class="grn">implements (Interface)</b>CAN-DO contract<code>implements Drivable, GPS</code></div></div></div>`,
};
const CARD_FOOT = {
  vars: m => { const p = Progress.forModule(m); return [`<div><div class="row"><span class="mut">Track Mastery</span><b class="red">${p.pct}%</b></div>${pctBar(p.pct, 'var(--coral)')}</div>`, `<span>${ic('checkc')} ${p.n} of ${p.total} Completed</span><a class="mini c" href="lesson.html?id=${(m.lessons.find(l => !Progress.has(l.id)) || m.lessons[0]).id}">${p.n ? 'Resume Lesson' : 'Start Lesson'}</a>`]; },
  methods: m => { const p = Progress.forModule(m); return [`<div><div class="row"><span class="mut">Track Mastery</span><b class="pur">${p.pct}%</b></div>${pctBar(p.pct, 'var(--purple)')}</div>`, `<span>${ic('dots')} ${p.n} of ${p.total} Completed</span><a class="mini" href="lesson.html?id=${(m.lessons.find(l => !Progress.has(l.id)) || m.lessons[0]).id}">Practice Methods</a>`]; },
  classes: m => ['', `<span>Next: Constructor Overloading</span><a class="mini c" href="lesson.html?id=${(m.lessons.find(l => !Progress.has(l.id)) || m.lessons[0]).id}">Explore OOP Lab</a>`],
  inherit: m => ['', `<span>Prerequisite: Classes &amp; Objects</span><a class="mini" href="lesson.html?id=${m.lessons[0].id}">View Diagram</a>`],
  interfaces: m => ['', `<span>${m.lessons.length} Interactive Contracts</span><a class="mini" href="lesson.html?id=${m.lessons[0].id}">Inspect Contracts</a>`],
};
function moduleCard(m) {
  const [extra, foot] = CARD_FOOT[m.id](m);
  return card({ c: m.track, icon: m.icon, tag: m.tag, meta: `${m.lessons.length} lessons • ${m.min} min`, title: m.name, desc: m.desc.replace(' new.', ' <span class="red">new</span>.').replace('what an object', '<i>what</i> an object'), body: VISUALS[m.id]() + extra, foot });
}
function labCards() {
  const mi = MISTAKES[0];
  return [
    card({ c: 'lab', icon: 'bug', tag: 'Mistake of the Day', meta: 'Common Pitfall #01', title: mi.t, desc: 'In Java, <b class="red">==</b> checks whether two variables share memory addresses, not string text!',
      icoStyle: 'background:#ffe3e0;color:var(--red)', tagStyle: 'background:#ffe3e0;color:var(--red)', metaStyle: 'color:var(--red);font-weight:700',
      body: `<div class="box" data-mistake><div class="bug"><button class="a on" data-m="bad">Buggy Code</button><button class="b" data-m="good">Corrected Fix</button><span class="red">Memory pointer mismatch</span></div>
        <pre class="code bugcode red">${mi.bad}</pre></div>`,
      foot: `<span>3,492 learners debugged this today</span><a class="mini r" href="mistakes.html#${mi.id}">Open Mistake Lab</a>` }),
    card({ c: 'lab', icon: 'terminal', tag: 'Debugging Lab', meta: 'Stack Trace Anatomy', title: 'Interpreting Stack Traces (NPE)', desc: 'Read stack frames backwards to uncover exact lines where null references caused failures.',
      icoStyle: 'background:#ffe3e0;color:var(--red)', tagStyle: 'background:#ffe3e0;color:var(--red)',
      body: `<div class="nl"><div class="e">Exception in thread "main" java.lang.NullPointerException</div><div class="fr2">at com.javazero.User.getName(<span class="g">User.java:24</span>)<br>at com.javazero.App.main(<span class="g">App.java:12</span>)</div><div class="fx"><b class="g">Fix:</b> Initialize \`User user = new User()\` before calling methods!</div></div>`,
      foot: `<span>Interactive Stack Analyzer</span><a class="mini" href="tools.html#stack">Deconstruct Trace</a>` }),
    card({ c: 'oop', icon: 'folder', tag: 'Project Structure', meta: 'Package &amp; Imports', title: 'Separating Classes Across Files', desc: "Real Java code doesn't live in one file. Learn clean package hierarchies in `src/com/company/`.",
      icoStyle: 'background:var(--lav);color:var(--purple)', tagStyle: 'background:var(--lav);color:#5a3fb5',
      body: `<div class="box tree"><span>${ic('folder')} src/main/java</span><span class="ind">${ic('file')} Main.java (entrypoint)</span><span class="ind">${ic('file')} Student.java (blueprint)</span><span class="ind">${ic('file')} Course.java (collection)</span><a class="open" href="tools.html#ide">Open in IDE ${ic('upright')}</a></div>`,
      foot: `<span>Standard Maven / Gradle conventions</span><a class="mini" href="tools.html#ide">Try Multi-File IDE</a>` }),
  ];
}
function bindCards(root = document) {
  let sp = 0;
  root.querySelectorAll('[data-car]').forEach(b => b.onclick = () => { sp = Math.max(0, sp + +b.dataset.car); root.querySelector('[data-speed]').textContent = `Speed: ${sp} mph (${sp ? 'Moving' : 'Parked'})`; });
  root.querySelectorAll('[data-mistake]').forEach(box => box.querySelectorAll('[data-m]').forEach(b => b.onclick = () => {
    const bad = b.dataset.m === 'bad', mi = MISTAKES[0];
    box.querySelectorAll('[data-m]').forEach(x => x.classList.toggle('on', x === b));
    const pre = box.querySelector('pre'); pre.textContent = bad ? mi.bad : mi.good; pre.classList.toggle('red', bad); pre.classList.toggle('grn', !bad);
    box.querySelector('.bug > span').textContent = bad ? 'Memory pointer mismatch' : 'Compares the text'; box.querySelector('.bug > span').className = bad ? 'red' : 'grn';
  }));
}
