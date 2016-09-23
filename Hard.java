package Gomoku;

import java.lang.Math;

public class Hard implements Ai {
    
    private Game g;
    private int[][] values, board;
    //number of recersive steps
    private static int reclvl = 2;
    
    public Hard(Game g) {
        this.g = g;
        values = new int[15][15];
        for (int i = 1; i < 8; i++) {
            for (int j = i; j < 15 - i; j++) {
                values[i][j] = i;
                values[14 - i][j] = i;
                values[j][i] = i;
                values[j][14 - i] = i;
            }
        }
    }
    
    public void setKey() {
        int[] lastmove = g.getLastMove();
        int r = 7, c = 7;
        if (lastmove[2] != 0) {
            int highest = Integer.MIN_VALUE;
            board = g.getGameboard();
            for (int i = 0; i < 15; i++) {
                for (int j = 0; j < 15; j++) {
                    if (board[i][j] != 0) continue;
                    int val = keyVal(i, j, board, 1);
                    if (val > highest) {
                        r = i;
                        c = j;
                        highest = val;
                    }
                }
            }
        }
        if (!g.setKey(r, c)) {
            System.err.println(String.format("AI_HARD_INVALID_LOCATION: %d %d", r, c));
        }
    }
    
    private int keyVal(int r, int c, int[][] board, int round) {
        int player = g.getPlayer();
        if (round == reclvl) {
            board[r][c] = player;
            int ret = calBoard(board);
            board[r][c] = 0;
            return ret;
        }
        else if (round % 2 == 1) {
            board[r][c] = player;
            int min = Integer.MAX_VALUE;
            for (int i = 0; i < 15; i++) {
                for (int j = 0; j < 15; j++) {
                    if (board[i][j] == 0) {
                        min = Math.min(min, keyVal(i, j, board, round + 1));
                    }
                }
            }
            board[r][c] = 0;
            return min;
        }
        else {
            board[r][c] = player == 1 ? 2 : 1;
            int max = Integer.MIN_VALUE;
            for (int i = 0; i < 15; i++) {
                for (int j = 0; j < 15; j++) {
                    if (board[i][j] == 0) {
                        max = Math.max(max, keyVal(i, j, board, round + 1));
                    }
                }
            }
            board[r][c] = 0;
            return max;
        }
    }
    
    private int calBoard(int[][] board) {
        int val = 0;
        int player = g.getPlayer();
        //horizontal 15 rows
        for (int i = 0; i < 15; i++) {
            for (int j = 0; j < 15; ) {
                if (board[i][j] == 0) {
                    j++;
                }
                else {
                    int open = 0, count = 0;
                    if (j > 0 && board[i][j - 1] == 0) open++;
                    if (board[i][j] == player) {
                        while (j < 15 && board[i][j++] == player) count++;
                        if (j < 15 && board[i][j] == 0) open++;
                        val += calSingleVal(count, open, true);
                    }
                    else {
                        int op = player == 1 ? 2 : 1;
                        while (j < 15 && board[i][j++] == op) count++;
                        if (j < 15 && board[i][j] == 0) open++;
                        val += calSingleVal(count, open, false);
                    }
                }
            }
        }
        //vertical 15 cols
        for (int j = 0; j < 15; j++) {
            for (int i = 0; i < 15; ) {
                if (board[i][j] == 0) {
                    i++;
                }
                else {
                    int open = 0, count = 0;
                    if (i > 0 && board[i - 1][j] == 0) open++;
                    if (board[i][j] == player) {
                        while (i < 15 && board[i++][j] == player) count++;
                        if (i < 15 && board[i][j] == 0) open++;
                        val += calSingleVal(count, open, true);
                    }
                    else {
                        int op = player == 1 ? 2 : 1;
                        while (i < 15 && board[i++][j] == op) count++;
                        if (i < 15 && board[i][j] == 0) open++;
                        val += calSingleVal(count, open, false);
                    }
                }
            }
        }
        //diagnal 10 + 10 + 1
        for (int k = 0; k < 21; k++) {
            int i = 0, j = 0;
            if (k < 10) i -= k - 10;
            else if (k > 10) j += k - 10;
            while (i < 15 && j < 15) {
                if (board[i][j] == 0) {
                    i++;
                    j++;
                }
                else {
                    int open = 0, count = 0;
                    if (i > 0 && j > 0 && board[i - 1][j - 1] == 0) open++;
                    if (board[i][j] == player) {
                        while (i < 15 && j < 15 && board[i++][j++] == player) count++;
                        if (i < 15 && j < 15 && board[i][j] == 0) open++;
                        val += calSingleVal(count, open, true);
                    }
                    else {
                        int op = player == 1 ? 2 : 1;
                        while (i < 15 && j < 15 && board[i++][j++] == op) count++;
                        if (i < 15 && j < 15 && board[i][j] == 0) open++;
                        val += calSingleVal(count, open, false);
                    }
                }
            }
        }
        //anti-diagnal 10 + 10 + 1
        for (int k = 0; k < 21; k++) {
            int i = 0, j = 14;
            if (k < 10) j -= 10 - k;
            else if (k > 10) i += k - 10;
            while (i < 15 && j >= 0) {
                if (board[i][j] == 0) {
                    i++;
                    j--;
                }
                else {
                    int open = 0, count = 0;
                    if (i > 0 && j < 14 && board[i - 1][j + 1] == 0) open++;
                    if (board[i][j] == player) {
                        while (i < 15 && j >= 0 && board[i++][j--] == player) count++;
                        if (i < 15 && j >= 0 && board[i][j] == 0) open++;
                        val += calSingleVal(count, open, true);
                    }
                    else {
                        int op = player == 1 ? 2 : 1;
                        while (i < 15 && j >= 0 && board[i++][j--] == op) count++;
                        if (i < 15 && j >= 0 && board[i][j] == 0) open++;
                        val += calSingleVal(count, open, false);
                    }
                }
            }
        }
        return val;
    }
    
    private int calSingleVal(int count, int open, boolean self) {
        if (count >= 5) {
            return self ? 200000000 : -200000000;
        }
        if (open == 0) {
            return 0;
        }
        int val = open;
        while (count > 1) {
            val *= 100;
            count--;
        }
        val *= self ? 1 : -1;
        return val;
    }
    
    private void print(int[][] board) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 15; i++) {
            sb.append('|');
            for (int j = 0; j < 15; j++) {
                sb.append(String.format("%2d", board[i][j]));
                sb.append('|');
            }
            sb.append('\n');
        }
        sb.append('\n');
        System.out.print(sb.toString());
    }
    
    public void printAi() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 15; i++) {
            sb.append('|');
            for (int j = 0; j < 15; j++) {
                sb.append(String.format("%2d", values[i][j]));
                sb.append('|');
            }
            sb.append('\n');
        }
        System.out.print(sb.toString());
    }
}