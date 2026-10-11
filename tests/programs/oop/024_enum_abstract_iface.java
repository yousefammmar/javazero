import java.util.*;
import java.util.function.*;

public class Main {
    interface Priced { int price(); default String label() { return "$" + price(); } }
    enum Op implements IntBinaryOperator {
        ADD("+") { public int applyAsInt(int a, int b) { return a + b; } },
        SUB("-") { public int applyAsInt(int a, int b) { return a - b; } },
        MUL("*") { public int applyAsInt(int a, int b) { return a * b; } },
        DIV("/") {
            public int applyAsInt(int a, int b) { return a / b; }
            @Override boolean safe() { return false; }
        };
        final String sym;
        Op(String s) { sym = s; }
        boolean safe() { return true; }
        static Op fromSymbol(String s) {
            for (Op o : values()) if (o.sym.equals(s)) return o;
            throw new NoSuchElementException("no op " + s);
        }
    }
    enum Coffee implements Priced {
        ESPRESSO(200), LATTE(350) { @Override public String label() { return "special " + super.label(); } }, MOCHA(400);
        private final int p;
        Coffee(int p) { this.p = p; }
        public int price() { return p; }
    }
    enum Shape {
        CIRCLE { double area(double s) { return Math.PI * s * s; } },
        SQUARE { double area(double s) { return s * s; } };
        abstract double area(double s);
    }
    public static void main(String[] args) {
        for (Op o : Op.values()) System.out.println(o + " " + o.sym + " " + o.applyAsInt(12, 4) + " safe=" + o.safe() + " " + o.getClass().equals(Op.class) + " " + o.getDeclaringClass().equals(Op.class));
        System.out.println(Op.fromSymbol("*").applyAsInt(6, 7));
        try { Op.fromSymbol("%"); } catch (NoSuchElementException e) { System.out.println(e.getMessage()); }
        try { Op.DIV.applyAsInt(1, 0); } catch (ArithmeticException e) { System.out.println("AE " + e.getMessage()); }
        IntBinaryOperator f = Op.SUB;
        System.out.println(f.applyAsInt(1, 10));
        for (Coffee c : Coffee.values()) System.out.println(c + " " + c.label() + " " + c.price());
        Priced pr = Coffee.MOCHA;
        System.out.println(pr.label() + " " + (pr instanceof Coffee) + " " + (pr instanceof Enum<?>));
        for (Shape s : Shape.values()) System.out.printf("%s %.3f%n", s, s.area(2));
        int total = 0;
        for (Coffee c : Coffee.values()) total += c.price();
        System.out.println(total);
        Map<Op, Integer> res = new EnumMap<>(Op.class);
        for (Op o : Op.values()) res.put(o, o.applyAsInt(20, 5));
        System.out.println(res);
        System.out.println(Op.valueOf("ADD").compareTo(Op.DIV) + " " + Coffee.valueOf("LATTE").ordinal());
        switch (Coffee.LATTE) {
            case ESPRESSO: System.out.println("e");
            case LATTE: System.out.println("l");
            case MOCHA: System.out.println("m (fallthrough)"); break;
        }
    }
}
