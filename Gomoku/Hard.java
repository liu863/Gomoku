package Gomoku;

import java.lang.*;
import java.util.*;

public class Hard implements Ai {
    
    private Game g;
    private int[][] values, board, aiValues;
    //number of recersive steps
    private final int recursionDepth = 4;
    private final int expendSize = 1;
    private final int winningValue = 10000000;
    
    public Hard(Game g) {
        this.g = g;
        values = new int[15][15];
        aiValues = new int[15][15];
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
                    if (board[i][j] != 0 || !valuableMove(i, j)) {
                        aiValues[i][j] = 0;
                        continue;
                    }
                    int val = keyVal(i, j, 1) + values[i][j];
                    //int val = keyVal(i, j, 1);
                    //aiValues[i][j] = val;
                    if (val > highest) {
                        r = i;
                        c = j;
                        highest = val;
                    }
                }
            }
            //printAi();
            //System.out.println(highest);
        }
        if (!g.setKey(r, c)) {
            System.err.println(String.format("AI_HARD_INVALID_LOCATION: %d %d", r, c));
        }
    }
    
    private int keyVal(int r, int c, int round) {
        int player = (g.getPlayer() + round % 2) % 2 + 1, ret = 0;
        board[r][c] = player;
        ret = calBoard();
        if (round < recursionDepth && Math.abs(ret) < winningValue) {
            if (round % 2 == 1) {
                for (int i = 0; i < 15; i++) {
                    for (int j = 0; j < 15; j++) {
                        if (board[i][j] == 0 && valuableMove(i, j)) {
                            int temp = keyVal(i, j, round + 1);
                            if (temp <= -winningValue / (round + 1)) {
                                board[r][c] = 0;
                                return temp;
                            }
                            ret = Math.min(ret, temp);
                        }
                    }
                }
                
            }
            else {
                for (int i = 0; i < 15; i++) {
                    for (int j = 0; j < 15; j++) {
                        if (board[i][j] == 0 && valuableMove(i, j)) {
                            int temp = keyVal(i, j, round + 1);
                            if (temp >= winningValue / (round + 1)) {
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
        int val = 0;
        int[] sum = new int[18];
        //horizontal 15 rows and vertical 15 cols
        for (int i = 0; i < 15; i++) {
            calLineVal(sum, i, 0, 0, 1);
            calLineVal(sum, 0, i, 1, 0);
        }
        //diagnal 10 + 10 + 1
        for (int k = 0; k < 21; k++) {
            int i = 0, j = 0;
            if (k < 10) i -= k - 10;
            else if (k > 10) j += k - 10;
            calLineVal(sum, i, j, 1, 1);
        }
        //anti-diagnal 10 + 10 + 1
        for (int k = 0; k < 21; k++) {
            int i = 0, j = 14;
            if (k < 10) j -= 10 - k;
            else if (k > 10) i += k - 10;
            calLineVal(sum, i, j, 1, -1);
        }
        val = sumCombinations(sum);
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
                }
                else {
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
        if (sum[8] > 0) val = winningValue;
        else if (sum[17] > 0) val = -winningValue;
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
        for (int i = r - expendSize; i <= r + expendSize; i++) {
            for (int j = c - expendSize; j <= c + expendSize; j++) {
                if (!insideBoard(i, j) || board[i][j] == 0) continue;
                int dis = Math.max(Math.abs(i - r), Math.abs(j - c));
                value += expendSize - dis + 1;
            }
        }
        return value >= expendSize;
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
                sb.append(String.format("%5d|", aiValues[i][j]));
            }
            sb.append('\n');
        }
        System.out.print(sb.toString());
    }
}