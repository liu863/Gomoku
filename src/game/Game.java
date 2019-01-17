package game;

import java.util.List;
import java.util.ArrayList;

public class Game {

    private int[][] gameboard; //15 rows & cols
    private int player;
    private boolean complete;
    private List<int[]> history; //row; col; player

    /**
     * New game without any move
     */
    public Game() {
        player = 1;
        complete = false;
        history = new ArrayList<>();
        gameboard = new int[15][15];
    }

    /**
     * Start the game with a given stage.
     *
     * @param board Customized uncompleteed game
     * @throws IllegalArgumentException When gameboard violate rules
     */
    public Game(int[][] board) throws IllegalArgumentException {
        this();
        //TODO: check board first
        if (board.length != 15 || board[0].length != 15) {
            throw new IllegalArgumentException();
        }
        for(int i = 0; i < 15; i++) {
            for (int j = 0; j < 15; j++) {
                gameboard[i][j] = board[i][j];
            }
        }
    }

    /**
     * Set a piece with current player's value on given location.
     *
     * @param r Row index
     * @param c Column index
     * @return true - Move is successful, false - Move is invalid.
     */
    public boolean move(int r, int c) {
        if (r < 0 || r > 14 || c < 0 || c > 14 || gameboard[r][c] != 0 || complete) {
            return false;
        } else {
            gameboard[r][c] = player;
            history.add(0, new int[]{r, c, player});
            player = player == 1 ? 2 : 1;
            changeStatus(r, c);
            return true;
        }
    }

    /**
     * Check if the player at given location won this game
     */
    private void changeStatus(int r, int c) {
        int piece = gameboard[r][c], count = 1;
        if (piece == 0) return;
        for (int i = r + 1; i < 15 && gameboard[i][c] == piece && count < 5; i++, count++) ;
        for (int i = r - 1; i >= 0 && gameboard[i][c] == piece && count < 5; i--, count++) ;
        if (count >= 5) {
            complete = true;
            return;
        }
        count = 1;
        for (int j = c + 1; j < 15 && gameboard[r][j] == piece && count < 5; j++, count++) ;
        for (int j = c - 1; j >= 0 && gameboard[r][j] == piece && count < 5; j--, count++) ;
        if (count >= 5) {
            complete = true;
            return;
        }
        count = 1;
        for (int i = r + 1, j = c + 1; i < 15 && j < 15 && gameboard[i][j] == piece && count < 5; i++, j++, count++) ;
        for (int i = r - 1, j = c - 1; i >= 0 && j >= 0 && gameboard[i][j] == piece && count < 5; i--, j--, count++) ;
        if (count >= 5) {
            complete = true;
            return;
        }
        count = 1;
        for (int i = r + 1, j = c - 1; i < 15 && j >= 0 && gameboard[i][j] == piece && count < 5; i++, j--, count++) ;
        for (int i = r - 1, j = c + 1; i >= 0 && j < 15 && gameboard[i][j] == piece && count < 5; i--, j++, count++) ;
        if (count >= 5) {
            complete = true;
            return;
        }
    }

    /**
     * Return the last move of this game
     *
     * @return int[0] - Row index of last move,
     * int[1] - Column index of last move.
     */
    public int[] getLastMove() {
        if (history.isEmpty())
            return new int[]{0, 0, 0};
        return history.get(0).clone();
    }

    /**
     * @return game board as 15 * 15 int array.
     */
    public int[][] getGameboard() {
        int[][] ret = new int[15][];
        for (int i = 0; i < 15; i++) {
            ret[i] = gameboard[i].clone();
        }
        return ret;
    }

    /**
     * Return current player
     *
     * @return 1 - Player with O (black) key,
     * 2 - Player with X (white) key.
     */
    public int getPlayer() {
        return player;
    }

    /**
     * @return true - One player won this game,
     * false - No one won this game.
     */
    public boolean complete() {
        return complete;
    }

    /**
     * @return list of history moves
     */
    public List<int[]> getHistory() {
        return new ArrayList<>(history);
    }

    /**
     * 1 - 'O', 2 - 'X'
     */
    public void printGame() {
        StringBuilder sb = new StringBuilder("  1 2 3 4 5 6 7 8 9 0 1 2 3 4 5\n");
        for (int i = 0; i < 15; i++) {
            sb.append((i + 1) % 10);
            sb.append('|');
            for (int j = 0; j < 15; j++) {
                if (gameboard[i][j] == 1) {
                    sb.append('O');
                } else if (gameboard[i][j] == 2) {
                    sb.append('X');
                } else {
                    sb.append(' ');
                }
                sb.append('|');
            }
            sb.append('\n');
        }
        System.out.print(sb.toString());
    }
}

