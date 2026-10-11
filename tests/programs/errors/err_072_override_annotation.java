class A {
    void show() { }
}
class B extends A {
    @Override
    void shw() { }
}
public class Main {
    public static void main(String[] args) {
        new B().shw();
    }
}
