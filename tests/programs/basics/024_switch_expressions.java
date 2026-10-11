public class Main {
    enum Color { RED, GREEN, BLUE }
    static String arrow(int n) {
        return switch (n) {
            case 1 -> "one";
            case 2, 3 -> "two-three";
            case 4 -> { String t = "fo"; yield t + "ur"; }
            default -> "many:" + n;
        };
    }
    static int oldYield(int n) {
        return switch (n) {
            case 1:
            case 2:
                yield 10;
            case 3:
                int tmp = n * 7;
                yield tmp;
            default:
                yield -1;
        };
    }
    static String color(Color c) {
        return switch (c) {
            case RED -> "r";
            case GREEN -> "g";
            case BLUE -> "b";
        };
    }
    static int days(String m) {
        return switch (m) {
            case "feb" -> 28;
            case "apr", "jun", "sep", "nov" -> 30;
            default -> 31;
        };
    }
    static String charKind(char c) {
        return switch (c) {
            case 'a', 'e', 'i', 'o', 'u' -> "vowel";
            case ' ' -> "space";
            default -> Character.isDigit(c) ? "digit" : "other";
        };
    }
    static double mixed(int k) {
        return switch (k) {
            case 0 -> 1;
            case 1 -> 2.5;
            default -> 'a';
        };
    }
    static String nested(int a, int b) {
        return switch (a) {
            case 0 -> switch (b) { case 0 -> "00"; default -> "0x"; };
            default -> {
                if (b > 5) yield "big";
                yield "x" + b;
            }
        };
    }
    public static void main(String[] args) {
        for (int i = 0; i <= 5; i++) System.out.println(i + " " + arrow(i) + " " + oldYield(i));
        for (Color c : Color.values()) System.out.println(c + " " + color(c) + " " + c.ordinal());
        for (String m : new String[]{"feb", "apr", "jan", "nov", "x"}) System.out.print(days(m) + " ");
        System.out.println();
        for (char c : "a 7z".toCharArray()) System.out.print(charKind(c) + ",");
        System.out.println();
        System.out.println(mixed(0) + " " + mixed(1) + " " + mixed(2));
        System.out.println(nested(0, 0) + nested(0, 1) + nested(1, 9) + nested(1, 2));
        int x = 3;
        switch (x) {
            case 1 -> System.out.println("one");
            case 3 -> {
                System.out.println("three");
                System.out.println("block");
            }
            default -> System.out.println("dflt");
        }
        String res = switch (x % 2) { case 0 -> "even"; default -> "odd"; };
        System.out.println(res);
        int v = switch (x) { case 3 -> { int t = x * x; yield t + 1; } default -> 0; };
        System.out.println(v);
        System.out.println(switch (x) { case 3 -> 'c'; default -> 'd'; });
        System.out.println(switch (x) { case 3 -> 1; default -> 2.0; });
        Color cc = Color.GREEN;
        switch (cc) {
            case RED: System.out.println("is red");
            case GREEN: System.out.println("is green");
            case BLUE: System.out.println("falls to blue"); break;
        }
        var obj = switch (cc) { case RED -> 1; case GREEN -> "gs"; case BLUE -> 3.0; };
        System.out.println(obj + " " + obj.getClass().getSimpleName());
        long lg = switch ("k") { case "k" -> 5; default -> 6L; };
        System.out.println(lg);
        Integer boxed = 2;
        String bs = switch (boxed) { case 1 -> "a"; case 2 -> "b"; default -> "c"; };
        System.out.println(bs);
        int total = 0;
        for (int i = 0; i < 6; i++) total += switch (i % 3) { case 0 -> 1; case 1 -> 10; default -> 100; };
        System.out.println(total);
        String t = switch (Color.valueOf("BLUE")) { case RED, GREEN -> "warm-ish"; case BLUE -> "cool"; };
        System.out.println(t);
    }
}
