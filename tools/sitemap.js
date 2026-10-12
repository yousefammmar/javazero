/* Regenerates sitemap.xml from the site's pages and every lesson in js/data.
   Run from the repo root after adding pages or lessons:  node tools/sitemap.js
   Then submit https://yousefammmar.github.io/javazero/sitemap.xml in Google Search Console (once is enough; Google re-reads it). */
const fs = require('fs'), vm = require('vm'), path = require('path');
const BASE = 'https://yousefammmar.github.io/javazero/';
const root = path.join(__dirname, '..');
const PAGES = ['', 'lessons.html', 'basics.html', 'intermediate.html', 'advanced.html', 'roadmap.html', 'playground.html', 'tools.html', 'mistakes.html', 'quiz.html', 'resources.html']; // settings.html is noindex
const ctx = vm.createContext({});
for (const f of ['curriculum', 'resources', 'basics-flow', 'intermediate', 'advanced'])
  vm.runInContext(fs.readFileSync(path.join(root, 'js/data', f + '.js'), 'utf8').replace(/^const (\w+) =/gm, 'var $1 ='), ctx);
const lessons = vm.runInContext('MODULES', ctx).flatMap(m => m.lessons.map(l => 'lesson.html?id=' + l.id));
const today = new Date().toISOString().slice(0, 10);
const url = (p, pr) => `  <url><loc>${BASE}${p}</loc><lastmod>${today}</lastmod><priority>${pr}</priority></url>`;
const xml = `<?xml version="1.0" encoding="UTF-8"?>\n<urlset xmlns="http://www.sitemaps.org/schemas/sitemap/0.9">\n${[
  ...PAGES.map(p => url(p, p === '' ? '1.0' : '0.8')), ...lessons.map(p => url(p, '0.6'))].join('\n')}\n</urlset>\n`;
fs.writeFileSync(path.join(root, 'sitemap.xml'), xml);
console.log(`sitemap.xml: ${PAGES.length} pages + ${lessons.length} lessons = ${PAGES.length + lessons.length} URLs`);
