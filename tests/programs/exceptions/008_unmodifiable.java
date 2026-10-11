import java.util.*;
public class Main {
    static void t(String label, Runnable r) {
        try { r.run(); System.out.println(label + ": ok"); }
        catch (Exception e) { System.out.println(label + ": " + e.getClass().getName() + " msg=" + e.getMessage()); }
    }
    public static void main(String[] args) {
        List<Integer> a = List.of(1, 2, 3);
        t("List.of add", () -> a.add(4));
        t("List.of set", () -> a.set(0, 9));
        t("List.of remove", () -> a.remove(0));
        t("List.of clear", () -> a.clear());
        t("List.of get", () -> a.get(3));
        List<Integer> b = Arrays.asList(1, 2, 3);
        t("asList add", () -> b.add(4));
        t("asList remove", () -> b.remove(0));
        t("asList set", () -> b.set(0, 5));
        t("asList get", () -> b.get(-1));
        List<Integer> c = Collections.unmodifiableList(new ArrayList<>(List.of(1, 2)));
        t("unmod add", () -> c.add(4));
        t("unmod set", () -> c.set(0, 4));
        t("unmod clear", () -> c.clear());
        Map<String, Integer> m = Map.of("a", 1);
        t("Map.of put", () -> m.put("b", 2));
        t("Map.of remove", () -> m.remove("a"));
        Set<Integer> s = Set.of(1, 2);
        t("Set.of add", () -> s.add(3));
        Map<String, Integer> um = Collections.unmodifiableMap(new HashMap<>());
        t("unmapped put", () -> um.put("x", 1));
        t("List.of null", () -> List.of(1, null));
        t("Set.of dup", () -> Set.of(1, 1));
        t("Map.of dup", () -> Map.of("a", 1, "a", 2));
        t("emptyList add", () -> Collections.emptyList().add(null));
        t("copyOf", () -> List.copyOf(Arrays.asList(1, 2)).add(3));
        t("stream toList", () -> java.util.stream.Stream.of(1).toList().add(2));
        t("iterator remove", () -> { Iterator<Integer> it = a.iterator(); it.next(); it.remove(); });
        System.out.println(b + " " + c);
    }
}
