/**
 * This AI uses Alpha-Beta Pruning algorithm.
 * Using different static evaluation function to
 * the medium AI. Apply time limited function.
 */

package ai;

import game.Game;

import java.util.*;

public class Hard implements Ai {

    private Game g;
    private int[][] offsets, board;
    private State curr_state;
    private final int EFFECTIVE_DEPTH = 4;
    private final int EXPEND_SIZE = 1;
    private long total_time = 0;

    public Hard(Game g) {
        this.g = g;
        board = g.getGameboard();
        offsets = new int[15][15];
        curr_state = new State(EXPEND_SIZE);
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
        int r = 7, c = 7, player = g.getPlayer();
        if (last_move[2] != 0) {
            curr_state.move(last_move[0], last_move[1], last_move[2]);
            int[] res = abPruning(curr_state, 0, Integer.MIN_VALUE, Integer.MAX_VALUE, player);
            r = res[0];
            c = res[1];
        }
        if (!g.move(r, c)) {
            System.err.println(String.format("AI_HARD_INVALID_LOCATION: %d %d", r, c));
        }
        board[r][c] = player;
        curr_state.move(r, c, player);
        total_time += System.currentTimeMillis() - start;
    }

    /**
     *
     * @param round Depth of future move, start from 0, even for self, odd for opponent.
     * @param alpha upper bound for min level
     * @param beta lower bound for max level
     * @param player current player
     * @return int array of size 3: {row, col, value}
     */
    private int[] abPruning(State state, int round, int alpha, int beta, int player) {
        if (state.isComplete() != 0 || round >= EFFECTIVE_DEPTH) {
            int val = state.calBoard(g.getPlayer());
            return new int[]{0, 0, val};
        }
        List<String> candidates = new ArrayList<>();
        int bound = round % 2 == 0 ? Integer.MIN_VALUE : Integer.MAX_VALUE;
        int next_player = player == 1 ? 2 : 1;
        for (String vm : state.valuableMoves()) {
            int[] m = str2arr(vm);
            State next_state = state.dup();
            next_state.move(m[0], m[1], player);
            int[] next_level = abPruning(next_state, round + 1, alpha, beta, next_player);
            if (round % 2 == 0) { //max level
                next_level[2] += offsets[m[0]][m[1]];
                if (next_level[2] >= bound) {
                    if (next_level[2] > bound) {
                        bound = next_level[2];
                        candidates.clear();
                    }
                    candidates.add(vm);
                }
                if (bound >= beta) break;
            }
            else { //min level
                next_level[2] -= offsets[m[0]][m[1]];
                if (next_level[2] <= bound) {
                    if (next_level[2] < bound) {
                        bound = next_level[2];
                        candidates.clear();
                    }
                    candidates.add(vm);
                }
                if (bound <= alpha) break;
            }
        }
        int[] ret = new int[3];
        if (candidates.size() > 0) {
            int index = (int) (Math.random() * candidates.size());
            int[] move = str2arr(candidates.get(index));
            ret[0] = move[0];
            ret[1] = move[1];
        }
        ret[2] = bound;
        return ret;
    }

    private int[] str2arr(String str) {
        String[] str_arr = str.split(",");
        return new int[]{Integer.parseInt(str_arr[0]), Integer.parseInt(str_arr[1])};
    }
}

class State {
    private int expend;
    private int[][] board;
    private Set<String> valuable_moves;
    private int complete = 0;

    protected State(int expend) {
        this.expend = expend;
        board = new int[15][15];
        valuable_moves = new HashSet<>();
    }

    private State(int expend, int[][] board, Set<String> valuable_moves) {
        this.expend = expend;
        this.board = new int[15][];
        for (int i = 0; i < 15; i++)
            this.board[i] = board[i].clone();
        this.valuable_moves = new HashSet<>(valuable_moves);
    }

