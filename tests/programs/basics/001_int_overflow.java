public class Main {
    public static void main(String[] args) {
        int max = Integer.MAX_VALUE, min = Integer.MIN_VALUE;
        System.out.println(max + 1);
        System.out.println(min - 1);
        System.out.println(max * 2);
        System.out.println(min * -1);
        System.out.println(-min);
        System.out.println(Math.abs(min));
        System.out.println(max + max);
        System.out.println(min / -1);
        System.out.println(min % -1);
        long lmax = Long.MAX_VALUE, lmin = Long.MIN_VALUE;
        System.out.println(lmax + 1);
        System.out.println(lmin - 1);
        System.out.println(lmax * 2);
        System.out.println(-lmin);
        System.out.println(lmin / -1);
        System.out.println(Math.abs(lmin));
        int a = 46341;
        System.out.println(a * a);
        long b = a * a;
        long c = (long) a * a;
        System.out.println(b + " " + c);
        int big = 1_000_000;
        System.out.println(big * big);
        System.out.println((long) big * big);
        System.out.println(big * big / big);
        System.out.println(0x7fffffff + 0x7fffffff);
        System.out.println(0xffffffff);
        System.out.println(0x80000000);
        System.out.println(0xffffffffL);
        System.out.println(0777 + " " + 0b1010 + " " + 0xFF);
        int fact = 1;
        for (int i = 1; i <= 15; i++) {
            fact *= i;
            System.out.println(i + "! = " + fact);
        }
        long lf = 1;
        for (int i = 1; i <= 22; i++) {
            lf *= i;
            if (i > 17) System.out.println(i + "! = " + lf);
        }
        byte bb = 127; bb++;
        System.out.println(bb);
        short ss = -32768; ss--;
        System.out.println(ss);
        char cc = 0; cc--;
        System.out.println((int) cc);
        try {
            System.out.println(Math.addExact(max, 1));
        } catch (ArithmeticException e) {
            System.out.println("caught " + e.getMessage());
        }
        try {
            System.out.println(Math.multiplyExact(lmax, 2L));
        } catch (ArithmeticException e) {
            System.out.println("caught " + e.getMessage());
        }
        try {
            System.out.println(Math.toIntExact(1L << 40));
        } catch (ArithmeticException e) {
            System.out.println("caught " + e.getMessage());
        }
    }
}
