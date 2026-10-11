public class Main {
    static int f1() { try { return 1; } finally { System.out.println("f1 finally"); } }
    @SuppressWarnings("finally")
    static int f2() { try { return 1; } finally { return 2; } }
    static int f3() { int x = 1; try { return x; } finally { x = 99; } }
    static int[] f4() { int[] a = {1}; try { return a; } finally { a[0] = 99; } }
    static String f5() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 4; i++) {
            try {
                if (i == 1) continue;
                if (i == 3) break;
                sb.append("body").append(i).append(' ');
            } finally {
                sb.append("fin").append(i).append(' ');
            }
        }
        return sb.toString();
    }
    @SuppressWarnings("finally")
    static int f6() { try { throw new RuntimeException("x"); } finally { return 6; } }
    static void f7() {
        try {
            try { throw new IllegalStateException("inner"); }
            finally { System.out.println("inner finally"); }
        } catch (IllegalStateException e) { System.out.println("outer caught " + e.getMessage()); }
        finally { System.out.println("outer finally"); }
    }
    static void f8() {
        try {
            try { throw new RuntimeException("first"); }
            finally { throw new IllegalArgumentException("second"); }
        } catch (RuntimeException e) { System.out.println("caught " + e.getMessage() + " " + e.getClass().getSimpleName()); }
    }
    static void f9() {
        try { throw new Exception("checked"); }
        catch (RuntimeException e) { System.out.println("runtime"); }
        catch (Exception e) { System.out.println("exception " + e.getMessage()); }
    }
    static void f10(String s) {
        try {
            System.out.println(s.length() + " " + Integer.parseInt(s));
        } catch (NullPointerException | NumberFormatException e) {
            System.out.println("multi " + e.getClass().getSimpleName());
        }
    }
    static class Res implements AutoCloseable {
        String n;
        Res(String n) { this.n = n; System.out.println("open " + n); }
        public void close() { System.out.println("close " + n); }
    }
    static class MyEx extends Exception {
        int code;
        MyEx(String m, int code) { super(m); this.code = code; }
    }
    static void thrower(int c) throws MyEx { if (c > 0) throw new MyEx("code" + c, c); System.out.println("no throw"); }
    static int depth = 0;
    static void recurse() { depth++; recurse(); }
    public static void main(String[] args) {
        System.out.println(f1());
        System.out.println(f2());
        System.out.println(f3());
        System.out.println(f4()[0]);
        System.out.println(f5());
        System.out.println(f6());
        f7();
        f8();
        f9();
        f10("12"); f10("zz"); f10(null);
        try (Res a = new Res("A"); Res b = new Res("B")) {
            System.out.println("in try " + a.n + b.n);
            throw new RuntimeException("boom");
        } catch (RuntimeException e) {
            System.out.println("after close: " + e.getMessage());
        } finally {
            System.out.println("twr finally");
        }
        try { thrower(0); thrower(7); thrower(8); } catch (MyEx e) { System.out.println(e.getMessage() + " " + e.code); }
        try { Object o = "s"; Integer i = (Integer) o; System.out.println(i); } catch (ClassCastException e) { System.out.println("CCE"); }
        try { Object[] arr = new Integer[1]; arr[0] = "s"; } catch (ArrayStoreException e) { System.out.println("ASE " + e.getMessage()); }
        try { throw new Error("custom error"); } catch (Throwable t) { System.out.println(t); }
        try { recurse(); } catch (StackOverflowError e) { System.out.println("SOE " + (depth > 500)); }
        try { int[] big = new int[Integer.MAX_VALUE]; System.out.println(big.length); } catch (OutOfMemoryError e) { System.out.println("OOME"); }
        try { throw new RuntimeException("outer", new IllegalStateException("cause")); } catch (RuntimeException e) { System.out.println(e.getMessage() + " <- " + e.getCause().getMessage() + " " + e); }
        try { Object o = null; o.toString(); } catch (NullPointerException e) { System.out.println("NPE caught"); }
        try { java.util.List.of(1).add(2); } catch (UnsupportedOperationException e) { System.out.println("UOE " + e.getMessage()); }
        try { new java.util.ArrayList<Integer>().iterator().next(); } catch (java.util.NoSuchElementException e) { System.out.println("NSEE"); }
        try { java.util.List<Integer> l = new java.util.ArrayList<>(java.util.List.of(1, 2, 3)); for (int v : l) l.remove(0); } catch (java.util.ConcurrentModificationException e) { System.out.println("CME"); }
        try { new java.util.ArrayList<Integer>().get(0); } catch (IndexOutOfBoundsException e) { System.out.println(e.getMessage()); }
        try { Integer.parseInt("x"); } catch (Exception e) { System.out.println(e.getClass().getName()); }
        System.out.println("end");
    }
}
