class A {
    int value() { return 1; }
}
class B extends A {
    String value() { return "x"; }
}
public class Main {
    public static void main(String[] args) {
        System.out.println(new B().value());
    }
}
