import java.util.*;

public class Main {
    interface Callback { void done(String result); }
    interface Handler { boolean handle(int req); }
    static class Worker {
        private final String name; private final List<String> log = new ArrayList<>();
        Worker(String n) { name = n; }
        void doWork(int n, Callback cb) {
            int r = 1; for (int i = 2; i <= n; i++) r *= i;
            log.add("work" + n);
            cb.done(name + " computed " + r);
        }
        void registerSelf(Registry r) { r.register(this::receive); r.register(new Handler() { public boolean handle(int q) { return receive(q * 2); } }); }
        boolean receive(int q) { log.add("recv" + q); return q % 2 == 0; }
        List<String> log() { return log; }
    }
    static class Registry {
        final List<Handler> hs = new ArrayList<>();
        void register(Handler h) { hs.add(h); }
        int dispatch(int req) { int ok = 0; for (Handler h : hs) if (h.handle(req)) ok++; return ok; }
    }
    static abstract class Chain {
        Chain next;
        Chain link(Chain n) { next = n; return n; }
        String process(int v) { String r = mine(v); return r != null ? r : next != null ? next.process(v) : "unhandled " + v; }
        abstract String mine(int v);
    }
    public static void main(String[] args) {
        Worker w = new Worker("W1");
        List<String> results = new ArrayList<>();
        w.doWork(5, r -> results.add(r));
        w.doWork(3, results::add);
        w.doWork(1, new Callback() { public void done(String r) { results.add("anon:" + r); } });
        System.out.println(results);
        System.out.println(w.log());
        Registry reg = new Registry();
        w.registerSelf(reg);
        reg.register(q -> q > 5);
        System.out.println(reg.dispatch(2) + " " + reg.dispatch(3) + " " + reg.dispatch(8));
        System.out.println(w.log());
        Chain c1 = new Chain() { String mine(int v) { return v < 10 ? "small " + v : null; } };
        Chain c2 = c1.link(new Chain() { String mine(int v) { return v < 100 ? "medium " + v : null; } });
        c2.link(new Chain() { String mine(int v) { return v % 2 == 0 ? "even-large " + v : null; } });
        for (int v : new int[] { 5, 50, 500, 501 }) System.out.println(c1.process(v));
    }
}
