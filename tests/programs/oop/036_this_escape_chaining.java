import java.util.*;

public class Main {
    static class Node {
        final String name; Node next; static final List<Node> REGISTRY = new ArrayList<>();
        Node(String n) { name = n; REGISTRY.add(this); }
        Node then(Node n) { next = n; return n; }
        Node self() { return this; }
        public String toString() { return name + (next == null ? "" : "->" + next); }
    }
    static class Fluent {
        private final StringBuilder sb = new StringBuilder();
        Fluent add(String s) { sb.append(s); return this; }
        Fluent sep() { return add(","); }
        Fluent addAll(String... ss) { for (int i = 0; i < ss.length; i++) { if (i > 0) sep(); add(ss[i]); } return this; }
        public String toString() { return sb.toString(); }
    }
    static class Child {
        final Parent parent; final int idx;
        Child(Parent p, int i) { parent = p; idx = i; }
        String path() { return parent.name + "/" + idx; }
    }
    static class Parent {
        final String name; final List<Child> kids = new ArrayList<>();
        Parent(String n, int k) { name = n; for (int i = 0; i < k; i++) kids.add(new Child(this, i)); }
    }
    static class Early {
        static Early last;
        int v = 1;
        Early() { last = this; v = 2; System.out.println("in ctor v=" + last.v); }
        Early(int v) { this(); this.v = v; }
    }
    public static void main(String[] args) {
        Node a = new Node("a"), b = new Node("b"), c = new Node("c");
        a.then(b).then(c);
        System.out.println(a + " " + b + " " + c);
        System.out.println(a.self() == a);
        c.then(a);
        System.out.println(c.next == a);
        System.out.println(Node.REGISTRY.size());
        System.out.println(new Fluent().add("x").sep().add("y").addAll("p", "q", "r").sep().addAll());
        Parent p = new Parent("root", 3);
        for (Child k : p.kids) System.out.println(k.path() + " " + (k.parent == p));
        Early e = new Early(9);
        System.out.println(e.v + " " + (Early.last == e) + " " + Early.last.v);
    }
}
