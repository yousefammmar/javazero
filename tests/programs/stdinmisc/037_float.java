public class Main {
    public static void main(String[] args) {
        System.out.println(0.1f + 0.2f);
        System.out.println(0.1 + 0.2);
        System.out.println((float) 0.1);
        System.out.println((double) 0.1f);
        System.out.println((double) (0.1f + 0.2f));
        System.out.println(16777217f);
        System.out.println(16777216f + 1);
        System.out.println((int) 16777217f);
        System.out.println(1.0f / 3);
        System.out.println(1.0 / 3);
        System.out.println(2f / 3 + " " + 10f / 3 + " " + 100f / 7);
        float f = 1.1f;
        System.out.println(f * f + " " + (double) f * f + " " + f * 2 + " " + f + 1);
        System.out.println(3.4028235E38f * 10 + " " + 1e-45f + " " + 1.4E-45f + " " + 1.17549435E-38f);
        float sum = 0;
        for (int i = 0; i < 10; i++) sum += 0.1f;
        System.out.println(sum);
        double dsum = 0;
        for (int i = 0; i < 10; i++) dsum += 0.1;
        System.out.println(dsum);
        System.out.println(1e7f + " " + 1.0E-3f + " " + 123456789f + " " + 0.001f + " " + 9999999f + " " + 1e10f + " " + 3.14159265358979f + " " + 100f + " " + 33.333332f);
        System.out.println((float) 1e40 + " " + (float) 123456789.123456789 + " " + (long) 1e10f + " " + (float) Long.MAX_VALUE);
        System.out.println(Float.compare(1f, 2f) + " " + Float.isNaN(0f / 0) + " " + Float.parseFloat("1.5e2") + " " + Float.floatToIntBits(1.0f) + " " + Float.intBitsToFloat(0x7f800000));
        System.out.println((0.1f == 0.1) + " " + (0.5f == 0.5) + " " + ((float) 0.1 == 0.1f));
        System.out.println(5 / 2 * 2.0f + " " + 5 / 2.0f * 2 + " " + (char) ('a' + 1.5f));
        System.out.println(String.valueOf(1.0f) + Float.toString(2.5f) + Float.valueOf("7"));
        System.out.println(Math.round(2.5f) + " " + Math.round(-2.5f) + " " + Math.round(0.49999997f) + " " + Math.round(0.49999999999999994));
    }
}
