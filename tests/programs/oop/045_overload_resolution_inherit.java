import java.util.*;

public class Main {
    static class Animal { public String toString() { return "Animal"; } }
    static class Dog extends Animal { public String toString() { return "Dog"; } }
    static class Puppy extends Dog { public String toString() { return "Puppy"; } }
    static String feed(Animal a) { return "feed-animal"; }
    static String feed(Dog d) { return "feed-dog"; }
    static String feed(Object o) { return "feed-object"; }
    static String num(long x) { return "long"; }
    static String num(Integer x) { return "Integer"; }
    static String num(Object x) { return "Object"; }
    static String num(int... x) { return "varargs"; }
    static String two(int a, long b) { return "int,long"; }
    static String two(long a, int b) { return "long,int"; }
    static String two(double a, double b) { return "double,double"; }
    static String sb(CharSequence cs) { return "CharSequence"; }
    static String sb(String s) { return "String"; }
    static String sb(Object o) { return "Object"; }
    static class Base {
        String m(Object o) { return "Base.m(Object)"; }
        String m(String s) { return "Base.m(String)"; }
        String call(Object o) { return m(o); }
    }
    static class Derived extends Base {
        @Override String m(Object o) { return "Derived.m(Object)"; }
        String m(Integer i) { return "Derived.m(Integer)"; }
    }
    public static void main(String[] args) {
        Animal a = new Puppy(); Dog d = new Puppy(); Puppy p = new Puppy();
        System.out.println(feed(a) + " " + feed(d) + " " + feed(p) + " " + feed("s") + " " + feed((Object) p) + " " + feed(null == null ? d : a));
        byte b = 1; short s = 2; char c = 'c'; long l = 4;
        System.out.println(num(b) + " " + num(s) + " " + num(c) + " " + num(5) + " " + num(l) + " " + num(Integer.valueOf(5)) + " " + num(2.5) + " " + num() + " " + num(1, 2));
        System.out.println(two(1, 2L) + " " + two(1L, 2) + " " + two(1.0, 2) + " " + two(1f, 2f));
        System.out.println(sb("x") + " " + sb(new StringBuilder("x")) + " " + sb(5) + " " + sb((CharSequence) "y") + " " + sb((Object) "z"));
        Base bs = new Derived();
        Derived dv = new Derived();
        System.out.println(bs.m("str") + " | " + bs.m((Object) "str") + " | " + bs.call("str") + " | " + bs.call(5));
        System.out.println(dv.m("str") + " | " + dv.m(5) + " | " + dv.m(5L) + " | " + dv.m((Object) 5) + " | " + dv.call(7));
        List<Integer> li = new ArrayList<>(List.of(10, 20, 30, 40));
        li.remove(1);
        li.remove(Integer.valueOf(40));
        System.out.println(li);
        StringBuilder sbd = new StringBuilder();
        sbd.append('a').append((int) 'a').append("b").append(1.0f).append((Object) "o").append(new char[] { 'x' }).append(7L);
        System.out.println(sbd);
        System.out.println(String.valueOf((Object) null) + " " + String.valueOf(1 > 0) + " " + Math.max(1, 2L) + " " + Math.max(1, 2.5f) + " " + Math.abs(-3) + " " + Math.abs(-3.5));
    }
}
