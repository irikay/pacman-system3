/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pacman_infd.Enums;

/**
 * Directions given in clockwise order
 * @author Marinus
 */
public enum Direction {

    UP,RIGHT,DOWN,LEFT;

    public static int length = Direction.values().length;

    public Direction getOpposite() {
        return values()[(ordinal() + values().length/2) % values().length];
    }
    
    public Direction nextDirectionClockwise() {
        return values()[(ordinal() + 1) % values().length];
    }

    public Direction nextDirectionCounterClockwise() {
        return values()[Math.floorMod((ordinal() - 1), values().length)];
    }
}
