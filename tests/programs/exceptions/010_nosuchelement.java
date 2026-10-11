import java.util.*;
public class Main {
    static void t(String label, Runnable r) {
        try { r.run(); System.out.println(label + ": ok"); }
        catch (Exception e) { System.out.println(label + ": " + e.getClass().getName() + " msg=" + e.getMessage()); }
    }
    public static void main(String[] args) {
        t("iterator", () -> new ArrayList<Integer>().iterator().next());
        t("optional get", () -> Optional.empty().get());
        t("optional orElseThrow", () -> Optional.empty().orElseThrow());
        t("deque removeFirst", () -> new ArrayDeque<Integer>().removeFirst());
        t("deque removeLast", () -> new ArrayDeque<Integer>().removeLast());
        t("deque getFirst", () -> new ArrayDeque<Integer>().getFirst());
        t("deque element", () -> new ArrayDeque<Integer>().element());
        t("deque pop", () -> new ArrayDeque<Integer>().pop());
        t("deque remove", () -> new ArrayDeque<Integer>().remove());
        t("deque poll", () -> System.out.println(new ArrayDeque<Integer>().poll()));
        t("queue remove", () -> new LinkedList<Integer>().remove());
        t("linkedlist getFirst", () -> new LinkedList<Integer>().getFirst());
        t("linkedlist removeLast", () -> new LinkedList<Integer>().removeLast());
        t("pq remove", () -> new PriorityQueue<Integer>().remove());
        t("pq element", () -> new PriorityQueue<Integer>().element());
        t("treeset first", () -> new TreeSet<Integer>().first());
        t("treeset last", () -> new TreeSet<Integer>().last());
        t("treemap firstKey", () -> new TreeMap<Integer, Integer>().firstKey());
        t("scanner empty nextLine", () -> new Scanner("").nextLine());
        t("scanner empty next", () -> new Scanner("").next());
        t("scanner nextInt empty", () -> new Scanner("").nextInt());
        t("list iterator", () -> List.of(1).iterator().next());
        t("map keySet iterator", () -> { Iterator<String> it = new HashMap<String, String>().keySet().iterator(); it.next(); });
        t("stream max get", () -> java.util.stream.IntStream.empty().max().getAsInt());
        t("stream findFirst", () -> java.util.stream.Stream.empty().findFirst().get());
        t("optional present", () -> System.out.println(Optional.of(3).get()));
        t("exhausted", () -> { Iterator<Integer> it = List.of(1, 2).iterator(); it.next(); it.next(); it.next(); });
        t("collections max", () -> Collections.max(new ArrayList<Integer>()));
        t("StringTokenizer", () -> new StringTokenizer("").nextToken());
    }
}
