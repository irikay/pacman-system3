/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pacman_infd.Elements;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import pacman_infd.Cell;
import pacman_infd.Enums.Direction;
import pacman_infd.ElementEventListener;
import pacman_infd.SoundManager;

/**
 *
 * @author ivanweller
 */
public class Pacman extends MovingGameElement implements KeyListener {

    private Direction currentDirection;

    public Pacman(Cell cell, ElementEventListener gameEventListener, int speed, SoundManager sMger) {
        super(cell, gameEventListener, speed, sMger);

    }

    /**
     * Move to the next cell according to currentDirection. Will not move if the
     * next cell has a wall. Check for collisions after move is complete.
     */
    @Override
    protected void move() {
        Cell moveTo = cell.getNeighbor(currentDirection);
        if (moveTo != null && !moveTo.hasWall()) {
            moveTo.addMovingElement(this);
            cell.removeMovingElement(this);
            setCell(moveTo);
        }
    }

    @Override
    public void moveTimerActionPerformed(ActionEvent e) {
        move();
        elementEventListener.movingElementActionPerformed(this);
    }

    /**
     * Draw Pacman.
     *
     * @param g
     */
    @Override
    public void draw(Graphics g) {

        //body
        g.setColor(Color.YELLOW);
        g.fillOval(
                (int) getPosition().getX() - 5,
                (int) getPosition().getY() - 5,
                cell.getSize() + 10,
                cell.getSize() + 10
        );
        //eye
        g.setColor(Color.WHITE);
        g.fillOval(
                (int) getPosition().getX() + 13,
                (int) getPosition().getY() - 1,
                10,
                10
        );
        //pupil
        g.setColor(Color.BLACK);
        g.fillOval(
                (int) getPosition().getX() + 16,
                (int) getPosition().getY() + 1,
                5,
                5
        );
        //mouth
        g.fillArc(
                (int) getPosition().getX() - 5,
                (int) getPosition().getY() - 5,
                cell.getSize() + 10,
                cell.getSize() + 10,
                -25,
                45
        );

    }

    @Override
    public void keyPressed(KeyEvent e) {
        Direction newDirection = currentDirection;

        switch (e.getKeyCode()) {
            case KeyEvent.VK_UP:
                newDirection = Direction.UP;
                break;
            case KeyEvent.VK_DOWN:
                newDirection = Direction.DOWN;
                break;
            case KeyEvent.VK_LEFT:
                newDirection = Direction.LEFT;
                break;
            case KeyEvent.VK_RIGHT:
                newDirection = Direction.RIGHT;
                break;
            default:
                break;
        }
        if (cell.getNeighbor(newDirection) != null && !cell.getNeighbor(newDirection).hasWall()) {
            currentDirection = newDirection;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

}
