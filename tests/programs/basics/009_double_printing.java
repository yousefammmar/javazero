public class Main {
    public static void main(String[] args) {
        double[] ds = {1.0E10, 1.0E-5, 100.0, 0.1 + 0.2, 0.1 + 0.7, 1e7, 9999999.0, 12345678.9, 1e-3, 0.001, 0.0001, 123456789012.0,
            1.0 / 3, 2.0 / 3, 10.0 / 3, 100.0 / 3, 1e21, 1e-300, Double.MAX_VALUE, Double.MIN_VALUE, Double.MIN_NORMAL,
            Math.PI, Math.E, 4.35 * 100, 1.1 * 1.1, 3 * 0.1, 0.5, 0.25, 1234.5678, -0.0, 0.0, 5e-324, 1.7976931348623157E308,
            100.0 * 1.1, 1e6, 1.0E7, 123.0E5, 0.1f, (double) 0.1f, 3.0e-10, 123456.789e3};
        for (double d : ds) System.out.println(d);
        float[] fs = {1.1f, 0.1f + 0.2f, 1e10f, 1.0E-5f, 16777216f, 16777217f, 3.4028235E38f, 1.4E-45f, 100f, 0.5f, 1f / 3, 2f / 3,
            1234567.9f, 12345678.9f, 0.001f, 0.0001f, 1e7f, 9999999f, (float) Math.PI, (float) 1e-10, 33.333332f, 0.3f, 1.0f * 3};
        for (float f : fs) System.out.println(f);
        System.out.println(0.0 == -0.0);
        System.out.println(Double.valueOf(0.0).equals(-0.0));
        System.out.println(Double.compare(0.0, -0.0));
        System.out.println(Double.NaN == Double.NaN);
        System.out.println(Double.valueOf(Double.NaN).equals(Double.NaN));
        System.out.println(Double.isNaN(0.0 / 0));
        System.out.println(Double.isInfinite(1 / 0.0));
        System.out.println(1 / 0.0 + " " + -1 / 0.0 + " " + (1 / 0.0 - 1 / 0.0) + " " + 1 / 0.0 * 0);
        System.out.println(1 / -0.0);
        System.out.println(Math.sqrt(-1));
        System.out.println(Math.min(0.0, -0.0) + " " + Math.max(-0.0, 0.0));
        System.out.println(0.1 + 0.2 == 0.3);
        System.out.println(0.1f + 0.2f == 0.3f);
        System.out.println((0.1 + 0.2) + " vs " + 0.3);
        System.out.println(1.0f + 1e-8f);
        System.out.println(1.0 + 1e-16);
        System.out.println(1.0 + 2e-16);
        System.out.println(100 * 1.1);
        System.out.println((int) (100 * 1.1));
        System.out.println(0.1 * 3);
        System.out.println(1e16 + 1);
        System.out.println(9007199254740993L + 0.0);
        System.out.println((float) 0.1 + (double) 0.2f);
        System.out.println(Double.toString(5) + " " + Float.toString(5) + " " + String.valueOf(5.0f) + " " + Double.valueOf("1e3"));
        System.out.println(Double.parseDouble("  3.5  ") + Double.parseDouble("-.5") + Double.parseDouble("1e-2") + Double.parseDouble("0x1p3"));
        System.out.println(Double.toHexString(1.0) + " " + Double.toHexString(0.1));
        System.out.println(Double.doubleToLongBits(1.0) + " " + Float.floatToIntBits(1.0f));
        System.out.println(Long.toHexString(Double.doubleToLongBits(-2.5)));
    }
}
