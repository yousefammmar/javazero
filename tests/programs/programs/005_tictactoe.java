public class Main {
    static char[][] board = new char[3][3];

    static void reset() {
        for (char[] row : board) java.util.Arrays.fill(row, '.');
    }

    static void print() {
        for (int r = 0; r < 3; r++) {
            StringBuilder sb = new StringBuilder();
            for (int c = 0; c < 3; c++) {
                sb.append(board[r][c]);
                if (c < 2) sb.append('|');
            }
            System.out.println(sb);
            if (r < 2) System.out.println("-+-+-");
        }
        System.out.println();
    }

    static char winner() {
        for (int i = 0; i < 3; i++) {
            if (board[i][0] != '.' && board[i][0] == board[i][1] && board[i][1] == board[i][2]) return board[i][0];
            if (board[0][i] != '.' && board[0][i] == board[1][i] && board[1][i] == board[2][i]) return board[0][i];
        }
        if (board[1][1] != '.') {
            if (board[0][0] == board[1][1] && board[1][1] == board[2][2]) return board[1][1];
            if (board[0][2] == board[1][1] && board[1][1] == board[2][0]) return board[1][1];
        }
        return ' ';
    }

    static boolean full() {
        for (char[] row : board) for (char c : row) if (c == '.') return false;
        return true;
    }

    static void play(int[] moves, String title) {
        System.out.println("=== " + title + " ===");
        reset();
        char turn = 'X';
        for (int i = 0; i < moves.length; i++) {
            int r = (moves[i] - 1) / 3, c = (moves[i] - 1) % 3;
            if (board[r][c] != '.') {
                System.out.println("Square " + moves[i] + " taken, skipping");
                continue;
            }
            board[r][c] = turn;
            System.out.println("Move " + (i + 1) + ": " + turn + " -> " + moves[i]);
            char w = winner();
            if (w != ' ') {
                print();
                System.out.println("Winner: " + w);
                return;
            }
            turn = (turn == 'X') ? 'O' : 'X';
        }
        print();
        System.out.println(full() ? "Draw" : "Unfinished");
    }

    public static void main(String[] args) {
        play(new int[]{1, 2, 5, 3, 9}, "diagonal win");
        play(new int[]{5, 1, 2, 8, 4, 6, 7, 3, 9}, "draw");
        play(new int[]{1, 1, 4, 2, 7}, "column with retry");
        play(new int[]{3, 1, 5}, "unfinished");
    }
}
