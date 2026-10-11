import java.util.*;

public class Main {
    enum State {
        IDLE, RUNNING, PAUSED, DONE;
        State on(String ev) {
            switch (this) {
                case IDLE: return ev.equals("start") ? RUNNING : this;
                case RUNNING:
                    if (ev.equals("pause")) return PAUSED;
                    if (ev.equals("finish")) return DONE;
                    return this;
                case PAUSED:
                    return switch (ev) { case "resume" -> RUNNING; case "stop" -> DONE; default -> this; };
                default: return this;
            }
        }
        boolean terminal() { return this == DONE; }
    }
    enum Light {
        RED(3) { Light next() { return GREEN; } },
        GREEN(2) { Light next() { return YELLOW; } },
        YELLOW(1) { Light next() { return RED; } };
        final int secs;
        Light(int s) { secs = s; }
        abstract Light next();
    }
    enum Dir { N(0, -1), E(1, 0), S(0, 1), W(-1, 0);
        final int dx, dy;
        Dir(int dx, int dy) { this.dx = dx; this.dy = dy; }
        Dir right() { return values()[(ordinal() + 1) % 4]; }
        Dir left() { return values()[(ordinal() + 3) % 4]; }
        Dir opposite() { return right().right(); }
    }
    public static void main(String[] args) {
        State s = State.IDLE;
        String[] evs = { "pause", "start", "pause", "noop", "resume", "finish", "start" };
        for (String e : evs) { State n = s.on(e); System.out.println(s + " --" + e + "--> " + n + (n.terminal() ? " (terminal)" : "")); s = n; }
        Light l = Light.RED;
        int t = 0;
        for (int i = 0; i < 7; i++) { t += l.secs; System.out.print(l + "@" + t + " "); l = l.next(); }
        System.out.println();
        int x = 0, y = 0; Dir d = Dir.N;
        String cmds = "FFRFFRFLFFLLF";
        for (char c : cmds.toCharArray()) {
            switch (c) {
                case 'F' -> { x += d.dx; y += d.dy; }
                case 'R' -> d = d.right();
                case 'L' -> d = d.left();
            }
            System.out.print(c + ":" + d + "(" + x + "," + y + ") ");
        }
        System.out.println();
        for (Dir dd : Dir.values()) System.out.println(dd + " opp " + dd.opposite() + " L " + dd.left());
        Map<State, List<String>> hist = new EnumMap<>(State.class);
        State cur = State.IDLE;
        for (String e : evs) { cur = cur.on(e); hist.computeIfAbsent(cur, k -> new ArrayList<>()).add(e); }
        System.out.println(hist);
    }
}
