import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.util.Random;

public class TopClass implements ActionListener, KeyListener {
    private static final int SCREEN_WIDTH = (int) Toolkit.getDefaultToolkit().getScreenSize().getWidth();
    private static final int SCREEN_HEIGHT = (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight();
    private static final int ROCK_GAP = SCREEN_HEIGHT / (new Random().nextInt(6) + 2);
    private static final int ROCK_WIDTH = SCREEN_WIDTH / 8;
    private static final int ROCK_HEIGHT = ROCK_WIDTH / 2;
    private static final int DINO_HEIGHT = 150;
    private static final int DINO_WIDTH = 80;
    private static final int UPDATE_DIFFERENCE = 25;
    private static final int X_MOVEMENT_DIFFERENCE = 5;
    private static final int SCREEN_DELAY = 300;
    private static final int DINO_X_LOCATION = SCREEN_WIDTH / 7;
    private static final int DINO_JUMP_DIFF = 10;
    private static final int DINO_FALL_DIFF = DINO_JUMP_DIFF / 2;
    private static final int DINO_JUMP_HEIGHT = ROCK_GAP - DINO_HEIGHT - DINO_JUMP_DIFF * 2;

    private boolean loopVar = true;
    private boolean gamePlay = false;
    private boolean dinoThrust = false;
    private boolean dinoFired = false;
    private boolean released = true;

    private int birdYTracker = SCREEN_HEIGHT / 2 - DINO_HEIGHT;
    private Object buildComplete = new Object();

    private JFrame f = new JFrame("Run Dino Run");
    private JButton startGame;
    private JPanel topPanel;

    private static TopClass topClass = new TopClass();
    private static PlayGameScreen playGameScreen;

    private TopClass() {
    }

    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(() -> {
            topClass.buildFrame();
            Thread t = new Thread(() -> topClass.gameScreen(true));
            t.start();
        });
    }

    private void buildFrame() {
        Image icon = Toolkit.getDefaultToolkit()
                .getImage(this.getClass()
                        .getResource("resource/dino.png.png"));

        f.setContentPane(createContentPane());
        f.setResizable(true);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setAlwaysOnTop(false);
        f.setVisible(true);
        f.setMinimumSize(new Dimension(SCREEN_WIDTH / 4, SCREEN_HEIGHT / 4));
        f.setExtendedState(JFrame.MAXIMIZED_BOTH);
        f.setIconImage(icon);
        f.addKeyListener(this);
    }

