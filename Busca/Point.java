// Importações necessárias para manipulação gráfica e uso de listas.
import java.awt.*;
import java.util.List;
import java.util.ArrayList;

// Definição da classe `Point` que representa a posição do jogador no labirinto.
public class Point {
    // Declaração dos atributos da classe.
    private Maze maze; // Referência ao objeto `Maze` para interagir com o labirinto.
    private int row, col; // Coordenadas da posição atual do jogador no labirinto.
    private List<java.awt.Point> path; // Lista para armazenar a trilha percorrida pelo jogador.

    // Construtor da classe `Point`.
    public Point(Maze maze) {
        this.maze = maze; // Associa o labirinto ao ponto.
        reset(); // Inicializa a posição do jogador.
    }

    // Método para resetar a posição do jogador ao início do labirinto.
    public void reset() {
        row = 0; // Define a linha inicial como 0.
        col = 0; // Define a coluna inicial como 0.
        path = new ArrayList<>(); // Inicializa a lista de trilhas percorridas.
        path.add(new java.awt.Point(col, row)); // Adiciona a posição inicial à trilha.
    }

    // Método para mover o jogador para cima.
    public boolean moveUp() {
        // Verifica se a nova posição é livre para mover.
        if (maze.isFree(row - 1, col)) {
            row--; // Decrementa a linha para mover para cima.
            path.add(new java.awt.Point(col, row)); // Adiciona a nova posição à trilha.
            return true; // Retorna true indicando que o movimento foi bem-sucedido.
        }
        return false; // Retorna false indicando que o movimento não foi possível.
    }

    // Método para mover o jogador para baixo.
    public boolean moveDown() {
        // Verifica se a nova posição é livre para mover.
        if (maze.isFree(row + 1, col)) {
            row++; // Incrementa a linha para mover para baixo.
            path.add(new java.awt.Point(col, row)); // Adiciona a nova posição à trilha.
            return true; // Retorna true indicando que o movimento foi bem-sucedido.
        }
        return false; // Retorna false indicando que o movimento não foi possível.
    }

    // Método para mover o jogador para a esquerda.
    public boolean moveLeft() {
        // Verifica se a nova posição é livre para mover.
        if (maze.isFree(row, col - 1)) {
            col--; // Decrementa a coluna para mover para a esquerda.
            path.add(new java.awt.Point(col, row)); // Adiciona a nova posição à trilha.
            return true; // Retorna true indicando que o movimento foi bem-sucedido.
        }
        return false; // Retorna false indicando que o movimento não foi possível.
    }

    // Método para mover o jogador para a direita.
    public boolean moveRight() {
        // Verifica se a nova posição é livre para mover.
        if (maze.isFree(row, col + 1)) {
            col++; // Incrementa a coluna para mover para a direita.
            path.add(new java.awt.Point(col, row)); // Adiciona a nova posição à trilha.
            return true; // Retorna true indicando que o movimento foi bem-sucedido.
        }
        return false; // Retorna false indicando que o movimento não foi possível.
    }

    // Método para desenhar o jogador e a trilha no painel.
    public void draw(Graphics g) {
        int cellSize = 50; // Define o tamanho das células no labirinto.
        g.setColor(Color.RED); // Define a cor para a trilha como vermelha.
        
        // Desenha a trilha percorrida.
        for (java.awt.Point p : path) {
            g.fillOval(p.x * cellSize + cellSize / 4, p.y * cellSize + cellSize / 4, cellSize / 2, cellSize / 2);
        }
        
        g.setColor(Color.BLUE); // Define a cor do jogador como azul.
        
        // Desenha o jogador na posição atual.
        g.fillOval(col * cellSize + cellSize / 4, row * cellSize + cellSize / 4, cellSize / 2, cellSize / 2);
    }

    // Método para obter a linha atual do jogador.
    public int getRow() {
        return row; // Retorna a linha atual.
    }

    // Método para obter a coluna atual do jogador.
    public int getCol() {
        return col; // Retorna a coluna atual.
    }

    // Método para definir diretamente a posição do jogador.
    public void setPosition(int row, int col) {
        this.row = row; // Define a nova linha.
        this.col = col; // Define a nova coluna.
        path.add(new java.awt.Point(col, row)); // Adiciona a nova posição à trilha.
    }
}
