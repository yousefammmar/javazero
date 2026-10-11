import java.util.*;

public class Main {
    record Edge(int to, int w) {}

    static List<List<Edge>> graph(int n, int[][] edges, boolean directed) {
        List<List<Edge>> g = new ArrayList<>();
        for (int i = 0; i < n; i++) g.add(new ArrayList<>());
        for (int[] e : edges) {
            g.get(e[0]).add(new Edge(e[1], e[2]));
            if (!directed) g.get(e[1]).add(new Edge(e[0], e[2]));
        }
        return g;
    }

    static List<Integer> bfs(List<List<Edge>> g, int s) {
        List<Integer> order = new ArrayList<>();
        boolean[] seen = new boolean[g.size()];
        Queue<Integer> q = new LinkedList<>();
        q.add(s); seen[s] = true;
        while (!q.isEmpty()) {
            int u = q.poll();
            order.add(u);
            for (Edge e : g.get(u)) if (!seen[e.to()]) { seen[e.to()] = true; q.add(e.to()); }
        }
        return order;
    }

    static void dfs(List<List<Edge>> g, int u, boolean[] seen, List<Integer> out) {
        seen[u] = true; out.add(u);
        for (Edge e : g.get(u)) if (!seen[e.to()]) dfs(g, e.to(), seen, out);
    }

    static int[] dijkstra(List<List<Edge>> g, int s, int[] prev) {
        int[] dist = new int[g.size()];
        Arrays.fill(dist, Integer.MAX_VALUE);
        Arrays.fill(prev, -1);
        dist[s] = 0;
        PriorityQueue<int[]> pq = new PriorityQueue<>((a, b) -> Integer.compare(a[1], b[1]));
        pq.add(new int[]{s, 0});
        while (!pq.isEmpty()) {
            int[] cur = pq.poll();
            if (cur[1] > dist[cur[0]]) continue;
            for (Edge e : g.get(cur[0])) {
                int nd = cur[1] + e.w();
                if (nd < dist[e.to()]) { dist[e.to()] = nd; prev[e.to()] = cur[0]; pq.add(new int[]{e.to(), nd}); }
            }
        }
        return dist;
    }

    static List<Integer> topo(List<List<Edge>> g) {
        int n = g.size();
        int[] indeg = new int[n];
        for (List<Edge> l : g) for (Edge e : l) indeg[e.to()]++;
        PriorityQueue<Integer> q = new PriorityQueue<>();
        for (int i = 0; i < n; i++) if (indeg[i] == 0) q.add(i);
        List<Integer> res = new ArrayList<>();
        while (!q.isEmpty()) {
            int u = q.poll(); res.add(u);
            for (Edge e : g.get(u)) if (--indeg[e.to()] == 0) q.add(e.to());
        }
        return res;
    }

    static int[] parent;
    static int find(int x) { return parent[x] == x ? x : (parent[x] = find(parent[x])); }

    public static void main(String[] args) {
        int[][] edges = {{0, 1, 4}, {0, 2, 1}, {2, 1, 2}, {1, 3, 1}, {2, 3, 5}, {3, 4, 3}, {5, 6, 1}};
        List<List<Edge>> g = graph(7, edges, false);
        System.out.println("BFS from 0: " + bfs(g, 0));
        List<Integer> d = new ArrayList<>();
        dfs(g, 0, new boolean[7], d);
        System.out.println("DFS from 0: " + d);
        int[] prev = new int[7];
        int[] dist = dijkstra(g, 0, prev);
        for (int i = 0; i < 7; i++) {
            if (dist[i] == Integer.MAX_VALUE) { System.out.println(i + ": unreachable"); continue; }
            LinkedList<Integer> path = new LinkedList<>();
            for (int v = i; v != -1; v = prev[v]) path.addFirst(v);
            System.out.println(i + ": dist=" + dist[i] + " path=" + path);
        }
        List<List<Edge>> dag = graph(6, new int[][]{{5, 2, 1}, {5, 0, 1}, {4, 0, 1}, {4, 1, 1}, {2, 3, 1}, {3, 1, 1}}, true);
        System.out.println("Topo order: " + topo(dag));

        parent = new int[7];
        for (int i = 0; i < 7; i++) parent[i] = i;
        int comps = 7;
        for (int[] e : edges) {
            int a = find(e[0]), b = find(e[1]);
            if (a != b) { parent[a] = b; comps--; }
        }
        System.out.println("Connected components: " + comps);
        System.out.println("0 and 4 connected? " + (find(0) == find(4)));
        System.out.println("0 and 6 connected? " + (find(0) == find(6)));
    }
}
