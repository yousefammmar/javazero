import java.util.*;

public class Main {
    static class Customer {
        final int id; final int arrival; final int service;
        int start;
        Customer(int id, int arrival, int service) { this.id = id; this.arrival = arrival; this.service = service; }
    }

    public static void main(String[] args) {
        Random rnd = new Random(42);
        Deque<Customer> waiting = new ArrayDeque<>();
        List<Customer> done = new ArrayList<>();
        Customer current = null;
        int busyUntil = 0, nextId = 1, maxQueue = 0, idleTicks = 0;
        for (int t = 0; t < 60; t++) {
            if (rnd.nextInt(100) < 40) {
                Customer c = new Customer(nextId++, t, 1 + rnd.nextInt(5));
                waiting.addLast(c);
                System.out.println("t=" + t + " customer " + c.id + " arrives (service " + c.service + ")");
            }
            if (current != null && t >= busyUntil) {
                System.out.println("t=" + t + " customer " + current.id + " leaves");
                done.add(current);
                current = null;
            }
            if (current == null && !waiting.isEmpty()) {
                current = waiting.pollFirst();
                current.start = t;
                busyUntil = t + current.service;
                System.out.println("t=" + t + " customer " + current.id + " starts (waited " + (t - current.arrival) + ")");
            } else if (current == null) idleTicks++;
            maxQueue = Math.max(maxQueue, waiting.size());
        }
        int totalWait = 0, longest = 0;
        for (Customer c : done) {
            int w = c.start - c.arrival;
            totalWait += w;
            longest = Math.max(longest, w);
        }
        System.out.println("Served: " + done.size() + ", still waiting: " + waiting.size());
        System.out.printf("Average wait: %.2f, longest: %d, max queue: %d, idle ticks: %d%n",
            done.isEmpty() ? 0.0 : (double) totalWait / done.size(), longest, maxQueue, idleTicks);
        System.out.println("Next in line: " + (waiting.peekFirst() == null ? "nobody" : "#" + waiting.peekFirst().id));
    }
}
