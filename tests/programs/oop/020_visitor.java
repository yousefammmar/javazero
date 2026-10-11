import java.util.*;

public class Main {
    interface Visitor<R> { R num(Num n); R add(Add a); R mul(Mul m); R neg(Neg n); }
    interface Expr { <R> R accept(Visitor<R> v); }
    static class Num implements Expr { final int v; Num(int v) { this.v = v; } public <R> R accept(Visitor<R> vis) { return vis.num(this); } }
    static class Add implements Expr { final Expr l, r; Add(Expr l, Expr r) { this.l = l; this.r = r; } public <R> R accept(Visitor<R> v) { return v.add(this); } }
    static class Mul implements Expr { final Expr l, r; Mul(Expr l, Expr r) { this.l = l; this.r = r; } public <R> R accept(Visitor<R> v) { return v.mul(this); } }
    static class Neg implements Expr { final Expr e; Neg(Expr e) { this.e = e; } public <R> R accept(Visitor<R> v) { return v.neg(this); } }
    static class Eval implements Visitor<Integer> {
        public Integer num(Num n) { return n.v; }
        public Integer add(Add a) { return a.l.accept(this) + a.r.accept(this); }
        public Integer mul(Mul m) { return m.l.accept(this) * m.r.accept(this); }
        public Integer neg(Neg n) { return -n.e.accept(this); }
    }
    static class Show implements Visitor<String> {
        public String num(Num n) { return Integer.toString(n.v); }
        public String add(Add a) { return "(" + a.l.accept(this) + " + " + a.r.accept(this) + ")"; }
        public String mul(Mul m) { return a(m.l) + " * " + a(m.r); }
        private String a(Expr e) { return e.accept(this); }
        public String neg(Neg n) { return "-" + n.e.accept(this); }
    }
    static class Depth implements Visitor<Integer> {
        public Integer num(Num n) { return 1; }
        public Integer add(Add a) { return 1 + Math.max(a.l.accept(this), a.r.accept(this)); }
        public Integer mul(Mul m) { return 1 + Math.max(m.l.accept(this), m.r.accept(this)); }
        public Integer neg(Neg n) { return 1 + n.e.accept(this); }
    }
    static class Counter implements Visitor<Void> {
        Map<String, Integer> counts = new TreeMap<>();
        void bump(String k) { counts.merge(k, 1, Integer::sum); }
        public Void num(Num n) { bump("num"); return null; }
        public Void add(Add a) { bump("add"); a.l.accept(this); a.r.accept(this); return null; }
        public Void mul(Mul m) { bump("mul"); m.l.accept(this); m.r.accept(this); return null; }
        public Void neg(Neg n) { bump("neg"); n.e.accept(this); return null; }
    }
    public static void main(String[] args) {
        Expr e = new Add(new Mul(new Num(3), new Num(4)), new Neg(new Add(new Num(1), new Num(2))));
        System.out.println(e.accept(new Show()));
        System.out.println(e.accept(new Eval()));
        System.out.println(e.accept(new Depth()));
        Counter c = new Counter();
        e.accept(c);
        System.out.println(c.counts);
        Expr big = new Num(1);
        for (int i = 2; i <= 6; i++) big = new Mul(big, new Add(new Num(i), new Neg(new Num(1))));
        System.out.println(big.accept(new Show()));
        System.out.println(big.accept(new Eval()) + " depth " + big.accept(new Depth()));
    }
}
