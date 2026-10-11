import java.util.*;
public class Main {
    static class Animal {}
    static class Dog extends Animal {}
    static class Cat extends Animal {}
    interface Shape {}
    static class Sq implements Shape {}
    public static void main(String[] args) {
        Object o = "text";
        try { Integer i = (Integer) o; System.out.println(i); }
        catch (ClassCastException e) { System.out.println(e.getMessage()); System.out.println(e.getClass().getName()); }
        Object n = 42;
        try { String s = (String) n; System.out.println(s); }
        catch (ClassCastException e) { System.out.println(e.getMessage()); }
        Animal a = new Dog();
        try { Cat c = (Cat) a; System.out.println(c); }
        catch (ClassCastException e) { System.out.println(e.getMessage()); }
        try { Shape sh = (Shape) (Object) new Dog(); System.out.println(sh); }
        catch (ClassCastException e) { System.out.println(e.getMessage()); }
        Object d = 3.5;
        try { Integer x = (Integer) d; System.out.println(x); }
        catch (ClassCastException e) { System.out.println(e.getMessage()); }
        Object arr = new int[]{1};
        try { String[] sa = (String[]) arr; System.out.println(sa.length); }
        catch (ClassCastException e) { System.out.println(e.getMessage()); }
        List raw = new ArrayList();
        raw.add("s"); raw.add(1);
        try { for (Object x : raw) { Integer q = (Integer) x; System.out.println(q); } }
        catch (ClassCastException e) { System.out.println(e.getMessage()); }
        Object l = Long.valueOf(5);
        try { Integer q = (Integer) l; System.out.println(q); }
        catch (ClassCastException e) { System.out.println(e.getMessage()); }
        Object nul = null;
        String ok = (String) nul;
        System.out.println("null cast ok: " + ok);
        System.out.println(o instanceof Integer);
        try { Comparable<Object> cmp = (Comparable<Object>) (Object) new Dog(); System.out.println(cmp); }
        catch (ClassCastException e) { System.out.println(e.getMessage()); }
    }
}
