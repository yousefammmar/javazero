import java.util.*;
public class Main {
    static void attempt(String what, Runnable r) {
        try { r.run(); System.out.println(what + ": ok"); }
        catch (UnsupportedOperationException e) { System.out.println(what + ": UOE"); }
        catch (NullPointerException e) { System.out.println(what + ": NPE"); }
        catch (IllegalArgumentException e) { System.out.println(what + ": IAE"); }
    }
    public static void main(String[] args) {
        List<Integer> l = List.of(3, 1, 2);
        attempt("List.of add", () -> l.add(4));
        attempt("List.of set", () -> l.set(0, 9));
        attempt("List.of remove", () -> l.remove(0));
        attempt("List.of sort", () -> l.sort(null));
        attempt("List.of clear", () -> l.clear());
        attempt("List.of contains(null)", () -> l.contains(null));
        attempt("List.of with null", () -> List.of(1, null));
        System.out.println(l + " " + l.size() + " " + l.get(1) + " " + l.indexOf(2));
        Set<String> s = Set.of("b", "a", "c");
        attempt("Set.of add", () -> s.add("d"));
        attempt("Set.of remove", () -> s.remove("a"));
        attempt("Set.of dup", () -> Set.of("a", "a"));
        System.out.println(new TreeSet<>(s) + " " + s.contains("a") + " " + s.size());
        Map<String, Integer> m = Map.of("one", 1, "two", 2, "three", 3);
        attempt("Map.of put", () -> m.put("four", 4));
        attempt("Map.of remove", () -> m.remove("one"));
        attempt("Map.of dup key", () -> Map.of("a", 1, "a", 2));
        System.out.println(new TreeMap<>(m) + " " + m.get("two") + " " + m.getOrDefault("x", 0));
        attempt("copyOf", () -> List.copyOf(new ArrayList<>(List.of(1, 2))).add(3));
        attempt("unmodifiableList", () -> Collections.unmodifiableList(new ArrayList<>(l)).add(1));
        attempt("unmodifiableMap", () -> Collections.unmodifiableMap(new HashMap<String, Integer>()).put("a", 1));
        attempt("unmodifiableSet", () -> Collections.unmodifiableSet(new HashSet<Integer>()).add(1));
        attempt("Arrays.asList set", () -> Arrays.asList(1, 2).set(0, 5));
        attempt("Arrays.asList add", () -> Arrays.asList(1, 2).add(5));
        attempt("emptyList", () -> Collections.emptyList().add(null));
        attempt("nCopies", () -> Collections.nCopies(2, 1).set(0, 1));
        attempt("singletonList", () -> Collections.singletonList(1).add(2));
        attempt("stream toList", () -> l.stream().toList().add(1));
        List<Integer> sorted = new ArrayList<>(l);
        Collections.sort(sorted);
        System.out.println(sorted);
        List<Integer> backing = new ArrayList<>(List.of(1, 2));
        List<Integer> view = Collections.unmodifiableList(backing);
        backing.add(3);
        System.out.println(view);
        List<Integer> copy = List.copyOf(backing);
        backing.add(4);
        System.out.println(copy + " " + backing);
        System.out.println(List.of().isEmpty() + " " + List.of(1, 2).equals(Arrays.asList(1, 2)) + " " + Map.of().size());
    }
}
