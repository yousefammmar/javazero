import java.util.*;
public class Main {
    public static void main(String[] args) {
        List<String> names = new ArrayList<>(List.of("ann", "bob", "cy", "dee", "ed", "flo"));
        Iterator<String> it = names.iterator();
        while (it.hasNext()) {
            String s = it.next();
            if (s.length() == 2) it.remove();
        }
        System.out.println("after iterator remove: " + names);
        try {
            for (String s : names) if (s.equals("bob")) names.remove(s);
            System.out.println("no CME (removed second to last)");
        } catch (ConcurrentModificationException e) {
            System.out.println("CME 1");
        }
        System.out.println(names);
        List<Integer> nums = new ArrayList<>(List.of(1, 2, 3, 4, 5));
        try {
            for (Integer n : nums) if (n == 2) nums.remove(n);
        } catch (ConcurrentModificationException e) {
            System.out.println("CME 2 " + e.getMessage());
        }
        System.out.println(nums);
        try {
            for (Integer n : nums) nums.add(n);
        } catch (ConcurrentModificationException e) {
            System.out.println("CME 3");
        }
        Map<String, Integer> m = new HashMap<>();
        m.put("a", 1); m.put("b", 2); m.put("c", 3);
        try {
            for (String k : m.keySet()) m.put(k + "x", 0);
        } catch (ConcurrentModificationException e) {
            System.out.println("CME map");
        }
        Iterator<Map.Entry<String, Integer>> mi = new HashMap<>(Map.of("q", 1)).entrySet().iterator();
        mi.next();
        mi.remove();
        try { mi.remove(); } catch (IllegalStateException e) { System.out.println("ISE remove twice"); }
        ListIterator<Integer> li = nums.listIterator();
        while (li.hasNext()) {
            int i = li.nextIndex();
            int v = li.next();
            if (v % 2 == 1) li.set(v * 100);
            else li.add(-v);
            System.out.println("at " + i + " saw " + v);
        }
        System.out.println(nums);
        while (li.hasPrevious()) System.out.print(li.previous() + " ");
        System.out.println();
        try { new ArrayList<Integer>().iterator().next(); } catch (NoSuchElementException e) { System.out.println("NSEE"); }
        try { nums.get(99); } catch (IndexOutOfBoundsException e) { System.out.println(e.getMessage()); }
    }
}
