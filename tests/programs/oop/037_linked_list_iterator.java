import java.util.*;

public class Main {
    static class MyList<T> implements Iterable<T> {
        private class Node { T val; Node next; Node(T v) { val = v; } }
        private Node head, tail;
        private int size;
        void add(T v) { Node n = new Node(v); if (head == null) head = tail = n; else { tail.next = n; tail = n; } size++; }
        void addFirst(T v) { Node n = new Node(v); n.next = head; head = n; if (tail == null) tail = n; size++; }
        T get(int i) {
            if (i < 0 || i >= size) throw new IndexOutOfBoundsException("index " + i + " size " + size);
            Node n = head; while (i-- > 0) n = n.next; return n.val;
        }
        boolean remove(T v) {
            Node prev = null;
            for (Node n = head; n != null; prev = n, n = n.next) {
                if (Objects.equals(n.val, v)) {
                    if (prev == null) head = n.next; else prev.next = n.next;
                    if (n == tail) tail = prev;
                    size--; return true;
                }
            }
            return false;
        }
        void reverse() {
            Node prev = null, cur = head; tail = head;
            while (cur != null) { Node nx = cur.next; cur.next = prev; prev = cur; cur = nx; }
            head = prev;
        }
        int size() { return size; }
        public Iterator<T> iterator() {
            return new Iterator<T>() {
                Node cur = head;
                public boolean hasNext() { return cur != null; }
                public T next() { T v = cur.val; cur = cur.next; return v; }
            };
        }
        public String toString() { StringJoiner sj = new StringJoiner(", ", "[", "]"); for (T t : this) sj.add(String.valueOf(t)); return sj.toString(); }
    }
    public static void main(String[] args) {
        MyList<Integer> l = new MyList<>();
        for (int i = 1; i <= 5; i++) l.add(i * 10);
        l.addFirst(0);
        System.out.println(l + " size " + l.size());
        System.out.println(l.get(0) + " " + l.get(3) + " " + l.get(5));
        try { l.get(6); } catch (IndexOutOfBoundsException e) { System.out.println(e.getMessage()); }
        System.out.println(l.remove(30) + " " + l.remove(99) + " " + l.remove(0) + " " + l.remove(50) + " " + l);
        l.add(60);
        System.out.println(l + " " + l.size());
        l.reverse();
        System.out.println(l);
        l.add(5);
        System.out.println(l);
        int sum = 0;
        for (int v : l) sum += v;
        System.out.println(sum);
        MyList<String> s = new MyList<>();
        s.add("a"); s.add(null); s.add("c");
        System.out.println(s + " " + s.remove(null) + " " + s);
        Iterator<String> it = s.iterator();
        while (it.hasNext()) System.out.print(it.next() + ";");
        System.out.println();
        MyList<Integer> empty = new MyList<>();
        empty.reverse();
        System.out.println(empty + " " + empty.size());
    }
}
