/* Tools page: labs mount themselves via data-mount; this just honours deep links. */
if (location.hash) setTimeout(() => { const t = document.getElementById(location.hash.slice(1)); if (t) { t.scrollIntoView({ block: 'start' }); t.classList.add('flash'); } }, 50);
