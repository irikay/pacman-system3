package pacman_infd.Elements.ExtensionElements;

import pacman_infd.Elements.AExtensionElement;
import pacman_infd.Elements.Pacman;
import pacman_infd.Game.Cell;
import pacman_infd.Game.ElementEventListener;
import pacman_infd.Game.GameEventListener;
import pacman_infd.Game.SoundManager;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

/**
 * Couple of teleporters can be created to teleport pacman between them following time rule.
 */
public class TeleporterElement extends AExtensionElement {

    private static final String[] TELEPORTER_NAMES = {"teleporter", "teleporter2"};

    private static final int BORKEN_LINK = 2000;

    /**
     * Used to switch teleporter skin each new one.
     */
    private static int ID = 0;

    /**
     * The teleporter linked to this one.
     */
    private TeleporterElement linkedTeleporter;

    /**
     * Timer used to know when the teleporter is usable.
     */
    private Timer timer;

    /**
     * true if the teleporter can be used.
     */
    private boolean isUsable;

    public TeleporterElement(Cell cell, ElementEventListener evtl, SoundManager sMger) throws IOException {
        super(cell, evtl, sMger, TELEPORTER_NAMES[ID]);
        ID = (ID + 1) % TELEPORTER_NAMES.length;

        this.timer = new Timer(BORKEN_LINK, new NotUsableAction());

        this.isUsable = true;
    }

    public TeleporterElement(Cell cell){
        super(cell);

        this.timer = new Timer(BORKEN_LINK, new NotUsableAction());

        this.isUsable = true;
    }

    /**
     *
     * @param teleporterElement the teleporter to link with this one.
     */
    public void linkTo(TeleporterElement teleporterElement){
        this.linkedTeleporter = teleporterElement;
    }

    /**
     *
     * Make the teleporter unusable for a time setup before.
     */
    public void setUnusable(){
        this.isUsable = false;
        this.timer.start();
    }

    /**
     *
     * @return true if the teleporter can be used, else false.
     */
    public boolean isUsable(){
        return this.isUsable;
    }

    /**
     *
     * @param gameEventListener the game world listener.
     * @param pacman the pacman who touches this element.
     */
    public void onCollided(GameEventListener gameEventListener, Pacman pacman) {
        if(this.isUsable() && this.linkedTeleporter.isUsable()) {

            this.setUnusable();
            this.linkedTeleporter.setUnusable();

            Cell teleportedCell = this.linkedTeleporter.cell;

            cell.removeMovingElement(pacman);
            teleportedCell.addMovingElement(pacman);
            pacman.setCell(teleportedCell);
        }
    }

    /**
     * Action at the end of the timer.
     */
    private void endTimer(){
        this.isUsable = true;
        this.timer.stop();
    }

    /**
     * Action to do to make the teleporter not usable.
     */
    private class NotUsableAction implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent e) {
            endTimer();
        }
    }
}
