/* Intermediate curriculum: OOP extras, exceptions, collections, generics & lambdas. */
MODULES.push(
  { id: 'abstraction', track: 'mid', tag: 'Intermediate', icon: 'blocks', min: 45, name: 'Abstraction, Enums & Nested Classes',
    desc: 'Define incomplete blueprints, fixed sets of constants, and classes that live inside other classes.',
    lessons: [
      L('abstraction',1,'Abstract classes','A class you cannot instantiate, only extend.',`abstract class Shape {
    String name;
    Shape(String name) { this.name = name; }
}
class Circle extends Shape {
    Circle() { super("circle"); }
}
public class Main {
    public static void main(String[] args) {
        Shape s = new Circle();
        System.out.println("I am a " + s.name);
        // new Shape("x");  // compile error: Shape is abstract
    }
}`,['Mark a class abstract to forbid new on it.','It can still have fields and constructors for subclasses.','Use it when the parent is an idea, not a real thing.']),
      L('abstraction',2,'Abstract methods and shared code','Parent supplies the steps, children fill in the blanks.',`abstract class Drink {
    abstract void addMain();
    void make() {
        System.out.println("Boil water");
        addMain();
        System.out.println("Pour into cup");
    }
}
class Tea extends Drink {
    void addMain() { System.out.println("Steep tea bag"); }
}
class Coffee extends Drink {
    void addMain() { System.out.println("Add coffee grounds"); }
}
public class Main {
    public static void main(String[] args) {
        new Tea().make();
        new Coffee().make();
    }
}`,['An abstract method has a signature but no body.','Every concrete subclass must implement it.','Concrete methods can call abstract ones: the template idea.']),
      L('abstraction',3,'Enums','A fixed set of named constants.',`enum Day { MON, TUE, WED }
public class Main {
    public static void main(String[] args) {
        Day d = Day.TUE;
        System.out.println(d);
        System.out.println(d.ordinal());
        for (Day x : Day.values()) {
            System.out.println(x.name());
        }
        System.out.println(Day.valueOf("WED"));
    }
}`,['Constants are written in UPPER_CASE by convention.','values() returns them all; ordinal() is the position.','valueOf throws if the name does not exist.']),
      L('abstraction',4,'Enums with fields and switch','Enums can carry data and work in a switch.',`enum Size {
    SMALL(250), MEDIUM(350), LARGE(500);
    private final int ml;
    Size(int ml) { this.ml = ml; }
    int getMl() { return ml; }
}
public class Main {
    public static void main(String[] args) {
        for (Size s : Size.values()) {
            System.out.println(s + " = " + s.getMl() + " ml");
        }
        Size pick = Size.MEDIUM;
        switch (pick) {
            case SMALL:
                System.out.println("light");
                break;
            case MEDIUM:
                System.out.println("just right");
                break;
            default:
                System.out.println("big");
        }
    }
}`,['The constructor is implicitly private.','Each constant passes its values in parentheses.','Inside switch, write case SMALL, not Size.SMALL.']),
      L('abstraction',5,'Static nested classes','A class tucked inside another, with no outer object needed.',`class Library {
    static class Book {
        String title;
        Book(String title) { this.title = title; }
        void show() { System.out.println("Book: " + title); }
    }
}
public class Main {
    public static void main(String[] args) {
        Library.Book b = new Library.Book("Dune");
        b.show();
        Library.Book c = new Library.Book("Emma");
        c.show();
    }
}`,['Reach it through the outer name: Library.Book.','It cannot see the outer instance fields.','Use it to group helpers that belong with one class.']),
      L('abstraction',6,'Inner classes','A non-static nested class is tied to one outer object.',`class Bank {
    private int balance = 100;
    class Teller {
        void report() {
            System.out.println("Balance: " + balance);
        }
    }
}
public class Main {
    public static void main(String[] args) {
        Bank bank = new Bank();
        Bank.Teller t = bank.new Teller();
        t.report();
        Bank other = new Bank();
        other.new Teller().report();
    }
}`,['An inner object always belongs to an outer object.','It can read the outer private fields directly.','Create one with outer.new Inner().']),
    ] },
  { id: 'exceptions', track: 'mid', tag: 'Intermediate', icon: 'bug', min: 40, name: 'Exceptions: When Things Go Wrong',
    desc: 'Catch runtime errors, clean up after them, and signal your own problems with custom exceptions.',
    lessons: [
      L('exceptions',1,'try and catch','Catch an error instead of crashing.',`public class Main {
    public static void main(String[] args) {
        int[] nums = {1, 2, 3};
        try {
            System.out.println(nums[1]);
            System.out.println(nums[5]);
            System.out.println("not reached");
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("Bad index: " + e.getMessage());
        }
        System.out.println("Program continues");
    }
}`,['Code after the failing line in try is skipped.','The catch block receives the exception object.','Execution then carries on after the whole try/catch.']),
      L('exceptions',2,'finally','Code that runs whether or not an error happened.',`public class Main {
    static void test(int d) {
        try {
            System.out.println("10 / " + d + " = " + (10 / d));
        } catch (ArithmeticException e) {
            System.out.println("Cannot divide: " + e.getMessage());
        } finally {
            System.out.println("done with " + d);
        }
    }
    public static void main(String[] args) {
        test(2);
        test(0);
    }
}`,['finally runs after try, and after catch if one ran.','Use it for cleanup you must never skip.','It even runs when the try block returns.']),
      L('exceptions',3,'Multiple catch blocks','Handle different problems differently; specific first.',`public class Main {
    static void run(String s, int i) {
        try {
            int n = Integer.parseInt(s);
            int[] a = new int[3];
            a[i] = n;
            System.out.println("stored " + n);
        } catch (NumberFormatException e) {
            System.out.println("not a number: " + s);
        } catch (ArrayIndexOutOfBoundsException | NullPointerException e) {
            System.out.println("array problem");
        } catch (Exception e) {
            System.out.println("something else");
        }
    }
    public static void main(String[] args) {
        run("7", 1);
        run("abc", 1);
        run("7", 9);
    }
}`,['Java uses the first catch that matches.','Put specific types before general ones like Exception.','Use | to share one handler between types.']),
      L('exceptions',4,'throw','Raise an exception yourself.',`public class Main {
    static void setAge(int age) {
        if (age < 0) {
            throw new IllegalArgumentException("age cannot be negative");
        }
        System.out.println("age set to " + age);
    }
    public static void main(String[] args) {
        setAge(20);
        try {
            setAge(-5);
        } catch (IllegalArgumentException e) {
            System.out.println("Caught: " + e.getMessage());
            System.out.println(e);
        }
    }
}`,['throw sends an exception object up the call stack.','Validate inputs and fail loudly and early.','Printing e shows the class name and message.']),
      L('exceptions',5,'Custom exceptions','Make your own exception type with a clear name.',`class InsufficientFundsException extends Exception {
    InsufficientFundsException(String msg) { super(msg); }
}
class Account {
    private int balance = 50;
    void withdraw(int amt) throws InsufficientFundsException {
        if (amt > balance) {
            throw new InsufficientFundsException("Need " + (amt - balance) + " more");
        }
        balance -= amt;
        System.out.println("Withdrew " + amt);
    }
}
public class Main {
    public static void main(String[] args) {
        Account a = new Account();
        try {
            a.withdraw(30);
            a.withdraw(30);
        } catch (InsufficientFundsException e) {
            System.out.println("Failed: " + e.getMessage());
        }
    }
}`,['Extend Exception (checked) or RuntimeException (unchecked).','Pass the message up with super(msg).','Good names make errors self-explanatory.']),
      L('exceptions',6,'throws, checked vs unchecked','Checked exceptions must be handled or declared.',`class ConfigException extends Exception {
    ConfigException(String m) { super(m); }
}
public class Main {
    static void load(String name) throws ConfigException {
        if (name.isEmpty()) throw new ConfigException("empty name");
        System.out.println("loaded " + name);
    }
    static int risky(String s) {
        return Integer.parseInt(s);
    }
    public static void main(String[] args) {
        try {
            load("app");
            load("");
        } catch (ConfigException e) {
            System.out.println("checked: " + e.getMessage());
        }
        try {
            risky("x1");
        } catch (RuntimeException e) {
            System.out.println("unchecked: " + e.getMessage());
        }
    }
}`,['throws in the signature warns callers about checked exceptions.','The compiler forces you to handle checked ones.','RuntimeException and its children are unchecked: no forced handling.']),
    ] },
  { id: 'collections', track: 'mid', tag: 'Intermediate', icon: 'db', min: 50, name: 'Collections: Lists, Maps & Sets',
    desc: 'Store and organize groups of objects with resizable lists, key-value maps and unique sets.',
    lessons: [
      L('collections',1,'ArrayList','A resizable array.',`import java.util.*;
public class Main {
    public static void main(String[] args) {
        ArrayList<String> names = new ArrayList<>();
        names.add("Ana");
        names.add("Ben");
        names.add("Cy");
        names.set(1, "Bo");
        names.remove("Cy");
        System.out.println(names);
        System.out.println(names.get(0) + " " + names.size());
        System.out.println(names.contains("Bo"));
    }
}`,['Unlike arrays, it grows and shrinks as needed.','The type in <> says what it holds.','Use get/set/add/remove instead of [].']),
      L('collections',2,'LinkedList','A list built from linked nodes, great at the ends.',`import java.util.*;
public class Main {
    public static void main(String[] args) {
        LinkedList<Integer> q = new LinkedList<>();
        q.add(2);
        q.add(3);
        q.addFirst(1);
        q.addLast(4);
        System.out.println(q);
        System.out.println(q.getFirst() + " " + q.getLast());
        q.removeFirst();
        q.removeLast();
        System.out.println(q);
    }
}`,['Adding or removing at the ends is fast.','Reaching the middle by index is slower than ArrayList.','It adds addFirst, addLast, removeFirst, removeLast.']),
      L('collections',3,'HashMap','Look up values by key.',`import java.util.*;
public class Main {
    public static void main(String[] args) {
        HashMap<String, Integer> ages = new HashMap<>();
        ages.put("Ana", 30);
        ages.put("Ben", 25);
        ages.put("Ana", 31);
        System.out.println(ages.get("Ana"));
        System.out.println(ages.containsKey("Zed"));
        System.out.println(ages.getOrDefault("Zed", -1));
        for (String k : ages.keySet()) {
            System.out.println(k + " -> " + ages.get(k));
        }
        System.out.println(ages.size());
    }
}`,['put with an existing key replaces the old value.','HashMap order is not insertion order.','getOrDefault avoids null for missing keys.']),
      L('collections',4,'HashSet','A collection with no duplicates.',`import java.util.*;
public class Main {
    public static void main(String[] args) {
        HashSet<String> tags = new HashSet<>();
        System.out.println(tags.add("java"));
        System.out.println(tags.add("code"));
        System.out.println(tags.add("java"));
        System.out.println(tags.size());
        System.out.println(tags.contains("code"));
        tags.remove("code");
        System.out.println(tags);
    }
}`,['add returns false when the item is already present.','contains is very fast.','Order is not guaranteed; there are no indexes.']),
      L('collections',5,'TreeMap and LinkedHashMap','Maps with predictable order.',`import java.util.*;
public class Main {
    public static void main(String[] args) {
        Map<String, Integer> sorted = new TreeMap<>();
        Map<String, Integer> linked = new LinkedHashMap<>();
        String[] words = {"pear", "apple", "fig"};
        for (String w : words) {
            sorted.put(w, w.length());
            linked.put(w, w.length());
        }
        System.out.println("TreeMap:       " + sorted);
        System.out.println("LinkedHashMap: " + linked);
        for (Map.Entry<String, Integer> e : sorted.entrySet()) {
            System.out.println(e.getKey() + "=" + e.getValue());
        }
    }
}`,['TreeMap keeps keys sorted.','LinkedHashMap keeps insertion order.','entrySet gives key and value together as Map.Entry.']),
      L('collections',6,'Iterator','Walk a collection and remove safely.',`import java.util.*;
public class Main {
    public static void main(String[] args) {
        ArrayList<Integer> nums = new ArrayList<>();
        for (int i = 1; i <= 8; i++) nums.add(i);
        Iterator<Integer> it = nums.iterator();
        while (it.hasNext()) {
            int n = it.next();
            if (n % 2 == 0) {
                it.remove();
            }
        }
        System.out.println(nums);
    }
}`,['hasNext checks, next moves and returns the item.','it.remove() deletes the last item returned.','Removing inside a for-each loop throws an exception.']),
      L('collections',7,'Sorting: Comparable and Comparator','Put objects in order.',`import java.util.*;
class Player implements Comparable<Player> {
    String name;
    int score;
    Player(String name, int score) { this.name = name; this.score = score; }
    public int compareTo(Player o) { return Integer.compare(score, o.score); }
    public String toString() { return name + ":" + score; }
}
class ByName implements Comparator<Player> {
    public int compare(Player a, Player b) { return a.name.compareTo(b.name); }
}
public class Main {
    public static void main(String[] args) {
        ArrayList<Player> ps = new ArrayList<>();
        ps.add(new Player("Zoe", 70));
        ps.add(new Player("Al", 95));
        ps.add(new Player("Mo", 82));
        Collections.sort(ps);
        System.out.println(ps);
        Collections.sort(ps, new ByName());
        System.out.println(ps);
        System.out.println(Collections.max(ps));
    }
}`,['Comparable gives a class its natural order.','A Comparator supplies an extra, different order.','compareTo returns negative, zero or positive.']),
      L('collections',8,'Wrapper classes and autoboxing','Objects that wrap primitives.',`import java.util.*;
public class Main {
    public static void main(String[] args) {
        Integer a = 5;
        int b = a + 1;
        ArrayList<Double> prices = new ArrayList<>();
        prices.add(2.5);
        prices.add(4.0);
        System.out.println(b + " " + prices);
        int n = Integer.parseInt("42");
        double d = Double.parseDouble("3.5");
        System.out.println(n + d);
        System.out.println(Integer.valueOf(7) + " " + Boolean.parseBoolean("true"));
        System.out.println(Integer.compare(3, 9) + " " + Integer.MAX_VALUE);
        System.out.println(Character.isDigit('7') + " " + Character.toUpperCase('q'));
    }
}`,['Collections hold objects, so int becomes Integer automatically.','parseXxx turns text into a primitive; valueOf into an object.','MAX_VALUE and MIN_VALUE give each type limits.']),
    ] },
  { id: 'lambdas', track: 'mid', tag: 'Intermediate', icon: 'zap', min: 40, name: 'Generics & Lambdas',
    desc: 'Write code once for many types, and pass behavior around as tiny functions.',
    lessons: [
      L('lambdas',1,'Generic classes','One class, any type.',`class Box<T> {
    private T item;
    Box(T item) { this.item = item; }
    T get() { return item; }
    void show() { System.out.println("Box holds " + item); }
}
public class Main {
    public static void main(String[] args) {
        Box<String> s = new Box<>("hello");
        Box<Integer> n = new Box<>(42);
        s.show();
        n.show();
        int doubled = n.get() * 2;
        System.out.println(doubled);
        System.out.println(s.get().length());
    }
}`,['T is a placeholder filled in when you use the class.','No casts needed: the compiler knows the type.','Generics work with objects, so Integer rather than int.']),
      L('lambdas',2,'Generic methods and bounds','Type-safe helpers for many types.',`import java.util.*;
public class Main {
    static <T> void printAll(T[] items) {
        for (T t : items) System.out.print(t + " ");
        System.out.println();
    }
    static <T extends Comparable<T>> T biggest(T a, T b) {
        return a.compareTo(b) >= 0 ? a : b;
    }
    public static void main(String[] args) {
        printAll(new Integer[]{1, 2, 3});
        printAll(new String[]{"a", "b"});
        System.out.println(biggest(3, 9));
        System.out.println(biggest("pear", "apple"));
        System.out.println(biggest(2.5, 1.5));
    }
}`,['Put <T> before the return type of the method.','extends sets a bound: T must be Comparable.','Java infers T from the arguments.']),
      L('lambdas',3,'Lambda expressions','A tiny anonymous function.',`import java.util.function.*;
public class Main {
    public static void main(String[] args) {
        Runnable hi = () -> System.out.println("Hi from a lambda");
        hi.run();
        BiFunction<Integer, Integer, Integer> add = (a, b) -> a + b;
        System.out.println(add.apply(2, 3));
        Function<String, Integer> len = s -> s.length();
        System.out.println(len.apply("lambda"));
        Function<Integer, Integer> sq = n -> {
            int r = n * n;
            return r;
        };
        System.out.println(sq.apply(7));
    }
}`,['Syntax: (parameters) -> expression or { statements }.','A lambda is an instance of a functional interface.','Types of the parameters are inferred.']),
      L('lambdas',4,'Functional interfaces','An interface with exactly one abstract method.',`import java.util.function.*;
interface Shouter {
    String shout(String s);
}
public class Main {
    public static void main(String[] args) {
        Shouter sh = s -> s.toUpperCase() + "!";
        System.out.println(sh.shout("hey"));
        Function<Integer, Integer> twice = x -> x * 2;
        Predicate<String> empty = s -> s.isEmpty();
        Supplier<String> greet = () -> "hello";
        Consumer<String> show = s -> System.out.println("> " + s);
        BiFunction<String, Integer, String> join = (s, n) -> s + n;
        System.out.println(twice.apply(21));
        System.out.println(empty.test("") + " " + empty.test("x"));
        show.accept(greet.get());
        System.out.println(join.apply("ab", 3));
    }
}`,['Function takes and returns, Predicate returns boolean.','Supplier gives a value, Consumer takes one and returns nothing.','Your own one-method interface works with lambdas too.']),
      L('lambdas',5,'Lambdas with collections','forEach, removeIf and Comparator in action.',`import java.util.*;
public class Main {
    public static void main(String[] args) {
        ArrayList<String> words = new ArrayList<>();
        words.add("banana");
        words.add("fig");
        words.add("apple");
        words.add("kiwi");
        words.forEach(w -> System.out.println("word: " + w));
        words.removeIf(w -> w.length() < 4);
        System.out.println(words);
        words.sort((a, b) -> a.length() - b.length());
        System.out.println(words);
        Collections.sort(words, (a, b) -> b.compareTo(a));
        words.forEach(System.out::println);
    }
}`,['forEach runs a lambda on every element.','removeIf deletes everything the Predicate accepts.','A Comparator lambda replaces a whole sorting class.']),
    ] },
);

