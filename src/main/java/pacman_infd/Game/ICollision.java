package pacman_infd.Game;

import pacman_infd.Elements.GameElement;

/**
 * Class to manager collision between two GameElement, inspired from System1.
 * @author BOOSKO Sam
 */
public interface ICollision {

    <C1 extends GameElement, C2 extends GameElement> void onCollision(C1 object1, C2 object2, GameEventListener gameEventListener);

}
