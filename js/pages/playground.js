/* Playground: edit and run Java (teaching subset) with friendly errors. */
{
  const T = {
    Hello: `public class Main {\n    public static void main(String[] args) {\n        System.out.println("Hello, Java!");\n    }\n}`,
    Loop: `public class Main {\n    public static void main(String[] args) {\n        int sum = 0;\n        for (int i = 1; i <= 5; i++) {\n            sum += i;\n            System.out.println("i = " + i + ", sum = " + sum);\n        }\n    }\n}`,
    'Class & object': MODULES[2].lessons[4].code,
    Inheritance: MODULES[3].lessons[1].code,
    Interface: MODULES[4].lessons[3].code,
    'Syntax error': `public class Main {\n    public static void main(String[] args) {\n        int x = 5\n        System.out.println(x);\n    }\n}`,
  };
  const draft = Store.get('draft', null); if (draft) { T['From lesson'] = draft; Store.set('draft', null); }
  const ed = $('#ed'), gut = $('#gut'), out = $('#pout'), sel = $('#tpl');
  sel.innerHTML = Object.keys(T).map(k => `<option>${k}</option>`).join(''); sel.value = draft ? 'From lesson' : 'Hello';
  const lines = () => { gut.textContent = Array.from({ length: ed.value.split('\n').length }, (_, i) => i + 1).join('\n'); gut.scrollTop = ed.scrollTop; };
  const set = k => { ed.value = T[k]; lines(); out.innerHTML = '<span class="mut2">Press Run (or Ctrl/Cmd + Enter) to compile and execute.</span>'; };
  const translate = msg => COMPILER_ERRORS.find(x => msg.includes("';'") ? x.e.includes("';'") : msg.includes('cannot find symbol') ? x.e.includes('cannot find') : false);
  const fixLine = (s, n) => n ? s.replace(/line 14/g, 'line ' + (n + 1)).replace(/line 13/g, 'line ' + n) : s;
  const run = () => {
    const r = Java.run(ed.value); let h = '';
    if (r.err && r.err.kind === 'compile') {
      h = r.err.list.map(e => { const t = translate(e.msg); return `<div class="er"><span class="e">error${e.line ? ` (line ${e.line})` : ''}: ${esc(e.msg)}</span>${t ? `<div class="tr">${ic('bulb')} ${fixLine(t.say, e.line)}<br><b>${fixLine(t.fix, e.line)}</b></div>` : ''}</div>`; }).join('') + '<div class="st bad">Build failed</div>';
    } else {
      h = r.out.map(l => `<div>${esc(l) || '&nbsp;'}</div>`).join('');
      if (r.err) { const ex = EXCEPTIONS.find(x => r.err.text.includes(x.n)); h += `<div class="er"><span class="e">Exception in thread "main" ${esc(r.err.text)}</span>${ex ? `<div class="tr">${ic('bulb')} ${ex.say}<br><b>Fix: ${ex.fix}</b></div>` : ''}</div><div class="st bad">Exited with error</div>`; }
      else h += '<div class="st ok">Process finished with exit code 0</div>';
    }
    out.innerHTML = h; Store.set('ran', Store.get('ran', 0) + 1);
  };
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
