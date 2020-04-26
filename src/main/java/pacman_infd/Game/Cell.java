/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pacman_infd.Game;

import pacman_infd.Elements.GameElement;
import pacman_infd.Elements.Pacman;
import pacman_infd.Enums.Direction;
import java.awt.Graphics;
import java.util.*;

import pacman_infd.Elements.MovingGameElement;
import pacman_infd.Elements.Wall;

/**
 *
 * @author Marinus
 */
public class Cell {

    protected final int xPos;
    protected final int yPos;
    protected final int size;

    protected final Map<Direction, Cell> neighbors;
    protected List<MovingGameElement> movingElements;
    protected GameElement staticElement;

    private Cell pathParent;

    public Cell(int x, int y, int size) {
        xPos = x;
        yPos = y;
        this.size = size;

        movingElements = new ArrayList<>();
        neighbors = new HashMap<Direction, Cell>();
    }

    /**
     * Draw this cell and all elements in it.
     * @param g 
     */
    public void draw(Graphics g) {
        this.drawElements(g);
    }

    /**
     * Draw all elements contained by this cell.
     *
     * @param g Graphics object
     */
    protected void drawElements(Graphics g) {
        if (staticElement != null) {
            staticElement.draw(g);
        }
        if (movingElements != null) {
            for (GameElement element : movingElements) {
                element.draw(g);
            }
        }
    }

    /**
     * Returns true if one of the movingElements on this Cell is a Wall.
     *
     * @return
     */
    public boolean hasWall() {
        boolean hasWall = false;

        if (staticElement instanceof Wall) {
            hasWall = true;
        }

        return hasWall;
    }
    
    void clearCell(){
        movingElements.clear();
        staticElement = null;
    }

    /**
     * Add a GameElement to this cell.
     *
     * @param e GameElement
     */
    public void addMovingElement(MovingGameElement e) {
        movingElements.add(e);
    }

    /**
     * Remove a GameElement from this cell.
     *
     * @param e GameElement
     */
    public void removeMovingElement(MovingGameElement e) {
        movingElements.remove(e);
    }

    /**
     *
     * @return a list of all GameElements on this cell.
     */
    public List<MovingGameElement> getMovingElements() {
        return movingElements;
    }

    /**
     *
     * @param movingGameElement the moving element which wants this information.
     * @return the list of moving element on this case.
     */
    public List<MovingGameElement> getMovingElements(MovingGameElement movingGameElement){
        return this.getMovingElements();
    }

    /**
     * Set the neighboring cells for this cell
     *
     * @param dir
     * @param cell
     */
    public void setNeighbor(Direction dir, Cell cell) {
        neighbors.put(dir, cell);
    }

    /**
     *
     * @param dir Direction of cell
     * @return Cell neighbor of this cell in the direction specified.
     */
    public Cell getNeighbor(Direction dir) {
        return neighbors.get(dir);
    }

    /**
     *
     * @param movingGameElement the pacman.
     * @param dir direction tried.
     * @return the cell next to this, useful for bridge.
     */
    public Cell getNeighbor(MovingGameElement movingGameElement, Direction dir) {
        return neighbors.get(dir);
    }

    public boolean isNeighborCellNotAWall(Direction direction) {
        return getNeighbor(direction) != null && !getNeighbor(direction).hasWall();
    }

    /**
     *
     * @return a HashMap of neighboring Cells.
     */
    public Map getNeighbors() {
        return neighbors;
    }

    /**
     * 
     * @return staticElement
     */
    public GameElement getStaticElement() {
        return staticElement;
    }

    /**
     *
     * @param accessor the accessor.
     * @return staticElement of this cell.
     */
    public GameElement getStaticElement(MovingGameElement accessor){
        return this.getStaticElement();
    }

    /**
     * 
     * @param element 
     */
    public void setStaticElement(GameElement element) {
        staticElement = element;
    }

    /**
     *
     * @return the size of this cell.
     */
    public int getSize() {
        return size;
    }

    /**
     *
     * @return the x position of this cell.
     */
    public int getXpos() {
        return xPos;
    }

