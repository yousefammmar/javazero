/* Interactive labs. Mount with <div data-mount="string|errors|stack|ide"></div>. */
const seenTool = n => { const s = new Set(Store.get('toolsSeen', [])); s.add(n); Store.set('toolsSeen', [...s]); };

const Labs = {
  string(el) {
    const ops = [['up', '.toUpperCase()', s => `"${s.toUpperCase()}"`], ['low', '.toLowerCase()', s => `"${s.toLowerCase()}"`], ['len', '.length()', s => s.length], ['sub', '.substring(0, 6)', s => `"${s.substring(0, 6)}"`], ['rep', '.replace("e", "3")', s => `"${s.replaceAll('e', '3')}"`], ['rev', 'reverse', s => `"${[...s].reverse().join('')}"`], ['trim', '.trim()', s => `"${s.trim()}"`]];
    let cur = ops[0];
    el.innerHTML = `<article class="card" id="string"><div class="ch"><div class="ico">${ic('type')}</div><h3 class="inl">Interactive String Lab</h3><span class="bd">Live Sandbox</span></div>
      <p>Strings are immutable sequences in Java. Test standard method calls instantly without restarting a compiler.</p>
      <label class="lbl" for="sl">String str =</label><input class="in" id="sl" value="CoffeeBeans" autocomplete="off">
      <div class="meth">${ops.map(o => `<button data-m="${o[0]}" class="${o === cur ? 'on' : ''}">${o[1]}</button>`).join('')}</div>
      <div class="ret">RETURN VALUE (JVM OUTPUT)<b></b></div></article>`;
    const out = el.querySelector('.ret b'), inp = el.querySelector('input'), upd = () => out.textContent = cur[2](inp.value);
    el.querySelector('.meth').onclick = e => { const o = ops.find(x => x[0] === e.target.dataset.m); if (!o) return; cur = o; el.querySelectorAll('.meth button').forEach(b => b.classList.toggle('on', b === e.target)); upd(); seenTool('string'); };
    inp.oninput = upd; upd();
  },

  errors(el) {
    el.innerHTML = `<article class="card" id="errors"><div class="ch"><div class="ico plain">${ic('lang')}</div><h3 class="inl">Compiler Error Translator</h3><span class="bd red-bd">Friendly Decoder</span></div>
      <p>Don't freeze when javac throws errors. Choose scary compiler output to see plain-English explanations.</p>
      <label class="lbl" for="ce">Select cryptic compiler error:</label>
      <select class="in" id="ce">${COMPILER_ERRORS.map((x, i) => `<option value="${i}">${esc(x.e)}</option>`).join('')}</select>
      <div class="box soft"><b class="grn tl">${ic('bulb')} Plain English Translation:</b><div class="say"></div><div class="code qf grn"></div></div></article>`;
    const sel = el.querySelector('select'), upd = () => { const x = COMPILER_ERRORS[sel.value]; el.querySelector('.say').textContent = x.say; el.querySelector('.qf').textContent = x.fix; seenTool('errors'); };
    sel.onchange = upd; upd();
  },

  stack(el) {
    el.innerHTML = `<article class="card" id="stack"><div class="ch"><div class="ico red-ico">${ic('terminal')}</div><h3 class="inl">Stack Trace Analyzer</h3><span class="bd red-bd">Debugging Lab</span></div>
      <p>Pick an exception, then click a frame. Read from the top: the first frame in your own code is where to look.</p>
      <select class="in" aria-label="Exception">${EXCEPTIONS.map((x, i) => `<option value="${i}">${x.n}</option>`).join('')}</select>
      <div class="nl"><div class="e"></div><div class="frames"></div></div>
      <div class="box soft"><b class="grn tl">${ic('bulb')} What it means</b><div class="say"></div><div class="code grn fixl"></div></div></article>`;
    const sel = el.querySelector('select');
    const upd = () => {
      const x = EXCEPTIONS[sel.value];
      el.querySelector('.e').textContent = `Exception in thread "main" java.lang.${x.n}`;
      el.querySelector('.frames').innerHTML = x.frames.map((f, i) => `<button class="frame" data-i="${i}">at ${esc(f)}</button>`).join('');
      el.querySelector('.say').textContent = x.say; el.querySelector('.fixl').textContent = 'Fix: ' + x.fix; seenTool('stack');
    };
    el.querySelector('.frames').onclick = e => { const b = e.target.closest('.frame'); if (!b) return; el.querySelectorAll('.frame').forEach(f => f.classList.toggle('on', f === b)); el.querySelector('.say').textContent = b.dataset.i === '0' ? 'This is the frame closest to the crash: start here. ' + EXCEPTIONS[sel.value].say : 'This frame called the one above it. Frames read like a chain of who-called-who, newest first.'; };
    sel.onchange = upd; upd();
  },

  ide(el) {
    const files = {
      Main: `public class Main {\n    public static void main(String[] args) {\n        // Instantiate real object in heap memory\n        Student student = new Student("Alex Mercer", 3.9);\n        Course course = new Course("JavaZero 101", 4);\n\n        student.enroll(course);\n        System.out.println(student.getSummary());\n    }\n}`,
      Student: `public class Student {\n    private String name;\n    private double gpa;\n    private int credits = 8;\n    private String enrolled;\n\n    public Student(String name, double gpa) {\n        this.name = name;\n        this.gpa = gpa;\n    }\n\n    public void enroll(Course c) {\n        credits += c.getCredits();\n        enrolled = c.getTitle();\n    }\n\n    public String getSummary() {\n        return "Student created: " + name + " (GPA: " + gpa + ") enrolled in " + enrolled + ".";\n    }\n}`,
      Course: `public class Course {\n    private String title;\n    private int credits;\n\n    public Course(String title, int credits) {\n        this.title = title;\n        this.credits = credits;\n    }\n\n    public int getCredits() { return credits; }\n    public String getTitle() { return title; }\n}`,
    };
    const icons = { Main: 'code', Student: 'file', Course: 'branch' };
    let cur = 'Main';
    el.innerHTML = `<section class="ide" id="ide"><div class="ide-h"><span class="tag sm">Multi-file sandbox</span><div><h3>Interactive Multi-File Java IDE</h3><p>Switch between source files to observe how <code>Student.java</code> interacts with <code>Main.java</code> in heap memory. Edit any file, then build.</p></div><button class="build">${ic('play')} BUILD &amp; RUN</button></div>
      <div class="ide-b"><div class="ed"><div class="tabs" role="tablist">${Object.keys(files).map(f => `<button role="tab" data-t="${f}" class="${f === cur ? 'on' : ''}">${ic(icons[f])} ${f}.java</button>`).join('')}</div>
        <textarea spellcheck="false" aria-label="Source code"></textarea>
        <div class="out"><small>OUTPUT TERMINAL</small><div class="oc"></div></div></div>
        <div class="mem"><h4>Live JVM Memory Model <span>Synchronized</span></h4>
          <div class="fr"><b>STACK FRAME: MAIN()</b><span class="red">student</span> ${ic('right', 'inl-i')} <span class="pur">@0x4a9b</span> (heap pointer)</div>
          <div class="fr h"><b class="pur">HEAP OBJECT: @0X4A9B</b>name: <span class="grn m-name"></span><br>gpa: <span class="red m-gpa"></span><br>credits: <b class="m-cr"></b></div>
          <button class="mut">${ic('trend')} Mutate Object in Heap</button></div></div></section>`;
    const ta = el.querySelector('textarea'), oc = el.querySelector('.oc');
    const load = () => { ta.value = files[cur]; el.querySelectorAll('.tabs button').forEach(b => b.classList.toggle('on', b.dataset.t === cur)); };
    const memory = () => {
      const m = files.Main, s = files.Student;
      const name = (m.match(/new Student\("([^"]*)"/) || [])[1] || '?', gpa = (m.match(/new Student\("[^"]*",\s*([\d.]+)/) || [])[1] || '?';
      const base = +(s.match(/credits\s*=\s*(\d+);/) || [0, 0])[1], add = +(m.match(/new Course\("[^"]*",\s*(\d+)/) || [0, 0])[1];
      el.querySelector('.m-name').textContent = `"${name}"`; el.querySelector('.m-gpa').textContent = gpa; el.querySelector('.m-cr').textContent = base + add;
    };
    const build = () => {
      files[cur] = ta.value;
      const r = Java.run([files.Course, files.Student, files.Main].join('\n\n'));
      oc.innerHTML = '[javac] Compiling 3 source files...' + (r.err ? (r.err.kind === 'compile' ? r.err.list.map(e => `<br><span class="e">error: ${esc(e.msg)}${e.line ? ` (line ${e.line})` : ''}</span>`).join('') : `<br><span class="e">Exception in thread "main" ${esc(r.err.text)}</span>`) : r.out.map(l => `<br>[java] ${esc(l)}`).join(''));
      memory(); seenTool('ide');
    };
    el.querySelector('.tabs').onclick = e => { const b = e.target.closest('[data-t]'); if (!b) return; files[cur] = ta.value; cur = b.dataset.t; load(); };
    el.querySelector('.build').onclick = build;
    el.querySelector('.mut').onclick = () => {
      files[cur] = ta.value;
      files.Main = files.Main.replace(/(new Student\("[^"]*",\s*)([\d.]+)/, (_, a, g) => a + Math.min(4, +(+g + 0.05).toFixed(2)));
      files.Student = files.Student.replace(/(credits\s*=\s*)(\d+);/, (_, a, n) => `${a}${+n + 4};`);
      load(); build();
    };
    load(); build();
  },
};
document.querySelectorAll('[data-mount]').forEach(el => Labs[el.dataset.mount] && Labs[el.dataset.mount](el));
