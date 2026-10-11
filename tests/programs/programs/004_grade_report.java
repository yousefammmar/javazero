import java.util.*;

public class Main {
    static String letter(double avg) {
        if (avg >= 90) return "A";
        else if (avg >= 80) return "B";
        else if (avg >= 70) return "C";
        else if (avg >= 60) return "D";
        return "F";
    }

    public static void main(String[] args) {
        String[] names = {"Alice", "Bob", "Charlie", "Diana", "Eduardo", "Fiona"};
        int[][] scores = {
            {92, 88, 95, 100},
            {75, 64, 80, 71},
            {55, 62, 48, 70},
            {83, 91, 79, 88},
            {100, 97, 99, 98},
            {68, 72, 65, 59}
        };
        System.out.printf("%-10s|%5s%5s%5s%5s |%8s|%6s%n", "Name", "T1", "T2", "T3", "T4", "Average", "Grade");
        System.out.println("-".repeat(10) + "+" + "-".repeat(20) + "-+" + "-".repeat(8) + "+" + "-".repeat(6));
        double[] avgs = new double[names.length];
        int[] colMax = new int[4];
        int[] colMin = {1000, 1000, 1000, 1000};
        double[] colSum = new double[4];
        for (int i = 0; i < names.length; i++) {
            int sum = 0;
            System.out.printf("%-10s|", names[i]);
            for (int j = 0; j < 4; j++) {
                System.out.printf("%5d", scores[i][j]);
                sum += scores[i][j];
                colMax[j] = Math.max(colMax[j], scores[i][j]);
                colMin[j] = Math.min(colMin[j], scores[i][j]);
                colSum[j] += scores[i][j];
            }
            avgs[i] = sum / 4.0;
            System.out.printf(" |%8.2f|%6s%n", avgs[i], letter(avgs[i]));
        }
        System.out.println();
        System.out.printf("%-10s|", "Max");
        for (int v : colMax) System.out.printf("%5d", v);
        System.out.println();
        System.out.printf("%-10s|", "Min");
        for (int v : colMin) System.out.printf("%5d", v);
        System.out.println();
        System.out.printf("%-10s|", "Mean");
        for (double v : colSum) System.out.printf("%5.1f", v / names.length);
        System.out.println();

        double total = 0;
        for (double a : avgs) total += a;
        double mean = total / avgs.length;
        double var = 0;
        for (double a : avgs) var += (a - mean) * (a - mean);
        System.out.printf("Class mean %.3f, stddev %.3f%n", mean, Math.sqrt(var / avgs.length));
        Map<String, List<String>> byGrade = new TreeMap<>();
        for (int i = 0; i < names.length; i++) {
            byGrade.computeIfAbsent(letter(avgs[i]), k -> new ArrayList<>()).add(names[i]);
        }
        for (var e : byGrade.entrySet()) System.out.println(e.getKey() + ": " + e.getValue());
        int best = 0;
        for (int i = 1; i < avgs.length; i++) if (avgs[i] > avgs[best]) best = i;
        System.out.println("Top student: " + names[best] + " (" + avgs[best] + ")");
        System.out.println(String.format("%08.3f|%+d|%x|%o|%e|%10s|%-10s|", 3.14159, 42, 255, 8, 12345.678, "right", "left"));
    }
}
