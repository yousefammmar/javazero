import java.util.*;

public class Main {
    enum Light {
        GREEN(4, "Go"), YELLOW(1, "Slow down"), RED(3, "Stop");

        private final int duration;
        private final String advice;

        Light(int duration, String advice) { this.duration = duration; this.advice = advice; }

        Light next() {
            return switch (this) {
                case GREEN -> YELLOW;
                case YELLOW -> RED;
                case RED -> GREEN;
            };
        }
        int duration() { return duration; }
        String advice() { return advice; }
    }

    static class Intersection {
        Light ns = Light.GREEN;
        Light ew = Light.RED;
        int nsLeft = ns.duration();
        int cars = 0, passed = 0;
        final int[] queue = new int[2];

        void tick(int t, Random rnd) {
            if (rnd.nextInt(3) == 0) queue[0]++;
            if (rnd.nextInt(3) == 0) queue[1]++;
            if (ns == Light.GREEN && queue[0] > 0) { queue[0]--; passed++; }
            if (ew == Light.GREEN && queue[1] > 0) { queue[1]--; passed++; }
            nsLeft--;
            if (nsLeft == 0) {
                ns = ns.next();
                if (ns == Light.GREEN) ew = Light.RED;
                else if (ns == Light.RED) ew = Light.GREEN;
                else ew = Light.RED;
                nsLeft = ns == Light.RED ? Light.RED.duration() : ns.duration();
                if (ns == Light.RED && ew == Light.GREEN) nsLeft = Light.GREEN.duration() + 0;
            }
            System.out.printf("t=%2d NS=%-6s EW=%-6s queues=%s passed=%d%n", t, ns, ew, Arrays.toString(queue), passed);
        }
    }

    public static void main(String[] args) {
        Light l = Light.RED;
        for (int i = 0; i < 7; i++) {
            System.out.println(l + " for " + l.duration() + "s: " + l.advice());
            l = l.next();
        }
        Intersection x = new Intersection();
        Random rnd = new Random(7);
        for (int t = 1; t <= 20; t++) x.tick(t, rnd);
        System.out.println("Cars passed: " + x.passed + ", still waiting: " + (x.queue[0] + x.queue[1]));
        int cycle = 0;
        for (Light each : Light.values()) cycle += each.duration();
        System.out.println("Cycle length: " + cycle);
        System.out.println(Light.valueOf("RED").name().toLowerCase() + " " + Light.YELLOW.ordinal());
        try {
            Light.valueOf("BLUE");
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }
}
