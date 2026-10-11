import java.util.*;
public class Main {
    public static void main(String[] args) {
        TreeMap<Integer, String> t = new TreeMap<>();
        int[] ks = {50, 20, 80, 10, 30, 70, 90, 60, 40};
        for (int k : ks) t.put(k, "v" + k);
        System.out.println(t);
        System.out.println(t.firstKey() + " " + t.lastKey() + " " + t.firstEntry() + " " + t.lastEntry());
        System.out.println("floor 55=" + t.floorKey(55) + " ceiling 55=" + t.ceilingKey(55) + " lower 50=" + t.lowerKey(50) + " higher 50=" + t.higherKey(50));
        System.out.println("floor 5=" + t.floorKey(5) + " ceiling 95=" + t.ceilingKey(95));
        System.out.println("floorEntry " + t.floorEntry(35) + " ceilingEntry " + t.ceilingEntry(35));
        System.out.println("head<40 " + t.headMap(40));
        System.out.println("head<=40 " + t.headMap(40, true));
        System.out.println("tail>=60 " + t.tailMap(60));
        System.out.println("tail>60 " + t.tailMap(60, false));
        System.out.println("sub[30,70) " + t.subMap(30, 70));
        System.out.println("sub(30,70] " + t.subMap(30, false, 70, true));
        System.out.println("desc " + t.descendingMap());
        System.out.println("descKeys " + t.descendingKeySet());
        SortedMap<Integer, String> view = t.headMap(35);
        t.put(5, "v5"); t.put(33, "v33");
        System.out.println("view reflects: " + view);
        System.out.println("pollFirst " + t.pollFirstEntry() + " pollLast " + t.pollLastEntry());
        System.out.println(t.size() + " " + t);
        t.remove(50);
        System.out.println(t.navigableKeySet());
        try { new TreeMap<Integer, Integer>().firstKey(); } catch (NoSuchElementException e) { System.out.println("NSEE"); }
        System.out.println(new TreeMap<Integer, Integer>().firstEntry());
        try { t.put(null, "x"); } catch (NullPointerException e) { System.out.println("NPE null key"); }
        TreeMap<String, Integer> sm = new TreeMap<>();
        for (String s : "the quick brown fox jumps over the lazy dog".split(" ")) sm.merge(s, 1, Integer::sum);
        System.out.println(sm);
        System.out.println(sm.firstKey() + " " + sm.ceilingKey("m") + " " + sm.headMap("l"));
    }
}
