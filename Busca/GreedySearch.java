import java.util.PriorityQueue;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.Comparator;

public class GreedySearch {
    private Maze maze;
    private Point start;
    private int destRow;
    private int destCol;
    private List<List<java.awt.Point>> failedPaths;
    private List<java.awt.Point> successfulPath;

    // Construtor
    public GreedySearch(Maze maze, Point start) {
        this.maze = maze;
        this.start = start;
        this.destRow = maze.getDestinationRow();
        this.destCol = maze.getDestinationCol();
        this.failedPaths = new LinkedList<>();
        this.successfulPath = new LinkedList<>();
    }

    // Método para iniciar a busca Greedy
    public boolean search(int attempts) {
        for (int attempt = 0; attempt < attempts; attempt++) {
            if (attempt < attempts - 1) {
                searchGreedy(false);
            } else {
                return searchGreedy(true);
            }
        }
        return false; // Não deve alcançar aqui, pois sempre encontrará na última tentativa
    }

    // Método para realizar uma tentativa de busca Greedy
    private boolean searchGreedy(boolean findCorrectPath) {
        PriorityQueue<State> queue = new PriorityQueue<>(Comparator.comparingInt(State::getHeuristic));
        Set<java.awt.Point> visited = new HashSet<>();
        List<java.awt.Point> currentPath = new LinkedList<>();

        queue.add(new State(start.getRow(), start.getCol(), 0, currentPath));

        while (!queue.isEmpty()) {
            State current = queue.poll();
            currentPath = new LinkedList<>(current.path);
            currentPath.add(new java.awt.Point(current.col, current.row));

            if (current.row == destRow && current.col == destCol) {
                if (findCorrectPath) {
                    successfulPath = currentPath;
                    return true;
                } else {
                    // Se é uma tentativa falha, rejeitamos o caminho bem-sucedido
                    failedPaths.add(currentPath);
                    return false;
                }
            }

            visited.add(new java.awt.Point(current.col, current.row));
            exploreNeighbors(current, queue, visited, findCorrectPath);
        }

        if (!findCorrectPath) {
            failedPaths.add(currentPath);
        }

        return false; // Retorna false se não encontrar caminho
    }

    // Método responsável por 
    private void exploreNeighbors(State current, PriorityQueue<State> queue, Set<java.awt.Point> visited, boolean findCorrectPath) {
        int[][] directions = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};

        for (int[] dir : directions) {
            int newRow = current.row + dir[0];
            int newCol = current.col + dir[1];

            if (maze.isFree(newRow, newCol) && !visited.contains(new java.awt.Point(newCol, newRow))) {
                List<java.awt.Point> newPath = new LinkedList<>(current.path);
                newPath.add(new java.awt.Point(current.col, current.row));

                if (!findCorrectPath && Math.random() < 0.7) {
                    continue;
                }

                queue.add(new State(newRow, newCol, current.cost + 1, newPath));
            }
        }
    }

    // Retorna caminhos falhos
    public List<List<java.awt.Point>> getFailedPaths() {
        return failedPaths;
    }

    // Retonra caminho que chega no resultado
    public List<java.awt.Point> getSuccessfulPath() {
        return successfulPath;
    }

    // Classe auxiliar
    private static class State {
        int row;
        int col;
        int cost;
        List<java.awt.Point> path;

        public State(int row, int col, int cost, List<java.awt.Point> path) {
            this.row = row;
            this.col = col;
            this.cost = cost;
            this.path = path;
        }

        public int getHeuristic() {
            return cost + Math.abs(row - 9) + Math.abs(col - 9); // Heurística é a distância Manhattan até (9, 9)
        }
    }
}
