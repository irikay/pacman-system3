/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pacman_infd.Elements;

import java.awt.Color;
import java.awt.Graphics;
import pacman_infd.Cell;
import pacman_infd.GameElement;

/**
 *
 * @author ivanweller
 */
public class Wall extends GameElement {

    private final char type;

    private final char LEFT_UP_CORNER = 'Q';
    private final char RIGHT_UP_CORNER = 'W';
    private final char RIGHT_DOWN_CORNER = 'E';
    private final char LEFT_DOWN_CORNER = 'R';
    private final char VERTICAL = 'A';
    private final char HORIZONTAL = 'S';
    private final char LEFT_DOWN_LEFT_UP_CORNER = 'G';
    private final char RIGHT_DOWN_RIGHT_UP_CORNER = 'H';
    private final char RIGHT_UP_LEFT_UP_CORNER = 'I';
    private final char RIGHT_DOWN_LEFT_DOWN = 'J';



    public Wall(Cell cell, char type) {

        super(cell, null, null);
        this.type = type;
    }

    @Override
    public void draw(Graphics g) {
        switch(type) {
            case LEFT_UP_CORNER:
                drawLeftUpCorner(g);
                break;
            case RIGHT_UP_CORNER:
                drawRightUpCorner(g);
                break;
            case RIGHT_DOWN_CORNER:
                drawRightDownCorner(g);
                break;
            case LEFT_DOWN_CORNER:
                drawLeftDownCorner(g);
                break;
            case VERTICAL:
                drawVerticalLine(g);
                break;
            case HORIZONTAL:
                drawHorizontalLine(g);
                break;
            case LEFT_DOWN_LEFT_UP_CORNER:
                drawLeftDownCorner(g);
                drawLeftUpCorner(g);
                break;
            case RIGHT_DOWN_RIGHT_UP_CORNER:
                drawRightDownCorner(g);
                drawRightUpCorner(g);
                break;
            case RIGHT_UP_LEFT_UP_CORNER:
                drawRightUpCorner(g);
                drawLeftUpCorner(g);
                break;
            case RIGHT_DOWN_LEFT_DOWN:
                drawRightDownCorner(g);
                drawLeftDownCorner(g);
                break;
            default:
                break;
        }
    }

    private void drawLeftUpCorner(Graphics g) {
        g.setColor(Color.cyan);
        g.drawArc(
                (int) getPosition().getX() + 9,
                (int) getPosition().getY() + 9,
                32,
                32,
                90,
                90
        );
        g.drawArc(
                (int) getPosition().getX() + 15,
                (int) getPosition().getY() + 15,
                20,
                20,
                90,
                90
        );
        g.setColor(Color.BLUE);
        g.drawArc(
                (int) getPosition().getX() + 8,
                (int) getPosition().getY() + 8,
                32,
                32,
                90,
                90
        );
        g.drawArc(
                (int) getPosition().getX() + 16,
                (int) getPosition().getY() + 16,
                20,
                20,
                90,
                90
        );
    }

    private void drawLeftDownCorner(Graphics g) {
        g.setColor(Color.cyan);
        g.drawArc(
                (int) getPosition().getX() + 9,
                (int) getPosition().getY() - 17,
                32,
                32,
                180,
                90
        );
        g.drawArc(
                (int) getPosition().getX() + 15,
                (int) getPosition().getY() - 11,
                20,
                20,
                180,
                90
        );
        g.setColor(Color.BLUE);
        g.drawArc(
                (int) getPosition().getX() + 8,
                (int) getPosition().getY() - 16,
                32,
                32,
                180,
                90
        );
        g.drawArc(
                (int) getPosition().getX() + 16,
                (int) getPosition().getY() - 11,
                19,
                19,
                180,
                90
        );
    }

    private void drawRightUpCorner(Graphics g) {
        g.setColor(Color.cyan);
        g.drawArc(
                (int) getPosition().getX() - 16,
                (int) getPosition().getY() + 9,
                31,
                31,
                0,
                90
        );
        g.drawArc(
                (int) getPosition().getX() - 11,
                (int) getPosition().getY() + 15,
                20,
                20,
                0,
                90
        );
        g.setColor(Color.BLUE);
        g.drawArc(
                (int) getPosition().getX() - 15,
                (int) getPosition().getY() + 8,
                31,
                31,
                0,
                90
        );
        g.drawArc(
                (int) getPosition().getX() - 12,
                (int) getPosition().getY() + 16,
                20,
                20,
                0,
                90
        );
    }

    private void drawRightDownCorner(Graphics g) {
        g.setColor(Color.cyan);
        g.drawArc(
                (int) getPosition().getX() - 17,
                (int) getPosition().getY() - 17,
                32,
                32,
                270,
                90
        );
        g.drawArc(
                (int) getPosition().getX() - 11,
                (int) getPosition().getY() - 11,
                20,
                20,
                270,
                90
        );
        g.setColor(Color.BLUE);
        g.drawArc(
                (int) getPosition().getX() - 17,
                (int) getPosition().getY() - 17,
                33,
                33,
                270,
                90
        );
        g.drawArc(
                (int) getPosition().getX() - 11,
                (int) getPosition().getY() - 11,
                19,
                19,
                270,
                90
        );

    }

    private void drawVerticalLine(Graphics g) {
        g.setColor(Color.BLUE);
        g.drawLine(
                (int) getPosition().getX() + 8,
                (int) getPosition().getY(),
                (int) getPosition().getX() + 8,
                (int) getPosition().getY() + cell.getSize()
        );
        g.drawLine(
                (int) getPosition().getX() + 16,
                (int) getPosition().getY(),
                (int) getPosition().getX() + 16,
                (int) getPosition().getY() + cell.getSize()
        );
        g.setColor(Color.cyan);
        g.drawLine(
                (int) getPosition().getX() + 9,
                (int) getPosition().getY(),
                (int) getPosition().getX() + 9,
                (int) getPosition().getY() + cell.getSize()
        );
        g.drawLine(
                (int) getPosition().getX() + 15,
                (int) getPosition().getY(),
                (int) getPosition().getX() + 15,
                (int) getPosition().getY() + cell.getSize()
        );
    }

    private void drawHorizontalLine(Graphics g) {
        g.setColor(Color.BLUE);
        g.drawLine(
                (int) getPosition().getX(),
                (int) getPosition().getY() + 8,
                (int) getPosition().getX() + cell.getSize() - 1,
                (int) getPosition().getY() + 8
        );
        g.drawLine(
                (int) getPosition().getX(),
                (int) getPosition().getY() + 16,
                (int) getPosition().getX() + cell.getSize() - 1,
                (int) getPosition().getY() + 16
        );

        g.setColor(Color.cyan);
        g.drawLine(
                (int) getPosition().getX(),
                (int) getPosition().getY() + 9,
                (int) getPosition().getX() + cell.getSize() - 1,
                (int) getPosition().getY() + 9
        );
        g.drawLine(
                (int) getPosition().getX(),
                (int) getPosition().getY() + 15,
                (int) getPosition().getX() + cell.getSize() - 1,
                (int) getPosition().getY() + 15
        );
    }

}
