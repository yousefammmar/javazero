public class Main {
    public static void main(String[] args) {
        String a = """
            Hello,
              World!
            end""";
        System.out.println(a);
        System.out.println(a.length());
        String b = """
            line1
            line2
            """;
        System.out.print(b);
        System.out.println(b.length() + " " + b.endsWith("\n"));
        String c = """
                indented more
            base
              mid
            """;
        System.out.print(c);
        String d = """
            trailing spaces stripped
            keep escaped\040\040
            tab:\there
            quote: "single" and \"""triple\"""
            backslash: \\ done
            """;
        System.out.print(d.replace(' ', '.'));
        String e = """
            joined \
            on one line \
            here
            """;
        System.out.print(e);
        String f = """
            a\nb
            c\s
            """;
        System.out.println(f.replace("\n", "|").replace(' ', '_'));
        String g = """
            {
              "name": "test",
              "n": 42
            }
            """;
        System.out.print(g);
        System.out.println(g.lines().count());
        String h = """
            """;
        System.out.println("[" + h + "]" + h.length());
        String i = """
            x""";
        System.out.println(i + i.length());
        String j = """
            first

            third (blank above)
            """;
        System.out.print(j.replace("\n", "$\n"));
        int n = 5;
        String k = """
            n=%d s=%s f=%.1f
            """.formatted(n, "str", 2.55);
        System.out.print(k);
        String l = """
            Hello""" + " tail";
        System.out.println(l);
        System.out.println("""
            inline arg""".toUpperCase());
        String m = """
            unicode A and tab\tand "quotes\040""";
        System.out.println(m + "|");
        System.out.println(a.equals("Hello,\n  World!\nend"));
        System.out.println("""
            <p>
                nested
            </p>""".indent(2));
        String lit1 = """
            same""";
        System.out.println(lit1 == "same");
        System.out.println(lit1.equals("same"));
        String o = """
              two
             one
            """;
        System.out.print(o.replace(' ', '.'));
    }
}
