public class Main {
    static int counter = trace("Main.counter");
    static { System.out.println("Main static block"); }
    static int trace(String s) { System.out.println("init " + s); return 1; }
    static class A {
        static int a = trace("A.a");
        static { System.out.println("A static block"); }
        static final int CONST = 10;
        static final Integer BOXED = 20;
        static void touch() { System.out.println("A.touch"); }
    }
    static class B extends A {
        static int b = trace("B.b");
        static { System.out.println("B static block"); }
    }
    static class C {
        static { System.out.println("C static block"); }
        static int v = 5;
    }
    static class Lazy {
        static final Lazy INSTANCE = new Lazy();
        static int count = 100;
        int seen;
        Lazy() { seen = count; System.out.println("Lazy ctor count=" + count); }
    }
    public static void main(String[] args) {
        System.out.println("main start");
        System.out.println("A.CONST=" + A.CONST);
        System.out.println("after const");
        System.out.println("A.BOXED=" + A.BOXED);
        System.out.println("after boxed");
        System.out.println("B.a via subclass");
        int x = B.a;
        System.out.println("x=" + x);
        System.out.println("B.b");
        System.out.println(B.b);
        A[] arr = new A[2];
        System.out.println("array of C created");
        C[] cs = new C[1];
        System.out.println("cs.length=" + cs.length);
        System.out.println("C.v=" + C.v);
        System.out.println("Lazy.count=" + Lazy.count);
        System.out.println("Lazy seen=" + Lazy.INSTANCE.seen);
        A.touch();
        System.out.println(arr.length);
    }
}
