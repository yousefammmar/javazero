public class Main {
    public static void main(String[] args) {
        String s = "Hello, World";
        System.out.println(s.length());
        System.out.println(s.charAt(4));
        System.out.println(s.substring(7));
        System.out.println(s.substring(0, 5));
        System.out.println(s.substring(5, 5).isEmpty());
        System.out.println(s.substring(12).length());
        System.out.println(s.indexOf('o'));
        System.out.println(s.indexOf('o', 5));
        System.out.println(s.indexOf("o, "));
        System.out.println(s.indexOf("xyz"));
        System.out.println(s.indexOf(""));
        System.out.println(s.indexOf("", 50));
        System.out.println(s.indexOf('o', -5));
        System.out.println(s.indexOf('o', 100));
        System.out.println(s.lastIndexOf('o'));
        System.out.println(s.lastIndexOf('o', 7));
        System.out.println(s.lastIndexOf("l"));
        System.out.println(s.lastIndexOf("l", 9));
        System.out.println(s.lastIndexOf(""));
        System.out.println(s.indexOf(108));
        System.out.println(s.contains("World") + " " + s.contains(""));
        System.out.println(s.startsWith("Hell") + " " + s.startsWith("World", 7) + " " + s.endsWith("ld") + " " + s.endsWith(""));
        System.out.println(s.toUpperCase() + s.toLowerCase());
        System.out.println(s.replace('l', 'L'));
        System.out.println(s.replace("l", "LL"));
        System.out.println(s.replace("", "-"));
        System.out.println("aaaa".replace("aa", "b"));
        System.out.println("aaa".replaceAll("a*", "X"));
        System.out.println("a.b.c".replaceAll(".", "x"));
        System.out.println("a.b.c".replace(".", "x"));
        System.out.println("a1b22c333".replaceAll("[0-9]+", "#"));
        System.out.println("a1b22c333".replaceFirst("[0-9]+", "#"));
        System.out.println("John Smith".replaceAll("(\\w+) (\\w+)", "$2, $1"));
        System.out.println("  pad  ".trim() + "|" + "  pad  ".strip() + "|" + "  pad  ".stripLeading() + "|" + "  pad  ".stripTrailing() + "|");
        System.out.println("\t\n x \r\n".trim().length());
        System.out.println("ab".repeat(3) + "|" + "x".repeat(0) + "|");
        System.out.println("apple".compareTo("banana"));
        System.out.println("apple".compareTo("apple"));
        System.out.println("apple".compareTo("app"));
        System.out.println("Apple".compareTo("apple"));
        System.out.println("a".compareTo("b") + " " + "b".compareTo("a") + " " + "az".compareTo("b"));
        System.out.println("Apple".compareToIgnoreCase("apple") + " " + "ABC".compareToIgnoreCase("abd"));
        System.out.println("Hello".equalsIgnoreCase("hELLO") + " " + "Hello".equals("hello") + " " + "x".equals(null));
        System.out.println("".isEmpty() + " " + " ".isEmpty() + " " + " ".isBlank());
        System.out.println("hello".chars().sum());
        System.out.println("hello".chars().filter(c -> c == 'l').count());
        System.out.println("abc".chars().mapToObj(c -> String.valueOf((char) (c + 1))).reduce("", String::concat));
        System.out.println(java.util.Arrays.toString("hey".toCharArray()));
        System.out.println(String.join("-", "a", "b", "c"));
        System.out.println(String.join(", ", java.util.List.of("x", "y")));
        System.out.println(String.join("", new String[0]) + "|");
        System.out.println("abc".concat("def"));
        System.out.println("Mississippi".indexOf("ss", 3) + " " + "Mississippi".lastIndexOf("ss", 4));
        System.out.println("hello".hashCode() + " " + "".hashCode() + " " + "Aa".hashCode() + " " + "BB".hashCode());
        System.out.println("a,b".contentEquals("a,b"));
        System.out.println("TitLe".charAt(0) + "TitLe".substring(1).toLowerCase());
        System.out.println("line1\nline2\nline3".lines().count());
        System.out.println("abc".matches("[a-c]+") + " " + "abcd".matches("[a-c]+"));
        System.out.println("x".compareTo("xyz") + " " + "xyz".compareTo("x"));
        try {
            System.out.println(s.charAt(20));
        } catch (StringIndexOutOfBoundsException e) {
            System.out.println("SIOOBE " + e.getMessage());
        }
        try {
            System.out.println(s.substring(5, 2));
        } catch (StringIndexOutOfBoundsException e) {
            System.out.println("SIOOBE " + e.getMessage());
        }
        try {
            System.out.println(s.substring(13));
        } catch (StringIndexOutOfBoundsException e) {
            System.out.println("SIOOBE " + e.getMessage());
        }
        try {
            System.out.println(s.substring(-1));
        } catch (StringIndexOutOfBoundsException e) {
            System.out.println("SIOOBE " + e.getMessage());
        }
    }
}
