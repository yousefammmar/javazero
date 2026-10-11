public class Main {
    interface Walker { default String move() { return "walk"; } default String id() { return "W"; } }
    interface Swimmer { default String move() { return "swim"; } default String id() { return "S"; } }
    interface Flyer extends Walker { default String move() { return "fly+" + Walker.super.move(); } }
    static class Duck implements Walker, Swimmer {
        public String move() { return Walker.super.move() + "&" + Swimmer.super.move(); }
        public String id() { return "D" + Walker.super.id() + Swimmer.super.id(); }
    }
    static class Bird implements Flyer {}
    static class Penguin implements Swimmer, Walker {
        public String move() { return Swimmer.super.move(); }
        public String id() { return Walker.super.id(); }
    }
    static class Base { public String move() { return "base-move"; } }
    static class Hybrid extends Base implements Walker { }
    interface A { default String hi() { return "A"; } }
    interface B extends A { default String hi() { return "B>" + A.super.hi(); } }
    interface C extends A { }
    static class BC implements B, C { }
    public static void main(String[] args) {
        System.out.println(new Duck().move() + " " + new Duck().id());
        System.out.println(new Bird().move() + " " + new Bird().id());
        System.out.println(new Penguin().move() + " " + new Penguin().id());
        System.out.println(new Hybrid().move() + " " + new Hybrid().id());
        System.out.println(new BC().hi());
        Walker w = new Duck();
        Swimmer s = new Duck();
        System.out.println(w.move() + " " + s.move());
        Walker f = new Bird();
        System.out.println(f.move());
        Walker lam = new Walker() { public String id() { return "anon"; } };
        System.out.println(lam.move() + lam.id());
    }
}
