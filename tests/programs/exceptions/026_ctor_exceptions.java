public class Main {
    static int created = 0;
    static class Person {
        final String name; final int age;
        Person(String name, int age) {
            System.out.println("constructing " + name);
            if (name == null || name.isBlank()) throw new IllegalArgumentException("name required");
            if (age < 0 || age > 150) throw new IllegalArgumentException("age out of range: " + age);
            this.name = name; this.age = age; created++;
        }
        public String toString() { return name + "(" + age + ")"; }
    }
    static class Child extends Person {
        Child(String n, int a) {
            super(n, a);
            System.out.println("Child body");
            if (a > 17) throw new IllegalStateException("not a child: " + a);
        }
    }
    static class Init {
        int v = compute();
        { System.out.println("init block"); }
        Init() { System.out.println("Init ctor"); }
        int compute() { System.out.println("compute"); return Integer.parseInt("n/a"); }
    }
    static class Chain {
        Chain() { this(-1); }
        Chain(int x) { if (x < 0) throw new IllegalArgumentException("x<0"); }
    }
    public static void main(String[] args) {
        Object[][] data = {{"Ann", 30}, {"", 5}, {"Bob", 200}, {null, 1}, {"Cy", 10}};
        for (Object[] d : data) {
            try { Person p = new Person((String) d[0], (Integer) d[1]); System.out.println("ok " + p); }
            catch (IllegalArgumentException e) { System.out.println("fail: " + e.getMessage()); }
        }
        System.out.println("created=" + created);
        try { new Child("Kid", 10); new Child("Old", 40); } catch (IllegalStateException e) { System.out.println(e.getMessage()); }
        System.out.println("created=" + created);
        try { new Child("", 4); } catch (RuntimeException e) { System.out.println(e); }
        try { new Init(); } catch (NumberFormatException e) { System.out.println(e.getMessage()); }
        try { new Chain(); } catch (IllegalArgumentException e) { System.out.println(e.getMessage()); }
        Person p = null;
        try { p = new Person("Z", -4); } catch (IllegalArgumentException e) { System.out.println("p is " + p); }
    }
}
