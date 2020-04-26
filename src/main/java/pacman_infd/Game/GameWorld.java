/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pacman_infd.Game;

import pacman_infd.Elements.ExtensionElements.*;
import pacman_infd.Enums.Direction;
import java.awt.Color;
import java.awt.Graphics;
import java.io.IOException;
import java.util.*;

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

    /**
     * THE DEFAULT PACMAN SPEED !
     */
    private static final int PACMAN_SPEED = 10;
    /**
     * THE DEFAULT GHOST SPEED !
     */
    private static final int GHOST_SPEED = 5;

    /**
     * Extension : grenade char value to design the level.
     */
    private static final char TRAP = '9';
    private static final char TELEPORTER = '8';
    private static final char BRIDGE = '7';

    private static final char GRENADE = '6';
    private static final char PEPPER = '5';
    private static final char TOMATO = '4';
    private static final char RED_BEAN = '3';

    private static final char POTATO = '€';
    private static final char FISH = '$';

    private static final char[] EXTENSION_ELEMENTS = {
            TRAP,
            GRENADE,
            PEPPER,
            TOMATO,
            RED_BEAN,
            POTATO,
            FISH
    };

    /**
     * Used to linked with previous teleporter.
     * Only one teleporter link is allow by game.
     */
    private TeleporterElement oldTeleporter;

    private final Map<Character, IElementBuilder> ELEMENT_BUILDERS = getElementBuilders();
    private final Map<Character, ICellBuilder> CELL_BUILDERS = getCellBuilders();

    /**
     * By BOOSKO Sam.
     * @return a map for char -> gameElementBuilder
     * Char already used :
     * <li>0</li>
     * <li>2</li>
     * <li>a</li>
     * <li>b</li>
     * <li>c</li>
     * <li>d</li>
     * <li>-</li>
     * <li>P</li>
     * <li>Q</li>
     * <li>W</li>
     * <li>E</li>
     * <li>R</li>
     * <li>A</li>
     * <li>S</li>
     * <li>G</li>
     * <li>H</li>
     * <li>I</li>
     * <li>J</li>
     * Char for extension :
     * <li>9 to 3</li>
     */
    private Map<Character, IElementBuilder> getElementBuilders(){
        Map<Character, IElementBuilder> builders = new HashMap<Character, IElementBuilder>();

        builders.put(PELLET, new IElementBuilder() {
            @Override
            public void buildElement(Cell cell) {
                new Pellet(cell, eventHandler, soundManager);
            }
        });

        builders.put(BRIDGE, new IElementBuilder() { // Just add Pullet to the bridge
            @Override
            public void buildElement(Cell cell) {
                new Pellet(cell, eventHandler, soundManager);
            }
        });

        builders.put(SUPER_PELLET, new IElementBuilder() {
            @Override
            public void buildElement(Cell cell) {
                new SuperPellet(cell, eventHandler, soundManager);
            }
        });

        builders.put(BLINKY, new IElementBuilder() {
            @Override
            public void buildElement(Cell cell) {
                new Ghost(cell, eventHandler, GHOST_SPEED, new ChasePacmanStrategy(), Color.RED, soundManager);
            }
        });

        builders.put(PINKY, new IElementBuilder() {
            @Override
            public void buildElement(Cell cell) {
                new Ghost(cell, eventHandler, GHOST_SPEED, new ChasePacmanStrategy(), Color.PINK, soundManager);
            }
        });

        builders.put(INKY, new IElementBuilder() {
            @Override
            public void buildElement(Cell cell) {
                new Ghost(cell, eventHandler, GHOST_SPEED, new MoveRandomStrategy(), Color.CYAN, soundManager);
            }
        });

        builders.put(CLYDE, new IElementBuilder() {
            @Override
            public void buildElement(Cell cell) {
                new Ghost(cell, eventHandler, GHOST_SPEED, new MoveRandomStrategy(), Color.ORANGE, soundManager);
            }
        });

        builders.put(PACMAN, new IElementBuilder() {
            @Override
            public void buildElement(Cell cell) {
                Pacman pacman = new Pacman(cell, eventHandler, PACMAN_SPEED, soundManager);
                view.addKeyListener(pacman);
            }
        });

        builders.put(NOTHING, new IElementBuilder() {
            @Override
            public void buildElement(Cell cell) {}
        });

        //Extension
        builders.put(TRAP, new AElementBuilder(){
            @Override
            protected void buildExtensionElement(Cell cell) throws IOException {
                new TrapElement(cell, eventHandler, soundManager);
            }
        });

        builders.put(TELEPORTER, new AElementBuilder(){
            @Override
            protected void buildExtensionElement(Cell cell) throws IOException {
                if(oldTeleporter == null) {
                    oldTeleporter = new TeleporterElement(cell, eventHandler, soundManager);
                }else{
                    TeleporterElement teleporterElement = new TeleporterElement(cell, eventHandler, soundManager);
                    teleporterElement.linkTo(oldTeleporter);
                    oldTeleporter.linkTo(teleporterElement);
                    oldTeleporter = null;
                }
            }
        });

        builders.put(GRENADE, new AElementBuilder(){
            @Override
            protected void buildExtensionElement(Cell cell) throws IOException {
                new GrenadeElement(cell, eventHandler, soundManager);
            }
        });

        builders.put(PEPPER, new AElementBuilder(){
            @Override
            protected void buildExtensionElement(Cell cell) throws IOException {
                new PepperElement(cell, eventHandler, soundManager);
            }
        });

        builders.put(TOMATO, new AElementBuilder(){
            @Override
            protected void buildExtensionElement(Cell cell) throws IOException {
                new TomatoElement(cell, eventHandler, soundManager);
            }
        });

        builders.put(RED_BEAN, new AElementBuilder(){
            @Override
            protected void buildExtensionElement(Cell cell) throws IOException {
                new RedBeanElement(cell, eventHandler, soundManager);
            }
        });

        builders.put(POTATO, new AElementBuilder(){
            @Override
            protected void buildExtensionElement(Cell cell) throws IOException {
                new PotatoElement(cell, eventHandler, soundManager);
            }
        });

        builders.put(FISH, new AElementBuilder(){
            @Override
            protected void buildExtensionElement(Cell cell) throws IOException {
                new FishElement(cell, eventHandler, soundManager);
            }
        });

        return builders;
    }

    private Map<Character, ICellBuilder> getCellBuilders(){
        Map<Character, ICellBuilder> builders = new HashMap<>();

        builders.put(BRIDGE, new ACellBuilder() {
            @Override
            protected Cell buildCellExtension(int x, int y, int cellSize) throws IOException {
                return new BridgeCell(x, y, CELL_SIZE);
            }
        });

        return builders;
    }

    private int width;
    private int height;

    public final SoundManager soundManager;
    private final View view;
    public EventHandler eventHandler;

    private List<Cell> cells;
    private Cell[][] cellMap;

    public final int gameSpeed;
    private int numberOfPelletsAtStart;

    private Portal portalBlue;
    private Portal portalOrange;

    public GameWorld(GameController gameController, char[][] levelMap, SoundManager sMger, View view, int speed) {

        soundManager = sMger;
        this.view = view;
        gameSpeed = speed;

        eventHandler = new EventHandler(gameController, this);

        if (levelMap != null) {

            width = levelMap[0].length;
            height = levelMap.length;
            createCells(levelMap);
            findNeighbors();

            placeElements(levelMap, cellMap);


            numberOfPelletsAtStart = countPellets();
        }
    }

    /**
     * Create a grid of cells.
     *
     */
    private void createCells(char[][] levelMap) {

        cells = new ArrayList<>();
        cellMap = new Cell[height][width];

        ICellBuilder cellBuilder;
        Cell cell;

        for (int x = 0; x < height; x++) {
            for (int y = 0; y < width; y++) {
                cellBuilder = CELL_BUILDERS.getOrDefault(levelMap[x][y], null);
                if(cellBuilder == null) {
                    cell = new Cell(y, x, CELL_SIZE);
                }else{
                    cell = cellBuilder.buildCell(y, x, CELL_SIZE);
                }

                cellMap[x][y] = cell;
                cells.add(cell);
            }
        }
    }

    private Cell getCell(int cellX, int cellY) {
        if (cellY < width && cellX < height)
            return cellMap[cellX][cellY];
        else
            return null;
    }

    /**
     * Finds all neighbors for each cell and adds them to the neighbors Map of
     * each cell.
     */
    private void findNeighbors() {

        for (int x = 0; x < height; x++) {
            for (int y = 0; y < width; y++) {
                if (x - 1 >= 0) {
                    getCell(x , y).setNeighbor(Direction.UP, getCell(x - 1, y));
                }
                if (x + 1 < height) {
                    getCell(x , y).setNeighbor(Direction.DOWN, getCell(x + 1, y));
                }
                if (y - 1 >= 0) {
                    getCell(x , y).setNeighbor(Direction.LEFT, getCell(x, y - 1));
                }
                if (y + 1 < width) {
                    getCell(x , y).setNeighbor(Direction.RIGHT, getCell(x, y + 1));
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
                placeElement(elementMap[x][y], getCell(x, y));
            }
        }
    }

    /**
     * Place a wall on the cellMap, and create another object if not a wall
     * @param elementType the type of element
     * @param cell the cell
     */
    private void placeElement(char elementType, Cell cell) {

        IElementBuilder builder = ELEMENT_BUILDERS.getOrDefault(elementType, null);

        if(builder == null){
            new Wall(cell, elementType);
        }else{
            builder.buildElement(cell);
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
    public int countPellets() {
        int number = 0;
        for (Cell cell : cells) {
            if (cell.getStaticElement() instanceof Pellet) {
                number++;
            }
        }
        return number;
    }

    /**
     * Game is won is there is no pellet left
     * @return True if the game is won
     */
    Boolean gameIsWon() {
        return countPellets() == 0;
    }

    /**
     * Places a cherry on a random cell that has no static element.
     */
    void placeCherryOnRandomEmptyCell() {
        List<Cell> emptyCells = getEmptyCells();
        Random r = new Random();
        if (countPellets() == numberOfPelletsAtStart / 2) {
            if (!emptyCells.isEmpty()) {
                Cherry c = new Cherry(emptyCells.remove(r.nextInt(emptyCells.size() - 1)), eventHandler, soundManager);
            }
        }

        if(!emptyCells.isEmpty() && r.nextInt(100) >= 90){
            IElementBuilder builder = ELEMENT_BUILDERS.getOrDefault(EXTENSION_ELEMENTS[r.nextInt(EXTENSION_ELEMENTS.length)], null);

            builder.buildElement(emptyCells.get(r.nextInt(emptyCells.size())));

        }

    }

    public void spawnPortal(int x, int y, int mouseButton) {
        int cellX = x / CELL_SIZE;
        int cellY = y / CELL_SIZE;
        findNeighbors();
        if (isEmptyGameCell(getCell(cellY, cellX))) {
            if (mouseButton == 1) {
                spawnPortalInCell(PortalType.BLUE, cellY, cellX);
            } else if (mouseButton == 3) {
                spawnPortalInCell(PortalType.ORANGE, cellY, cellX);
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
        setPortal(new Portal(getCell(cellX, cellY), portalType, soundManager));
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
    public Portal getPortalBlue() {
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
    public void setPortalBlue(Portal portalBlue) {
        this.portalBlue = portalBlue;
    }

    /**
     * @return the portalOrange
     */
    public Portal getPortalOrange() {
        return portalOrange;
    }

    /**
     * @param portalOrange the portalOrange to set
     */
    public void setPortalOrange(Portal portalOrange) {
        this.portalOrange = portalOrange;
    }

    public Cell[][] getCellMap() {
        return cellMap;
    }

    List<Cell> getCells() {
        return cells;
    }

    /**
     * Interface to create an element from a character
     */
    private interface IElementBuilder{
        void buildElement(Cell cell);
    }

    /**
     * Class to help the creation of element from extension
     * By BOOSKO Sam
     */
    private abstract class AElementBuilder implements  IElementBuilder{
        public void buildElement(Cell cell){
            try{
                buildExtensionElement(cell);
            }catch (IOException e){
                System.err.println(e);
            }
        }

        protected abstract void buildExtensionElement(Cell cell) throws IOException;

    }

    /**
     * Interface to create an cell from a character
     */
    private interface ICellBuilder{
        Cell buildCell(int x, int y, int cellSize);
    }

    /**
     * Class to help the creation of cell from extension
     * By BOOSKO Sam
     */
    private abstract class ACellBuilder implements ICellBuilder{
        public Cell buildCell(int x, int y, int cellSize){
            try{
                return buildCellExtension(x, y, cellSize);
            }catch (IOException e){
                System.err.println(e);
            }
            return new Cell(x, y, cellSize);
        }

        protected abstract Cell buildCellExtension(int x, int y, int cellSize) throws IOException;
    }

}
