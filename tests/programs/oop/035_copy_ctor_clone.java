import java.util.*;

public class Main {
    static class Address {
        String city; int zip;
        Address(String c, int z) { city = c; zip = z; }
        Address(Address o) { this(o.city, o.zip); }
        public String toString() { return city + ":" + zip; }
    }
    static class Person {
        String name; Address addr; List<String> tags;
        Person(String n, Address a, List<String> t) { name = n; addr = a; tags = t; }
        Person(Person o) { name = o.name; addr = new Address(o.addr); tags = new ArrayList<>(o.tags); }
        Person shallow() { return new Person(name, addr, tags); }
        public String toString() { return name + "@" + addr + tags; }
    }
    static class Matrix {
        final int[][] cells;
        Matrix(int[][] c) { cells = new int[c.length][]; for (int i = 0; i < c.length; i++) cells[i] = c[i].clone(); }
        Matrix(Matrix m) { this(m.cells); }
        public String toString() { return Arrays.deepToString(cells); }
    }
    static class Pt implements Cloneable {
        int x; int[] arr = {1, 2};
        @Override public Pt clone() {
            try { Pt p = (Pt) super.clone(); return p; } catch (CloneNotSupportedException e) { throw new AssertionError(e); }
        }
    }
    public static void main(String[] args) {
        Person a = new Person("Ann", new Address("Oslo", 150), new ArrayList<>(List.of("x")));
        Person deep = new Person(a), shallow = a.shallow();
        a.addr.city = "Rome"; a.tags.add("y"); a.name = "Anna";
        System.out.println(a);
        System.out.println(deep);
        System.out.println(shallow);
        System.out.println((deep.addr == a.addr) + " " + (shallow.addr == a.addr) + " " + (shallow.tags == a.tags));
        int[][] src = { {1, 2}, {3, 4} };
        Matrix m = new Matrix(src), m2 = new Matrix(m);
        src[0][0] = 99; m.cells[1][1] = -1;
        System.out.println(m + " " + m2 + " " + Arrays.deepToString(src));
        int[] orig = { 1, 2, 3 };
        int[] c1 = orig.clone(), c2 = orig, c3 = Arrays.copyOf(orig, 5);
        orig[0] = 100;
        System.out.println(Arrays.toString(c1) + Arrays.toString(c2) + Arrays.toString(c3));
        Pt p = new Pt(); p.x = 5;
        Pt q = p.clone();
        q.x = 6; q.arr[0] = 42;
        System.out.println(p.x + " " + q.x + " " + p.arr[0] + " " + (p.arr == q.arr) + " " + (p != q));
        List<Address> orig2 = new ArrayList<>(List.of(new Address("A", 1)));
        List<Address> copy = new ArrayList<>(orig2);
        copy.add(new Address("B", 2)); copy.get(0).zip = 9;
        System.out.println(orig2 + " " + copy);
    }
}
