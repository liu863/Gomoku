/**
 * This AI uses Alpha-Beta Pruning algorithm.
 * For any given game state, tt counts the
 * amount of adjacent same pieces, and multiplied
 * with different multipliers. Then, sum up all
 * values to get the evaluation of certain state.
 */

package ai;

import game.Game;

public class Medium implements Ai {

    private Game g;
    private int[][] offsets, board;
    private final int EFFECTIVE_DEPTH = 4;
    private final int EXPEND_SIZE = 1;
    private final int WINNING_VALUE = 10000000;
    private long total_time = 0;

    public Medium(Game g) {
        this.g = g;
        board = g.getGameboard();
        offsets = new int[15][15];
        for (int i = 1; i < 8; i++) {
            for (int j = i; j < 15 - i; j++) {
                offsets[i][j] = i;
                offsets[14 - i][j] = i;
                offsets[j][i] = i;
                offsets[j][14 - i] = i;
            }
        }
    }

    public long totalTime() {
        return total_time;
    }

    public void move() {
        long start = System.currentTimeMillis();
        int[] last_move = g.getLastMove();
        int r = 7, c = 7;
        if (last_move[2] != 0) {
            board[last_move[0]][last_move[1]] = last_move[2];
            int highest = Integer.MIN_VALUE;
            for (int i = 0; i < 15; i++) {
                for (int j = 0; j < 15; j++) {
                    if (board[i][j] != 0 || !valuableMove(i, j)) {
                        continue;
                    }
                    int val = posVal(i, j, 1) + offsets[i][j];
                    if (val > highest) {
                        r = i;
                        c = j;
                        highest = val;
                    }
                }
            }
        }
        board[r][c] = g.getPlayer();
        if (!g.move(r, c)) {
            System.err.println(String.format("AI_MEDIUM_INVALID_LOCATION: %d %d", r, c));
        }
        total_time += System.currentTimeMillis() - start;
    }

    /**
     * @param r     Row index of target position.
     * @param c     Column index of target position.
     * @param round Recursion depth of forward step, start from 1.
     * @return The value of game if set in target position.
     */
    private int posVal(int r, int c, int round) {
        int player = (g.getPlayer() + round % 2) % 2 + 1, ret = 0;
        board[r][c] = player;
        ret = calBoard();
        if (round < EFFECTIVE_DEPTH && Math.abs(ret) < WINNING_VALUE) {
            if (round % 2 == 1) {
                for (int i = 0; i < 15; i++) {
                    for (int j = 0; j < 15; j++) {
                        if (board[i][j] == 0 && valuableMove(i, j)) {
                            int temp = posVal(i, j, round + 1);
                            if (temp <= -WINNING_VALUE / (round + 1)) {
                                board[r][c] = 0;
                                return temp;
                            }
                            ret = Math.min(ret, temp);
                        }
                    }
                }

            } else {
                for (int i = 0; i < 15; i++) {
                    for (int j = 0; j < 15; j++) {
                        if (board[i][j] == 0 && valuableMove(i, j)) {
                            int temp = posVal(i, j, round + 1);
                            if (temp >= WINNING_VALUE / (round + 1)) {
                                board[r][c] = 0;
                                return temp;
                            }
                            ret = Math.max(ret, temp);
                        }
                    }
                }
            }
        }
        board[r][c] = 0;
        return ret / round;
    }

    private int calBoard() {
        int[] sum = new int[18];
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
        int val = sumCombinations(sum);
        return val;
    }

    private void calLineVal(int[] sum, int startRow, int startCol, int delRow, int delCol) {
        int player = g.getPlayer(), r = startRow, c = startCol, blank = 0;
        while (insideBoard(r, c)) {
            if (board[r][c] == 0) {
                r += delRow;
                c += delCol;
                blank++;
            }
            else {
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

    private int sumCombinations(int[] sum) {
        int val = 0;
        if (sum[8] > 0) val = WINNING_VALUE;
        else if (sum[17] > 0) val = -WINNING_VALUE;
        else {
            val += (sum[0] - sum[9]) * 1;
            val += (sum[1] - sum[10]) * 10;
            val += (sum[2] - sum[11]) * 10;
            val += (sum[3] - sum[12]) * 100;
            val += (sum[4] - sum[13]) * 100;
            val += (sum[5] - sum[14]) * 1000;
            val += (sum[6] - sum[15]) * 1000;
            val += (sum[7] - sum[16]) * 10000;
        }
        return val;
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
}