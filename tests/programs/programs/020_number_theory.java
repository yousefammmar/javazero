import java.math.BigInteger;
import java.util.*;

public class Main {
    static List<Integer> sieve(int n) {
        boolean[] comp = new boolean[n + 1];
        List<Integer> primes = new ArrayList<>();
        for (int i = 2; i <= n; i++) {
            if (comp[i]) continue;
            primes.add(i);
            for (long j = (long) i * i; j <= n; j += i) comp[(int) j] = true;
        }
        return primes;
    }

    static Map<Integer, Integer> factorize(int n) {
        Map<Integer, Integer> f = new LinkedHashMap<>();
        for (int p = 2; (long) p * p <= n; p++)
            while (n % p == 0) { f.merge(p, 1, Integer::sum); n /= p; }
        if (n > 1) f.merge(n, 1, Integer::sum);
        return f;
    }

    static long gcd(long a, long b) { return b == 0 ? a : gcd(b, a % b); }
    static long lcm(long a, long b) { return a / gcd(a, b) * b; }

    static boolean perfect(int n) {
        int s = 1;
        for (int d = 2; d * d <= n; d++)
            if (n % d == 0) { s += d; if (d != n / d) s += n / d; }
        return n > 1 && s == n;
    }

    static int collatz(long n) {
        int steps = 0;
        while (n != 1) { n = n % 2 == 0 ? n / 2 : 3 * n + 1; steps++; }
        return steps;
    }

    static long power(long b, int e) {
        long r = 1;
        while (e > 0) {
            if ((e & 1) == 1) r *= b;
            b *= b;
            e >>= 1;
        }
        return r;
    }

    static long modPow(long b, long e, long m) {
        long r = 1; b %= m;
        while (e > 0) { if ((e & 1) == 1) r = r * b % m; b = b * b % m; e >>= 1; }
        return r;
    }

    static int digitSum(long n) { int s = 0; while (n > 0) { s += n % 10; n /= 10; } return s; }

    public static void main(String[] args) {
        List<Integer> p = sieve(100);
        System.out.println(p.size() + " primes: " + p);
        for (int n : new int[]{360, 97, 1001, 65536, 999999937}) System.out.println(n + " = " + factorize(n));
        System.out.println("gcd(84,36)=" + gcd(84, 36) + " lcm(4,6)=" + lcm(4, 6));
        long l = 1;
        for (int i = 1; i <= 20; i++) l = lcm(l, i);
        System.out.println("lcm(1..20) = " + l);
        List<Integer> perf = new ArrayList<>();
        for (int i = 1; i < 10000; i++) if (perfect(i)) perf.add(i);
        System.out.println("perfect: " + perf);
        System.out.println("collatz(27)=" + collatz(27) + " collatz(97)=" + collatz(97));
        System.out.println("2^62=" + power(2, 62) + " 2^63=" + power(2, 63) + " 2^64=" + power(2, 64));
        System.out.println("3^50 overflowed = " + power(3, 50) + ", exact = " + BigInteger.valueOf(3).pow(50));
        System.out.println("modPow=" + modPow(2, 1_000_000, 1_000_000_007L));
        BigInteger f = BigInteger.ONE;
        for (int i = 2; i <= 30; i++) f = f.multiply(BigInteger.valueOf(i));
        System.out.println("30! = " + f + " digit sum " + digitSum(123456789012345L));
        System.out.println("int overflow: " + (Integer.MAX_VALUE + 1) + " " + Math.abs(Integer.MIN_VALUE));
        int[][] pascal = new int[8][];
        for (int i = 0; i < 8; i++) {
            pascal[i] = new int[i + 1];
            pascal[i][0] = pascal[i][i] = 1;
            for (int j = 1; j < i; j++) pascal[i][j] = pascal[i - 1][j - 1] + pascal[i - 1][j];
            System.out.println(" ".repeat((8 - i) * 2) + Arrays.toString(pascal[i]).replaceAll("[\\[\\],]", " "));
        }
    }
}
