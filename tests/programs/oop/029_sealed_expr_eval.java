import java.util.*;

public class Main {
    sealed interface Expr permits Const, Add, Mul, Neg, Var {}
    record Const(int v) implements Expr {}
    record Add(Expr l, Expr r) implements Expr {}
    record Mul(Expr l, Expr r) implements Expr {}
    record Neg(Expr e) implements Expr {}
    record Var(String name) implements Expr {}
    static int eval(Expr e, Map<String, Integer> env) {
        return switch (e) {
            case Const c -> c.v();
            case Add a -> eval(a.l(), env) + eval(a.r(), env);
            case Mul m -> eval(m.l(), env) * eval(m.r(), env);
            case Neg n -> -eval(n.e(), env);
            case Var v -> {
                Integer x = env.get(v.name());
                if (x == null) throw new NoSuchElementException("unbound " + v.name());
                yield x;
            }
        };
    }
    static Expr simplify(Expr e) {
        return switch (e) {
            case Add(Const(int a), Const(int b)) -> new Const(a + b);
            case Add(Expr l, Const(int z)) when z == 0 -> simplify(l);
            case Add(Const(int z), Expr r) when z == 0 -> simplify(r);
            case Mul(Const(int a), Const(int b)) -> new Const(a * b);
            case Mul(Expr l, Const(int one)) when one == 1 -> simplify(l);
            case Mul(Expr l, Const(int z)) when z == 0 -> new Const(0);
            case Neg(Neg(Expr inner)) -> simplify(inner);
            case Neg(Const(int c)) -> new Const(-c);
            case Add a -> { Expr l = simplify(a.l()), r = simplify(a.r()); yield (l.equals(a.l()) && r.equals(a.r())) ? a : simplify(new Add(l, r)); }
            case Mul m -> { Expr l = simplify(m.l()), r = simplify(m.r()); yield (l.equals(m.l()) && r.equals(m.r())) ? m : simplify(new Mul(l, r)); }
            case Neg n -> new Neg(simplify(n.e()));
            default -> e;
        };
    }
    static String show(Expr e) {
        if (e instanceof Const c) return String.valueOf(c.v());
        if (e instanceof Var v) return v.name();
        if (e instanceof Add a) return "(" + show(a.l()) + " + " + show(a.r()) + ")";
        if (e instanceof Mul m) return show(m.l()) + "*" + show(m.r());
        if (e instanceof Neg(Expr inner)) return "-" + show(inner);
        throw new AssertionError();
    }
    public static void main(String[] args) {
        Map<String, Integer> env = Map.of("x", 5, "y", 7);
        Expr[] es = {
            new Add(new Const(1), new Const(2)),
            new Add(new Var("x"), new Const(0)),
            new Mul(new Add(new Const(0), new Var("y")), new Const(1)),
            new Neg(new Neg(new Var("x"))),
            new Mul(new Var("x"), new Const(0)),
            new Add(new Mul(new Const(2), new Const(3)), new Neg(new Const(4))),
            new Mul(new Add(new Var("x"), new Var("y")), new Neg(new Var("x"))),
        };
        for (Expr e : es) {
            Expr s = simplify(e);
            System.out.println(show(e) + " => " + show(s) + " = " + eval(s, env) + " (orig " + eval(e, env) + ")");
        }
        System.out.println(es[0]);
        System.out.println(simplify(es[0]));
        try { eval(new Var("zz"), env); } catch (NoSuchElementException ex) { System.out.println(ex.getMessage()); }
    }
}
