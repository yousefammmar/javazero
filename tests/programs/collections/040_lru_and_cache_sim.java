import java.util.*;
public class Main {
    static class LRUCache {
        private final int cap;
        private final Map<Integer, Integer> map = new HashMap<>();
        private final LinkedList<Integer> order = new LinkedList<>();
        LRUCache(int cap) { this.cap = cap; }
        int get(int k) {
            if (!map.containsKey(k)) return -1;
            order.remove(Integer.valueOf(k)); order.addFirst(k);
            return map.get(k);
        }
        void put(int k, int v) {
            if (map.containsKey(k)) order.remove(Integer.valueOf(k));
            else if (map.size() == cap) { int old = order.removeLast(); map.remove(old); System.out.println("  evicted " + old); }
            map.put(k, v); order.addFirst(k);
        }
        public String toString() { return order + " " + map; }
    }
    static class LFU {
        Map<Integer, Integer> val = new HashMap<>(), cnt = new HashMap<>();
        Map<Integer, LinkedHashSet<Integer>> byCnt = new HashMap<>();
        int min = 0; final int cap;
        LFU(int c) { cap = c; }
        void touch(int k) {
            int c = cnt.get(k);
            cnt.put(k, c + 1);
            byCnt.get(c).remove(k);
            if (byCnt.get(c).isEmpty()) { byCnt.remove(c); if (min == c) min++; }
            byCnt.computeIfAbsent(c + 1, x -> new LinkedHashSet<>()).add(k);
        }
        int get(int k) { if (!val.containsKey(k)) return -1; touch(k); return val.get(k); }
        void put(int k, int v) {
            if (cap == 0) return;
            if (val.containsKey(k)) { val.put(k, v); touch(k); return; }
            if (val.size() == cap) {
                int ev = byCnt.get(min).iterator().next();
                byCnt.get(min).remove(ev);
                val.remove(ev); cnt.remove(ev);
                System.out.println("  LFU evicted " + ev);
            }
            val.put(k, v); cnt.put(k, 1); min = 1;
            byCnt.computeIfAbsent(1, x -> new LinkedHashSet<>()).add(k);
        }
    }
    public static void main(String[] args) {
        LRUCache c = new LRUCache(3);
        c.put(1, 10); c.put(2, 20); c.put(3, 30);
        System.out.println(c);
        System.out.println(c.get(1) + " " + c);
        c.put(4, 40);
        System.out.println(c);
        System.out.println(c.get(2) + " " + c.get(3) + " " + c);
        c.put(5, 50);
        c.put(3, 33);
        System.out.println(c);
        LFU f = new LFU(2);
        f.put(1, 1); f.put(2, 2);
        System.out.println(f.get(1));
        f.put(3, 3);
        System.out.println(f.get(2) + " " + f.get(3));
        f.put(4, 4);
        System.out.println(f.get(1) + " " + f.get(3) + " " + f.get(4));
        System.out.println(f.byCnt + " " + f.cnt);
        // simulate hits/misses
        int[] refs = {1, 2, 3, 2, 4, 1, 5, 2, 1, 2, 3, 4, 5};
        for (int cap = 2; cap <= 4; cap++) {
            Deque<Integer> cache = new ArrayDeque<>();
            int hits = 0;
            for (int r : refs) {
                if (cache.remove(r)) hits++;
                else if (cache.size() == cap) cache.removeLast();
                cache.addFirst(r);
            }
            System.out.println("cap " + cap + " hits " + hits + " final " + cache);
        }
    }
}
