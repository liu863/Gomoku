package ai;

public interface Ai {
    /**
     * Set key on game board, should call game.setKey(int, int) inside.
     */
    void setKey();

    void printAi();
}

