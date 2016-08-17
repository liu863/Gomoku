import Gomoku.Game;
import Gomoku.Ai;
import Gomoku.Easy;
import Gomoku.Medium;
import Gomoku.Hard;

public class Test {
    public static void main(String[] args) {
        Test t = new Test();
        t.game();
        //t.aiHard();
    }
    
    private void game() {
        //new game
        Game game = new Game();
        //initialization
        game.printGame();
        
        //customized game
        //invalid board length
        try {
            game = new Game(new int[12][12]);
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid input");
        }
        //invalid moves
        int[][] board = new int[15][15];
    }
    
    private void aiHard() {
        Game g = new Game();
        Ai ai = new Hard(g);
        //check values initialization
        ai.printAi();
    }
}