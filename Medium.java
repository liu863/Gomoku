package Gomoku;

public class Medium implements Ai {
    
    Game g;
    int[][] values;
    
    public Medium(Game g) {
        this.g = g;
        values = new int[15][15];
    }
    
    public void setKey() {
        int[] nextmove = bestLocation();
        if (!g.runGame(nextmove[0], nextmove[1])) {
            System.out.println("AI went for an invalid position!");
        }
    }
    
    private int[] bestLocation() {
        int[] key = {7, 7};
        if (g.getlastMove()[2] == -1) {
            return key;
        }
        int highest = 0;
        for (int i = 0; i < 15; i++) {
            for (int j = 0; j < 15; j++) {
                values[i][j] = calValue(i, j);
                if (values[i][j] > highest) {
                    highest = values[i][j];
                    key[0] = i;
                    key[1] = j;
                }
            }
        }
        return key;
    }
    
    private int calValue(int row, int col) {
        int[][] board = g.getGameboard();
        if (board[row][col] != -1) {
            return -1;
        }
        int player = g.getPlayer();
        int value = 0, dir, pos, i;
        //dir: 0 for both side, 1 for right,up side, -1 for left,down side
        //right-left
        for (i = 1, dir = 0, pos = 1; i < 5 && pos < 5; i++) {
            if (dir == 0 || dir == 1) {
                if ((col + i) < 15 && board[row][col + i] == player) {
                    value += Math.pow(10, pos);
                    pos++;
                }
                else if (dir == 1) {
                    break;
                }
                else {
                    dir = -1;
                }
            }
            if (dir == 0 || dir == -1) {
                if ((col - i) > 0 && board[row][col - i] == player) {
                    value += Math.pow(10, pos);
                    pos++;
                }
                else if (dir == -1) {
                    break;
                }
                else {
                    dir = 1;
                }
            }
        }
        //upright-downleft
        for (i = 1, dir = 0, pos = 1; i < 5 && pos < 5; i++) {
            if (dir == 0 || dir == 1) {
                if ((col + i) < 15 && (row - i) > 0 && board[row - i][col + i] == player) {
                    value += Math.pow(10, pos);
                    pos++;
                }
                else if (dir == 1) {
                    break;
                }
                else {
                    dir = -1;
                }
            }
            if (dir == 0 || dir == -1) {
                if ((row + i) < 15 && (col - i) > 0 && board[row + i][col - i] == player) {
                    value += Math.pow(10, pos);
                    pos++;
                }
                else if (dir == -1) {
                    break;
                }
                else {
                    dir = 1;
                }
            }
        }
        //up-down
        for (i = 1, dir = 0, pos = 1; i < 5 && pos < 5; i++) {
            if (dir == 0 || dir == 1) {
                if ((row - i) > 0 && board[row - i][col] == player) {
                    value += Math.pow(10, pos);
                    pos++;
                }
                else if (dir == 1) {
                    break;
                }
                else {
                    dir = -1;
                }
            }
            if (dir == 0 || dir == -1) {
                if ((row + i) < 15 && board[row + i][col] == player) {
                    value += Math.pow(10, pos);
                    pos++;
                }
                else if (dir == -1) {
                    break;
                }
                else {
                    dir = 1;
                }
            }
        }
        //up-left
        for (i = 1, dir = 0, pos = 1; i < 5 && pos < 5; i++) {
            if (dir == 0 || dir == 1) {
                if ((row - i) > 0 && (col - i) > 0 && board[row - i][col - i] == player) {
                    value += Math.pow(10, pos);
                    pos++;
                }
                else if (dir == 1) {
                    break;
                }
                else {
                    dir = -1;
                }
            }
            if (dir == 0 || dir == -1) {
                if ((row + i) < 15 && (col + i) < 15 && board[row + i][col + i] == player) {
                    value += Math.pow(10, pos);
                    pos++;
                }
                else if (dir == -1) {
                    break;
                }
                else {
                    dir = 1;
                }
            }
        }
        player = (player + 1) % 2;
        //right-left
        for (i = 1, dir = 0, pos = 1; i < 5 && pos < 5; i++) {
            if (dir == 0 || dir == 1) {
                if ((col + i) < 15 && board[row][col + i] == player) {
                    value += Math.pow(10, pos);
                    pos++;
                }
                else if (dir == 1) {
                    break;
                }
                else {
                    dir = -1;
                }
            }
            if (dir == 0 || dir == -1) {
                if ((col - i) > 0 && board[row][col - i] == player) {
                    value += Math.pow(10, pos);
                    pos++;
                }
                else if (dir == -1) {
                    break;
                }
                else {
                    dir = 1;
                }
            }
        }
        //upright-downleft
        for (i = 1, dir = 0, pos = 1; i < 5 && pos < 5; i++) {
            if (dir == 0 || dir == 1) {
                if ((col + i) < 15 && (row - i) > 0 && board[row - i][col + i] == player) {
                    value += Math.pow(10, pos);
                    pos++;
                }
                else if (dir == 1) {
                    break;
                }
                else {
                    dir = -1;
                }
            }
            if (dir == 0 || dir == -1) {
                if ((row + i) < 15 && (col - i) > 0 && board[row + i][col - i] == player) {
                    value += Math.pow(10, pos);
                    pos++;
                }
                else if (dir == -1) {
                    break;
                }
                else {
                    dir = 1;
                }
            }
        }
        //up-down
        for (i = 1, dir = 0, pos = 1; i < 5 && pos < 5; i++) {
            if (dir == 0 || dir == 1) {
                if ((row - i) > 0 && board[row - i][col] == player) {
                    value += Math.pow(10, pos);
                    pos++;
                }
                else if (dir == 1) {
                    break;
                }
                else {
                    dir = -1;
                }
            }
            if (dir == 0 || dir == -1) {
                if ((row + i) < 15 && board[row + i][col] == player) {
                    value += Math.pow(10, pos);
                    pos++;
                }
                else if (dir == -1) {
                    break;
                }
                else {
                    dir = 1;
                }
            }
        }
        //up-left
        for (i = 1, dir = 0, pos = 1; i < 5 && pos < 5; i++) {
            if (dir == 0 || dir == 1) {
                if ((row - i) > 0 && (col - i) > 0 && board[row - i][col - i] == player) {
                    value += Math.pow(10, pos);
                    pos++;
                }
                else if (dir == 1) {
                    break;
                }
                else {
                    dir = -1;
                }
            }
            if (dir == 0 || dir == -1) {
                if ((row + i) < 15 && (col + i) < 15 && board[row + i][col + i] == player) {
                    value += Math.pow(10, pos);
                    pos++;
                }
                else if (dir == -1) {
                    break;
                }
                else {
                    dir = 1;
                }
            }
        }
        return value;
    }
    
    public void printAi() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 15; i++) {
            for (int j = 0; j < 15; j++) {
                sb.append(values[i][j]);
                sb.append(' ');
            }
            sb.append('\n');
        }
        System.out.print(sb.toString());
    }
}