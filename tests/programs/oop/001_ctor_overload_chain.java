public class Main {
    static class Point {
        int x, y; String label;
        Point() { this(0, 0); System.out.println("Point()"); }
        Point(int x) { this(x, x); System.out.println("Point(int)"); }
        Point(int x, int y) { this(x, y, "pt"); System.out.println("Point(int,int)"); }
        Point(int x, int y, String label) { this.x = x; this.y = y; this.label = label; System.out.println("Point(int,int,String)"); }
        Point(long v) { this((int) v, 1); System.out.println("Point(long)"); }
        Point(double d) { this((int) d); System.out.println("Point(double)"); }
        public String toString() { return label + "(" + x + "," + y + ")"; }
    }
    static void show(Object o) { System.out.println("Object " + o); }
    static void show(String s) { System.out.println("String " + s); }
    static void show(int i) { System.out.println("int " + i); }
    static void show(long l) { System.out.println("long " + l); }
    static void show(double d) { System.out.println("double " + d); }
    static void show(Integer i) { System.out.println("Integer " + i); }
    static void show(char c) { System.out.println("char " + c); }
    static void show(int... xs) { System.out.println("varargs " + xs.length); }
    public static void main(String[] args) {
        System.out.println(new Point());
        System.out.println(new Point(5));
        System.out.println(new Point(3, 4));
        System.out.println(new Point(7L));
        System.out.println(new Point(2.9));
        System.out.println(new Point(1, 2, "zz"));
        byte b = 3; short s = 4; char c = 'x'; float f = 1.5f;
        show(b); show(s); show(c); show(5); show(5L); show(f); show(2.0);
        show("hi"); show((Object) "hi"); show(Integer.valueOf(9));
        show(); show(1, 2, 3);
        show((Object) null);
        show((String) null);
        show('a' + 1);
        show((char) ('a' + 1));
        Long boxed = 4L;
        show(boxed);
    }
}
