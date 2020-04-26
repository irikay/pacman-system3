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
 * Decrease the speed of the Pacman.
 */
public class FishElement extends ATimeBasedElement{

    private static final String IMAGINE_NAME = "fish";

    private static final int DEFAULT_TIME = 1000;
    private static final int RANDOM_TIME_OFFSET = 1000;

    private static final float SPEED_MODIFIER = 0.1f;
    private static final Color EFFECT_COLOR = Color.ORANGE;

    private Pacman victim;
    private Color previousColor;

    public FishElement(Cell cell, ElementEventListener evtl, SoundManager sMger) throws IOException {
        super(cell, evtl, sMger, IMAGINE_NAME, DEFAULT_TIME + new Random().nextInt(RANDOM_TIME_OFFSET));
    }

    /**
     *
     * @param gameEventListener the game world listener.
     * @param pacman the pacman who touches this element.
     */
    public void onCollided(GameEventListener gameEventListener, Pacman pacman) {
        cell.setStaticElement(null);

        this.victim = pacman;
        this.previousColor = pacman.getPacmanColor();

        this.victim.setSpeedModifier(SPEED_MODIFIER);
        this.victim.setPacmanColor(EFFECT_COLOR);

        super.start();
    }

    @Override
    protected void endAction() {
        this.victim.setSpeedModifier(1f);
        this.victim.setPacmanColor(this.previousColor);
    }
}
