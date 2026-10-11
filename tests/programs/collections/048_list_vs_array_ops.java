import java.util.*;
import java.util.stream.*;
public class Main {
    public static void main(String[] args) {
        List<String> a = new ArrayList<>(List.of("a", "b", "c"));
        List<String> b = new LinkedList<>(a);
        System.out.println(a.equals(b) + " " + a.hashCode() + " " + b.hashCode() + " " + a);
        a.addAll(b);
        a.addAll(0, List.of("X", "Y"));
        System.out.println(a + " " + a.size());
        System.out.println(a.indexOf("b") + " " + a.lastIndexOf("b") + " " + a.containsAll(List.of("a", "X")) + " " + a.containsAll(List.of("a", "Q")));
        a.retainAll(List.of("a", "b", "X"));
        System.out.println(a);
        a.removeAll(List.of("a"));
        System.out.println(a);
        List<Integer> nums = IntStream.rangeClosed(1, 12).boxed().collect(Collectors.toList());
        List<Integer> sub = nums.subList(3, 9);
        System.out.println(sub + " " + sub.size() + " " + sub.indexOf(7) + " " + sub.contains(1));
        Collections.reverse(sub);
        System.out.println(nums);
        sub.add(100);
        System.out.println(nums + " " + sub);
        sub.remove(Integer.valueOf(100));
        nums.subList(0, 3).clear();
        System.out.println(nums);
        nums.subList(2, 4).replaceAll(x -> x * 10);
        System.out.println(nums);
        List<Integer> evens = new ArrayList<>(nums);
        evens.removeIf(x -> x % 2 != 0);
        System.out.println(evens);
        Collections.sort(nums.subList(0, 5));
        System.out.println(nums);
        ListIterator<Integer> li = nums.listIterator(nums.size());
        StringBuilder sb = new StringBuilder();
        while (li.hasPrevious()) { int idx = li.previousIndex(); int v = li.previous(); if (idx % 3 == 0) sb.append(v).append(' '); }
        System.out.println(sb.toString().trim());
        List<Integer> big = new ArrayList<>();
        for (int i = 0; i < 1000; i++) big.add(i % 17);
        System.out.println(big.size() + " " + big.stream().mapToInt(i -> i).sum() + " " + big.indexOf(16) + " " + big.lastIndexOf(0) + " " + Collections.frequency(big, 5));
        try { nums.subList(3, 1); } catch (IllegalArgumentException e) { System.out.println("IAE " + e.getMessage()); }
        try { nums.subList(0, 100); } catch (IndexOutOfBoundsException e) { System.out.println("IOOBE " + e.getMessage()); }
        try { nums.add(100, 1); } catch (IndexOutOfBoundsException e) { System.out.println("IOOBE add " + e.getMessage()); }
        try { new ArrayList<Integer>().get(0); } catch (IndexOutOfBoundsException e) { System.out.println(e.getMessage()); }
        try { new LinkedList<Integer>().get(2); } catch (IndexOutOfBoundsException e) { System.out.println(e.getMessage()); }
        try { int[] z = new int[2]; z[2] = 1; } catch (ArrayIndexOutOfBoundsException e) { System.out.println(e.getMessage()); }
        try { List.of(1).get(3); } catch (IndexOutOfBoundsException e) { System.out.println("List.of " + e.getMessage()); }
        try { nums.subList(1, 3).get(5); } catch (IndexOutOfBoundsException e) { System.out.println("sub " + e.getMessage()); }
    }
}
