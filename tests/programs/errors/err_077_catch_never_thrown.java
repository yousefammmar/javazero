import java.io.IOException;
public class Main {
    public static void main(String[] args) {
        try {
            System.out.println("hi");
        } catch (IOException e) {
            System.out.println("io");
        }
    }
}
