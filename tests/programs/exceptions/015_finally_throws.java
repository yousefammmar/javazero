public class Main {
    static void a() {
        try { throw new RuntimeException("original"); }
        finally { throw new IllegalStateException("from finally"); }
    }
    static void b() {
        try { throw new RuntimeException("original b"); }
        catch (RuntimeException e) { throw new IllegalArgumentException("from catch", e); }
        finally { System.out.println("finally b"); }
    }
    static void c() {
        try { System.out.println("c try"); }
        finally { throw new UnsupportedOperationException("finally only"); }
    }
    static void d() throws Exception {
        try {
            try { throw new Exception("level1"); }
            finally { System.out.println("inner finally"); }
        } catch (Exception e) {
            System.out.println("outer caught " + e.getMessage());
            throw new Exception("level2: " + e.getMessage());
        } finally {
            System.out.println("outer finally");
        }
    }
    static void e() {
        try { throw new RuntimeException("e1"); }
        finally {
            try { throw new RuntimeException("e2"); }
            catch (RuntimeException ex) { System.out.println("handled inside finally " + ex.getMessage()); }
        }
    }
    public static void main(String[] args) {
        try { a(); } catch (Exception e) { System.out.println(e + " suppressed=" + e.getSuppressed().length + " cause=" + e.getCause()); }
        try { b(); } catch (Exception e) { System.out.println(e + " cause=" + e.getCause()); }
        try { c(); } catch (Exception e) { System.out.println(e.getMessage()); }
        try { d(); } catch (Exception e) { System.out.println("main " + e.getMessage()); }
        try { e(); } catch (Exception e) { System.out.println("main " + e.getMessage()); }
    }
}
