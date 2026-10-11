import java.util.*;
public class Main {
    static void t(String label, Runnable r) {
        try { r.run(); System.out.println(label + ": ok"); }
        catch (Exception e) { System.out.println(label + ": " + e.getClass().getName() + " msg=" + e.getMessage()); }
    }
    static String describe(String s) { return Objects.requireNonNull(s, "s must not be null").toUpperCase(); }
    public static void main(String[] args) {
        String n = null;
        t("requireNonNull", () -> Objects.requireNonNull(n));
        t("requireNonNull msg", () -> Objects.requireNonNull(n, "name is required"));
        t("requireNonNull supplier", () -> Objects.requireNonNull(n, () -> "supplied message"));
        t("requireNonNull ok", () -> System.out.println(Objects.requireNonNull("x")));
        t("describe null", () -> describe(null));
        t("describe ok", () -> System.out.println(describe("abc")));
        t("requireNonNullElse", () -> System.out.println(Objects.requireNonNullElse(n, "default")));
        t("requireNonNullElse both null", () -> Objects.requireNonNullElse(n, null));
        t("checkIndex bad", () -> Objects.checkIndex(5, 3));
        t("checkIndex neg", () -> Objects.checkIndex(-1, 3));
        t("checkIndex ok", () -> System.out.println(Objects.checkIndex(2, 3)));
        t("checkFromToIndex", () -> Objects.checkFromToIndex(2, 9, 5));
        t("checkFromIndexSize", () -> Objects.checkFromIndexSize(2, 9, 5));
        t("Optional.of null", () -> Optional.of(n));
        t("Optional.ofNullable", () -> System.out.println(Optional.ofNullable(n).isPresent()));
        t("List.of null elem", () -> List.of("a", n));
        t("Map.of null val", () -> Map.of("k", n));
        t("ArrayDeque null", () -> new ArrayDeque<String>().add(n));
        t("PriorityQueue null", () -> new PriorityQueue<String>().add(n));
        t("Objects.equals", () -> System.out.println(Objects.equals(n, null)));
        t("Objects.toString", () -> System.out.println(Objects.toString(n, "dflt") + " " + Objects.toString(n)));
        t("Objects.hash", () -> System.out.println(Objects.hash(n, 1) + " " + Objects.hashCode(n)));
        t("String.valueOf(null obj)", () -> System.out.println(String.valueOf((Object) n)));
        t("concat null", () -> System.out.println("a" + n));
        t("Integer.valueOf null", () -> Integer.valueOf((String) n));
    }
}
