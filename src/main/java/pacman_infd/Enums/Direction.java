/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pacman_infd.Enums;

/**
 *
 * @author Marinus
 */
public enum Direction {

    UP, DOWN, LEFT, RIGHT;

    /**
     *
     * @return
     */
    public Direction getOpposite() {
        Direction d = null;
        switch (this) {
            case UP:
                d = DOWN; break;
            case DOWN:
                d = UP; break;
            case LEFT:
                d = RIGHT; break;
            case RIGHT:
                d = UP; break;

        }
        return d;
    }
    
    public Direction nextDirectionClockwise() {
        Direction d = null;
        switch (this) {
            case UP:
                d = RIGHT; break;
            case DOWN:
                d = LEFT; break;
            case LEFT:
                d = UP; break;
            case RIGHT:
                d = DOWN; break;
        }
        return d;
    }
    
    public Direction nextDirectionCounterClockwise() {
        Direction d = null;
        switch (this) {
            case UP:
                d = LEFT; break;
            case DOWN:
                d = RIGHT; break;
            case LEFT:
                d = DOWN; break;
            case RIGHT:
                d = UP; break;
        }
        return d;
    }
}
