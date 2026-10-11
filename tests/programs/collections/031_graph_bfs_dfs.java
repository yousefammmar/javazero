import java.util.*;
public class Main {
    static Map<Integer, List<Integer>> g = new TreeMap<>();
    static void edge(int a, int b) {
        g.computeIfAbsent(a, k -> new ArrayList<>()).add(b);
        g.computeIfAbsent(b, k -> new ArrayList<>()).add(a);
    }
    static void dfs(int n, Set<Integer> seen, List<Integer> order) {
        if (!seen.add(n)) return;
        order.add(n);
        for (int m : g.get(n)) dfs(m, seen, order);
    }
    public static void main(String[] args) {
        int[][] es = {{1, 2}, {1, 3}, {2, 4}, {3, 4}, {4, 5}, {6, 7}, {5, 8}, {8, 9}, {3, 10}};
        for (int[] e : es) edge(e[0], e[1]);
        System.out.println(g);
        Queue<Integer> q = new ArrayDeque<>();
        Map<Integer, Integer> dist = new HashMap<>();
        q.add(1); dist.put(1, 0);
        List<Integer> order = new ArrayList<>();
        while (!q.isEmpty()) {
            int n = q.poll();
            order.add(n);
            for (int m : g.get(n)) if (!dist.containsKey(m)) { dist.put(m, dist.get(n) + 1); q.add(m); }
        }
        System.out.println("BFS " + order);
        System.out.println("dist " + dist);
        List<Integer> d = new ArrayList<>();
        dfs(1, new HashSet<>(), d);
        System.out.println("DFS rec " + d);
        Deque<Integer> st = new ArrayDeque<>();
        Set<Integer> seen = new LinkedHashSet<>();
        st.push(1);
        while (!st.isEmpty()) {
            int n = st.pop();
            if (!seen.add(n)) continue;
            List<Integer> nb = new ArrayList<>(g.get(n));
            Collections.reverse(nb);
            for (int m : nb) st.push(m);
        }
        System.out.println("DFS iter " + seen);
        // components
        Set<Integer> visited = new HashSet<>();
        int comps = 0;
        for (int n : g.keySet()) if (!visited.contains(n)) { comps++; List<Integer> c = new ArrayList<>(); dfs(n, visited, c); System.out.println("component " + comps + ": " + c); }
        // shortest path reconstruction
        Map<Integer, Integer> parent = new HashMap<>();
        Queue<Integer> bq = new LinkedList<>();
        bq.add(1); parent.put(1, -1);
        while (!bq.isEmpty()) { int n = bq.remove(); for (int m : g.get(n)) if (!parent.containsKey(m)) { parent.put(m, n); bq.add(m); } }
        LinkedList<Integer> path = new LinkedList<>();
        for (int at = 9; at != -1; at = parent.get(at)) path.addFirst(at);
        System.out.println("path 1->9 " + path);
        // grid BFS
        String[] maze = {"S.#.....", ".##.###.", "....#...", ".####.#.", "......#E"};
        int R = maze.length, C = maze[0].length();
        int[][] dd = new int[R][C];
        for (int[] row : dd) Arrays.fill(row, -1);
        Deque<int[]> gq = new ArrayDeque<>();
        gq.add(new int[]{0, 0}); dd[0][0] = 0;
        int[][] dirs = {{0, 1}, {1, 0}, {0, -1}, {-1, 0}};
        while (!gq.isEmpty()) {
            int[] cur = gq.poll();
            for (int[] dir : dirs) {
                int nr = cur[0] + dir[0], nc = cur[1] + dir[1];
                if (nr >= 0 && nr < R && nc >= 0 && nc < C && maze[nr].charAt(nc) != '#' && dd[nr][nc] < 0) { dd[nr][nc] = dd[cur[0]][cur[1]] + 1; gq.add(new int[]{nr, nc}); }
            }
        }
        System.out.println("maze dist to E: " + dd[R - 1][C - 1]);
    }
}
