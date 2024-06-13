// Importação necessária para manipulação gráfica.
import java.awt.*;

// Definição da classe `Maze` que representa o labirinto.
public class Maze {
    // Declaração dos atributos da classe.
    private int rows, cols; // Número de linhas e colunas do labirinto.
    private int[][] grid; // Matriz que armazena a estrutura do labirinto (caminhos livres e paredes).
    private static final int WALL = 1; // Valor representando uma parede.
    private static final int FREE = 0; // Valor representando um caminho livre.
    private static final int DESTINATION_ROW = 9; // Linha da posição de destino no labirinto.
    private static final int DESTINATION_COL = 14; // Coluna da posição de destino no labirinto.

    // Construtor da classe `Maze`.
    public Maze(int rows, int cols) {
        this.rows = rows; // Inicializa o número de linhas.
        this.cols = cols; // Inicializa o número de colunas.
        grid = new int[rows][cols]; // Cria a matriz para representar o labirinto.
        randomize(); // Gera a configuração inicial aleatória do labirinto.
    }

    // Método para gerar caminhos livres aleatórios no labirinto.
    public void randomize() {
        // Percorre todas as células do labirinto.
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                // Define cada célula como parede ou caminho livre com base em uma probabilidade de 40% para ser parede.
                grid[i][j] = (Math.random() < 0.4) ? WALL : FREE;
            }
        }
        grid[0][0] = FREE; // Garante que a célula inicial (0,0) seja sempre um caminho livre.
        grid[DESTINATION_ROW][DESTINATION_COL] = FREE; // Garante que a célula de destino seja sempre um caminho livre.
    }

    // Método para verificar se uma célula específica é um caminho livre.
    public boolean isFree(int row, int col) {
        // Verifica se a posição está dentro dos limites do labirinto.
        if (row < 0 || row >= rows || col < 0 || col >= cols) return false;
        return grid[row][col] == FREE; // Retorna verdadeiro se a célula for um caminho livre.
    }

    // Método para desenhar o labirinto no painel gráfico.
    public void draw(Graphics g) {
        int cellSize = 50; // Define o tamanho de cada célula do labirinto.
        
        // Percorre todas as células do labirinto.
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                // Define a cor da célula com base no seu estado.
                if (grid[i][j] == WALL) {
                    g.setColor(Color.BLACK); // Paredes são desenhadas em preto.
                } else if (i == DESTINATION_ROW && j == DESTINATION_COL) {
                    g.setColor(Color.GREEN); // A célula de destino é desenhada em verde.
                } else {
                    g.setColor(Color.WHITE); // Caminhos livres são desenhados em branco.
                }
                
                // Preenche a célula com a cor definida.
                g.fillRect(j * cellSize, i * cellSize, cellSize, cellSize);
                g.setColor(Color.GRAY); // Define a cor para os contornos das células.
                g.drawRect(j * cellSize, i * cellSize, cellSize, cellSize); // Desenha o contorno da célula.
            }
        }
    }

    // Método para obter a linha da posição de destino.
    public int getDestinationRow() {
        return DESTINATION_ROW; // Retorna a linha de destino predefinida.
    }

    // Método para obter a coluna da posição de destino.
    public int getDestinationCol() {
        return DESTINATION_COL; // Retorna a coluna de destino predefinida.
    }

    // Método para obter o número de linhas do labirinto.
    public int getRows() {
        return rows; // Retorna o número de linhas.
    }

    // Método para obter o número de colunas do labirinto.
    public int getCols() {
        return cols; // Retorna o número de colunas.
    }
}
