import java.util.*;

public class Main {
    static class P {
        final int x;
        P(int x) { this.x = x; }
        @Override public boolean equals(Object o) { return o instanceof P p && p.x == x; }
        @Override public int hashCode() { return x; }
        public String toString() { return "P" + x; }
    }
    static class CP extends P {
        final String color;
        CP(int x, String c) { super(x); color = c; }
        @Override public boolean equals(Object o) { return o instanceof CP c && c.x == x && c.color.equals(color); }
        @Override public int hashCode() { return x * 31 + color.hashCode(); }
        public String toString() { return "CP" + x + color; }
    }
    static class BadHash {
        final int v;
        BadHash(int v) { this.v = v; }
        @Override public boolean equals(Object o) { return o instanceof BadHash b && b.v == v; }
        @Override public int hashCode() { return 7; }
        public String toString() { return "BH" + v; }
    }
    static class Mutable {
        int v;
        Mutable(int v) { this.v = v; }
        @Override public boolean equals(Object o) { return o instanceof Mutable m && m.v == v; }
        @Override public int hashCode() { return v; }
        public String toString() { return "M" + v; }
    }
    public static void main(String[] args) {
        P p = new P(1);
        CP cp = new CP(1, "red");
        System.out.println(p.equals(cp) + " " + cp.equals(p) + " (asymmetric)");
        List<P> list = new ArrayList<>(List.of(p));
        System.out.println(list.contains(cp) + " " + List.of(cp).contains(p));
        Set<P> set = new HashSet<>();
        set.add(p); set.add(cp); set.add(new P(1)); set.add(new CP(1, "red"));
        System.out.println(set.size());
        Set<BadHash> bh = new HashSet<>();
        for (int i = 0; i < 20; i++) bh.add(new BadHash(i % 5));
        System.out.println(bh.size() + " " + bh.contains(new BadHash(3)) + " " + bh.contains(new BadHash(9)));
        Mutable m = new Mutable(1);
        Set<Mutable> ms = new HashSet<>();
        ms.add(m);
        System.out.println(ms.contains(m));
        m.v = 2;
        System.out.println(ms.contains(m) + " " + ms.contains(new Mutable(1)) + " " + ms.contains(new Mutable(2)) + " " + ms.size());
        Integer a = 127, b = 127, c = 1000, d = 1000;
        System.out.println((a == b) + " " + (c == d) + " " + c.equals(d));
        Long l = 5L;
        System.out.println(l.equals(5) + " " + l.equals(5L) + " " + (l == 5));
        String s1 = "hi", s2 = new String("hi");
        System.out.println((s1 == s2) + " " + s1.equals(s2) + " " + (s1 == s2.intern()));
        Object o1 = new int[]{1}, o2 = new int[]{1};
        System.out.println(o1.equals(o2) + " " + Arrays.equals((int[]) o1, (int[]) o2));
        System.out.println(List.of(1, 2).equals(Arrays.asList(1, 2)) + " " + new ArrayList<>(List.of(1)).equals(new LinkedList<>(List.of(1))));
        System.out.println(new HashSet<>(List.of(1, 2)).equals(Set.of(2, 1)) + " " + Map.of("a", 1).equals(new HashMap<>(Map.of("a", 1))));
        System.out.println(Double.valueOf(0.0).equals(-0.0) + " " + (0.0 == -0.0) + " " + Double.valueOf(Double.NaN).equals(Double.NaN) + " " + (Double.NaN == Double.NaN));
    }
}
