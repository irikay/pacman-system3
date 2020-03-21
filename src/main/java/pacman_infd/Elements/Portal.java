/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pacman_infd.Elements;

import java.awt.Color;
import java.awt.Graphics;
import pacman_infd.Game.Cell;
import pacman_infd.Enums.Direction;
import pacman_infd.Enums.PortalType;
import pacman_infd.Game.SoundManager;

/**
 *
 * @author Marinus
 */
public class Portal extends GameElement {

    private Portal linkedPortal;
    private final PortalType type;

    public Portal(Cell cell, PortalType type, SoundManager sMger) {
        super(cell, null, sMger);
        this.type = type;
    }


    public PortalType getType() {
        return type;
    }

    public void remove() {
        if (cell.getStaticElement() == this) {
            cell.setStaticElement(null);
        }

    }

    public void warpNeighbors() {
        if (cell.isNeighborCellNotAWall(Direction.UP)) {
            cell.getNeighbor(Direction.UP).setNeighbor(Direction.DOWN, linkedPortal.getCell());
        }
        if (cell.isNeighborCellNotAWall(Direction.DOWN)) {
            cell.getNeighbor(Direction.DOWN).setNeighbor(Direction.UP, linkedPortal.getCell());
        }
        if (cell.isNeighborCellNotAWall(Direction.LEFT)) {
            cell.getNeighbor(Direction.LEFT).setNeighbor(Direction.RIGHT, linkedPortal.getCell());
        }
        if (cell.isNeighborCellNotAWall(Direction.RIGHT)) {
            cell.getNeighbor(Direction.RIGHT).setNeighbor(Direction.LEFT, linkedPortal.getCell());
        }

    }

    public void setLinkedPortal(Portal portal) {
        linkedPortal = portal;
    }

    public Portal getLinkedPortal() {
        return linkedPortal;
    }

    @Override
    public void draw(Graphics g) {

        int n;
        if(linkedPortal == null){
            n = 40;
        }
        else{
            n = 12;
        }

        int x_pos = (int) getPosition().getX();
        int y_pos = (int) getPosition().getY();
        int cell_size = getCell().getSize();

        if (type == PortalType.BLUE) {
            for (int i = 0; i < n; i++) {
                g.setColor(new Color(i, 4 * i + 50, 255));
                g.drawOval(
                         x_pos + (cell_size / 2) - 20 + (i / 2),
                        y_pos + (cell_size / 2) - 20 + (i / 2),
                        40 - i,
                        48 - i
                );
            }
        } else {
            for (int i = 0; i < n; i++) {
                g.setColor(new Color(255, 4 * i + 50, i));
                g.drawOval(
                        x_pos + (cell_size/ 2) - 20 + (i / 2),
                        y_pos + (cell_size / 2) - 20 + (i / 2),
                        40 - i,
                        48 - i
                );
            }
        }
    }

}
