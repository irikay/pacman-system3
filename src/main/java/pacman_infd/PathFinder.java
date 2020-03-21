/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pacman_infd;

import java.util.Collection;
import java.util.List;
import java.util.LinkedList;
import java.util.Queue;

import pacman_infd.Elements.MovingGameElement;
import pacman_infd.Elements.Pacman;

/**
 *
 * @author Marinus
 */
public class PathFinder {

    public PathFinder() {
    }

    /**
     * The first cell in the path List is the one that the object moving towards
     * pacman needs to take, so this returns the first cell in the path.
     *
     * @param rootCell
     * @return first cell in the path towards packman.
     */
    public Cell nextCellInPathToPacman(Cell rootCell) {
        List<Cell> path = findPath(rootCell, Pacman.class);
        return nextCell(rootCell, path);
    }

    public Cell nextCellInPath(Cell rootCell, Cell targetCell) {
        List<Cell> path = findPath(rootCell, targetCell);
        return nextCell(rootCell, path);
    }

    private Cell nextCell(Cell rootCell, List<Cell> path) {
        if (path != null && !path.isEmpty()) {
            return path.get(0);
        } else {
            return rootCell;
        }
    }

    /**
     * Constructs a path (List of cells in order) by 'walking' back along
     * cellParents of each cell starting with the given cell.
     *
     * @param cell start cell from which to walk back.
     * @return list of cells making up the path.
     */
    private List<Cell> constructPath(Cell cell) {

        LinkedList path = new LinkedList();
        while (cell.getPathParent() != null) {
            path.addFirst(cell);
            cell = cell.getPathParent();
        }

        return path;
    }

    /**
     * Uses a Breath-First search algorithm to determine the shortest path from
     * the start cell to a cell, if the target is a Class, it needs to be a Class of an instance of a GameElement
     * for exemple, Pacman, then it will search the nearest cell with Pacman.
     * If the target is a Cell, it will search the path to that cell.
     *
     * @param startCell the starting cell
     * @param target the target, a Cell or an Object Class
     * @return the path
     */
    List<Cell> findPath(Cell startCell, Object target) {

        LinkedList visitedCells = new LinkedList();

        Queue queue = new LinkedList();
        queue.offer(startCell);
        startCell.setPathParent(null);

        while (!queue.isEmpty()) {
            Cell cell = (Cell) queue.poll();

            if (target instanceof Class) {
                for (GameElement e : cell.getMovingElements()) {
                    if (e.getClass() == target) {
                        //pacman found
                        return constructPath(cell);
                    }
                }
            }

            if (target instanceof  Cell) {
                if (cell.equals(target)) {
                    //targetCell found
                    return constructPath(cell);
                }
            }

            visitedCells.add(cell);

            addCellChildInQueue(visitedCells, queue, cell);
        }

        //no path found
        return null;
    }

    private void addCellChildInQueue(LinkedList visitedCells, Queue queue, Cell cell) {
        for (Cell cellChild : (Collection<Cell>) cell.getNeighbors().values()) {
            if (!cellChild.hasWall() && !visitedCells.contains(cellChild) && !queue.contains(cellChild)) {
                cellChild.setPathParent(cell);
                queue.add(cellChild);
            }
        }
    }

}
