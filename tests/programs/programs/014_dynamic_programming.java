import java.util.*;

public class Main {
    static Map<Long, Long> memo = new HashMap<>();
    static long fib(long n) {
        if (n < 2) return n;
        Long c = memo.get(n);
        if (c != null) return c;
        long r = fib(n - 1) + fib(n - 2);
        memo.put(n, r);
        return r;
    }

    static int knapsack(int[] w, int[] v, int cap) {
        int[][] dp = new int[w.length + 1][cap + 1];
        for (int i = 1; i <= w.length; i++)
            for (int c = 0; c <= cap; c++) {
                dp[i][c] = dp[i - 1][c];
                if (w[i - 1] <= c) dp[i][c] = Math.max(dp[i][c], dp[i - 1][c - w[i - 1]] + v[i - 1]);
            }
        return dp[w.length][cap];
    }

    static String lcs(String a, String b) {
        int[][] dp = new int[a.length() + 1][b.length() + 1];
        for (int i = 1; i <= a.length(); i++)
            for (int j = 1; j <= b.length(); j++)
                dp[i][j] = a.charAt(i - 1) == b.charAt(j - 1) ? dp[i - 1][j - 1] + 1 : Math.max(dp[i - 1][j], dp[i][j - 1]);
        StringBuilder sb = new StringBuilder();
        int i = a.length(), j = b.length();
        while (i > 0 && j > 0) {
            if (a.charAt(i - 1) == b.charAt(j - 1)) { sb.append(a.charAt(i - 1)); i--; j--; }
            else if (dp[i - 1][j] >= dp[i][j - 1]) i--;
            else j--;
        }
        return sb.reverse().toString();
    }

    static int editDistance(String a, String b) {
        int[][] dp = new int[a.length() + 1][b.length() + 1];
        for (int i = 0; i <= a.length(); i++) dp[i][0] = i;
        for (int j = 0; j <= b.length(); j++) dp[0][j] = j;
        for (int i = 1; i <= a.length(); i++)
            for (int j = 1; j <= b.length(); j++) {
                int cost = a.charAt(i - 1) == b.charAt(j - 1) ? 0 : 1;
                dp[i][j] = Math.min(Math.min(dp[i - 1][j] + 1, dp[i][j - 1] + 1), dp[i - 1][j - 1] + cost);
            }
        return dp[a.length()][b.length()];
    }

    static int coinChange(int[] coins, int amount) {
        int[] dp = new int[amount + 1];
        Arrays.fill(dp, Integer.MAX_VALUE);
        dp[0] = 0;
        for (int a = 1; a <= amount; a++)
            for (int c : coins)
                if (c <= a && dp[a - c] != Integer.MAX_VALUE) dp[a] = Math.min(dp[a], dp[a - c] + 1);
        return dp[amount] == Integer.MAX_VALUE ? -1 : dp[amount];
    }

    static long countWays(int[] coins, int amount) {
        long[] ways = new long[amount + 1];
        ways[0] = 1;
        for (int c : coins) for (int a = c; a <= amount; a++) ways[a] += ways[a - c];
        return ways[amount];
    }

    static int lis(int[] a) {
        int[] dp = new int[a.length];
        int best = 0;
        for (int i = 0; i < a.length; i++) {
            dp[i] = 1;
            for (int j = 0; j < i; j++) if (a[j] < a[i]) dp[i] = Math.max(dp[i], dp[j] + 1);
            best = Math.max(best, dp[i]);
        }
        return best;
    }

    public static void main(String[] args) {
        for (long n : new long[]{10, 50, 90}) System.out.println("fib(" + n + ") = " + fib(n));
        System.out.println("memo size " + memo.size());
        System.out.println("knapsack = " + knapsack(new int[]{1, 3, 4, 5}, new int[]{1, 4, 5, 7}, 7));
        System.out.println("knapsack = " + knapsack(new int[]{10, 20, 30}, new int[]{60, 100, 120}, 50));
        System.out.println("lcs = " + lcs("AGGTAB", "GXTXAYB"));
        System.out.println("lcs = " + lcs("programming", "gaming"));
        System.out.println("edit(kitten,sitting) = " + editDistance("kitten", "sitting"));
        System.out.println("edit(sunday,saturday) = " + editDistance("sunday", "saturday"));
        System.out.println("coins min for 63 = " + coinChange(new int[]{1, 5, 10, 21, 25}, 63));
        System.out.println("coins min for 7 with {2,4} = " + coinChange(new int[]{2, 4}, 7));
        System.out.println("ways(100) = " + countWays(new int[]{1, 5, 10, 25, 50}, 100));
        System.out.println("lis = " + lis(new int[]{10, 9, 2, 5, 3, 7, 101, 18, 4, 20}));
    }
}
