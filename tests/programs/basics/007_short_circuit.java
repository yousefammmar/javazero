public class Main {
    static StringBuilder log = new StringBuilder();
    static boolean t(String n) { log.append(n).append('T').append(' '); return true; }
    static boolean f(String n) { log.append(n).append('F').append(' '); return false; }
    static void show(String label, boolean v) {
        System.out.println(label + " => " + v + " | " + log.toString().trim());
        log.setLength(0);
    }
    public static void main(String[] args) {
        show("f && t", f("a") && t("b"));
        show("t && f", t("a") && f("b"));
        show("t || f", t("a") || f("b"));
        show("f || t", f("a") || t("b"));
        show("f & t", f("a") & t("b"));
        show("t | f", t("a") | f("b"));
        show("t ^ t", t("a") ^ t("b"));
        show("t ^ f", t("a") ^ f("b"));
        show("f || f || t", f("a") || f("b") || t("c"));
        show("t && t && f", t("a") && t("b") && f("c"));
        show("f && t || t", f("a") && t("b") || t("c"));
        show("t || f && f", t("a") || f("b") && f("c"));
        show("(t || f) && f", (t("a") || f("b")) && f("c"));
        show("!f && !t", !f("a") && !t("b"));
        show("f | t & f", f("a") | t("b") & f("c"));
        show("t ^ t & f", t("a") ^ t("b") & f("c"));
        show("t ? f : t", t("a") ? f("b") : t("c"));
        show("f ? f : t", f("a") ? f("b") : t("c"));
        boolean x = true, y = false;
        x &= y;
        System.out.println(x);
        x |= true;
        System.out.println(x);
        x ^= true;
        System.out.println(x);
        y ^= y;
        System.out.println(y);
        System.out.println(true == false);
        System.out.println(true != false);
        System.out.println((true ^ true) == false);
        System.out.println(!true == false);
        int n = 0;
        boolean r = n != 0 && 10 / n > 1;
        System.out.println(r);
        r = n == 0 || 10 / n > 1;
        System.out.println(r);
        String s = null;
        System.out.println(s != null && s.length() > 0);
        System.out.println(s == null || s.isEmpty());
        try {
            System.out.println(s != null & s.length() > 0);
        } catch (NullPointerException e) {
            System.out.println("NPE");
        }
        try {
            System.out.println(n != 0 & 10 / n > 1);
        } catch (ArithmeticException e) {
            System.out.println("AE");
        }
        int a = 5, b = 10;
        System.out.println(a < b && b < 20);
        System.out.println(a > b || b > 20);
        System.out.println(a < b == b > a);
        System.out.println(a == 5 ? "five" : a == 6 ? "six" : "other");
        boolean[] flags = new boolean[2];
        System.out.println(flags[0] + " " + flags[1]);
    }
}
