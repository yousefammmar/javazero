import java.util.*;
public class Main {
    public static void main(String[] args) {
        List<Object> items = new ArrayList<>();
        items.add("one"); items.add(2); items.add(3.0);
        for (Object o : items) {
            System.out.println("item " + o);
            Integer i = (Integer) o;
            System.out.println("int " + i);
        }
    }
}
