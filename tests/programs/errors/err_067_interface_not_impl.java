interface Runner {
    void run();
}
class Person implements Runner {
    void walk() { }
}
public class Main {
    public static void main(String[] args) {
        new Person().walk();
    }
}
