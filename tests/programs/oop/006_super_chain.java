public class Main {
    static class A {
        protected int v;
        A() { this(1); System.out.println("A()"); }
        A(int v) { this.v = v; System.out.println("A(int) v=" + v); }
        int calc() { return v; }
        public String toString() { return "A[" + calc() + "]"; }
    }
    static class B extends A {
        B() { super(10); System.out.println("B()"); }
        B(int x) { System.out.println("B(int) x=" + x); v += x; }
        B(String s) { this(s.length()); System.out.println("B(String) " + s); }
        @Override int calc() { return super.calc() * 2; }
    }
    static class C extends B {
        int extra = 7;
        C() { super("hello"); System.out.println("C() extra=" + extra); }
        C(int e) { super(e); extra = e; System.out.println("C(int)"); }
        @Override int calc() { return super.calc() + extra; }
    }
    static class D extends C {
        D() { System.out.println("D()"); }
    }
    public static void main(String[] args) {
        System.out.println(new A());
        System.out.println(new A(5));
        System.out.println(new B());
        System.out.println(new B(3));
        System.out.println(new B("abc"));
        System.out.println(new C());
        System.out.println(new C(4));
        System.out.println(new D());
        A a = new D();
        System.out.println(a.v + " " + a.calc());
    }
}
