import java.util.*;
public class Main {
    enum Color { RED, GREEN, BLUE }
    enum Level { LOW(1), HIGH(9); final int v; Level(int v) { this.v = v; } }
    static void t(String label, Runnable r) {
        try { r.run(); System.out.println(label + ": ok"); }
        catch (Exception e) { System.out.println(label + ": " + e.getClass().getName() + " msg=" + e.getMessage()); }
    }
    public static void main(String[] args) {
        t("Color.valueOf RED", () -> System.out.println(Color.valueOf("RED")));
        t("Color.valueOf PURPLE", () -> Color.valueOf("PURPLE"));
        t("Color.valueOf lowercase", () -> Color.valueOf("red"));
        t("Color.valueOf empty", () -> Color.valueOf(""));
        t("Color.valueOf null", () -> Color.valueOf(null));
        t("Level.valueOf", () -> Level.valueOf("MEDIUM"));
        t("Enum.valueOf", () -> Enum.valueOf(Level.class, "NOPE"));
        List<Color> cs = new ArrayList<>(List.of(Color.values()));
        for (Iterator<Color> it = cs.iterator(); it.hasNext();) { if (it.next() == Color.GREEN) it.remove(); }
        System.out.println(cs);
        Map<String, Integer> m = new TreeMap<>(Map.of("a", 1, "b", 2, "c", 3));
        for (Iterator<Map.Entry<String, Integer>> it = m.entrySet().iterator(); it.hasNext();) { if (it.next().getValue() == 2) it.remove(); }
        System.out.println(m);
        t("ordinal", () -> System.out.println(Color.BLUE.ordinal() + " " + Color.BLUE.name() + " " + Color.BLUE.compareTo(Color.RED)));
        t("values index", () -> System.out.println(Color.values()[3]));
        t("Level getDeclaringClass", () -> System.out.println(Level.HIGH.getDeclaringClass().getName()));
    }
}
