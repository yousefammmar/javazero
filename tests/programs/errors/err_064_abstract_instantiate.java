abstract class Shape {
    abstract double area();
}
public class Main {
    public static void main(String[] args) {
        Shape s = new Shape();
        System.out.println(s.area());
    }
}
