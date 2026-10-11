import java.util.*;

public class Main {
    static class MyMap<K, V> {
        private static class Entry<K, V> {
            final K key; V value; Entry<K, V> next;
            Entry(K k, V v, Entry<K, V> n) { key = k; value = v; next = n; }
        }
        private Entry<K, V>[] table;
        private int size;
        private int resizes;

        @SuppressWarnings("unchecked")
        MyMap(int cap) { table = (Entry<K, V>[]) new Entry[cap]; }

        private int idx(Object k, int len) { return (k == null ? 0 : (k.hashCode() & 0x7fffffff)) % len; }

        V put(K k, V v) {
            int i = idx(k, table.length);
            for (Entry<K, V> e = table[i]; e != null; e = e.next) {
                if (Objects.equals(e.key, k)) { V old = e.value; e.value = v; return old; }
            }
            table[i] = new Entry<>(k, v, table[i]);
            if (++size > table.length * 3 / 4) resize();
            return null;
        }

        V get(K k) {
            for (Entry<K, V> e = table[idx(k, table.length)]; e != null; e = e.next)
                if (Objects.equals(e.key, k)) return e.value;
            return null;
        }

        boolean remove(K k) {
            int i = idx(k, table.length);
            Entry<K, V> prev = null;
            for (Entry<K, V> e = table[i]; e != null; prev = e, e = e.next) {
                if (Objects.equals(e.key, k)) {
                    if (prev == null) table[i] = e.next; else prev.next = e.next;
                    size--;
                    return true;
                }
            }
            return false;
        }

        @SuppressWarnings("unchecked")
        private void resize() {
            Entry<K, V>[] nt = (Entry<K, V>[]) new Entry[table.length * 2];
            for (Entry<K, V> head : table)
                for (Entry<K, V> e = head; e != null; e = e.next)
                    nt[idx(e.key, nt.length)] = new Entry<>(e.key, e.value, nt[idx(e.key, nt.length)]);
            table = nt;
            resizes++;
        }

        int size() { return size; }
        int capacity() { return table.length; }
        int resizes() { return resizes; }

        int longestChain() {
            int best = 0;
            for (Entry<K, V> head : table) {
                int c = 0;
                for (Entry<K, V> e = head; e != null; e = e.next) c++;
                best = Math.max(best, c);
            }
            return best;
        }
    }

    public static void main(String[] args) {
        MyMap<String, Integer> m = new MyMap<>(4);
        String text = "it was the best of times it was the worst of times it was the age of wisdom";
        for (String w : text.split(" ")) {
            Integer c = m.get(w);
            m.put(w, c == null ? 1 : c + 1);
        }
        for (String w : new String[]{"it", "was", "the", "of", "times", "best", "missing"})
            System.out.println(w + " -> " + m.get(w));
        System.out.println("size=" + m.size() + " cap=" + m.capacity() + " resizes=" + m.resizes());
        System.out.println("remove(best)=" + m.remove("best") + " remove(best)=" + m.remove("best") + " size=" + m.size());
        MyMap<Integer, String> big = new MyMap<>(2);
        for (int i = 0; i < 100; i++) big.put(i * 7, "v" + i);
        System.out.println("big size=" + big.size() + " cap=" + big.capacity() + " resizes=" + big.resizes());
        System.out.println("get(49)=" + big.get(49) + " get(50)=" + big.get(50));
        System.out.println("put again returns old: " + big.put(49, "new") + ", now " + big.get(49));
        System.out.println("null key: " + big.put(null, "nil") + " " + big.get(null));
        System.out.println("longest chain " + big.longestChain());
        System.out.println("hash of 'hello' = " + "hello".hashCode());
    }
}
