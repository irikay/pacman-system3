/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package pacman_infd.Game;

import pacman_infd.Elements.Ghost;

/**
 *
 * @author Marinus
 */
public interface GameEventListener {
    
    public void decreaseLife();
    public void increaseLife();
    public void increasePoints(int amount);
    public void refocus();
    public void stopTime(int time);
    void levelIsWon();

    void actionOnGhost(IGhostAction action);

    interface IGhostAction{
        void perform(Ghost ghost);
    }

}
