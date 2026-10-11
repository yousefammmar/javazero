import java.util.List;
public class Main {
    public static void main(String[] args) {
        List<String> l = List.of("a", "b");
        l.forEach(s -> printIt(s));
    }
}
