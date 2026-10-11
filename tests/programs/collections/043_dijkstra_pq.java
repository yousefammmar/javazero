import java.util.*;
public class Main {
    record Edge(int to, int w) {}
    record State(int node, int dist) {}
    public static void main(String[] args) {
        int n = 8;
        List<List<Edge>> adj = new ArrayList<>();
        for (int i = 0; i < n; i++) adj.add(new ArrayList<>());
        int[][] es = {{0, 1, 4}, {0, 2, 1}, {2, 1, 2}, {1, 3, 5}, {2, 3, 8}, {3, 4, 3}, {4, 5, 1}, {3, 5, 6}, {5, 6, 2}, {2, 7, 20}, {6, 7, 1}};
        for (int[] e : es) { adj.get(e[0]).add(new Edge(e[1], e[2])); adj.get(e[1]).add(new Edge(e[0], e[2])); }
        int[] dist = new int[n];
        Arrays.fill(dist, Integer.MAX_VALUE);
        int[] prev = new int[n];
        Arrays.fill(prev, -1);
        dist[0] = 0;
        PriorityQueue<State> pq = new PriorityQueue<>(Comparator.comparingInt(State::dist));
        pq.add(new State(0, 0));
        int pops = 0;
        while (!pq.isEmpty()) {
            State s = pq.poll();
            pops++;
            if (s.dist() > dist[s.node()]) continue;
            System.out.println("settle " + s);
            for (Edge e : adj.get(s.node())) {
                int nd = s.dist() + e.w();
                if (nd < dist[e.to()]) { dist[e.to()] = nd; prev[e.to()] = s.node(); pq.add(new State(e.to(), nd)); }
            }
        }
        System.out.println("pops " + pops);
        System.out.println(Arrays.toString(dist));
        System.out.println(Arrays.toString(prev));
        for (int t = 1; t < n; t++) {
            LinkedList<Integer> path = new LinkedList<>();
            for (int at = t; at != -1; at = prev[at]) path.addFirst(at);
            System.out.println("0->" + t + " cost " + dist[t] + " via " + path);
        }
        // Prim MST with PQ of int[]
        boolean[] in = new boolean[n];
        PriorityQueue<int[]> pm = new PriorityQueue<>((a, b) -> a[1] != b[1] ? a[1] - b[1] : a[0] - b[0]);
        pm.add(new int[]{0, 0});
        int total = 0;
        List<Integer> orderAdded = new ArrayList<>();
        while (!pm.isEmpty()) {
            int[] cur = pm.poll();
            if (in[cur[0]]) continue;
            in[cur[0]] = true; total += cur[1]; orderAdded.add(cur[0]);
            for (Edge e : adj.get(cur[0])) if (!in[e.to()]) pm.add(new int[]{e.to(), e.w()});
        }
        System.out.println("MST weight " + total + " order " + orderAdded);
        // topological sort (Kahn) with PQ for lexicographic order
        int[][] deps = {{5, 2}, {5, 0}, {4, 0}, {4, 1}, {2, 3}, {3, 1}};
        Map<Integer, List<Integer>> g = new HashMap<>();
        int[] indeg = new int[6];
        for (int[] d : deps) { g.computeIfAbsent(d[0], k -> new ArrayList<>()).add(d[1]); indeg[d[1]]++; }
        PriorityQueue<Integer> ready = new PriorityQueue<>();
        for (int i = 0; i < 6; i++) if (indeg[i] == 0) ready.add(i);
        List<Integer> topo = new ArrayList<>();
        while (!ready.isEmpty()) { int x = ready.poll(); topo.add(x); for (int y : g.getOrDefault(x, List.of())) if (--indeg[y] == 0) ready.add(y); }
        System.out.println("topo " + topo);
    }
}
