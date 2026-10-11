import java.io.*;
import java.util.*;
public class Main {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String header = br.readLine();
        String[] cols = header.split(",");
        System.out.println(Arrays.toString(cols));
        String line;
        double total = 0;
        int rows = 0;
        while ((line = br.readLine()) != null) {
            if (line.isBlank()) continue;
            String[] f = line.split(",", -1);
            System.out.println(f.length + " fields: " + Arrays.toString(f));
            if (!f[2].isEmpty()) total += Double.parseDouble(f[2]);
            rows++;
        }
        System.out.println("rows=" + rows + " total=" + total);
    }
}
