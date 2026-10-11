abstract class Shape {
    abstract double area();
}
class Circle extends Shape {
    double r = 1;
}
public class Main {
    public static void main(String[] args) {
        System.out.println(new Circle().r);
    }
}
