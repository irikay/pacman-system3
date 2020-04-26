package pacman_infd.Elements.ExtensionElements;

import pacman_infd.Elements.AExtensionElement;
import pacman_infd.Elements.Ghost;
import pacman_infd.Elements.Pacman;
import pacman_infd.Game.*;

import java.io.IOException;
import java.util.List;

/**
 * Grenade, kill all ghost with a distance of 4 squares.
 */
public class GrenadeElement extends AExtensionElement {

    private static final String IMAGE_NAME = "grenade";

    private static final int GRENADE_DISTANCE = 4;

    public GrenadeElement(Cell cell, ElementEventListener evtl, SoundManager sMger) throws IOException {
        super(cell, evtl, sMger, IMAGE_NAME);
    }

    /**
     *
     * @param gameEventListener the game world listener.
     * @param pacman the pacman who touches this element.
     */
    public void onCollided(GameEventListener gameEventListener, Pacman pacman){
        cell.setStaticElement(null);
        List<Ghost> ghosts = cell.getGameElementNear(Ghost.class, GRENADE_DISTANCE);

        for(Ghost ghost : ghosts){
            ghost.dead();
        }

        gameEventListener.increasePoints(ghosts.size() * 200);
    }
}
