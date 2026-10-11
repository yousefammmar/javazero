import java.util.*;
public class Main {
    public static void main(String[] args) {
        TreeSet<Integer> ts = new TreeSet<>();
        for (int x : new int[]{42, 7, 19, 3, 88, 56, 7, 19, 71, 1}) ts.add(x);
        System.out.println(ts + " size " + ts.size());
        System.out.println(ts.first() + " " + ts.last());
        System.out.println("floor 20=" + ts.floor(20) + " ceiling 20=" + ts.ceiling(20) + " lower 19=" + ts.lower(19) + " higher 19=" + ts.higher(19));
        System.out.println("lower 1=" + ts.lower(1) + " higher 88=" + ts.higher(88));
        System.out.println("headSet(19) " + ts.headSet(19) + " headSet(19,true) " + ts.headSet(19, true));
        System.out.println("tailSet(56) " + ts.tailSet(56) + " tailSet(56,false) " + ts.tailSet(56, false));
        System.out.println("subSet(7,56) " + ts.subSet(7, 56) + " incl " + ts.subSet(7, true, 56, true));
        System.out.println("desc " + ts.descendingSet());
        Iterator<Integer> di = ts.descendingIterator();
        while (di.hasNext()) System.out.print(di.next() + " ");
        System.out.println();
        System.out.println("pollFirst " + ts.pollFirst() + " pollLast " + ts.pollLast() + " " + ts);
        SortedSet<Integer> view = ts.headSet(50);
        ts.add(2); ts.add(60);
        System.out.println("view " + view + " ts " + ts);
        System.out.println(ts.contains(19) + " " + ts.remove(19) + " " + ts.remove(19));
        TreeSet<String> words = new TreeSet<>(Arrays.asList("delta", "alpha", "charlie", "bravo", "echo"));
        System.out.println(words + " " + words.ceiling("c") + " " + words.floor("c") + " " + words.headSet("charlie"));
        TreeSet<Character> cs = new TreeSet<>();
        for (char c : "mississippi river".toCharArray()) cs.add(c);
        System.out.println(cs);
        TreeSet<Integer> a = new TreeSet<>(List.of(1, 2, 3, 4, 5)), b = new TreeSet<>(List.of(4, 5, 6, 7));
        a.retainAll(b);
        System.out.println(a);
        b.removeAll(a);
        System.out.println(b);
        try { new TreeSet<Integer>().first(); } catch (NoSuchElementException e) { System.out.println("NSEE"); }
        System.out.println(new TreeSet<Integer>().pollFirst());
        // ranges: count of values in [lo, hi]
        TreeSet<Integer> data = new TreeSet<>();
        for (int i = 0; i < 50; i++) data.add((i * 17) % 101);
        System.out.println(data.subSet(20, true, 40, true).size() + " " + data.subSet(20, true, 40, true));
    }
}
