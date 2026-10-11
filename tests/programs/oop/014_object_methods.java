import java.util.*;

public class Main {
    static class Person {
        final String name; final int age;
        Person(String n, int a) { name = n; age = a; }
        @Override public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Person p = (Person) o;
            return age == p.age && name.equals(p.name);
        }
        @Override public int hashCode() { return Objects.hash(name, age); }
        @Override public String toString() { return "Person{" + name + "," + age + "}"; }
    }
    static class Student extends Person {
        Student(String n, int a) { super(n, a); }
    }
    static class NoEq { int v; NoEq(int v) { this.v = v; } public String toString() { return "NoEq" + v; } }
    public static void main(String[] args) {
        Person a = new Person("Ann", 30), b = new Person("Ann", 30), c = new Person("Bob", 30);
        Student s = new Student("Ann", 30);
        System.out.println((a == b) + " " + a.equals(b) + " " + a.equals(c) + " " + a.equals(null));
        System.out.println(a.equals(s) + " " + s.equals(a));
        System.out.println((a.hashCode() == b.hashCode()) + " " + (a.hashCode() == c.hashCode()));
        System.out.println(a.hashCode() == Objects.hash("Ann", 30));
        System.out.println(a.getClass().getSimpleName() + " " + s.getClass().getSimpleName());
        System.out.println(a.getClass().getName() + " " + s.getClass().getName());
        System.out.println(a.getClass() == b.getClass());
        System.out.println(a.getClass() == s.getClass());
        System.out.println(new int[0].getClass().getSimpleName() + " " + "s".getClass().getName() + " " + Integer.valueOf(1).getClass().getSimpleName());
        Set<Person> set = new HashSet<>();
        set.add(a); set.add(b); set.add(c); set.add(s);
        System.out.println(set.size() + " " + set.contains(new Person("Bob", 30)));
        Map<Person, String> m = new HashMap<>();
        m.put(a, "first"); m.put(b, "second"); m.put(c, "third");
        System.out.println(m.size() + " " + m.get(new Person("Ann", 30)) + " " + m.get(s));
        NoEq n1 = new NoEq(1), n2 = new NoEq(1);
        System.out.println(n1.equals(n2) + " " + n1.equals(n1));
        Set<NoEq> ns = new HashSet<>(List.of(n1, n2));
        System.out.println(ns.size());
        System.out.println(Objects.equals(null, null) + " " + Objects.equals(a, null) + " " + Objects.hashCode(null) + " " + Objects.toString(null) + " " + Objects.toString(null, "dflt"));
        System.out.println("abc".hashCode() + " " + Integer.valueOf(7).hashCode() + " " + Boolean.TRUE.hashCode() + " " + Long.valueOf(1L << 32).hashCode() + " " + Character.valueOf('a').hashCode());
        System.out.println(List.of(1, 2, 3).hashCode() + " " + Arrays.hashCode(new int[]{1, 2, 3}) + " " + Objects.hash(1, 2, 3));
        System.out.println(Double.valueOf(1.5).hashCode() + " " + "".hashCode());
    }
}
