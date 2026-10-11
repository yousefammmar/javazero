import java.util.*;
public class Main {
    static class LRU<K, V> extends LinkedHashMap<K, V> {
        private final int cap;
        LRU(int cap) { super(16, 0.75f, true); this.cap = cap; }
        @Override protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
            boolean r = size() > cap;
            if (r) System.out.println("  evict " + eldest.getKey() + "=" + eldest.getValue());
            return r;
        }
    }
    public static void main(String[] args) {
        Map<String, Integer> lhm = new LinkedHashMap<>();
        lhm.put("zeta", 1); lhm.put("alpha", 2); lhm.put("mid", 3); lhm.put("beta", 4);
        System.out.println(lhm);
        lhm.put("zeta", 10);
        lhm.remove("alpha"); lhm.put("alpha", 20);
        System.out.println("reinsert/update: " + lhm);
        System.out.println(lhm.keySet() + " " + lhm.values());
        LRU<String, Integer> cache = new LRU<>(3);
        cache.put("a", 1); cache.put("b", 2); cache.put("c", 3);
        System.out.println(cache);
        cache.get("a");
        System.out.println("after get a: " + cache);
        cache.put("d", 4);
        System.out.println(cache);
        cache.put("c", 33);
        System.out.println("update c: " + cache);
        cache.put("e", 5);
        System.out.println(cache);
        System.out.println(cache.getOrDefault("zz", -1) + " " + cache.containsKey("a") + " " + cache);
        cache.get("c");
        System.out.println(cache);
        cache.putIfAbsent("f", 6);
        System.out.println(cache);
        Map<Integer, String> ao = new LinkedHashMap<>(16, 0.75f, true);
        for (int i = 1; i <= 5; i++) ao.put(i, "n" + i);
        ao.get(2); ao.get(1); ao.get(5);
        System.out.println(ao);
        for (Map.Entry<Integer, String> e : ao.entrySet()) { System.out.print(e.getKey() + " "); break; }
        System.out.println();
        Set<Integer> big = new LinkedHashSet<>();
        for (int i = 30; i > 0; i -= 3) big.add(i * 7 % 50);
        System.out.println(big);
    }
}
