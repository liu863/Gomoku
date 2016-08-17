package Gomoku;

public class Hard implements Ai {
    
    private Game g;
    private int[][] values;
    
    public Hard(Game g) {
        this.g = g;
        values = new int[15][15];
        for (int i = 1; i < 8; i++) {
            for (int j = i; j < 15 - i; j++) {
                values[i][j] = i;
                values[14 - i][j] = i;
                values[j][i] = i;
                values[j][14 - i] = i;
            }
        }
    }
    
    public void setKey() {
    
    }
    
    public void printAi() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 15; i++) {
            sb.append('|');
            for (int j = 0; j < 15; j++) {
                sb.append(String.format("%2d", values[i][j]));
                sb.append('|');
            }
            sb.append('\n');
        }
        System.out.print(sb.toString());
    }
}