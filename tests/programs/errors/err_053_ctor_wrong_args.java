class Point {
    int x, y;
    Point(int x, int y) { this.x = x; this.y = y; }
}
public class Main {
    public static void main(String[] args) {
        Point p = new Point(1);
        System.out.println(p.x);
    }
}
