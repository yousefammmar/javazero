public class Main {
    public static void main(String[] args) {
        int i = 0;
        while (i < 5) { System.out.print(i++ + " "); }
        System.out.println();
        do { System.out.print("do" + i + " "); i--; } while (i > 3);
        System.out.println();
        do { System.out.println("runs once"); } while (false);
        for (int a = 0, b = 10; a < b; a += 3, b--) System.out.print("(" + a + "," + b + ")");
        System.out.println();
        for (;;) { if (++i > 8) break; }
        System.out.println(i);
        outer:
        for (int r = 0; r < 4; r++) {
            for (int c = 0; c < 4; c++) {
                if (c == 2) continue outer;
                if (r == 3) break outer;
                System.out.print(r + "" + c + " ");
            }
        }
        System.out.println();
        int found = -1;
        search:
        for (int x = 1; x < 20; x++) {
            for (int y = x; y < 20; y++) {
                if (x * y == 91) { found = x * 100 + y; break search; }
            }
        }
        System.out.println(found);
        lbl:
        {
            System.out.println("in block");
            if (found > 0) break lbl;
            System.out.println("not reached");
        }
        int w = 0;
        loop:
        while (true) {
            w++;
            switch (w) {
                case 2: continue loop;
                case 5: break loop;
                default: System.out.print("w" + w + " ");
            }
        }
        System.out.println();
        int[] nums = {3, 1, 4, 1, 5, 9, 2, 6};
        int sum = 0;
        for (int n : nums) { if (n == 1) continue; if (n == 9) break; sum += n; }
        System.out.println(sum);
        for (int row = 1; row <= 5; row++) {
            for (int col = 1; col <= row; col++) System.out.print(row * col + (col < row ? "\t" : ""));
            System.out.println();
        }
        for (int k = 5; k > 0; k -= 2) System.out.print(k);
        System.out.println();
        for (int k = 0; k < 3; k++) { int sq = k * k; System.out.print(sq); }
        System.out.println();
        long big = 0;
        for (int k = 0; k < 100000; k++) big += k;
        System.out.println(big);
        int ct = 0;
        for (int p = 2; p < 100; p++) {
            boolean prime = true;
            for (int q = 2; q * q <= p; q++) if (p % q == 0) { prime = false; break; }
            if (prime) ct++;
        }
        System.out.println(ct + " primes below 100");
        int n = 27, steps = 0;
        while (n != 1) { n = (n % 2 == 0) ? n / 2 : 3 * n + 1; steps++; }
        System.out.println("collatz 27: " + steps);
        for (char ch : "abc".toCharArray()) for (int rep = 0; rep < 2; rep++) System.out.print(ch);
        System.out.println();
        double dd = 0;
        for (double v = 0; v < 1.0; v += 0.1) dd++;
        System.out.println(dd);
        int z = 10;
        while (z --> 7) System.out.print(z + " ");
        System.out.println();
        for (int u = 0; u < 3; u++) for (int v = 0; v < 3; v++) { if (v > u) break; System.out.print(u + "-" + v + " "); }
        System.out.println();
    }
}
