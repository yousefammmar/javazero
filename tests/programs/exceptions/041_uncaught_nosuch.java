import java.util.*;
public class Main {
    public static void main(String[] args) {
        Optional<String> a = Optional.of("here");
        System.out.println(a.get());
        Optional<String> b = Optional.empty();
        System.out.println(b.orElse("fallback"));
        System.out.println(b.get());
    }
}
