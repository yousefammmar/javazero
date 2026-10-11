import java.util.*;
import java.util.function.*;

public class Main {
    interface Listener { void onEvent(String name, int value); }
    static class Bus {
        private final List<Listener> ls = new ArrayList<>();
        void subscribe(Listener l) { ls.add(l); }
        boolean unsubscribe(Listener l) { return ls.remove(l); }
        void publish(String n, int v) { for (Listener l : new ArrayList<>(ls)) l.onEvent(n, v); }
        int count() { return ls.size(); }
    }
    static class Logger implements Listener {
        final String prefix; final List<String> log = new ArrayList<>();
        Logger(String p) { prefix = p; }
        public void onEvent(String n, int v) { log.add(prefix + n + "=" + v); }
    }
    static class Summer {
        int sum;
        void add(String n, int v) { sum += v; }
    }
    public static void main(String[] args) {
        Bus bus = new Bus();
        Logger lg = new Logger("L:");
        Summer sm = new Summer();
        int[] counter = { 0 };
        Listener lambda = (n, v) -> counter[0]++;
        Listener anon = new Listener() {
            int calls;
            public void onEvent(String n, int v) {
                calls++;
                System.out.println("anon #" + calls + " got " + n + " " + v);
            }
        };
        Listener once = new Listener() {
            public void onEvent(String n, int v) { System.out.println("once " + n); bus.unsubscribe(this); }
        };
        bus.subscribe(lg); bus.subscribe(sm::add); bus.subscribe(lambda); bus.subscribe(anon); bus.subscribe(once);
        System.out.println("subs=" + bus.count());
        bus.publish("a", 1);
        bus.publish("b", 20);
        System.out.println("subs=" + bus.count());
        System.out.println(bus.unsubscribe(lg) + " " + bus.unsubscribe(lg));
        bus.publish("c", 300);
        System.out.println(lg.log);
        System.out.println(sm.sum + " " + counter[0]);
        Map<String, List<Consumer<Integer>>> topics = new TreeMap<>();
        topics.computeIfAbsent("x", k -> new ArrayList<>()).add(i -> System.out.println("x1 " + i));
        topics.computeIfAbsent("x", k -> new ArrayList<>()).add(i -> System.out.println("x2 " + (i * 2)));
        topics.computeIfAbsent("y", k -> new ArrayList<>()).add(i -> System.out.println("y1 " + (i + 1)));
        for (Map.Entry<String, List<Consumer<Integer>>> e : topics.entrySet())
            for (Consumer<Integer> c : e.getValue()) c.accept(e.getKey().length() + 10);
    }
}
