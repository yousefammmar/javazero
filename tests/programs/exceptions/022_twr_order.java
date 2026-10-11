public class Main {
    static class Res implements AutoCloseable {
        final String name;
        Res(String name) { this.name = name; System.out.println("open " + name); }
        void use() { System.out.println("use " + name); }
        @Override public void close() { System.out.println("close " + name); }
    }
    static class FailOpen implements AutoCloseable {
        FailOpen() { throw new IllegalStateException("cannot open"); }
        @Override public void close() { System.out.println("close FailOpen (never)"); }
    }
    public static void main(String[] args) {
        try (Res a = new Res("A"); Res b = new Res("B"); Res c = new Res("C")) {
            a.use(); b.use(); c.use();
        }
        System.out.println("--");
        try (Res a = new Res("A2"); Res b = new Res("B2")) {
            a.use();
            throw new RuntimeException("body fail");
        } catch (RuntimeException e) {
            System.out.println("catch " + e.getMessage());
        } finally {
            System.out.println("finally");
        }
        System.out.println("--");
        try (Res a = new Res("A3"); FailOpen f = new FailOpen(); Res c = new Res("C3")) {
            System.out.println("not reached");
        } catch (IllegalStateException e) {
            System.out.println("catch " + e.getMessage());
        }
        System.out.println("--");
        Res shared = new Res("shared");
        try (shared) { shared.use(); }
        System.out.println("--");
        try (Res n = null) { System.out.println("null resource ok"); }
        System.out.println("--");
        System.out.println(f());
    }
    static int f() {
        try (Res r = new Res("F")) { return 7; }
        finally { System.out.println("f finally"); }
    }
}