Object.assign(W3_LESSON, {
  'abstraction-1': 'java_abstract.asp',
  'abstraction-2': 'java_abstract.asp',
  'abstraction-3': 'java_enums.asp',
  'abstraction-4': 'java_enums.asp',
  'abstraction-5': 'java_inner_classes.asp',
  'abstraction-6': 'java_inner_classes.asp',
  'exceptions-1': 'java_try_catch.asp',
  'exceptions-2': 'java_try_catch.asp',
  'exceptions-3': 'java_try_catch.asp',
  'exceptions-4': 'java_exceptions.asp',
  'exceptions-5': 'java_exceptions.asp',
  'exceptions-6': 'java_exceptions.asp',
  'collections-1': 'java_arraylist.asp',
  'collections-2': 'java_linkedlist.asp',
  'collections-3': 'java_hashmap.asp',
  'collections-4': 'java_hashset.asp',
  'collections-5': 'java_hashmap.asp',
  'collections-6': 'java_iterator.asp',
  'collections-7': 'java_arraylist.asp',
  'collections-8': 'java_wrapper_classes.asp',
  'lambdas-1': 'java_generics.asp',
  'lambdas-2': 'java_generics.asp',
  'lambdas-3': 'java_lambda.asp',
  'lambdas-4': 'java_lambda.asp',
  'lambdas-5': 'java_lambda.asp',
});

