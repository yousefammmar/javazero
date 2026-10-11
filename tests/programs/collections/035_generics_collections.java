import java.util.*;
import java.util.function.*;
public class Main {
    static class Pair<A, B> {
        final A first; final B second;
        Pair(A a, B b) { first = a; second = b; }
        <C> Pair<A, C> withSecond(C c) { return new Pair<>(first, c); }
        static <X extends Comparable<X>> X maxOf(X a, X b) { return a.compareTo(b) >= 0 ? a : b; }
        @Override public String toString() { return "<" + first + "," + second + ">"; }
    }
    static class Box<T extends Comparable<T>> {
        private final List<T> items = new ArrayList<>();
        void add(T t) { items.add(t); }
        T max() { T best = items.get(0); for (T t : items) if (t.compareTo(best) > 0) best = t; return best; }
        List<T> sorted() { List<T> c = new ArrayList<>(items); Collections.sort(c); return c; }
    }
    static double sum(Collection<? extends Number> c) { double s = 0; for (Number n : c) s += n.doubleValue(); return s; }
    static void fill(List<? super Integer> l, int n) { for (int i = 0; i < n; i++) l.add(i); }
    static <T> void swap(List<T> l, int i, int j) { T t = l.get(i); l.set(i, l.get(j)); l.set(j, t); }
    static <K, V extends Comparable<V>> K argMax(Map<K, V> m) {
        K best = null; V bv = null;
        for (Map.Entry<K, V> e : m.entrySet()) if (bv == null || e.getValue().compareTo(bv) > 0) { best = e.getKey(); bv = e.getValue(); }
        return best;
    }
    static <T, R> List<R> map(List<T> in, Function<? super T, ? extends R> f) { List<R> out = new ArrayList<>(); for (T t : in) out.add(f.apply(t)); return out; }
    static <T> List<T> filter(Collection<T> in, Predicate<T> p) { List<T> out = new ArrayList<>(); for (T t : in) if (p.test(t)) out.add(t); return out; }
    static <T> T reduce(List<T> in, T id, BinaryOperator<T> op) { T acc = id; for (T t : in) acc = op.apply(acc, t); return acc; }
    public static void main(String[] args) {
        Pair<String, Integer> p = new Pair<>("age", 30);
        System.out.println(p + " " + p.withSecond(2.5) + " " + p.withSecond(List.of(1)));
        System.out.println(Pair.maxOf("pear", "apple") + " " + Pair.maxOf(3, 9) + " " + Pair.maxOf(2.5, -1.0));
        Box<String> b = new Box<>();
        for (String s : "kiwi fig apple".split(" ")) b.add(s);
        System.out.println(b.max() + " " + b.sorted());
        System.out.println(sum(List.of(1, 2, 3)) + " " + sum(Set.of(2.5)) + " " + sum(new ArrayList<Long>(List.of(10L, 20L))));
        List<Number> nl = new ArrayList<>(); List<Object> ol = new ArrayList<>();
        fill(nl, 3); fill(ol, 2);
        System.out.println(nl + " " + ol);
        List<String> sl = new ArrayList<>(List.of("a", "b", "c"));
        swap(sl, 0, 2);
        System.out.println(sl);
        Map<String, Double> scores = new HashMap<>();
        scores.put("x", 1.5); scores.put("y", 9.25); scores.put("z", 3.0);
        System.out.println(argMax(scores));
        System.out.println(map(List.of(1, 2, 3), x -> "n" + x) + " " + map(List.of("a", "bb"), String::length));
        System.out.println(filter(List.of(1, 2, 3, 4, 5, 6), x -> x % 2 == 0) + " " + filter(new TreeSet<>(List.of("a", "bb", "ccc")), s -> s.length() > 1));
        System.out.println(reduce(List.of(1, 2, 3, 4), 0, Integer::sum) + " " + reduce(List.of("a", "b", "c"), "", String::concat));
        Map<String, List<Pair<Integer, String>>> complex = new TreeMap<>();
        complex.computeIfAbsent("k", x -> new ArrayList<>()).add(new Pair<>(1, "one"));
        complex.computeIfAbsent("k", x -> new ArrayList<>()).add(new Pair<>(2, "two"));
        System.out.println(complex);
        List<? extends Number> wild = List.of(1, 2.5, 3L);
        Number first = wild.get(0);
        System.out.println(first + " " + wild.size());
        Optional<Pair<String, Integer>> op = Optional.of(p);
        System.out.println(op.map(q -> q.second * 2).orElse(0));
    }
}
