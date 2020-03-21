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
public interface Strategy {
    public Cell giveNextCell(Cell currentCell);
}
