/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pacman_infd.Elements;

import pacman_infd.Enums.GhostState;
import pacman_infd.Game.Cell;
import pacman_infd.Game.ElementEventListener;
import pacman_infd.Game.SoundManager;
import pacman_infd.Strategies.FleeStrategy;
import pacman_infd.Strategies.ReturnHomeStrategy;
import pacman_infd.Strategies.Strategy;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 *
 * @author Marinus
 */
public class  Ghost extends MovingGameElement implements Eatable{

    private Strategy strategy;
    private final Strategy initialStrategy;
    private final Color color;
    private GhostState state;

    private Timer vulnerabilityTimer;
    private final Timer deathTimer;
    private final int DEATH_TIMER_DELAY = 5000;
    
    private static final int VALUE = 400;



    public Ghost(Cell cell, ElementEventListener gameEventListener, int speed, Strategy strategy, Color color, SoundManager sMger) {
        super(cell, gameEventListener, speed, sMger);
        this.strategy = strategy;
        this.color = color;
        initialStrategy = strategy;
        state = GhostState.NORMAL;

        ActionListener deathTimerAction = new java.awt.event.ActionListener() {

            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deathTimerActionPerformed(evt);
            }
        };

        deathTimer = new Timer(DEATH_TIMER_DELAY, deathTimerAction);
        vulnerabilityTimer = new Timer(0, vulnerabilityTimerAction);
    }

    /**
     * Draw this Ghost
     *
     * @param g
     */
    @Override
    public void draw(Graphics g) {
        if (state.equals(GhostState.VULNERABLE)) {
            g.setColor(Color.BLUE);
        } else if (state.equals(GhostState.DEAD)) {
            g.setColor(Color.BLACK);
        } else {
            g.setColor(color);
        }
        //body
        g.fillRoundRect(
                (int) getPosition().getX() - 5,
                (int) getPosition().getY() - 5,
                getCell().getSize() + 10,
                getCell().getSize() + 10,
                10, 5
        );
        // eyes
        g.setColor(Color.WHITE);
        // left eye
        g.fillOval(
                (int) getPosition().getX(),
                (int) getPosition().getY(),
                12,
                12
        );
        // right eye
        g.fillOval(
                (int) getPosition().getX() + 20,
                (int) getPosition().getY(),
                12,
                12
        );
        // pupils
        g.setColor(Color.BLACK);
        // left pupil
        g.fillOval(
                (int) getPosition().getX() + 4,
                (int) getPosition().getY() + 2,
                6,
                6
        );
        // right pupil
        g.fillOval(
                (int) getPosition().getX() + 24,
                (int) getPosition().getY() + 2,
                6,
                6
        );

    }

    /**
     * Move to the next cell. Uses its current strategy to determine which cell
     * to move to.
     */
    @Override
    protected void move() {
        Cell nextCell = strategy.giveNextCell(cell);
        if (nextCell != null) {
            nextCell.addMovingElement(this);
            cell.removeMovingElement(this);
            setCell(nextCell);
        }

    }

    ActionListener vulnerabilityTimerAction = new java.awt.event.ActionListener() {

        @Override
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            vulnerabilityTimerActionPerformed(evt);
        }
    };

    /**
     * Change current strategy to FleeStrategy and lowers the speed of this
     * Ghost by 50% This is called when Pacman eats a superPellet
     */
    public void flee(int time) {
        if (state.equals(GhostState.VULNERABLE)) {
            vulnerabilityTimer.stop();
            vulnerabilityTimer = new Timer(time * 1000, vulnerabilityTimerAction);
            vulnerabilityTimer.restart();
        } else if (state.equals(GhostState.NORMAL)){
            this.strategy = new FleeStrategy();
            state = GhostState.VULNERABLE;
            setSpeed((int) (speed * 1.50));
            vulnerabilityTimer = new Timer(time * 1000, vulnerabilityTimerAction);
            vulnerabilityTimer.start();
        }

    }

    /**
     * Revert back to the initial Strategy and initial speed and stops.
     */
    private void backToNormal() {
        strategy = initialStrategy;
        state = GhostState.NORMAL;
        setSpeed(speed);
        vulnerabilityTimer.stop();
        deathTimer.stop();
    }

    private void dead() {
        // Make them fast so they can reach the middle of the maze in less than 5 seconds
        setSpeed(speed / 5);
        strategy = new ReturnHomeStrategy(startCell);
        state = GhostState.DEAD;
        deathTimer.start();
    }
    
    @Override
    public void reset(){
        super.reset();
        backToNormal();
    }
    
    public GhostState getState(){
        return state;
    }

    /**
     * This is called each 'tick' of the timer. This is used by the GameWorld to
     *
     * @param e
     */
    @Override
    public void moveTimerActionPerformed(ActionEvent e) {
        move();
        //checkCollisions();
        elementEventListener.movingElementActionPerformed(this);
    }

    private void vulnerabilityTimerActionPerformed(ActionEvent evt) {
        if(state.equals(GhostState.VULNERABLE)){
            backToNormal();
        }
        if(state.equals(GhostState.DEAD)){
            deathTimer.restart();
        }
        vulnerabilityTimer.stop();
    }

    private void deathTimerActionPerformed(ActionEvent evt) {
        backToNormal();
        deathTimer.stop();
    }
    
    @Override
    public void eatMe() {
        if(state.equals(GhostState.VULNERABLE)){
            elementEventListener.eatAGhost(this);
            soundManager.playSound("ghost");
            dead();
            
        }
        else if(state.equals(GhostState.NORMAL)){
            soundManager.playSound("death");
            elementEventListener.killPacman();
        }
    }

    @Override
    public int getValue() {
        return VALUE;
    }

}
