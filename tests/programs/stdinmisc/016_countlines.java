import java.util.Scanner;
public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = Integer.parseInt(sc.nextLine().trim());
        String longest = "";
        for (int i = 0; i < n; i++) {
            String s = sc.nextLine();
            System.out.println(i + ": [" + s + "] len=" + s.length() + " upper=" + s.toUpperCase());
            if (s.length() > longest.length()) longest = s;
        }
        System.out.println("longest=[" + longest + "]");
        System.out.println("more=" + sc.hasNextLine());
        while (sc.hasNextLine()) System.out.println("extra: " + sc.nextLine());
    }
}
