public class Main {
    static String day(String d) {
        switch (d) {
            case "sat":
            case "sun":
                return "weekend";
            case "mon":
                return "ugh";
            default:
                return "weekday";
        }
    }
    static int fall(int n) {
        int r = 0;
        switch (n) {
            case 1: r += 1;
            case 2: r += 10;
            case 3: r += 100; break;
            case 4: r += 1000;
            default: r += 5;
            case 5: r += 50;
        }
        return r;
    }
    static String grade(int score) {
        if (score >= 90) return "A";
        else if (score >= 80) return "B";
        else if (score >= 70) return "C";
        else if (score >= 60) return "D";
        else return "F";
    }
    static String cls(char c) {
        switch (c) {
            case 'a': case 'e': case 'i': case 'o': case 'u': return "vowel";
            case ' ': return "space";
            case '0': case '1': case '2': return "low digit";
            default:
                if (c >= 'a' && c <= 'z') return "consonant";
                return "other";
        }
    }
    public static void main(String[] args) {
        for (String d : new String[]{"mon", "tue", "sat", "sun", "MON", ""}) System.out.println(d + " -> " + day(d));
        for (int i = 0; i <= 6; i++) System.out.println("fall(" + i + ")=" + fall(i));
        for (int s : new int[]{100, 90, 89, 80, 79, 70, 69, 60, 59, 0, -5}) System.out.print(grade(s) + " ");
        System.out.println();
        for (char c : "ab z1 9!".toCharArray()) System.out.println("'" + c + "' " + cls(c));
        int x = 7;
        if (x > 5)
            if (x > 10) System.out.println("big");
            else System.out.println("medium");
        if (x % 2 == 0) { System.out.println("even"); } else { System.out.println("odd"); }
        byte b = 3;
        switch (b) { case 3: System.out.println("byte three"); break; default: System.out.println("no"); }
        Integer boxed = 2;
        switch (boxed) { case 1 -> System.out.println("one"); case 2 -> System.out.println("two"); default -> System.out.println("many"); }
        String sw = "b";
        switch (sw) {
            case "a" -> System.out.println("A");
            case "b", "c" -> { System.out.println("B or C"); }
            default -> System.out.println("?");
        }
        switch (x) { }
        switch (x) { default: System.out.println("only default"); }
        final int K = 3;
        switch (x - 4) { case K: System.out.println("K matched"); break; case K + 1: System.out.println("K+1"); }
        switch ("Aa".hashCode() == "BB".hashCode() ? "BB" : "none") {
            case "Aa": System.out.println("Aa"); break;
            case "BB": System.out.println("BB"); break;
        }
        String ss = "Aa";
        switch (ss) { case "BB": System.out.println("BB"); break; case "Aa": System.out.println("Aa again"); break; }
        int total = 0;
        for (int i = 0; i < 10; i++) {
            switch (i % 3) {
                case 0: continue;
                case 1: total += i; break;
                default: total += 100;
            }
            total++;
        }
        System.out.println(total);
    }
}
