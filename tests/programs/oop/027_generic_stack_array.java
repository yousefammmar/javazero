import java.util.*;

public class Main {
    static class Stack<E> implements Iterable<E> {
        private E[] data;
        private int size;
        @SuppressWarnings("unchecked")
        Stack(int cap) { data = (E[]) new Object[cap]; }
        void push(E e) {
            if (size == data.length) data = Arrays.copyOf(data, size * 2);
            data[size++] = e;
        }
        E pop() {
            if (size == 0) throw new RuntimeException("empty stack");
            E e = data[--size];
            data[size] = null;
            return e;
        }
        E peek() { return size == 0 ? null : data[size - 1]; }
        int size() { return size; }
        boolean isEmpty() { return size == 0; }
        public Iterator<E> iterator() {
            return new Iterator<E>() {
                int i = size - 1;
                public boolean hasNext() { return i >= 0; }
                public E next() { return data[i--]; }
            };
        }
        public String toString() { StringBuilder sb = new StringBuilder("["); for (E e : this) sb.append(e).append(sb.length() > 1 ? "" : "").append(" "); return sb.toString().trim() + "]"; }
    }
    static class Queue2<T> {
        private final Stack<T> in = new Stack<>(2), out = new Stack<>(2);
        void add(T t) { in.push(t); }
        T poll() {
            if (out.isEmpty()) while (!in.isEmpty()) out.push(in.pop());
            return out.isEmpty() ? null : out.pop();
        }
    }
    interface Transformer<A, B> { B apply(A a); }
    static <A, B> Stack<B> mapStack(Stack<A> s, Transformer<A, B> t) {
        Stack<B> tmp = new Stack<>(1), res = new Stack<>(1);
        for (A a : s) tmp.push(t.apply(a));
        for (B b : tmp) res.push(b);
        return res;
    }
    public static void main(String[] args) {
        Stack<Integer> s = new Stack<>(1);
        for (int i = 1; i <= 5; i++) s.push(i * i);
        System.out.println(s + " size=" + s.size() + " peek=" + s.peek());
        System.out.println(s.pop() + " " + s.pop() + " " + s);
        int sum = 0;
        for (int v : s) sum += v;
        System.out.println("sum=" + sum);
        Stack<String> ss = mapStack(s, i -> "#" + i);
        System.out.println(ss);
        Stack<Integer> lens = mapStack(ss, String::length);
        System.out.println(lens);
        while (!s.isEmpty()) s.pop();
        try { s.pop(); } catch (RuntimeException e) { System.out.println(e.getMessage()); }
        System.out.println(s.peek());
        Queue2<String> q = new Queue2<>();
        q.add("a"); q.add("b"); q.add("c");
        System.out.print(q.poll() + q.poll());
        q.add("d");
        System.out.println(q.poll() + q.poll() + q.poll());
        Stack<int[]> arrs = new Stack<>(2);
        arrs.push(new int[]{1, 2}); arrs.push(new int[]{3});
        System.out.println(arrs.pop().length + " " + arrs.pop().length);
    }
}
