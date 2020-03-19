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

    private int width;
    private int height;

    private SoundManager soundManager;
    private View view;
    private EventHandler eventHandler;

    private ArrayList<Cell> cells;
    private Cell[][] cellMap;

    private char[][] elementMap;

    private int gameSpeed;
    private int numberOfPelletsAtStart;

    private Portal portalBlue;
    private Portal portalOrange;

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
     * @param width
     * @param height
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
     * @param wMap array of integers representing the walls (1=wall, 0=no wall)
     * @param cMap cell array of level.
     */
    private void placeElements(char[][] elementMap, Cell[][] cellMap) {

        for (int x = 0; x < height; x++) {
            for (int y = 0; y < width; y++) {
                if (elementMap[x][y] == '0') {
                    Pellet p = new Pellet(cellMap[x][y], eventHandler, soundManager);
                } else if (elementMap[x][y] == '2') {
                    SuperPellet s = new SuperPellet(cellMap[x][y], eventHandler, soundManager);
                } else if (elementMap[x][y] == 'a') {
                    Ghost blinky = new Ghost(cellMap[x][y], eventHandler, gameSpeed, new ChasePacmanStrategy(), Color.RED, soundManager);
                } else if (elementMap[x][y] == 'b') {
                    Ghost pinky = new Ghost(cellMap[x][y], eventHandler, gameSpeed, new ChasePacmanStrategy(), Color.PINK, soundManager);
                } else if (elementMap[x][y] == 'c') {
                    Ghost inky = new Ghost(cellMap[x][y], eventHandler, gameSpeed, new MoveRandomStrategy(), Color.CYAN, soundManager);
                } else if (elementMap[x][y] == 'd') {
                    Ghost clyde = new Ghost(cellMap[x][y], eventHandler, gameSpeed, new MoveRandomStrategy(), Color.ORANGE, soundManager);
                } else if (elementMap[x][y] == '-') {
                    // niks
                } else if (elementMap[x][y] == 'P') {
                    Pacman pacman = new Pacman(cellMap[x][y], eventHandler, gameSpeed, soundManager);
                    view.addKeyListener(pacman);
                } else {
                    Wall w = new Wall(cellMap[x][y], elementMap[x][y]);
                }
            }
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
            ArrayList<Cell> emptyCells = getEmptyCells();
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
        if (cellY < cellMap.length) {
            if (!cellMap[cellY][cellX].hasWall() && cellMap[cellY][cellX].getStaticElement() == null) {
                if (mouseButton == 1) {
                    if (getPortalBlue() != null) {
                        getPortalBlue().remove();
                    }
                    setPortalBlue(new Portal(cellMap[cellY][cellX], PortalType.BLUE, soundManager));
                    soundManager.playSound("portal");
                    if (getPortalOrange() != null) {
                        getPortalBlue().setLinkedPortal(getPortalOrange());
                        getPortalOrange().setLinkedPortal(getPortalBlue());
                        getPortalBlue().warpNeighbors();
                        getPortalOrange().warpNeighbors();
                    }
                } else if (mouseButton == 3) {
                    if (getPortalOrange() != null) {
                        getPortalOrange().remove();
                    }
                    setPortalOrange(new Portal(cellMap[cellY][cellX], PortalType.ORANGE, soundManager));
                    soundManager.playSound("portal");
                    if (getPortalBlue() != null) {
                        getPortalOrange().setLinkedPortal(getPortalBlue());
                        getPortalBlue().setLinkedPortal(getPortalOrange());
                        getPortalOrange().warpNeighbors();
                        getPortalBlue().warpNeighbors();
                    }
                }

            }
        }
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
    private ArrayList<Cell> getEmptyCells() {
        ArrayList<Cell> emptyCells = new ArrayList<>();
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

    ArrayList<Cell> getCells() {
        return cells;
    }

}
