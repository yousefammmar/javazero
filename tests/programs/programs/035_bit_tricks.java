public class Main {
    static int popcount(int x) {
        int c = 0;
        while (x != 0) { x &= x - 1; c++; }
        return c;
    }

    static boolean isPow2(long x) { return x > 0 && (x & (x - 1)) == 0; }

    static int reverseBits(int x) {
        int r = 0;
        for (int i = 0; i < 32; i++) { r = (r << 1) | (x & 1); x >>>= 1; }
        return r;
    }

    static int singleNumber(int[] a) {
        int r = 0;
        for (int v : a) r ^= v;
        return r;
    }

    static String bin(int x, int width) {
        String s = Integer.toBinaryString(x);
        return "0".repeat(Math.max(0, width - s.length())) + s;
    }

    static int grayCode(int n) { return n ^ (n >> 1); }

    public static void main(String[] args) {
        int[] vals = {0, 1, 7, 8, 255, 1023, -1, Integer.MIN_VALUE, 0x55555555};
        for (int v : vals)
            System.out.printf("%11d pop=%2d pow2=%-5b lowbit=%d hex=%s%n", v, popcount(v), isPow2(v), v & -v, Integer.toHexString(v));
        System.out.println("reverse(1)=" + reverseBits(1) + " reverse(0xF0)=" + Integer.toHexString(reverseBits(0xF0)));
        System.out.println("single: " + singleNumber(new int[]{4, 1, 2, 1, 2, 4, 9}));
        int a = 5, b = 12;
        a ^= b; b ^= a; a ^= b;
        System.out.println("swapped: " + a + " " + b);
        System.out.println("-17 >> 2 = " + (-17 >> 2) + ", -17 >>> 28 = " + (-17 >>> 28) + ", -17 << 3 = " + (-17 << 3));
        System.out.println("1 << 33 = " + (1 << 33) + ", 1L << 33 = " + (1L << 33));
        int flags = 0;
        final int READ = 1, WRITE = 2, EXEC = 4;
        flags |= READ | EXEC;
        System.out.println("flags=" + bin(flags, 3) + " canWrite=" + ((flags & WRITE) != 0));
        flags ^= WRITE;
        flags &= ~READ;
        System.out.println("flags=" + bin(flags, 3));
        for (int i = 0; i < 8; i++) System.out.print(bin(grayCode(i), 3) + " ");
        System.out.println();
        System.out.println("Integer.bitCount(255)=" + Integer.bitCount(255) + " highestOneBit(100)=" + Integer.highestOneBit(100)
            + " nlz(1)=" + Integer.numberOfLeadingZeros(1) + " ntz(8)=" + Integer.numberOfTrailingZeros(8));
        System.out.println("rotateLeft: " + Integer.toHexString(Integer.rotateLeft(0x80000001, 4)));
        int mask = 0;
        for (int bit : new int[]{1, 3, 5, 7}) mask |= 1 << bit;
        System.out.println("mask=" + mask + " " + bin(mask, 8));
        byte bt = (byte) 200;
        System.out.println("byte 200 -> " + bt + ", unsigned " + (bt & 0xFF) + ", short " + (short) 70000 + ", char " + (int) (char) -1);
        long big = 0xFFFFFFFFL;
        System.out.println(big + " " + (int) big + " " + Long.toBinaryString(5L << 40).length());
        for (int n = 1; n <= 16; n++) if (isPow2(n)) System.out.print(n + " ");
        System.out.println();
    }
}
