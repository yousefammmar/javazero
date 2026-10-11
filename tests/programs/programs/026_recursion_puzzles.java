import java.util.*;

public class Main {
    static int solutions = 0;
    static int[] firstSolution;

    static void queens(int n, int row, int[] cols) {
        if (row == n) {
            if (solutions == 0) firstSolution = cols.clone();
            solutions++;
            return;
        }
        for (int c = 0; c < n; c++) {
            boolean ok = true;
            for (int r = 0; r < row; r++)
                if (cols[r] == c || Math.abs(cols[r] - c) == row - r) { ok = false; break; }
            if (ok) { cols[row] = c; queens(n, row + 1, cols); }
        }
    }

    static void permute(String prefix, String rest, List<String> out) {
        if (rest.isEmpty()) { out.add(prefix); return; }
        for (int i = 0; i < rest.length(); i++)
            permute(prefix + rest.charAt(i), rest.substring(0, i) + rest.substring(i + 1), out);
    }

    static void combos(int[] a, int k, int start, Deque<Integer> cur, List<List<Integer>> out) {
        if (cur.size() == k) { out.add(new ArrayList<>(cur)); return; }
        for (int i = start; i < a.length; i++) {
            cur.addLast(a[i]);
            combos(a, k, i + 1, cur, out);
            cur.removeLast();
        }
    }

    static void subsets(char[] a, int i, String cur, List<String> out) {
        if (i == a.length) { out.add("{" + cur + "}"); return; }
        subsets(a, i + 1, cur, out);
        subsets(a, i + 1, cur + a[i], out);
    }

    static int moves = 0;
    static void hanoi(int n, char from, char to, char via, boolean print) {
        if (n == 0) return;
        hanoi(n - 1, from, via, to, print);
        moves++;
        if (print) System.out.println("  disk " + n + ": " + from + " -> " + to);
        hanoi(n - 1, via, to, from, print);
    }

    static int ackermann(int m, int n) {
        if (m == 0) return n + 1;
        if (n == 0) return ackermann(m - 1, 1);
        return ackermann(m - 1, ackermann(m, n - 1));
    }

    public static void main(String[] args) {
        for (int n = 4; n <= 8; n++) {
            solutions = 0;
            queens(n, 0, new int[n]);
            System.out.println(n + "-queens: " + solutions + " solutions");
        }
        solutions = 0;
        queens(6, 0, new int[6]);
        for (int r = 0; r < 6; r++) {
            char[] line = ".".repeat(6).toCharArray();
            line[firstSolution[r]] = 'Q';
            System.out.println(new String(line));
        }
        List<String> perms = new ArrayList<>();
        permute("", "abcd", perms);
        System.out.println(perms.size() + " perms: " + perms);
        List<List<Integer>> cs = new ArrayList<>();
        combos(new int[]{1, 2, 3, 4, 5}, 3, 0, new ArrayDeque<>(), cs);
        System.out.println(cs.size() + " combos: " + cs);
        List<String> subs = new ArrayList<>();
        subsets("xyz".toCharArray(), 0, "", subs);
        System.out.println(subs);
        System.out.println("Hanoi 3:");
        hanoi(3, 'A', 'C', 'B', true);
        moves = 0;
        hanoi(10, 'A', 'C', 'B', false);
        System.out.println("Hanoi 10 moves: " + moves);
        System.out.println("ackermann(2,3)=" + ackermann(2, 3) + " ackermann(3,3)=" + ackermann(3, 3));
    }
}
