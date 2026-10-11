import java.util.*;

public class Main {
    static class AppException extends Exception {
        final int code;
        AppException(String m, int code) { super(m); this.code = code; }
        AppException(String m, int code, Throwable cause) { super(m, cause); this.code = code; }
    }
    static class NotFound extends AppException { NotFound(String what) { super(what + " not found", 404); } }
    static class Invalid extends AppException { Invalid(String m) { super("invalid: " + m, 400); } }
    static class Fatal extends RuntimeException { Fatal(String m, Throwable c) { super(m, c); } }
    static class Res implements AutoCloseable {
        final String n; Res(String n) { this.n = n; System.out.println("open " + n); }
        void use(boolean fail) { System.out.println("use " + n); if (fail) throw new IllegalStateException("fail in " + n); }
        public void close() { System.out.println("close " + n); }
    }
    static String lookup(String k) throws AppException {
        switch (k) {
            case "a": return "alpha";
            case "": throw new Invalid("empty key");
            case "boom": throw new Fatal("boom!", new NotFound("x"));
            default: throw new NotFound(k);
        }
    }
    static int tryFinally(int n) {
        try {
            if (n == 0) return 10;
            if (n == 1) throw new RuntimeException("one");
            return 20;
        } catch (RuntimeException e) {
            return 30;
        } finally {
            System.out.println("finally n=" + n);
        }
    }
    public static void main(String[] args) {
        for (String k : new String[] { "a", "zz", "", "boom" }) {
            try {
                System.out.println(lookup(k));
            } catch (NotFound e) {
                System.out.println("NF " + e.getMessage() + " " + e.code);
            } catch (AppException e) {
                System.out.println("APP " + e.getMessage() + " " + e.code + " " + (e instanceof Invalid));
            } catch (RuntimeException e) {
                System.out.println("RT " + e.getMessage() + " cause=" + e.getCause().getMessage() + " " + e.getCause().getClass().getSimpleName());
            } finally {
                System.out.println("done " + k);
            }
        }
        System.out.println(tryFinally(0) + " " + tryFinally(1) + " " + tryFinally(2));
        try (Res a = new Res("A"); Res b = new Res("B")) {
            a.use(false);
            b.use(true);
        } catch (IllegalStateException e) {
            System.out.println("caught " + e.getMessage());
        }
        try {
            try { throw new Invalid("inner"); }
            catch (AppException e) { throw new AppException("wrapped", 500, e); }
        } catch (AppException e) {
            System.out.println(e.getMessage() + " <- " + e.getCause().getMessage() + " " + e.code);
        }
        try { Object o = null; o.toString(); } catch (NullPointerException e) { System.out.println("NPE"); }
        try { int[] a = new int[2]; a[2] = 1; } catch (ArrayIndexOutOfBoundsException e) { System.out.println(e.getMessage()); }
        try { Object s = "x"; Integer i = (Integer) s; } catch (ClassCastException e) { System.out.println("CCE"); }
        try { System.out.println(10 / (args.length)); } catch (ArithmeticException e) { System.out.println(e.getMessage()); }
        try { Integer.parseInt("12x"); } catch (NumberFormatException e) { System.out.println(e.getMessage()); }
        try { throw new Fatal("top", null); } catch (Fatal e) { System.out.println(e + " | " + e.getCause()); }
    }
}
