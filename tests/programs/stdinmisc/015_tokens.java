import java.util.*;
public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int words = 0, ints = 0, other = 0;
        long isum = 0;
        List<String> ws = new ArrayList<>();
        while (sc.hasNext()) {
            if (sc.hasNextInt()) { isum += sc.nextInt(); ints++; }
            else if (sc.hasNextDouble()) { System.out.println("double " + sc.nextDouble()); other++; }
            else { ws.add(sc.next()); words++; }
        }
        System.out.println("ints=" + ints + " sum=" + isum + " words=" + words + " other=" + other);
        System.out.println(ws);
    }
}
