import java.util.*;
public class Main {
    static class Task {
        final String name; final int priority; final int seq;
        Task(String n, int p, int s) { name = n; priority = p; seq = s; }
        @Override public String toString() { return name + "(" + priority + ")"; }
    }
    public static void main(String[] args) {
        PriorityQueue<Task> pq = new PriorityQueue<>((a, b) -> a.priority != b.priority ? Integer.compare(b.priority, a.priority) : Integer.compare(a.seq, b.seq));
        String[] names = {"write", "test", "deploy", "review", "plan", "lint", "fix", "docs"};
        int[] pr = {3, 5, 1, 4, 5, 2, 4, 1};
        for (int i = 0; i < names.length; i++) {
            pq.add(new Task(names[i], pr[i], i));
            System.out.println("heap: " + pq);
        }
        while (!pq.isEmpty()) System.out.print(pq.poll() + " ");
        System.out.println();
        // top-k
        int[] data = {7, 2, 9, 4, 11, 3, 8, 1, 10, 6, 5};
        int k = 4;
        PriorityQueue<Integer> minHeap = new PriorityQueue<>();
        for (int d : data) {
            minHeap.offer(d);
            if (minHeap.size() > k) minHeap.poll();
            System.out.println("after " + d + ": " + minHeap);
        }
        List<Integer> top = new ArrayList<>(minHeap);
        Collections.sort(top, Collections.reverseOrder());
        System.out.println("top " + k + ": " + top);
        // k smallest via max-heap
        PriorityQueue<Integer> maxHeap = new PriorityQueue<>(k, (a, b) -> b - a);
        for (int d : data) { maxHeap.add(d); if (maxHeap.size() > k) maxHeap.poll(); }
        System.out.println("max-heap content " + maxHeap);
        // frequent words top-2
        String text = "a b c a b a d e a b c c c c";
        Map<String, Integer> freq = new HashMap<>();
        for (String w : text.split(" ")) freq.merge(w, 1, Integer::sum);
        System.out.println(freq);
        PriorityQueue<Map.Entry<String, Integer>> pe = new PriorityQueue<>((x, y) -> y.getValue() - x.getValue() != 0 ? y.getValue() - x.getValue() : x.getKey().compareTo(y.getKey()));
        pe.addAll(freq.entrySet());
        for (int i = 0; i < 3; i++) { Map.Entry<String, Integer> e = pe.poll(); System.out.println(e.getKey() + "=" + e.getValue()); }
    }
}
