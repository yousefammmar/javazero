import java.util.Arrays;

public class Main {
    static void p(String s, String regex) {
        String[] r = s.split(regex);
        System.out.println("'" + s + "' / '" + regex + "' -> " + r.length + " " + Arrays.toString(r));
    }
    static void p(String s, String regex, int lim) {
        String[] r = s.split(regex, lim);
        System.out.println("'" + s + "' / '" + regex + "' / " + lim + " -> " + r.length + " " + Arrays.toString(r));
    }
    public static void main(String[] args) {
        p("a,b,c", ",");
        p("a,b,c,,,", ",");
        p(",a,b", ",");
        p(",,a,,b,,", ",");
        p("", ",");
        p(",", ",");
        p(",,,", ",");
        p("abc", "");
        p("abc", "x");
        p("a b  c   d", " ");
        p("a b  c   d", " +");
        p("a b  c   d", "\\s+");
        p("  lead and trail  ", "\\s+");
        p("a.b.c", ".");
        p("a.b.c", "\\.");
        p("a.b.c", "[.]");
        p("a|b|c", "|");
        p("a|b|c", "\\|");
        p("one1two22three333four", "\\d+");
        p("one1two22three333four", "[0-9]");
        p("2024-01-15", "-");
        p("k1=v1;k2=v2", "[=;]");
        p("camelCaseString", "(?=[A-Z])");
        p("a,b,c,d,e", ",", 3);
        p("a,b,c,d,e", ",", 1);
        p("a,b,c,,,", ",", -1);
        p("a,b,c,,,", ",", 0);
        p("a,b,c,,,", ",", 4);
        p("a,b,c,,,", ",", 10);
        p(",a", ",", 2);
        p("a,,", ",", 2);
        p("abc", "", 2);
        p("abc", "", -1);
        p("a1b2c3", "(?<=\\d)", -1);
        p("hello world", "o");
        p("hello world", "l");
        p("hello world", "ll");
        p("xaxbxcx", "x");
        p("xaxbxcx", "x", -1);
        p("a\tb\nc", "\\s");
        p("a, b ,c", "\\s*,\\s*");
        p("AbcDefGhi", "[A-Z]");
        p("tom;dick:harry", "[;:]");
        p("   ", " ");
        p("   ", "\\s+");
        p(" a", " ");
        p("a:b:c", ":", 2);
        String[] parts = "10 20 30 x".split(" ");
        int sum = 0;
        for (String q : parts) {
            try {
                sum += Integer.parseInt(q);
            } catch (NumberFormatException e) {
                System.out.println("bad: " + e.getMessage());
            }
        }
        System.out.println(sum);
    }
}
