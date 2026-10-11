import java.util.*;

public class Main {
    static class Config {
        private static Config instance;
        private static int created = 0;
        private final Map<String, String> data = new TreeMap<>();
        private Config() { created++; System.out.println("Config created #" + created); }
        static Config get() { if (instance == null) instance = new Config(); return instance; }
        void set(String k, String v) { data.put(k, v); }
        String get(String k) { return data.getOrDefault(k, "<none>"); }
        public String toString() { return data.toString(); }
    }
    enum Registry {
        INSTANCE;
        private int hits;
        int hit() { return ++hits; }
    }
    interface Product { String info(); }
    static class Pen implements Product { public String info() { return "Pen"; } }
    static class Book implements Product {
        final int pages; Book(int p) { pages = p; }
        public String info() { return "Book/" + pages; }
    }
    static class Factory {
        static Product create(String kind) {
            switch (kind) {
                case "pen": return new Pen();
                case "book": return new Book(100);
                case "bigbook": return new Book(1000);
                default: throw new IllegalArgumentException("unknown product: " + kind);
            }
        }
    }
    public static void main(String[] args) {
        Config a = Config.get();
        Config b = Config.get();
        System.out.println(a == b);
        a.set("mode", "fast"); b.set("level", "3");
        System.out.println(Config.get() + " " + a.get("mode") + " " + b.get("nope"));
        System.out.println(Registry.INSTANCE.hit() + " " + Registry.INSTANCE.hit() + " " + (Registry.valueOf("INSTANCE") == Registry.INSTANCE));
        for (String k : new String[] { "pen", "book", "bigbook", "cup" }) {
            try { System.out.println(k + " -> " + Factory.create(k).info()); }
            catch (IllegalArgumentException e) { System.out.println("error: " + e.getMessage()); }
        }
        List<Product> ps = new ArrayList<>();
        for (String k : List.of("pen", "pen", "book")) ps.add(Factory.create(k));
        System.out.println(ps.get(0) == ps.get(1));
        System.out.println(ps.get(0).info().equals(ps.get(1).info()));
        System.out.println(Config.created);
    }
}
