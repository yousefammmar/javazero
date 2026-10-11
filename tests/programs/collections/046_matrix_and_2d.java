import java.util.*;
public class Main {
    public static void main(String[] args) {
        List<List<Integer>> m = new ArrayList<>();
        for (int i = 0; i < 4; i++) { List<Integer> row = new ArrayList<>(); for (int j = 0; j < 5; j++) row.add(i * 5 + j); m.add(row); }
        System.out.println(m);
        List<List<Integer>> t = new ArrayList<>();
        for (int j = 0; j < 5; j++) { List<Integer> col = new ArrayList<>(); for (List<Integer> row : m) col.add(row.get(j)); t.add(col); }
        System.out.println(t);
        m.get(2).set(2, -1);
        m.remove(0);
        m.get(0).remove(Integer.valueOf(8));
        m.get(1).add(0, 99);
        System.out.println(m);
        List<Integer> sharedRow = new ArrayList<>(List.of(0, 0));
        List<List<Integer>> shared = new ArrayList<>(Collections.nCopies(3, sharedRow));
        shared.get(0).set(0, 7);
        System.out.println("shared aliasing " + shared);
        List<List<Integer>> indep = new ArrayList<>();
        for (int i = 0; i < 3; i++) indep.add(new ArrayList<>(List.of(0, 0)));
        indep.get(0).set(0, 7);
        System.out.println("independent " + indep);
        int[][] grid = new int[4][4];
        for (int i = 0; i < 4; i++) for (int j = 0; j < 4; j++) grid[i][j] = (i + 1) * (j + 1);
        System.out.println(Arrays.deepToString(grid));
        int[][] rot = new int[4][4];
        for (int i = 0; i < 4; i++) for (int j = 0; j < 4; j++) rot[j][3 - i] = grid[i][j];
        System.out.println(Arrays.deepToString(rot));
        int[][] jag = new int[4][];
        for (int i = 0; i < 4; i++) { jag[i] = new int[i + 1]; Arrays.fill(jag[i], i); }
        System.out.println(Arrays.deepToString(jag));
        int[][] copyShallow = grid.clone();
        copyShallow[0][0] = 100;
        System.out.println(grid[0][0] + " " + (copyShallow[0] == grid[0]));
        int[][] deep = new int[4][];
        for (int i = 0; i < 4; i++) deep[i] = grid[i].clone();
        deep[1][1] = -9;
        System.out.println(grid[1][1] + " " + deep[1][1]);
        Arrays.sort(grid, (a, b) -> b[3] - a[3]);
        System.out.println(Arrays.deepToString(grid));
        Map<String, int[]> pos = new TreeMap<>();
        pos.put("a", new int[]{1, 2}); pos.put("b", new int[]{3, 4});
        pos.get("a")[0] += 10;
        for (Map.Entry<String, int[]> e : pos.entrySet()) System.out.print(e.getKey() + Arrays.toString(e.getValue()) + " ");
        System.out.println();
        char[][] board = new char[3][3];
        for (char[] row : board) Arrays.fill(row, '.');
        board[1][1] = 'X'; board[0][2] = 'O';
        for (char[] row : board) System.out.println(new String(row));
        long[][] pas = new long[10][];
        for (int i = 0; i < 10; i++) { pas[i] = new long[i + 1]; pas[i][0] = pas[i][i] = 1; for (int j = 1; j < i; j++) pas[i][j] = pas[i - 1][j - 1] + pas[i - 1][j]; }
        System.out.println(Arrays.toString(pas[9]));
        List<int[]> moves = new ArrayList<>();
        moves.add(new int[]{1, 2}); moves.add(new int[]{1, 2});
        System.out.println(moves.contains(new int[]{1, 2}) + " " + moves.get(0).equals(moves.get(1)) + " " + Arrays.equals(moves.get(0), moves.get(1)));
    }
}
