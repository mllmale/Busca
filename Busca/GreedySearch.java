// Importações necessárias para usar estruturas de dados e comparadores.
import java.util.PriorityQueue; // Fila de prioridade para a implementação do algoritmo de busca gulosa.
import java.util.HashSet; // Conjunto para rastrear pontos visitados e caminhos únicos falhos.
import java.util.LinkedList; // Lista encadeada para armazenar caminhos.
import java.util.List; // Interface para listas.
import java.util.Set; // Interface para conjuntos.
import java.util.Comparator; // Comparador para ordenar estados na fila de prioridade.

// Definição da classe `GreedySearch` que implementa a busca gulosa no labirinto.
public class GreedySearch {
    // Declaração dos atributos da classe.
    private Maze maze; // Referência ao labirinto onde a busca será realizada.
    private Point start; // Ponto de início da busca.
    private int destRow; // Linha da célula de destino.
    private int destCol; // Coluna da célula de destino.
    private List<List<java.awt.Point>> failedPaths; // Lista de caminhos que falharam.
    private List<java.awt.Point> successfulPath; // Caminho bem-sucedido, se encontrado.
    private Set<String> uniqueFailedPaths; // Conjunto para armazenar representações únicas dos caminhos falhos.

    // Construtor da classe `GreedySearch`.
    public GreedySearch(Maze maze, Point start) {
        this.maze = maze; // Inicializa a referência ao labirinto.
        this.start = start; // Inicializa o ponto de início.
        this.destRow = maze.getDestinationRow(); // Obtém a linha de destino a partir do labirinto.
        this.destCol = maze.getDestinationCol(); // Obtém a coluna de destino a partir do labirinto.
        this.failedPaths = new LinkedList<>(); // Inicializa a lista de caminhos falhos.
        this.successfulPath = new LinkedList<>(); // Inicializa a lista do caminho bem-sucedido.
        this.uniqueFailedPaths = new HashSet<>(); // Inicializa o conjunto para caminhos falhos únicos.
    }

    // Método para iniciar a busca, tentando várias vezes até encontrar um caminho ou esgotar as tentativas.
    public boolean search(int minFailedAttempts, int maxFailedAttempts) {
        // Calcula um número aleatório de tentativas falhas baseado nos limites fornecidos.
        int failAttempts = (int) (minFailedAttempts + Math.random() * (maxFailedAttempts - minFailedAttempts + 1));

        // Realiza tentativas de busca.
        for (int attempt = 0; attempt < failAttempts + 1; attempt++) {
            if (attempt < failAttempts) {
                // Tenta buscar um caminho falho.
                searchGreedy(false);
            } else {
                // Na última tentativa, busca o caminho correto.
                return searchGreedy(true);
            }
        }
        return false; // Retorna falso se não encontrar um caminho bem-sucedido.
    }

    // Método privado que realiza a busca gulosa.
    private boolean searchGreedy(boolean findCorrectPath) {
        // Fila de prioridade para armazenar estados a serem explorados, ordenada pelo heurístico.
        PriorityQueue<State> queue = new PriorityQueue<>(Comparator.comparingInt(State::getHeuristic));
        Set<java.awt.Point> visited = new HashSet<>(); // Conjunto para rastrear pontos visitados.
        List<java.awt.Point> currentPath = new LinkedList<>(); // Lista para armazenar o caminho atual.

        // Adiciona o estado inicial à fila de prioridade.
        queue.add(new State(start.getRow(), start.getCol(), 0, currentPath));

        // Loop até que a fila de prioridade esteja vazia.
        while (!queue.isEmpty()) {
            State current = queue.poll(); // Obtém o estado com menor heurística.
            currentPath = new LinkedList<>(current.path); // Clona o caminho atual.
            currentPath.add(new java.awt.Point(current.col, current.row)); // Adiciona a posição atual ao caminho.

            // Se a posição atual for a posição de destino.
            if (current.row == destRow && current.col == destCol) {
                if (findCorrectPath) {
                    successfulPath = currentPath; // Armazena o caminho bem-sucedido.
                    return true; // Retorna verdadeiro indicando sucesso.
                } else {
                    // Gera uma representação única do caminho falho.
                    String pathRepresentation = generatePathRepresentation(currentPath);
                    // Se o caminho ainda não foi registrado.
                    if (!uniqueFailedPaths.contains(pathRepresentation)) {
                        failedPaths.add(currentPath); // Adiciona o caminho falho à lista.
                        uniqueFailedPaths.add(pathRepresentation); // Marca o caminho como visto.
                    }
                    return false; // Retorna falso indicando falha.
                }
            }

            visited.add(new java.awt.Point(current.col, current.row)); // Marca a posição atual como visitada.
            // Explora os vizinhos da posição atual.
            exploreNeighbors(current, queue, visited, findCorrectPath, currentPath.size());
        }

        // Se a busca falhou, ainda registra o caminho como falho, se aplicável.
        if (!findCorrectPath) {
            String pathRepresentation = generatePathRepresentation(currentPath);
            if (!uniqueFailedPaths.contains(pathRepresentation)) {
                failedPaths.add(currentPath);
                uniqueFailedPaths.add(pathRepresentation);
            }
        }

        return false; // Retorna falso indicando que a busca falhou.
    }

