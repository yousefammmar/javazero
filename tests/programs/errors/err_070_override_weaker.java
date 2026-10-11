class A {
    public void show() { System.out.println("A"); }
}
class B extends A {
    void show() { System.out.println("B"); }
}
public class Main {
    public static void main(String[] args) {
        new B().show();
    }
}
