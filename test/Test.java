import ai.*;
import game.*;
import server.*;
import java.util.*;

public class Test {
    int[][] board = {
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}};

    public static void main(String[] args) {
        Test t = new Test();
        //t.game();
        //t.aiHard();
        int[] sum = new int[18];
        t.calLineVal(sum, 0, 0, 1, 0);
        System.out.println(Arrays.toString(sum));
    }

    private void game() {
        //new game
        Game game = new Game();
        //initialization
        game.printGame();

        //customized game
        //invalid board length
        try {
            game = new Game(new int[12][12]);
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid input");
        }
        //invalid moves
        //int[][] board = new int[15][15];
    }

    private void aiHard() {
        Game g = new Game();
        Ai ai = new Hard(g);
        //check values initialization
        ai.printAi();
    }

    private void calLineVal(int[] sum, int startRow, int startCol, int delRow, int delCol) {
        int player = 1, r = startRow, c = startCol, blank = 0;
        while (insideBoard(r, c)) {
            if (board[r][c] == 0) {
                r += delRow;
                c += delCol;
                blank++;
            } else {
                int space = 0, count = 0, p = board[r][c];
                boolean open = blank > 0;
                space += blank;
                while (insideBoard(r, c) && board[r][c] == p) {
                    count++;
                    r += delRow;
                    c += delCol;
                }
                space += count;
                blank = 0;
                while (insideBoard(r, c) && board[r][c] == 0) {
                    blank++;
                    r += delRow;
                    c += delCol;
                }
                open &= blank > 0;
                space += blank;
                if (p == player) {
                    if (space >= 5) {
                        int index = 0;
                        if (count >= 5) index += 8;
                        else index += count * 2 - (open ? 0 : 1) - 1;
                        sum[index]++;
                    }
                } else {
                    if (space >= 5) {
                        int index = 9;
                        if (count >= 5) index += 8;
                        else index += count * 2 - (open ? 0 : 1) - 1;
                        sum[index]++;
                    }
                }
            }
        }
    }

    private boolean insideBoard(int r, int c) {
        return r >= 0 && r < 15 && c >= 0 && c < 15;
    }
}