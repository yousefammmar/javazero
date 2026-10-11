public class Main {
    public static void main(String[] args) {
        float f = 0;
        double d = 0;
        for (int i = 0; i < 10; i++) { f += 0.1f; d += 0.1; }
        System.out.println(f + " " + d);
        f = 0; d = 0;
        for (int i = 0; i < 1000; i++) { f += 0.1f; d += 0.1; }
        System.out.println(f + " " + d);
        float big = 16777216f;
        System.out.println(big + 1 + " " + (big + 2) + " " + (big + 1 == big));
        double bigd = 9007199254740992.0;
        System.out.println(bigd + 1 + " " + (bigd + 2) + " " + (bigd + 1 == bigd));
        System.out.println((0.1f + 0.2f) + " " + (0.1 + 0.2) + " " + (0.1f + 0.2) + " " + (0.1f + 0.2f == 0.3f) + " " + ((double) 0.1f == 0.1));
        System.out.println(1.1f * 1.1f + " " + 1.1 * 1.1 + " " + (double) (1.1f * 1.1f) + " " + 1.1f * 1.1);
        System.out.println(1f / 3 + " " + 1.0 / 3 + " " + (double) (1f / 3) + " " + 2f / 3 + " " + 10f / 3);
        System.out.println(100f / 3f * 3f + " " + 100.0 / 3 * 3);
        System.out.println((float) Math.PI + " " + Math.PI + " " + (float) Math.E + " " + (double) (float) Math.PI);
        System.out.println(1e8f + " " + 1.0E8 + " " + 123456789f + " " + 1234567890123.0f + " " + 0.00001f + " " + 0.0001f);
        System.out.println(Float.MIN_VALUE / 2 + " " + Float.MAX_VALUE * 1.0000001f + " " + Float.MIN_NORMAL + " " + Float.MAX_VALUE + " " + (double) Float.MAX_VALUE);
        System.out.println((int) 1e9f + " " + (int) 2.9e9f + " " + (long) 1e19f + " " + (int) -0.9f + " " + (int) 0.9999999f + " " + (int) 0.99999999f);
        System.out.println((float) 1 / 3 + " " + (float) (1 / 3) + " " + 1 / 3f + " " + 7 / 2 * 2f + " " + 7 / 2f * 2);
        float a = 1e20f, b = 1f;
        System.out.println(a + b - a + " " + (a - a + b));
        double c = 1e20, e = 1;
        System.out.println(c + e - c + " " + (c - c + e));
        System.out.println(Math.sqrt(2) * Math.sqrt(2) + " " + (float) Math.sqrt(2) * (float) Math.sqrt(2) + " " + (float) (Math.sqrt(2) * Math.sqrt(2)));
        System.out.println(Float.compare(0f, -0f) + " " + (0f == -0f) + " " + Float.valueOf(0f).equals(-0f) + " " + Float.floatToIntBits(-0f) + " " + Float.floatToIntBits(Float.NaN) + " " + Float.intBitsToFloat(0x7f800000) + " " + Float.intBitsToFloat(1));
        System.out.println(Float.toString(1.0E-3f) + " " + Float.toString(9.999999E-4f) + " " + Float.toString(1.0E7f) + " " + Float.toString(9999999f) + " " + Float.parseFloat("3.4028236e38") + " " + Float.parseFloat("1e-46"));
        float avg = (1 + 2 + 4) / 3f;
        double avgd = (1 + 2 + 4) / 3.0;
        System.out.println(avg + " " + avgd + " " + (avg == avgd) + " " + (avg == (float) avgd));
        double celsius = 36.6;
        System.out.println(celsius * 9 / 5 + 32);
        System.out.println((float) (celsius * 9 / 5 + 32));
        double compound = 1000;
        for (int y = 0; y < 10; y++) compound *= 1.05;
        System.out.println(compound);
        System.out.println(Math.round(compound * 100) / 100.0);
        System.out.println(Math.round(compound * 100.0) / 100 + " " + (int) (compound * 100) / 100.0);
        float fs = 0;
        for (int i = 1; i <= 100; i++) fs += 1f / i;
        double ds = 0;
        for (int i = 1; i <= 100; i++) ds += 1.0 / i;
        System.out.println(fs + " " + ds);
        float x = 0.7f;
        System.out.println(x * 100 + " " + (int) (x * 100) + " " + (int) (0.7 * 100) + " " + (int) (0.57 * 100) + " " + (int) (0.58 * 100) + " " + (int) (1.15 * 100));
        System.out.println(3.0e-5f + " " + 3.0e-5 + " " + 12345.678f + " " + 12345.678 + " " + 0.3f + " " + 0.3 + " " + (0.3f == 0.3));
    }
}
