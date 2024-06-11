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

public class Window extends JFrame {
    private Maze maze;
    private Point boneco;
    private JPanel mazePanel;
    private JButton randomizeButton;
    private JButton startGreedyButton;
    private boolean gameWon;
    private Timer timer;
    private int stepIndex;
    private int attemptIndex;
    private List<List<java.awt.Point>> failedPaths;
    private List<java.awt.Point> successfulPath;

    // Construtor
    public Window() {
        // Definir a janela
        setTitle("Labirinto");
        setSize(500, 580);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Definir o labirinto
        maze = new Maze(10, 10);
        boneco = new Point(maze);
        gameWon = false;

        mazePanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                maze.draw(g);
                boneco.draw(g);
            }
        };
        mazePanel.setPreferredSize(new Dimension(500, 500));
        add(mazePanel, BorderLayout.CENTER);

        // Adiciona KeyListener para capturar eventos de teclado
        addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {}

            @Override
            public void keyPressed(KeyEvent e) {
                if (!gameWon) { // Apenas permite movimentos se o jogo não estiver vencido
                    switch (e.getKeyCode()) {
                        case KeyEvent.VK_UP:
                            boneco.moveUp();
                            break;
                        case KeyEvent.VK_DOWN:
                            boneco.moveDown();
                            break;
                        case KeyEvent.VK_LEFT:
                            boneco.moveLeft();
                            break;
                        case KeyEvent.VK_RIGHT:
                            boneco.moveRight();
                            break;
                    }
                    checkWin(); // Verifica se o jogador venceu após cada movimento
                    repaint(); // Re-desenha a interface gráfica após o movimento
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {}
        });
        setFocusable(true); // Permite que a janela capture eventos de teclado
        requestFocusInWindow(); // Solicita foco para a janela

        // Botão para aleatorizar o labirinto
        randomizeButton = new JButton("Aleatorizar Labirinto");
        randomizeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                maze.randomize();
                boneco.reset();
                gameWon = false; // Resetar o estado do jogo
                repaint();
                mazePanel.revalidate(); // Revalida o painel do labirinto
                mazePanel.repaint(); // Redesenha o painel do labirinto
                requestFocusInWindow(); // Restaura o foco para a janela
            }
        });
        add(randomizeButton, BorderLayout.SOUTH);

        // Botão para iniciar a busca Greedy
        startGreedyButton = new JButton("Iniciar Busca Greedy");
        startGreedyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                GreedySearch search = new GreedySearch(maze, boneco);
                boolean found = search.search(20); // Tentar pelo menos 20 vezes
                failedPaths = search.getFailedPaths();
                successfulPath = search.getSuccessfulPath();
                attemptIndex = 0;
                if (found) {
                    animateFailedPaths();
                } else {
                    JOptionPane.showMessageDialog(Window.this, "Caminho não encontrado após várias tentativas!");
                }
            }
        });
        add(startGreedyButton, BorderLayout.NORTH);
    }

    // Verifica se o boneco chegou ao destino
    private void checkWin() {
        if (boneco.getRow() == maze.getDestinationRow() && boneco.getCol() == maze.getDestinationCol()) {
            gameWon = true;
            JOptionPane.showMessageDialog(this, "Você ganhou!");
        }
    }

    // Método para animar as tentativas falhas e salvar imagens
    private void animateFailedPaths() {
        if (attemptIndex < failedPaths.size()) {
            List<java.awt.Point> path = failedPaths.get(attemptIndex);
            stepIndex = 0;

            timer = new Timer(250, new ActionListener() { // Intervalo de 0.25 segundo
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (stepIndex < path.size()) {
                        java.awt.Point step = path.get(stepIndex);
                        boneco.setPosition(step.y, step.x);
                        repaint();
                        stepIndex++;
                    } else {
                        saveMazeImage("Falho_" + (attemptIndex + 1) + ".png"); // Salva a imagem do caminho falho
                        timer.stop();
                        attemptIndex++;
                        animateFailedPaths(); // Animar a próxima tentativa falha
                    }
                }
            });
            timer.start();
        } else {
            animateSuccessfulPath(); // Após falhas, animar o caminho bem-sucedido
        }
    }

    // Os métodos abaixos posteriormente serão alterados

    // Método para animar o caminho bem-sucedido
    private void animateSuccessfulPath() {
        stepIndex = 0;

        timer = new Timer(250, new ActionListener() { // Intervalo de 0.25 segundo
            @Override
            public void actionPerformed(ActionEvent e) {
                if (stepIndex < successfulPath.size()) {
                    java.awt.Point step = successfulPath.get(stepIndex);
                    boneco.setPosition(step.y, step.x);
                    repaint();
                    stepIndex++;
                } else {
                    timer.stop();
                    JOptionPane.showMessageDialog(Window.this, "Busca concluída com sucesso!");
                }
            }
        });
        timer.start();
    }

    // Método para salvar uma imagem do labirinto atual
    private void saveMazeImage(String filename) {
        BufferedImage image = new BufferedImage(mazePanel.getWidth(), mazePanel.getHeight(), BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = image.createGraphics();
        mazePanel.paint(g2d);
        g2d.dispose();
        try {
            ImageIO.write(image, "png", new File(filename));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
