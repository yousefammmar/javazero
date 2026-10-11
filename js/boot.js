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
  const page = document.currentScript.dataset.page;
  const common = ['core/icons', 'core/store', 'data/curriculum', 'data/mistakes', 'data/errors', 'data/quiz', 'data/resources', 'data/badges', 'core/java', 'core/hl', 'core/shell'];
  const extra = { index: ['components/module-cards', 'components/labs'], tools: ['components/labs'] }[page] || [];
  [...common, ...extra, 'pages/' + page].forEach(f => document.write(`<script src="js/${f}.js"><\/script>`));
})();
