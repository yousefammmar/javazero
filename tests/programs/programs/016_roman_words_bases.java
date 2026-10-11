import java.util.*;

public class Main {
    static final int[] VALS = {1000, 900, 500, 400, 100, 90, 50, 40, 10, 9, 5, 4, 1};
    static final String[] SYMS = {"M", "CM", "D", "CD", "C", "XC", "L", "XL", "X", "IX", "V", "IV", "I"};

    static String toRoman(int n) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < VALS.length; i++)
            while (n >= VALS[i]) { sb.append(SYMS[i]); n -= VALS[i]; }
        return sb.toString();
    }

    static int fromRoman(String s) {
        Map<Character, Integer> m = Map.of('I', 1, 'V', 5, 'X', 10, 'L', 50, 'C', 100, 'D', 500, 'M', 1000);
        int total = 0;
        for (int i = 0; i < s.length(); i++) {
            int v = m.get(s.charAt(i));
            if (i + 1 < s.length() && v < m.get(s.charAt(i + 1))) total -= v; else total += v;
        }
        return total;
    }

    static final String[] ONES = {"zero", "one", "two", "three", "four", "five", "six", "seven", "eight", "nine", "ten",
        "eleven", "twelve", "thirteen", "fourteen", "fifteen", "sixteen", "seventeen", "eighteen", "nineteen"};
    static final String[] TENS = {"", "", "twenty", "thirty", "forty", "fifty", "sixty", "seventy", "eighty", "ninety"};

    static String words(int n) {
        if (n < 20) return ONES[n];
        if (n < 100) return TENS[n / 10] + (n % 10 != 0 ? "-" + ONES[n % 10] : "");
        if (n < 1000) return ONES[n / 100] + " hundred" + (n % 100 != 0 ? " and " + words(n % 100) : "");
        if (n < 1_000_000) return words(n / 1000) + " thousand" + (n % 1000 != 0 ? " " + words(n % 1000) : "");
        return words(n / 1_000_000) + " million" + (n % 1_000_000 != 0 ? " " + words(n % 1_000_000) : "");
    }

    static String toBase(long n, int base) {
        if (n == 0) return "0";
        String digits = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        StringBuilder sb = new StringBuilder();
        boolean neg = n < 0;
        n = Math.abs(n);
        while (n > 0) { sb.append(digits.charAt((int) (n % base))); n /= base; }
        if (neg) sb.append('-');
        return sb.reverse().toString();
    }

    static long fromBase(String s, int base) {
        long r = 0;
        for (char c : s.toCharArray()) r = r * base + Character.digit(c, base);
        return r;
    }

    public static void main(String[] args) {
        for (int n : new int[]{1, 4, 9, 14, 40, 90, 400, 1994, 2024, 3999}) {
            String r = toRoman(n);
            System.out.printf("%4d -> %-12s -> %d%n", n, r, fromRoman(r));
        }
        for (int n : new int[]{0, 7, 13, 21, 40, 99, 100, 101, 342, 1000, 12345, 1000001, 999999})
            System.out.println(n + ": " + words(n));
        long[] nums = {255, 1024, 0, -42, 123456789};
        for (long n : nums) {
            System.out.printf("%d -> bin %s, oct %s, hex %s, base36 %s | lib: %s %s%n", n,
                toBase(n, 2), toBase(n, 8), toBase(n, 16), toBase(n, 36),
                Long.toBinaryString(n >= 0 ? n : 0), Long.toHexString(n >= 0 ? n : 0).toUpperCase());
        }
        System.out.println(fromBase("11111111", 2) + " " + fromBase("FF", 16) + " " + fromBase("zz", 36));
        System.out.println(Integer.parseInt("-7f", 16) + " " + Integer.toString(255, 7));
    }
}
