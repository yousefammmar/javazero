/* Dashboard */
{
  const mods = $('#mods');
  mods.innerHTML = MODULES.map(moduleCard).join('') + labCards().join('');
  bindCards(mods);

  const eli = $('#eli'), lead = $('#lead');
  const text = { on: 'Understand the architectural mental models behind bytecodes and pointers, not just empty rote syntax. Built deliberately for future engineers.', off: 'JVM bytecode, stack/heap layout and OOP design patterns for developers coming from other languages.' };
  const setEli = on => { eli.setAttribute('aria-pressed', on); eli.querySelector('.sw').classList.toggle('off', !on); lead.textContent = on ? text.on : text.off; Store.set('eli', on); };
  setEli(Store.get('eli', true));
  eli.onclick = () => setEli(eli.getAttribute('aria-pressed') !== 'true');

  $('#filters').onclick = e => {
    const f = e.target.dataset.f; if (!f) return;
    $$('#filters button').forEach(b => b.classList.toggle('on', b === e.target));
    $$('#mods .card').forEach(c => c.classList.toggle('hide', f !== 'all' && c.dataset.c !== f));
  };

  $('#runBtn').onclick = () => {
    const r = Java.run($('#heroSrc').textContent);
    $('#consoleOut').innerHTML = r.err ? `<span class="e">${esc(r.err.list ? r.err.list[0].msg : r.err.text)}</span>` : r.out.map(esc).join('<br>');
    Store.set('ran', Store.get('ran', 0) + 1);
  };
  $('#modCount').textContent = `${MODULES.length + LABS.length} modules active`;
}
