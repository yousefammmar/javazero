import java.util.*;

public class Main {
    record Point(int x, int y) {
        static final Point ORIGIN = new Point(0, 0);
        static Point of(int v) { return new Point(v, v); }
        double dist() { return Math.sqrt(x * x + y * y); }
        Point plus(Point o) { return new Point(x + o.x, y + o.y); }
    }
    record Range(int lo, int hi) {
        Range {
            if (lo > hi) { int t = lo; lo = hi; hi = t; }
            System.out.println("compact " + lo + ".." + hi);
        }
        int len() { return hi - lo; }
    }
    record Person(String name, int age) {
        Person {
            Objects.requireNonNull(name, "name");
            if (age < 0) throw new IllegalArgumentException("age " + age);
            name = name.trim();
        }
        Person(String name) { this(name, 18); }
        String initials() { return name.substring(0, 1); }
        @Override public String toString() { return name + "(" + age + ")"; }
    }
    record Pair<A, B>(A first, B second) {
        <C> Pair<A, C> withSecond(C c) { return new Pair<>(first, c); }
    }
    record Wrapper(int[] data, List<String> names) {}
    record Empty() {}
    public static void main(String[] args) {
        Point p = new Point(3, 4), q = new Point(3, 4);
        System.out.println(p + " " + p.x() + " " + p.y() + " " + p.dist());
        System.out.println(p.equals(q) + " " + (p == q) + " " + (p.hashCode() == q.hashCode()) + " " + p.equals(Point.ORIGIN));
        System.out.println(Point.of(2).plus(p) + " " + Point.ORIGIN);
        Range r = new Range(9, 2);
        System.out.println(r + " len=" + r.len());
        System.out.println(new Range(1, 1).equals(new Range(1, 1)));
        Person a = new Person("  Ann ", 30);
        System.out.println(a + " " + a.name().length() + " " + a.initials() + " " + new Person("Bob"));
        System.out.println(a.equals(new Person("Ann", 30)));
        try { new Person("x", -1); } catch (IllegalArgumentException e) { System.out.println("IAE " + e.getMessage()); }
        try { new Person(null, 1); } catch (NullPointerException e) { System.out.println("NPE " + e.getMessage()); }
        Pair<String, Integer> pr = new Pair<>("k", 1);
        System.out.println(pr + " " + pr.withSecond(2.5) + " " + pr.first().length());
        System.out.println(new Pair<>(null, null) + " " + new Pair<>(null, null).equals(new Pair<>(null, null)));
        Wrapper w1 = new Wrapper(new int[]{1}, List.of("a"));
        Wrapper w2 = new Wrapper(w1.data(), List.of("a"));
        System.out.println(w1.equals(w2) + " " + w1.equals(new Wrapper(new int[]{1}, List.of("a"))));
        System.out.println(new Empty() + " " + new Empty().equals(new Empty()) + " " + new Empty().hashCode());
        Set<Point> set = new HashSet<>(List.of(p, Point.ORIGIN));
        System.out.println(set.contains(new Point(0, 0)) + " " + set.size());
        Map<Point, String> m = new TreeMap<>(Comparator.comparingInt(Point::x).thenComparingInt(Point::y));
        m.put(new Point(2, 1), "b"); m.put(new Point(1, 5), "a"); m.put(new Point(2, 0), "c");
        System.out.println(m);
        System.out.println(new Point(-1, 2).hashCode() == new Point(-1, 2).hashCode());
    }
}
