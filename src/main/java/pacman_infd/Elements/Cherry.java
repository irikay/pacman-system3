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
import pacman_infd.Game.SoundManager;


/**
 *
 * @author Marinus
 */
public class Cherry extends GameElement implements Eatable{

    private static final int VALUE = 100;
    
    public Cherry(Cell cell, ElementEventListener evtl, SoundManager sMger) {
        super(cell, evtl, sMger);
    }

    @Override
    public void draw(Graphics g) {
        g.setColor(Color.red);
        g.drawOval(
                (int)getPosition().getX(),
                (int)getPosition().getY(), 
                15, 
                15
        );
    }

    @Override
    public void eatMe() {
        elementEventListener.eatableElementEaten(this);
        
        if(cell.getStaticElement().equals(this)){
            cell.setStaticElement(null);
        }  
        
        soundManager.playSound("cherry");
    }

    @Override
    public int getValue() {
        return VALUE;
    }
    
}
