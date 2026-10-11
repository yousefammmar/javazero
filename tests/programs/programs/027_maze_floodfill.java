import java.util.*;

public class Main {
    static char[][] parse(String[] rows) {
        char[][] g = new char[rows.length][];
        for (int i = 0; i < rows.length; i++) g[i] = rows[i].toCharArray();
        return g;
    }

    static void print(char[][] g) {
        for (char[] row : g) System.out.println(new String(row));
        System.out.println();
    }

    static boolean solve(char[][] g, int r, int c, int er, int ec) {
        if (r < 0 || c < 0 || r >= g.length || c >= g[0].length) return false;
        if (g[r][c] == '#' || g[r][c] == '*' || g[r][c] == 'x') return false;
        char old = g[r][c];
        if (r == er && c == ec) return true;
        g[r][c] = '*';
        if (solve(g, r + 1, c, er, ec) || solve(g, r, c + 1, er, ec) || solve(g, r - 1, c, er, ec) || solve(g, r, c - 1, er, ec)) {
            return true;
        }
        g[r][c] = old == 'S' ? 'S' : 'x';
        return false;
    }

    static int shortest(char[][] g, int sr, int sc, int er, int ec) {
        int[][] dist = new int[g.length][g[0].length];
        for (int[] d : dist) Arrays.fill(d, -1);
        Deque<int[]> q = new ArrayDeque<>();
        q.add(new int[]{sr, sc});
        dist[sr][sc] = 0;
        int[] dr = {1, -1, 0, 0}, dc = {0, 0, 1, -1};
        while (!q.isEmpty()) {
            int[] cur = q.poll();
            if (cur[0] == er && cur[1] == ec) return dist[er][ec];
            for (int k = 0; k < 4; k++) {
                int nr = cur[0] + dr[k], nc = cur[1] + dc[k];
                if (nr >= 0 && nc >= 0 && nr < g.length && nc < g[0].length && g[nr][nc] != '#' && dist[nr][nc] < 0) {
                    dist[nr][nc] = dist[cur[0]][cur[1]] + 1;
                    q.add(new int[]{nr, nc});
                }
            }
        }
        return -1;
    }

    static int fill(char[][] g, int r, int c, char target, char repl) {
        if (r < 0 || c < 0 || r >= g.length || c >= g[r].length || g[r][c] != target) return 0;
        g[r][c] = repl;
        return 1 + fill(g, r + 1, c, target, repl) + fill(g, r - 1, c, target, repl)
            + fill(g, r, c + 1, target, repl) + fill(g, r, c - 1, target, repl);
    }

    public static void main(String[] args) {
        String[] maze = {
            "S.#.......",
            ".##.#####.",
            "....#...#.",
            ".####.#.#.",
            "......#...",
            "#####.###.",
            "......#..E"
        };
        char[][] g = parse(maze);
        System.out.println("Shortest path length: " + shortest(g, 0, 0, 6, 9));
        boolean found = solve(g, 0, 0, 6, 9);
        System.out.println("DFS found path: " + found);
        print(g);
        char[][] blocked = parse(new String[]{"S#.", "##.", "..E"});
        System.out.println("Blocked: " + shortest(blocked, 0, 0, 2, 2) + " " + solve(blocked, 0, 0, 2, 2));
        char[][] img = parse(new String[]{
            "..##....",
            ".#..#...",
            ".#..#.##",
            "..##..#.",
            "......##"
        });
        int filled = fill(img, 2, 2, '.', 'o');
        System.out.println("Filled inner region: " + filled);
        int outer = fill(img, 0, 0, '.', '~');
        System.out.println("Filled outer region: " + outer);
        print(img);
    }
}
