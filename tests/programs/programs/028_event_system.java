import java.util.*;
import java.util.function.*;

public class Main {
    @FunctionalInterface
    interface Listener<E> {
        void handle(E event);
    }

    record UserEvent(String user, String action) {}

    static class EventBus<E> {
        private final Map<String, List<Listener<E>>> handlers = new LinkedHashMap<>();
        private int dispatched;

        void subscribe(String topic, Listener<E> l) {
            handlers.computeIfAbsent(topic, k -> new ArrayList<>()).add(l);
        }

        int publish(String topic, E event) {
            List<Listener<E>> ls = handlers.getOrDefault(topic, Collections.emptyList());
            for (Listener<E> l : ls) { l.handle(event); dispatched++; }
            return ls.size();
        }

        int dispatched() { return dispatched; }
    }

    static int counter = 0;

    public static void main(String[] args) {
        EventBus<UserEvent> bus = new EventBus<>();
        List<String> audit = new ArrayList<>();
        Map<String, Integer> perUser = new TreeMap<>();

        bus.subscribe("login", e -> System.out.println("Welcome, " + e.user() + "!"));
        bus.subscribe("login", e -> audit.add("LOGIN:" + e.user()));
        bus.subscribe("login", e -> perUser.merge(e.user(), 1, Integer::sum));
        bus.subscribe("logout", e -> audit.add("LOGOUT:" + e.user()));
        bus.subscribe("logout", e -> System.out.println("Bye " + e.user().toUpperCase()));
        bus.subscribe("purchase", new Listener<UserEvent>() {
            @Override public void handle(UserEvent e) {
                counter++;
                System.out.println("Purchase #" + counter + " by " + e.user() + ": " + e.action());
            }
        });

        String[][] script = {
            {"login", "ann", "-"}, {"login", "bob", "-"}, {"purchase", "ann", "book"},
            {"unknown", "x", "-"}, {"logout", "ann", "-"}, {"login", "ann", "-"}, {"purchase", "bob", "pen"}
        };
        for (String[] s : script) {
            int n = bus.publish(s[0], new UserEvent(s[1], s[2]));
            System.out.println("  [" + s[0] + "] reached " + n + " handler(s)");
        }
        System.out.println("Audit: " + audit);
        System.out.println("Logins: " + perUser);
        System.out.println("Total dispatched: " + bus.dispatched());

        Function<Integer, Integer> doubler = x -> x * 2;
        Function<Integer, Integer> inc = x -> x + 1;
        System.out.println(doubler.andThen(inc).apply(5) + " " + doubler.compose(inc).apply(5));
        Predicate<String> longWord = s -> s.length() > 4;
        Predicate<String> startsA = s -> s.startsWith("a");
        for (String w : new String[]{"apple", "ant", "banana"})
            System.out.println(w + " " + longWord.and(startsA).test(w) + " " + longWord.or(startsA).test(w) + " " + longWord.negate().test(w));
        Supplier<List<String>> sup = ArrayList::new;
        List<String> fresh = sup.get();
        BiConsumer<List<String>, String> adder = List::add;
        adder.accept(fresh, "z");
        UnaryOperator<String> up = String::toUpperCase;
        BinaryOperator<Integer> mx = BinaryOperator.maxBy(Comparator.naturalOrder());
        System.out.println(fresh + up.apply("done") + mx.apply(3, 9));
    }
}
