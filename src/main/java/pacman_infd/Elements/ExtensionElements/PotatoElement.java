package pacman_infd.Elements.ExtensionElements;

import pacman_infd.Elements.Pacman;
import pacman_infd.Game.Cell;
import pacman_infd.Game.ElementEventListener;
import pacman_infd.Game.GameEventListener;
import pacman_infd.Game.SoundManager;

import java.io.IOException;
import java.util.Random;

/**
 * Increase de speed the ghosts on the board.
 */
public class PotatoElement extends ATimeBasedElement{

    private static final String IMAGE_NAME = "potato";

    private static final int TIME = 1000;
    private static final int OFFSET_TIME = 3000;

    private static final float GHOST_SPEED_MODIFIER = 2f;

    private GameEventListener gameEventListener;

    public PotatoElement(Cell cell, ElementEventListener evtl, SoundManager sMger) throws IOException {
        super(cell, evtl, sMger, IMAGE_NAME, TIME + new Random().nextInt(OFFSET_TIME));
    }

    public PotatoElement(Cell cell){
        super(cell, TIME);
    }

    /**
     *
     * @param gameEventListener the game world listener.
     * @param pacman the pacman who touches this element.
     */
    public void onCollided(GameEventListener gameEventListener, Pacman pacman){
        cell.setStaticElement(null);

        this.gameEventListener = gameEventListener;

        gameEventListener.actionOnGhost((ghost) -> {
            ghost.setSpeedModifier(GHOST_SPEED_MODIFIER);
        });

        super.start();

    }

    @Override
    protected void endAction() {
        this.gameEventListener.actionOnGhost((ghost) -> {
            ghost.setSpeedModifier(1f);
        });
    }
}
