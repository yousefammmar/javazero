public class Main {
    static String f(int x) { return "int"; }
    static String f(long x) { return "long"; }
    static String f(double x) { return "double"; }
    static String f(Integer x) { return "Integer"; }
    static String f(Object x) { return "Object"; }
    static String f(char x) { return "char"; }
    static String g(long x) { return "g-long"; }
    static String g(Integer x) { return "g-Integer"; }
    static String h(double x) { return "h-double"; }
    static String h(Object x) { return "h-Object"; }
    static String k(Object x) { return "k-Object"; }
    static String k(String x) { return "k-String"; }
    static String k(Integer x) { return "k-Integer"; }
    static String m(long a, int b) { return "long,int"; }
    static String m(int a, long b) { return "int,long"; }
    static String n(float x) { return "n-float"; }
    static String n(double x) { return "n-double"; }
    static String p(short x) { return "p-short"; }
    static String p(int x) { return "p-int"; }
    static String r(byte b) { return "r-byte"; }
    static String r(int b) { return "r-int"; }
    static String s(StringBuilder sb) { return "s-SB"; }
    static String s(CharSequence cs) { return "s-CS"; }
    static String t(int... x) { return "t-varargs"; }
    static String t(long x) { return "t-long"; }
    static String u(Object o) { return "u-Object"; }
    static String u(int[] o) { return "u-int[]"; }
    static String v(double d) { return "v-double"; }
    static String v(float d) { return "v-float"; }
    static String v(long d) { return "v-long"; }
    public static void main(String[] args) {
        byte b = 1;
        short sh = 2;
        char c = 'c';
        int i = 3;
        long l = 4;
        float fl = 5f;
        double d = 6;
        System.out.println(f(b) + " " + f(sh) + " " + f(c) + " " + f(i) + " " + f(l) + " " + f(fl) + " " + f(d));
        System.out.println(f(Integer.valueOf(1)) + " " + f(Long.valueOf(1)) + " " + f("s") + " " + f(true) + " " + f((Object) 1) + " " + f(null == null ? (Object) "x" : 1));
        System.out.println(f('a' + 1) + " " + f((char) ('a' + 1)) + " " + f(1 + 1L) + " " + f(1 + 1.0f) + " " + f(c + c));
        System.out.println(g(i) + " " + g(Integer.valueOf(i)));
        System.out.println(h(i) + " " + h(Integer.valueOf(i)) + " " + h('x') + " " + h(1L));
        System.out.println(k("s") + " " + k(1) + " " + k(1.5) + " " + k((Object) "s") + " " + k('c'));
        System.out.println(m(1L, 2) + " " + m(1, 2L));
        System.out.println(n(1) + " " + n(1L) + " " + n(1f) + " " + n(1.0) + " " + n('a'));
        System.out.println(p(sh) + " " + p(b) + " " + p(c) + " " + p((short) 1) + " " + p(1));
        System.out.println(r(b) + " " + r(sh) + " " + r((byte) 3) + " " + r('c'));

        System.out.println(s(new StringBuilder()) + " " + s("str") + " " + s((CharSequence) new StringBuilder()));
        System.out.println(t(1) + " " + t() + " " + t(1, 2) + " " + t(new int[]{1}) + " " + t(1L));
        System.out.println(u(new int[0]) + " " + u((Object) new int[0]) + " " + u("s") + " " + u(null));
        System.out.println(v(1) + " " + v(1L) + " " + v(1f) + " " + v(1.0) + " " + v('a'));
    }
}
