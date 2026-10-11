public class Main {
    static String kind(String s) {
        switch (s) {
            case "apple": case "pear": return "fruit";
            case "carrot": return "veg";
            case "": return "empty";
            default: return "unknown:" + s;
        }
    }
    public static void main(String[] args) {
        char[] cs = {'h', 'e', 'l', 'l', 'o'};
        System.out.println(String.valueOf(cs) + " " + String.valueOf(cs, 1, 3) + " " + new String(cs, 0, 2) + " " + String.valueOf(3.0) + String.valueOf(true) + String.valueOf('c') + String.valueOf(10L) + String.valueOf((Object) null));
        for (String s : new String[]{"apple", "pear", "carrot", "", "Apple"}) System.out.println(kind(s));
        String tb = """
            Hello,
              "World" \
            continued
            tab\there
            end\
            """;
        System.out.println(tb);
        System.out.println(tb.length());
        String tb2 = """
            a
            b
            """;
        System.out.println(tb2.length() + " " + tb2.lines().count());
        int n = 15;
        String r = n < 0 ? "neg" : n == 0 ? "zero" : n < 10 ? "small" : n < 100 ? "medium" : "large";
        System.out.println(r);
        System.out.println("  trim me ".trim() + "|" + "  strip ".strip() + "|" + " x ".stripLeading() + "|" + " x ".stripTrailing() + "|" + "ab".repeat(3) + "|" + "".isEmpty() + " ".isBlank());
        System.out.println("a,b,,c,,".split(",").length + " " + "a,b,,c,,".split(",", -1).length + " " + java.util.Arrays.toString(" a  b ".split(" ")) + " " + java.util.Arrays.toString("a1b22c".split("\\d+")) + java.util.Arrays.toString("abc".split("")));
        System.out.println("Hello".chars().sum() + " " + "hello".indexOf('l') + " " + "hello".lastIndexOf("l") + " " + "hello".indexOf("z") + " " + "hello".contains("ell") + " " + "Hello".equalsIgnoreCase("hELLO") + " " + "b".compareTo("a") + " " + "apple".compareTo("apricot") + " " + "a".compareTo("abc"));
        System.out.println("hello".replace('l', 'L') + " " + "a.b.c".replace(".", "-") + " " + "a.b.c".replaceAll(".", "x") + " " + "aaa".replaceFirst("a", "b") + " " + "x".concat("y") + " " + "Mississippi".replaceAll("(ss)", "[$1]"));
        System.out.println(String.join("-", "a", "b", "c") + " " + String.join("/", java.util.List.of("x", "y")) + " " + "abc".substring(3) + "|" + "abc".charAt(2) + " " + "Hello".toCharArray().length + " " + "%d items".formatted(3));
        String s1 = "hi", s2 = "hi", s3 = new String("hi");
        System.out.println((s1 == s2) + " " + (s1 == s3) + " " + s1.equals(s3) + " " + (s1 == s3.intern()) + " " + ("h" + "i" == s1));
        System.out.println(1 + 2 + "x" + 1 + 2 + 'c' + (char) 100 + 1.0 + null + true);
        try { "abc".substring(5); } catch (StringIndexOutOfBoundsException e) { System.out.println(e.getMessage()); }
        try { "abc".charAt(-1); } catch (StringIndexOutOfBoundsException e) { System.out.println(e.getMessage()); }
    }
}
