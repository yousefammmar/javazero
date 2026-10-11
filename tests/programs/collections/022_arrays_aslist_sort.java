import java.util.*;
public class Main {
    public static void main(String[] args) {
        String[] arr = {"pear", "Apple", "fig", "banana", "cherry", "date"};
        List<String> view = Arrays.asList(arr);
        view.set(0, "PEAR");
        System.out.println(Arrays.toString(arr) + " " + view);
        arr[1] = "apple";
        System.out.println(view);
        Collections.sort(view);
        System.out.println("sorted through view: " + Arrays.toString(arr));
        try { view.add("x"); } catch (UnsupportedOperationException e) { System.out.println("UOE add"); }
        try { view.remove(0); } catch (UnsupportedOperationException e) { System.out.println("UOE remove"); }
        Arrays.sort(arr, String.CASE_INSENSITIVE_ORDER);
        System.out.println(Arrays.toString(arr));
        Arrays.sort(arr, Comparator.comparing(String::length).thenComparing(Comparator.reverseOrder()));
        System.out.println(Arrays.toString(arr));
        Integer[] nums = {5, 3, 9, 1, 7, 2};
        Arrays.sort(nums, Collections.reverseOrder());
        System.out.println(Arrays.toString(nums));
        Arrays.sort(nums, 1, 5);
        System.out.println(Arrays.toString(nums));
        Arrays.sort(nums, (x, y) -> (x % 3) - (y % 3));
        System.out.println("stable by mod 3: " + Arrays.toString(nums));
        int[] prim = {4, 2, 9};
        List<int[]> wrapped = Arrays.asList(prim);
        System.out.println("asList(int[]) size " + wrapped.size());
        List<Integer> boxed = Arrays.asList(4, 2, 9);
        System.out.println(boxed + " " + boxed.size() + " " + boxed.contains(2) + " " + boxed.indexOf(9));
        List<Integer> mutable = new ArrayList<>(boxed);
        mutable.add(1);
        System.out.println(mutable);
        String[][] table = {{"b", "a"}, {"d", "c"}};
        Arrays.sort(table, (r1, r2) -> r1[1].compareTo(r2[1]));
        System.out.println(Arrays.deepToString(table));
        Object[] objs = {1, "two", 3.0, 'c', null, new int[]{1}.length};
        System.out.println(Arrays.toString(objs));
        Arrays.fill(arr, "z");
        System.out.println(view);
        System.out.println(Arrays.asList(1, 2, 3).equals(List.of(1, 2, 3)));
        Arrays.setAll(nums, i -> i * i);
        System.out.println(Arrays.toString(nums));
        System.out.println(Arrays.asList(arr).stream().distinct().count());
    }
}
