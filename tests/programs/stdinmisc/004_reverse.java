import java.io.*;
public class Main {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String line;
        int n = 0;
        while ((line = br.readLine()) != null) {
            n++;
            System.out.println(n + ": " + new StringBuilder(line).reverse() + "|");
        }
        System.out.println("total " + n);
    }
}
