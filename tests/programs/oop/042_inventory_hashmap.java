import java.util.*;

public class Main {
    static final class Sku {
        private final String code; private final int variant;
        Sku(String c, int v) { code = c; variant = v; }
        @Override public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Sku)) return false;
            Sku s = (Sku) o;
            return variant == s.variant && code.equals(s.code);
        }
        @Override public int hashCode() { int h = 17; h = 31 * h + code.hashCode(); h = 31 * h + variant; return h; }
        @Override public String toString() { return code + "-" + variant; }
    }
    static class Item {
        final Sku sku; int qty; final double price;
        Item(Sku s, int q, double p) { sku = s; qty = q; price = p; }
        public String toString() { return sku + " x" + qty + " @" + price; }
    }
    static class Inventory {
        private final Map<Sku, Item> items = new LinkedHashMap<>();
        void add(String code, int variant, int qty, double price) {
            Sku k = new Sku(code, variant);
            Item it = items.get(k);
            if (it == null) items.put(k, new Item(k, qty, price)); else it.qty += qty;
        }
        boolean remove(String code, int variant, int qty) {
            Item it = items.get(new Sku(code, variant));
            if (it == null || it.qty < qty) return false;
            it.qty -= qty;
            if (it.qty == 0) items.remove(it.sku);
            return true;
        }
        double value() { double v = 0; for (Item i : items.values()) v += i.qty * i.price; return v; }
        public String toString() { return items.values().toString(); }
    }
    public static void main(String[] args) {
        Inventory inv = new Inventory();
        inv.add("shirt", 1, 10, 9.5); inv.add("shirt", 2, 5, 9.5); inv.add("hat", 1, 3, 12.0);
        inv.add("shirt", 1, 5, 9.5);
        System.out.println(inv);
        System.out.println(inv.value());
        System.out.println(inv.remove("hat", 1, 4) + " " + inv.remove("hat", 1, 3) + " " + inv.remove("shoe", 1, 1) + " " + inv.remove("shirt", 2, 2));
        System.out.println(inv + " " + inv.value());
        Map<Sku, Integer> counts = new HashMap<>();
        String[] codes = { "a", "b", "a", "c", "b", "a" };
        for (int i = 0; i < codes.length; i++) counts.merge(new Sku(codes[i], i % 2), 1, Integer::sum);
        List<String> out = new ArrayList<>();
        for (Map.Entry<Sku, Integer> e : counts.entrySet()) out.add(e.getKey() + "=" + e.getValue());
        Collections.sort(out);
        System.out.println(out);
        System.out.println(new Sku("a", 1).equals(new Sku("a", 1)) + " " + new Sku("a", 1).hashCode() + " " + new Sku("a", 2).hashCode());
        Set<Sku> seen = new HashSet<>();
        int dup = 0;
        for (int i = 0; i < 20; i++) if (!seen.add(new Sku("k" + (i % 7), i % 3))) dup++;
        System.out.println(seen.size() + " " + dup);
    }
}
