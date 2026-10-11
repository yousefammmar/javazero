public class Main {
    static void t(String label, java.util.function.Supplier<Object> s) {
        try { System.out.println(label + " = " + s.get()); }
        catch (Exception e) { System.out.println(label + " -> " + e.getClass().getName() + ": " + e.getMessage()); }
    }
    public static void main(String[] args) {
        int max = Integer.MAX_VALUE, min = Integer.MIN_VALUE;
        long lmax = Long.MAX_VALUE, lmin = Long.MIN_VALUE;
        t("addExact ok", () -> Math.addExact(1, 2));
        t("addExact int", () -> Math.addExact(max, 1));
        t("addExact neg", () -> Math.addExact(min, -1));
        t("addExact long", () -> Math.addExact(lmax, 1L));
        t("subtractExact", () -> Math.subtractExact(min, 1));
        t("subtractExact long", () -> Math.subtractExact(lmin, 1L));
        t("multiplyExact", () -> Math.multiplyExact(65536, 65536));
        t("multiplyExact ok", () -> Math.multiplyExact(46340, 46340));
        t("multiplyExact long", () -> Math.multiplyExact(lmax, 2L));
        t("multiplyExact long,int", () -> Math.multiplyExact(1L << 40, 1 << 30));
        t("negateExact", () -> Math.negateExact(min));
        t("negateExact long", () -> Math.negateExact(lmin));
        t("incrementExact", () -> Math.incrementExact(max));
        t("decrementExact", () -> Math.decrementExact(min));
        t("toIntExact ok", () -> Math.toIntExact(123L));
        t("toIntExact big", () -> Math.toIntExact(1L << 31));
        t("toIntExact neg", () -> Math.toIntExact(-(1L << 31) - 1));
        t("absExact", () -> Math.absExact(min));
        t("floorDiv exact", () -> Math.floorDiv(min, -1));
        t("divideExact", () -> Math.divideExact(min, -1));
        t("silent wrap", () -> max + 1);
        t("silent mul", () -> 65536 * 65536);
        t("abs min", () -> Math.abs(min));
        t("(int) cast", () -> (int) 3_000_000_000L);
        t("(int) double", () -> (int) 1e20);
        t("(int) NaN", () -> (int) Double.NaN);
        t("Integer.sum", () -> Integer.sum(max, 1));
        t("String->int overflow", () -> Integer.parseInt("2147483648"));
        t("Long wrap", () -> lmax + 1);
    }
}
