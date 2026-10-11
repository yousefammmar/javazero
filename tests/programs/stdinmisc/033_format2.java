public class Main {
    public static void main(String[] args) {
        double[] vs = {0.125, 0.135, 2.675, 1.005, 0.5, 1.5, 2.5, 3.5, -0.5, -1.5, 0.045, 0.995, 9.995, 1.115, 100.0 / 3, 2.0 / 3, 1e-10, 123456789.987654321};
        for (double v : vs) {
            System.out.println(v + " -> " + String.format("%.0f|%.1f|%.2f|%.3f|%.5f", v, v, v, v, v));
        }
        System.out.println(String.format("%f", 1e15));
        System.out.println(String.format("%f", 1.0e20));
        System.out.println(String.format("%f", 1e-7));
        System.out.println(String.format("%.10f", 1e-7));
        System.out.println(String.format("%f", Double.MAX_VALUE).length());
        System.out.println(String.format("%f %f %f", Double.NaN, Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY));
        System.out.println(String.format("%.2f %.2f", 0.0, -0.0));
        System.out.println(String.format("%.3f", Double.MIN_VALUE));
        System.out.println(String.format("%10.2f|%-10.2f|%+.2f", -3.14159, 3.14159, 3.14159));
        System.out.println(String.format("%.2f %.2f", 1.0f / 3, 0.1f + 0.2f));
        System.out.println(String.format("%.15f", 0.1));
        System.out.println(String.format("%.20f", 0.1));
        System.out.println(String.format("%.1f %.1f %.1f %.1f", 0.25, 0.35, 0.45, 0.05));
        System.out.println(String.format("%.2e", 9.995));
        System.out.println(String.format("%08.2f|%(.2f|%,10.1f", -5.5, -5.5, 12345.67));
    }
}
