public class Main {
    static class Widget {
        protected String title = "base-title";
        Widget() {
            System.out.println("Widget ctor: describe=" + describe());
            init();
        }
        String describe() { return "Widget:" + title; }
        void init() { System.out.println("Widget.init"); }
    }
    static class Button extends Widget {
        private String label = "OK";
        private int clicks;
        private final int size = 12;
        private final int dyn;
        { dyn = 99; }
        Button() {
            super();
            System.out.println("Button ctor: label=" + label + " clicks=" + clicks + " size=" + size + " dyn=" + dyn);
        }
        @Override String describe() { return "Button:" + label + ":" + clicks + ":" + size + ":" + dyn; }
        @Override void init() { clicks += 5; System.out.println("Button.init clicks=" + clicks); }
    }
    static class Registry {
        static java.util.List<String> seen = new java.util.ArrayList<>();
        Registry() { seen.add("registered " + this); }
        public String toString() { return "Registry-" + getClass().getSimpleName(); }
    }
    static class SubRegistry extends Registry {
        String s = "x";
        public String toString() { return "Sub(" + s + ")"; }
    }
    public static void main(String[] args) {
        Widget w = new Button();
        System.out.println(w.describe());
        new Registry();
        new SubRegistry();
        for (String s : Registry.seen) System.out.println(s);
    }
}
