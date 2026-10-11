public class Main {
    public static void main(String[] args) {
        System.out.println(1_000_000 + " " + 0x7F + " " + 0XFFFF + " " + 0b1111_0000 + " " + 017 + " " + 0_7 + " " + 1__2);
        System.out.println(0xFFFFFFFF + " " + 0x80000000 + " " + 0x7FFFFFFF + " " + 037777777777 + " " + 0b11111111111111111111111111111111);
        System.out.println(0xFFFFFFFFFFFFFFFFL + " " + 0x8000000000000000L + " " + 9223372036854775807L + " " + -9223372036854775808L);
        System.out.println(2147483647 + " " + -2147483648 + " " + (-2147483648 == Integer.MIN_VALUE));
        System.out.println(1e3 + " " + 1E3 + " " + 1e+3 + " " + 1.5e-3 + " " + .5 + " " + 5. + " " + 5.0D + " " + 5d + " " + 5f + " " + 5F + " " + 1e1f + " " + 0x1p4 + " " + 0x1.8p1 + " " + 1_0.0_1);
        System.out.println(100L + " " + 100l + " " + 'a' + " " + (int) 'a' + " " + '\101' + " " + 'A' + " " + '\7' + "|" + (int) '\377' + " " + (int) '\0');
        System.out.println(1.0f + " " + 1.00000001f + " " + 16777216f + " " + 16777217f + " " + 3.4e38f + " " + 1.17549435E-38f + " " + 0.1f * 3 + " " + 0.1 * 3);
        System.out.println(Integer.MAX_VALUE + 1L + " " + (Integer.MAX_VALUE + 1) + " " + (Integer.MAX_VALUE + 1.0) + " " + (Integer.MAX_VALUE + 1.0f));
        System.out.println(07 + 07 + " " + 010 + 010 + " " + (010 + 010) + " " + 0x10 + 0x10 + " " + (0x10 + 0x10));
        byte b1 = 0x7F, b2 = -0x80, b3 = (byte) 0xFF, b4 = 'a', b5 = 0b101;
        short s1 = 0x7FFF, s2 = (short) 0xFFFF, s3 = 'b';
        char c1 = 97, c2 = 0x41, c3 = 'B', c4 = 0b1000011, c5 = (char) -1, c6 = 'a' + 1;
        System.out.println(b1 + " " + b2 + " " + b3 + " " + b4 + " " + b5 + " " + s1 + " " + s2 + " " + s3);
        System.out.println(c1 + " " + c2 + " " + c3 + " " + c4 + " " + (int) c5 + " " + c6);
        long l1 = 123456789012L, l2 = 0xCAFEBABEL, l3 = 0xCAFEBABE, l4 = 1 << 31, l5 = 1L << 31, l6 = 'a', l7 = 1_000_000 * 1_000_000, l8 = 1_000_000L * 1_000_000;
        System.out.println(l1 + " " + l2 + " " + l3 + " " + l4 + " " + l5 + " " + l6 + " " + l7 + " " + l8);
        float f1 = 1, f2 = 'a', f3 = 123456789L, f4 = 0x7FFFFFFF, f5 = 1e-45f, f6 = 3.4028235e38f;
        double d1 = 1, d2 = 'a', d3 = 123456789L, d4 = 1.1f, d5 = 4.9e-324, d6 = 1.7976931348623157e308, d7 = 0.1f + 0.2f;
        System.out.println(f1 + " " + f2 + " " + f3 + " " + f4 + " " + f5 + " " + f6);
        System.out.println(d1 + " " + d2 + " " + d3 + " " + d4 + " " + d5 + " " + d6 + " " + d7);
        System.out.println((5 / 2) + " " + (5 / 2.) + " " + (5 / 2f) + " " + (5L / 2) + " " + (5 % 2.5) + " " + (5.5 % 2) + " " + (-5 / 2) + " " + (-5 % 2) + " " + (5 / -2) + " " + (5 % -2));
        System.out.println(1 + 2 * 3 - 4 / 2 + " " + (1 + 2) * 3 + " " + (10 - 2));
        System.out.println(-5 + 3 + " " + - -5 + " " + ~5 + " " + -~5 + " " + ~-5 + " " + +5 + " " + !true);
        System.out.println(1 / 2 + 1 / 2.0 + " " + 3 * 1.0 / 2 + " " + 3 / 2 * 1.0 + " " + 1.0 * 3 / 2);
        System.out.println((int) 'a' + (int) 'b' + " " + (char) 97 + (char) 98 + " " + (char) (97 + 1));
        System.out.println(5 > 3 == true);
        System.out.println(5 + 3 > 7 && 2 * 2 == 4 || false);
        System.out.println(10 == 10.0 ? "eq" : "ne");
        System.out.println('a' == 97.0);
        System.out.println(0.1 + 0.2 == 0.3 ? "eq" : "ne");
        System.out.println((float) 0.1 + (float) 0.2 == (float) 0.3 ? "eq" : "ne");
        System.out.println(0.5 + 0.25 == 0.75);
        System.out.println(100 * 1.1 == 110.0);
        System.out.println(3 * 0.1 == 0.3);
        System.out.println(Long.MAX_VALUE == (double) Long.MAX_VALUE);
        System.out.println((long) (double) Long.MAX_VALUE == Long.MAX_VALUE);
        System.out.println(16777217 == 16777217f);
        System.out.println(Integer.MAX_VALUE == (float) Integer.MAX_VALUE);
        System.out.println(Integer.MAX_VALUE == (double) Integer.MAX_VALUE);
    }
}
