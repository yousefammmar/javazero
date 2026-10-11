import java.util.*;

public class Main {
    static class LRU<K, V> {
        private class Entry { K k; V v; Entry prev, next; Entry(K k, V v) { this.k = k; this.v = v; } }
        private final Map<K, Entry> map = new HashMap<>();
        private final Entry head = new Entry(null, null), tail = new Entry(null, null);
        private final int cap; int hits, misses;
        LRU(int cap) { this.cap = cap; head.next = tail; tail.prev = head; }
        private void unlink(Entry e) { e.prev.next = e.next; e.next.prev = e.prev; }
        private void pushFront(Entry e) { e.next = head.next; e.prev = head; head.next.prev = e; head.next = e; }
        V get(K k) {
            Entry e = map.get(k);
            if (e == null) { misses++; return null; }
            hits++; unlink(e); pushFront(e); return e.v;
        }
        void put(K k, V v) {
            Entry e = map.get(k);
            if (e != null) { e.v = v; unlink(e); pushFront(e); return; }
            if (map.size() == cap) { Entry last = tail.prev; unlink(last); map.remove(last.k); System.out.println("  evict " + last.k); }
            e = new Entry(k, v); map.put(k, e); pushFront(e);
        }
        public String toString() {
            StringBuilder sb = new StringBuilder("{");
            for (Entry e = head.next; e != tail; e = e.next) { if (sb.length() > 1) sb.append(", "); sb.append(e.k).append('=').append(e.v); }
            return sb.append('}').toString();
        }
    }
    public static void main(String[] args) {
        LRU<String, Integer> c = new LRU<>(3);
        c.put("a", 1); c.put("b", 2); c.put("c", 3);
        System.out.println(c);
        System.out.println(c.get("a") + " " + c);
        c.put("d", 4);
        System.out.println(c);
        System.out.println(c.get("b") + " " + c.get("c"));
        c.put("a", 10); c.put("e", 5);
        System.out.println(c);
        System.out.println(c.hits + "/" + c.misses);
        LRU<Integer, String> n = new LRU<>(2);
        for (int i = 0; i < 6; i++) { n.put(i % 3, "v" + i); System.out.println(n); }
        LRU<Integer, long[]> fibs = new LRU<>(5);
        long a = 0, b = 1;
        for (int i = 0; i < 8; i++) { fibs.put(i, new long[] { a }); long t = a + b; a = b; b = t; }
        System.out.println(fibs.get(7)[0] + " " + fibs.get(1) + " " + fibs.get(5)[0]);
    }
}
