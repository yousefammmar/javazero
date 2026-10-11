import java.util.*;
public class Main {
    public static void main(String[] args) {
        Map<String, Integer> m = new HashMap<>();
        m.put("alpha", 1); m.put("beta", 2); m.put("gamma", 3); m.put("delta", 4);
        for (Map.Entry<String, Integer> e : m.entrySet()) e.setValue(e.getValue() * 100);
        System.out.println("setValue write-through: " + m);
        Map.Entry<String, Integer> first = m.entrySet().iterator().next();
        System.out.println(first.getKey() + " " + first.getValue() + " " + first);
        Map.Entry<String, Integer> fixed = Map.entry("k", 5);
        System.out.println(fixed + " " + fixed.getKey() + " " + fixed.getValue());
        try { fixed.setValue(6); } catch (UnsupportedOperationException e) { System.out.println("UOE entry"); }
        Map.Entry<String, Integer> simple = new AbstractMap.SimpleEntry<>("s", 1);
        simple.setValue(2);
        System.out.println(simple + " equals " + simple.equals(Map.entry("s", 2)) + " hash " + (simple.hashCode() == Map.entry("s", 2).hashCode()));
        List<Map.Entry<String, Integer>> entries = new ArrayList<>(m.entrySet());
        entries.sort(Map.Entry.comparingByValue(Comparator.reverseOrder()));
        System.out.println(entries);
        entries.sort(Map.Entry.comparingByKey());
        System.out.println(entries);
        for (Map.Entry<String, Integer> e : entries) System.out.print(e.getKey().charAt(0) + "=" + e.getValue() / 100 + " ");
        System.out.println();
        Iterator<Map.Entry<String, Integer>> it = m.entrySet().iterator();
        while (it.hasNext()) if (it.next().getValue() > 250) it.remove();
        System.out.println("after removing >250: " + m);
        m.entrySet().removeIf(e -> e.getKey().equals("alpha"));
        System.out.println(m);
        Map<String, Integer> tm = new TreeMap<>(Map.of("x", 1, "y", 2, "z", 3));
        tm.entrySet().forEach(e -> e.setValue(e.getValue() + 10));
        System.out.println(tm);
        for (var e : tm.entrySet()) System.out.print(e.getKey() + e.getValue() + ";");
        System.out.println();
        Map<String, Integer> cp = new TreeMap<>();
        for (var e : tm.entrySet()) cp.put(e.getKey().toUpperCase(), e.getValue());
        System.out.println(cp);
        Map<String, Integer> fromEntries = Map.ofEntries(Map.entry("only", 1));
        System.out.println(fromEntries);
        Map.Entry<Integer, Integer> be = new AbstractMap.SimpleEntry<>(1, 2);
        System.out.println(be.getKey() + be.getValue());
    }
}
