import java.util.*;

public class Main {
    static class Poly {
        private final int[] c;

        Poly(int... coeffs) {
            int n = coeffs.length;
            while (n > 1 && coeffs[n - 1] == 0) n--;
            c = Arrays.copyOf(coeffs, n);
        }

        int degree() { return c.length - 1; }

        Poly add(Poly o) {
            int[] r = new int[Math.max(c.length, o.c.length)];
            for (int i = 0; i < r.length; i++) r[i] = (i < c.length ? c[i] : 0) + (i < o.c.length ? o.c[i] : 0);
            return new Poly(r);
        }

        Poly mul(Poly o) {
            int[] r = new int[c.length + o.c.length - 1];
            for (int i = 0; i < c.length; i++)
                for (int j = 0; j < o.c.length; j++) r[i + j] += c[i] * o.c[j];
            return new Poly(r);
        }

        Poly derivative() {
            if (c.length == 1) return new Poly(0);
            int[] r = new int[c.length - 1];
            for (int i = 1; i < c.length; i++) r[i - 1] = c[i] * i;
            return new Poly(r);
        }

        long eval(long x) {
            long r = 0;
            for (int i = c.length - 1; i >= 0; i--) r = r * x + c[i];
            return r;
        }

        Poly pow(int n) {
            Poly r = new Poly(1);
            for (int i = 0; i < n; i++) r = r.mul(this);
            return r;
        }

        @Override public boolean equals(Object o) { return o instanceof Poly p && Arrays.equals(c, p.c); }
        @Override public int hashCode() { return Arrays.hashCode(c); }

        @Override public String toString() {
            StringBuilder sb = new StringBuilder();
            for (int i = c.length - 1; i >= 0; i--) {
                if (c[i] == 0 && c.length > 1) continue;
                int a = Math.abs(c[i]);
                if (sb.length() > 0) sb.append(c[i] < 0 ? " - " : " + ");
                else if (c[i] < 0) sb.append("-");
                if (a != 1 || i == 0) sb.append(a);
                if (i >= 1) sb.append("x");
                if (i > 1) sb.append("^").append(i);
            }
            return sb.toString();
        }
    }

    public static void main(String[] args) {
        Poly p = new Poly(1, -3, 0, 2);
        Poly q = new Poly(-1, 1);
        System.out.println("p = " + p + "  (deg " + p.degree() + ")");
        System.out.println("q = " + q);
        System.out.println("p + q = " + p.add(q));
        System.out.println("p * q = " + p.mul(q));
        System.out.println("p' = " + p.derivative() + ", p'' = " + p.derivative().derivative());
        System.out.println("(x+1)^5 = " + new Poly(1, 1).pow(5));
        System.out.println("(x-1)^4 = " + new Poly(-1, 1).pow(4));
        for (int x = -2; x <= 3; x++) System.out.print("p(" + x + ")=" + p.eval(x) + " ");
        System.out.println();
        System.out.println("zero: " + new Poly(0, 0, 0) + " const: " + new Poly(7) + " neg: " + new Poly(0, -1));
        System.out.println(new Poly(1, 2).equals(new Poly(1, 2, 0, 0)) + " " + new Poly(1, 2).hashCode());
        System.out.println("p(10) = " + p.eval(10) + ", big: " + new Poly(1, 1).pow(20).eval(3));
        Poly acc = new Poly(1);
        for (int r = 1; r <= 5; r++) acc = acc.mul(new Poly(-r, 1));
        System.out.println("prod (x-r), r=1..5: " + acc);
        System.out.println("roots check: " + acc.eval(1) + acc.eval(3) + acc.eval(5) + " " + acc.eval(6));
    }
}
