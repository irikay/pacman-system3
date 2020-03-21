/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pacman_infd;

import pacman_infd.Enums.Direction;
import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import pacman_infd.Elements.*;
import pacman_infd.Enums.PortalType;
import pacman_infd.Strategies.ChasePacmanStrategy;
import pacman_infd.Strategies.MoveRandomStrategy;

/**
 *
 * @author Marinus
 */
public class GameWorld {

    private static final int CELL_SIZE = 26; //pixels

    private static final char PELLET = '0';
    private static final char SUPER_PELLET = '2';
    private static final char BLINKY = 'a';
    private static final char PINKY = 'b';
    private static final char INKY = 'c';
    private static final char CLYDE = 'd';
    private static final char NOTHING = '-';
    private static final char PACMAN = 'P';

    private int width;
    private int height;

    private final SoundManager soundManager;
    private final View view;
    private EventHandler eventHandler;

    private List<Cell> cells;
    private Cell[][] cellMap;

    private char[][] elementMap;

    private final int gameSpeed;
    private int numberOfPelletsAtStart;

    private Portal portalBlue;
    private Portal portalOrange;

    //todo soundmanager appartient pas a gameController?
    GameWorld(GameController gameController, char[][] levelMap, SoundManager sMger, View view, int speed) {

        soundManager = sMger;
        this.view = view;
        gameSpeed = speed;

        eventHandler = new EventHandler(gameController, this);

        if (levelMap != null) {

            this.elementMap = levelMap;
            width = elementMap[0].length;
            height = elementMap.length;
            createCells();
            findNeighbors();

            placeElements(elementMap, cellMap);

            numberOfPelletsAtStart = countPellets();
        }
    }

    /**
     * Create a grid of cells.
     *
     */
    private void createCells() {

        cells = new ArrayList<>();
        cellMap = new Cell[height][width];

        for (int x = 0; x < height; x++) {
            for (int y = 0; y < width; y++) {
                Cell cell = new Cell(y, x, CELL_SIZE);
                cellMap[x][y] = cell;
                cells.add(cell);
            }
        }
    }

    /**
     * Finds all neighbors for each cell and adds them to the neighbors Map of
     * each cell.
     */
    private void findNeighbors() {

        for (int x = 0; x < height; x++) {
            for (int y = 0; y < width; y++) {
                if (x - 1 >= 0) {
                    cellMap[x][y].setNeighbor(Direction.UP, cellMap[x - 1][y]);
                }
                if (x + 1 < height) {
                    cellMap[x][y].setNeighbor(Direction.DOWN, cellMap[x + 1][y]);
                }
                if (y - 1 >= 0) {
                    cellMap[x][y].setNeighbor(Direction.LEFT, cellMap[x][y - 1]);
                }
                if (y + 1 < width) {
                    cellMap[x][y].setNeighbor(Direction.RIGHT, cellMap[x][y + 1]);
                }
            }
        }

    }

    /**
     * Places walls on the cellMap according to wallMap
     *
     * @param elementMap array of integers representing the walls (1=wall, 0=no wall)
     * @param cellMap cell array of level.
     */
    private void placeElements(char[][] elementMap, Cell[][] cellMap) {

        for (int x = 0; x < height; x++) {
            for (int y = 0; y < width; y++) {
                placeElement(elementMap[x][y], cellMap[x][y]);
            }
        }
    }

    /**
     * Place a wall on the cellMap, and create another object if not a wall
     * @param elementType the type of element
     * @param cell the cell
     */
    private void placeElement(char elementType, Cell cell) {
        switch (elementType) {
            case PELLET:
                Pellet p = new Pellet(cell, eventHandler, soundManager);
                break;
            case SUPER_PELLET:
                SuperPellet s = new SuperPellet(cell, eventHandler, soundManager);
                break;
            case BLINKY:
                Ghost blinky = new Ghost(cell, eventHandler, gameSpeed, new ChasePacmanStrategy(), Color.RED, soundManager);
                break;
            case PINKY:
                Ghost pinky = new Ghost(cell, eventHandler, gameSpeed, new ChasePacmanStrategy(), Color.PINK, soundManager);
                break;
            case INKY:
                Ghost inky = new Ghost(cell, eventHandler, gameSpeed, new MoveRandomStrategy(), Color.CYAN, soundManager);
                break;
            case CLYDE:
                Ghost clyde = new Ghost(cell, eventHandler, gameSpeed, new MoveRandomStrategy(), Color.ORANGE, soundManager);
                break;
            case PACMAN:
                Pacman pacman = new Pacman(cell, eventHandler, gameSpeed, soundManager);
                view.addKeyListener(pacman);
                break;
            case NOTHING:
                break;
            default:
                Wall w = new Wall(cell, elementType);
                break;
        }
    }

