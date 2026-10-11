import java.util.*;

public class Main {
    enum Planet {
        MERCURY(3.303e+23, 2.4397e6), EARTH(5.976e+24, 6.37814e6), JUPITER(1.9e+27, 7.1492e7);
        private final double mass, radius;
        static final double G = 6.67300E-11;
        Planet(double m, double r) { mass = m; radius = r; }
        double gravity() { return G * mass / (radius * radius); }
        static Planet heaviest() {
            Planet best = null;
            for (Planet p : values()) if (best == null || p.mass > best.mass) best = p;
            return best;
        }
    }
    enum Day { MON, TUE, WED, THU, FRI, SAT, SUN;
        boolean weekend() { return this == SAT || this == SUN; }
        Day next() { return values()[(ordinal() + 1) % values().length]; }
    }
    enum Level { LOW(1), MID(5), HIGH(10);
        final int w;
        Level(int w) { this.w = w; System.out.println("init " + name() + " w=" + w); }
        @Override public String toString() { return name().charAt(0) + name().substring(1).toLowerCase() + "(" + w + ")"; }
    }
    static String kind(Day d) {
        switch (d) {
            case SAT: case SUN: return "rest";
            case FRI: return "almost";
            default: return "work";
        }
    }
    public static void main(String[] args) {
        System.out.println("start");
        for (Planet p : Planet.values()) System.out.printf("%s %.2f%n", p, p.gravity());
        System.out.println(Planet.heaviest());
        for (Day d : Day.values()) System.out.println(d + " " + d.ordinal() + " " + d.weekend() + " " + kind(d) + " next=" + d.next());
        System.out.println(Level.MID + " " + Level.valueOf("HIGH").w + " " + Level.HIGH.name());
        System.out.println(Day.MON.compareTo(Day.FRI) + " " + Day.SUN.compareTo(Day.MON) + " " + Day.TUE.equals(Day.TUE));
        try { Day.valueOf("XYZ"); } catch (IllegalArgumentException e) { System.out.println("IAE " + e.getMessage()); }
        try { Day.valueOf("mon"); } catch (IllegalArgumentException e) { System.out.println("IAE case-sensitive"); }
        System.out.println(Day.WED.getDeclaringClass().getSimpleName() + " " + (Day.values() != Day.values()));
        EnumMap<Day, Integer> hours = new EnumMap<>(Day.class);
        hours.put(Day.FRI, 6); hours.put(Day.MON, 8); hours.put(Day.WED, 7);
        System.out.println(hours);
        EnumSet<Day> we = EnumSet.of(Day.SUN, Day.SAT);
        System.out.println(we + " " + EnumSet.complementOf(we) + " " + EnumSet.range(Day.TUE, Day.THU));
        Map<Day, String> hm = new HashMap<>();
        hm.put(Day.MON, "m"); hm.put(Day.MON, "mm");
        System.out.println(hm.size() + hm.get(Day.MON));
        Day d = Day.THU;
        String s = switch (d) { case MON, TUE -> "early"; case WED, THU -> "mid"; default -> "late"; };
        System.out.println(s);
    }
}
