import java.util.Arrays;
import java.util.Map;
import java.util.TreeMap;

public class Main {
    static boolean isPalindrome(String s) {
        String t = s.toLowerCase().replaceAll("[^a-z0-9]", "");
        return new StringBuilder(t).reverse().toString().equals(t);
    }
    static boolean anagram(String a, String b) {
        char[] x = a.replace(" ", "").toLowerCase().toCharArray(), y = b.replace(" ", "").toLowerCase().toCharArray();
        Arrays.sort(x); Arrays.sort(y);
        return Arrays.equals(x, y);
    }
    static String compress(String s) {
        if (s.isEmpty()) return s;
        StringBuilder sb = new StringBuilder();
        int run = 1;
        for (int i = 1; i <= s.length(); i++) {
            if (i < s.length() && s.charAt(i) == s.charAt(i - 1)) run++;
            else { sb.append(s.charAt(i - 1)); if (run > 1) sb.append(run); run = 1; }
        }
        return sb.toString();
    }
    static String reverseWords(String s) {
        String[] w = s.trim().split("\\s+");
        StringBuilder sb = new StringBuilder();
        for (int i = w.length - 1; i >= 0; i--) { sb.append(w[i]); if (i > 0) sb.append(' '); }
        return sb.toString();
    }
    static String title(String s) {
        StringBuilder sb = new StringBuilder();
        boolean up = true;
        for (char c : s.toCharArray()) {
            sb.append(up ? Character.toUpperCase(c) : Character.toLowerCase(c));
            up = c == ' ';
        }
        return sb.toString();
    }
    static String toBase(int n, int base) {
        if (n == 0) return "0";
        String digits = "0123456789ABCDEF";
        StringBuilder sb = new StringBuilder();
        boolean neg = n < 0;
        long v = Math.abs((long) n);
        while (v > 0) { sb.append(digits.charAt((int) (v % base))); v /= base; }
        return (neg ? "-" : "") + sb.reverse();
    }
    public static void main(String[] args) {
        for (String s : new String[]{"A man, a plan, a canal: Panama", "hello", "", "No lemon, no melon", "ab"}) System.out.println("'" + s + "' palindrome=" + isPalindrome(s));
        System.out.println(anagram("Listen", "Silent") + " " + anagram("Dormitory", "dirty room") + " " + anagram("abc", "abd"));
        for (String s : new String[]{"aaabbc", "abc", "aabbaa", "", "zzzzzzzzzzzz"}) System.out.println("'" + s + "' -> '" + compress(s) + "'");
        System.out.println(reverseWords("  the quick   brown fox "));
        System.out.println(title("hELLO wORLD from jAVA"));
        System.out.println(toBase(255, 2) + " " + toBase(255, 16) + " " + toBase(-255, 8) + " " + toBase(0, 2) + " " + toBase(Integer.MIN_VALUE, 16));
        String text = "the cat and the hat and the bat sat on the mat";
        Map<String, Integer> freq = new TreeMap<>();
        for (String w : text.split(" ")) freq.merge(w, 1, Integer::sum);
        System.out.println(freq);
        Map<Character, Integer> cf = new TreeMap<>();
        for (char c : text.replace(" ", "").toCharArray()) cf.put(c, cf.getOrDefault(c, 0) + 1);
        System.out.println(cf);
        int[] counts = new int[26];
        for (char c : text.toCharArray()) if (c >= 'a' && c <= 'z') counts[c - 'a']++;
        StringBuilder top = new StringBuilder();
        for (int i = 0; i < 26; i++) if (counts[i] >= 4) top.append((char) ('a' + i)).append('=').append(counts[i]).append(' ');
        System.out.println(top.toString().trim());
        String csv = "name,age,city\nAnn,30,NYC\nBob,,LA\n";
        for (String line : csv.split("\n")) System.out.println(Arrays.toString(line.split(",")) + " " + line.split(",", -1).length);
        System.out.println("  multiple   spaces here ".trim().replaceAll(" +", " "));
        System.out.println("a-b_c d".replaceAll("[-_ ]", ""));
        System.out.println("CamelCaseToSnake".replaceAll("([a-z])([A-Z])", "$1_$2").toLowerCase());
        System.out.println("x=1, y=22, z=333".replaceAll("\\d+", "<$0>"));
        System.out.println("hello".chars().mapToObj(c -> "" + (char) c).reduce((a, b) -> b + a).get());
        System.out.println("hello world".indexOf("o") + " " + "hello world".indexOf("o", 5) + " " + "hello world".lastIndexOf("o"));
        StringBuilder acro = new StringBuilder();
        for (String w : "portable network graphics".split(" ")) acro.append(Character.toUpperCase(w.charAt(0)));
        System.out.println(acro);
        System.out.println("Mississippi".replace("ss", "SS").replaceAll("i(?=p)", "I"));
        int vowels = 0, cons = 0;
        for (char c : "Hello World".toLowerCase().toCharArray()) { if ("aeiou".indexOf(c) >= 0) vowels++; else if (Character.isLetter(c)) cons++; }
        System.out.println(vowels + " " + cons);
        System.out.println(String.valueOf(new char[]{'a', 'b'}) + String.copyValueOf(new char[]{'c', 'd', 'e'}, 1, 2) + new String(new char[]{'x', 'y', 'z'}, 1, 2));
        System.out.println("%d%%|%5s|%-5s|".formatted(50, "ab", "cd") + "abc".indent(2).replace("\n", "$") + "  x\n y".stripIndent().replace("\n", "$"));
        System.out.println("tEsT".toLowerCase().equals("test") + " " + "ß".length() + " " + "\t".isBlank() + " " + "a\tb".split("\t").length + " " + "abc".contains("") + " " + "".split(",").length + " " + "".split("").length);
        System.out.println("ab".compareTo("abc") + " " + "b".compareTo("abc") + " " + "B".compareTo("a") + " " + "10".compareTo("9") + " " + "apple".compareToIgnoreCase("APPLE"));
    }
}
