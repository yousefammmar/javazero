import java.util.*;
public class Main {
    public static void main(String[] args) {
        List<String> names = new ArrayList<>(List.of("a", "b", "c", "d"));
        for (String n : names) {
            System.out.println("visiting " + n);
            if (n.equals("b")) names.remove(n);
        }
        System.out.println("unreachable");
    }
}
