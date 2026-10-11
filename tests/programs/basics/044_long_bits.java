public class Main {
    public static void main(String[] args) {
        long a = 0x0123456789ABCDEFL;
        System.out.println(a + " " + Long.toHexString(a) + " " + Long.toBinaryString(a).length() + " " + Long.toOctalString(a));
        System.out.println(Long.toHexString(a << 4) + " " + Long.toHexString(a >> 4) + " " + Long.toHexString(a >>> 60) + " " + Long.toHexString(~a));
        System.out.println((a << 64) == a);
        System.out.println((a << 65) == (a << 1));
        System.out.println((a >> 64) == a);
        System.out.println(Long.toHexString(-1L >>> 1) + " " + Long.toHexString(-1L >> 1) + " " + Long.toHexString(-1L << 63) + " " + (-1L >>> 63));
        System.out.println((int) (a >>> 32) + " " + (int) a + " " + (short) a + " " + (byte) a + " " + (char) a + 0);
        System.out.println(Long.bitCount(a) + " " + Long.numberOfLeadingZeros(a) + " " + Long.numberOfTrailingZeros(a << 8) + " " + Long.reverse(1L) + " " + Long.rotateLeft(a, 8) + " " + Long.highestOneBit(a));
        long mask = (1L << 40) - 1;
        System.out.println(mask + " " + (a & mask) + " " + (a | mask) + " " + (a ^ mask));
        int shiftAmt = 33;
        System.out.println((1 << shiftAmt) + " " + (1L << shiftAmt) + " " + (1 << (shiftAmt & 31)) + " " + (-16 >> shiftAmt) + " " + (-16L >> shiftAmt) + " " + (-16 >>> shiftAmt) + " " + (-16L >>> shiftAmt));
        long packed = 0;
        int[] fields = {5, 300, 70000, 9};
        int[] widths = {4, 12, 20, 8};
        int shift = 0;
        for (int i = 0; i < fields.length; i++) { packed |= ((long) fields[i] & ((1L << widths[i]) - 1)) << shift; shift += widths[i]; }
        System.out.println(packed + " " + Long.toHexString(packed));
        shift = 0;
        for (int i = 0; i < fields.length; i++) { System.out.print(((packed >>> shift) & ((1L << widths[i]) - 1)) + " "); shift += widths[i]; }
        System.out.println();
        long x = Long.MAX_VALUE;
        x++;
        System.out.println(x + " " + (x == Long.MIN_VALUE) + " " + (x - 1) + " " + (-x) + " " + Math.abs(x) + " " + (x >> 63) + " " + (x >>> 63));
        long seed = 42;
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 6; i++) {
            seed = (seed * 0x5DEECE66DL + 0xBL) & ((1L << 48) - 1);
            sb.append((int) (seed >>> 16)).append(' ');
        }
        System.out.println(sb.toString().trim());
        long xs = 88172645463325252L;
        for (int i = 0; i < 5; i++) { xs ^= xs << 13; xs ^= xs >>> 7; xs ^= xs << 17; System.out.print(xs + " "); }
        System.out.println();
        long fnv = 0xcbf29ce484222325L;
        for (char c : "javazero".toCharArray()) { fnv ^= c; fnv *= 0x100000001b3L; }
        System.out.println(fnv + " " + Long.toUnsignedString(fnv) + " " + Long.compareUnsigned(fnv, 0) + " " + Long.remainderUnsigned(fnv, 1000) + " " + Long.divideUnsigned(fnv, 1000));
        int bits = Float.floatToIntBits(-6.25f);
        System.out.println(Integer.toBinaryString(bits) + " sign=" + (bits >>> 31) + " exp=" + ((bits >> 23) & 0xFF) + " mant=" + (bits & 0x7FFFFF));
        long dbits = Double.doubleToLongBits(0.1);
        System.out.println(Long.toHexString(dbits) + " sign=" + (dbits >>> 63) + " exp=" + ((dbits >> 52) & 0x7FF) + " mant=" + Long.toHexString(dbits & 0xFFFFFFFFFFFFFL));
        System.out.println(Double.longBitsToDouble(0x4000000000000000L) + " " + Double.longBitsToDouble(0x7FF0000000000000L) + " " + Double.longBitsToDouble(1L) + " " + Float.intBitsToFloat(0x3fc00000));
        System.out.println((5L & 3) + " " + (5L | 3) + " " + (5L ^ 3) + " " + (~5L) + " " + (5 & 3L) + " " + (-5L >> 1) + " " + (-5L >>> 62) + " " + (-5L / 2) + " " + (-5L % 3));
        System.out.println(Long.parseLong("7fffffffffffffff", 16) + " " + Long.parseUnsignedLong("ffffffffffffffff", 16) + " " + Long.toString(-255L, 16) + " " + Long.toUnsignedString(-255L, 16));
        System.out.println(Long.MAX_VALUE + Long.MAX_VALUE + " " + Long.MIN_VALUE + Long.MIN_VALUE + " " + (Long.MIN_VALUE + Long.MIN_VALUE) + " " + Long.MAX_VALUE * Long.MAX_VALUE + " " + Long.MIN_VALUE * -1);
        System.out.println(Math.multiplyHigh(Long.MAX_VALUE, 4L) + " " + Math.floorMod(Long.MIN_VALUE, 10L) + " " + Math.floorDiv(Long.MIN_VALUE, 10L) + " " + Long.MIN_VALUE / 10 + " " + Long.MIN_VALUE % 10);
    }
}
