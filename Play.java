package Gomoku;

import java.util.Scanner;

public class Play {
    public static void main(String[] args) {
        Game g = new Game();
        Scanner s = new Scanner(System.in);
        
        System.out.println("type 1 if you want to play with another player\ntype 2 if you want to play with AI");
        int mode = s.nextInt();
        
        if (mode == 1) {
            while (!g.complete()) {
                g.printGame();
                System.out.println("Enter the location(enter 999 to exit the game):");
                int x = s.nextInt();
                if (x == 999) {
                    System.exit(0);
                }
                int y = s.nextInt();
                if (!g.runGame(x - 1, y - 1)) {
                    System.out.println("Invalid input, try it again.");
                    continue;
                }
            }
            g.printGame();
            System.out.println("player " + (g.getPlayer() == 0 ? 2 : 1) + " win!");
        }
        else {
            Ai ai = new Medium(g);
            System.out.println("type 1 if you want to play first\ntype 2 if you let AI to play first");
            int player = s.nextInt() % 2;
            
            while (!g.complete()) {
                if (player == 1) {
                    g.printGame();
                    System.out.println("Enter the location(enter 999 to exit the game):");
                    int x = s.nextInt();
                    if (x == 999) {
                        System.exit(0);
                    }
                    int y = s.nextInt();
                    if (!g.runGame(x - 1, y - 1)) {
                        System.out.println("Invalid input, try it again.");
                        continue;
                    }
                }
                else {
                    ai.setKey();
                }
                player = (player + 1) % 2;
            }
            g.printGame();
            if (player == 0) {
                System.out.println("You Win!!");
            }
            else {
                System.out.println("You Lose!!");
            }
        }
    }
}