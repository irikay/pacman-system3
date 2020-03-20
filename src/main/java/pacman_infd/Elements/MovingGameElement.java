/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package pacman_infd.Elements;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Timer;
import pacman_infd.Cell;
import pacman_infd.GameElement;
import pacman_infd.ElementEventListener;
import pacman_infd.SoundManager;

/**
 *
 * @author Marinus
 */
public abstract class MovingGameElement extends GameElement{

    Cell startCell;
    private final Timer timer;
    int speed;

    MovingGameElement(Cell cell, ElementEventListener gameEventListener, int speed, SoundManager sMger) {
        this.cell = cell;
        this.elementEventListener = gameEventListener;
        cell.addMovingElement(this);
        startCell = cell;
        this.speed = speed;
        soundManager = sMger;

        ActionListener moveTimerActionListener = new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent evt) {
                moveTimerActionPerformed(evt);
            }

        };
        
        timer = new Timer(speed, moveTimerActionListener);
        timer.start();
    }
    
    protected abstract void move();   

    public abstract void moveTimerActionPerformed(ActionEvent e); 
    
    public void reset(){
        cell.getMovingElements().remove(this);
        cell = startCell;
       cell.addMovingElement(this);
    }
    
    protected Cell getStartCell(){
        return startCell;
    }
    
    void setSpeed(int speed)
    {
        timer.setDelay(speed);
    }
    
    public void startTimer(){
        timer.start();
    }
    
    public void stopTimer(){
        timer.stop();
    }  
}
