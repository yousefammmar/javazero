import java.util.*;
public class Main {
    static void t(String label, Runnable r) {
        try { r.run(); System.out.println(label + ": ok"); }
        catch (Exception e) { System.out.println(label + ": " + e.getClass().getSimpleName() + " msg=" + e.getMessage()); }
    }
    public static void main(String[] args) {
        List<String> l = new ArrayList<>(List.of("a", "b", "c"));
        t("get(3)", () -> l.get(3));
        t("get(-1)", () -> l.get(-1));
        t("set(3)", () -> l.set(3, "x"));
        t("remove(3)", () -> l.remove(3));
        t("remove(-2)", () -> l.remove(-2));
        t("add(5,x)", () -> new ArrayList<>(List.of(1, 2)).add(5, 9));
        t("add(-1,x)", () -> new ArrayList<>(List.of(1, 2)).add(-1, 9));
        t("add(3,x) ok", () -> l.add(3, "d"));
        System.out.println(l);
        t("empty get(0)", () -> new ArrayList<String>().get(0));
        t("linked get(2)", () -> new LinkedList<>(List.of(1, 2)).get(2));
        t("linked add(4)", () -> new LinkedList<>(List.of(1, 2)).add(4, 1));
        t("linked remove(7)", () -> new LinkedList<>(List.of(1, 2)).remove(7));
        t("linked set", () -> new LinkedList<>(List.of(1, 2)).set(-1, 1));
        t("subList(0,9)", () -> l.subList(0, 9));
        t("subList(3,1)", () -> l.subList(3, 1));
        t("subList(-1,1)", () -> l.subList(-1, 1));
        t("addAll(9,..)", () -> l.addAll(9, List.of("q")));
        t("listIterator(9)", () -> l.listIterator(9));
        t("vector get", () -> new Vector<>(List.of(1)).get(4));
        t("Arrays.asList get", () -> Arrays.asList(1, 2).get(2));
        t("List.of get", () -> List.of(1, 2).get(2));
        t("List.of empty get", () -> List.of().get(0));
        t("ArrayList cap", () -> new ArrayList<Integer>(-5));
        t("ArrayDeque? no idx", () -> new ArrayList<>(List.of(1)).get(Integer.MAX_VALUE));
        t("remove object", () -> System.out.println(l.remove("zzz")));
        t("Collections.swap", () -> Collections.swap(l, 0, 10));
        t("String.valueOf char[]", () -> String.valueOf(new char[2], 1, 5));
    }
}
