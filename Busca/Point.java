import java.awt.*;
import java.util.List;
import java.util.ArrayList;

public class Point {
    private Maze maze;
    private int row, col;
    private List<java.awt.Point> path;

    public Point(Maze maze) {
        this.maze = maze;
        reset();
    }

    public void reset() {
        row = 0;
        col = 0;
        path = new ArrayList<>();
        path.add(new java.awt.Point(col, row)); // Adiciona a posição inicial à trilha
    }

    // Move para cima
    public boolean moveUp() {
        if (maze.isFree(row - 1, col)) {
            row--;
            path.add(new java.awt.Point(col, row)); // Adiciona a nova posição à trilha
            return true;
        }
        return false;
    }

    // Move para cima
    public boolean moveDown() {
        if (maze.isFree(row + 1, col)) {
            row++;
            path.add(new java.awt.Point(col, row)); // Adiciona a nova posição à trilha
            return true;
        }
        return false;
    }

    // Move para esquerda
    public boolean moveLeft() {
        if (maze.isFree(row, col - 1)) {
            col--;
            path.add(new java.awt.Point(col, row)); // Adiciona a nova posição à trilha
            return true;
        }
        return false;
    }

    // Move para direita
    public boolean moveRight() {
        if (maze.isFree(row, col + 1)) {
            col++;
            path.add(new java.awt.Point(col, row)); // Adiciona a nova posição à trilha
            return true;
        }
        return false;
    }
    
    // Desnha
    public void draw(Graphics g) {
        int cellSize = 50; // Ajusta o tamanho das células para se adequar ao novo tamanho do labirinto
        g.setColor(Color.RED);
        for (java.awt.Point p : path) {
            g.fillOval(p.x * cellSize + cellSize / 4, p.y * cellSize + cellSize / 4, cellSize / 2, cellSize / 2);
        }
        g.setColor(Color.BLUE);
        g.fillOval(col * cellSize + cellSize / 4, row * cellSize + cellSize / 4, cellSize / 2, cellSize / 2);
    }

    // Retorna a linha
    public int getRow() {
        return row;
    }

    // Retorna a coluna
    public int getCol() {
        return col;
    }

    // Define a posição diretamente
    public void setPosition(int row, int col) {
        this.row = row;
        this.col = col;
        path.add(new java.awt.Point(col, row)); // Adiciona a posição à trilha
    }
}
