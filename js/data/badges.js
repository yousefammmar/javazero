/* 16 badges; each has a test against saved progress. */
const BADGES = (() => {
  const d = id => Progress.has(id), all = m => MODULES.find(x => x.id === m).lessons.every(l => d(l.id));
  const b = (id, t, icon, test, hint) => ({ id, t, icon, test, hint });
  return [
    b('hello', 'Hello Java', 'spark', () => d('vars-1'), 'Finish your first lesson'),
    b('vars', 'Variables', 'cpu', () => d('vars-5'), 'Reach lesson 5 of Variables'),
    b('fast', 'Fast Run', 'zap', () => Store.get('ran', 1) >= 1, 'Run code in the playground'),
    b('methods', 'Method Maker', 'sigma', () => d('methods-3'), 'Finish Return values'),
    b('classes', 'Builder', 'wrench', () => d('classes-3'), 'Finish Constructors'),
    b('extends', 'Heir', 'network', () => d('inherit-1'), 'Finish extends'),
    b('contract', 'Contractor', 'link', () => d('interfaces-1'), 'Finish What is an interface?'),
    b('vmaster', 'Memory Master', 'db', () => all('vars'), 'Complete Variables'),
    b('mmaster', 'Method Master', 'code', () => all('methods'), 'Complete Methods'),
    b('cmaster', 'Class Master', 'blocks', () => all('classes'), 'Complete Classes'),
    b('imaster', 'Family Head', 'branch', () => all('inherit'), 'Complete Inheritance'),
    b('pmaster', 'Polymorph', 'target', () => all('interfaces'), 'Complete Interfaces'),
    b('quiz8', 'Quiz Whiz', 'quiz', () => Store.get('quizBest', 0) >= 8, 'Score 8 or more on the quiz'),
    b('quiz10', 'Perfect Score', 'trophy', () => Store.get('quizBest', 0) >= 10, 'Score 10 out of 10'),
    b('bugs', 'Bug Hunter', 'bug', () => Store.get('seenMistakes', []).length >= 4, 'Open 4 mistakes in the lab'),
    b('tools', 'Tinkerer', 'wrench', () => Store.get('toolsSeen', []).length >= 3, 'Use 3 tools'),
  ];
})();
const TIPS = [
  ['Pro Tip: Bytecode', 'Java source compiles down to <code>.class</code> bytecode which runs on any JVM, from servers to Android devices.'],
  ['Pro Tip: Naming', 'Classes are <code>PascalCase</code>, variables and methods are <code>camelCase</code>, constants are <code>UPPER_SNAKE</code>.'],
  ['Pro Tip: Read errors', 'Read a stack trace from the top: the first line names the problem, the first frame in your code is the location.'],
  ['Pro Tip: Strings', 'Always compare Strings with <code>.equals()</code>. <code>==</code> only checks if they are the same object.'],
];
