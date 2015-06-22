import java.lang.Math;

public class Gai {
    private Gomoku g;
    private boolean newgame = true;
    private int[][] boardValue = new int[15][15];
    private int level;
    
    public Gai(Gomoku g, int level) {
        this.g = g;
        this.level = level;
        initAi();
    }
    
    private void initAi() {
        for (int i = 0; i < 15; i++) {
            for (int j = 0; j < 15; j++) {
                boardValue[i][j] = 0;
            }
        }
    }
    
    public void setKey() {
        int[] nextmove = bestLocation();
        if (!g.runGame(nextmove[0], nextmove[1])) {
            System.out.println("AI went for a invalid position!");
        }
    }
    
    private int[] bestLocation() {
        if (newgame && g.getlastMove()[0] == -1) {
            newgame = false;
            int[] key = {7, 7};
            return key;
        }
        int[] key = new int[2];
        int highest = 0;
        for (int i = 0; i < 15; i++) {
            for (int j = 0; j < 15; j++) {
                if (level == 1) {
                    boardValue[i][j] = calValue1(i, j);
                }
                else if (level == 2) {
                    boardValue[i][j] = calValue2(i, j);
                }
                else if (level == 3) {
                    ;
                }
                if (boardValue[i][j] > highest) {
                    highest = boardValue[i][j];
                    key[0] = i;
                    key[1] = j;
                }
            }
        }
        return key;
    }
    
    private int calValue1(int raw, int col) {
        int[][] board = g.getGameboard();
        if (board[raw][col] != -1) {
            return -1;
        }
        int player = g.getPlayer();
        int value = 0, dir, pos, i;
        //dir: 0 for both side, 1 for right,up side, -1 for left,down side
        //right-left
        for (i = 1, dir = 0, pos = 1; i < 5 && pos < 5; i++) {
            if (dir == 0 || dir == 1) {
                if ((col + i) < 15 && board[raw][col + i] == player) {
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
                if ((col - i) > 0 && board[raw][col - i] == player) {
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
                if ((col + i) < 15 && (raw - i) > 0 && board[raw - i][col + i] == player) {
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
                if ((raw + i) < 15 && (col - i) > 0 && board[raw + i][col - i] == player) {
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
                if ((raw - i) > 0 && board[raw - i][col] == player) {
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
                if ((raw + i) < 15 && board[raw + i][col] == player) {
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
                if ((raw - i) > 0 && (col - i) > 0 && board[raw - i][col - i] == player) {
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
                if ((raw + i) < 15 && (col + i) < 15 && board[raw + i][col + i] == player) {
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
    
    private int calValue2(int raw, int col) {
        int[][] board = g.getGameboard();
        if (board[raw][col] != -1) {
            return -1;
        }
        int player = g.getPlayer();
        int value = 0, dir, pos, i;
        //dir: 0 for both side, 1 for right,up side, -1 for left,down side
        //right-left
        for (i = 1, dir = 0, pos = 1; i < 5 && pos < 5; i++) {
            if (dir == 0 || dir == 1) {
                if ((col + i) < 15 && board[raw][col + i] == player) {
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
                if ((col - i) > 0 && board[raw][col - i] == player) {
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
                if ((col + i) < 15 && (raw - i) > 0 && board[raw - i][col + i] == player) {
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
                if ((raw + i) < 15 && (col - i) > 0 && board[raw + i][col - i] == player) {
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
                if ((raw - i) > 0 && board[raw - i][col] == player) {
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
                if ((raw + i) < 15 && board[raw + i][col] == player) {
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
                if ((raw - i) > 0 && (col - i) > 0 && board[raw - i][col - i] == player) {
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
                if ((raw + i) < 15 && (col + i) < 15 && board[raw + i][col + i] == player) {
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
                if ((col + i) < 15 && board[raw][col + i] == player) {
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
                if ((col - i) > 0 && board[raw][col - i] == player) {
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
                if ((col + i) < 15 && (raw - i) > 0 && board[raw - i][col + i] == player) {
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
                if ((raw + i) < 15 && (col - i) > 0 && board[raw + i][col - i] == player) {
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
                if ((raw - i) > 0 && board[raw - i][col] == player) {
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
                if ((raw + i) < 15 && board[raw + i][col] == player) {
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
                if ((raw - i) > 0 && (col - i) > 0 && board[raw - i][col - i] == player) {
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
                if ((raw + i) < 15 && (col + i) < 15 && board[raw + i][col + i] == player) {
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
        String str = "";
        for (int i = 0; i < 15; i++) {
            for (int j = 0; j < 15; j++) {
                str += boardValue[i][j] + " ";
                //System.out.print(AI[i][j].value + " ");
            }
            str += "\n";
            //System.out.println();
        }
        System.out.print(str);
    }
}

