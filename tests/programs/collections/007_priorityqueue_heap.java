import java.util.*;
public class Main {
    public static void main(String[] args) {
        PriorityQueue<Integer> pq = new PriorityQueue<>();
        int[] vals = {50, 20, 40, 10, 30, 5, 45, 15, 25, 35};
        for (int v : vals) {
            pq.add(v);
            System.out.println("added " + v + " -> " + pq);
        }
        System.out.println("peek " + pq.peek() + " size " + pq.size());
        StringBuilder sb = new StringBuilder();
        for (int x : pq) sb.append(x).append(' ');
        System.out.println("iter: " + sb);
        System.out.println("poll " + pq.poll() + " -> " + pq);
        System.out.println("poll " + pq.poll() + " -> " + pq);
        pq.remove(40);
        System.out.println("remove 40 -> " + pq);
        System.out.println("contains 25: " + pq.contains(25));
        List<Integer> order = new ArrayList<>();
        while (!pq.isEmpty()) order.add(pq.poll());
        System.out.println("poll order " + order);
        PriorityQueue<Integer> rev = new PriorityQueue<>(Collections.reverseOrder());
        for (int v : vals) rev.offer(v);
        System.out.println("max-heap " + rev);
        System.out.println(rev.poll() + " " + rev.poll() + " " + rev);
        PriorityQueue<String> ps = new PriorityQueue<>(Comparator.comparing(String::length).thenComparing(Comparator.naturalOrder()));
        for (String s : "the quick brown fox jumps over a lazy dog".split(" ")) ps.add(s);
        System.out.println(ps);
        StringBuilder o = new StringBuilder();
        while (ps.size() > 0) o.append(ps.poll()).append(' ');
        System.out.println(o);
        PriorityQueue<Integer> fromList = new PriorityQueue<>(List.of(9, 8, 7, 6, 5, 4, 3, 2, 1));
        System.out.println("from collection: " + fromList);
        System.out.println(pq.poll() + " " + pq.peek());
    }
}
