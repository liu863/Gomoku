package Gomoku;

public class Game {
    
    private int [][] gameboard; //15 rows & cols
    private int[] lastmove = new int[3]; //row; col; player
    private int player; //1 - 'O'; 2 - 'X'
    private boolean complete;
    /**
     * New game without any move
     */
    public Game() {
        gameboard = new int[15][15];
        player = 1;
        complete = false;
    }
    /**
     * Start the game with a given stage.
     * @param gameboard Customized uncompleteed game
     * @exception IllegalArgumentException When gameboard violate rules
     */
    public Game(int[][] gameboard) throws IllegalArgumentException{
        //TODO: check gameboard first
        if (gameboard.length != 15 || gameboard[0].length != 15) {
            throw new IllegalArgumentException();
        }
        this.gameboard = gameboard;
    }
    
    public boolean setKey(int r, int c) {
        if (r < 0 || r > 14 || c < 0 || c > 14 || gameboard[r][c] != 0 || complete) {
            return false;
        }
        else {
            gameboard[r][c] = player;
            lastmove[0] = r;
            lastmove[1] = c;
            lastmove[2] = player;
            player = player == 1 ? 2 : 1;
            changeStatus(r, c);
            return true;
        }
    }
    
    private void changeStatus(int r, int c) {
        int key = gameboard[r][c], count = 1;
        for (int i = r + 1; i < 15 && gameboard[i][c] == key && count < 5; i++, count++);
        for (int i = r - 1; i >= 0 && gameboard[i][c] == key && count < 5; i--, count++);
        if (count >= 5) {
            complete = true;
            return;
        }
        count = 1;
        for (int j = c + 1; j < 15 && gameboard[r][j] == key && count < 5; j++, count++);
        for (int j = c - 1; j >= 0 && gameboard[r][j] == key && count < 5; j--, count++);
        if (count >= 5) {
            complete = true;
            return;
        }
        count = 1;
        for (int i = r + 1, j = c + 1; i < 15 && j < 15 && gameboard[i][j] == key && count < 5; i++, j++, count++);
        for (int i = r - 1, j = c - 1; i >= 0 && j >= 0 && gameboard[i][j] == key && count < 5; i--, j--, count++);
        if (count >= 5) {
            complete = true;
            return;
        }
        count = 1;
        for (int i = r + 1, j = c - 1; i < 15 && j >= 0 && gameboard[i][j] == key && count < 5; i++, j--, count++);
        for (int i = r - 1, j = c + 1; i >= 0 && j < 15 && gameboard[i][j] == key && count < 5; i--, j++, count++);
        if (count >= 5) {
            complete = true;
            return;
        }
    }
    
    public int[] getLastMove() {
        return lastmove.clone();
    }
    
    public int[][] getGameboard() {
        int[][] ret = new int[15][];
        for (int i = 0; i < 15; i++) {
            ret[i] = gameboard[i].clone();
        }
        return ret;
    }
    
    /**
     * Return current player
     * @return 1 - player with O key,
     *         2 - player with X key.
     */
    public int getPlayer() {
        return player;
    }
    
    public boolean complete() {
        return complete;
    }
    
    public void printGame() {
        StringBuilder sb = new StringBuilder("  1 2 3 4 5 6 7 8 9 0 1 2 3 4 5\n");
        for (int i = 0; i < 15; i++) {
            sb.append((i + 1) % 10);
            sb.append('|');
            for (int j = 0; j < 15; j++) {
                if (gameboard[i][j] == 1) {
                    sb.append('O');
                }
                else if (gameboard[i][j] == 2) {
                    sb.append('X');
                }
                else {
                    sb.append(' ');
                }
                sb.append('|');
            }
            sb.append('\n');
        }
        System.out.print(sb.toString());
    }
}

