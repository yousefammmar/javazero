public class Main {
    static class Bad implements AutoCloseable {
        final String name;
        Bad(String n) { name = n; }
        @Override public void close() throws Exception {
            System.out.println("closing " + name);
            throw new Exception("close failed " + name);
        }
    }
    public static void main(String[] args) {
        try (Bad a = new Bad("a"); Bad b = new Bad("b")) {
            throw new RuntimeException("body");
        } catch (Exception e) {
            System.out.println(e.getMessage() + " suppressed=" + e.getSuppressed().length);
            for (Throwable s : e.getSuppressed()) System.out.println("  " + s);
        }
        try (Bad a = new Bad("c"); Bad b = new Bad("d")) {
            System.out.println("body ok");
        } catch (Exception e) {
            System.out.println(e.getMessage() + " suppressed=" + e.getSuppressed().length);
            for (Throwable s : e.getSuppressed()) System.out.println("  " + s.getMessage());
        }
        try (Bad a = new Bad("e")) {
            System.out.println("single");
        } catch (Exception e) {
            System.out.println(e + " " + e.getSuppressed().length);
        }
        Exception main = new Exception("main");
        main.addSuppressed(new RuntimeException("s1"));
        main.addSuppressed(new Error("s2"));
        System.out.println(main.getSuppressed().length + " " + main.getSuppressed()[1]);
        try { main.addSuppressed(main); } catch (IllegalArgumentException e) { System.out.println(e.getMessage()); }
        try { main.addSuppressed(null); } catch (NullPointerException e) { System.out.println(e.getMessage()); }
        try (Bad z = new Bad("z")) {
            throw new Error("err body");
        } catch (Error e) {
            System.out.println(e.getMessage() + " " + e.getSuppressed()[0].getMessage());
        } catch (Exception e) {
            System.out.println("unexpected");
        }
    }
}
