import java.util.*;
public class Main {
    public static void main(String[] args) {
        TreeMap<String, Integer> ci = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
        ci.put("Banana", 1); ci.put("apple", 2); ci.put("Cherry", 3); ci.put("APPLE", 4); ci.put("banana", 5);
        System.out.println(ci);
        System.out.println(ci.get("CHERRY") + " " + ci.containsKey("BANANA"));
        TreeMap<String, Integer> cs = new TreeMap<>();
        cs.put("Banana", 1); cs.put("apple", 2); cs.put("Cherry", 3); cs.put("APPLE", 4);
        System.out.println(cs);
        TreeMap<Integer, String> rev = new TreeMap<>(Comparator.reverseOrder());
        for (int i = 1; i <= 6; i++) rev.put(i * 3, "x" + i);
        System.out.println(rev + " first " + rev.firstKey() + " floor(10)=" + rev.floorKey(10) + " ceiling(10)=" + rev.ceilingKey(10));
        TreeMap<String, String> byLen = new TreeMap<>(Comparator.comparingInt(String::length).thenComparing(Comparator.reverseOrder()));
        for (String s : "a bb cc ddd e fff gggg".split(" ")) byLen.put(s, s.toUpperCase());
        System.out.println(byLen);
        TreeSet<String> ts = new TreeSet<>((x, y) -> y.compareTo(x));
        ts.addAll(List.of("mango", "kiwi", "apple", "zebra"));
        System.out.println(ts + " first " + ts.first());
        TreeSet<Integer> mod = new TreeSet<>(Comparator.comparingInt((Integer x) -> x % 10).thenComparing(x -> x));
        for (int x : new int[]{15, 21, 33, 5, 11, 40, 100, 8}) mod.add(x);
        System.out.println(mod);
        TreeSet<String> nat = new TreeSet<>(List.of("b", "B", "a", "A", "10", "9", "_"));
        System.out.println(nat);
        Map<String, Integer> pairs = new TreeMap<>(Comparator.nullsFirst(Comparator.naturalOrder()));
        pairs.put("b", 1); pairs.put(null, 0); pairs.put("a", 2);
        System.out.println(pairs);
    }
}
