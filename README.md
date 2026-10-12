# JavaZero

**Learn Java from absolute zero.** JavaZero is an interactive, browser-based learning platform for complete beginners. It combines short lessons, a runnable playground, hands-on labs and a mistake library, built around one idea: *muscle memory beats passive watching.*

There is no build step, no framework and no backend. It is plain HTML, CSS and JavaScript, and all progress is saved locally in your browser.

---

## Table of contents

1. [Highlights](#highlights)
2. [Pages](#pages)
3. [Quick start](#quick-start)
4. [How learning works](#how-learning-works)
5. [Study plan: from zero to advanced in 22 weeks](#study-plan-from-zero-to-advanced-in-22-weeks)
6. [Project structure](#project-structure)
7. [Architecture notes](#architecture-notes)
8. [The in-browser Java runner](#the-in-browser-java-runner)
9. [Data and privacy](#data-and-privacy)
10. [Accessibility and responsiveness](#accessibility-and-responsiveness)
11. [Extending the content](#extending-the-content)
12. [Known limitations](#known-limitations)
13. [Credits and authorship](#credits-and-authorship)

---

## Highlights

| Area | What you get |
|---|---|
| Curriculum | 78 lessons across 15 modules in three levels (Basics, Intermediate, Advanced), each with a runnable example and key points |
| Levels | A Lessons page with three level cards (name, description, progress), a dedicated page per level, and W3Schools topic lists for each |
| Playground | Write and run Java in the browser on a real in-browser compiler (javac-style errors, JDK 21 library behavior), with interactive console input and plain-English error explanations |
| Labs | Multi-file IDE with a live memory model, String Lab, Compiler Error Translator, Stack Trace Analyzer |
| Mistake Lab | 9 classic beginner errors, each with buggy code, the fix and the reason |
| Daily Quiz | Three sets: 10 core, 8 intermediate and 10 advanced questions, with instant feedback and a saved best score for each |
| Progress | Lesson checkboxes, module mastery, 26 badges, all stored in `localStorage` |
| Navigation | A slide-out sections menu with Basics, Intermediate and Advanced nested under Lessons (hidden until you press the sidebar button in the header) and a command palette (`Cmd/Ctrl + K` or `/`) searching lessons, errors, tools and resources |
| Resources | Curated links to W3Schools Java reference pages, plus a per-lesson "read more" link |
| Design | Responsive layout, SVG icon system, keyboard-friendly, reduced-motion aware |

## Pages

| Page | File | Purpose |
|---|---|---|
| Dashboard | `index.html` | Hero, mastery path, module cards, IDE and labs |
| Lessons | `lessons.html` | Choose a level: three cards showing each level's name, description, size and your progress |
| Basics | `basics.html` | Level 1: its interactive lessons (with checkboxes) and its W3Schools topic groups |
| Intermediate | `intermediate.html` | Level 2: lessons and topic groups |
| Advanced | `advanced.html` | Level 3: lessons and topic groups, including topics that need a local JDK |
| Lesson viewer | `lesson.html?id=<lesson-id>` | One lesson: explanation, runnable code, key points, previous/next |
| Roadmap | `roadmap.html` | The Learn, See, Try, Break, Build loop applied to every module |
| Playground | `playground.html` | Free-form editor and console with templates |
| Tools | `tools.html` | Multi-file IDE, String Lab, Compiler Errors, Stack Traces |
| Mistake Lab | `mistakes.html` | Buggy versus corrected code for common errors |
| Daily Quiz | `quiz.html` | Practice quiz with core, intermediate and advanced sets |
| Resources | `resources.html` | W3Schools Java links grouped by topic |
| Settings | `settings.html` | Badges overview and progress reset |

## Quick start

JavaZero is static, but it should be **served over HTTP** (not opened by double-clicking) because the stylesheet uses `@import` and the scripts load each other.

```bash
cd javazero
python3 -m http.server 8000
```

Then open <http://localhost:8000/>. Any static server works (`npx serve`, VS Code Live Server, and so on).

Requirements: a current version of Chrome, Edge, Firefox or Safari. Fonts load from Google Fonts, so an internet connection gives the intended typography; the site still works offline with fallback fonts.

## How learning works

Every module follows the same five-step loop, shown on the dashboard and the Roadmap page:

1. **Learn**: read one short lesson that teaches one idea.
2. **See**: run the example and watch the output; use the IDE to inspect how objects sit in heap memory.
3. **Try**: change values in the Playground and predict the result before you run it.
4. **Break**: reproduce the classic mistake on purpose in the Mistake Lab.
5. **Build**: combine ideas across files in the multi-file IDE.

Progress is tracked per lesson. Tick a lesson when you finish it; the sidebar, module mastery bars and badges update immediately.

---

## Study plan: from zero to advanced in 22 weeks

**Time commitment:** about 5 hours per week (roughly 45 to 60 minutes on five days, with a longer review session at the weekend). The 38 Basics lessons contain about 250 minutes of core reading; the rest of the time is deliberate practice, which is where the learning happens.

### The daily routine (45 to 60 minutes)

| Block | Minutes | What to do |
|---|---|---|
| Warm-up recall | 5 | Without looking, write yesterday's key idea in one sentence |
| Learn | 10 | Read the day's lesson and its key points |
| See and Try | 20 | Run the example, change it three different ways, predict each output first |
| Break | 10 | Introduce one deliberate bug; read the error; fix it |
| Log | 5 | Tick the lesson, note one thing you still find confusing |

Rules that make the plan work:

- **Type everything.** Never copy and paste code into the Playground.
- **Predict before you run.** A wrong prediction is the most valuable event in your study session.
- **Read the error from the top.** Use the Compiler Error Translator and Stack Trace Analyzer when stuck.
- **Review at the weekend.** Redo one earlier lesson from memory in a blank Playground.

### Basics: weeks 1 to 10

Level 1 on the Lessons page. The Basics topics follow the W3Schools Java Tutorial, Methods and Classes groups.

| Week | Module and lessons | Practice tasks | Checkpoint |
|---|---|---|---|
| **1** | **Variables & Memory Boxes**, lessons 1 to 4: what a variable is, primitive types, declaring and assigning, integer versus decimal math | Declare one variable of each primitive type and print them. Predict `7 / 2`, `7 % 2`, `7 / 2.0` before running. | Explain why `7 / 2` is `3` |
| **2** | **Variables**, lessons 5 to 8: Strings, casting, `final`, naming rules and `var` | Build a small program that stores your name and age and prints a sentence, then make it ask for them with `Scanner` (try the *Read input* template). Use the String Lab on five methods. Read the Mistake Lab entries on `==` versus `.equals()` and immutability. | Daily Quiz: score 6 or more |
| **3** | **Decisions, Loops & Arrays**, lessons 1 to 4: if/else, booleans, switch, while and do/while | Write a grade calculator with `if / else if`, a day-of-week `switch` in both the classic and arrow forms, and a loop that counts down from 10. | Explain why a `while` loop can run zero times but `do/while` cannot |
| **4** | **Decisions, Loops & Arrays**, lessons 5 to 8: for loops, break/continue, arrays, the Math class | Print a multiplication table with nested loops, find the largest number in an array, and use `Math` to compute a circle's area. | Sum an array from memory in a blank Playground. Daily Quiz: score 6 or more |
| **5** | **Methods, Parameters & Scope**, lessons 1 to 3: method anatomy, parameters, return values | Write `add`, `isEven` and `max` as methods. Call each with three inputs. | Write a method from memory in a blank Playground |
| **6** | **Methods**, lessons 4 to 6: scope, overloading, recursion | Overload `area` for a square and a rectangle. Write a recursive `factorial`, then remove the base case and read the `StackOverflowError`. | Daily Quiz: score 7 or more. Mistake Lab: the off-by-one and missing-semicolon entries |
| **7** | **Classes as Blueprints**, lessons 1 to 4: class versus object, fields, constructors, `this` | Model a `Car` with fields and a constructor. Create three cars. Open the multi-file IDE and watch the heap model update with *Mutate Object*. | Draw, on paper, the stack and heap for `Car a = new Car("Golf");` |
| **8** | **Classes**, lessons 5 to 7: encapsulation, `static` versus instance, `toString` | Build an `Account` with a private balance, `deposit` and `getBalance`. Add a `static` counter of accounts created. Override `toString`. | Explain when to use `static` and when not to |
| **9** | **Inheritance**, lessons 1 to 5: `extends`, `super`, overriding, `protected`, the `Object` class | Create `Animal` and `Dog`, override `speak`, call `super.speak()`. Trigger a `NullPointerException` on purpose and read the trace. | Daily Quiz: score 8 or more |
| **10** | **Interfaces as Contracts**, lessons 1 to 4, then the capstone | Define `Shape` with `Circle` and `Square`, store both in one array and loop over it. **Capstone:** extend the multi-file IDE with a new class (for example `Teacher`) across separate files. | Daily Quiz: score 9 or more. Earn the *Polymorph* badge |

### Intermediate: weeks 11 to 18

Level 2. The topics follow the W3Schools Classes (going further), Errors and Data Structures groups.

Same daily routine, about 5 hours per week. All lessons and examples are original; W3Schools is linked for further reading.

| Week | Module and lessons | Practice tasks |
|---|---|---|
| **11 to 12** | **Abstraction, Enums & Nested Classes** (6 lessons) | Model shapes with an `abstract` class, add an `enum` with fields and a `switch` over it, write one inner and one static nested class |
| **13 to 14** | **Exceptions: When Things Go Wrong** (6 lessons) | Handle `NumberFormatException`, write a custom checked exception, use try-with-resources and read a `Caused by:` chain |
| **15 to 16** | **Collections: Lists, Maps & Sets** (8 lessons) | Count word frequencies with a `HashMap`, de-duplicate with a `HashSet`, sort with `Comparator`, iterate safely |
| **17 to 18** | **Generics & Lambdas** (5 lessons) | Write a generic `Box<T>`, replace loops with lambdas and a stream pipeline |

Checkpoint: the intermediate quiz (8 questions) and the intermediate badges in *Settings*.

### Advanced: weeks 19 to 22

Level 3. It covers the topics in W3Schools' *Java Advanced* section (Wrapper Classes, Generics, Annotations, RegEx, Threads, Lambda, Advanced Sorting); Generics and Lambda are taught in the Intermediate track. The File Handling and I/O Streams topics need a real computer, so the level page links them to W3Schools instead of running them in the browser.

| Week | Module and lessons | Practice tasks |
|---|---|---|
| **19** | **Wrapper Classes & Autoboxing** (3 lessons) and **Annotations** (2 lessons) | Reproduce the `Integer` cache trap with 127 and 1000 and fix it with `.equals()`; mistype `@Override` on a method and read the compiler's complaint; declare an annotation with a default value |
| **20** | **Regular Expressions** (3 lessons) | Validate an email address and a phone number, pull two groups out of a sentence, and clean text with `replaceAll` |
| **21** | **Threads & Concurrency** (4 lessons) | Run two threads and `join` them, explain in your own words why the shared counter needs `synchronized`, then submit three tasks to a thread pool |
| **22** | **Advanced Sorting** (3 lessons), then the W3Schools File Handling topics | Sort students by score and then by name with a chained `Comparator`; on your own computer with a JDK, write a program that copies a text file |

Checkpoint: score 8 or more on the Advanced set of the Daily Quiz (it earns the *Advanced Quiz Whiz* badge), explain `Comparable` versus `Comparator` and what a race condition is, then pick a W3Schools *Java Projects* exercise and build it.

### Weekly milestones at a glance

- **End of week 2:** you can read and write basic programs with variables, Strings and arithmetic.
- **End of week 4:** your programs can make decisions, repeat work and store lists of values.
- **End of week 6:** you can break a problem into reusable methods and explain scope.
- **End of week 8:** you can design a small class with hidden state and safe methods.
- **End of week 10:** you can model related types with inheritance and interfaces and explain polymorphism.
- **End of week 18:** you can handle errors, use collections and write lambdas.
- **End of week 22:** you can use regular expressions and threads and sort by any rule.

### Self-assessment: are you ready to move on?

You are ready when you can do all of these *without looking anything up*:

- Predict the output of a short program containing a loop, a method call and a String method.
- Explain the difference between a class and an object, and between `==` and `.equals()`.
- Read a compiler error and a stack trace and say which line to fix and why.
- Write a class with a constructor, private fields and getters from a blank file.

### Study tips

- **Spaced repetition:** revisit each module 1 week, then 3 weeks, after finishing it. Use the Daily Quiz and the Mistake Lab for fast refreshers.
- **Use the Resources page:** every lesson links to a matching W3Schools reference. Read it *after* you have tried the lesson, not before.
- **Keep an error journal:** write down every error message you meet and its cause. Within a month you will recognize most of them on sight.
- **Slow weeks are fine:** the plan is a guide. Repeat a week if a checkpoint feels shaky.

### After week 8: where to go next

Collections (`ArrayList`, `HashMap`), exceptions with `try` and `catch`, reading input with `Scanner`, file I/O, then a build tool (Maven or Gradle) and unit testing with JUnit. Install a real JDK and IDE (IntelliJ IDEA or VS Code with the Java extension) to run programs outside the browser.

---

## Project structure

```
javazero/
├── index.html                 Dashboard
├── lessons.html               Lessons landing: choose a level
├── basics.html                Level 1
├── intermediate.html          Level 2
├── advanced.html              Level 3
├── lesson.html                Lesson viewer (?id=vars-4)
├── roadmap.html               Roadmap
├── playground.html            Playground
├── tools.html                 Labs and tools
├── mistakes.html              Mistake Lab
├── quiz.html                  Daily Quiz
├── resources.html             W3Schools links
├── settings.html              Badges and reset
├── sitemap.xml                For search engines (regenerate with node tools/sitemap.js)
├── tools/sitemap.js           Builds sitemap.xml from the pages and lessons
├── tests/                     Compiler test tooling and differential corpus (see Testing)
├── img/
│   └── avatar.png             Author avatar (footer)
├── css/
│   ├── main.css               Imports everything below, in order
│   ├── base.css               Variables, reset, buttons, icon base
│   ├── responsive.css         Breakpoints
│   ├── components/            layout, shell, drawer, a11y, hero, path, modules, ide, labs, sidebar
│   └── pages/                 lessons, levels, roadmap, playground, mistakes, quiz, settings, resources
└── js/
    ├── boot.js                Loads shared scripts, then the page script
    ├── core/
    │   ├── icons.js           SVG icon sprite and ic() helper
    │   ├── store.js           localStorage wrapper and Progress API
    │   ├── java.js            Public API: Java.run / check / compile
│   ├── java-parse.js      Lexer and parser (Java 21 syntax, AST)
│   ├── java-gen*.js       Semantic analysis (javac-style errors) and JavaScript code generation
│   ├── java-flow.js       Flow analysis: definite assignment, unreachable code, checked exceptions
│   ├── java-rt.js         Runtime: values, strings, exceptions, Scanner, formatting
│   ├── java-coll.js       Runtime: collections, streams, Optional, BigInteger/BigDecimal, regex
    │   ├── hl.js              HTML escape, progress bar, Java syntax highlighter
    │   └── shell.js           Header, slide-out sections menu, progress panel, command palette, footer
    ├── data/                  curriculum, basics-flow, intermediate, advanced, levels, mistakes, errors, quiz, badges, resources
    ├── components/            module-cards, module-list, labs
    └── pages/                 one script per page (lessons = level chooser, level = one level page)
```

## Architecture notes

- **One shell, many pages.** Each HTML file contains only its own `<main>` content and a single `<script src="js/boot.js" data-page="...">`. `boot.js` loads the shared scripts in order and then the matching file from `js/pages/`. `shell.js` wraps the page in the header, navigation, sidebar and footer.
- **Data separated from presentation.** Lessons, mistakes, errors, quiz questions, badges and resource links live in `js/data/`. Adding content never requires touching layout code.
- **Mount points for labs.** Interactive components render into `<div data-mount="string|errors|stack|ide">`, so the same lab can appear on the dashboard and on the Tools page without duplicated markup.
- **SVG icon system.** All icons are inline SVG symbols from a single sprite (`js/core/icons.js`), colored through `currentColor`. There are no emoji and no icon font.
- **Design tokens.** Colors, radii and fonts are CSS custom properties in `css/base.css`.

## The in-browser Java runner

The Playground, the lesson viewer and the multi-file IDE all use the compiler in `js/core/java*.js`. It is **not a JVM**, but it is a real compiler pipeline rather than a pattern matcher:

1. **Parses** Java 21 syntax: classes, interfaces, enums, records, sealed types, nested/inner/anonymous/local classes, generics, lambdas and method references, `var`, text blocks, switch expressions and pattern matching (`instanceof` patterns, `switch` patterns with guards, record patterns).
2. **Checks** the program like `javac`, with the same line numbers and message wording for the common errors: missing symbols, incompatible types, possible lossy conversion, missing return, uninitialized variables, unreachable code, unreported checked exceptions, missing abstract-method implementations, bad overrides, wrong argument types, effectively-final violations, and more.
3. **Generates JavaScript** and runs it against a runtime library that emulates the JDK 21 behavior beginners rely on: `int`/`long` overflow, `char` arithmetic, exact `double` printing, `String`/`StringBuilder`, `Math`, wrapper classes, `String.format`/`printf`, `Scanner`/`BufferedReader`, collections (`ArrayList`, `LinkedList`, `HashMap` with real iteration order, `TreeMap`, sets, `Deque`, `PriorityQueue`), `Collections`/`Arrays`, streams and `Collectors`, `Optional`, `BigInteger`/`BigDecimal`, regex, `Random` (same sequence as the JDK for a given seed), and a synchronous `Thread`/executor model.
4. **Reports failures** as Java would: `Exception in thread "main" ...` with helpful `NullPointerException` messages, `ArrayIndexOutOfBoundsException`, `ClassCastException`, `ArithmeticException`, `StackOverflowError`, `Caused by:` chains and so on. A time limit stops infinite loops.

Not supported: real threads/concurrency timing, reflection, file and network I/O, `System.console()`, and most of the JDK outside the areas above. An unsupported class is reported as a compile error instead of silently misbehaving.

### Console input

Programs that read input work the way they do in a terminal:

- Run the program; when it calls `nextLine()`, `nextInt()`, `next()` and so on, the console shows a prompt and waits. Type a value and press Enter.
- Or pre-fill the **Input (stdin)** box under the console, one line per value, and the program consumes it without prompting.
- Supported: `Scanner` (`next`, `nextLine`, `nextInt`, `nextLong`, `nextDouble`, `nextBoolean`, `hasNext`, `hasNextInt`, `hasNextDouble`, `hasNextLine`) and `BufferedReader.readLine()`.
- Java's real behaviors are reproduced, including the `nextInt()` then `nextLine()` trap, `InputMismatchException` for bad input, and `NoSuchElementException` when input runs out.
- Implementation note: each time you submit a value the program is replayed from the start with all values so far (the runner cannot pause mid-execution), so keep input-driven programs deterministic.

All 55 lesson examples are verified to produce the same output as OpenJDK 21.

## Testing

The `tests/` folder contains Node scripts (no dependencies) that load the compiler exactly as the browser does:

- `tests/diff.js` runs every program under `tests/programs/**` and compares stdout, exit status and the first compile-error line and message against cached results from a real JDK 21 (`*.expected.json`; regenerate with `tests/realcheck.js` where `java` is installed).
- `tests/show.js` and `tests/jr.js` run one program and print its output or generated JavaScript for debugging.
- The corpus has about 460 programs covering basics, OOP, exceptions, collections, stdin handling, larger programs and 129 compile-error cases.


## Data and privacy

- Progress is stored **only in your browser** under `localStorage` keys that start with `jz:`: completed lessons, quiz best score, mistakes explored, tools used, and run count.
- Progress is never sent to a server. There are no accounts. If the site owner sets a Google Analytics 4 Measurement ID in `js/boot.js`, Google Analytics loads and sets cookies to count visits; it is off by default.
- The only network requests are the page assets, Google Fonts, and the external links you choose to click.
- *Settings* has a **Reset all progress** button that clears every `jz:` key.

## Accessibility and responsiveness

- Semantic landmarks (`header`, `nav`, `main`, `aside`, `footer`), labelled controls, `aria-expanded` on the menu button, focus moved into the drawer when it opens and returned to the button when it closes, and `aria-current` on the active page.
- Lesson checkboxes use `role="checkbox"` with `aria-checked`; the quiz supports keyboard answers (keys `1` to `4`); the command palette is fully keyboard driven.
- Visible focus rings and `prefers-reduced-motion` support.
- Layouts adapt from large desktop to phone width. The sections menu is a drawer on every screen size: nothing of it is visible until the header button is pressed, and it closes with `Esc`, the close button or a click outside it.
- **Contrast:** all text meets WCAG 2.1 AA (4.5:1, or 3:1 for large text). Decorative coral stays bright; text and button fills use the darker `--coral-ink` and `--coral-btn` tokens in `css/base.css`.
- **Structure:** every page has exactly one `h1` and no skipped heading levels, plus a "Skip to main content" link as the first Tab stop.
- **Targets and text:** interactive controls are at least 24px (40px on touch screens; the lesson tick has a 44px hit area), and no text is smaller than 12px.
- **Focus:** every control shows a focus ring, including the dark code editors (a ring around the whole editor).
- **Motion:** animations and transitions are removed when the system asks for reduced motion.


## Extending the content

| To add... | Edit |
|---|---|
| A beginner lesson | `js/data/curriculum.js`: add `L('<module>', n, title, summary, code, [points])` to a module |
| An intermediate lesson | `js/data/intermediate.js` (modules, lessons, W3Schools links and the intermediate quiz) |
| An advanced lesson | `js/data/advanced.js` (modules, lessons and their W3Schools links) |
| A level's description or W3Schools topic list | `js/data/levels.js`: each level has `desc`, `tracks` (which modules belong to it) and `groups` of `[title, path, lessonId?, description?]` topics. Add a module's `track` to a level and it appears on that level's page, in the sidebar menu and in search |
| A module | `js/data/curriculum.js`: add an object to `MODULES`, and a card visual in `js/components/module-cards.js` |
| A mistake | `js/data/mistakes.js` |
| A quiz question | `js/data/quiz.js` (core), `js/data/intermediate.js` (`QUIZ_MID`) or `js/data/advanced.js` (`QUIZ_ADV`) |
| A compiler error or exception translation | `js/data/errors.js` |
| A badge | `js/data/badges.js` |
| A resource link | `js/data/resources.js` |

After adding a lesson, check that its example runs by opening it in the lesson viewer and pressing **Run**.

## Known limitations

- The compiler covers the Java 21 language and the JDK areas listed above, not the whole JDK. Threads run synchronously, and reflection, file I/O and networking are unavailable.
- Because input is handled by replaying the program, programs that use randomness (without a seed) or the clock may behave differently between prompts.
- A few exotic corner cases still differ from the real JDK (rare formatting and exception-message details); the differential test corpus tracks them.
- Progress statistics, including the streak, come from real activity recorded in `localStorage` on this device only.
- No license has been selected for this repository yet.

---

## Credits and authorship

**Designed and built by Yousef Odeh.**
Portfolio: <https://yousefammmar.github.io/profile>

**Documentation drafted by Claude (Anthropic)** at the author's request, with the author directing the project's scope, design and content.

W3Schools links point to the public pages at <https://www.w3schools.com/java/>. W3Schools is a trademark of Refsnes Data; JavaZero is not affiliated with or endorsed by W3Schools, and none of their content is reproduced here.

The initial interface was based on a Stitch design ("JavaZero: Beginner Java Platform") and then rebuilt by hand as the static site you see here.

<br>

<p align="center">
  <strong>Yousef Odeh</strong> &nbsp;·&nbsp; Author and developer<br>
  <strong>Claude</strong> &nbsp;·&nbsp; Documentation
</p>
