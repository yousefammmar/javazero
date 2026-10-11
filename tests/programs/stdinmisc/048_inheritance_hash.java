import java.util.*;
public class Main {
    static class Pt {
        final int x, y;
        Pt(int x, int y) { this.x = x; this.y = y; }
        @Override public boolean equals(Object o) { return o instanceof Pt p && p.x == x && p.y == y; }
        @Override public int hashCode() { return Objects.hash(x, y); }
        @Override public String toString() { return "(" + x + "," + y + ")"; }
    }
    public static void main(String[] args) {
        Set<Pt> set = new HashSet<>();
        for (int i = 0; i < 20; i++) set.add(new Pt(i % 5, i % 3));
        System.out.println(set.size() + " " + set.contains(new Pt(2, 2)) + " " + new Pt(1, 2).hashCode() + " " + new Pt(2, 1).hashCode());
        Map<Pt, String> m = new HashMap<>();
        for (int i = 0; i < 6; i++) m.put(new Pt(i, i * i), "v" + i);
        System.out.println(m);
        System.out.println(set);
        Map<String, Integer> hm = new HashMap<>();
        for (String s : "the quick brown fox jumps over the lazy dog and the cat".split(" ")) hm.merge(s, 1, Integer::sum);
        System.out.println(hm);
        Set<Integer> hs = new HashSet<>(List.of(100, 5, 33, 17, 64, 1000, -3, 16, 48));
        System.out.println(hs);
        Set<String> ss = new HashSet<>(List.of("banana", "apple", "cherry", "date", "elderberry", "fig", "grape"));
        System.out.println(ss);
        Set<Character> cs = new HashSet<>();
        for (char c : "hello world".toCharArray()) cs.add(c);
        System.out.println(cs);
        Set<Double> ds = new HashSet<>(List.of(1.5, 2.5, 0.1, 100.0, -4.25));
        System.out.println(ds);
        Map<Long, Boolean> lm = new HashMap<>();
        for (long l = 1; l < 1L << 40; l *= 37) lm.put(l, l % 2 == 0);
        System.out.println(lm);
        Map<Integer, String> big = new HashMap<>();
        for (int i = 0; i < 30; i++) big.put(i * 7 % 40, "x");
        System.out.println(big.keySet());
        System.out.println(new Object().equals(new Object()) + " " + "x".equals(null));
        enum Color { RED, GREEN, BLUE }
        EnumMap<Color, Integer> em = new EnumMap<>(Color.class);
        em.put(Color.BLUE, 3); em.put(Color.RED, 1);
        System.out.println(em + " " + Color.valueOf("GREEN").ordinal() + " " + Arrays.toString(Color.values()) + " " + Color.BLUE.compareTo(Color.RED));
    }
}
