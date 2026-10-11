import java.util.function.Supplier;
public class Main {
    public static void main(String[] args) {
        int count = 0;
        Runnable r = () -> System.out.println(count);
        count++;
        r.run();
    }
}
