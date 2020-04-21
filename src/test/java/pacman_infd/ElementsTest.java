package pacman_infd;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import pacman_infd.Elements.Cherry;
import pacman_infd.Elements.Ghost;
import pacman_infd.Elements.Pacman;
import pacman_infd.Elements.Portal;
import pacman_infd.Enums.Direction;
import pacman_infd.Enums.GhostState;
import pacman_infd.Enums.PortalType;
import pacman_infd.Game.GameController;
import pacman_infd.Game.GameWorld;
import pacman_infd.Game.ScorePanel;
import pacman_infd.Strategies.ChasePacmanStrategy;

import java.awt.*;

public class ElementsTest {

    GameController gameController;
    GameWorld gameWorld;
    ScorePanel scorePanel;

    public ElementsTest(){
    }

    @Before
    public void setUp() {
        char[][] levelMap = {
                {'A', '-', '0'},
                {'A', '-', '0'},
                {'A', '2', '-'}
        };
        scorePanel = new ScorePanel();
        gameController = new GameController(null, scorePanel);
        gameWorld = new GameWorld(gameController, levelMap, gameController.getSoundManager(), null, 0);

    }

    @Test
    public void testEatingPelletAndCherry() {
        Pacman pacman = new Pacman(gameWorld.getCellMap()[1][1], null, 1, null);
        pacman.changeDirection(Direction.RIGHT);
        assert (gameWorld.getCellMap()[1][1].getMovingElements().size() == 1);
        assert (gameWorld.getCellMap()[1][2].getMovingElements().size() == 0);
        assert (gameWorld.countPellets() == 2);

        // Test the eating of one pellet
        pacman.move();
        gameWorld.eventHandler.movingElementActionPerformed(pacman);
        assert (gameWorld.getCellMap()[1][1].getMovingElements().size() == 0);
        assert (gameWorld.getCellMap()[1][2].getMovingElements().size() == 1);
        assert (gameWorld.countPellets() == 1);
        assert (scorePanel.getLives() == 3);

        Cherry cherry = new Cherry(gameWorld.getCellMap()[1][1], gameWorld.eventHandler, gameWorld.soundManager);

        pacman.changeDirection(Direction.LEFT);
        pacman.move();
        gameWorld.eventHandler.movingElementActionPerformed(pacman);

        assert (gameWorld.getCellMap()[1][1].getMovingElements().size() == 1);
        assert (scorePanel.getLives() == 4);
    }

    @Test
    public void testEatingSuperPellet() {
        Pacman pacman = new Pacman(gameWorld.getCellMap()[1][1], gameWorld.eventHandler, 1, gameWorld.soundManager);
        Ghost ghost1 = new Ghost(gameWorld.getCellMap()[0][1], gameWorld.eventHandler, 0, null, Color.yellow, gameWorld.soundManager);

        pacman.changeDirection(Direction.DOWN);
        assert (gameWorld.getCellMap()[1][1].getMovingElements().size() == 1);
        assert (gameWorld.getCellMap()[2][1].getMovingElements().size() == 0);
        assert (ghost1.getState() == GhostState.NORMAL);

        // Test eating a super pellet
        pacman.move();
        gameWorld.eventHandler.movingElementActionPerformed(pacman);
        assert (gameWorld.getCellMap()[1][1].getMovingElements().size() == 0);
        assert (gameWorld.getCellMap()[2][1].getMovingElements().size() == 1);
        assert (gameWorld.getCellMap()[0][1].getMovingElements().size() == 1);
        assert (ghost1.getState() == GhostState.VULNERABLE);

        // Pacman moves up to eat the ghost
        pacman.changeDirection(Direction.UP);
        pacman.move();
        gameWorld.eventHandler.movingElementActionPerformed(pacman);
        assert (gameWorld.getCellMap()[1][1].getMovingElements().size() == 1);
        pacman.move();
        gameWorld.eventHandler.movingElementActionPerformed(pacman);
        gameWorld.eventHandler.movingElementActionPerformed(ghost1);
        // Test if the ghost is dead
        assert (ghost1.getState() == GhostState.DEAD);
    }

    @Test
    public void testMovingToAWall() {
        Pacman pacman = new Pacman(gameWorld.getCellMap()[1][1], gameWorld.eventHandler, 1, gameWorld.soundManager);

        pacman.changeDirection(Direction.LEFT);
        assert (gameWorld.getCellMap()[1][1].getMovingElements().size() == 1);

        // Test move to a wall
        pacman.move();
        assert (gameWorld.getCellMap()[1][1].getMovingElements().size() == 1);
    }
}
