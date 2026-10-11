import java.util.*;

public class Main {
    static class A { public String toString() { return "A"; } }
    static class B extends A { public String toString() { return "B<" + super.toString() + ">"; } }
    static class Nul { public String toString() { return null; } }
    static class Rec { Rec self; public String toString() { return "Rec" + (self == null ? "" : "(self)"); } }
    static class Temp {
        final double deg;
        Temp(double d) { deg = d; }
        public String toString() { return String.format("%.1f C", deg); }
    }
    enum Color { RED, GREEN; public String toString() { return name().toLowerCase(); } }
    record R(A a, Color c, int[] arr) {}
    public static void main(String[] args) {
        A a = new A(); B b = new B(); A ab = b;
        System.out.println(a + " " + b + " " + ab);
        System.out.println("x" + a + 1 + 2 + b);
        System.out.println(1 + 2 + "x" + 1 + 2);
        System.out.println('a' + 'b' + "c" + 'd' + 'e');
        System.out.println("" + 'a' + 'b' + (char) ('a' + 1) + (int) 'a');
        Object nul = null; String sn = null;
        System.out.println("n=" + nul + " " + sn + " " + String.valueOf(nul) + " " + Objects.toString(sn));
        System.out.println(new Nul() + "|" + String.valueOf(new Nul()) + "|");
        Rec r = new Rec(); System.out.println(r); r.self = r; System.out.println(r);
        System.out.println(new Temp(21.55) + " " + new Temp(-3));
        System.out.println(Color.RED + " " + Color.GREEN.name() + " " + Color.valueOf("RED") + " " + Arrays.toString(Color.values()));
        List<Object> mix = new ArrayList<>(List.of(a, b, Color.GREEN, 1, 2.0, 'c', "s", true, 5L, 1.5f));
        System.out.println(mix);
        System.out.println(Arrays.asList(a, null, b));
        Map<String, Object> m = new LinkedHashMap<>(); m.put("k", a); m.put("n", null); m.put("l", List.of(b));
        System.out.println(m);
        System.out.println(new R(a, Color.RED, null));
        StringBuilder sb = new StringBuilder();
        sb.append(a).append(1).append('c').append(2.5).append(true).append((Object) null).append(new char[]{'x', 'y'}).append(b);
        System.out.println(sb + " " + sb.length());
        System.out.println(String.join("-", "a", "b") + String.valueOf(new char[]{'h', 'i'}) + String.valueOf(3.0) + String.valueOf(true));
        char[] cs = { 'a', 'b' };
        System.out.println(cs);
        System.out.println("cs=" + String.valueOf(cs));
        double d = 1.0 / 3; float f = 1.1f; long big = Long.MAX_VALUE;
        System.out.println(d + " " + f + " " + big + " " + (float) d + " " + 100.0 + " " + 1e10 + " " + 1.0E-5 + " " + (0.1 + 0.2));
    }
}
