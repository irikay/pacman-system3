/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pacman_infd.Game;

import pacman_infd.Enums.GameState;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;

/**
 *
 * @author Marinus
 */
public class View extends JFrame implements MouseListener {

    private static final int FRAME_WIDTH = 740;
    private static final int FRAME_HEIGHT = 918;
    private static final int SCORE_PANEL_HEIGHT = 40;
    private static final int CONTROL_PANEL_HEIGHT = 35;
    private static final int IMAGE_HEIGHT = 850;

    private final BufferedImage image;

    private final GameController gameController;

    private JPanel gamePanel;
    private ScorePanel scorePanel;

    private JButton startButton;
    private JButton pauseButton;

    public View() {
        initComponents();

        gameController = new GameController(this, scorePanel);
        image = new BufferedImage(FRAME_WIDTH, IMAGE_HEIGHT, BufferedImage.TYPE_INT_ARGB);
    }

    private void initComponents() {
        setSize(FRAME_WIDTH, FRAME_HEIGHT);
        setTitle("Pacman");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        Container contentPane;
        contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());
        setBackground(Color.BLACK);

        scorePanel = new ScorePanel();
        scorePanel.setPreferredSize(new Dimension(FRAME_WIDTH, SCORE_PANEL_HEIGHT));

        gamePanel = new JPanel();
        gamePanel.addMouseListener(this);
        startButton = new JButton("Start");
        startButton.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                startButtonActionPerformed(evt);
            }

        });

        pauseButton = new JButton("Pause");
        pauseButton.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                pauseButtonActionPerformed(evt);
            }

        });

        JPanel controlPanel = new JPanel();
        controlPanel.setLayout(new FlowLayout());
        controlPanel.setPreferredSize(new Dimension(FRAME_WIDTH, CONTROL_PANEL_HEIGHT));
        controlPanel.setBackground(Color.BLACK);

        controlPanel.add(startButton);
        controlPanel.add(pauseButton);

        contentPane.add(controlPanel, BorderLayout.SOUTH);
        contentPane.add(gamePanel, BorderLayout.CENTER);
        contentPane.add(scorePanel, BorderLayout.NORTH);

        setFocusable(true);
    }

    Graphics getGameWorldGraphics() {

        return image.getGraphics();

    }

    void drawGameWorld() {

        gamePanel.getGraphics().drawImage(image, 0, 0, null);

    }

    private void startButtonActionPerformed(ActionEvent evt) {
        gameController.newGame();
        if (gameController.getGameState() == GameState.RUNNING) {
            startButton.setText("Restart");
        } else {
            startButton.setText("Start");
        }
    }

    private void pauseButtonActionPerformed(ActionEvent evt) {
        gameController.pauseGame();
        if (gameController.getGameState() == GameState.PAUSED) {
            pauseButton.setText("Resume");
        } else {
            pauseButton.setText("Pause");
        }

    }

    @Override
    public void mouseClicked(MouseEvent e) {
 
    }

    @Override
    public void mousePressed(MouseEvent e) {
        //
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (e.getY() > SCORE_PANEL_HEIGHT && e.getY() < IMAGE_HEIGHT + SCORE_PANEL_HEIGHT) {
            gameController.mouseClicked(e.getX(), e.getY(), e.getButton());
        }
        gamePanel.requestFocus();
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        //
    }

    @Override
    public void mouseExited(MouseEvent e) {
        //
    }

}
