/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package pacman_infd.Elements;

import pacman_infd.Enums.Direction;
import pacman_infd.Game.Cell;
import pacman_infd.Game.ElementEventListener;
import pacman_infd.Game.SoundManager;

import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * Documented by BOOSKO Sam
 * @author Marinus
 * Reworked about 99% by BOOSKO Sam. Marinus you're crazy, what is this unreadable code ???
 */
public abstract class MovingGameElement extends GameElement{

    /**
     * Respawn cell.
     */
    protected Cell startCell;

    /**
     * Timer used to schedule movement.
     */
    private final Timer timer;

    /**
     * Speed based on the number of square moved per second.
     */
    private float speed;

    /**
     * Speed modifier which infected the final speed.
     */
    private float speedModifier;

    /**
     * Set if the element can move.
     */
    private boolean isMoving;

    /**
     * The current direction of the element.
     */
    protected Direction currentDirection;

    /**
     * true if the pacman is invisible, else false.
     */
    protected boolean isVisible;

    public MovingGameElement(Cell cell, ElementEventListener gameEventListener, int speed, SoundManager sMger) {
        this(cell, gameEventListener, speed, sMger, null);

    }

    public MovingGameElement(Cell cell, ElementEventListener gameEventListener, int speed, SoundManager sMger,
                             Direction direction) {
        this.currentDirection = direction;
        this.cell = cell;
        this.elementEventListener = gameEventListener;

        cell.addMovingElement(this);

        this.startCell = cell;
        this.speed = speed;
        this.speedModifier = 1f;
        this.isMoving = true;
        this.isVisible = true;

        super.soundManager = sMger;

        this.timer = new Timer(this.getNextStepTime(), new TaskMovement());
        this.timer.start();
    }

    /**
     *
     * @param isVisible true to make the element visible, else false.
     */
    public void setVisible(boolean isVisible){
        this.isVisible = isVisible;
    }

    /**
     *
     * @return true is the element invisible, else false.
     */
    public boolean isVisible(){
        return this.isVisible;
    }

    /**
     * Make this element doing one movement step.
     */
    protected abstract void move();

    /**
     * Attempt to move the movingGameObject and call the notice it to the elementEventLister (Collision).
     */
    public void moveTimerActionPerformed(){
        move();
        elementEventListener.movingElementActionPerformed(this);
    }

    /**
     *
     * @return the current moving direction.
     */
    public Direction getCurrentDirection(){
        return this.currentDirection;
    }

    /**
     * Like a respawn.
     */
    public void reset(){
        cell.getMovingElements().remove(this);
        cell = startCell;
        cell.addMovingElement(this);
    }

    /**
     *
     * @return the respawn Cell.
     */
    protected Cell getStartCell(){
        return startCell;
    }

    /**
     *
     * @param isMoving true if the element can move, else false.
     */
    public void setMoving(boolean isMoving){
        this.isMoving = isMoving;
    }

    /**
     *
     * @return true if Pacman is moving.
     */
    public boolean isMoving(){
        return this.isMoving;
    }

    /**
     * Start the moving timer.
     */
    public void startTimer(){
        this.timer.start();
    }


    /**
     * Stop the moving timer.
     */
    public void stopTimer(){
        this.timer.stop();
    }

    /**
     *
     * @param speedModifier the new speed modifier.
     */
    public void setSpeedModifier(float speedModifier){
        this.speedModifier = speedModifier;
    }

    /**
     *
     * @return the default speed without the speed modifier.
     */
    public float getDefaultSpeed(){
        return this.speed;
    }

    /**
     *
     * @return the speed used, assessed by speed * speedModifier.
     */
    public float getSpeed(){
        return this.speed * this.speedModifier;
    }

    /**
     *
     * @return the time in milliseconds before the next moving step;
     */
    protected int getNextStepTime(){
        return (int) (1000 / this.getSpeed());
    }

    /**
     * Schedule the next movement step.
     */
    private void nextStep(){
        //this.timer.schedule(new PlayerTaskMovement(), this.getNextStepTime());
        this.timer.setDelay(getNextStepTime());
    }

    /**
     * Runnable class to move the player depending on his speed.
     */
    private class TaskMovement extends AbstractAction {

        @Override
        public void actionPerformed(ActionEvent e) {
            if(isMoving) {
                moveTimerActionPerformed();
            }
            nextStep();
        }
    }
}
