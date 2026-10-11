import java.util.*;

public class Main {
    static boolean isPalindrome(String s) {
        String t = s.toLowerCase().replaceAll("[^a-z0-9]", "");
        return new StringBuilder(t).reverse().toString().equals(t);
    }

    static boolean anagrams(String a, String b) {
        char[] x = a.replace(" ", "").toLowerCase().toCharArray();
        char[] y = b.replace(" ", "").toLowerCase().toCharArray();
        Arrays.sort(x); Arrays.sort(y);
        return Arrays.equals(x, y);
    }

    static String caesar(String s, int shift) {
        StringBuilder sb = new StringBuilder();
        for (char c : s.toCharArray()) {
            if (Character.isUpperCase(c)) sb.append((char) ('A' + (c - 'A' + shift + 26) % 26));
            else if (Character.isLowerCase(c)) sb.append((char) ('a' + (c - 'a' + shift + 26) % 26));
            else sb.append(c);
        }
        return sb.toString();
    }

    static String vigenere(String text, String key, boolean enc) {
        StringBuilder sb = new StringBuilder();
        int k = 0;
        for (char c : text.toCharArray()) {
            if (Character.isLetter(c)) {
                int sh = key.charAt(k++ % key.length()) - 'a';
                if (!enc) sh = -sh;
                sb.append((char) ('a' + (c - 'a' + sh + 26) % 26));
            } else sb.append(c);
        }
        return sb.toString();
    }

    static String rle(String s) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < s.length(); ) {
            int j = i;
            while (j < s.length() && s.charAt(j) == s.charAt(i)) j++;
            sb.append(s.charAt(i)).append(j - i);
            i = j;
        }
        return sb.toString();
    }

    static String unrle(String s) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < s.length(); ) {
            char c = s.charAt(i++);
            int n = 0;
            while (i < s.length() && Character.isDigit(s.charAt(i))) n = n * 10 + (s.charAt(i++) - '0');
            sb.append(String.valueOf(c).repeat(n));
        }
        return sb.toString();
    }

    static String reverseWords(String s) {
        String[] w = s.trim().split("\\s+");
        Collections.reverse(Arrays.asList(w));
        return String.join(" ", w);
    }

    static int vowels(String s) {
        int n = 0;
        for (char c : s.toLowerCase().toCharArray()) if ("aeiou".indexOf(c) >= 0) n++;
        return n;
    }

    static String toSnake(String s) {
        StringBuilder sb = new StringBuilder();
        for (char c : s.toCharArray()) {
            if (Character.isUpperCase(c)) { if (sb.length() > 0) sb.append('_'); sb.append(Character.toLowerCase(c)); }
            else sb.append(c);
        }
        return sb.toString();
    }

    public static void main(String[] args) {
        for (String s : new String[]{"A man, a plan, a canal: Panama", "Hello", "Was it a car or a cat I saw?", ""})
            System.out.println("\"" + s + "\" palindrome? " + isPalindrome(s));
        System.out.println(anagrams("Dormitory", "dirty room") + " " + anagrams("abc", "abd"));
        String enc = caesar("Hello, World!", 3);
        System.out.println(enc + " | " + caesar(enc, -3));
        String v = vigenere("attack at dawn", "lemon", true);
        System.out.println(v + " | " + vigenere(v, "lemon", false));
        String r = rle("aaabccddddde");
        System.out.println(r + " | " + unrle(r) + " | " + unrle("x12y1"));
        System.out.println(reverseWords("  the sky   is blue "));
        System.out.println("vowels: " + vowels("Programming Language Java"));
        System.out.println(toSnake("parseHttpResponseCode") + " " + toSnake("Already"));
        Map<Character, Integer> freq = new TreeMap<>();
        for (char c : "mississippi river".toCharArray()) if (c != ' ') freq.merge(c, 1, Integer::sum);
        System.out.println(freq);
    }
}
