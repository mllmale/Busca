import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.util.List;

// Definição da classe `Window` que herda de `JFrame`.
public class Window extends JFrame {
    // Declaração dos atributos da classe.
    private Maze maze; // Representa o labirinto.
    private Point boneco; // Representa a posição do jogador no labirinto.
    private JPanel mazePanel; // Painel onde o labirinto e o jogador são desenhados.
    private JButton randomizeButton; // Botão para aleatorizar o labirinto.
    private JButton startGreedyButton; // Botão para iniciar a busca gulosa.
    private boolean gameWon; // Indica se o jogador ganhou o jogo.
    private Timer timer; // Timer para animação dos caminhos.
    private int stepIndex; // Índice do passo atual na animação do caminho.
    private int attemptIndex; // Índice da tentativa atual na animação dos caminhos falhos.
    private List<List<java.awt.Point>> failedPaths; // Lista de caminhos falhos.
    private List<java.awt.Point> successfulPath; // Caminho bem-sucedido.

    // Construtor da classe `Window`.
    public Window() {
        // Configurações da janela.
        setTitle("Labirinto"); // Título da janela.
        setSize(800, 635); // Tamanho da janela.
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Ação ao fechar a janela.
        setLayout(new BorderLayout()); // Define o layout da janela como BorderLayout.

        // Inicializa o labirinto e a posição do jogador.
        maze = new Maze(11, 16); // Cria um labirinto de 11x15.
        boneco = new Point(maze); // Posiciona o jogador no labirinto.
        gameWon = false; // Inicializa o jogo como não ganho.

        // Cria o painel do labirinto.
        mazePanel = new JPanel() {
            // Sobrescreve o método `paintComponent` para desenhar o labirinto e o jogador.
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g); // Chama o método da superclasse.
                maze.draw(g); // Desenha o labirinto.
                boneco.draw(g); // Desenha o jogador.
            }
        };
        mazePanel.setPreferredSize(new Dimension(550, 550)); // Define o tamanho preferido do painel.
        add(mazePanel, BorderLayout.CENTER); // Adiciona o painel ao centro da janela.

        // Adiciona um ouvinte para eventos de teclado.
        addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {} // Não utilizado.

            @Override
            public void keyPressed(KeyEvent e) {
                // Se o jogo não foi ganho, processa o movimento.
                if (!gameWon) {
                    // Verifica qual tecla foi pressionada e move o jogador.
                    switch (e.getKeyCode()) {
                        case KeyEvent.VK_UP:
                            boneco.moveUp(); // Move o jogador para cima.
                            break;
                        case KeyEvent.VK_DOWN:
                            boneco.moveDown(); // Move o jogador para baixo.
                            break;
                        case KeyEvent.VK_LEFT:
                            boneco.moveLeft(); // Move o jogador para a esquerda.
                            break;
                        case KeyEvent.VK_RIGHT:
                            boneco.moveRight(); // Move o jogador para a direita.
                            break;
                    }
                    checkWin(); // Verifica se o jogador ganhou o jogo.
                    repaint(); // Redesenha o painel.
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {} // Não utilizado.
        });
        setFocusable(true); // Define a janela como focável para capturar eventos de teclado.
        requestFocusInWindow(); // Solicita o foco para a janela.

        // Cria o botão para aleatorizar o labirinto.
        randomizeButton = new JButton("Aleatorizar Labirinto");
        randomizeButton.addActionListener(new ActionListener() {
            // Define a ação a ser realizada quando o botão for clicado.
            @Override
            public void actionPerformed(ActionEvent e) {
                maze.randomize(); // Aleatoriza o layout do labirinto.
                boneco.reset(); // Redefine a posição do jogador.
                gameWon = false; // Redefine o estado de vitória.
                repaint(); // Redesenha o painel.
                mazePanel.revalidate(); // Valida o layout do painel.
                mazePanel.repaint(); // Redesenha o painel.
                requestFocusInWindow(); // Solicita o foco para a janela.
            }
        });
        add(randomizeButton, BorderLayout.SOUTH); // Adiciona o botão à parte inferior da janela.

        // Cria o botão para iniciar a busca gulosa.
        startGreedyButton = new JButton("Iniciar Busca Greedy");
        startGreedyButton.addActionListener(new ActionListener() {
            // Define a ação a ser realizada quando o botão for clicado.
            @Override
            public void actionPerformed(ActionEvent e) {
                GreedySearch search = new GreedySearch(maze, boneco); // Cria um objeto `GreedySearch` para a busca gulosa.
                boolean found = search.search(10, 25); // Executa a busca tentando entre 10 e 25 vezes.

                failedPaths = search.getFailedPaths(); // Armazena os caminhos falhos encontrados.
                successfulPath = search.getSuccessfulPath(); // Armazena o caminho bem-sucedido.
                attemptIndex = 0; // Inicializa o índice de tentativas.

                if (found) {
                    animatePaths(); // Anima os caminhos encontrados.
                } else {
                    // Exibe uma mensagem se o caminho não for encontrado após várias tentativas.
                    JOptionPane.showMessageDialog(Window.this, "Caminho não encontrado após várias tentativas!");
                }
            }
        });
        add(startGreedyButton, BorderLayout.NORTH); // Adiciona o botão à parte superior da janela.
    }

    // Método para verificar se o jogador ganhou.
    private void checkWin() {
        // Verifica se o jogador está na posição de destino.
        if (boneco.getRow() == maze.getDestinationRow() && boneco.getCol() == maze.getDestinationCol()) {
            gameWon = true; // Define o estado de vitória.
            JOptionPane.showMessageDialog(this, "Você ganhou!"); // Exibe uma mensagem de vitória.
        }
    }

    // Método para animar os caminhos encontrados.
    private void animatePaths() {
        // Verifica se há caminhos falhos para animar.
        if (attemptIndex < failedPaths.size()) {
            List<java.awt.Point> path = failedPaths.get(attemptIndex); // Obtém o caminho falho atual.
            animateSinglePath(path, "Falho" + (attemptIndex + 1) + ".png", false); // Anima o caminho falho.
        } else if (attemptIndex == failedPaths.size()) {
            animateSinglePath(successfulPath, "successful_path.png", true); // Anima o caminho bem-sucedido.
        }
    }

    // Método para animar um único caminho.
    private void animateSinglePath(List<java.awt.Point> path, String filename, boolean isSuccessful) {
        stepIndex = 0; // Inicializa o índice de passos.

        timer = new Timer(250, new ActionListener() {
            // Define a ação a ser realizada a cada intervalo de tempo.
            @Override
            public void actionPerformed(ActionEvent e) {
                if (stepIndex < path.size()) {
                    java.awt.Point step = path.get(stepIndex); // Obtém o passo atual.
                    boneco.setPosition(step.y, step.x); // Define a posição do jogador.
                    repaint(); // Redesenha o painel.
                    stepIndex++; // Incrementa o índice de passos.
                } else {
                    if (!isSuccessful) {
                        saveMazeImage(filename); // Salva uma imagem do labirinto com o caminho falho.
                    }
                    timer.stop(); // Para o timer.
                    attemptIndex++; // Incrementa o índice de tentativas.
                    animatePaths(); // Chama a animação do próximo caminho.
                }
            }
        });
        timer.start(); // Inicia o timer.
    }

    // Método para salvar uma imagem do labirinto.
    private void saveMazeImage(String filename) {
        // Cria uma imagem do labirinto no tamanho do painel.
        BufferedImage image = new BufferedImage(mazePanel.getWidth(), mazePanel.getHeight(), BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = image.createGraphics(); // Cria um contexto gráfico para desenhar na imagem.
        mazePanel.paint(g2d); // Desenha o painel do labirinto na imagem.
        g2d.dispose(); // Libera os recursos do contexto gráfico.
        try {
            ImageIO.write(image, "png", new File(filename)); // Escreve a imagem em um arquivo PNG.
        } catch (IOException ex) {
            ex.printStackTrace(); // Exibe a exceção em
        }
    }
}