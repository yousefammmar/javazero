public class Main {
    public static void main(String[] args) {
        outer:
        for (int i = 1; i <= 5; i++) {
            for (int j = 1; j <= 5; j++) {
                if (j == 4) continue outer;
                if (i == 4) break outer;
                if ((i + j) % 2 == 0) continue;
                System.out.print(i + "" + j + " ");
            }
        }
        System.out.println();
        int found = -1;
        search:
        {
            for (int k = 0; k < 100; k++) if (k * k > 500) { found = k; break search; }
            found = -2;
        }
        System.out.println(found);
        int w = 0;
        loop:
        while (true) {
            do { w++; if (w > 7) break loop; if (w % 3 == 0) continue loop; } while (w < 100);
        }
        System.out.println(w);
        int x = 0b1011_0110;
        System.out.println(x + " " + (x & 0xF) + " " + (x | 1) + " " + (x ^ 0xFF) + " " + (~x) + " " + (x << 3) + " " + (x >> 2) + " " + (-x >>> 28) + " " + (x & -x) + " " + (x & (x - 1)));
        System.out.println(Integer.toBinaryString(x) + " " + Integer.toBinaryString(x ^ (x >> 1)) + " " + 0x7fffffff + " " + 0xFFFFFFFF + " " + 0xFFFFFFFFL + " " + 017 + " " + 1_000_000 + " " + 'a' % 7 + " " + (byte) (0x7f + 1));
        long mask = 0;
        for (int b : new int[]{0, 5, 63, 32}) mask |= 1L << b;
        System.out.println(mask + " " + Long.toBinaryString(mask).length() + " " + Long.bitCount(mask) + " " + ((mask >> 63) & 1) + " " + (mask >>> 63) + " " + (1 << 31) + " " + (1 << 32) + " " + (1L << 64));
        int flags = 0;
        flags |= 1 << 2; flags |= 1 << 4; flags &= ~(1 << 2); flags ^= 1 << 0;
        System.out.println(flags + " " + ((flags & 16) != 0) + " " + Integer.toBinaryString(flags));
        byte bb = (byte) 0xF0; short sh = (short) 0x8000; char ch = (char) -1;
        System.out.println(bb + " " + (bb >> 2) + " " + (bb >>> 2) + " " + ((bb & 0xFF) >>> 2) + " " + sh + " " + (int) ch + " " + (bb & 0xFF));
        bb >>>= 1; bb += 200; 
        System.out.println(bb);
        int swap1 = 12, swap2 = 34;
        swap1 ^= swap2; swap2 ^= swap1; swap1 ^= swap2;
        System.out.println(swap1 + " " + swap2);
        int i = 5; i = i++ + ++i; int j = 3; j += j++ * 2;
        System.out.println(i + " " + j + " " + (i > 5 && j++ > 0 || j-- > 0) + " " + j + " " + (true ^ true) + " " + (5 & 3 | 4 ^ 1));
        long big = 1L << 40; int trunc = (int) big; int sh2 = 1 << 33;
        System.out.println(trunc + " " + sh2 + " " + (big >> 38) + " " + (5 / 2) + (5 % -3) + (-5 / 2) + (5.0 / 2));
    }
}
