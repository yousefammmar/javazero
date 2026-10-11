public class Main {
    static String log(String s) { System.out.println("  " + s); return s; }
    static class Base {
        String bf = log("Base field init");
        { log("Base init block"); }
        Base() { log("Base ctor"); hook(); }
        void hook() { log("Base.hook"); }
    }
    static class Derived extends Base {
        String df = log("Derived field init");
        int n = 42;
        { log("Derived init block n=" + n); }
        Derived() { super(); log("Derived ctor n=" + n + " df=" + df); }
        @Override void hook() { log("Derived.hook n=" + n + " df=" + df); }
    }
    static class Third extends Derived {
        final String tag = "T";
        String name = "third";
        Third() { log("Third ctor"); }
        @Override void hook() { log("Third.hook name=" + name + " tag=" + tag); }
    }
    public static void main(String[] args) {
        System.out.println("new Base");
        new Base();
        System.out.println("new Derived");
        new Derived();
        System.out.println("new Third");
        new Third();
        System.out.println("again Third");
        Base b = new Third();
        b.hook();
    }
}
