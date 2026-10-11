public class Main {
    public static void main(String[] args) {
        int[] a = new int[3];
        int[] idx = {5, -1, 3, 2};
        for (int i : idx) {
            try {
                a[i] = 7;
                System.out.println("wrote " + i + " -> " + a[i]);
            } catch (ArrayIndexOutOfBoundsException e) {
                System.out.println(e.getMessage());
                System.out.println(e);
                System.out.println(e.getClass().getName());
                System.out.println(e instanceof IndexOutOfBoundsException);
            }
        }
        String[] s = new String[2];
        try { System.out.println(s[2].length()); }
        catch (RuntimeException e) { System.out.println(e.getClass().getSimpleName() + ": " + e.getMessage()); }
        int[][] g = new int[2][4];
        try { g[1][4] = 1; } catch (ArrayIndexOutOfBoundsException e) { System.out.println(e.getMessage()); }
        try { g[2][0] = 1; } catch (ArrayIndexOutOfBoundsException e) { System.out.println(e.getMessage()); }
        int[] empty = {};
        try { System.out.println(empty[0]); } catch (ArrayIndexOutOfBoundsException e) { System.out.println(e.getMessage()); }
        long[] la = new long[1];
        try { la[Integer.MAX_VALUE] = 1; } catch (Exception e) { System.out.println(e.getMessage()); }
        char[] ca = new char[2];
        try { ca[-5]++; } catch (Exception e) { System.out.println(e.getMessage()); }
        try { System.out.println(java.util.Arrays.copyOfRange(a, 5, 2).length); } catch (Exception e) { System.out.println(e); }
        try { System.arraycopy(a, 0, new int[2], 0, 3); } catch (Exception e) { System.out.println(e.getClass().getName() + ": " + e.getMessage()); }
    }
}
