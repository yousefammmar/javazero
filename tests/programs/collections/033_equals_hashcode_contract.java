import java.util.*;
public class Main {
    static class Good {
        final String name; final int id;
        Good(String n, int i) { name = n; id = i; }
        @Override public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Good g = (Good) o;
            return id == g.id && name.equals(g.name);
        }
        @Override public int hashCode() { return Objects.hash(name, id); }
        @Override public String toString() { return name + "#" + id; }
    }
    static class OnlyEquals {
        final int v;
        OnlyEquals(int v) { this.v = v; }
        @Override public boolean equals(Object o) { return o instanceof OnlyEquals && ((OnlyEquals) o).v == v; }
        @Override public int hashCode() { return 7; }
        @Override public String toString() { return "OE" + v; }
    }
    static class Mutable {
        int v;
        Mutable(int v) { this.v = v; }
        @Override public boolean equals(Object o) { return o instanceof Mutable && ((Mutable) o).v == v; }
        @Override public int hashCode() { return v; }
        @Override public String toString() { return "M" + v; }
    }
    public static void main(String[] args) {
        Good a = new Good("ann", 1), b = new Good("ann", 1), c = new Good("bob", 2);
        System.out.println(a.equals(b) + " " + (a == b) + " " + a.equals(c) + " " + (a.hashCode() == b.hashCode()) + " " + a.equals(null));
        System.out.println(a.hashCode() + " " + c.hashCode() + " " + Objects.hash(1, 2, 3) + " " + Objects.hash() + " " + Objects.hashCode(null) + " " + Arrays.hashCode(new Object[]{"ann", 1}));
        Set<Good> hs = new HashSet<>(List.of(a, c));
        System.out.println(hs.size() + " " + hs.contains(b) + " " + hs.add(b) + " " + hs);
        List<Good> list = new ArrayList<>(List.of(a, c));
        System.out.println(list.contains(b) + " " + list.indexOf(new Good("bob", 2)) + " " + list.remove(new Good("ann", 1)) + " " + list);
        Map<Good, String> m = new HashMap<>();
        m.put(a, "first"); m.put(b, "second"); m.put(c, "third");
        System.out.println(m + " " + m.size());
        Set<OnlyEquals> oe = new HashSet<>();
        for (int i = 0; i < 20; i++) oe.add(new OnlyEquals(i % 8));
        System.out.println(oe.size() + " " + oe);
        Mutable mu = new Mutable(5);
        Set<Mutable> ms = new HashSet<>();
        ms.add(mu);
        System.out.println(ms.contains(mu));
        mu.v = 6;
        System.out.println("after mutate: contains " + ms.contains(mu) + " contains new M5 " + ms.contains(new Mutable(5)) + " " + ms + " size " + ms.size());
        System.out.println(Objects.equals(null, null) + " " + Objects.equals(a, b) + " " + Objects.equals(a, null));
        System.out.println(Arrays.asList(a, c).equals(Arrays.asList(b, new Good("bob", 2))));
        System.out.println(new Good("x", 1).hashCode() == new Good("x", 1).hashCode());
        System.out.println(Objects.hash("a") + " " + Objects.hash("a", "b") + " " + List.of("a", "b").hashCode() + " " + Map.entry("a", "b").hashCode());
        Map<List<Integer>, String> lk = new HashMap<>();
        lk.put(List.of(1, 2), "x"); lk.put(Arrays.asList(1, 2), "y"); lk.put(new ArrayList<>(List.of(2, 1)), "z");
        System.out.println(lk.size() + " " + lk.get(new LinkedList<>(List.of(1, 2))));
    }
}
