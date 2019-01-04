package ai;

import game.Game;

import java.lang.*;
import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;

public class Hard implements Ai {

    private Game g;
    private int[][] board, ai_values;
    private final int EFFECTIVE_DEPTH = 5;
    private final int EXPEND_SIZE = 2;

    public Hard(Game g) {
        this.g = g;
        board = g.getGameboard();
        ai_values = new int[15][15];
    }

    public void move() {
        int[] last_move = g.getLastMove();
        int[] move = new int[]{7, 7};
        if (last_move[2] != 0) {
            board[last_move[0]][last_move[1]] = last_move[2];
            move = getMove(1);
        }
        board[move[0]][move[1]] = g.getPlayer();
        if (!g.move(move[0], move[1])) {
            System.err.println(String.format("AI_HARD_INVALID_LOCATION: %d %d", move[0], move[1]));
        }
    }

    /**
     * @param round Recursion depth of forward step, start from 1.
     * @return Next move with highest profit.
     */
    private int[] getMove(int round) {
        int player = (g.getPlayer() + round % 2) % 2 + 1;
        boolean odd = round % 2 == 1;
        int[] highest = new int[9], ret = new int[3];
        Arrays.fill(highest, odd ? Integer.MIN_VALUE : Integer.MAX_VALUE);
        List<int[]> candidates = new ArrayList<>();
        for (int i = 0 ; i < 15; i++) {
            for (int j = 0; j < 15; j++) {
                if (board[i][j] == 0 && valuableMove(i, j)) {
                    board[i][j] = player;
                    int[] temp = calBoard();
                    int comp = compareAndReplace(temp, highest, round == EFFECTIVE_DEPTH, odd);
                    // System.out.println("round: " + round + ", comp: " + comp);
                    if (comp >= 0) {
                        if (comp > 0)
                            candidates.clear();
                        candidates.add(new int[]{i, j});
                        highest = temp;
                    }
                    board[i][j] = 0;
                }
            }
        }
        if (round < EFFECTIVE_DEPTH && highest[8] == 0) {
            List<int[]> cands = new ArrayList<>();
            int hi = odd ? Integer.MIN_VALUE : Integer.MAX_VALUE;
            // System.out.println("round: " + round + ", size: " + candidates.size());
            for (int[] candidate : candidates) {
                // System.out.println("round: " + round + ", " + candidate[0] + " " + candidate[1]);
                board[candidate[0]][candidate[1]] = player;
                int[] next = getMove(round + 1);
                if (odd) {
                    if (next[2] >= hi) {
                        if (next[2] > hi)
                            cands.clear();
                        cands.add(new int[]{candidate[0], candidate[1]});
                        hi = next[2];
                    }
                }
                else {
                    if (next[2] <= hi) {
                        if (next[2] < hi)
                            cands.clear();
                        cands.add(new int[]{candidate[0], candidate[1]});
                        hi = next[2];
                    }
                }
                board[candidate[0]][candidate[1]] = 0;
            }
            candidates = cands;
            ret[2] = hi;
        }
        else {
            ret[2] = sumUp(highest);
        }
        int[] move = candidates.get((int)(Math.random() * candidates.size()));
        ret[0] = move[0];
        ret[1] = move[1];
        return ret;
    }

    private int compareAndReplace(int[] arr1, int[] arr2, boolean last_round, boolean odd) {
        int ret = 0;
        for (int i = 8; i >= 0; i--) {
            if (!last_round && (arr1[i] != 0 || arr2[i] != 0)) {
                if ((arr1[i] <= 0 && arr2[i] > 0) || (arr1[i] < 0 && arr2[i] == 0)) ret = -1;
                else if ((arr1[i] > 0 && arr2[i] <= 0) || (arr1[i] == 0 && arr2[i] < 0)) ret = 1;
                else ret = 0;
                break;
            }
            if (last_round && arr1[i] != arr2[i]) {
                ret = arr1[i] > arr2[i] ? 1 : -1;
                break;
            }
        }
        return ret * (odd ? 1 : -1);
    }

    private int[] calBoard() {
        int[] sum = new int[9];
        //horizontal 15 rows and vertical 15 cols
        for (int i = 0; i < 15; i++) {
            calLineVal(sum, i, 0, 0, 1);
            calLineVal(sum, 0, i, 1, 0);
        }
        //diagonal 10 + 10 + 1
        for (int k = 0; k < 21; k++) {
            int i = 0, j = 0;
            if (k < 10) i -= k - 10;
            else if (k > 10) j += k - 10;
            calLineVal(sum, i, j, 1, 1);
        }
        //anti-diagonal 10 + 10 + 1
        for (int k = 0; k < 21; k++) {
            int i = 0, j = 14;
            if (k < 10) j -= 10 - k;
            else if (k > 10) i += k - 10;
            calLineVal(sum, i, j, 1, -1);
        }
        return sum;
    }

    private void calLineVal(int[] sum, int startRow, int startCol, int delRow, int delCol) {
        int player = g.getPlayer(), r = startRow, c = startCol, blank = 0;
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
                if (space >= 5) {
                    int index = count >= 5 ? 8 : (count * 2 - (open ? 0 : 1) - 1);
                    sum[index] += p == player ? 1 : -1;
                }
            }
        }
    }

    private int sumUp(int[] sum) {
        int val = 0, mult = 1;
        for (int i = 0; i < 9; i++) {
            val += sum[i] * mult;
            if (i % 2 == 0) mult *= 10;
        }
        val += sum[8] * 1000000;
        return Math.abs(val);
    }

    private boolean valuableMove(int r, int c) {
        int value = 0;
        for (int i = r - EXPEND_SIZE; i <= r + EXPEND_SIZE; i++) {
            for (int j = c - EXPEND_SIZE; j <= c + EXPEND_SIZE; j++) {
                if (!insideBoard(i, j) || board[i][j] == 0) continue;
                int dis = Math.max(Math.abs(i - r), Math.abs(j - c));
                value += EXPEND_SIZE - dis + 1;
            }
        }
        return value >= EXPEND_SIZE;
    }

    private boolean insideBoard(int r, int c) {
        return r >= 0 && r < 15 && c >= 0 && c < 15;
    }

    private void print() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 15; i++) {
            sb.append('|');
            for (int j = 0; j < 15; j++) {
                sb.append(String.format("%2d|", board[i][j]));
            }
            sb.append('\n');
        }
        System.out.print(sb.toString());
    }

    public void printAi() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 15; i++) {
            sb.append('|');
            for (int j = 0; j < 15; j++) {
                sb.append(String.format("%5d|", ai_values[i][j]));
            }
            sb.append('\n');
        }
        System.out.print(sb.toString());
    }
}