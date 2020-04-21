/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package pacman_infd.Strategies;

import pacman_infd.Game.Cell;

/**
 *
 * @author Marinus
 */
public class ReturnHomeStrategy implements Strategy{
    
    private final PathFinder pathFinder;
    private Cell homeCell;
    
    public ReturnHomeStrategy(Cell homeCell){
        pathFinder = new PathFinder();
        this.homeCell = homeCell;
    }
    
    @Override
    public Cell giveNextCell(Cell currentCell) {
        return pathFinder.nextCellInPath(currentCell, homeCell);
    }
}
