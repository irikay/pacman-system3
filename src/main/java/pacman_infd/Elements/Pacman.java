/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pacman_infd.Elements;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.security.Key;
import java.util.HashMap;
import java.util.Map;

import pacman_infd.Game.BridgeCell;
import pacman_infd.Game.Cell;
import pacman_infd.Enums.Direction;
import pacman_infd.Game.ElementEventListener;
import pacman_infd.Game.SoundManager;

/**
 *
 * @author ivanweller
 */
public class Pacman extends MovingGameElement implements KeyListener {

    private int MOUTH_SIZE = 45;
    private final int END_MOUTH_ARC = 20;
    private final double CELL_SIZE_TO_PACMAN_SIZE_RATIO = 1.39;
    private final double CELL_SIZE_TO_EYE_SIZE_RATIO = 0.35;
    private final double CELL_SIZE_TO_PUPIL_SIZE_RATIO = 0.2;
    private final int PADDING = 5;
    private Color PACMAN_COLOR = Color.yellow;
    private final Color EYE_COLOR = Color.white;
    private final Color PUPIL_COLOR = Color.black;

    private int timeBeforeClosingMouth = 10;
    private int actualTimer = timeBeforeClosingMouth;

    /**
     * By Sam, to improve the movement
     */
    public Map<Integer, Direction> keyDirectionMap;

    /**
     * Save this new direction and try it on case if can do it. Improve the gameplay.
     */
    public Direction newDirection;

    public Pacman(Cell cell, ElementEventListener gameEventListener, int speed, SoundManager sMger) {
        super(cell, gameEventListener, speed, sMger);

        this.keyDirectionMap = new HashMap<Integer, Direction>();
        this.keyDirectionMap.put(KeyEvent.VK_UP, Direction.UP);
        this.keyDirectionMap.put(KeyEvent.VK_DOWN, Direction.DOWN);
        this.keyDirectionMap.put(KeyEvent.VK_LEFT, Direction.LEFT);
        this.keyDirectionMap.put(KeyEvent.VK_RIGHT, Direction.RIGHT);

    }

    /**
     *
     * @param color the new color for the pacman.
     */
    public void setPacmanColor(Color color){
        this.PACMAN_COLOR = color;
    }

    /**
     *
     * @return the color of pacman.
     */
    public Color getPacmanColor(){
        return this.PACMAN_COLOR;
    }

    /**
     *
     * @param alpha the alpha to the pacman color.
     */
    public void setColorAlpha(float alpha){
        this.PACMAN_COLOR = new Color(
                PACMAN_COLOR.getRed() / 255f,
                PACMAN_COLOR.getGreen() / 255f,
                PACMAN_COLOR.getBlue() / 255f,
                alpha
        );
    }

    /**
     *
     * @return the current Direction.
     */
    public Direction getCurrentDirection(){
        return this.currentDirection;
    }

    /**
     * Move to the next cell according to currentDirection. Will not move if the
     * next cell has a wall. Check for collisions after move is complete.
     */
    @Override
    public void move() {
        if(this.newDirection != null &&
                cell.getNeighbor(this, newDirection) != null &&
                !cell.getNeighbor(this, newDirection).hasWall()) {
            currentDirection = newDirection;
            newDirection = null;
        }

        Cell moveTo = cell.getNeighbor(this, currentDirection);
        if (moveTo != null && !moveTo.hasWall()) {
            moveTo.addMovingElement(this);
            cell.removeMovingElement(this);
            setCell(moveTo);
        }
    }

    /**
     * Change pacman's direction
     * @param direction the new direction
     */
    public void changeDirection(Direction direction) {
        currentDirection = direction;
    }

    /**
     * Draw Pacman.
     *
     * @param g
     */
    @Override
    public void draw(Graphics g) {

        int pacmanSize = (int) Math.round(cell.getSize() * CELL_SIZE_TO_PACMAN_SIZE_RATIO);
        int eyeSize = (int) Math.round(cell.getSize() * CELL_SIZE_TO_EYE_SIZE_RATIO);
        int pupilSize = (int) Math.round(cell.getSize() * CELL_SIZE_TO_PUPIL_SIZE_RATIO);

        int posX = (int) getPosition().getX() - PADDING;
        int posY = (int) getPosition().getY() - PADDING;
        int centerPacmanX = posX + pacmanSize / 2;
        int centerPacmanY = posY + pacmanSize / 2;
        Point centerPacman = new Point(centerPacmanX, centerPacmanY);
        int centerEyeX = centerPacmanX + pacmanSize/4;
        int centerEyeY = centerPacmanY - pacmanSize/4;
        Point centerEye = new Point(centerEyeX, centerEyeY);

        //body
        int numberRotation = getNumberRotation();
        g.setColor(PACMAN_COLOR);
        int end = getEndMouthArc(numberRotation);

        int bodySize;
        if (actualTimer > 0) {
            bodySize = 360 - MOUTH_SIZE;
            actualTimer -= 1;
        } else {
            actualTimer = timeBeforeClosingMouth;
            bodySize = 360;
        }
        g.fillArc( posX,
                posY,
                pacmanSize,
                pacmanSize,
                end,
                bodySize
        );

        for(int i=0; i < numberRotation; i++) {
            rotateAroundCenter(centerPacman, centerEye);
        }
        //eye
        g.setColor(EYE_COLOR);
        drawOval(g, eyeSize, centerEye);

        //pupil
        g.setColor(PUPIL_COLOR);
        drawOval(g, pupilSize, centerEye);
    }

    private void drawOval(Graphics g, int size, Point center) {
        g.fillOval(
                doubleToInt(center.getX() - size / 2),
                doubleToInt(center.getY() - size / 2),
                size,
                size
        );
    }

    private int doubleToInt(double dbl) {
        return (int) Math.round(dbl);
    }

    private int getNumberRotation() {
        Direction tmpDir;
        int numberRotation = 0;
        if (currentDirection == null)
            tmpDir = Direction.RIGHT;
        else
            tmpDir = currentDirection;
        while(tmpDir != Direction.RIGHT) {
            tmpDir = tmpDir.nextDirectionCounterClockwise();
            numberRotation++;
        }
        return numberRotation;
    }

    private void rotateAroundCenter(Point center, Point point) {
        double angle = (double) 360/Direction.length;
        angle = Math.toRadians(angle);
        double newX = center.getX() +
                (point.getX()-center.getX())*Math.cos(angle) - (point.getY()-center.getY())*Math.sin(angle);
        double newY = center.getY() +
                (point.getX()-center.getX())*Math.sin(angle) + (point.getY()-center.getY())*Math.cos(angle);
        point.setLocation(newX, newY);
    }

    /**
     * Get the end arc of the mouth
     * @return the arc in degree
     */
    private int getEndMouthArc(int numberRotation) {
        int endArc = END_MOUTH_ARC;
        for(int i=0; i < numberRotation; i++) {
            endArc -= 360/Direction.length;
        }
        return endArc;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        newDirection = this.keyDirectionMap.getOrDefault(e.getKeyCode(), null);
        /*if (cell.getNeighbor(newDirection) != null && !cell.getNeighbor(newDirection).hasWall()) {
            currentDirection = newDirection;
        }*/
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

}
