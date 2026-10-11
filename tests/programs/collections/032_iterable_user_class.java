import java.util.*;
import java.util.function.*;
public class Main {
    static class Range implements Iterable<Integer> {
        final int lo, hi, step;
        Range(int lo, int hi, int step) { this.lo = lo; this.hi = hi; this.step = step; }
        public Iterator<Integer> iterator() {
            return new Iterator<>() {
                int cur = lo;
                public boolean hasNext() { return cur < hi; }
                public Integer next() { if (!hasNext()) throw new NoSuchElementException(); int v = cur; cur += step; return v; }
            };
        }
    }
    static class MyList<T> implements Iterable<T> {
        private Object[] data = new Object[2];
        private int size;
        void add(T t) { if (size == data.length) data = Arrays.copyOf(data, size * 2); data[size++] = t; }
        @SuppressWarnings("unchecked") T get(int i) { if (i >= size) throw new IndexOutOfBoundsException("idx " + i); return (T) data[i]; }
        int size() { return size; }
        public Iterator<T> iterator() {
            return new Iterator<T>() {
                int i = 0;
                public boolean hasNext() { return i < size; }
                public T next() { return get(i++); }
            };
        }
    }
    static class Node<T> { T val; Node<T> next; Node(T v, Node<T> n) { val = v; next = n; } }
    static class Chain<T> implements Iterable<T> {
        Node<T> head;
        void prepend(T v) { head = new Node<>(v, head); }
        public Iterator<T> iterator() {
            return new Iterator<T>() {
                Node<T> n = head;
                public boolean hasNext() { return n != null; }
                public T next() { T v = n.val; n = n.next; return v; }
            };
        }
    }
    static <T> List<T> toList(Iterable<T> it) { List<T> l = new ArrayList<>(); for (T t : it) l.add(t); return l; }
    public static void main(String[] args) {
        for (int x : new Range(0, 10, 3)) System.out.print(x + " ");
        System.out.println();
        System.out.println(toList(new Range(5, 8, 1)));
        MyList<String> ml = new MyList<>();
        for (String s : "alpha beta gamma delta epsilon".split(" ")) ml.add(s);
        for (String s : ml) System.out.print(s.toUpperCase() + ",");
        System.out.println(" size " + ml.size());
        try { ml.get(10); } catch (IndexOutOfBoundsException e) { System.out.println(e.getMessage()); }
        Chain<Integer> ch = new Chain<>();
        for (int i = 1; i <= 5; i++) ch.prepend(i * i);
        System.out.println(toList(ch));
        int sum = 0;
        for (int v : ch) sum += v;
        System.out.println("sum " + sum);
        new Range(0, 3, 1).forEach(i -> System.out.print("fe" + i + " "));
        System.out.println();
        Iterator<Integer> it = new Range(0, 1, 1).iterator();
        it.next();
        try { it.next(); } catch (NoSuchElementException e) { System.out.println("exhausted"); }
        try { it.remove(); } catch (UnsupportedOperationException e) { System.out.println("remove unsupported by default"); }
        Spliterator<Integer> sp = new Range(0, 4, 1).spliterator();
        sp.forEachRemaining(v -> System.out.print("[" + v + "]"));
        System.out.println();
        List<Integer> combined = new ArrayList<>();
        for (int a : new Range(0, 3, 1)) for (int b : new Range(0, 3, 1)) if (a < b) combined.add(a * 10 + b);
        System.out.println(combined);
    }
}
