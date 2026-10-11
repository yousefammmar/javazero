import java.util.*;

public class Main {
    static class Node<T> {
        T val; Node<T> next;
        Node(T v) { val = v; }
    }

    static class MyStack<T> implements Iterable<T> {
        private Node<T> head; private int size;
        void push(T v) { Node<T> n = new Node<>(v); n.next = head; head = n; size++; }
        T pop() {
            if (head == null) throw new RuntimeException("stack empty");
            T v = head.val; head = head.next; size--; return v;
        }
        T peek() { return head == null ? null : head.val; }
        boolean isEmpty() { return size == 0; }
        public Iterator<T> iterator() {
            return new Iterator<T>() {
                Node<T> cur = head;
                public boolean hasNext() { return cur != null; }
                public T next() { T v = cur.val; cur = cur.next; return v; }
            };
        }
    }

    static class MyQueue<T> {
        private Node<T> head, tail; private int size;
        void enqueue(T v) {
            Node<T> n = new Node<>(v);
            if (tail == null) head = tail = n; else { tail.next = n; tail = n; }
            size++;
        }
        T dequeue() {
            if (head == null) throw new RuntimeException("queue empty");
            T v = head.val; head = head.next;
            if (head == null) tail = null;
            size--; return v;
        }
        int size() { return size; }
    }

    static class LinkedList2<T> {
        Node<T> head;
        void addFirst(T v) { Node<T> n = new Node<>(v); n.next = head; head = n; }
        void addLast(T v) {
            Node<T> n = new Node<>(v);
            if (head == null) { head = n; return; }
            Node<T> c = head;
            while (c.next != null) c = c.next;
            c.next = n;
        }
        void reverse() {
            Node<T> prev = null, cur = head;
            while (cur != null) { Node<T> nx = cur.next; cur.next = prev; prev = cur; cur = nx; }
            head = prev;
        }
        boolean remove(T v) {
            if (head == null) return false;
            if (head.val.equals(v)) { head = head.next; return true; }
            Node<T> c = head;
            while (c.next != null && !c.next.val.equals(v)) c = c.next;
            if (c.next == null) return false;
            c.next = c.next.next;
            return true;
        }
        public String toString() {
            StringBuilder sb = new StringBuilder("[");
            for (Node<T> c = head; c != null; c = c.next) { sb.append(c.val); if (c.next != null) sb.append(" -> "); }
            return sb.append("]").toString();
        }
    }

    public static void main(String[] args) {
        MyStack<String> st = new MyStack<>();
        for (String s : "to be or not to be".split(" ")) st.push(s);
        System.out.println("peek=" + st.peek());
        for (String s : st) System.out.print(s + " ");
        System.out.println();
        while (!st.isEmpty()) System.out.print(st.pop() + ",");
        System.out.println();
        try { st.pop(); } catch (RuntimeException e) { System.out.println("error: " + e.getMessage()); }

        MyQueue<Integer> q = new MyQueue<>();
        for (int i = 1; i <= 5; i++) q.enqueue(i * i);
        System.out.println("size=" + q.size() + " first=" + q.dequeue() + " second=" + q.dequeue());
        q.enqueue(99);
        int sum = 0;
        while (q.size() > 0) sum += q.dequeue();
        System.out.println("sum of rest = " + sum);

        LinkedList2<Character> ll = new LinkedList2<>();
        for (char c : "hello".toCharArray()) ll.addLast(c);
        ll.addFirst('>');
        System.out.println(ll);
        ll.reverse();
        System.out.println(ll);
        System.out.println(ll.remove('l') + " " + ll.remove('z') + " " + ll);
    }
}
