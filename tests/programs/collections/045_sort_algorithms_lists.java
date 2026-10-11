import java.util.*;
public class Main {
    static <T extends Comparable<T>> List<T> mergeSort(List<T> l) {
        if (l.size() <= 1) return l;
        int mid = l.size() / 2;
        List<T> a = mergeSort(new ArrayList<>(l.subList(0, mid))), b = mergeSort(new ArrayList<>(l.subList(mid, l.size())));
        List<T> out = new ArrayList<>();
        int i = 0, j = 0;
        while (i < a.size() && j < b.size()) out.add(a.get(i).compareTo(b.get(j)) <= 0 ? a.get(i++) : b.get(j++));
        while (i < a.size()) out.add(a.get(i++));
        while (j < b.size()) out.add(b.get(j++));
        return out;
    }
    static void insertionSort(List<Integer> l) {
        for (int i = 1; i < l.size(); i++) {
            int key = l.get(i), j = i - 1;
            while (j >= 0 && l.get(j) > key) { l.set(j + 1, l.get(j)); j--; }
            l.set(j + 1, key);
        }
    }
    static List<Integer> quick(List<Integer> l) {
        if (l.size() < 2) return l;
        int p = l.get(l.size() / 2);
        List<Integer> lo = new ArrayList<>(), eq = new ArrayList<>(), hi = new ArrayList<>();
        for (int x : l) (x < p ? lo : x == p ? eq : hi).add(x);
        List<Integer> r = new ArrayList<>(quick(lo));
        r.addAll(eq); r.addAll(quick(hi));
        return r;
    }
    public static void main(String[] args) {
        Random r = new Random(2024);
        List<Integer> data = new ArrayList<>();
        for (int i = 0; i < 15; i++) data.add(r.nextInt(100));
        System.out.println(data);
        System.out.println(mergeSort(data));
        System.out.println(quick(data));
        List<Integer> ins = new ArrayList<>(data);
        insertionSort(ins);
        System.out.println(ins + " " + ins.equals(mergeSort(data)));
        List<String> ws = new ArrayList<>(List.of("pear", "Apple", "fig", "banana", "Cherry", "date", "elder"));
        System.out.println(mergeSort(ws));
        ws.sort(String::compareToIgnoreCase);
        System.out.println(ws);
        ws.sort(Comparator.comparing(String::length).thenComparing(String.CASE_INSENSITIVE_ORDER));
        System.out.println(ws);
        List<Integer> asc = new ArrayList<>(ins);
        int[] targets = {ins.get(0), ins.get(7), -5, 500, 50};
        for (int t : targets) System.out.println("search " + t + " -> " + Collections.binarySearch(asc, t));
        Integer[] boxed = data.toArray(new Integer[0]);
        Arrays.sort(boxed, (x, y) -> Integer.compare(y % 10, x % 10));
        System.out.println(Arrays.toString(boxed));
        int[] prim = data.stream().mapToInt(Integer::intValue).toArray();
        Arrays.sort(prim);
        System.out.println(Arrays.toString(prim) + " median " + prim[prim.length / 2]);
        System.out.println(r.nextInt(1000) + " " + r.nextDouble() + " " + r.nextBoolean() + " " + r.nextLong() + " " + r.nextInt(5, 10));
        Collections.shuffle(data, new Random(1));
        System.out.println(data);
    }
}
