public class Main {
    static void show(Object o) { System.out.println(o + " : " + o.getClass().getSimpleName()); }
    public static void main(String[] args) {
        boolean t = true, f = false;
        System.out.println(true ? 1 : 2.0);
        System.out.println(false ? 1 : 2.0);
        show(t ? 1 : 2.0);
        show(t ? 1 : 'a');
        show(t ? 'b' : 0);
        int i = 98;
        show(t ? 'b' : i);
        show(t ? (short) 1 : 'a');
        show(t ? (byte) 1 : (short) 2);
        show(t ? 1L : 2);
        show(t ? 1 : 2L);
        show(t ? 1f : 2L);
        show(t ? 1 : "s");
        show(f ? 1 : "s");
        show(t ? 'a' : "s");
        show(t ? Integer.valueOf(5) : Double.valueOf(2));
        show(t ? Integer.valueOf(5) : "x");
        Integer in = 7;
        Double db = 2.5;
        Object o = t ? in : db;
        show(o);
        Object o2 = f ? in : db;
        show(o2);
        Object o3 = t ? (Object) in : db;
        show(o3);
        long l = t ? 1 : 2L;
        System.out.println(l);
        System.out.println(t ? null : 0);
        System.out.println(t ? 'x' : 1.5);
        System.out.println(t ? 10 : 'x');
        System.out.println(f ? 10 : 'x');
        System.out.println(f ? 100000 : 'x');
        char c = 'q';
        System.out.println(t ? c : 0);
        System.out.println(t ? c : 70000);
        System.out.println(t ? c : i);
        byte b = 5;
        System.out.println(t ? b : 'a');
        short s = 9;
        show(t ? b : s);
        show(t ? s : 'a');
        String str = t ? "yes" : "no";
        System.out.println(str);
        String nul = null;
        System.out.println("v=" + (nul == null ? "none" : nul));
        int x = 5;
        System.out.println(x > 3 ? x < 10 ? "mid" : "high" : "low");
        System.out.println(x > 30 ? "a" : x > 20 ? "b" : x > 4 ? "c" : "d");
        System.out.println((x > 3 ? 1 : 0) + (x > 4 ? 10 : 0));
    }
}
