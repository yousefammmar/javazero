public class Main {
    public static void main(String[] args) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 10; i++) sb.append(i).append(',');
        sb.setLength(sb.length() - 1);
        System.out.println(sb);
        sb.insert(0, "[").append(']');
        System.out.println(sb + " " + sb.length());
        sb.reverse();
        System.out.println(sb);
        sb.reverse().deleteCharAt(0).delete(0, 4);
        System.out.println(sb);
        sb.replace(0, 2, "XYZ");
        System.out.println(sb + " idx=" + sb.indexOf("5") + " last=" + sb.lastIndexOf(",") + " " + sb.charAt(3));
        sb.setCharAt(0, 'q');
        System.out.println(sb);
        sb.append(1.5).append(true).append('c').append(10L).append(2.5f).append((Object) null).append((String) null).append(new char[]{'h', 'i'});
        System.out.println(sb);
        StringBuilder s2 = new StringBuilder("abc");
        s2.insert(1, 42).insert(0, 1.5).insert(s2.length(), 'z');
        System.out.println(s2);
        System.out.println(s2.substring(2) + " " + s2.substring(1, 4) + " " + s2.toString().equals("x") + " " + s2.compareTo(new StringBuilder("1")));
        StringBuilder big = new StringBuilder();
        for (int i = 0; i < 2000; i++) big.append((char) ('a' + i % 26));
        System.out.println(big.length() + " " + big.substring(1990) + " " + big.toString().hashCode());
        try { sb.insert(999, "x"); } catch (StringIndexOutOfBoundsException e) { System.out.println("SIOOBE " + e.getMessage()); }
        try { new StringBuilder("ab").deleteCharAt(5); } catch (StringIndexOutOfBoundsException e) { System.out.println("SIOOBE2 " + e.getMessage()); }
    }
}
