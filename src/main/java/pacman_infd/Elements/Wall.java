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

    private char type;

    public Wall(Cell cell, char type) {

        super(cell, null, null);
        this.type = type;
    }

    @Override
    public void draw(Graphics g) {

        if (type == 'Q') {
            drawLeftUpCorner(g);
        }

        if (type == 'W') {
            drawRightUpCorner(g);
        }

        if (type == 'E') {
            drawRightDownCorner(g);
        }

        if (type == 'R') {
            drawLeftDownCorner(g);
        }

        if (type == 'A') {
            drawVerticalLine(g);
        }

        if (type == 'S') {
            drawHorizontalLine(g);
        }

        if (type == 'G') {
            drawLeftDownCorner(g);
            drawLeftUpCorner(g);
        }

        if (type == 'H') {
            drawRightDownCorner(g);
            drawRightUpCorner(g);
        }

        if (type == 'I') {
            drawRightUpCorner(g);
            drawLeftUpCorner(g);
        }
        if (type == 'J') {
            drawRightDownCorner(g);
            drawLeftDownCorner(g);
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
