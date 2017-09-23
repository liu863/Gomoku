package Game;

import AI.*;
import java.util.Scanner;

public class Play {
    public static void main(String[] args) {
        Game g = new Game();
        Scanner s = new Scanner(System.in);
        
        System.out.println("1 -- play with another player\n2 -- play against AI");
        int mode = s.nextInt();
        
        if (mode == 1) {
            while (!g.complete()) {
                g.printGame();
                System.out.println("Enter the location(999 to exit the game):");
                int x = s.nextInt();
                if (x == 999) {
                    System.exit(0);
                }
                int y = s.nextInt();
                if (!g.setKey(x - 1, y - 1)) {
                    System.out.println("Invalid input, try it again.");
                    continue;
                }
            }
            g.printGame();
            System.out.println("Player " + (g.getPlayer() == 1 ? 2 : 1) + " win!");
        }
        else {
            Ai ai;
            System.out.println("Choose difficulty:\n0 -- Easy\n1 -- Medium\n2 -- Hard");
            int level = s.nextInt();
            if (level == 0) ai = new Easy(g);
            else if (level == 1) ai = new Medium(g);
            else ai = new Hard(g);
                
            System.out.println("1 -- you play first\n2 -- AI play first");
            int player = s.nextInt() % 2;
            
            while (!g.complete()) {
                if (player == 1) {
                    g.printGame();
                    System.out.println("Enter the location(999 to exit the game):");
                    int x = s.nextInt();
                    if (x == 999) {
                        System.exit(0);
                    }
                    int y = s.nextInt();
                    if (!g.setKey(x - 1, y - 1)) {
                        System.out.println("Invalid input, try it again.");
                        continue;
                    }
                }
                else {
                    ai.setKey();
                    int[] last = g.getLastMove();
                    System.out.format("AI:(%d, %d)%n", last[0] + 1, last[1] + 1);
                }
                player = (player + 1) % 2;
            }
            g.printGame();
            if (player == 0) {
                System.out.println("You Win!");
            }
            else {
                System.out.println("You Lose!");
            }
        }
    }
}