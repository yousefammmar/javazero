public class Main {
    static int sCount = 0;
    int id;
    static final int CONST = 10;
    static int sInit;
    static { sInit = CONST * 2; System.out.println("static init Main " + sInit); }
    int iInit;
    { iInit = ++sCount * 100; System.out.println("instance init " + iInit); }
    Main() { id = sCount; System.out.println("ctor " + id); }
    Main(int extra) { this(); id += extra; System.out.println("ctor(int) " + id); }
    static int next() { return ++sCount; }
    int mine() { return id * 2; }

    static class Counter {
        private static int total;
        private int mine;
        void inc() { mine++; total++; }
        static int getTotal() { return total; }
        int getMine() { return mine; }
    }
    static class Base {
        static String who() { return "Base.who"; }
        String name() { return "Base.name"; }
        String hello() { return "hello from " + name() + "/" + who(); }
        String field = "baseField";
    }
    static class Derived extends Base {
        static String who() { return "Derived.who"; }
        @Override String name() { return "Derived.name"; }
        String field = "derivedField";
    }
    static class Lazy {
        static { System.out.println("Lazy loaded"); }
        static int value = 5;
        static void touch() { System.out.println("touch"); }
    }
    static class Order {
        static int a = init("a");
        int x = init("x");
        static int b = init("b");
        int y = init("y");
        static int init(String n) { System.out.println("init " + n); return 1; }
        Order() { System.out.println("Order()"); }
        static { System.out.println("Order static block"); }
        { System.out.println("Order instance block"); }
    }
    public static void main(String[] args) {
        System.out.println("main start " + CONST + " " + sCount);
        Main m1 = new Main();
        Main m2 = new Main(5);
        System.out.println(m1.id + " " + m2.id + " " + m1.mine() + " " + m2.mine() + " " + sCount + " " + next());
        Counter c1 = new Counter(), c2 = new Counter();
        c1.inc(); c1.inc(); c2.inc();
        System.out.println(c1.getMine() + " " + c2.getMine() + " " + Counter.getTotal());
        Base b = new Derived();
        System.out.println(b.hello());
        System.out.println(b.field + " " + ((Derived) b).field + " " + b.name() + " " + Base.who() + " " + Derived.who());
        System.out.println("before lazy");
        System.out.println(Lazy.value);
        Lazy.touch();
        System.out.println("before order");
        new Order();
        new Order();
        System.out.println("done");
    }
}
