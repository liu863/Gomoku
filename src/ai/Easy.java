/*
This AI calculates the value for all possible moves
and select the move with highest value.
This AI computes the maximum adjacent same color pieces,
and add 10^(# pieces) to the total value.
It considers pieces for both sides.
 */

package ai;

import game.Game;

public class Easy implements Ai {

    private Game g;
    private int[][] values;
    private long total_time = 0;

    public Easy(Game g) {
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

    public long totalTime() {
        return total_time;
    }

    public void move() {
        long start = System.currentTimeMillis();
        int[] nextmove = bestLocation();
        if (!g.move(nextmove[0], nextmove[1])) {
            System.err.println(String.format("AI_MEDIUM_INVALID_LOCATION: %d %d", nextmove[0], nextmove[1]));
        }
        total_time += System.currentTimeMillis() - start;
    }

    private int[] bestLocation() {
        int[] key = {7, 7};
        if (g.getLastMove()[2] == 0) {
            return key;
        }
        int highest = 0;
        for (int i = 0; i < 15; i++) {
            for (int j = 0; j < 15; j++) {
                int val = values[i][j] + calValue(i, j);
                if (val > highest) {
                    highest = val;
                    key[0] = i;
                    key[1] = j;
                }
            }
        }
        return key;
    }

    private int calValue(int row, int col) {
        int[][] board = g.getGameboard();
        if (board[row][col] != 0) {
            return 0;
        }
        int player = g.getPlayer();
        int value = 0, counter, i;
        //dir: 0 for both side, 1 for right & top, -1 for left & bot
        //horizontal
        for (i = 1, counter = 1; i < 5 && counter < 5; i++) {
            if (isPlayer(board, row, col + i, player)) {
                value += Math.pow(10, counter++);
            }
            else {
                break;
            }
        }
        for (i = 1; i < 5 && counter < 5; i++) {
            if (isPlayer(board, row, col - i, player)) {
                value += Math.pow(10, counter++);
            }
            else {
                break;
            }
        }
        //diagonal
        for (i = 1, counter = 1; i < 5 && counter < 5; i++) {
            if (isPlayer(board, row + i, col + i, player)) {
                value += Math.pow(10, counter++);
            }
            else {
                break;
            }
        }
        for (i = 1; i < 5 && counter < 5; i++) {
            if (isPlayer(board, row - i, col - i, player)) {
                value += Math.pow(10, counter++);
            }
            else {
                break;
            }
        }
        //vertical
        for (i = 1, counter = 1; i < 5 && counter < 5; i++) {
            if (isPlayer(board, row + i, col, player)) {
                value += Math.pow(10, counter++);
            }
            else {
                break;
            }
        }
        for (i = 1; i < 5 && counter < 5; i++) {
            if (isPlayer(board, row - i, col, player)) {
                value += Math.pow(10, counter++);
            }
            else {
                break;
            }
        }
        //anti-diagonal
        for (i = 1, counter = 1; i < 5 && counter < 5; i++) {
            if (isPlayer(board, row - i, col + i, player)) {
                value += Math.pow(10, counter++);
            }
            else {
                break;
            }
        }
        for (i = 1; i < 5 && counter < 5; i++) {
            if (isPlayer(board, row + i, col - i, player)) {
                value += Math.pow(10, counter++);
            }
            else {
                break;
            }
        }
        //calculate opponent's board value
        player = player == 1 ? 2 : 1;
        //horizontal
        for (i = 1, counter = 1; i < 5 && counter < 5; i++) {
            if (isPlayer(board, row, col + i, player)) {
                value += Math.pow(10, counter++);
            }
            else {
                break;
            }
        }
        for (i = 1; i < 5 && counter < 5; i++) {
            if (isPlayer(board, row, col - i, player)) {
                value += Math.pow(10, counter++);
            }
            else {
                break;
            }
        }
        //diagonal
        for (i = 1, counter = 1; i < 5 && counter < 5; i++) {
            if (isPlayer(board, row + i, col + i, player)) {
                value += Math.pow(10, counter++);
            }
            else {
                break;
            }
        }
        for (i = 1; i < 5 && counter < 5; i++) {
            if (isPlayer(board, row - i, col - i, player)) {
                value += Math.pow(10, counter++);
            }
            else {
                break;
            }
        }
        //vertical
        for (i = 1, counter = 1; i < 5 && counter < 5; i++) {
            if (isPlayer(board, row + i, col, player)) {
                value += Math.pow(10, counter++);
            }
            else {
                break;
            }
        }
        for (i = 1; i < 5 && counter < 5; i++) {
            if (isPlayer(board, row - i, col, player)) {
                value += Math.pow(10, counter++);
            }
            else {
                break;
            }
        }
        //anti-diagonal
        for (i = 1, counter = 1; i < 5 && counter < 5; i++) {
            if (isPlayer(board, row - i, col + i, player)) {
                value += Math.pow(10, counter++);
            }
            else {
                break;
            }
        }
        for (i = 1; i < 5 && counter < 5; i++) {
            if (isPlayer(board, row + i, col - i, player)) {
                value += Math.pow(10, counter++);
            } else {
                break;
            }
        }
        return value;
    }

    private boolean isPlayer(int[][] board, int x, int y, int player) {
        return x < board.length && x >= 0 && y < board[0].length && y >= 0 && board[x][y] == player;
    }
}