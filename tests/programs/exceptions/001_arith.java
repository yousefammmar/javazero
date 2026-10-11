public class Main {
    static int zero = 0;
    static void run(String label, Runnable r) {
        try { r.run(); System.out.println(label + ": ok"); }
        catch (ArithmeticException e) {
            System.out.println(label + ": " + e.getMessage() + " | " + e + " | " + e.getClass().getName() + " | " + e.getClass().getSimpleName() + " | cause=" + e.getCause());
        }
    }
    public static void main(String[] args) {
        run("int div", () -> System.out.println(10 / zero));
        run("int mod", () -> System.out.println(10 % zero));
        run("long div", () -> System.out.println(10L / (long) zero));
        run("long mod", () -> System.out.println(10L % (long) zero));
        run("double div", () -> System.out.println(10.0 / zero));
        run("neg double div", () -> System.out.println(-10.0 / zero));
        run("double 0/0", () -> System.out.println(0.0 / zero));
        run("double mod", () -> System.out.println(5.5 % zero));
        run("float div", () -> System.out.println(1f / zero));
        run("compound", () -> { int x = 5; x /= zero; System.out.println(x); });
        run("compound mod", () -> { int x = 5; x %= zero; System.out.println(x); });
        run("min/-1", () -> System.out.println(Integer.MIN_VALUE / -1));
        run("min%-1", () -> System.out.println(Integer.MIN_VALUE % -1));
        run("floorDiv", () -> System.out.println(Math.floorDiv(7, zero)));
        run("floorMod", () -> System.out.println(Math.floorMod(7, zero)));
        run("Integer.divideUnsigned", () -> System.out.println(Integer.divideUnsigned(7, zero)));
    }
}
