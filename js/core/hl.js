/* UI helpers: HTML escape, progress bar, minimal Java syntax highlighter (returns safe HTML). */
const esc = s => String(s).replace(/[&<>]/g, c => ({ '&': '&amp;', '<': '&lt;', '>': '&gt;' }[c]));
const pctBar = (p, c) => `<div class="bar"><i style="width:${p}%;background:${c}"></i></div>`;
const hl = c => String(c).replace(/[&<>]/g, x => ({ '&': '&amp;', '<': '&lt;', '>': '&gt;' }[x]))
  .replace(/(\/\/.*$)|("(?:[^"\\]|\\.)*")|\b(public|private|protected|static|final|void|class|interface|extends|implements|new|return|if|else|for|while|int|double|long|boolean|char|this|super|default|null|true|false|import|try|catch|throw|switch|case|break)\b|\b([A-Z]\w*)\b|\b(\d+(?:\.\d+)?)\b/gm,
    (m, c1, s, k, t, n) => `<span class="${c1 ? 'hc' : s ? 'hs' : k ? 'hk' : t ? 'ht' : 'hn'}">${m}</span>`);