    /**
     * Draw each cell in the game world.
     *
     * @param g Graphics object
     */
    private void drawCells(Graphics g) {
        if (cells != null) {
            for (Cell cell : cells) {
                cell.draw(g);
            }
        }
    }

    /**
     * Draw the game world.
     *
     * @param g
     */
    public void draw(Graphics g) {
        g.clearRect(0, 0, width * CELL_SIZE, height * CELL_SIZE);
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, width * CELL_SIZE, height * CELL_SIZE);
        drawCells(g);
    }

    /**
     * Counts the number of pellets currently in the GameWorld.
     *
     * @return number of pellets
     */
    private int countPellets() {
        int number = 0;
        for (Cell cell : cells) {
            if (cell.getStaticElement() instanceof Pellet) {
                number++;
            }
        }
        return number;
    }

    /**
     * Places a cherry on a random cell that has no static element.
     */
    void placeCherryOnRandomEmptyCell() {
        if (countPellets() == numberOfPelletsAtStart / 2) {
            List<Cell> emptyCells = getEmptyCells();
            Random r = new Random();
            if (!emptyCells.isEmpty()) {
                Cherry c = new Cherry(emptyCells.get(r.nextInt(emptyCells.size() - 1)), eventHandler, soundManager);
            }
        }
    }

    void spawnPortal(int x, int y, int mouseButton) {
        int cellX = x / CELL_SIZE;
        int cellY = y / CELL_SIZE;
        findNeighbors();
        if (isEmptyGameCell(cellMap[cellY][cellX])) {
            if (mouseButton == 1) {
                spawnPortalInCell(PortalType.BLUE, cellX, cellY);
            } else if (mouseButton == 3) {
                spawnPortalInCell(PortalType.ORANGE, cellX, cellY);
            }
        }
    }

    private void spawnPortalInCell(PortalType portalType, int cellX, int cellY) {
        PortalType otherPortalType;
        if (portalType.equals(PortalType.BLUE)) {
            otherPortalType = PortalType.ORANGE;
        } else if (portalType.equals(PortalType.ORANGE)) {
            otherPortalType = PortalType.BLUE;
        } else {
            otherPortalType = null;
        }
        if (getPortal(portalType) != null) {
            getPortal(portalType).remove();
        }
        setPortal(new Portal(cellMap[cellY][cellX], portalType, soundManager));
        soundManager.playSound("portal");
        if (getPortal(otherPortalType) != null) {
            getPortal(portalType).setLinkedPortal(getPortal(otherPortalType));
            getPortal(otherPortalType).setLinkedPortal(getPortal(portalType));
            getPortal(portalType).warpNeighbors();
            getPortal(otherPortalType).warpNeighbors();
        }
    }

    private boolean isEmptyGameCell(Cell cell) {
        return cell.getYPos() < cellMap.length && !cell.hasWall() && cell.getStaticElement() == null;
    }

    void clearGameWorld(){
        for(Cell cell: cells){
            cell.clearCell();
        }
        eventHandler = null;
        cellMap = null;
        cells = null;
    }

    /**
     * Returns a list of all cells that have no static element placed on them.
     *
     * @return list of cells
     */
    private List<Cell> getEmptyCells() {
        List<Cell> emptyCells = new ArrayList<>();
        for (Cell cell : cells) {
            if (cell.getStaticElement() == null) {
                emptyCells.add(cell);
            }
        }
        return emptyCells;
    }

    /**
     *
     * @return number of Pellets at the start of the game.
     */
    public int getNumberOfPelletsAtStart() {
        return numberOfPelletsAtStart;
    }

    /**
     * @return the portalBlue
     */
    Portal getPortalBlue() {
        return portalBlue;
    }

    /**
     * @param portal the portal to set
     */
    void setPortal(Portal portal) {
        if (portal.getType().equals(PortalType.BLUE)) {
            this.portalBlue = portal;
        }
       else if (portal.getType().equals(PortalType.ORANGE)) {
            this.portalOrange = portal;
        }
    }

    /**
     * @param portalType the portal type to get
     */
    Portal getPortal(PortalType portalType) {
        if (portalType.equals(PortalType.BLUE)) {
            return portalBlue;
        }
        else if (portalType.equals(PortalType.ORANGE)) {
            return portalOrange;
        }
        else {
            return null;
        }
    }

    /**
     * @param portalBlue the portalBlue to set
     */
    void setPortalBlue(Portal portalBlue) {
        this.portalBlue = portalBlue;
    }

    /**
     * @return the portalOrange
     */
    Portal getPortalOrange() {
        return portalOrange;
    }

    /**
     * @param portalOrange the portalOrange to set
     */
    void setPortalOrange(Portal portalOrange) {
        this.portalOrange = portalOrange;
    }

    Cell[][] getCellMap() {
        return cellMap;
    }

    List<Cell> getCells() {
        return cells;
    }

}
