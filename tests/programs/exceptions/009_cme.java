import java.util.*;
public class Main {
    static void iterate(String label, List<Integer> list, int removeAt) {
        try {
            for (Integer x : list) {
                System.out.println(label + " visit " + x);
                if (x == removeAt) list.remove(x);
            }
            System.out.println(label + " done " + list);
        } catch (ConcurrentModificationException e) {
            System.out.println(label + " CME msg=" + e.getMessage() + " list=" + list);
        }
    }
    public static void main(String[] args) {
        iterate("first", new ArrayList<>(List.of(1, 2, 3, 4)), 1);
        iterate("middle", new ArrayList<>(List.of(1, 2, 3, 4)), 2);
        iterate("secondLast", new ArrayList<>(List.of(1, 2, 3, 4)), 3);
        iterate("last", new ArrayList<>(List.of(1, 2, 3, 4)), 4);
        iterate("single", new ArrayList<>(List.of(1)), 1);
        List<String> l = new ArrayList<>(List.of("a", "b", "c"));
        try { for (String s : l) { l.add("z"); } }
        catch (ConcurrentModificationException e) { System.out.println("add CME " + l); }
        Map<String, Integer> m = new HashMap<>();
        m.put("a", 1); m.put("b", 2); m.put("c", 3);
        try { for (String k : m.keySet()) { m.remove("a"); m.put("q", 1); } }
        catch (ConcurrentModificationException e) { System.out.println("map CME " + e.getMessage()); }
        List<Integer> nums = new ArrayList<>(List.of(1, 2, 3, 4, 5, 6));
        Iterator<Integer> it = nums.iterator();
        while (it.hasNext()) if (it.next() % 2 == 0) it.remove();
        System.out.println(nums);
        nums.removeIf(x -> x > 3);
        System.out.println(nums);
        Iterator<Integer> it2 = nums.iterator();
        try { it2.remove(); } catch (IllegalStateException e) { System.out.println("ISE " + e.getMessage()); }
        it2.next();
        it2.remove();
        try { it2.remove(); } catch (IllegalStateException e) { System.out.println("ISE2 " + e.getMessage()); }
        System.out.println(nums);
        try { nums.forEach(x -> nums.add(9)); } catch (ConcurrentModificationException e) { System.out.println("forEach CME"); }
        List<Integer> sub = new ArrayList<>(List.of(1, 2, 3, 4));
        List<Integer> view = sub.subList(0, 2);
        sub.add(5);
        try { view.size(); } catch (ConcurrentModificationException e) { System.out.println("subList CME"); }
    }
}
