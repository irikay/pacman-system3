/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package pacman_infd.Elements;

import java.awt.Color;
import java.awt.Graphics;
import pacman_infd.Game.Cell;
import pacman_infd.Game.ElementEventListener;
import pacman_infd.Game.GameEventListener;
import pacman_infd.Game.SoundManager;

/**
 *
 * @author Marinus
 */
public class SuperPellet extends GameElement implements Eatable {

    private static final int VALUE = 50;
    
    public SuperPellet(Cell cell, ElementEventListener evtl, SoundManager sMger) {
        super(cell, evtl, sMger);
    }
      

    @Override
    public void draw(Graphics g) {
        g.setColor(Color.WHITE);
        g.fillOval(
                (int)getPosition().getX() + getCell().getSize()/ 2 - 10, 
                (int)getPosition().getY() + getCell().getSize()/ 2 - 10, 
                20, 20
        );
    }

    @Override
    public void eatMe() {
        elementEventListener.eatableElementEaten(this);
        if(cell.getStaticElement() == this){
            cell.setStaticElement(null);
        }  
        elementEventListener.enterSuperMode();
    }

    @Override
    public int getValue() {
        return VALUE;
    }

}
