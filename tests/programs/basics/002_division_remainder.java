public class Main {
    static int zero() { return 0; }
    public static void main(String[] args) {
        int[] ns = {7, -7, 8, -8, 0};
        int[] ds = {2, -2, 3, -3, 1};
        for (int n : ns) {
            for (int d : ds) {
                System.out.println(n + " / " + d + " = " + (n / d) + ", " + n + " % " + d + " = " + (n % d)
                    + ", floorDiv=" + Math.floorDiv(n, d) + ", floorMod=" + Math.floorMod(n, d));
            }
        }
        System.out.println(7.5 % 2);
        System.out.println(-7.5 % 2);
        System.out.println(7.5 % -2);
        System.out.println(5.0 / 2);
        System.out.println(5 / 2);
        System.out.println(5 / 2.0);
        System.out.println((double) (5 / 2));
        System.out.println((double) 5 / 2);
        System.out.println(1 / 3 * 3);
        System.out.println(1.0 / 3 * 3);
        System.out.println(10L / 3);
        System.out.println(-10L % 3);
        System.out.println(5.0 / 0);
        System.out.println(-5.0 / 0);
        System.out.println(0.0 / 0);
        System.out.println(5 % 0.0);
        System.out.println(1.0f / 0);
        try {
            System.out.println(5 / zero());
        } catch (ArithmeticException e) {
            System.out.println("AE: " + e.getMessage());
        }
        try {
            System.out.println(5 % zero());
        } catch (ArithmeticException e) {
            System.out.println("AE: " + e.getMessage());
        }
        try {
            System.out.println(5L / (long) zero());
        } catch (ArithmeticException e) {
            System.out.println("AE: " + e.getMessage());
        }
        System.out.println(Math.floorMod(-7L, 3));
        System.out.println(Math.floorMod(7, -3));
        System.out.println(Integer.MIN_VALUE / -1);
        System.out.println(Math.floorDiv(Integer.MIN_VALUE, -1));
        System.out.println((-7) / 2 * 2 + (-7) % 2);
        System.out.println(-7 / 2);
        System.out.println(-7 >> 1);
        System.out.println(-7 >>> 28);
    }
}
