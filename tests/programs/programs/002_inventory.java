import java.util.*;

public class Main {
    static class Item {
        String name; String category; int qty; double price;
        Item(String name, String category, int qty, double price) {
            this.name = name; this.category = category; this.qty = qty; this.price = price;
        }
        double total() { return qty * price; }
        @Override public String toString() {
            return String.format("%-10s %-8s %4d %8.2f %9.2f", name, category, qty, price, total());
        }
    }

    public static void main(String[] args) {
        Map<String, Item> inv = new HashMap<>();
        String[][] raw = {
            {"hammer", "tools", "10", "12.5"}, {"saw", "tools", "4", "18.0"},
            {"apple", "food", "100", "0.4"}, {"bread", "food", "20", "2.25"},
            {"lamp", "home", "7", "22.0"}, {"chair", "home", "7", "45.9"},
            {"nails", "tools", "500", "0.05"}, {"milk", "food", "30", "1.1"}
        };
        for (String[] r : raw) {
            inv.put(r[0], new Item(r[0], r[1], Integer.parseInt(r[2]), Double.parseDouble(r[3])));
        }
        List<Item> items = new ArrayList<>(inv.values());
        items.sort(Comparator.comparing((Item i) -> i.category).thenComparing(i -> -i.qty).thenComparing(i -> i.name));
        System.out.println("By category, qty desc, name:");
        for (Item i : items) System.out.println(i);

        items.sort(Comparator.comparingDouble(Item::total).reversed());
        System.out.println("\nBy total value desc:");
        for (Item i : items) System.out.println(i);

        Map<String, Double> byCat = new TreeMap<>();
        Map<String, Integer> countCat = new TreeMap<>();
        for (Item i : inv.values()) {
            byCat.merge(i.category, i.total(), Double::sum);
            countCat.merge(i.category, 1, Integer::sum);
        }
        System.out.println("\nCategory totals:");
        for (Map.Entry<String, Double> e : byCat.entrySet()) {
            System.out.printf("%-6s items=%d total=%.2f%n", e.getKey(), countCat.get(e.getKey()), e.getValue());
        }

        // restock and sell
        inv.get("saw").qty += 6;
        Item apple = inv.get("apple");
        apple.qty -= 35;
        inv.remove("lamp");
        System.out.println("\nContains lamp? " + inv.containsKey("lamp"));
        System.out.println("Saw now: " + inv.get("saw"));
        System.out.println("Apple now: " + apple);
        List<String> low = new ArrayList<>();
        for (Item i : inv.values()) if (i.qty < 10) low.add(i.name);
        Collections.sort(low);
        System.out.println("Low stock: " + low);
        double grand = 0;
        for (Item i : inv.values()) grand += i.total();
        System.out.printf("Grand total: %.2f%n", grand);
        System.out.println("Missing: " + inv.getOrDefault("ghost", new Item("none", "-", 0, 0)));
    }
}
