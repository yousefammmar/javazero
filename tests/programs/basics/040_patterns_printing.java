public class Main {
    public static void main(String[] args) {
        for (int i = 1; i <= 15; i++) {
            if (i % 15 == 0) System.out.print("FizzBuzz");
            else if (i % 5 == 0) System.out.print("Buzz");
            else if (i % 3 == 0) System.out.print("Fizz");
            else System.out.print(i);
            System.out.print(i < 15 ? "," : "\n");
        }
        for (int i = 1; i <= 5; i++) System.out.println("*".repeat(i));
        for (int i = 5; i >= 1; i--) System.out.println(" ".repeat(5 - i) + "*".repeat(2 * i - 1));
        for (int i = 1; i <= 4; i++) {
            for (int j = 1; j <= 4; j++) System.out.print((i == j || i + j == 5) ? "X" : ".");
            System.out.println();
        }
        for (int i = 1; i <= 4; i++) {
            for (int j = 1; j <= i; j++) System.out.print(j);
            for (int j = i - 1; j >= 1; j--) System.out.print(j);
            System.out.println();
        }
        for (int i = 1; i <= 9; i++) {
            StringBuilder sb = new StringBuilder();
            for (int j = 1; j <= 9; j++) sb.append(String.format("%3d", i * j));
            if (i % 4 == 1) System.out.println(sb);
        }
        int n = 5;
        int[][] spiral = new int[n][n];
        int val = 1, top = 0, bot = n - 1, left = 0, right = n - 1;
        while (val <= n * n) {
            for (int j = left; j <= right; j++) spiral[top][j] = val++;
            top++;
            for (int j = top; j <= bot; j++) spiral[j][right] = val++;
            right--;
            for (int j = right; j >= left; j--) spiral[bot][j] = val++;
            bot--;
            for (int j = bot; j >= top; j--) spiral[j][left] = val++;
            left++;
        }
        for (int[] row : spiral) { for (int v : row) System.out.print(String.format("%3d", v)); System.out.println(); }
        for (char c = 'A'; c <= 'E'; c++) {
            for (char d = 'A'; d <= c; d++) System.out.print(d);
            System.out.println();
        }
        long a = 0, b = 1;
        StringBuilder fib = new StringBuilder();
        for (int i = 0; i < 20; i++) { fib.append(a).append(i < 19 ? " " : ""); long t = a + b; a = b; b = t; }
        System.out.println(fib);
        for (int i = 0; i <= 16; i += 4) System.out.print(Integer.toBinaryString(i) + "|" + Integer.toHexString(i * 17) + " ");
        System.out.println();
        String row = "";
        for (int i = 0; i < 3; i++) { row += i + ":"; for (int j = 0; j < 3; j++) row += (i * 3 + j) + (j < 2 ? "," : ";"); }
        System.out.println(row);
        int sumDiag = 0;
        for (int i = 0; i < n; i++) sumDiag += spiral[i][i] - spiral[i][n - 1 - i];
        System.out.println(sumDiag);
        for (int i = 0; i < 3; i++) {
            System.out.printf("%-4s|%4s|%n", "r" + i, "c" + i * i);
        }
        System.out.println("=".repeat(12));
        System.out.println(String.join(",", "a", "b").toUpperCase() + "\t|☃|");
    }
}
