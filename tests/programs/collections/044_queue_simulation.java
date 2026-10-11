import java.util.*;
public class Main {
    static class Customer {
        final int id, arrive, service;
        Customer(int id, int arrive, int service) { this.id = id; this.arrive = arrive; this.service = service; }
        public String toString() { return "C" + id; }
    }
    public static void main(String[] args) {
        Random rnd = new Random(12345);
        Queue<Customer> line = new LinkedList<>();
        List<Customer> all = new ArrayList<>();
        int t = 0;
        for (int i = 1; i <= 8; i++) { t += rnd.nextInt(4); all.add(new Customer(i, t, 1 + rnd.nextInt(5))); }
        for (Customer c : all) System.out.println(c + " arrives " + c.arrive + " needs " + c.service);
        int clock = 0, idx = 0, busyUntil = 0, totalWait = 0;
        Customer serving = null;
        while (idx < all.size() || !line.isEmpty() || serving != null) {
            while (idx < all.size() && all.get(idx).arrive <= clock) { line.offer(all.get(idx)); System.out.println("t=" + clock + " " + all.get(idx) + " joins, line=" + line); idx++; }
            if (serving != null && clock >= busyUntil) { System.out.println("t=" + clock + " " + serving + " done"); serving = null; }
            if (serving == null && !line.isEmpty()) {
                serving = line.poll();
                totalWait += clock - serving.arrive;
                busyUntil = clock + serving.service;
                System.out.println("t=" + clock + " serving " + serving + " waited " + (clock - serving.arrive));
            }
            clock++;
        }
        System.out.println("total wait " + totalWait + ", avg " + (double) totalWait / all.size() + ", finished at " + clock);
        // Josephus with ArrayDeque
        Deque<Integer> circle = new ArrayDeque<>();
        for (int i = 1; i <= 10; i++) circle.add(i);
        List<Integer> out = new ArrayList<>();
        while (!circle.isEmpty()) { for (int k = 0; k < 2; k++) circle.addLast(circle.pollFirst()); out.add(circle.pollFirst()); }
        System.out.println("josephus " + out);
        // hot potato
        Queue<String> kids = new LinkedList<>(List.of("ann", "bo", "cy", "di", "ed"));
        int round = 0;
        while (kids.size() > 1) { round++; for (int i = 0; i < 3 + round; i++) kids.add(kids.remove()); System.out.println("round " + round + " out: " + kids.remove() + " left " + kids); }
        System.out.println("winner " + kids.peek());
        // sliding window max
        int[] a = {1, 3, -1, -3, 5, 3, 6, 7};
        Deque<Integer> dq = new ArrayDeque<>();
        List<Integer> mx = new ArrayList<>();
        for (int i = 0; i < a.length; i++) {
            while (!dq.isEmpty() && dq.peekFirst() <= i - 3) dq.pollFirst();
            while (!dq.isEmpty() && a[dq.peekLast()] < a[i]) dq.pollLast();
            dq.addLast(i);
            if (i >= 2) mx.add(a[dq.peekFirst()]);
        }
        System.out.println("window max " + mx);
    }
}
