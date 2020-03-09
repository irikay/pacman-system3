/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pacman_infd;

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

    private BufferedImage image;

    private GameController gameController;

    private Container contentPane;

    private JPanel gamePanel;
    private ScorePanel scorePanel;
    private JPanel controlPanel;

    private JButton startButton;
    private JButton pauseButton;

    public View() {
        initComponents();

        gameController = new GameController(this, scorePanel);
        image = new BufferedImage(FRAME_WIDTH, 850, BufferedImage.TYPE_INT_ARGB);
    }

    private void initComponents() {
        setSize(FRAME_WIDTH, FRAME_HEIGHT);
        setTitle("Pacman");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());
        setBackground(Color.BLACK);

        scorePanel = new ScorePanel();
        scorePanel.setPreferredSize(new Dimension(FRAME_WIDTH, 40));

        gamePanel = new JPanel();
        gamePanel.addMouseListener(this);
        startButton = new JButton("Start");
        startButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                startButtontActionPerformed(evt);
            }

        });

        pauseButton = new JButton("Pause");
        pauseButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                pauseButtontActionPerformed(evt);
            }

        });

        controlPanel = new JPanel();
        controlPanel.setLayout(new FlowLayout());
        controlPanel.setPreferredSize(new Dimension(FRAME_WIDTH, 35));
        controlPanel.setBackground(Color.BLACK);

        controlPanel.add(startButton);
        controlPanel.add(pauseButton);

        contentPane.add(controlPanel, BorderLayout.SOUTH);
        contentPane.add(gamePanel, BorderLayout.CENTER);
        contentPane.add(scorePanel, BorderLayout.NORTH);

        setFocusable(true);
    }

    public Graphics getGameWorldGraphics() {

        return image.getGraphics();

    }

    public void drawGameWorld() {

        gamePanel.getGraphics().drawImage(image, 0, 0, null);

    }

    private void startButtontActionPerformed(ActionEvent evt) {
        gameController.newGame();
        if (gameController.getGameState() == GameState.RUNNING) {
            startButton.setText("Restart");
        } else {
            startButton.setText("Start");
        }
    }

    private void pauseButtontActionPerformed(ActionEvent evt) {
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
        if (e.getY() > 40 && e.getY() < 890) {
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
