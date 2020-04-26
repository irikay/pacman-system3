package pacman_infd.Elements.ExtensionElements;

import pacman_infd.Elements.Ghost;
import pacman_infd.Elements.Pacman;
import pacman_infd.Game.Cell;
import pacman_infd.Game.ElementEventListener;
import pacman_infd.Game.GameEventListener;
import pacman_infd.Game.SoundManager;

import java.awt.*;
import java.io.IOException;
import java.util.Random;

/**
 * A trap on the floor which set the pacman not moving for a random time.
 */
public class TrapElement extends ATimeBasedElement {

    private static final String IMAGE_NAME = "trap";

    private static final int DEFAULT_TIME = 1000;
    private static final int RANDOM_TIME_OFFSET = 3000;

    private Pacman trappedPacman;
    private Ghost trappedGhost;
    private Color previousColor;
    private boolean isUsed;

    public TrapElement(Cell cell, ElementEventListener evtl, SoundManager sMger) throws IOException {
        super(cell, evtl, sMger, IMAGE_NAME, DEFAULT_TIME + new Random().nextInt(RANDOM_TIME_OFFSET));
    }

    /**
     *
     * @param gameEventListener the game world listener.
     * @param pacman the pacman who touches this element.
     */
    public void onCollided(GameEventListener gameEventListener, Pacman pacman) {
        //super.onCollided(gameEventListener, pacman);

        if(!this.isUsed) {
            this.isUsed = true;

            this.trappedPacman = pacman;
            this.previousColor = pacman.getPacmanColor();
            this.trappedPacman.setMoving(false);
            this.trappedPacman.setPacmanColor(Color.RED);
            super.start();
        }
    }

    /**
     *
     * @param gameEventListener the game world listener.
     * @param ghost the ghost who touches this element.
     */
    public void onCollided(GameEventListener gameEventListener, Ghost ghost) {
        if(!this.isUsed) {
            this.isUsed = true;
            this.trappedGhost = ghost;
            this.trappedGhost.setMoving(false);
            super.start();
        }
    }

    @Override
    protected void endAction() {
        if(this.trappedPacman != null) {
            this.trappedPacman.setMoving(true);
            this.trappedPacman.setPacmanColor(this.previousColor);
        }else{
            this.trappedGhost.setMoving(true);
        }
        cell.setStaticElement(null);
    }
}
