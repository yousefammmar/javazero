/* Settings: badges and reset. */
{
  const root = $('#root'), render = () => {
    const earned = BADGES.filter(b => b.test()).length;
    root.innerHTML = `<div class="mh"><div><h2>Settings</h2><p>Your progress is stored only in this browser.</p></div></div>
      <section class="panel" id="badges"><h3>Badges <span class="cnt-s">${earned} / ${BADGES.length}</span></h3><div class="bgrid">${BADGES.map(b => `<div class="${b.test() ? '' : 'lock'}"><i>${ic(b.icon)}</i><b>${b.t}</b><small>${b.hint}</small></div>`).join('')}</div></section>
      <section class="panel"><h3>Progress</h3><p class="mut">Reset lessons, quiz score, badges and tool history.</p><button class="btn b3 danger" id="rs">${ic('reset')} Reset all progress</button></section>`;
    $('#rs').onclick = () => { if (confirm('Reset all JavaZero progress on this device?')) { Store.del(); location.reload(); } };
  };
  render();
}
