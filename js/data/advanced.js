/* Advanced curriculum: the topics of W3Schools' "Java Advanced" section (Wrapper Classes, RegEx, Threads, Advanced Sorting, Annotations).
   Generics and Lambda from that section live in the Intermediate track (module "lambdas"). All text and code here is original. */
MODULES.push(
  { id: 'wrappers', track: 'adv', tag: 'Advanced', icon: 'db', min: 30, name: 'Wrapper Classes & Autoboxing',
    desc: 'Treat primitives as objects: autoboxing, the Integer cache, parsing and helper methods.',
    lessons: [
      L('wrappers',1,'Wrapper classes','Every primitive type has an object twin.',`public class Main {
    public static void main(String[] args) {
        Integer a = Integer.valueOf(42);
        Double d = 3.5;
        Character c = 'J';
        Boolean b = true;
        int unboxed = a;
        System.out.println(a + " " + d + " " + c + " " + b);
        System.out.println(unboxed + 1);
        System.out.println(a.doubleValue() + " " + d.intValue());
    }
}`,['int has Integer, double has Double, char has Character and boolean has Boolean.','Collections such as ArrayList can only hold objects, so they need wrappers.','Wrapper objects carry helper methods like doubleValue() and intValue().']),
      L('wrappers',2,'Autoboxing and the == trap','Java converts for you, but == compares objects.',`public class Main {
    public static void main(String[] args) {
        Integer x = 127, y = 127;
        Integer p = 1000, q = 1000;
        System.out.println(x == y);       // cached small values
        System.out.println(p == q);       // two different objects
        System.out.println(p.equals(q));  // compares the numbers
    }
}`,['Autoboxing turns an int into an Integer automatically; unboxing turns it back.','Integer values from -128 to 127 are cached, so == happens to work for them.','Outside that range == compares objects, so always use .equals() on wrappers.']),
      L('wrappers',3,'Parsing and helper methods','Convert text to numbers and inspect characters.',`public class Main {
    public static void main(String[] args) {
        System.out.println(Integer.parseInt("123") + 1);
        System.out.println(Integer.toBinaryString(10));
        System.out.println(Integer.MAX_VALUE);
        System.out.println(Integer.compare(3, 7));
        System.out.println(Double.parseDouble("2.5") * 2);
        System.out.println(Character.isDigit('7') + " " + Character.toUpperCase('a'));
    }
}`,['parseInt and parseDouble turn text into numbers, and throw NumberFormatException on bad input.','Integer.MAX_VALUE and MIN_VALUE are the limits of an int.','Character.isDigit, isLetter and toUpperCase classify and convert single characters.']),
    ] },
  { id: 'regex', track: 'adv', tag: 'Advanced', icon: 'search', min: 40, name: 'Regular Expressions',
    desc: 'Describe text patterns and use them to find, validate, replace and split.',
    lessons: [
      L('regex',1,'Pattern and Matcher','Compile a pattern once, then search any text with it.',`import java.util.regex.*;

public class Main {
    public static void main(String[] args) {
        Pattern p = Pattern.compile("[a-z]+");
        Matcher m = p.matcher("abc 123 def");
        while (m.find()) {
            System.out.println(m.group() + " at " + m.start());
        }
    }
}`,['Pattern.compile builds the pattern; matcher(text) applies it to a String.','find() moves to the next match, group() returns it and start() gives its position.','A pattern is just a String, so backslashes are doubled in Java source code.']),
      L('regex',2,'matches, replaceAll and split','Regular expressions built into String.',`import java.util.regex.*;

public class Main {
    public static void main(String[] args) {
        System.out.println(Pattern.matches("[A-Z][a-z]+", "Java"));
        System.out.println("hello world".matches("hello.*"));
        System.out.println("a1b22c333".replaceAll("[0-9]+", "#"));
        String[] parts = "one, two;three".split("[,;]\\\\s*");
        System.out.println(parts.length + " " + parts[2]);
    }
}`,['String has regex shortcuts: matches, replaceAll and split.','matches() must match the whole String, while find() looks anywhere inside it.','[0-9]+ means one or more digits, and [,;] means a comma or a semicolon.']),
      L('regex',3,'Groups and validating input','Capture parts of a match and check a whole value.',`import java.util.regex.*;

public class Main {
    public static void main(String[] args) {
        Matcher m = Pattern.compile("(\\\\d+)-(\\\\d+)").matcher("call 555-1234 now");
        if (m.find()) {
            System.out.println(m.group(0));
            System.out.println(m.group(1) + " / " + m.group(2));
        }
        Matcher e = Pattern.compile("^[\\\\w.]+@[\\\\w.]+\\\\.[a-z]{2,}$").matcher("me@site.com");
        System.out.println(e.matches());
    }
}`,['Parentheses create groups: group(1) is the first one and group(0) is the whole match.','The anchors ^ and $ make a pattern cover the entire input, which is how you validate.','\\w, \\d and \\s are shortcuts for word, digit and space characters.']),
    ] },
  { id: 'threads', track: 'adv', tag: 'Advanced', icon: 'branch', min: 45, name: 'Threads & Concurrency',
    desc: 'Run work in parallel, share data safely and manage threads with a pool.',
    lessons: [
      L('threads',1,'Creating a thread','Run code alongside main.',`public class Main {
    public static void main(String[] args) {
        Thread t = new Thread(() -> System.out.println("hello from " + Thread.currentThread().getName()));
        t.setName("worker");
        t.start();
        try { t.join(); } catch (InterruptedException e) { }
        System.out.println("main done");
    }
}`,['A Thread runs code at the same time as the rest of your program.','start() launches the new thread; calling run() directly would not.','join() makes the caller wait until the thread has finished.']),
      L('threads',2,'Thread or Runnable?','Two ways to describe what a thread does.',`class Worker extends Thread {
    String id;
    Worker(String id) { this.id = id; }
    public void run() { System.out.println("Worker " + id); }
}
class Task implements Runnable {
    public void run() { System.out.println("Task"); }
}
public class Main {
    public static void main(String[] args) {
        Worker w = new Worker("A");
        Thread t = new Thread(new Task());
        w.start(); t.start();
        try { w.join(); t.join(); } catch (InterruptedException e) { }
        System.out.println("both finished");
    }
}`,['You can extend Thread or, usually better, implement Runnable and hand it to a Thread.','Implementing Runnable keeps your class free to extend something else.','On a real JVM the order two threads print in can change from run to run.']),
      L('threads',3,'Shared data and synchronized','Stop two threads from stepping on each other.',`class Counter {
    private int n = 0;
    synchronized void inc() { n++; }
    int get() { return n; }
}
public class Main {
    public static void main(String[] args) {
        Counter c = new Counter();
        Thread a = new Thread(() -> { for (int i = 0; i < 1000; i++) c.inc(); });
        Thread b = new Thread(() -> { for (int i = 0; i < 1000; i++) c.inc(); });
        a.start(); b.start();
        try { a.join(); b.join(); } catch (InterruptedException e) { }
        System.out.println(c.get());
    }
}`,['Two threads changing one variable can overwrite each other: a race condition.','synchronized lets only one thread run the method at a time.','With it, two threads adding 1000 each always give 2000.']),
      L('threads',4,'Thread pools','Reuse a few threads for many tasks.',`import java.util.concurrent.*;

public class Main {
    public static void main(String[] args) {
        ExecutorService pool = Executors.newFixedThreadPool(2);
        Future<Integer> f = pool.submit(() -> 6 * 7);
        try { System.out.println(f.get()); } catch (Exception e) { }
        pool.shutdown();
    }
}`,['An ExecutorService reuses a small set of threads for many tasks.','submit() returns a Future, and get() waits for the result.','Always call shutdown() when you are finished with the pool.']),
    ] },
  { id: 'sorting', track: 'adv', tag: 'Advanced', icon: 'trend', min: 35, name: 'Advanced Sorting',
    desc: 'Teach your classes an order and sort collections by one or many rules.',
    lessons: [
      L('sorting',1,'Comparable: a natural order','Give a class its own default order.',`import java.util.*;

class Student implements Comparable<Student> {
    String name; int score;
    Student(String name, int score) { this.name = name; this.score = score; }
    public int compareTo(Student o) { return Integer.compare(score, o.score); }
    public String toString() { return name + ":" + score; }
}
public class Main {
    public static void main(String[] args) {
        List<Student> list = new ArrayList<>();
        list.add(new Student("Zoe", 70));
        list.add(new Student("Al", 95));
        list.add(new Student("Mo", 82));
        Collections.sort(list);
        for (Student s : list) System.out.println(s);
    }
}`,['Implement Comparable<T> and write compareTo to give a class a natural order.','compareTo returns a negative number, zero or a positive number, like Integer.compare.','Collections.sort(list) then uses that order.']),
      L('sorting',2,'Comparator: an order from outside','Sort by a rule that is not built into the class.',`import java.util.*;

class Student {
    String name; int score;
    Student(String name, int score) { this.name = name; this.score = score; }
    public String toString() { return name + ":" + score; }
}
public class Main {
    public static void main(String[] args) {
        List<Student> list = new ArrayList<>(List.of(new Student("Zoe", 70), new Student("Al", 95), new Student("Mo", 82)));
        list.sort(Comparator.comparing(s -> s.name));
        System.out.println(list);
        list.sort((a, b) -> a.score - b.score);
        System.out.println(list);
    }
}`,['A Comparator describes an order separately from the class, so one class can have many.','Comparator.comparing(...) builds one from a field; a lambda builds a custom one.','list.sort(comparator) sorts the list in place.']),
      L('sorting',3,'Chaining comparators','Break ties and reverse the order.',`import java.util.*;

class Student {
    private String name; private int score;
    Student(String name, int score) { this.name = name; this.score = score; }
    String getName() { return name; }
    int getScore() { return score; }
    public String toString() { return name + ":" + score; }
}
public class Main {
    public static void main(String[] args) {
        List<Student> list = new ArrayList<>(List.of(new Student("Zoe", 80), new Student("Al", 80), new Student("Mo", 95)));
        list.sort(Comparator.comparingInt(Student::getScore).reversed().thenComparing(Student::getName));
        System.out.println(list);
    }
}`,['comparingInt(...).reversed() flips the order, so the highest score comes first.','thenComparing breaks ties with a second rule, here the name.','Method references like Student::getName keep comparators short.']),
    ] },
  { id: 'annotations', track: 'adv', tag: 'Advanced', icon: 'bulb', min: 20, name: 'Annotations',
    desc: 'Add metadata with @ markers and declare your own.',
    lessons: [
      L('annotations',1,'Built-in annotations','Markers that tell the compiler more.',`@FunctionalInterface
interface Shouter { String shout(String s); }

class Base { String greet() { return "base"; } }
class Legacy extends Base {
    @Override
    String greet() { return "legacy"; }
    @Deprecated
    void old() { }
}
public class Main {
    public static void main(String[] args) {
        System.out.println(new Legacy().greet());
        Shouter s = text -> text.toUpperCase();
        System.out.println(s.shout("hi"));
    }
}`,['Annotations start with @ and give the compiler or other tools extra information.','@Override makes the compiler check that you really override a method.','@FunctionalInterface guards an interface so it keeps one abstract method, and @Deprecated marks old code.']),
      L('annotations',2,'Declaring your own','Create an annotation with @interface.',`@interface Info {
    String author();
    int version() default 1;
}

@Info(author = "Yousef")
class Tool { }

public class Main {
    public static void main(String[] args) {
        new Tool();
        System.out.println("annotated");
    }
}`,['@interface declares a new annotation with named elements.','An element can have a default value, like version() default 1.','Frameworks read annotations at runtime to configure your code.']),
    ] },
);

