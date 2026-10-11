public class Main {
    interface Greeter {
        String PREFIX = ">> ";
        String name();
        default String greet() { return PREFIX + decorate("Hello, " + name()); }
        default String shout() { return greet().toUpperCase(); }
        private String decorate(String s) { return "[" + s + "]"; }
        static Greeter of(String n) { return () -> n; }
        static String twice(Greeter g) { return g.greet() + g.greet(); }
    }
    static class Polite implements Greeter {
        public String name() { return "Sir"; }
        @Override public String greet() { return Greeter.super.greet() + "!"; }
    }
    static class Plain implements Greeter {
        public String name() { return "plain"; }
    }
    interface Counter {
        int next();
        default int skip(int n) { int r = 0; for (int i = 0; i < n; i++) r = next(); return r; }
    }
    public static void main(String[] args) {
        Greeter g = Greeter.of("World");
        System.out.println(g.greet());
        System.out.println(g.shout());
        System.out.println(new Polite().greet());
        System.out.println(new Polite().shout());
        System.out.println(new Plain().greet());
        System.out.println(Greeter.twice(new Plain()));
        System.out.println(Greeter.PREFIX.length());
        System.out.println(Plain.PREFIX + "x");
        Greeter anon = new Greeter() {
            public String name() { return "anon"; }
            public String greet() { return "custom " + name(); }
        };
        System.out.println(anon.greet() + " | " + anon.shout());
        int[] state = {0};
        Counter c = () -> ++state[0];
        System.out.println(c.next() + " " + c.next() + " " + c.skip(5) + " " + c.next());
    }
}
