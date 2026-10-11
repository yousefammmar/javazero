/* Settings: badges and reset. */
{
  const root = $('#root'), render = () => {
    const earned = BADGES.filter(b => b.test()).length;
    root.innerHTML = `<div class="mh"><div><h1>Settings</h1><p>Your progress is stored only in this browser.</p></div></div>
      <section class="panel" id="badges"><h2>Badges <span class="cnt-s">${earned} / ${BADGES.length}</span></h2><div class="bgrid">${BADGES.map(b => `<div class="${b.test() ? '' : 'lock'}"><i>${ic(b.icon)}</i><b>${b.t}</b><small>${b.hint}</small></div>`).join('')}</div></section>
      <section class="panel"><h2>Progress</h2><p class="mut">Reset lessons, quiz score, badges and tool history.</p><button class="btn b3 danger" id="rs">${ic('reset')} Reset all progress</button></section>`;
    $('#rs').onclick = () => { if (confirm('Reset all JavaZero progress on this device?')) { Store.del(); location.reload(); } };
  };
  render();
}
