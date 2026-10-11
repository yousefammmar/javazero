public class Main {
    static void triangle(int n) {
        for (int i = 1; i <= n; i++) System.out.println(" ".repeat(n - i) + "*".repeat(2 * i - 1));
    }

    static void diamond(int n) {
        for (int i = 1; i <= n; i++) System.out.println(" ".repeat(n - i) + "*".repeat(2 * i - 1));
        for (int i = n - 1; i >= 1; i--) System.out.println(" ".repeat(n - i) + "*".repeat(2 * i - 1));
    }

    static void rightAligned(int n) {
        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= n; j++) System.out.print(j <= n - i ? ' ' : (char) ('0' + (n - j + 1) % 10));
            System.out.println();
        }
    }

    static void histogram(int[] data) {
        int max = 0;
        for (int d : data) max = Math.max(max, d);
        for (int level = max; level >= 1; level--) {
            StringBuilder sb = new StringBuilder(String.format("%2d |", level));
            for (int d : data) sb.append(d >= level ? " # " : "   ");
            System.out.println(sb);
        }
        System.out.println("   +" + "---".repeat(data.length));
        StringBuilder lbl = new StringBuilder("    ");
        for (int i = 0; i < data.length; i++) lbl.append(' ').append((char) ('A' + i)).append(' ');
        System.out.println(lbl);
    }

    static void box(String text) {
        String line = "+" + "-".repeat(text.length() + 2) + "+";
        System.out.println(line);
        System.out.println("| " + text + " |");
        System.out.println(line);
    }

    static void checker(int n) {
        for (int r = 0; r < n; r++) {
            StringBuilder sb = new StringBuilder();
            for (int c = 0; c < n; c++) sb.append((r + c) % 2 == 0 ? "##" : "  ");
            System.out.println(sb);
        }
    }

    static void multiplicationTable(int n) {
        System.out.print("   |");
        for (int i = 1; i <= n; i++) System.out.printf("%4d", i);
        System.out.println();
        System.out.println("---+" + "-".repeat(4 * n));
        for (int i = 1; i <= n; i++) {
            System.out.printf("%2d |", i);
            for (int j = 1; j <= n; j++) System.out.printf("%4d", i * j);
            System.out.println();
        }
    }

    public static void main(String[] args) {
        triangle(5);
        System.out.println();
        diamond(4);
        System.out.println();
        rightAligned(6);
        System.out.println();
        histogram(new int[]{3, 7, 1, 5, 4, 6, 2});
        System.out.println();
        box("Hello, Java!");
        checker(4);
        multiplicationTable(7);
        for (int i = 1; i <= 4; i++) {
            StringBuilder sb = new StringBuilder();
            for (int j = 0; j < i; j++) sb.append(j == 0 ? "" : ", ").append(i * (j + 1));
            System.out.println(sb);
        }
    }
}
