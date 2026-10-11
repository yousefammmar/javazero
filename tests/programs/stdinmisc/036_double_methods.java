public class Main {
    public static void main(String[] args) {
        System.out.println(Double.compare(0.0, -0.0) + " " + Double.compare(Double.NaN, 1) + " " + Double.compare(1, 2) + " " + (0.0 == -0.0) + " " + (Double.NaN == Double.NaN) + " " + Double.valueOf(0.0).equals(-0.0));
        System.out.println(Double.isNaN(0.0 / 0) + " " + Double.isInfinite(1 / 0.0) + " " + Double.isFinite(1e308 * 10) + " " + (1 / 0.0) + " " + (-1 / 0.0) + " " + (0.0 / 0));
        System.out.println(Double.parseDouble("1e3") + " " + Double.parseDouble("-1.5E-3") + " " + Double.parseDouble("  42  ") + " " + Double.parseDouble(".5") + " " + Double.parseDouble("5.") + " " + Double.parseDouble("0x1p4") + " " + Double.parseDouble("1d") + " " + Double.parseDouble("NaN") + " " + Double.parseDouble("-Infinity"));
        System.out.println(Double.MIN_VALUE + " " + Double.MAX_VALUE + " " + Double.MIN_NORMAL + " " + Float.MIN_VALUE + " " + Float.MAX_VALUE);
        double[] vs = {1e7, 9999999.0, 1.0E-3, 0.001, 0.0001, 123456789.123, 1.0E21, 1e22, 100.0 / 3, 1e6, 12345678.9, 0.1 + 0.2, 1.0 / 3, 2.0 / 3, 1e-5, 123e-10, 4.9e-324, 1.7976931348623157E308, 100, 1e100, 123456.789e3, 0.5, 5e-1, 3.0e10, 1234567.0, 12345678.0, 0.00123, 100.0, -0.0, 9.999999999999999e22, 1e23, 2e23, 8.41e21, 5e-324 * 3};
        for (double v : vs) System.out.println(v);
        System.out.println((double) Long.MAX_VALUE + " " + (double) (1L << 53) + " " + ((double) (1L << 53) + 1) + " " + (long) 1e19 + " " + (float) 1e10);
        System.out.println(Double.toString(5) + " " + Double.valueOf("3.50") + " " + Double.toHexString(1.0) + " " + Double.doubleToLongBits(1.0) + " " + Double.longBitsToDouble(0x7ff0000000000000L));
        System.out.println(Double.sum(1, 2) + " " + Double.max(1, 2) + " " + (int) 'a' / 2.0 + " " + 7 / 2 + " " + 7 / 2.0 + " " + 7 % 2.5 + " " + -7.5 % 2);
        System.out.println(0.1 * 3 + " " + 1.1 * 1.1 + " " + 3 * 0.1f + " " + 1e16 + 1 + " " + (1e16 + 1));
        System.out.println(Double.parseDouble("1e400") + " " + Double.parseDouble("1e-400"));
        try { Double.parseDouble("abc"); } catch (NumberFormatException e) { System.out.println(e.getMessage()); }
        try { Double.parseDouble(""); } catch (NumberFormatException e) { System.out.println(e.getMessage()); }
    }
}
