import java.util.*;
public class Main {
    public static void main(String[] args) {
        List<Integer> list = new ArrayList<>();
        for (int i = 1; i <= 8; i++) list.add(i * 10);
        System.out.println(list);
        list.add(2, 15);
        System.out.println("insert at 2: " + list);
        list.remove(1);
        System.out.println("remove(int 1): " + list);
        list.remove(Integer.valueOf(50));
        System.out.println("remove(Object 50): " + list);
        list.remove((Integer) 999);
        System.out.println("remove missing: " + list);
        System.out.println("set returns " + list.set(0, 11));
        System.out.println("indexOf 60=" + list.indexOf(60) + " lastIndexOf 60=" + list.lastIndexOf(60) + " indexOf 5=" + list.indexOf(5));
        System.out.println("contains 70: " + list.contains(70) + ", contains 71: " + list.contains(71));
        List<Integer> sub = list.subList(1, 4);
        System.out.println("sub: " + sub);
        sub.set(0, -1);
        System.out.println("after sub.set: " + list);
        sub.clear();
        System.out.println("after sub.clear: " + list);
        list.addAll(1, Arrays.asList(7, 8, 9));
        System.out.println("addAll(1,..): " + list);
        list.sort(Comparator.reverseOrder());
        System.out.println("sorted desc: " + list);
        list.replaceAll(x -> x * 2);
        System.out.println("doubled: " + list);
        list.removeIf(x -> x % 3 == 0);
        System.out.println("removeIf %3: " + list);
        Integer[] arr = list.toArray(new Integer[0]);
        System.out.println("array: " + Arrays.toString(arr) + " len " + arr.length);
        Object[] oa = list.toArray();
        System.out.println("object array len " + oa.length);
        List<Integer> copy = new ArrayList<>(list);
        System.out.println("equals copy: " + list.equals(copy));
        copy.add(0);
        System.out.println("equals after add: " + list.equals(copy));
        System.out.println("hashCode equal lists: " + new ArrayList<>(List.of(1, 2, 3)).hashCode() + " " + Arrays.asList(1, 2, 3).hashCode());
        Iterator<Integer> it = list.iterator();
        int sum = 0;
        while (it.hasNext()) sum += it.next();
        System.out.println("sum " + sum);
        list.clear();
        System.out.println(list + " empty=" + list.isEmpty() + " size=" + list.size());
    }
}
