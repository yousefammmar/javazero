import java.util.*;
import java.util.function.*;

public class Main {
    static int sCount = 0;
    int field = 5;
    static abstract class Task {
        final String label;
        Task(String l) { label = l; sCount++; }
        abstract int run(int x);
        String describe() { return label + "#" + sCount; }
    }
    String outerHelper(String s) { return "[" + s + field + "]"; }
    List<Supplier<String>> build() {
        List<Supplier<String>> out = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            final int k = i * 10;
            String tag = "t" + i;
            class Local implements Supplier<String> {
                public String get() { return tag + ":" + (k + field) + ":" + outerHelper(tag); }
            }
            out.add(new Local());
            out.add(new Supplier<String>() { public String get() { return "anon" + k + outerHelper("a"); } });
        }
        field = 50;
        return out;
    }
    public static void main(String[] args) {
        int base = 7;
        String word = "w";
        class Adder {
            final int n; Adder(int n) { this.n = n; }
            int add(int x) { return x + n + base; }
        }
        Adder a1 = new Adder(1), a2 = new Adder(100);
        System.out.println(a1.add(1) + " " + a2.add(1));
        Task t = new Task("double") { int calls; int run(int x) { calls++; return x * 2 + base + calls; } };
        System.out.println(t.describe() + " " + t.run(5) + " " + t.run(5));
        Task t2 = new Task("neg") { int run(int x) { return -x; } @Override String describe() { return "custom-" + super.describe(); } };
        System.out.println(t2.describe() + " " + t2.run(3) + " " + sCount);
        Main m = new Main();
        for (Supplier<String> s : m.build()) System.out.println(s.get());
        Runnable r = new Runnable() {
            int n = 0;
            public void run() { n++; System.out.println(word + n + base); }
        };
        r.run(); r.run();
        Comparator<String> cmp = new Comparator<>() {
            public int compare(String x, String y) { return y.compareTo(x); }
        };
        List<String> l = new ArrayList<>(List.of("b", "c", "a"));
        l.sort(cmp);
        System.out.println(l);
        Object o = new Object() { public String toString() { return "anon-object " + base; } };
        System.out.println(o);
        var anonVar = new Object() { int hidden = 42; int twice() { return hidden * 2; } };
        System.out.println(anonVar.hidden + " " + anonVar.twice());
    }
}
