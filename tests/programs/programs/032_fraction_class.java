import java.util.*;

public class Main {
    static final class Fraction implements Comparable<Fraction> {
        private final long num, den;

        Fraction(long n, long d) {
            if (d == 0) throw new ArithmeticException("zero denominator");
            if (d < 0) { n = -n; d = -d; }
            long g = gcd(Math.abs(n), d);
            num = n / g; den = d / g;
        }

        static long gcd(long a, long b) { return b == 0 ? (a == 0 ? 1 : a) : gcd(b, a % b); }

        Fraction add(Fraction o) { return new Fraction(num * o.den + o.num * den, den * o.den); }
        Fraction sub(Fraction o) { return new Fraction(num * o.den - o.num * den, den * o.den); }
        Fraction mul(Fraction o) { return new Fraction(num * o.num, den * o.den); }
        Fraction div(Fraction o) { return new Fraction(num * o.den, den * o.num); }
        Fraction negate() { return new Fraction(-num, den); }
        double value() { return (double) num / den; }

        @Override public int compareTo(Fraction o) { return Long.compare(num * o.den, o.num * den); }
        @Override public boolean equals(Object o) {
            return o instanceof Fraction f && f.num == num && f.den == den;
        }
        @Override public int hashCode() { return Objects.hash(num, den); }
        @Override public String toString() { return den == 1 ? Long.toString(num) : num + "/" + den; }
    }

    static Fraction parse(String s) {
        String[] p = s.split("/");
        return p.length == 1 ? new Fraction(Long.parseLong(p[0]), 1) : new Fraction(Long.parseLong(p[0]), Long.parseLong(p[1]));
    }

    public static void main(String[] args) {
        Fraction a = new Fraction(1, 2), b = new Fraction(3, 4), c = new Fraction(-6, -8), d = new Fraction(10, -4);
        System.out.println(a + " " + b + " " + c + " " + d);
        System.out.println("a+b=" + a.add(b) + " a-b=" + a.sub(b) + " a*b=" + a.mul(b) + " a/b=" + a.div(b));
        System.out.println("b equals c? " + b.equals(c) + ", hash equal? " + (b.hashCode() == c.hashCode()));
        System.out.println("compare a,b: " + a.compareTo(b) + ", b,a: " + b.compareTo(a) + ", b,c: " + b.compareTo(c));
        Fraction sum = new Fraction(0, 1);
        for (int i = 1; i <= 10; i++) sum = sum.add(new Fraction(1, i));
        System.out.println("H(10) = " + sum + " ~ " + sum.value());
        List<Fraction> fs = new ArrayList<>();
        for (String s : "1/3 -1/2 5/6 2 7/8 0/5 3/9 -3/-2".split(" ")) fs.add(parse(s));
        System.out.println("unsorted " + fs);
        Collections.sort(fs);
        System.out.println("sorted   " + fs);
        System.out.println("max " + Collections.max(fs) + " min " + Collections.min(fs));
        Set<Fraction> set = new HashSet<>(fs);
        System.out.println("distinct " + set.size() + " of " + fs.size());
        TreeSet<Fraction> ts = new TreeSet<>(fs);
        System.out.println("tree " + ts + " first=" + ts.first() + " ceil(1/2)=" + ts.ceiling(a));
        try {
            a.div(new Fraction(0, 3));
        } catch (ArithmeticException e) {
            System.out.println("Error: " + e.getMessage());
        }
        Fraction x = new Fraction(1, 1);
        for (int i = 0; i < 5; i++) x = x.mul(new Fraction(2, 3));
        System.out.println("(2/3)^5 = " + x + " negated " + x.negate());
    }
}
