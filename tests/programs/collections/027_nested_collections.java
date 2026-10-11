import java.util.*;
public class Main {
    public static void main(String[] args) {
        Map<String, List<Integer>> scores = new HashMap<>();
        String[] names = {"ann", "bob", "cy", "ann", "bob", "ann", "dee"};
        int[] pts = {90, 70, 85, 80, 75, 100, 60};
        for (int i = 0; i < names.length; i++) scores.computeIfAbsent(names[i], k -> new ArrayList<>()).add(pts[i]);
        System.out.println(scores);
        for (Map.Entry<String, List<Integer>> e : scores.entrySet()) {
            int sum = 0;
            for (int v : e.getValue()) sum += v;
            System.out.printf("%s avg %.2f max %d%n", e.getKey(), (double) sum / e.getValue().size(), Collections.max(e.getValue()));
        }
        List<int[]> pairs = new ArrayList<>();
        for (int i = 0; i < 4; i++) pairs.add(new int[]{i, i * i});
        pairs.sort((p, q) -> q[1] - p[1]);
        for (int[] p : pairs) System.out.print(Arrays.toString(p));
        System.out.println();
        Map<Integer, Set<String>> byLen = new HashMap<>();
        for (String w : "the quick brown fox jumps over the lazy dog again and again".split(" ")) byLen.computeIfAbsent(w.length(), k -> new TreeSet<>()).add(w);
        System.out.println(byLen);
        List<List<Integer>> tri = new ArrayList<>();
        for (int r = 0; r < 6; r++) {
            List<Integer> row = new ArrayList<>();
            for (int c = 0; c <= r; c++) row.add(c == 0 || c == r ? 1 : tri.get(r - 1).get(c - 1) + tri.get(r - 1).get(c));
            tri.add(row);
        }
        System.out.println(tri);
        List<List<String>> grid = new ArrayList<>();
        for (int i = 0; i < 3; i++) { grid.add(new ArrayList<>()); for (int j = 0; j < 3; j++) grid.get(i).add("" + (char) ('a' + i * 3 + j)); }
        System.out.println(grid);
        grid.get(1).set(1, "#");
        for (List<String> row : grid) System.out.println(String.join("", row));
        Map<String, Map<String, Integer>> nested = new TreeMap<>();
        nested.computeIfAbsent("fruit", k -> new TreeMap<>()).put("apple", 3);
        nested.computeIfAbsent("fruit", k -> new TreeMap<>()).merge("apple", 2, Integer::sum);
        nested.computeIfAbsent("veg", k -> new TreeMap<>()).put("kale", 1);
        System.out.println(nested + " " + nested.get("fruit").get("apple"));
        List<List<Integer>> flat = List.of(List.of(1, 2), List.of(), List.of(3));
        int total = 0;
        for (List<Integer> l : flat) for (int x : l) total += x;
        System.out.println(total);
        Map<Integer, List<Integer>> buckets = new HashMap<>();
        for (int i = 1; i <= 20; i++) buckets.computeIfAbsent(i % 4, k -> new LinkedList<>()).add(i);
        System.out.println(buckets);
        List<Map<String, Integer>> rows = new ArrayList<>();
        for (int i = 0; i < 3; i++) { Map<String, Integer> r = new LinkedHashMap<>(); r.put("id", i); r.put("sq", i * i); rows.add(r); }
        System.out.println(rows);
    }
}
