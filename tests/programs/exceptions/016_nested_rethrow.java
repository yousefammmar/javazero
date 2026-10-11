public class Main {
    static void level3(int x) {
        if (x > 2) throw new IllegalArgumentException("x too big: " + x);
        System.out.println("level3 ok " + x);
    }
    static void level2(int x) {
        try { level3(x); }
        catch (IllegalArgumentException e) { System.out.println("level2 rethrow"); throw e; }
        finally { System.out.println("level2 finally " + x); }
    }
    static void level1(int x) {
        try { level2(x); }
        catch (RuntimeException e) { throw new IllegalStateException("wrapped: " + e.getMessage(), e); }
        finally { System.out.println("level1 finally " + x); }
    }
    static void precise() throws java.io.IOException {
        try { if (System.nanoTime() > 0) throw new java.io.IOException("io"); }
        catch (Exception e) { System.out.println("rethrowing precise " + e.getMessage()); throw e; }
    }
    public static void main(String[] args) {
        for (int i = 1; i <= 4; i += 2) {
            try { level1(i); System.out.println("ok " + i); }
            catch (IllegalStateException e) {
                System.out.println("main: " + e.getMessage());
                Throwable c = e.getCause();
                System.out.println("cause: " + c + " same? " + (c instanceof IllegalArgumentException));
            }
        }
        try { precise(); } catch (java.io.IOException e) { System.out.println("io caught " + e); }
        try {
            try {
                try { throw new Exception("deep"); }
                catch (Exception e) { throw new RuntimeException("mid", e); }
            } catch (RuntimeException e) { throw new Error("top", e); }
        } catch (Error e) {
            Throwable t = e;
            int d = 0;
            while (t != null) { System.out.println("  ".repeat(d++) + t.getClass().getSimpleName() + ": " + t.getMessage()); t = t.getCause(); }
        }
    }
}
