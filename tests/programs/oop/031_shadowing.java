public class Main {
    static int x = 1;
    int y = 2;
    int z = 3;
    void test(int y) {
        System.out.println("param y=" + y + " this.y=" + this.y);
        int z = 30;
        System.out.println("local z=" + z + " this.z=" + this.z);
        this.z = z + y;
        {
            int w = 5;
            System.out.println("block w=" + w);
        }
        int w = 6;
        System.out.println("outer w=" + w + " z=" + this.z);
    }
    void setY(int y) { y = y; }
    void setY2(int y) { this.y = y; }
    static void shadowStatic() {
        int x = 10;
        System.out.println("local x=" + x + " Main.x=" + Main.x);
        for (int i = 0; i < 2; i++) { int x2 = x + i; System.out.println("loop " + x2); }
    }
    static class Parent { String v = "parent"; String get() { return v; } }
    static class Child extends Parent {
        String v = "child";
        String both() { String v = "local"; return v + "," + this.v + "," + super.v + "," + get(); }
    }
    class In {
        int y = 20;
        void show(int y) { System.out.println(y + " " + this.y + " " + Main.this.y); }
    }
    public static void main(String[] args) {
        Main m = new Main();
        m.test(100);
        System.out.println(m.y + " " + m.z);
        m.setY(55);
        System.out.println("after setY " + m.y);
        m.setY2(55);
        System.out.println("after setY2 " + m.y);
        shadowStatic();
        System.out.println(new Child().both());
        m.new In().show(200);
        int i = 99;
        for (int j = 0; j < 2; j++) i += j;
        System.out.println(i);
        Runnable r = () -> { int q = 4; System.out.println("lambda q=" + q + " x=" + x); };
        r.run();
        String args2 = "shadow";
        { String t = args2; System.out.println(t); }
        { String t = "again"; System.out.println(t); }
    }
}