    private JPanel createContentPane() {
        topPanel = new JPanel();
        topPanel.setBackground(Color.BLACK);
        LayoutManager overlay = new OverlayLayout(topPanel);
        topPanel.setLayout(overlay);

        startGame = new JButton("Start Playing!");
        startGame.setBackground(Color.BLUE);
        startGame.setForeground(Color.WHITE);
        startGame.setFocusable(false);
        startGame.setFont(new Font("Calibri", Font.BOLD, 42));
        startGame.setAlignmentX(0.5f);
        startGame.setAlignmentY(0.5f);
        startGame.addActionListener(this);
        topPanel.add(startGame);

        playGameScreen = new PlayGameScreen(SCREEN_WIDTH, SCREEN_HEIGHT, true);
        topPanel.add(playGameScreen);

        return topPanel;
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == startGame) {

            loopVar = false;

            fadeOperation();
        } else if (e.getSource() == buildComplete) {
            Thread t = new Thread(() -> {
                loopVar = true;
                gamePlay = true;
                topClass.gameScreen(false);
            });
            t.start();
        }
    }

    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_SPACE && gamePlay && released) {
            if (dinoThrust) {
                dinoFired = true;
            }
            dinoThrust = true;
            released = false;
        } else if (e.getKeyCode() == KeyEvent.VK_B && !gamePlay) {
            birdYTracker = SCREEN_HEIGHT / 2 - DINO_HEIGHT;
            dinoThrust = false;
            actionPerformed(new ActionEvent(startGame, -1, ""));
        }
        if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
            System.exit(0);
        }
    }

    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            released = true;
        }
    }

    public void keyTyped(KeyEvent e) {

    }

    private void fadeOperation() {
        Thread t = new Thread(() -> {
            topPanel.remove(startGame);
            topPanel.remove(playGameScreen);
            topPanel.revalidate();
            topPanel.repaint();

            JPanel temp = new JPanel();
            int alpha = 0;
            temp.setBackground(new Color(0, 0, 0, alpha));
            topPanel.add(temp);
            topPanel.add(playGameScreen);
            topPanel.revalidate();
            topPanel.repaint();

            long currentTime = System.currentTimeMillis();

            while (temp.getBackground().getAlpha() != 255) {
                if ((System.currentTimeMillis() - currentTime) > UPDATE_DIFFERENCE / 2) {
                    if (alpha < 255 - 10) {
                        alpha += 10;
                    } else {
                        alpha = 255;
                    }

                    temp.setBackground(new Color(0, 0, 0, alpha));

                    topPanel.revalidate();
                    topPanel.repaint();
                    currentTime = System.currentTimeMillis();
                }
            }

            topPanel.removeAll();
            topPanel.add(temp);
            playGameScreen = new PlayGameScreen(SCREEN_WIDTH, SCREEN_HEIGHT, false);
            playGameScreen.sendText("");
            topPanel.add(playGameScreen);

            while (temp.getBackground().getAlpha() != 0) {
                if ((System.currentTimeMillis() - currentTime) > UPDATE_DIFFERENCE / 2) {
                    if (alpha > 10) {
                        alpha -= 10;
                    } else {
                        alpha = 0;
                    }

                    temp.setBackground(new Color(0, 0, 0, alpha));

                    topPanel.revalidate();
                    topPanel.repaint();
                    currentTime = System.currentTimeMillis();
                }
            }

            actionPerformed(new ActionEvent(buildComplete, -1, "Build Finished"));
        });

        t.start();
    }

    private void gameScreen(boolean isSplash) {
        Rock bp1 = new Rock(ROCK_WIDTH, ROCK_HEIGHT);
        Rock bp2 = new Rock(ROCK_WIDTH, ROCK_HEIGHT);
        Dino bird = new Dino(DINO_WIDTH, DINO_HEIGHT);


        int xLoc1 = SCREEN_WIDTH + SCREEN_DELAY, xLoc2
                = (int) (3.0 / 2.0 * SCREEN_WIDTH + ROCK_WIDTH / 2.0) + SCREEN_DELAY;
        int yLoc1 = bottomPipeLoc(), yLoc2 = bottomPipeLoc();
        int birdX = DINO_X_LOCATION, birdY = birdYTracker;


        long startTime = System.currentTimeMillis();

        while (loopVar) {
            if ((System.currentTimeMillis() - startTime) > UPDATE_DIFFERENCE) {
                if (xLoc1 < (0 - ROCK_WIDTH)) {
                    xLoc1 = SCREEN_WIDTH;
                    yLoc1 = bottomPipeLoc();
                } else if (xLoc2 < (0 - ROCK_WIDTH)) {
                    xLoc2 = SCREEN_WIDTH;
                    yLoc2 = bottomPipeLoc();
                }

                xLoc1 -= X_MOVEMENT_DIFFERENCE;
                xLoc2 -= X_MOVEMENT_DIFFERENCE;

                if (dinoFired && !isSplash) {
                    birdYTracker = birdY;
                    dinoFired = false;
                }

                if (dinoThrust && !isSplash) {

                    if (birdYTracker - birdY - DINO_JUMP_DIFF < DINO_JUMP_HEIGHT) {
                        if (birdY - DINO_JUMP_DIFF > 0) {
                            birdY -= DINO_JUMP_DIFF;
                        } else {
                            birdY = 0;
                            birdYTracker = birdY;
                            dinoThrust = false;
                        }
                    } else {
                        birdYTracker = birdY;
                        dinoThrust = false;
                    }
                } else if (!isSplash) {
                    birdY += DINO_FALL_DIFF;
                    birdYTracker = birdY;
                }

                bp1.setxLocation(xLoc1);
                bp1.setyLocation(yLoc1);
                bp2.setxLocation(xLoc2);
                bp2.setyLocation(yLoc2);

                if (!isSplash) {
                    bird.setxLocation(birdX);
                    bird.setyLocation(birdY);
                    playGameScreen.setDino(bird);
                }


                playGameScreen.setRock(bp1, bp2);

                if (!isSplash && bird.getDinosaurWidth() != -1) {
                    collisionDetection(bp1, bp2, bird);
                    updateScore(bp1, bp2, bird);
                }


                topPanel.revalidate();
                topPanel.repaint();

                startTime = System.currentTimeMillis();
            }
        }
    }

    private int bottomPipeLoc() {
        int temp = 0;
        while (temp <= ROCK_GAP + 50 || temp >= SCREEN_HEIGHT - ROCK_GAP) {
            temp = (int) (Math.random() * ((double) SCREEN_HEIGHT));
        }
        return temp;
    }

    private void updateScore(Rock rock, Rock rock1, Dino dino) {
        if (rock.getxLocation() + ROCK_WIDTH < dino.getxLocation()
                && rock.getxLocation() + ROCK_WIDTH
                > dino.getxLocation() - X_MOVEMENT_DIFFERENCE) {
            playGameScreen.incrementJump();
        } else if (rock1.getxLocation() + ROCK_WIDTH
                < dino.getxLocation() && rock1.getxLocation() + ROCK_WIDTH
                > dino.getxLocation() - X_MOVEMENT_DIFFERENCE) {
            playGameScreen.incrementJump();
        }
    }

    private void collisionDetection(Rock rock1, Rock rock, Dino dino) {
        collisionHelper(dino.getRectangle(),
                rock1.getRectangle(),
                dino.getBufferedImage(),
                rock1.getBufferedImage());
        collisionHelper(dino.getRectangle(),
                rock.getRectangle(),
                dino.getBufferedImage(),
                rock.getBufferedImage());

        if (dino.getyLocation() + DINO_HEIGHT > SCREEN_HEIGHT * 7 / 8) {
            playGameScreen.sendText("Game Over");
            loopVar = false;
            gamePlay = false;
        }
    }

    private void collisionHelper(Rectangle r1,
                                 Rectangle r2,
                                 BufferedImage b1,
                                 BufferedImage b2) {
        if (r1.intersects(r2)) {
            Rectangle r = r1.intersection(r2);

            int firstI = (int) (r.getMinX() - r1.getMinX());
            int firstJ = (int) (r.getMinY() - r1.getMinY());
            int bp1XHelper = (int) (r1.getMinX() - r2.getMinX());
            int bp1YHelper = (int) (r1.getMinY() - r2.getMinY());

            for (int i = firstI; i < r.getWidth() + firstI; i++) { //
                for (int j = firstJ; j < r.getHeight() + firstJ; j++) {
                    if ((b1.getRGB(i, j) & 0xFF000000) != 0x00
                            && (b2.getRGB(i + bp1XHelper, j + bp1YHelper) & 0xFF000000) != 0x00) {
                        playGameScreen.sendText("Game Over");
                        loopVar = false;
                        gamePlay = false;
                        break;
                    }
                }
            }
        }
    }
}
