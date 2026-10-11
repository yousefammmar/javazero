import java.util.*;
public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int lines = 0, words = 0, chars = 0;
        Map<String, Integer> freq = new TreeMap<>();
        while (sc.hasNextLine()) {
            String line = sc.nextLine();
            lines++;
            chars += line.length();
            for (String w : line.trim().split("\\s+")) {
                if (w.isEmpty()) continue;
                words++;
                String k = w.toLowerCase().replaceAll("[^a-z']", "");
                if (!k.isEmpty()) freq.merge(k, 1, Integer::sum);
            }
        }
        System.out.println("lines=" + lines + " words=" + words + " chars=" + chars);
        for (Map.Entry<String, Integer> e : freq.entrySet())
            System.out.println(e.getKey() + ": " + e.getValue());
    }
}
