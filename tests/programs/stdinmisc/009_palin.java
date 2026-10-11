import java.io.*;
public class Main {
    static boolean pal(String s) {
        String t = s.toLowerCase().replaceAll("[^a-z0-9]", "");
        int i = 0, j = t.length() - 1;
        while (i < j) if (t.charAt(i++) != t.charAt(j--)) return false;
        return true;
    }
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String line;
        while ((line = br.readLine()) != null) {
            System.out.println("\"" + line + "\" -> " + (pal(line) ? "palindrome" : "not palindrome"));
        }
    }
}
