public class Main {
    public static void main(String[] args) {
        System.out.printf("[%f]%n", 3.14159265);
        System.out.printf("[%.2f]%n", 3.14159265);
        System.out.printf("[%.0f] [%.0f] [%.0f] [%.0f]%n", 0.5, 1.5, 2.5, 3.5);
        System.out.printf("[%.1f] [%.1f] [%.1f]%n", 0.05, 0.15, 0.25);
        System.out.printf("[%.2f] [%.2f] [%.2f]%n", 1.005, 2.675, 1.115);
        System.out.printf("[%10.3f]%n", 3.14159265);
        System.out.printf("[%-10.3f]%n", 3.14159265);
        System.out.printf("[%010.3f]%n", -3.14159265);
        System.out.printf("[%+.1f] [%+.1f]%n", 2.0, -2.0);
        System.out.printf("[%,.2f]%n", 1234567.891);
        System.out.printf("[%,.2f]%n", -9876.5);
        System.out.printf("[%.3f]%n", 1e-5);
        System.out.printf("[%f]%n", 1e10);
        System.out.printf("[%f]%n", 123456789.123456789);
        System.out.printf("[%.2f]%n", 1e15);
        System.out.printf("[%.10f]%n", 0.1);
        System.out.printf("[%.20f]%n", 0.1);
        System.out.printf("[%.2f]%n", 0.1f);
        System.out.printf("[%.10f]%n", 0.1f);
        System.out.printf("[%f] [%f] [%f]%n", Double.NaN, Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY);
        System.out.printf("[%8.2f] [%-8.2f]%n", Double.NaN, Double.POSITIVE_INFINITY);
        System.out.printf("[%.2f] [%.2f]%n", -0.0, 0.0);
        System.out.printf("[%.1f] [%.1f]%n", -0.04, 0.04);
        System.out.printf("[%e]%n", 12345.6789);
        System.out.printf("[%.2e]%n", 12345.6789);
        System.out.printf("[%E]%n", 0.00012345);
        System.out.printf("[%.0e]%n", 5.5);
        System.out.printf("[%12.3e]%n", -987654.321);
        System.out.printf("[%e]%n", 0.0);
        System.out.printf("[%g] [%g] [%g]%n", 12345.6789, 0.00001234, 1e10);
        System.out.printf("[%.3g]%n", 3.14159);
        System.out.printf("[%a]%n", 1.0);
        System.out.printf("[%s] [%s] [%s]%n", 1.0, 1.5f, 1e10);
        System.out.printf("[%s] [%s]%n", 100.0 / 3, 100.0f / 3);
        System.out.printf("[%5.1f%%]%n", 45.678);
        try {
            System.out.printf("[%.2f]%n", 5);
        } catch (java.util.IllegalFormatConversionException e) {
            System.out.println("IFCE " + e.getMessage());
        }
        double[] prices = {19.99, 5.0, 123.456, 0.5, 1000};
        double total = 0;
        for (double p : prices) { System.out.printf("%-8s%10.2f%n", "item", p); total += p; }
        System.out.printf("%-8s%10.2f%n", "total", total);
        System.out.println(String.format("%.2f", 2.0 / 3) + String.format("%6.1f", 99.95) + String.format("%.3f", 1.0005) + String.format("%.1f", 0.95));
        System.out.println(String.format("%.2f", new java.math.BigDecimal("2.675")));
        System.out.println(String.format("%5.2f|%-7.3f|%07.2f", Math.PI, Math.E, -Math.PI));
        System.out.println(String.format("%.15f %.16f %.17f", 0.1, 0.1, 0.1));
        System.out.println(String.format("%.3f", 1234567.0005f));
    }
}
