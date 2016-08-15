import Gomoku.Game;

public class Test {
    public static void main(String[] args) {
        try {
            Game game = new Game(new int[12][12]);
            game.printGame();
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid input");
        }
    }
}