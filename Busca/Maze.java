import java.awt.*;

public class Maze {
    private int rows, cols;
    private int[][] grid;
    private static final int WALL = 1;
    private static final int FREE = 0;
    private static final int DESTINATION_ROW = 9;
    private static final int DESTINATION_COL = 9;

    public Maze(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
        grid = new int[rows][cols];
        randomize();
    }

    // Cria caminhos livre randômicos
    public void randomize() {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                grid[i][j] = (Math.random() < 0.4) ? WALL : FREE;
            }
        }
        grid[0][0] = FREE;
        grid[DESTINATION_ROW][DESTINATION_COL] = FREE;
    }

    public boolean isFree(int row, int col) {
        if (row < 0 || row >= rows || col < 0 || col >= cols) return false;
        return grid[row][col] == FREE;
    }
    
    // Desenha o labirinto tendo os caminhos livre e ocupados
    public void draw(Graphics g) {
        int cellSize = 50;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (grid[i][j] == WALL) {
                    g.setColor(Color.BLACK);
                } else if (i == DESTINATION_ROW && j == DESTINATION_COL) {
                    g.setColor(Color.GREEN);
                } else {
                    g.setColor(Color.WHITE);
                }
                g.fillRect(j * cellSize, i * cellSize, cellSize, cellSize);
                g.setColor(Color.GRAY);
                g.drawRect(j * cellSize, i * cellSize, cellSize, cellSize);
            }
        }
    }

    public int getDestinationRow() {
        return DESTINATION_ROW;
    }

    public int getDestinationCol() {
        return DESTINATION_COL;
    }

    // Novo método para obter o número de linhas do labirinto
    public int getRows() {
        return rows;
    }
}
