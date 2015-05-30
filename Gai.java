public class Gai {
    private Gomoku g;
    private boolean newgame = true;
    private int[][] boardValue = new int[15][15];
    
    public Gai(Gomoku g) {
        this.g = g;
        initAi();
    }
    
    class Boardunit {
        int player;
        int value;
        int value0;
        int value1;
    }
    
    private Boardunit[][] AI = new Boardunit[15][15];
    
    public void initAi() {
        for (int i = 0; i < 15; i++) {
            for (int j = 0; j < 15; j++) {
                AI[i][j] = new Boardunit();
                AI[i][j].player = -1;
                AI[i][j].value = 0;
                AI[i][j].value0 = 0;
                AI[i][j].value1 = 0;
            }
        }
    }
    
    private void updateAi(int x, int y, int player) {
        newgame = false;
        AI[x][y].player = player;
        AI[x][y].value = 0;
        AI[x][y].value0 = 0;
        AI[x][y].value1 = 0;
    }
    
    private void calValue() {
        for (int i = 0; i < 15; i++) {
            for (int j = 0; j < 15; j++) {
                if (AI[i][j].player == -1)
                    AI[i][j].value = calSingleval(i, j);
            }
        }
    }
    
    private int calSingleval(int x, int y) {
        int val = 0;
        boolean left = true;
        int count;
        int index;
        //cal value0
        for (count = 0, index = 1; count < 5; count++) {
            if (left) {
                
            }
            else {
                
            }
        }
        
        //cal value1
        
        
        val = AI[x][y].value0 + AI[x][y].value1;
        //val = AI[x][y].value0 > AI[x][y].value1 ? AI[x][y].value0 : AI[x][y].value1;
        //System.out.println("location: " + x + " " + y + " value0: " + AI[x][y].value0 + " value1: " + AI[x][y].value1 + " val: " + val);
        return val;
    }
    
    private int[] bestLocation() {
        calValue();
        //printAi();
        int[] key = new int[2];
        int val = 0;
        if (newgame) {
            key[0] = 7;
            key[1] = 7;
            return key;
        }
        
        for (int i = 0; i < 15; i++) {
            for (int j = 0; j < 15; j++) {
                if (AI[i][j].player == -1) {
                    if (AI[i][j].value > val) {
                        key[0] = i;
                        key[1] = j;
                        val = AI[i][j].value;
                    }
                }
            }
        }
        
        return key;
    }
    
    public void printAi() {
        String str = "";
        for (int i = 0; i < 15; i++) {
            for (int j = 0; j < 15; j++) {
                str += AI[i][j].value + " ";
                //System.out.print(AI[i][j].value + " ");
            }
            str += "\n";
            //System.out.println();
        }
        System.out.print(str);
    }
    
    public void setKey() {
        g.printGame();
        int[] lastmove = g.getlastMove();
        if (lastmove[0] != 999)
            updateAi(lastmove[0], lastmove[1], 1);
        int[] nextmove = bestLocation();
        g.runGame(nextmove[0], nextmove[1]);
        updateAi(nextmove[0], nextmove[1], 0);
    }
    
    public static void main(String[] ris) {
        Gomoku g = new Gomoku();
        Gai ai = new Gai(g);
        g.initGame();
        //g.printGame();
        ai.setKey();
        g.printGame();
        //ai.printAi();
    }
    
}

/* copy the gameboard */

/* calculate the value of each box */

