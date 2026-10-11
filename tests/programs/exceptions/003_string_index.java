public class Main {
    static void t(String label, Runnable r) {
        try { r.run(); }
        catch (Exception e) { System.out.println(label + " -> " + e.getClass().getName() + ": " + e.getMessage()); }
    }
    public static void main(String[] args) {
        String s = "hello";
        t("charAt(5)", () -> s.charAt(5));
        t("charAt(-1)", () -> s.charAt(-1));
        t("charAt(7) abc", () -> "abc".charAt(7));
        t("substring(2,10)", () -> s.substring(2, 10));
        t("substring(4,2)", () -> s.substring(4, 2));
        t("substring(-1,2)", () -> s.substring(-1, 2));
        t("substring(6)", () -> s.substring(6));
        t("substring(-2)", () -> s.substring(-2));
        t("empty charAt", () -> "".charAt(0));
        t("subSequence", () -> s.subSequence(3, 9));
        t("codePointAt", () -> s.codePointAt(9));
        t("sb charAt", () -> new StringBuilder("abc").charAt(3));
        t("sb deleteCharAt", () -> new StringBuilder("abc").deleteCharAt(5));
        t("sb insert", () -> new StringBuilder("abc").insert(9, "x"));
        t("sb setCharAt", () -> new StringBuilder("abc").setCharAt(3, 'x'));
        t("sb substring", () -> new StringBuilder("abc").substring(2, 8));
        t("sb delete", () -> new StringBuilder("abc").delete(5, 6));
        t("sb setLength", () -> new StringBuilder("abc").setLength(-1));
        t("toCharArray ok", () -> System.out.println(s.toCharArray().length));
        t("repeat", () -> "x".repeat(-1));
        t("substring ok", () -> System.out.println(s.substring(5).isEmpty()));
        System.out.println(new StringIndexOutOfBoundsException("custom").getMessage());
        System.out.println(new StringIndexOutOfBoundsException(4).getMessage());
        System.out.println(new IndexOutOfBoundsException(9).getMessage());
        System.out.println(new ArrayIndexOutOfBoundsException(3).getMessage());
    }
}