Object.assign(W3_LESSON, {
  'wrappers-1': 'java_wrapper_classes.asp', 'wrappers-2': 'java_wrapper_classes.asp', 'wrappers-3': 'java_wrapper_classes.asp',
  'regex-1': 'java_regex.asp', 'regex-2': 'java_regex.asp', 'regex-3': 'java_regex.asp',
  'threads-1': 'java_threads.asp', 'threads-2': 'java_threads.asp', 'threads-3': 'java_threads.asp', 'threads-4': 'java_threads.asp',
  'sorting-1': 'java_advanced_sorting.asp', 'sorting-2': 'java_advanced_sorting.asp', 'sorting-3': 'java_advanced_sorting.asp',
  'annotations-1': 'java_annotations.asp', 'annotations-2': 'java_annotations.asp',
});

RESOURCES.push({ t: 'Advanced', icon: 'zap', items: [
  ['Wrapper Classes', 'java_wrapper_classes.asp', 'Objects for primitives and autoboxing.'], ['Generics', 'java_generics.asp', 'Type-safe classes and methods.'], ['Annotations', 'java_annotations.asp', 'Metadata markers.'],
  ['RegEx', 'java_regex.asp', 'Patterns for searching text.'], ['Threads', 'java_threads.asp', 'Running tasks in parallel.'], ['Lambda', 'java_lambda.asp', 'Short anonymous functions.'], ['Advanced Sorting', 'java_advanced_sorting.asp', 'Comparable and Comparator.'],
  ['File Handling', 'java_files.asp', 'Reading and writing files.'], ['I/O Streams', 'java_io_streams.asp', 'Byte and character streams.'], ['Java Projects', 'java_projects.asp', 'Practice with bigger programs.'] ] });

