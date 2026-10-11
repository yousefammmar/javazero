import java.util.*;
public class Main {
    public static void main(String[] args) {
        List<Integer> l = new ArrayList<>();
        for (int i = 0; i < 3; i++) l.add(i * i);
        System.out.println(l);
        System.out.println(l.get(2));
        l.remove(1);
        System.out.println(l.get(2));
    }
}
