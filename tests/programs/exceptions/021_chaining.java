public class Main {
    public static void main(String[] args) {
        Exception root = new Exception("root cause");
        RuntimeException a = new RuntimeException("wrapper", root);
        System.out.println(a.getMessage() + " | " + a.getCause().getMessage());
        RuntimeException b = new RuntimeException(root);
        System.out.println(b.getMessage());
        System.out.println(b);
        RuntimeException c = new RuntimeException((Throwable) null);
        System.out.println(c.getMessage() + " " + c.getCause());
        RuntimeException d = new RuntimeException("m", null);
        System.out.println(d.getMessage() + " " + d.getCause());
        RuntimeException e = new RuntimeException(new IllegalStateException());
        System.out.println(e.getMessage());
        RuntimeException f = new RuntimeException(new IllegalStateException("inner msg"));
        System.out.println(f.getMessage());
        Exception g = new Exception("g");
        g.initCause(new Error("initialized"));
        System.out.println(g.getCause());
        try { g.initCause(new Error("again")); } catch (IllegalStateException ex) { System.out.println("ISE: " + ex.getMessage()); }
        Exception h = new Exception("h", new Error("c"));
        try { h.initCause(new Error("x")); } catch (IllegalStateException ex) { System.out.println("ISE2: " + ex.getMessage()); }
        Exception self = new Exception("self");
        try { self.initCause(self); } catch (IllegalArgumentException ex) { System.out.println("IAE: " + ex.getMessage()); }
        Throwable t = new Exception("top", new RuntimeException("mid", new Error("bottom")));
        int depth = 0;
        for (Throwable x = t; x != null; x = x.getCause()) System.out.println(depth++ + ": " + x);
        Throwable r = t;
        while (r.getCause() != null) r = r.getCause();
        System.out.println("root: " + r.getMessage());
        System.out.println(new Exception("x", root).getCause() == root);
        System.out.println(new Exception(root).getLocalizedMessage());
        Error er = new AssertionError("assert msg");
        System.out.println(er + " " + new AssertionError(42).getMessage() + " " + new AssertionError(root).getCause());
    }
}
