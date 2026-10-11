import java.util.*;
public class Main {
    public static void main(String[] args) {
        List<Integer> l = new ArrayList<>();
        for (int i = 1; i <= 10; i++) l.add(i);
        Collections.shuffle(l, new Random(1));
        System.out.println(l);
        Collections.shuffle(l, new Random(1));
        System.out.println(l);
        Collections.shuffle(l, new Random(777));
        System.out.println(l);
        List<String> s = new LinkedList<>(Arrays.asList("a", "b", "c", "d", "e", "f"));
        Collections.shuffle(s, new Random(31337));
        System.out.println(s);
        Collections.sort(l);
        Collections.shuffle(l, new Random(0));
        System.out.println(l);
        Random r = new Random(5);
        int[] counts = new int[6];
        for (int i = 0; i < 600; i++) counts[r.nextInt(6)]++;
        System.out.println(Arrays.toString(counts));
    }
}
