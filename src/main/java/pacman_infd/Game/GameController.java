/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pacman_infd.Game;

import pacman_infd.Elements.Ghost;
import pacman_infd.Enums.GameState;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JOptionPane;
import javax.swing.Timer;
import pacman_infd.Elements.MovingGameElement;
import pacman_infd.Fileloader.LevelManager;

/**
 *
 * @author Marinus
 */
public class GameController implements GameEventListener {

    private GameWorld gameWorld;
    private final View view;
    private final ScorePanel scorePanel;
    private GameState gameState;
    private final Timer gameTimer;
    private final StopWatch stopWatch;
    private final LevelManager levelManager;
    private final SoundManager soundManager;
    private int gameSpeed;
    
    private static final int REFRESH_RATE = 10;
    private final int INITIAL_GAME_SPEED = 250;
    private final int MIN_GAME_SPEED = 100;
    private final int NEXT_LEVEL_SPEED_CHANGE = -10;

    public GameController(View view, ScorePanel scorePanel) {

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

    public void setGameWorld(GameWorld gameWorld){
        this.gameWorld = gameWorld;
    }

    /**
     * give focus back to the view
     */
    @Override
    public void refocus(){
        if (view != null){
            view.requestFocus();
        }
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
        }

        stopWatch.reset();
        stopWatch.start();
        gameSpeed = INITIAL_GAME_SPEED;
        char[][] level = levelManager.getFirstLevel();
        setupLevel(level);
        scorePanel.resetStats();
    }

    public void levelIsWon() {
        soundManager.playSound("win");
        pauseGame();
        char[][] nextLevel = levelManager.getNextLevel();
        if (nextLevel != null) {
            JOptionPane.showMessageDialog(
                    null,
                    "Well done!\nGet ready for the next level!",
                    "Level Complete",
                    JOptionPane.ERROR_MESSAGE
            );
            if(gameSpeed > MIN_GAME_SPEED) {
                gameSpeed += NEXT_LEVEL_SPEED_CHANGE;
            }
            setupLevel(nextLevel);
        } else {
            JOptionPane.showMessageDialog(
                    null,
                    "Well done!\nYou won all levels!",
                    "Game Complete!!!",
                    JOptionPane.ERROR_MESSAGE
            );
        }

    }

    /**
     * Setup a level
     */
    public void setupLevel(char[][] level) {
        gameWorld = new GameWorld(this, level, soundManager, view, gameSpeed);
        gameState = GameState.RUNNING;
        drawGame();
        gameTimer.start();
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
        GameWorld gameWorld = null;
        gameState = GameState.PREGAME;

    }

    public GameState getGameState() {
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
    public void increaseLife() {
        scorePanel.earnLife();
        scorePanel.repaint();
    }

    @Override
    public void increasePoints(int amount) {
        scorePanel.addScore(amount);
        scorePanel.repaint();
    }
    
    public SoundManager getSoundManager(){
        return soundManager;
    }
    
    void mouseClicked(int x, int y, int mouseButton){
        if(gameWorld != null){
             gameWorld.spawnPortal(x, y, mouseButton);  
        }
    }

    /**
     * Stop the gametime for a certain time
     * @param time the time in second
     */
    public void stopTime(int time) {
        stopWatch.stop();
        final Timer stopTimer = new Timer(time * 1000, new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                stopWatch.start();
                ((Timer)evt.getSource()).stop();
            }
        });
        stopTimer.start();
    }

    /**
     *
     * @param action make an action on all Ghost of the board.
     */
    @Override
    public void actionOnGhost(IGhostAction action) {
        for (Cell cell : gameWorld.getCells()) {
            for (MovingGameElement element : cell.getMovingElements()) {
                if (element instanceof Ghost) {
                    action.perform((Ghost) element);
                }
            }
        }
    }
}
