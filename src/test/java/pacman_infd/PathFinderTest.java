/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pacman_infd;

import java.awt.Color;
import java.util.List;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import pacman_infd.Elements.Ghost;
import pacman_infd.Elements.Pacman;
import pacman_infd.Enums.Direction;

/**
 *
 * @author ivanweller
 */
public class PathFinderTest {

    GameController gameController;
    GameWorld gameWorld;
    PathFinder pathFinder;

    public PathFinderTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
        gameController = new GameController(null, null);
        pathFinder = new PathFinder();

    }

    @After
    public void tearDown() {
    }

    @Test
    public void pathFinderTestGeval1() {
        char[][] levelMap = {
            {'-', 'A'},
            {'-', '-'},
            {'-', 'A'},
            {'A', 'A'}
        };
        
        //create a new GameWorld.
        gameWorld = new GameWorld(gameController, levelMap, gameController.getSoundManager(), new View(), 0);
        
        // Set up moving elements in the GameWorld.
        Ghost ghost1 = new Ghost(gameWorld.getCellMap()[1][0], null, 100, null, Color.yellow, null);
        Ghost ghost2 = new Ghost(gameWorld.getCellMap()[1][0], null, 100, null, Color.yellow, null);
        Ghost ghost3 = new Ghost(gameWorld.getCellMap()[1][0], null, 100, null, Color.yellow, null);
        Ghost ghost4 = new Ghost(gameWorld.getCellMap()[2][0], null, 100, null, Color.yellow, null);
        Pacman pacman = new Pacman(gameWorld.getCellMap()[2][0], null, 100, null);
        
        //Find the path to Pacman.
        List path = pathFinder.findPathToPacman(gameWorld.getCellMap()[0][0]);
        
        //assert that the list is not empty.
        assert(path != null);
        //assert that the size of the list equals 2.
        assert(path.size() == 2);
        //assert that the path is correct.
        assert(path.get(0) == gameWorld.getCellMap()[0][0].getNeighbor(Direction.DOWN));
        Cell pacCell = (Cell)path.get(1);
        //assert that the cell that holds Pacman, holds it as the second element in the list.
        assert(pacCell.getMovingElements().get(1) == pacman);
        
    }

    @Test
    public void pathFinderTestGeval2() {
        char[][] levelMap = {
            {'-', 'P'},
            {'A', '-'}
        };
        //create a new GameWorld.   
        gameWorld = new GameWorld(gameController, levelMap, gameController.getSoundManager(), new View(), 0);
        //find the next cell in the shortest path to the cell that holds Pacman from LevelMap[0][1].
        Cell cell = pathFinder.nextCellInPathToPacman(gameWorld.getCellMap()[0][1]);
        //assert that cell equals the cell that holds Pacman.
        assert (cell == gameWorld.getCellMap()[0][1]);
        //assert that cell holds an instance of Pacman as the first element in the cell.
        assert (cell.getMovingElements().get(0) instanceof Pacman);
    }

    @Test
    public void pathFinderTestGeval3() {

        char[][] levelMap = {
            {'-'},
            {'A'}
        };
        
        //create a new GameWorld.        
        gameWorld = new GameWorld(gameController, levelMap, gameController.getSoundManager(), new View(), 0);
        //assert that there's no valid path to Pacman.
        assert (pathFinder.findPathToPacman(gameWorld.getCellMap()[0][0]) == null);

        

    }

    @Test
    public void pathFinderTestGeval4() {

        char[][] levelMap = {
            {'-'},
            {'P'}
        };
        
        //create a new GameWorld.
        gameWorld = new GameWorld(gameController, levelMap, gameController.getSoundManager(), new View(), 0);
        //Get the next cell in the path to Pacman.
        Cell cell = pathFinder.nextCellInPathToPacman((gameWorld.getCellMap()[0][0]));
        //assert that cell holds an instance of Pacman.
        assert (cell.getMovingElements().get(0) instanceof Pacman);
        //assert that cell is a valid neighbour of gameWorld.getCellMap()[0][0].
        assert (gameWorld.getCellMap()[0][0].getNeighbors().values().contains(cell));

    }

}
