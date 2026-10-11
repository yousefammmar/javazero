import java.util.*;

public class Main {
    static class Box<T> {
        private T value;
        Box(T v) { value = v; }
        T get() { return value; }
        void set(T v) { value = v; }
        <R> Box<R> map(java.util.function.Function<? super T, ? extends R> f) { return new Box<>(f.apply(value)); }
        boolean isPresent() { return value != null; }
        public String toString() { return "Box(" + value + ")"; }
    }
    static class NumBox<N extends Number> extends Box<N> {
        NumBox(N n) { super(n); }
        double twice() { return get().doubleValue() * 2; }
    }
    static class Pair<K extends Comparable<K>, V> implements Comparable<Pair<K, V>> {
        final K k; final V v;
        Pair(K k, V v) { this.k = k; this.v = v; }
        public int compareTo(Pair<K, V> o) { return k.compareTo(o.k); }
        public String toString() { return k + "=" + v; }
    }
    static <T extends Comparable<T>> T max(T a, T b) { return a.compareTo(b) >= 0 ? a : b; }
    static <T extends Comparable<T>> T maxOf(List<T> l) {
        T best = l.get(0);
        for (T t : l) if (t.compareTo(best) > 0) best = t;
        return best;
    }
    static <T> void swap(T[] arr, int i, int j) { T t = arr[i]; arr[i] = arr[j]; arr[j] = t; }
    static double sum(List<? extends Number> l) { double s = 0; for (Number n : l) s += n.doubleValue(); return s; }
    static void fill(List<? super Integer> l, int n) { for (int i = 0; i < n; i++) l.add(i); }
    static <T> List<T> listOf(T a, T b) { List<T> l = new ArrayList<>(); l.add(a); l.add(b); return l; }
    @SafeVarargs static <T> int count(T... xs) { return xs.length; }
    public static void main(String[] args) {
        Box<String> bs = new Box<>("hi");
        Box<Integer> bi = bs.map(String::length);
        System.out.println(bs + " " + bi + " " + bi.map(x -> x * 2.5));
        bs.set(null);
        System.out.println(bs + " " + bs.isPresent());
        NumBox<Integer> nb = new NumBox<>(21);
        NumBox<Double> nd = new NumBox<>(1.25);
        System.out.println(nb.twice() + " " + nd.twice());
        System.out.println(max(3, 9) + " " + max("pear", "apple") + " " + max(2.5, 2.4) + " " + max('a', 'b'));
        System.out.println(maxOf(Arrays.asList(4, 8, 1)) + " " + maxOf(List.of("x", "yy", "ab")));
        Integer[] arr = { 1, 2, 3 };
        swap(arr, 0, 2);
        System.out.println(Arrays.toString(arr));
        String[] sa = { "a", "b" };
        Main.<String>swap(sa, 0, 1);
        System.out.println(Arrays.toString(sa));
        System.out.println(sum(List.of(1, 2, 3)) + " " + sum(List.of(1.5, 2.5)) + " " + sum(new ArrayList<Long>(List.of(10L))));
        List<Number> nums = new ArrayList<>();
        List<Object> objs = new ArrayList<>();
        fill(nums, 3); fill(objs, 2);
        System.out.println(nums + " " + objs);
        System.out.println(listOf(1, "a") + " " + listOf(1, 2).get(0).getClass().getSimpleName());
        System.out.println(count() + " " + count(1, 2, 3) + " " + count("a"));
        List<Pair<String, Integer>> ps = new ArrayList<>(List.of(new Pair<>("b", 1), new Pair<>("a", 2), new Pair<>("c", 0)));
        Collections.sort(ps);
        System.out.println(ps);
        Box<Box<String>> nested = new Box<>(new Box<>("deep"));
        System.out.println(nested + " " + nested.get().get().length());
        Object o = bi;
        Box<?> wild = (Box<?>) o;
        System.out.println(wild.get());
    }
}
