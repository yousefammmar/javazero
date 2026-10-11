import java.util.*;
public class Main {
    public static void main(String[] args) {
        List<Integer> fixed = List.of(1, 2, 3);
        System.out.println(fixed);
        List<Integer> copy = new ArrayList<>(fixed);
        copy.add(4);
        System.out.println(copy);
        fixed.add(4);
        System.out.println("unreachable");
    }
}
