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
 * Make pacman invisible for a time.
 */
public class TomatoElement extends ATimeBasedElement{

    private static final String IMAGE_NAME = "tomato";

    private static final int DEFAULT_TIME = 1000;
    private static final int RANDOM_TIME_OFFSET = 5000;

    private Pacman invisiblePacman;

    public TomatoElement(Cell cell, ElementEventListener evtl, SoundManager sMger) throws IOException {
        super(cell, evtl, sMger, IMAGE_NAME, DEFAULT_TIME + new Random().nextInt(RANDOM_TIME_OFFSET));
    }

    public TomatoElement(Cell cell){
        super(cell, DEFAULT_TIME);
    }

    /**
     *
     * @param gameEventListener the game world listener.
     * @param pacman the pacman who touches this element.
     */
    public void onCollided(GameEventListener gameEventListener, Pacman pacman) {
        cell.setStaticElement(null);

        this.invisiblePacman = pacman;
        this.invisiblePacman.setVisible(false);
        this.invisiblePacman.setColorAlpha(0.5f);
        super.start();
    }

    @Override
    protected void endAction() {
        this.invisiblePacman.setVisible(true);
        this.invisiblePacman.setColorAlpha(1f);
    }
}
