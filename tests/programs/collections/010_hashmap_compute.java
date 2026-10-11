import java.util.*;
public class Main {
    public static void main(String[] args) {
        Map<String, Integer> m = new HashMap<>();
        String[] words = "one two three two one two four".split(" ");
        for (String w : words) m.merge(w, 1, Integer::sum);
        System.out.println("merge counts " + m);
        for (String w : words) m.compute(w, (k, v) -> v == null ? 1 : v + 10);
        System.out.println("compute " + m);
        System.out.println(m.computeIfAbsent("five", k -> k.length()) + " " + m.computeIfAbsent("one", k -> -1));
        System.out.println(m.computeIfPresent("five", (k, v) -> v + 100) + " " + m.computeIfPresent("six", (k, v) -> 0));
        System.out.println(m);
        m.computeIfPresent("five", (k, v) -> null);
        System.out.println("after null-return computeIfPresent: " + m.containsKey("five"));
        m.compute("one", (k, v) -> null);
        System.out.println("compute->null removes: " + m);
        m.merge("two", 5, (a, b) -> null);
        System.out.println("merge->null removes: " + m);
        System.out.println(m.merge("new", 8, Integer::sum) + " " + m.merge("new", 8, Integer::sum));
        Map<Character, List<String>> byInitial = new HashMap<>();
        for (String w : new String[]{"apple", "avocado", "banana", "blueberry", "cherry", "apricot", "kiwi"})
            byInitial.computeIfAbsent(w.charAt(0), c -> new ArrayList<>()).add(w);
        System.out.println(byInitial);
        Map<Integer, Integer> sq = new HashMap<>();
        for (int i = -3; i <= 3; i++) sq.merge(i * i, i, (a, b) -> a * 100 + b);
        System.out.println(sq);
        Map<String, Integer> scores = new HashMap<>();
        scores.put("a", 1); scores.put("b", 2); scores.put("c", 3);
        scores.replaceAll((k, v) -> v * v);
        scores.forEach((k, v) -> System.out.println(k + ":" + v));
        int total = 0;
        for (int v : scores.values()) total += v;
        System.out.println("total " + total);
        Map<String, Integer> fib = new HashMap<>();
        System.out.println(fibm(30, fib) + " memo size " + fib.size());
    }
    static int fibm(int n, Map<String, Integer> memo) {
        if (n < 2) return n;
        String key = "f" + n;
        Integer c = memo.get(key);
        if (c != null) return c;
        int r = fibm(n - 1, memo) + fibm(n - 2, memo);
        memo.put(key, r);
        return r;
    }
}
