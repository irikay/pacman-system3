package pacman_infd.Elements.ExtensionElements;

import pacman_infd.Elements.Pacman;
import pacman_infd.Game.Cell;
import pacman_infd.Game.ElementEventListener;
import pacman_infd.Game.GameEventListener;
import pacman_infd.Game.SoundManager;

import java.awt.*;
import java.io.IOException;
import java.util.Random;

/**
 * Pepper can be ate by Pacman and it will increase his speed during a random time setup.
 */
public class PepperElement extends ATimeBasedElement {

    private static final String IMAGE_NAME = "pepper";

    private static final int TIME = 1000;
    private static final int OFFSET_TIME = 3000;

    private static final float SPEED_MODIFIER = 1.5f;

    private Pacman eater;
    private Color previousColor;

    public PepperElement(Cell cell, ElementEventListener evtl, SoundManager sMger) throws IOException {
        super(cell, evtl, sMger, IMAGE_NAME, TIME + new Random().nextInt(OFFSET_TIME));
    }

    /**
     *
     * @param gameEventListener the game world listener.
     * @param pacman the pacman who touches this element.
     */
    public void onCollided(GameEventListener gameEventListener, Pacman pacman){
        cell.setStaticElement(null);

        this.eater = pacman;
        this.previousColor = pacman.getPacmanColor();
        this.eater.setSpeedModifier(SPEED_MODIFIER);
        this.eater.setPacmanColor(Color.MAGENTA);
        super.start();

    }

    @Override
    protected void endAction() {
        this.eater.setSpeedModifier(1f);
        this.eater.setPacmanColor(this.previousColor);
    }
}
