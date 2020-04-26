package pacman_infd.Elements.ExtensionElements;

import pacman_infd.Elements.AExtensionElement;
import pacman_infd.Game.Cell;
import pacman_infd.Game.ElementEventListener;
import pacman_infd.Game.SoundManager;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

/**
 * Help to create easily and element with end time-based.
 */
public abstract class ATimeBasedElement extends AExtensionElement {

    /**
     * The timer which is called at the end.
     */
    private Timer timer;

    public ATimeBasedElement(Cell cell, ElementEventListener evtl, SoundManager sMger, String imageName, int time)
            throws IOException {
        super(cell, evtl, sMger, imageName);

        this.timer = new Timer(time, new EndActionTask());
    }

    public ATimeBasedElement(Cell cell, int time){
        super(cell);

        this.timer = new Timer(time, new EndActionTask());
    }

    /**
     * Useful for unit test.
     */
    public void performEnd(){
        this.endTimer();
    }

    /**
     * Start the timer to call the end method after the timer given.
     */
    protected void start(){
        this.timer.start();
    }

    /**
     * The action thta the element should do at the end of the timer.
     */
    protected abstract void endAction();

    /**
     * The endTimer.
     */
    private void endTimer(){
        this.timer.stop();
        this.endAction();
    }

    /**
     * The action listener of the timer who call the end method.
     */
    private class EndActionTask implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent e) {
            endTimer();
        }
    }
}