    // Método para explorar os vizinhos da célula atual.
    private void exploreNeighbors(State current, PriorityQueue<State> queue, Set<java.awt.Point> visited, boolean findCorrectPath, int pathLength) {
        int[][] directions = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}}; // Vetor de direções: cima, baixo, esquerda, direita.

        // Percorre todas as direções possíveis.
        for (int[] dir : directions) {
            int newRow = current.row + dir[0]; // Calcula a nova linha.
            int newCol = current.col + dir[1]; // Calcula a nova coluna.

            // Verifica se a nova posição é livre e não foi visitada.
            if (maze.isFree(newRow, newCol) && !visited.contains(new java.awt.Point(newCol, newRow))) {
                // Cria um novo caminho adicionando a posição atual.
                List<java.awt.Point> newPath = new LinkedList<>(current.path);
                newPath.add(new java.awt.Point(current.col, current.row));

                // Verifica a posição relativa no caminho para aplicar probabilidades de falha.
                boolean isNearStart = pathLength < 3; // Perto do início.
                boolean isNearMiddle = pathLength > (maze.getRows() / 2 - 2) && pathLength < (maze.getRows() / 2 + 2); // Perto do meio.
                boolean isNearEnd = pathLength > maze.getRows() - 3; // Perto do fim.

                if (!findCorrectPath) {
                    // Probabilidades de ignorar caminhos perto do início, meio ou fim.
                    if (isNearStart && Math.random() < 0.4) {
                        continue; // Ignora este vizinho.
                    } else if (isNearMiddle && Math.random() < 0.25) {
                        continue; // Ignora este vizinho.
                    } else if (isNearEnd && Math.random() < 0.25) {
                        continue; // Ignora este vizinho.
                    }
                }

                // Adiciona o novo estado à fila de prioridade.
                queue.add(new State(newRow, newCol, current.cost + 1, newPath));
            }
        }
    }

    // Método para gerar uma representação única de um caminho.
    private String generatePathRepresentation(List<java.awt.Point> path) {
        StringBuilder sb = new StringBuilder(); // Usa StringBuilder para eficiência.
        for (java.awt.Point point : path) {
            sb.append(point.x).append(",").append(point.y).append(";"); // Concatena coordenadas.
        }
        return sb.toString(); // Retorna a string representando o caminho.
    }

    // Método para obter a lista de caminhos falhos.
    public List<List<java.awt.Point>> getFailedPaths() {
        return failedPaths;
    }

    // Método para obter o caminho bem-sucedido.
    public List<java.awt.Point> getSuccessfulPath() {
        return successfulPath;
    }

    // Classe estática interna para representar um estado na busca.
    private static class State {
        int row; // Linha atual.
        int col; // Coluna atual.
        int cost; // Custo acumulado até o estado atual.
        List<java.awt.Point> path; // Caminho percorrido até o estado atual.

        // Construtor da classe `State`.
        public State(int row, int col, int cost, List<java.awt.Point> path) {
            this.row = row; // Inicializa a linha.
            this.col = col; // Inicializa a coluna.
            this.cost = cost; // Inicializa o custo.
            this.path = path; // Inicializa o caminho.
        }

        // Método para calcular o heurístico de um estado.
        public int getHeuristic() {
            // Calcula a função heurística como a soma do custo e a distância Manhattan até a célula de destino.
            return cost + Math.abs(row - 9) + Math.abs(col - 9);
        }
    }
}
