import java.io.*;
public class Main {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int n = Integer.parseInt(br.readLine().trim());
        for (int i = 0; i < n; i++) {
            String s = br.readLine();
            int v = 0, c = 0, d = 0, sp = 0;
            for (char ch : s.toCharArray()) {
                if ("aeiouAEIOU".indexOf(ch) >= 0) v++;
                else if (Character.isLetter(ch)) c++;
                else if (Character.isDigit(ch)) d++;
                else if (Character.isWhitespace(ch)) sp++;
            }
            System.out.println(s + " -> v=" + v + " c=" + c + " d=" + d + " sp=" + sp);
        }
    }
}
