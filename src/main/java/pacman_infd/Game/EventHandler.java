/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pacman_infd.Game;

import java.util.ArrayList;

import pacman_infd.Elements.Eatable;
import pacman_infd.Elements.Ghost;
import pacman_infd.Elements.MovingGameElement;
import pacman_infd.Elements.Pacman;

/**
 *
 * @author Marinus
 */
public class EventHandler implements ElementEventListener {

    private final GameEventListener gameEventListener;
    private final GameWorld gameWorld;

    EventHandler(GameEventListener gameEventListener, GameWorld gameWorld) {
        this.gameEventListener = gameEventListener;
        this.gameWorld = gameWorld;
    }

    /**
     * Checks if ghost is in the same cell as pacman
     * @param g 
     */
    private void checkCollisions(Ghost g) {
        Cell cell = g.getCell();

        boolean pacmanFound = false;
        for (MovingGameElement element : cell.getMovingElements()) {
            if (element instanceof Pacman) {
                pacmanFound = true;
                break;
            }
        }
        if (pacmanFound) {
            g.eatMe();
        }
    }

    /**
     * checks if pacman found any eatable object.
     * @param p 
     */
    private void checkCollisions(Pacman p) {

        Cell cell = p.getCell();

        ArrayList<Eatable> eatables = new ArrayList();

        if (cell.getStaticElement() instanceof Eatable) {
            Eatable element = (Eatable) cell.getStaticElement();
            eatables.add(element);
        }

        for (MovingGameElement element : cell.getMovingElements()) {
            if (element instanceof Eatable) {
                Eatable eatable = (Eatable) element;
                eatables.add(eatable);
            }
        }

        for (Eatable eatable : eatables) {
            eatable.eatMe();
        }

    }

    /**
     * This is called whenever a moving gameElement has moved.
     * @param e 
     */
    @Override
    public void movingElementActionPerformed(MovingGameElement e) {
        if (e instanceof Pacman) {
            checkCollisions((Pacman) e);
            gameEventListener.refocus();
        } else if (e instanceof Ghost) {
            checkCollisions((Ghost) e);
        }

    }

    /**
     * this is called whenever an eatable object has been eaten.
     * @param e 
     */
    @Override
    public void eatableElementEaten(Eatable e) {
        gameEventListener.increasePoints(e.getValue());
        gameWorld.placeCherryOnRandomEmptyCell();
    }

    @Override
    public void killPacman() {
        gameEventListener.decreaseLife();

        try {
            Thread.sleep(1000);
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
        }

        ArrayList<MovingGameElement> movers = new ArrayList();
        for (Cell cell : gameWorld.getCells()) {
            movers.addAll(cell.getMovingElements());
        }
        for (MovingGameElement mover : movers) {
            mover.reset();
        }

    }

    @Override
    public void makeGhostsVulnerable() {
        for (Cell cell : gameWorld.getCells()) {
            for (MovingGameElement element : cell.getMovingElements()) {
                if (element instanceof Ghost) {
                    Ghost ghost = (Ghost) element;
                    ghost.flee();
                }

            }
        }
    }

}
