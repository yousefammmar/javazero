/* JavaZero - designed and built by Yousef Odeh */
/* Loads shared scripts in order, then this page's script. Usage: <script src="js/boot.js" data-page="lessons"></script> */
(() => {
  /* Visitor counting via Google Analytics 4. Set your Measurement ID, e.g. 'G-ABC123XYZ9' (GA4 > Admin > Data streams). Empty = disabled. */
  const GA_ID = 'G-T9VHKMPTLR';
  if (/^G-[A-Z0-9]+$/.test(GA_ID) && !/^(localhost|127\.|\[::1\])/.test(location.hostname) && location.protocol !== 'file:') {
    const g = document.createElement('script');
    g.async = true;
    g.src = `https://www.googletagmanager.com/gtag/js?id=${GA_ID}`;
    document.head.appendChild(g);
    window.dataLayer = window.dataLayer || [];
    window.gtag = function () { window.dataLayer.push(arguments); };
    gtag('js', new Date());
    gtag('config', GA_ID);
  }
  if (!document.querySelector('link[rel~="icon"]')) {
    const l = document.createElement('link');
    l.rel = 'icon';
    l.href = "data:image/svg+xml,%3Csvg xmlns='http://www.w3.org/2000/svg' viewBox='0 0 32 32'%3E%3Crect width='32' height='32' rx='7' fill='%23f89820'/%3E%3Ctext x='16' y='23' font-size='20' font-family='monospace' font-weight='bold' text-anchor='middle' fill='%23fff'%3EJ%3C/text%3E%3C/svg%3E";
    document.head.appendChild(l);
  }
  const page = document.currentScript.dataset.page;
  const common = ['core/icons', 'core/store', 'data/curriculum', 'data/mistakes', 'data/errors', 'data/quiz', 'data/resources', 'data/basics-flow', 'data/intermediate', 'data/advanced', 'data/levels', 'data/badges',
    'core/java-parse', 'core/java-rt', 'core/java-coll', 'core/java-gen', 'core/java-gen-expr', 'core/java-gen-call', 'core/java-flow', 'core/java', 'core/hl', 'core/shell'];
  const extra = { index: ['components/module-cards', 'components/labs'], tools: ['components/labs'], level: ['components/module-list'] }[page] || [];
  [...common, ...extra, 'pages/' + page].forEach(f => document.write(`<script src="js/${f}.js"><\/script>`));
})();
