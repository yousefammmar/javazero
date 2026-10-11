import java.util.*;

public class Main {
    static abstract class Report {
        private final String title;
        protected final List<String> lines = new ArrayList<>();
        Report(String title) { this.title = title; System.out.println("Report ctor " + title + " rows=" + rowCount()); }
        abstract int rowCount();
        abstract String row(int i);
        protected String header() { return "== " + title + " =="; }
        protected String footer() { return "-- end --"; }
        public final String render() {
            StringBuilder sb = new StringBuilder(header()).append('\n');
            for (int i = 0; i < rowCount(); i++) sb.append(i).append(": ").append(row(i)).append('\n');
            return sb.append(footer()).toString();
        }
    }
    static class Squares extends Report {
        int n;
        Squares(int n) { super("Squares"); this.n = n; }
        int rowCount() { return n; }
        String row(int i) { return String.valueOf(i * i); }
    }
    static class Words extends Report {
        final String[] ws;
        Words(String... ws) { super("Words"); this.ws = ws; }
        int rowCount() { return ws == null ? -1 : ws.length; }
        String row(int i) { return ws[i].toUpperCase(); }
        @Override protected String footer() { return "(" + ws.length + " words)"; }
    }
    public static void main(String[] args) {
        Report r1 = new Squares(4);
        System.out.println(r1.render());
        Report r2 = new Words("a", "bc", "def");
        System.out.println(r2.render());
        Report anon = new Report("Anon") {
            int rowCount() { return 2; }
            String row(int i) { return "anon-" + i; }
            @Override protected String header() { return "<<" + super.header() + ">>"; }
        };
        System.out.println(anon.render());
        System.out.println(anon.getClass().getSimpleName().isEmpty());
        System.out.println(r1 instanceof Report);
    }
}
