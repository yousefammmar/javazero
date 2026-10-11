/* JavaZero - designed and built by Yousef Odeh */
/* Loads shared scripts in order, then this page's script. Usage: <script src="js/boot.js" data-page="lessons"></script> */
(() => {
  /* Visitor counting via GoatCounter (cookie-free). Set your site code, e.g. 'javazero' for javazero.goatcounter.com. Empty = disabled. */
  const GOATCOUNTER = '';
  if (GOATCOUNTER && !/^(localhost|127\.|\[::1\])/.test(location.hostname) && location.protocol !== 'file:') {
    const g = document.createElement('script');
    g.async = true;
    g.src = 'https://gc.zgo.at/count.js';
    g.dataset.goatcounter = `https://${GOATCOUNTER}.goatcounter.com/count`;
    document.head.appendChild(g);
  }
  const page = document.currentScript.dataset.page;
  const common = ['core/icons', 'core/store', 'data/curriculum', 'data/mistakes', 'data/errors', 'data/quiz', 'data/resources', 'data/badges', 'core/java', 'core/hl', 'core/shell'];
  const extra = { index: ['components/module-cards', 'components/labs'], tools: ['components/labs'] }[page] || [];
  [...common, ...extra, 'pages/' + page].forEach(f => document.write(`<script src="js/${f}.js"><\/script>`));
})();
