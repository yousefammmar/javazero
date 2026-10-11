import java.util.*;

public class Main {
    interface Pricing { double price(double base); }
    static class Regular implements Pricing { public double price(double b) { return b; } }
    static class Sale implements Pricing {
        final double pct; Sale(double p) { pct = p; }
        public double price(double b) { return b * (1 - pct / 100); }
    }
    static class Cart {
        private Pricing strat = new Regular();
        final List<Double> items = new ArrayList<>();
        void setStrategy(Pricing p) { strat = p; }
        double total() { double t = 0; for (double d : items) t += strat.price(d); return t; }
    }
    interface Text { String render(); }
    static class Plain implements Text {
        final String s; Plain(String s) { this.s = s; }
        public String render() { return s; }
    }
    static abstract class Deco implements Text {
        protected final Text inner;
        Deco(Text t) { inner = t; }
    }
    static class Bold extends Deco { Bold(Text t) { super(t); } public String render() { return "<b>" + inner.render() + "</b>"; } }
    static class Italic extends Deco { Italic(Text t) { super(t); } public String render() { return "<i>" + inner.render() + "</i>"; } }
    static class Upper extends Deco { Upper(Text t) { super(t); } public String render() { return inner.render().toUpperCase(); } }
    static class Counting extends Deco {
        static int calls;
        Counting(Text t) { super(t); }
        public String render() { calls++; return inner.render(); }
    }
    public static void main(String[] args) {
        Cart c = new Cart();
        c.items.addAll(List.of(10.0, 20.0, 30.5));
        System.out.println(c.total());
        c.setStrategy(new Sale(50));
        System.out.println(c.total());
        c.setStrategy(b -> b > 15 ? b - 5 : b);
        System.out.println(c.total());
        c.setStrategy(new Pricing() { int n; public double price(double b) { return b + (++n); } });
        System.out.println(c.total() + " " + c.total());
        Text t = new Plain("hello");
        System.out.println(t.render());
        System.out.println(new Bold(t).render());
        System.out.println(new Italic(new Bold(t)).render());
        System.out.println(new Bold(new Upper(new Italic(t))).render());
        Text counted = new Counting(new Counting(new Bold(t)));
        counted.render(); counted.render();
        System.out.println(Counting.calls + " " + counted.render());
        System.out.println((counted instanceof Deco) + " " + (t instanceof Deco));
    }
}