const QUIZ_MID = [
  { q: 'Which keyword prevents a class from being instantiated directly?', o: ['final', 'static', 'abstract', 'private'], a: 2, why: 'An abstract class is an incomplete blueprint; only subclasses can be created.' },
  { q: 'Which enum method returns all the constants as an array?', o: ['all()', 'values()', 'list()', 'constants()'], a: 1, why: 'Every enum has a static values() method.' },
  { q: 'What does finally guarantee?', o: ['It runs only on error', 'It runs only on success', 'It runs whether or not an exception occurred', 'It stops the program'], a: 2, why: 'finally is for cleanup that must always happen.' },
  { q: 'In multiple catch blocks, where should Exception go?', o: ['First', 'Last', 'Anywhere', 'It is not allowed'], a: 1, why: 'The general type must come after specific ones, or the specific ones become unreachable.' },
  { q: 'Which exception type is unchecked?', o: ['Exception', 'IOException', 'RuntimeException', 'Throwable'], a: 2, why: 'RuntimeException and its subclasses need no throws or catch.' },
  { q: 'What does a HashSet do with a duplicate add?', o: ['Throws an error', 'Stores it twice', 'Ignores it and add returns false', 'Overwrites the old one'], a: 2, why: 'Sets hold unique elements; add returns false for a duplicate.' },
  { q: 'Which map keeps keys in sorted order?', o: ['HashMap', 'TreeMap', 'LinkedHashMap', 'Hashtable'], a: 1, why: 'TreeMap sorts keys; LinkedHashMap keeps insertion order.' },
  { q: 'What does Function<Integer, String> mean?', o: ['Takes a String, returns an Integer', 'Takes an Integer, returns a String', 'Takes nothing', 'Returns void'], a: 1, why: 'Function<T, R> maps an input of type T to a result of type R.' },
];
