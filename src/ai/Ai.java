package ai;

public interface Ai {
    /**
     * Put price on game board, should call game.move(int, int) inside.
     */
    void move();

    /**
     * Get total compute time since the start of the game
     */
    long totalTime();
}

