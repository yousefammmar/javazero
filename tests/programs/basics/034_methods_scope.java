import java.util.Arrays;

public class Main {
    static int x = 10;
    static int shadow(int x) { return x * 2 + Main.x; }
    static int counter() { return ++x; }
    static int sumTo(int n) { int s = 0; for (int i = 1; i <= n; i++) s += i; return s; }
    static boolean isPrime(int n) { if (n < 2) return false; for (int i = 2; (long) i * i <= n; i++) if (n % i == 0) return false; return true; }
    static int max3(int a, int b, int c) { return Math.max(a, Math.max(b, c)); }
    static double average(int[] a) { if (a.length == 0) return Double.NaN; long s = 0; for (int v : a) s += v; return (double) s / a.length; }
    static String classify(int n) { if (n < 0) return "neg"; if (n == 0) return "zero"; return n % 2 == 0 ? "even" : "odd"; }
    static int[] minMax(int[] a) { int mn = a[0], mx = a[0]; for (int v : a) { if (v < mn) mn = v; if (v > mx) mx = v; } return new int[]{mn, mx}; }
    static void noReturn(int n) { if (n > 2) return; System.out.println("small " + n); }
    static long power(long b, int e) { long r = 1; while (e > 0) { if ((e & 1) == 1) r *= b; b *= b; e >>= 1; } return r; }
    static String binary(int n) { if (n == 0) return "0"; String r = ""; while (n > 0) { r = (n % 2) + r; n /= 2; } return r; }
    static int reverse(int n) { int r = 0; while (n != 0) { r = r * 10 + n % 10; n /= 10; } return r; }
    static boolean isArmstrong(int n) { int s = 0, t = n; while (t > 0) { int d = t % 10; s += d * d * d; t /= 10; } return s == n; }
    static int countVowels(String s) { int c = 0; for (char ch : s.toLowerCase().toCharArray()) if ("aeiou".indexOf(ch) >= 0) c++; return c; }
    static String caesar(String s, int k) { StringBuilder sb = new StringBuilder(); for (char c : s.toCharArray()) sb.append(Character.isLetter(c) ? (char) ((c - 'a' + k + 26) % 26 + 'a') : c); return sb.toString(); }
    static int[][] pascal(int n) { int[][] t = new int[n][]; for (int i = 0; i < n; i++) { t[i] = new int[i + 1]; t[i][0] = t[i][i] = 1; for (int j = 1; j < i; j++) t[i][j] = t[i - 1][j - 1] + t[i - 1][j]; } return t; }
    static void bubble(int[] a) { for (int i = 0; i < a.length - 1; i++) for (int j = 0; j < a.length - 1 - i; j++) if (a[j] > a[j + 1]) { int t = a[j]; a[j] = a[j + 1]; a[j + 1] = t; } }
    static double sqrtNewton(double v) { double g = v / 2; for (int i = 0; i < 20; i++) g = (g + v / g) / 2; return g; }
    public static void main(String[] args) {
        System.out.println(shadow(3) + " " + counter() + " " + counter() + " " + x);
        System.out.println(sumTo(100) + " " + sumTo(0) + " " + sumTo(-3));
        StringBuilder primes = new StringBuilder();
        for (int i = 0; i < 50; i++) if (isPrime(i)) primes.append(i).append(' ');
        System.out.println(primes.toString().trim());
        System.out.println(isPrime(2147483647) + " " + isPrime(1000000007) + " " + isPrime(1) + " " + isPrime(49));
        System.out.println(max3(1, 5, 3) + " " + max3(-1, -5, -3) + " " + max3(7, 7, 7));
        System.out.println(average(new int[]{1, 2, 4}) + " " + average(new int[0]) + " " + average(new int[]{Integer.MAX_VALUE, Integer.MAX_VALUE}));
        for (int n : new int[]{-3, 0, 4, 7}) System.out.print(classify(n) + " ");
        System.out.println();
        System.out.println(Arrays.toString(minMax(new int[]{4, -2, 9, 0})));
        noReturn(1); noReturn(5);
        System.out.println(power(2, 62) + " " + power(3, 40) + " " + power(10, 18) + " " + power(10, 19) + " " + power(7, 0));
        System.out.println(binary(10) + " " + binary(255) + " " + binary(0) + " " + reverse(12345) + " " + reverse(-120) + " " + reverse(1000000009));
        for (int i = 100; i < 1000; i++) if (isArmstrong(i)) System.out.print(i + " ");
        System.out.println();
        System.out.println(countVowels("Programming Language") + " " + caesar("hello world", 3) + " " + caesar(caesar("hello world", 3), -3));
        for (int[] r : pascal(6)) System.out.println(Arrays.toString(r));
        int[] data = {5, 2, 9, 1, 5, 6, -3};
        bubble(data);
        System.out.println(Arrays.toString(data));
        System.out.println(sqrtNewton(2) + " " + sqrtNewton(144) + " " + (sqrtNewton(2) == Math.sqrt(2)));
        int y = 1;
        { int z = y + 1; y = z * 2; }
        for (int i = 0; i < 2; i++) { int z = i; y += z; }
        System.out.println(y);
    }
}
