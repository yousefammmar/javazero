public class Main {
    public static void main(String[] args) {
        System.out.println(Integer.MAX_VALUE + 1);
        System.out.println(Integer.MIN_VALUE - 1);
        System.out.println(Integer.MAX_VALUE * 2);
        System.out.println(Long.MAX_VALUE + 1);
        System.out.println(Long.MIN_VALUE - 1);
        System.out.println(Long.MAX_VALUE * Long.MAX_VALUE);
        System.out.println(-Integer.MIN_VALUE);
        System.out.println(Integer.MIN_VALUE / -1);
        System.out.println(Integer.toBinaryString(-8) + " " + Integer.toBinaryString(10) + " " + Integer.toBinaryString(0));
        System.out.println(Integer.toHexString(-1) + " " + Integer.toHexString(255) + " " + Integer.toHexString(Integer.MIN_VALUE));
        System.out.println(Integer.toOctalString(-1) + " " + Integer.toOctalString(64));
        System.out.println(Long.toBinaryString(-1L).length() + " " + Long.toHexString(-255L) + " " + Long.toOctalString(Long.MIN_VALUE));
        System.out.println(Integer.parseInt("-123") + " " + Integer.parseInt("+45") + " " + Integer.parseInt("ff", 16) + " " + Integer.parseInt("-1010", 2) + " " + Integer.parseInt("zz", 36) + " " + Integer.parseInt("007"));
        System.out.println(Integer.valueOf("12") + Integer.valueOf(3) + " " + Long.parseLong("-9223372036854775808"));
        System.out.println(Integer.compare(3, 5) + " " + Integer.compare(5, 5) + " " + Integer.compare(9, 1) + " " + Long.compare(1, 2) + " " + Integer.signum(-9));
        System.out.println(Integer.reverse(1) + " " + Integer.reverseBytes(0x01020304) + " " + Integer.bitCount(255) + " " + Integer.bitCount(-1) + " " + Long.bitCount(-1L));
        System.out.println(Integer.highestOneBit(100) + " " + Integer.lowestOneBit(100) + " " + Integer.numberOfTrailingZeros(8) + " " + Integer.numberOfLeadingZeros(1) + " " + Integer.numberOfTrailingZeros(0) + " " + Long.numberOfLeadingZeros(1L));
        System.out.println(Integer.rotateLeft(1, 33) + " " + Integer.rotateRight(1, 1));
        System.out.println(Integer.toString(255, 16) + " " + Integer.toString(-255, 2) + " " + Long.toString(100L, 36));
        System.out.println(Integer.MAX_VALUE + " " + Integer.MIN_VALUE + " " + Long.MAX_VALUE + " " + Long.MIN_VALUE + " " + Short.MAX_VALUE + " " + Byte.MIN_VALUE);
        System.out.println(Integer.sum(1, 2) + " " + Integer.max(3, 9) + " " + Integer.min(3, 9));
        System.out.println((byte) 200 + " " + (short) 70000 + " " + (int) 3000000000L + " " + (int) 3.99e10 + " " + (long) -1e30 + " " + (int) Double.NaN + " " + (char) 65.7 + " " + (byte) -129);
        System.out.println(Integer.parseUnsignedInt("4294967295") + " " + Integer.toUnsignedString(-1) + " " + Integer.divideUnsigned(-1, 2) + " " + Long.toUnsignedString(-1L));
        System.out.println((-17 >> 2) + " " + (-17 >>> 28) + " " + (1 << 35) + " " + (1L << 35) + " " + (-1L >>> 60));
        try { Integer.parseInt("2147483648"); } catch (NumberFormatException e) { System.out.println(e.getMessage()); }
        try { Integer.parseInt(""); } catch (NumberFormatException e) { System.out.println(e.getMessage()); }
        try { Integer.parseInt(null); } catch (NumberFormatException e) { System.out.println(e.getMessage()); }
        try { Integer.parseInt("12", 99); } catch (NumberFormatException e) { System.out.println(e.getMessage()); }
        Integer a = 127, b = 127, c = 128, d = 128;
        System.out.println((a == b) + " " + (c == d) + " " + c.equals(d));
    }
}
