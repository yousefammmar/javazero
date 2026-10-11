import java.util.*;

class PackageHelper {
    int pkgField = 1;
    protected int protField = 2;
    private int privField = 3;
    public int pubField = 4;
    int pkgMethod() { return 10; }
    protected int protMethod() { return 20; }
    private int privMethod() { return 30; }
    int callPriv() { return privMethod() + privField; }
    static String ping() { return "ping"; }
}

class Sub extends PackageHelper {
    int total() { return pkgField + protField + pubField + pkgMethod() + protMethod(); }
    private int privField = 300;
    int ownPriv() { return privField; }
}

public class Main {
    private int secret = 42;
    private static int sCount;
    static class Peek {
        int look(Main m) { return m.secret + sCount; }
    }
    private void hidden() { System.out.println("hidden called"); }
    class Spy { void act() { hidden(); secret++; } }
    public static void main(String[] args) {
        PackageHelper h = new PackageHelper();
        System.out.println(h.pkgField + " " + h.protField + " " + h.pubField + " " + h.pkgMethod() + " " + h.protMethod() + " " + h.callPriv());
        Sub s = new Sub();
        System.out.println(s.total() + " " + s.ownPriv() + " " + s.callPriv());
        PackageHelper up = s;
        System.out.println(up.pkgField + " " + ((Sub) up).ownPriv());
        Main m = new Main();
        sCount = 5;
        System.out.println(new Peek().look(m));
        m.new Spy().act();
        System.out.println(m.secret + " " + new Peek().look(m));
        m.hidden();
        System.out.println(PackageHelper.ping());
        Main other = new Main();
        other.secret = 7;
        System.out.println(m.secret + other.secret);
    }
}