    /**
     *
     * @return the y position of this cell.
     */
    public int getYPos() {
        return yPos;
    }

    /**
     * Set the parent for this Cell. This is used by the PathFinder class to
     * create a path between two cells.
     *
     * @param cell parent of this Cell.
     */
    public void setPathParent(Cell cell) {
        pathParent = cell;
    }

    /**
     *
     * @return parent of this Cell in a path
     */
    public Cell getPathParent() {
        return pathParent;
    }

    @Override
    public String toString() {
        return "xPos: " + xPos + "\nYPos: " + yPos + "\nNumber of Neighbors: " + neighbors.size();
    }

    /**
     * By BOOSKO Sam.
     * @param type the selector type.
     * @param distance the distance (or depth) max of the searching.
     * @param <C> extends GameElement, we search only GameElement (cause the grid cells contain GameElement).
     * @return a list of Game Elements Found.
     */
    public <C extends GameElement> List<C> getGameElementNear(Class<C> type, int distance){
        List<C> gameElementsFound = new LinkedList<>();
        distance++;

        List<Cell> explored = new LinkedList<>();
        Queue<Cell>[] toExplore = loadQueues(distance);
        toExplore[0].add(this);

        Cell isExploring;
        Queue<Cell> exploringQueue;
        Queue<Cell> nextQueue;

        for(int currentDepth = 0; currentDepth < distance; currentDepth++){
            exploringQueue = toExplore[currentDepth];
            //Don't need to explore useless next depth because we are already on the max.
            nextQueue = currentDepth == distance - 1 ? null : toExplore[currentDepth + 1];

            while((isExploring = exploringQueue.poll()) != null){
                isExploring.getGameElementsOfType(type, gameElementsFound);
                explored.add(isExploring);
                if(nextQueue != null){ // In the case of the current depth is the max.
                    nextQueue.addAll(isExploring.nextCells(explored));
                }
            }

        }

        return gameElementsFound;
    }

    /**
     *
     * @param size depending of distance.
     * @return Using array of Queues to be able to know what is the depth. i = distance or depth.
     */
    private Queue<Cell>[] loadQueues(int size){
        Queue<Cell>[] queues = new Queue[size];
        for(int i = 0; i < size; i++){
            queues[i] = new LinkedList<>();
        }
        return queues;
    }

    /**
     *
     * @param except list of ignored cells.
     * @return a list of cells next this cell except if the cell is present in the list except.
     */
    private List<Cell> nextCells(List<Cell> except){
        List<Cell> nextCellsList = new LinkedList<>();

        Cell nextCell;
        for(Direction direction : Direction.values()){
            nextCell = this.neighbors.getOrDefault(direction, null);
            if(nextCell != null &&
                    (nextCell.staticElement == null ||
                    nextCell.staticElement.getClass() != Wall.class)
                    && !except.contains(nextCell)){
                nextCellsList.add(nextCell);
            }
        }

        return nextCellsList;
    }

    /**
     *
     * @param type the selector type.
     * @param outputList the output list where all gameElement of the selector type are added.
     * @param <C> extends GameElement, explained why before.
     */
    private <C extends GameElement> void getGameElementsOfType(Class<C> type, List<C> outputList){
        for(GameElement gameElement : movingElements){
            if(gameElement.getClass() == type){
                outputList.add((C) gameElement);
            }
        }

        if(staticElement != null && staticElement.getClass() == type){
            outputList.add((C) staticElement);
        }

    }

    protected Object o = new Object();

    /**
     *
     * @param collision the checker between two game elements.
     * @param collider the game element who collides others.
     * @param gameEventListener the game event listener provided to collision.
     */
    public void checkCollision(ICollision collision, MovingGameElement collider, GameEventListener gameEventListener){
        synchronized (this.o) {
            if(this.getStaticElement() != null){
                collision.onCollision(collider, this.getStaticElement(), gameEventListener);
            }


            for (MovingGameElement movingGameElement : this.getMovingElements()) {
                collision.onCollision(collider, movingGameElement, gameEventListener);
            }
        }
    }

}
