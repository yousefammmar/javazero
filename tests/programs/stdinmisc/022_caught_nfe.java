import java.io.*;
public class Main {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String line;
        int good = 0, bad = 0;
        while ((line = br.readLine()) != null) {
            try {
                int v = Integer.parseInt(line);
                System.out.println("ok " + v);
                good++;
            } catch (NumberFormatException e) {
                System.out.println("bad: " + e.getMessage());
                bad++;
            }
        }
        System.out.println(good + " good, " + bad + " bad");
        try { Double.parseDouble("1.2.3"); } catch (NumberFormatException e) { System.out.println(e.getMessage()); }
        try { Long.parseLong("99999999999999999999"); } catch (NumberFormatException e) { System.out.println(e.getMessage()); }
    }
}
