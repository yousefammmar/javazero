import java.util.*;
public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        sc.nextLine();
        String[] names = new String[n];
        double[] avg = new double[n];
        for (int i = 0; i < n; i++) {
            String line = sc.nextLine();
            String[] parts = line.split(",");
            names[i] = parts[0].trim();
            double s = 0;
            for (int j = 1; j < parts.length; j++) s += Integer.parseInt(parts[j].trim());
            avg[i] = s / (parts.length - 1);
        }
        for (int i = 0; i < n; i++) {
            char g = avg[i] >= 90 ? 'A' : avg[i] >= 80 ? 'B' : avg[i] >= 70 ? 'C' : 'F';
            System.out.printf("%-8s %6.2f %c%n", names[i], avg[i], g);
        }
        double best = -1; String bn = "";
        for (int i = 0; i < n; i++) if (avg[i] > best) { best = avg[i]; bn = names[i]; }
        System.out.println("Best: " + bn + " " + best);
    }
}
