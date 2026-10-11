public class Main {
    static StringBuilder log = new StringBuilder();
    static void step(String s) { System.out.println(s); log.append(s.charAt(0)); }
    static void a(int n) {
        step("a-enter " + n);
        try { b(n); step("a-after-b " + n); }
        finally { step("a-finally " + n); }
    }
    static void b(int n) {
        step("b-enter " + n);
        try { c(n); step("b-after-c " + n); }
        catch (IllegalStateException e) { step("b-catch " + e.getMessage()); }
        finally { step("b-finally " + n); }
    }
    static void c(int n) {
        step("c-enter " + n);
        try {
            if (n == 1) throw new IllegalStateException("ise" + n);
            if (n == 2) throw new IllegalArgumentException("iae" + n);
            step("c-normal " + n);
        } finally { step("c-finally " + n); }
    }
    public static void main(String[] args) {
        for (int n = 0; n < 3; n++) {
            try { a(n); step("main-ok " + n); }
            catch (RuntimeException e) { step("main-caught " + e); }
            System.out.println("-----");
        }
        System.out.println(log.length());
        System.out.println(log);
        int depth = recurse(0);
        System.out.println("depth " + depth);
    }
    static int recurse(int d) {
        try {
            if (d == 4) throw new RuntimeException("bottom");
            return recurse(d + 1);
        } catch (RuntimeException e) {
            if (d > 2) { System.out.println("rethrow at " + d); throw e; }
            System.out.println("absorbed at " + d);
            return d * 100;
        } finally {
            System.out.println("unwind " + d);
        }
    }
}
