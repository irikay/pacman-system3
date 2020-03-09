/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package pacman_infd;

import java.util.EventListener;
import pacman_infd.Elements.MovingGameElement;

/**
 *
 * @author Marinus
 */
public interface ElementEventListener extends EventListener{
    
    public void movingElementActionPerformed(MovingGameElement e);
    public void eatableElementEaten(Eatable e);
    public void makeGhostsVulnerable();
    public void killPacman();

}
