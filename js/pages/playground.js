/* Playground: edit and run Java (teaching subset) with friendly errors. */
{
  const LES = id => MODULES.flatMap(m => m.lessons).find(l => l.id === id).code;
  const T = {
    Hello: `public class Main {\n    public static void main(String[] args) {\n        System.out.println("Hello, Java!");\n    }\n}`,
    Loop: `public class Main {\n    public static void main(String[] args) {\n        int sum = 0;\n        for (int i = 1; i <= 5; i++) {\n            sum += i;\n            System.out.println("i = " + i + ", sum = " + sum);\n        }\n    }\n}`,
    'Class & object': MODULES[2].lessons[4].code,
    Inheritance: MODULES[3].lessons[1].code,
    Interface: MODULES[4].lessons[3].code,
    'Exceptions: try / finally': LES('exceptions-2'),
    'Custom exception': LES('exceptions-5'),
    'Enum + switch': LES('abstraction-4'),
    'HashMap word count': `import java.util.*;\n\npublic class Main {\n    public static void main(String[] args) {\n        String text = "the quick brown fox jumps over the lazy dog the end";\n        Map<String, Integer> counts = new TreeMap<>();\n        for (String word : text.split(" ")) {\n            counts.merge(word, 1, Integer::sum);\n        }\n        for (Map.Entry<String, Integer> e : counts.entrySet()) {\n            System.out.println(e.getKey() + " -> " + e.getValue());\n        }\n    }\n}`,
    'Sorting with a Comparator': LES('collections-7'),
    'Lambdas & functional interfaces': LES('lambdas-4'),
    'Read input (name & age)': `import java.util.Scanner;\n\npublic class Main {\n    public static void main(String[] args) {\n        Scanner sc = new Scanner(System.in);\n        System.out.print("What is your name? ");\n        String name = sc.nextLine();\n        System.out.print("How old are you? ");\n        int age = sc.nextInt();\n        System.out.println("Hi " + name + ", next year you will be " + (age + 1) + ".");\n    }\n}`,
    'Sum of numbers': `import java.util.Scanner;\n\npublic class Main {\n    public static void main(String[] args) {\n        Scanner sc = new Scanner(System.in);\n        System.out.print("How many numbers? ");\n        int n = sc.nextInt();\n        int sum = 0;\n        for (int i = 1; i <= n; i++) {\n            System.out.print("Number " + i + ": ");\n            sum += sc.nextInt();\n        }\n        System.out.println("Sum = " + sum);\n    }\n}`,
    'nextLine pitfall': `import java.util.Scanner;\n\npublic class Main {\n    public static void main(String[] args) {\n        Scanner sc = new Scanner(System.in);\n        System.out.print("Age: ");\n        int age = sc.nextInt();\n        // nextInt() leaves the Enter key behind, so this reads an empty line.\n        // Add sc.nextLine(); before it to fix.\n        System.out.print("Name: ");\n        String name = sc.nextLine();\n        System.out.println("[" + name + "] is " + age);\n    }\n}`,
    'Syntax error': `public class Main {\n    public static void main(String[] args) {\n        int x = 5\n        System.out.println(x);\n    }\n}`,
  };
  const draft = Store.get('draft', null); if (draft) { T['From lesson'] = draft; Store.set('draft', null); }
  const ed = $('#ed'), gut = $('#gut'), out = $('#pout'), sel = $('#tpl');
  sel.innerHTML = Object.keys(T).map(k => `<option>${k}</option>`).join(''); sel.value = draft ? 'From lesson' : 'Hello';
  const lines = () => { gut.textContent = Array.from({ length: ed.value.split('\n').length }, (_, i) => i + 1).join('\n'); gut.scrollTop = ed.scrollTop; };
  const set = k => { ed.value = T[k]; lines(); out.innerHTML = '<span class="mut2">Press Run (or Ctrl/Cmd + Enter) to compile and execute.</span>'; };
  const translate = msg => COMPILER_ERRORS.find(x => x.re && x.re.test(msg));
  const fixLine = (s, n) => n ? s.replace(/line 14/g, 'line ' + (n + 1)).replace(/line 13/g, 'line ' + n) : s;
  // console input: lines pre-typed in the Input box, plus anything typed at the prompt (replayed on each step)
  let typed = [];
  const prefill = () => { const v = $('#stdin').value; return v ? v.replace(/\n$/, '').split('\n') : []; };
  const exec = () => {
    const r = Java.run(ed.value, { stdin: [...prefill(), ...typed], interactive: true }); let h = '';
    if (r.err && r.err.kind === 'compile') {
      h = r.err.list.map(e => { const t = translate(e.msg); return `<div class="er"><span class="e">error${e.line ? ` (line ${e.line})` : ''}: ${esc(e.msg)}</span>${t ? `<div class="tr">${ic('bulb')} ${fixLine(t.say, e.line)}<br><b>${fixLine(t.fix, e.line)}</b></div>` : ''}</div>`; }).join('') + '<div class="st bad">Build failed</div>';
    } else {
      h = r.out.map(l => `<div>${esc(l) || '&nbsp;'}</div>`).join('');
      if (r.needInput) h += `<form class="inrow" id="inform"><span class="pr">${esc(r.prompt)}</span><input id="inin" autocomplete="off" spellcheck="false" aria-label="Program input"></form><div class="st wait">Waiting for input: type a value and press Enter</div>`;
      else if (r.err) { const ex = EXCEPTIONS.find(x => r.err.text.includes(x.n)); h += `<div class="er"><span class="e">Exception in thread "main" ${esc(r.err.text)}</span>${ex ? `<div class="tr">${ic('bulb')} ${ex.say}<br><b>Fix: ${ex.fix}</b></div>` : ''}</div><div class="st bad">Exited with error</div>`; }
      else h += '<div class="st ok">Process finished with exit code 0</div>';
    }
    out.innerHTML = h;
    const f = $('#inform');
    if (f) { const i = $('#inin'); i.focus(); f.onsubmit = e => { e.preventDefault(); typed.push(i.value); exec(); }; }
    out.scrollTop = out.scrollHeight;
    return r;
  };
  const run = () => { typed = []; exec(); Store.set('ran', Store.get('ran', 0) + 1); Activity.touch(); };
  ed.oninput = lines; ed.onscroll = () => gut.scrollTop = ed.scrollTop;
  ed.onkeydown = e => {
    if ((e.metaKey || e.ctrlKey) && e.key === 'Enter') { e.preventDefault(); run(); }
    if (e.key === 'Tab') { e.preventDefault(); const s = ed.selectionStart; ed.setRangeText('    ', s, ed.selectionEnd, 'end'); lines(); }
  };
  sel.onchange = () => set(sel.value);
  $('#prun').onclick = run;
  $('#preset').onclick = () => set(sel.value);
  $('#pcopy').onclick = async e => { try { await navigator.clipboard.writeText(ed.value); e.currentTarget.lastChild.textContent = ' Copied'; setTimeout(() => e.currentTarget.lastChild.textContent = ' Copy', 1200); } catch {} };
  set(sel.value);
}
