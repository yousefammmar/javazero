public class Main {
    static class Counter {
        static int total = 0;
        static int created;
        int id;
        int mine;
        Counter() { created++; id = created; }
        void inc() { mine++; total++; }
        static int getTotal() { return total; }
        static Counter make() { return new Counter(); }
    }
    public static void main(String[] args) {
        Counter a = new Counter();
        Counter b = Counter.make();
        Counter c = new Counter();
        a.inc(); a.inc(); b.inc(); c.inc(); c.inc(); c.inc();
        System.out.println("a.mine=" + a.mine + " b.mine=" + b.mine + " c.mine=" + c.mine);
        System.out.println("total=" + Counter.total + " getTotal=" + Counter.getTotal());
        System.out.println("created=" + Counter.created);
        System.out.println("ids " + a.id + " " + b.id + " " + c.id);
        Counter n = null;
        System.out.println("static via null ref: " + n.getTotal());
        System.out.println("static field via null ref: " + n.total);
        Counter.total = 100;
        System.out.println("via instance: " + a.total + " " + b.total);
        a.total++;
        System.out.println("after a.total++: " + c.total);
        int[] arr = new int[3];
        for (int i = 0; i < 3; i++) arr[i] = Counter.make().id;
        System.out.println(arr[0] + "," + arr[1] + "," + arr[2]);
        System.out.println("created=" + Counter.created);
        Counter x = a, y = a, z = new Counter();
        System.out.println((x == y) + " " + (x == z) + " " + x.equals(y) + " " + x.equals(z));
    }
}
