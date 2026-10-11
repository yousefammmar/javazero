import java.io.*;
public class Main {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int total = 0;
        String line;
        while ((line = br.readLine()) != null) {
            System.out.println("parsing [" + line + "]");
            total += Integer.parseInt(line.trim());
            System.out.println("total " + total);
        }
        System.out.println("never printed");
    }
}
