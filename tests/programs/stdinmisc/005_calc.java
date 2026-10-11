import java.util.Scanner;
public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        while (sc.hasNextLine()) {
            String line = sc.nextLine().trim();
            if (line.isEmpty()) continue;
            String[] p = line.split(" ");
            if (p.length != 3) { System.out.println("Bad line: " + line); continue; }
            double a = Double.parseDouble(p[0]);
            double b = Double.parseDouble(p[2]);
            String op = p[1];
            switch (op) {
                case "+": System.out.println(a + b); break;
                case "-": System.out.println(a - b); break;
                case "*": System.out.println(a * b); break;
                case "/":
                    if (b == 0) System.out.println("Division by zero"); else System.out.println(a / b);
                    break;
                case "%": System.out.println(a % b); break;
                default: System.out.println("Unknown op " + op);
            }
        }
    }
}