    protected void move(int r, int c, int p) {
        board[r][c] = p;
        valuable_moves.remove(pt2str(r, c));
        for (int i = -expend; i <= expend; i++) {
            for (int j = -expend; j <= expend; j++) {
                int nr = r + i, nc = c + j;
                if (insideBoard(nr, nc) && board[nr][nc] == 0) {
                    valuable_moves.add(pt2str(nr, nc));
                }
            }
        }
        int count = 1;
        for (int i = r + 1; i < 15 && board[i][c] == p && count < 5; i++, count++) ;
        for (int i = r - 1; i >= 0 && board[i][c] == p && count < 5; i--, count++) ;
        if (count >= 5) {
            complete = p;
            return;
        }
        count = 1;
        for (int j = c + 1; j < 15 && board[r][j] == p && count < 5; j++, count++) ;
        for (int j = c - 1; j >= 0 && board[r][j] == p && count < 5; j--, count++) ;
        if (count >= 5) {
            complete = p;
            return;
        }
        count = 1;
        for (int i = r + 1, j = c + 1; i < 15 && j < 15 && board[i][j] == p && count < 5; i++, j++, count++) ;
        for (int i = r - 1, j = c - 1; i >= 0 && j >= 0 && board[i][j] == p && count < 5; i--, j--, count++) ;
        if (count >= 5) {
            complete = p;
            return;
        }
        count = 1;
        for (int i = r + 1, j = c - 1; i < 15 && j >= 0 && board[i][j] == p && count < 5; i++, j--, count++) ;
        for (int i = r - 1, j = c + 1; i >= 0 && j < 15 && board[i][j] == p && count < 5; i--, j++, count++) ;
        if (count >= 5) {
            complete = p;
            return;
        }
    }

    protected int isComplete() {
        return complete;
    }

    protected int calBoard(int player) {
        if (isComplete() != 0) {
//            return isComplete() == player ? Integer.MAX_VALUE : Integer.MIN_VALUE;
            return isComplete() == player ? 10000000 : -10000000;
        }
        int[] sum = new int[16];
        //horizontal 15 rows and vertical 15 cols
        for (int i = 0; i < 15; i++) {
            calLineVal(sum, i, 0, 0, 1, player);
            calLineVal(sum, 0, i, 1, 0, player);
        }
        //diagonal 10 + 10 + 1
        for (int k = 0; k < 21; k++) {
            int i = 0, j = 0;
            if (k < 10) i -= k - 10;
            else if (k > 10) j += k - 10;
            calLineVal(sum, i, j, 1, 1, player);
        }
        //anti-diagonal 10 + 10 + 1
        for (int k = 0; k < 21; k++) {
            int i = 0, j = 14;
            if (k < 10) j -= 10 - k;
            else if (k > 10) i += k - 10;
            calLineVal(sum, i, j, 1, -1, player);
        }
        int val = sumCombinations(sum);
        return val;
    }

    private void calLineVal(int[] sum, int startRow, int startCol, int delRow, int delCol, int player) {
        int r = startRow, c = startCol, blank = 0;
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
                        int index = count * 2 - (open ? 0 : 1) - 1;
                        sum[index]++;
                    }
                } else {
                    if (space >= 5) {
                        int index = 8 + count * 2 - (open ? 0 : 1) - 1;
                        sum[index]++;
                    }
                }
            }
        }
    }

    private int sumCombinations(int[] sum) {
        int val = 0;
        val += (sum[0] - sum[8]) * 10;
        val += (sum[1] - sum[9]) * 100;
        val += (sum[2] - sum[10]) * 100;
        val += (sum[3] - sum[11]) * 1000;
        val += (sum[4] - sum[12]) * 1000;
        val += (sum[5] - sum[13]) * 10000;
        val += (sum[6] - sum[14]) * 10000;
        val += (sum[7] - sum[15]) * 100000;
        return val;
    }

    protected Set<String> valuableMoves() {
        return valuable_moves;
    }

    protected State dup() {
        return new State(expend, board, valuable_moves);
    }

    private String pt2str(int r, int c) {
        return r + "," + c;
    }

    private boolean insideBoard(int r, int c) {
        return r >= 0 && r < 15 && c >= 0 && c < 15;
    }
}