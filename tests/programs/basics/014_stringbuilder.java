public class Main {
    public static void main(String[] args) {
        StringBuilder sb = new StringBuilder();
        System.out.println(sb.length() + "|" + sb + "|");
        sb.append("hello").append(' ').append(42).append(true).append(1.5).append(2.5f).append(10L).append((Object) null).append((String) null);
        System.out.println(sb);
        sb.setLength(5);
        System.out.println(sb);
        sb.insert(0, "[").insert(sb.length(), ']');
        System.out.println(sb);
        sb.insert(3, 123).insert(1, 4.5).insert(0, true).insert(2, 'c');
        System.out.println(sb);
        sb.reverse();
        System.out.println(sb);
        sb.reverse();
        sb.deleteCharAt(0);
        System.out.println(sb);
        sb.delete(0, 3);
        System.out.println(sb);
        sb.delete(2, 100);
        System.out.println(sb);
        sb.setCharAt(0, 'Z');
        System.out.println(sb);
        sb.replace(0, 1, "abc");
        System.out.println(sb);
        sb.replace(1, 2, "");
        System.out.println(sb);
        sb.replace(1, 50, "tail");
        System.out.println(sb);
        System.out.println(sb.indexOf("a") + " " + sb.indexOf("zz") + " " + sb.lastIndexOf("a") + " " + sb.charAt(2));
        System.out.println(sb.substring(2) + " " + sb.substring(1, 3));
        sb.setLength(0);
        System.out.println(sb.length() + " " + sb.isEmpty());
        for (int i = 0; i < 10; i++) sb.append(i).append(i < 9 ? "," : "");
        System.out.println(sb);
        sb.setLength(3);
        sb.append("xyz");
        System.out.println(sb);
        sb.setLength(8);
        System.out.println(sb.length() + " " + (int) sb.charAt(7) + " " + sb.toString().replace('\0', '_'));
        StringBuilder pal = new StringBuilder("racecar");
        System.out.println(pal.toString().equals(pal.reverse().toString()));
        StringBuilder a = new StringBuilder("same"), b = new StringBuilder("same");
        System.out.println(a.equals(b) + " " + a.toString().equals(b.toString()) + " " + (a.compareTo(b)) + " " + a.compareTo(new StringBuilder("samf")));
        System.out.println(new StringBuilder("abc").append(new char[]{'d', 'e'}).append("fghij", 1, 3).append(1 + 1).append('a' + 1).append((char) ('a' + 1)));
        StringBuilder chain = new StringBuilder("x");
        StringBuilder same = chain.append("y");
        System.out.println(chain == same);
        System.out.println(new StringBuilder("cap").capacity() >= 3);
        try {
            new StringBuilder("abc").deleteCharAt(3);
        } catch (StringIndexOutOfBoundsException e) {
            System.out.println("SIOOBE");
        }
        try {
            new StringBuilder("abc").insert(4, "x");
        } catch (StringIndexOutOfBoundsException e) {
            System.out.println("SIOOBE2");
        }
        try {
            new StringBuilder("abc").setCharAt(3, 'x');
        } catch (IndexOutOfBoundsException e) {
            System.out.println("IOOBE");
        }
        StringBuffer buf = new StringBuffer("buf");
        buf.append(1).insert(0, '>').reverse();
        System.out.println(buf);
        StringBuilder tri = new StringBuilder();
        for (int i = 1; i <= 4; i++) {
            tri.append(" ".repeat(4 - i)).append("*".repeat(2 * i - 1)).append('\n');
        }
        System.out.print(tri);
        System.out.println(tri.length());
    }
}
