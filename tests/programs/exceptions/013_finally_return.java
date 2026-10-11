public class Main {
    static int a() { try { return 1; } finally { System.out.println("finally a"); } }
    @SuppressWarnings("finally")
    static int b() { try { return 1; } finally { return 2; } }
    static int c() { int x = 1; try { return x; } finally { x = 99; System.out.println("x set " + x); } }
    @SuppressWarnings("finally")
    static int d() { try { throw new RuntimeException("lost"); } finally { return 3; } }
    static int e() { try { throw new RuntimeException("e"); } catch (RuntimeException ex) { return 4; } finally { System.out.println("finally e"); } }
    @SuppressWarnings("finally")
    static int f() { try { return 5; } catch (Exception ex) { return 6; } finally { System.out.println("finally f"); } }
    static StringBuilder g() { StringBuilder sb = new StringBuilder("a"); try { return sb; } finally { sb.append("b"); } }
    static String h() {
        try { System.out.println("try h"); throw new IllegalStateException("h"); }
        catch (IllegalStateException ex) { System.out.println("catch h"); return "from catch"; }
        finally { System.out.println("finally h"); }
    }
    static int i() {
        int count = 0;
        for (int k = 0; k < 3; k++) {
            try { count += 10; if (k == 1) return count; }
            finally { count++; System.out.println("loop finally " + k + " count=" + count); }
        }
        return -1;
    }
    static int j() {
        try { try { return 1; } finally { System.out.println("inner fin"); } }
        finally { System.out.println("outer fin"); }
    }
    public static void main(String[] args) {
        System.out.println(a());
        System.out.println(b());
        System.out.println(c());
        System.out.println(d());
        System.out.println(e());
        System.out.println(f());
        System.out.println(g());
        System.out.println(h());
        System.out.println(i());
        System.out.println(j());
    }
}
