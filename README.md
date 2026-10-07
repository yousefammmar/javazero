# JavaZero

**Learn Java from absolute zero.** JavaZero is an interactive, browser-based learning platform for complete beginners. It combines short lessons, a runnable playground, hands-on labs and a mistake library, built around one idea: *muscle memory beats passive watching.*

There is no build step, no framework and no backend. It is plain HTML, CSS and JavaScript, and all progress is saved locally in your browser.

---

## Table of contents

1. [Highlights](#highlights)
2. [Pages](#pages)
3. [Quick start](#quick-start)
4. [How learning works](#how-learning-works)
5. [Study plan: 8 weeks to Java fundamentals](#study-plan-8-weeks-to-java-fundamentals)
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
| Curriculum | 30 lessons across 5 modules, each with a runnable example and key points |
| Playground | Write and run Java in the browser, with plain-English error explanations |
| Labs | Multi-file IDE with a live memory model, String Lab, Compiler Error Translator, Stack Trace Analyzer |
| Mistake Lab | 8 classic beginner errors, each with buggy code, the fix and the reason |
| Daily Quiz | 10 questions with instant feedback and a saved best score |
| Progress | Lesson checkboxes, module mastery, 16 badges, all stored in `localStorage` |
| Navigation | Command palette (`Cmd/Ctrl + K` or `/`) searching lessons, errors, tools and resources |
| Resources | Curated links to W3Schools Java reference pages, plus a per-lesson "read more" link |
| Design | Responsive layout, SVG icon system, keyboard-friendly, reduced-motion aware |

## Pages

| Page | File | Purpose |
|---|---|---|
| Dashboard | `index.html` | Hero, mastery path, module cards, IDE and labs |
| Lessons | `lessons.html` | Every module and lesson with checkboxes and filters |
| Lesson viewer | `lesson.html?id=<lesson-id>` | One lesson: explanation, runnable code, key points, previous/next |
| Roadmap | `roadmap.html` | The Learn, See, Try, Break, Build loop applied to every module |
| Playground | `playground.html` | Free-form editor and console with templates |
| Tools | `tools.html` | Multi-file IDE, String Lab, Compiler Errors, Stack Traces |
| Mistake Lab | `mistakes.html` | Buggy versus corrected code for common errors |
| Daily Quiz | `quiz.html` | Ten-question practice quiz |
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

## Study plan: 8 weeks to Java fundamentals

**Time commitment:** about 5 hours per week (roughly 45 to 60 minutes on five days, with a longer review session at the weekend). The 30 lessons contain about 200 minutes of core reading; the rest of the time is deliberate practice, which is where the learning happens.

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

### Week-by-week

| Week | Module and lessons | Practice tasks | Checkpoint |
|---|---|---|---|
| **1** | **Variables & Memory Boxes**, lessons 1 to 4: what a variable is, primitive types, declaring and assigning, integer versus decimal math | Declare one variable of each primitive type and print them. Predict `7 / 2`, `7 % 2`, `7 / 2.0` before running. | Explain why `7 / 2` is `3` |
| **2** | **Variables**, lessons 5 to 8: Strings, casting, `final`, naming rules and `var` | Build a small program that stores your name and age and prints a sentence. Use the String Lab on five methods. Read the Mistake Lab entries on `==` versus `.equals()` and immutability. | Daily Quiz: score 6 or more |
| **3** | **Methods, Parameters & Scope**, lessons 1 to 3: method anatomy, parameters, return values | Write `add`, `isEven` and `max` as methods. Call each with three inputs. | Write a method from memory in a blank Playground |
| **4** | **Methods**, lessons 4 to 6: scope, overloading, recursion | Overload `area` for a square and a rectangle. Write a recursive `factorial`, then remove the base case and read the `StackOverflowError`. | Daily Quiz: score 7 or more. Mistake Lab: the off-by-one and missing-semicolon entries |
| **5** | **Classes as Blueprints**, lessons 1 to 4: class versus object, fields, constructors, `this` | Model a `Car` with fields and a constructor. Create three cars. Open the multi-file IDE and watch the heap model update with *Mutate Object*. | Draw, on paper, the stack and heap for `Car a = new Car("Golf");` |
| **6** | **Classes**, lessons 5 to 7: encapsulation, `static` versus instance, `toString` | Build an `Account` with a private balance, `deposit` and `getBalance`. Add a `static` counter of accounts created. Override `toString`. | Explain when to use `static` and when not to |
| **7** | **Inheritance**, lessons 1 to 5: `extends`, `super`, overriding, `protected`, the `Object` class | Create `Animal` and `Dog`, override `speak`, call `super.speak()`. Trigger a `NullPointerException` on purpose and read the trace. | Daily Quiz: score 8 or more |
| **8** | **Interfaces as Contracts**, lessons 1 to 4, then the capstone | Define `Shape` with `Circle` and `Square`, store both in one array and loop over it. **Capstone:** extend the multi-file IDE with a new class (for example `Teacher`) across separate files. | Daily Quiz: score 9 or more. Earn the *Polymorph* badge |

### Weekly milestones at a glance

- **End of week 2:** you can read and write basic programs with variables, Strings and arithmetic.
- **End of week 4:** you can break a problem into reusable methods and explain scope.
- **End of week 6:** you can design a small class with hidden state and safe methods.
- **End of week 8:** you can model related types with inheritance and interfaces and explain polymorphism.

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
├── lessons.html               Lessons index
├── lesson.html                Lesson viewer (?id=vars-4)
├── roadmap.html               Roadmap
├── playground.html            Playground
├── tools.html                 Labs and tools
├── mistakes.html              Mistake Lab
├── quiz.html                  Daily Quiz
├── resources.html             W3Schools links
├── settings.html              Badges and reset
├── img/
│   └── avatar.png             Author avatar (footer)
├── css/
│   ├── main.css               Imports everything below, in order
│   ├── base.css               Variables, reset, buttons, icon base
│   ├── responsive.css         Breakpoints
│   ├── components/            layout, shell, hero, path, modules, ide, labs, sidebar
│   └── pages/                 lessons, roadmap, playground, mistakes, quiz, settings, resources
└── js/
    ├── boot.js                Loads shared scripts, then the page script
    ├── core/
    │   ├── icons.js           SVG icon sprite and ic() helper
    │   ├── store.js           localStorage wrapper and Progress API
    │   ├── java.js            In-browser Java runner (teaching subset)
    │   ├── hl.js              HTML escape, progress bar, Java syntax highlighter
    │   └── shell.js           Header, nav, sidebar, command palette, footer
    ├── data/                  curriculum, mistakes, errors, quiz, badges, resources
    ├── components/            module-cards, labs
    └── pages/                 one script per page
```

## Architecture notes

- **One shell, many pages.** Each HTML file contains only its own `<main>` content and a single `<script src="js/boot.js" data-page="...">`. `boot.js` loads the shared scripts in order and then the matching file from `js/pages/`. `shell.js` wraps the page in the header, navigation, sidebar and footer.
- **Data separated from presentation.** Lessons, mistakes, errors, quiz questions, badges and resource links live in `js/data/`. Adding content never requires touching layout code.
- **Mount points for labs.** Interactive components render into `<div data-mount="string|errors|stack|ide">`, so the same lab can appear on the dashboard and on the Tools page without duplicated markup.
- **SVG icon system.** All icons are inline SVG symbols from a single sprite (`js/core/icons.js`), colored through `currentColor`. There are no emoji and no icon font.
- **Design tokens.** Colors, radii and fonts are CSS custom properties in `css/base.css`.

## The in-browser Java runner

The Playground, the lesson viewer and the multi-file IDE all use `js/core/java.js`. It is **not a JVM.** It does three things:

1. **Checks** for common beginner mistakes: missing semicolons, unbalanced braces, no `main` method.
2. **Translates** a teaching subset of Java into JavaScript. It supports classes, fields, constructors, `static` members, inheritance (`extends`, `super`), interfaces (including `default` methods), method overloading by argument count, loops, arrays, `String` and `Math` basics, and `ArrayList`.
3. **Runs** the result in the page, captures `System.out`, and converts failures into Java-style messages (`NullPointerException`, `StackOverflowError`, `ArithmeticException`, and others). An iteration limit stops infinite loops.

All 30 lesson examples are verified to run. The runner is intentionally small so that its behavior is easy to read and extend.

## Data and privacy

- Progress is stored **only in your browser** under `localStorage` keys that start with `jz:`: completed lessons, quiz best score, mistakes explored, tools used, and run count.
- Nothing is sent to a server. There are no accounts, cookies or analytics.
- The only network requests are the page assets, Google Fonts, and the external links you choose to click.
- *Settings* has a **Reset all progress** button that clears every `jz:` key.

## Accessibility and responsiveness

- Semantic landmarks (`header`, `nav`, `main`, `aside`, `footer`), labelled controls, and `aria-current` on the active navigation link.
- Lesson checkboxes use `role="checkbox"` with `aria-checked`; the quiz supports keyboard answers (keys `1` to `4`); the command palette is fully keyboard driven.
- Visible focus rings and `prefers-reduced-motion` support.
- Layouts adapt from large desktop to phone width. On small screens the navigation becomes a bottom bar.

## Extending the content

| To add... | Edit |
|---|---|
| A lesson | `js/data/curriculum.js`: add `L('<module>', n, title, summary, code, [points])` to a module |
| A module | `js/data/curriculum.js`: add an object to `MODULES`, and a card visual in `js/components/module-cards.js` |
| A mistake | `js/data/mistakes.js` |
| A quiz question | `js/data/quiz.js` |
| A compiler error or exception translation | `js/data/errors.js` |
| A badge | `js/data/badges.js` |
| A resource link | `js/data/resources.js` |

After adding a lesson, check that its example runs by opening it in the lesson viewer and pressing **Run**.

## Known limitations

- The runner supports a teaching subset of Java. Programs that read input with `Scanner`, use generics beyond `ArrayList`, or rely on advanced APIs will not run.
- Decimal output follows JavaScript formatting, so a `double` such as `9.0` may print as `9`.
- Integer division is detected by a simple rule (both operands are plain `int` variables or numbers), not by full type checking.
- The "Active streak" panel is illustrative and is not yet tied to real activity.
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
