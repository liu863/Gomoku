package game;

import ai.*;
import java.util.Scanner;

public class Play {
    public static void main(String[] args) {
        Game g = new Game();
        Scanner s = new Scanner(System.in);

        System.out.println("1 -- play with another player\n2 -- play against AI\n3 -- AI vs AI");
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
                if (!g.move(x - 1, y - 1)) {
                    System.out.println("Invalid input, try it again.");
                    continue;
                }
            }
            g.printGame();
            System.out.println("Player " + (g.getPlayer() == 1 ? 2 : 1) + " win!");
        }
        else if (mode == 2) {
            Ai ai;
            System.out.println("Choose difficulty:\n0 -- AI.Easy\n1 -- AI.Medium\n2 -- AI.Hard");
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
                    if (!g.move(x - 1, y - 1)) {
                        System.out.println("Invalid input, try it again.");
                        continue;
                    }
                } else {
                    ai.move();
                    int[] last = g.getLastMove();
                    System.out.format("ai:(%d, %d)%n", last[0] + 1, last[1] + 1);
                }
                player = (player + 1) % 2;
            }
            g.printGame();
            if (player == 0) {
                System.out.println("You Win!");
            } else {
                System.out.println("You Lose!");
            }
        }
        else if (mode == 3) {
            Ai[] ai = new Ai[2];
            System.out.println("Choose first AI:\n0 -- Easy\n1 -- Medium\n2 -- Hard");
            int level = s.nextInt();
            if (level == 0) ai[0] = new Easy(g);
            else if (level == 1) ai[0] = new Medium(g);
            else ai[0] = new Hard(g);
            System.out.println("Choose second AI:\n0 -- Easy\n1 -- Medium\n2 -- Hard");
            level = s.nextInt();
            if (level == 0) ai[1] = new Easy(g);
            else if (level == 1) ai[1] = new Medium(g);
            else ai[1] = new Hard(g);

            for (int i = 0; i < 1; i++) {
                int turn = 0;
                while (!g.complete()) {
                    ai[turn].move();
//                int[] last = g.getLastMove();
//                System.out.format("ai[%d]:(%d, %d)%n", turn, last[0] + 1, last[1] + 1);
//                g.printGame();
                    turn = (turn + 1) % 2;
                }
                g.printGame();
                if (turn == 0) {
                    System.out.println("AI 1 Win!");
                } else {
                    System.out.println("AI 2 Win!");
                }
                System.out.format("Total time for AI 1: %d\n", ai[0].totalTime());
                System.out.format("Total time for AI 2: %d\n", ai[1].totalTime());
            }
        }
    }
}