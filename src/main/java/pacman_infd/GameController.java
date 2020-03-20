/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pacman_infd;

import pacman_infd.Enums.GameState;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JOptionPane;
import javax.swing.Timer;
import pacman_infd.Elements.MovingGameElement;

/**
 *
 * @author Marinus
 */
public class GameController implements GameEventListener {

    private GameWorld gameWorld;
    private View view;
    private ScorePanel scorePanel;
    private GameState gameState;
    private Timer gameTimer;
    private StopWatch stopWatch;
    private LevelManager levelManager;
    private SoundManager soundManager;
    private final int minGameSpeed = 100;
    private int gameSpeed = 250;
    
    private static final int REFRESH_RATE = 10;

    GameController(View view, ScorePanel scorePanel) {

        this.view = view;
        this.scorePanel = scorePanel;
        gameState = GameState.PREGAME;
        levelManager = new LevelManager();
        soundManager = new SoundManager();

        ActionListener gameTimerAction = new java.awt.event.ActionListener() {

            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                gameTimerActionPerformed(evt);
            }
        };

        gameTimer = new Timer(REFRESH_RATE, gameTimerAction);
        stopWatch = new StopWatch();

    }

    /**
     * give focus back to the view
     */
    @Override
    public void refocus(){
        view.requestFocus();
    }

    /**
     * draw the game.
     */
    private void drawGame() {

        Graphics g = view.getGameWorldGraphics();

        if (g != null && gameWorld != null) {
            gameWorld.draw(g);

            view.drawGameWorld();
        }
    }

    public View getView() {
        return view;
    }

    /**
     * Start a new game, or, if the game is already running, restart the game.
     */
    void newGame() {
        if(gameState == GameState.RUNNING){
            pauseGame();
            gameWorld.clearGameWorld();
            gameWorld = null;
        }
        
        
        gameWorld = new GameWorld(this, levelManager.getFirstLevel(), soundManager, view, gameSpeed);
        scorePanel.resetStats();
        gameState = GameState.RUNNING;
        drawGame();
        gameTimer.start();
        stopWatch.reset();
        stopWatch.start();

    }

    /**
     * Go to the next level.
     */
    public void nextLevel() {

        soundManager.playSound("win");
        pauseGame();
        JOptionPane.showMessageDialog(
                null,
                "Well done!\nGet ready for the next level!",
                "Level Complete",
                JOptionPane.ERROR_MESSAGE
        );

        if(gameSpeed > minGameSpeed){
            gameSpeed -= 10;
        }
        gameWorld = null;
        gameWorld = new GameWorld(this, levelManager.getNextLevel(), soundManager, view, gameSpeed);
    }

    /**
     * Pause game will stop all timers
     */
    void pauseGame() {
        if (gameState == GameState.RUNNING) {
            for (Cell cell : gameWorld.getCells()) {
                for (MovingGameElement element: cell.getMovingElements()) {
                    element.stopTimer();
                }
            }

            gameTimer.stop();
            stopWatch.stop();
            gameState = GameState.PAUSED;
        } else if (gameState == GameState.PAUSED) {

            for (Cell cell : gameWorld.getCells()) {
                for (MovingGameElement element: cell.getMovingElements()) {
                    element.startTimer();
                }

            }
            gameTimer.start();
            stopWatch.start();
            gameState = GameState.RUNNING;
        }
    }

    /**
     * draw the game at each tick of the game timer.
     * @param e 
     */
    private void gameTimerActionPerformed(ActionEvent e) {
        drawGame();
        scorePanel.setTime(stopWatch.getElapsedTimeMinutesSeconds());
        scorePanel.repaint();
    }
    
    private void gameOver() {
       
        pauseGame();
        drawGame();     
        JOptionPane.showMessageDialog(
                null,
                "Game over!\nYour score: " + scorePanel.getScore(),
                "Game over!",
                JOptionPane.ERROR_MESSAGE
        );
        gameWorld.clearGameWorld();
        gameWorld = null;
        gameState = GameState.PREGAME;

    }

    GameState getGameState() {
        return gameState;
    }

    @Override
    public void decreaseLife() {
        scorePanel.loseLife();
        scorePanel.repaint();
        if(scorePanel.getLives() == 0){
            gameOver();
        }
    }

    @Override
    public void increasePoints(int amount) {
        scorePanel.addScore(amount);
        scorePanel.repaint();
    }
    
    SoundManager getSoundManager(){
        return soundManager;
    }
    
    void mouseClicked(int x, int y, int mouseButton){
        if(gameWorld != null){
             gameWorld.spawnPortal(x, y, mouseButton);  
        }
    }
}
