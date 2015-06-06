import java.util.Scanner;

public class Gomoku {
    
    private int [][] Gameboard = new int[15][15];
    
    private int[] Lastmove = new int[3]; //raw; col; player
    
    private int player; //0-black; 1-white
    
    public int[] getlastMove() {
        return Lastmove;
    }
    
    public int[][] getGameboard() {
        return Gameboard;
    }
    
    public int getPlayer() {
        return player;
    }
    
    public void initGame() {
        //init Gameboard
        for (int i = 0; i < 15; i++) {
            for (int j = 0; j < 15; j++) {
                Gameboard[i][j] = -1;
            }
        }
        //init Lastmove
        Lastmove[0] = Lastmove[1] = Lastmove[2] = -1;
        //init player
        player = 0;
    }
    
    public boolean runGame(int x, int y) {
        if (x < 0 || x > 14 || y < 0 || y > 14 || Gameboard[x][y] != -1) {
            return false;
        }
        else {
            Gameboard[x][y] = player;
            Lastmove[0] = x;
            Lastmove[1] = y;
            Lastmove[2] = player;
            player = (player + 1) % 2;
            return true;
        }
    }
    
    public boolean winGame() {
        int key;
        for (int i = 0; i < 15; i++) {
            for (int j = 0; j < 15; j++) {
                key = Gameboard[i][j];
                if (key != -1) {
                    if (j < 11 && key == Gameboard[i][j + 1] && key == Gameboard[i][j + 2] && key == Gameboard[i][j + 3] && key == Gameboard[i][j + 4]) {
                        return true;
                    }
                    else if (i < 11 && key == Gameboard[i + 1][j] && key == Gameboard[i + 2][j] && key == Gameboard[i + 3][j] && key == Gameboard[i + 4][j]) {
                        return true;
                    }
                    else if (j < 11 && i < 11 && key == Gameboard[i + 1][j + 1] && key == Gameboard[i + 2][j + 2] && key == Gameboard[i + 3][j + 3] && key == Gameboard[i + 4][j + 4]) {
                        return true;
                    }
                    else if (j > 3 && i < 11 && key == Gameboard[i + 1][j - 1] && key == Gameboard[i + 2][j - 2] && key == Gameboard[i + 3][j - 3] && key == Gameboard[i + 4][j - 4]) {
                        return true;
                    }
                }
            }
        } 
        return false;
    }
    
    public void printGame() {
        String game = "  1 2 3 4 5 6 7 8 9 0 1 2 3 4 5\n";
        //System.out.println("  1 2 3 4 5 6 7 8 9 0 1 2 3 4 5");
        for (int i = 0; i < 15; i++) {
            game += (i + 1) % 10 + "|";
            //System.out.print((i + 1) % 10 + "|");
            for (int j = 0; j < 15; j++) {
                if (Gameboard[i][j] != -1) {
                    game += Gameboard[i][j] + "|";
                    //System.out.print(Gameboard[i][j] + "|");
                }
                else {
                    game += " |";
                    //System.out.print(" |");
                }
            }
            game += "\n";
            //System.out.println();
        }
        System.out.print(game);
    }
    
    public static void main(String[] ris) {
        int player;
        Gomoku g = new Gomoku();
        Scanner s = new Scanner(System.in);
        
        System.out.println("type 1 if you want to play with another player\ntype 2 if you want to play with AI");
        int mode = s.nextInt();
        
        if (mode == 1) {
            g.initGame();
            while (!g.winGame()) {
                g.printGame();
                System.out.println("Enter the location(enter 999 to exit the game):");
                int x = s.nextInt();
                if (x == 999)
                    System.exit(0);
                int y = s.nextInt();
                if (!g.runGame(x - 1, y - 1)) {
                    System.out.println("try it again!!!!!");
                    continue;
                }
            }
            player = g.getPlayer();
            player = (player + 1) % 2 == 1 ? 2 : 1;
            g.printGame();
            System.out.println("player " + player + " win!");
        }
        else {
            g.initGame();
            Gai ai = new Gai(g, 1);
            System.out.println("type 1 if you want to play first\ntype 2 if you let AI to play first");
            player = s.nextInt() % 2;
            
            while (!g.winGame()) {
                if (player == 1) {
                    g.printGame();
                    System.out.println("Enter the location(enter 999 to exit the game):");
                    int x = s.nextInt();
                    if (x == 999) {
                        System.exit(0);
                    }
                    int y = s.nextInt();
                    if (!g.runGame(x - 1, y - 1)) {
                        System.out.println("try it again!!!!!");
                        continue;
                    }
                }
                else {
                    ai.setKey();
                }
                player = (player + 1) % 2;
            }
            player = (player + 1) % 2;
            g.printGame();
            if (player == 1) {
                System.out.println("You Win!!");
            }
            else {
                System.out.println("You Lose!!");
            }
        }
    }
}

