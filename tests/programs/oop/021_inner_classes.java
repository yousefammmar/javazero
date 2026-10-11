public class Main {
    private int base = 10;
    private static int sbase = 100;
    private String name;
    Main(String n) { name = n; }
    class Inner {
        int base = 1;
        int val() { return base + Main.this.base + sbase; }
        String who() { return "Inner of " + name; }
        void bump() { Main.this.base++; base += 10; }
    }
    class Deep {
        class Deeper {
            String path() { return name + "/" + base; }
        }
    }
    static class Nested {
        int x = 5;
        int val() { return x + sbase; }
        Main make(String n) { return new Main(n); }
    }
    Inner create() { return new Inner(); }
    int sumWith(Inner... is) { int s = 0; for (Inner i : is) s += i.val(); return s; }
    public static void main(String[] args) {
        Main o1 = new Main("one"), o2 = new Main("two");
        Main.Inner i1 = o1.new Inner();
        Inner i2 = o2.create();
        Inner i3 = o1.new Inner();
        System.out.println(i1.val() + " " + i2.val() + " " + i3.who() + " " + i2.who());
        i1.bump();
        System.out.println(i1.val() + " " + i3.val() + " " + i2.val());
        System.out.println(o1.base + " " + o2.base + " " + i1.base);
        sbase = 200;
        System.out.println(i1.val() + " " + o1.sumWith(i1, i3));
        Main.Deep.Deeper dd = o2.new Deep().new Deeper();
        System.out.println(dd.path());
        o2.base = 77;
        System.out.println(dd.path());
        Main.Nested n = new Main.Nested();
        System.out.println(n.val() + " " + n.make("three").name);
        Nested n2 = new Nested();
        n2.x = 9;
        System.out.println(n2.val() + " " + n.val());
        System.out.println(i1.getClass().getSimpleName() + " " + n.getClass().getSimpleName());
        System.out.println(i1.getClass().getName() + " " + n.getClass().getName());
    }
}
