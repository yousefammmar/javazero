import java.util.Arrays;

public class Main {
    enum Day { MON, TUE, WED, THU, FRI, SAT, SUN;
        boolean weekend() { return this == SAT || this == SUN; }
        Day next() { return values()[(ordinal() + 1) % values().length]; }
    }
    enum Planet {
        MERCURY(3.303e+23, 2.4397e6), EARTH(5.976e+24, 6.37814e6);
        private final double mass, radius;
        Planet(double m, double r) { mass = m; radius = r; }
        double gravity() { return 6.67300E-11 * mass / (radius * radius); }
    }
    enum Op {
        ADD("+") { int apply(int a, int b) { return a + b; } },
        MUL("*") { int apply(int a, int b) { return a * b; } };
        final String sym;
        Op(String s) { sym = s; }
        abstract int apply(int a, int b);
    }
    record Point(int x, int y) {
        Point { if (x < 0) throw new IllegalArgumentException("neg x"); }
        int dist2() { return x * x + y * y; }
        static Point origin() { return new Point(0, 0); }
    }
    record Pair<A, B>(A first, B second) { }
    record Named(String name, double[] vals) { }
    public static void main(String[] args) {
        for (Day d : Day.values()) System.out.print(d + (d.weekend() ? "*" : "") + " ");
        System.out.println();
        System.out.println(Day.valueOf("WED").ordinal() + " " + Day.SUN.next() + " " + Day.FRI.compareTo(Day.MON) + " " + Day.MON.name().toLowerCase() + " " + Day.TUE.equals(Day.TUE) + " " + (Day.TUE == Day.valueOf("TUE")));
        try { Day.valueOf("XYZ"); } catch (IllegalArgumentException e) { System.out.println(e.getMessage()); }
        for (Planet p : Planet.values()) System.out.printf("%s %.2f%n", p, p.gravity());
        for (Op o : Op.values()) System.out.println(o + " " + o.sym + " " + o.apply(6, 7));
        Point p = new Point(3, 4);
        System.out.println(p + " " + p.x() + " " + p.dist2() + " " + p.equals(new Point(3, 4)) + " " + (p.hashCode() == new Point(3, 4).hashCode()) + " " + Point.origin());
        try { new Point(-1, 0); } catch (IllegalArgumentException e) { System.out.println(e.getMessage()); }
        Pair<String, Integer> pr = new Pair<>("k", 5);
        System.out.println(pr + " " + pr.first() + pr.second());
        System.out.println(new Pair<>(null, 1.5) + " " + new Pair<>(1, 2).equals(new Pair<>(1, 2)) + " " + new Pair<>(1, 2).equals(new Pair<>(2, 1)));
        System.out.println(new Named("n", null).vals() == null);
        java.util.Map<Day, Integer> em = new java.util.EnumMap<>(Day.class);
        em.put(Day.FRI, 5); em.put(Day.MON, 1);
        System.out.println(em);
        Day dd = Day.THU;
        switch (dd) { case MON, TUE -> System.out.println("early"); case THU -> System.out.println("thursday"); default -> System.out.println("other"); }
        System.out.println(Arrays.toString(Day.values()).length() + " " + Day.values().length + " " + Day.valueOf("SAT").weekend());
        int[] counts = new int[Day.values().length];
        for (int i = 0; i < 20; i++) counts[Day.values()[i * 3 % 7].ordinal()]++;
        System.out.println(Arrays.toString(counts));
    }
}
