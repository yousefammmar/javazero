import java.util.ArrayList;
import java.util.List;

public class Main {
    static long fact(int n) { return n <= 1 ? 1 : n * fact(n - 1); }
    static int factInt(int n) { return n <= 1 ? 1 : n * factInt(n - 1); }
    static int fib(int n) { return n < 2 ? n : fib(n - 1) + fib(n - 2); }
    static long fibMemo(int n, long[] memo) {
        if (n < 2) return n;
        if (memo[n] != 0) return memo[n];
        return memo[n] = fibMemo(n - 1, memo) + fibMemo(n - 2, memo);
    }
    static int gcd(int a, int b) { return b == 0 ? a : gcd(b, a % b); }
    static int hanoiMoves = 0;
    static void hanoi(int n, char from, char to, char via, boolean print) {
        if (n == 0) return;
        hanoi(n - 1, from, via, to, print);
        hanoiMoves++;
        if (print) System.out.println("move disk " + n + " " + from + "->" + to);
        hanoi(n - 1, via, to, from, print);
    }
    static void permute(String prefix, String rest, List<String> out) {
        if (rest.isEmpty()) { out.add(prefix); return; }
        for (int i = 0; i < rest.length(); i++)
            permute(prefix + rest.charAt(i), rest.substring(0, i) + rest.substring(i + 1), out);
    }
    static int ack(int m, int n) {
        if (m == 0) return n + 1;
        if (n == 0) return ack(m - 1, 1);
        return ack(m - 1, ack(m, n - 1));
    }
    static int power(int b, int e) { return e == 0 ? 1 : (e % 2 == 0 ? power(b * b, e / 2) : b * power(b, e - 1)); }
    static String rev(String s) { return s.isEmpty() ? "" : rev(s.substring(1)) + s.charAt(0); }
    static boolean isPal(String s) { return s.length() < 2 || (s.charAt(0) == s.charAt(s.length() - 1) && isPal(s.substring(1, s.length() - 1))); }
    static int digitSum(int n) { return n == 0 ? 0 : n % 10 + digitSum(n / 10); }
    static boolean even(int n) { return n == 0 || odd(n - 1); }
    static boolean odd(int n) { return n != 0 && even(n - 1); }
    static int binSearch(int[] a, int key, int lo, int hi) {
        if (lo > hi) return -1;
        int mid = (lo + hi) >>> 1;
        if (a[mid] == key) return mid;
        return a[mid] < key ? binSearch(a, key, mid + 1, hi) : binSearch(a, key, lo, mid - 1);
    }
    static void subsets(int[] a, int i, String cur) {
        if (i == a.length) { System.out.print("{" + cur.trim() + "} "); return; }
        subsets(a, i + 1, cur);
        subsets(a, i + 1, cur + " " + a[i]);
    }
    static int depth(int n) { return n == 0 ? 0 : 1 + depth(n - 1); }
    static String toBin(int n) { return n < 2 ? "" + n : toBin(n / 2) + n % 2; }
    public static void main(String[] args) {
        for (int i = 0; i <= 21; i += 3) System.out.println(i + "! = " + fact(i));
        System.out.println(fact(20) + " " + fact(21) + " " + fact(25));
        System.out.println(factInt(12) + " " + factInt(13) + " " + factInt(17));
        for (int i = 0; i < 15; i++) System.out.print(fib(i) + " ");
        System.out.println();
        System.out.println(fibMemo(50, new long[51]) + " " + fibMemo(90, new long[91]));
        System.out.println(gcd(48, 18) + " " + gcd(17, 5) + " " + gcd(0, 9) + " " + gcd(100, 75));
        hanoi(3, 'A', 'C', 'B', true);
        hanoi(10, 'A', 'C', 'B', false);
        System.out.println(hanoiMoves);
        List<String> perms = new ArrayList<>();
        permute("", "abc", perms);
        System.out.println(perms);
        perms.clear();
        permute("", "abcd", perms);
        System.out.println(perms.size() + " " + perms.get(0) + " " + perms.get(23));
        System.out.println(ack(0, 0) + " " + ack(1, 2) + " " + ack(2, 3) + " " + ack(3, 3));
        System.out.println(power(2, 10) + " " + power(3, 13) + " " + power(2, 31) + " " + power(7, 0));
        System.out.println(rev("recursion") + " " + isPal("racecar") + " " + isPal("abca") + " " + isPal(""));
        System.out.println(digitSum(98765) + " " + digitSum(0));
        System.out.println(even(10) + " " + odd(7) + " " + even(7));
        int[] sorted = {1, 3, 5, 7, 9, 11, 13};
        System.out.println(binSearch(sorted, 7, 0, 6) + " " + binSearch(sorted, 8, 0, 6) + " " + binSearch(sorted, 1, 0, 6) + " " + binSearch(sorted, 13, 0, 6));
        subsets(new int[]{1, 2, 3}, 0, "");
        System.out.println();
        System.out.println(depth(5000));
        System.out.println(toBin(37) + " " + toBin(255) + " " + toBin(1));
    }
}
