import java.util.*;
public class Main {
    static <K, V> void show(String label, Map<K, V> m) { System.out.println(label + " " + m.size() + " " + m); }
    public static void main(String[] args) {
        String[] ks = {"alpha", "bravo", "charlie", "delta", "echo", "foxtrot", "golf", "hotel", "india", "juliet", "kilo", "lima", "mike", "november", "oscar", "papa"};
        Map<String, Integer> def = new HashMap<>();
        Map<String, Integer> c4 = new HashMap<>(4);
        Map<String, Integer> c100 = new HashMap<>(100);
        Map<String, Integer> c1 = new HashMap<>(1, 0.5f);
        for (int i = 0; i < ks.length; i++) { def.put(ks[i], i); c4.put(ks[i], i); c100.put(ks[i], i); c1.put(ks[i], i); }
        show("default", def); show("cap4   ", c4); show("cap100 ", c100); show("cap1   ", c1);
        System.out.println(def.equals(c4) + " " + c4.equals(c100) + " " + def.keySet().equals(c1.keySet()));
        Map<String, Integer> copy = new HashMap<>(def);
        show("copy   ", copy);
        Map<String, Integer> half = new HashMap<>();
        for (int i = 0; i < 8; i++) half.put(ks[i], i);
        Map<String, Integer> target = new HashMap<>();
        for (int i = 8; i < 16; i++) target.put(ks[i], i);
        show("half   ", half); show("target ", target);
        target.putAll(half);
        show("putAll ", target);
        for (int i = 0; i < 16; i += 3) target.remove(ks[i]);
        show("removed", target);
        for (int i = 0; i < 16; i += 3) target.put(ks[i], 100 + i);
        show("re-add ", target);
        List<String> order = new ArrayList<>(target.keySet());
        System.out.println(order);
        target.keySet().retainAll(List.of("alpha", "delta", "golf", "juliet", "mike", "papa"));
        show("retain ", target);
        target.values().removeIf(v -> v > 105);
        show("rmvalue", target);
        Set<String> hs = new HashSet<>(Arrays.asList(ks));
        System.out.println(hs);
        Set<String> hs2 = new HashSet<>(100);
        hs2.addAll(Arrays.asList(ks));
        System.out.println(hs2);
        System.out.println(hs.equals(hs2));
        Map<Integer, Integer> im = new HashMap<>(4);
        for (int i = 0; i < 20; i++) im.put(i * 5, i);
        show("int cap4", im);
        try { new HashMap<String, String>(-1); } catch (IllegalArgumentException e) { System.out.println("IAE negative capacity"); }
    }
}