/* Advanced quiz: one question per idea taught in the Advanced track. */
const QUIZ_ADV = [
  { q: 'What does this print? Integer a = 1000, b = 1000; System.out.println(a == b);', o: ['true', 'false', 'It does not compile', 'It throws an exception'], a: 1, why: 'Only Integer values from -128 to 127 are cached. 1000 creates two separate objects, so == compares references. Use .equals().' },
  { q: 'Which call turns the text "42" into an int?', o: ['Integer.parseInt("42")', 'int.parse("42")', 'Integer.toInt("42")', '(int) "42"'], a: 0, why: 'parseInt is the standard way; it throws NumberFormatException if the text is not a whole number.' },
  { q: 'What does Matcher.find() do?', o: ['Checks that the whole String matches', 'Compiles the pattern', 'Moves to the next match and returns true if there is one', 'Replaces every match'], a: 2, why: 'Call find() in a loop to visit every match, then group() to read each one.' },
  { q: 'Which Java String holds a regex for one or more digits?', o: ['"\\\\d+"', '"\\d+"', '"[digits]"', '"#+"'], a: 0, why: 'The regex is \\d+, and the backslash is written twice inside a Java String literal: "\\\\d+".' },
  { q: 'Which call starts a new thread running your code?', o: ['t.run()', 't.join()', 't.wait()', 't.start()'], a: 3, why: 'start() creates the new thread. Calling run() directly just runs the code on the current thread.' },
  { q: 'Why mark a shared counter\'s increment method synchronized?', o: ['To make it faster', 'So only one thread can change it at a time, avoiding lost updates', 'So it cannot be garbage collected', 'So it becomes static'], a: 1, why: 'Without it two threads can read the same value and both write back n + 1, losing an update (a race condition).' },
  { q: 'What does Future.get() do?', o: ['Cancels the task', 'Starts the task', 'Returns null immediately', 'Waits for the task to finish and returns its result'], a: 3, why: 'get() blocks until the result is ready. It can throw if the task failed.' },
  { q: 'Which interface gives a class its natural sort order?', o: ['Comparator', 'Sortable', 'Comparable', 'Ordered'], a: 2, why: 'A class implements Comparable<T> and defines compareTo. A Comparator is a separate object describing another order.' },
  { q: 'What does Comparator.comparingInt(Student::getScore).reversed() produce?', o: ['Lowest score first', 'Alphabetical order', 'Highest score first', 'The original order'], a: 2, why: 'comparingInt sorts ascending, and reversed() flips it, so the highest score comes first.' },
  { q: 'What is @Override for?', o: ['It makes the method faster', 'It makes the method final', 'It hides the method', 'It asks the compiler to check that you really override a parent method'], a: 3, why: 'If the name or parameters do not match a parent method, the compiler reports an error instead of silently creating a new method.' },
];
