package pacman_infd;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import pacman_infd.Elements.ExtensionElements.*;
import pacman_infd.Elements.Ghost;
import pacman_infd.Elements.Pacman;
import pacman_infd.Enums.Direction;
import pacman_infd.Game.BridgeCell;
import pacman_infd.Game.GameController;
import pacman_infd.Game.GameWorld;
import pacman_infd.Game.ScorePanel;

import java.awt.*;
import java.io.IOException;

public class ExtentionTest {

    GameController gameController;
    GameWorld gameWorld;
    ScorePanel scorePanel;

    @Before
    public void setUp() {
        char[][] levelMap = {
                {'A', 'A', 'A', 'A'},
                {'A', '-', '-', 'A'},
                {'A', '-', '-', 'A'},
                {'A', 'A', 'A', 'A'}
        };
        scorePanel = new ScorePanel();
        gameController = new GameController(null, scorePanel);
        gameWorld = new GameWorld(gameController, levelMap, gameController.getSoundManager(), null, 0);
        gameController.setGameWorld(gameWorld);
    }

    @Test
    public void testTrapElement() {

        Pacman pacman = new Pacman(gameWorld.getCellMap()[1][1], gameWorld.eventHandler, 1, null);
        pacman.stopTimer();

        new TrapElement(gameWorld.getCellMap()[2][1]);

        pacman.changeDirection(Direction.DOWN);
        pacman.moveTimerActionPerformed();

        Assert.assertFalse(pacman.isMoving());

        Ghost ghost1 = new Ghost(gameWorld.getCellMap()[1][1], gameWorld.eventHandler, 1, null,
                Color.yellow, gameWorld.soundManager);
        ghost1.stopTimer();

        TrapElement trapElement = new TrapElement(gameWorld.getCellMap()[2][1]);
        trapElement.onCollided(null, ghost1);

        Assert.assertFalse(ghost1.isMoving());

    }

    @Test
    public void testTeleporter(){

        Pacman pacman = new Pacman(gameWorld.getCellMap()[2][1], gameWorld.eventHandler, 1, null);
        pacman.stopTimer();

        TeleporterElement t1 = new TeleporterElement(gameWorld.getCellMap()[1][1]);
        TeleporterElement t2 = new TeleporterElement(gameWorld.getCellMap()[3][3]);

        t1.linkTo(t2);
        t2.linkTo(t1);

        Assert.assertTrue(t1.isUsable());
        Assert.assertTrue(t2.isUsable());

        Assert.assertTrue(pacman.getCell() == gameWorld.getCellMap()[2][1]);

        pacman.changeDirection(Direction.UP);
        pacman.moveTimerActionPerformed();

        Assert.assertTrue(pacman.getCell() == gameWorld.getCellMap()[3][3]);
        Assert.assertFalse(t1.isUsable());
        Assert.assertFalse(t2.isUsable());

    }

    @Test
    public void testBridge() throws IOException {

        BridgeCell bridge = new BridgeCell(1, 1, 2);

        Pacman pacman = new Pacman(bridge, gameWorld.eventHandler, 1, null, Direction.UP);
        pacman.stopTimer();

        Ghost ghost = new Ghost(bridge, gameWorld.eventHandler, 0, null, Color.yellow,
                gameWorld.soundManager, Direction.LEFT);
        ghost.stopTimer();

        Assert.assertTrue(bridge.isUnderTheBridgeByDirection(pacman.getCurrentDirection()));
        Assert.assertTrue(bridge.isAboveTheBridgeByDirection(ghost.getCurrentDirection()));

    }

    @Test
    public void testGrenade() throws IOException {

        Pacman pacman = new Pacman(gameWorld.getCellMap()[1][1], gameWorld.eventHandler, 1, null,
                Direction.UP);
        pacman.stopTimer();

        new GrenadeElement(gameWorld.getCellMap()[2][1], gameWorld.eventHandler, null);

        Ghost ghost1 = new Ghost(gameWorld.getCellMap()[1][2], gameWorld.eventHandler, 0, null,
                Color.yellow, gameWorld.soundManager);
        ghost1.stopTimer();

        Ghost ghost2 = new Ghost(gameWorld.getCellMap()[1][2], gameWorld.eventHandler, 0, null,
                Color.yellow, gameWorld.soundManager);
        ghost2.stopTimer();

        pacman.changeDirection(Direction.DOWN);
        pacman.moveTimerActionPerformed();

        Assert.assertTrue(ghost1.isDead());
        Assert.assertTrue(ghost2.isDead());

    }

    @Test
    public void testPepper() throws IOException {
        Pacman pacman = new Pacman(gameWorld.getCellMap()[1][1], gameWorld.eventHandler, 1, null,
                Direction.UP);
        pacman.stopTimer();

        PepperElement pepper = new PepperElement(gameWorld.getCellMap()[2][1], gameWorld.eventHandler, null);

        pacman.changeDirection(Direction.DOWN);
        pacman.moveTimerActionPerformed();

        Assert.assertTrue(pacman.getSpeed() > pacman.getDefaultSpeed());
        pepper.performEnd();
        Assert.assertTrue(pacman.getSpeed() <= pacman.getDefaultSpeed());

    }

    @Test
    public void testTomato() throws IOException{
        Pacman pacman = new Pacman(gameWorld.getCellMap()[1][1], gameWorld.eventHandler, 1, null,
                Direction.UP);
        pacman.stopTimer();

        TomatoElement tomato = new TomatoElement(gameWorld.getCellMap()[2][1], gameWorld.eventHandler, null);
        Ghost ghost1 = new Ghost(gameWorld.getCellMap()[2][2], gameWorld.eventHandler, 0, null,
                Color.yellow, gameWorld.soundManager);
        ghost1.stopTimer();

        pacman.changeDirection(Direction.DOWN);
        pacman.moveTimerActionPerformed();

        Assert.assertFalse(pacman.isVisible());

        pacman.changeDirection(Direction.RIGHT);
        pacman.moveTimerActionPerformed();

        Assert.assertTrue(pacman.getCell() == ghost1.getCell());

        tomato.performEnd();
        Assert.assertTrue(pacman.isVisible());

    }

    @Test
    public void testRedBean() throws IOException, InterruptedException {
        Pacman pacman = new Pacman(gameWorld.getCellMap()[1][1], gameWorld.eventHandler, 1, null,
                Direction.UP);
        pacman.stopTimer();

        RedBeanElement redBeanElement = new RedBeanElement(gameWorld.getCellMap()[2][1], gameWorld.eventHandler, null);
        Ghost ghost1 = new Ghost(gameWorld.getCellMap()[2][2], gameWorld.eventHandler, 0, null,
                Color.yellow, gameWorld.soundManager);
        ghost1.stopTimer();

        pacman.changeDirection(Direction.DOWN);
        pacman.moveTimerActionPerformed();
        pacman.changeDirection(Direction.RIGHT);

        Assert.assertTrue(redBeanElement.getShooter() == pacman);
    }

    @Test
    public void testPotato() throws IOException{
        Pacman pacman = new Pacman(gameWorld.getCellMap()[1][1], gameWorld.eventHandler, 1, null,
                Direction.UP);
        pacman.stopTimer();

        PotatoElement potatoElement = new PotatoElement(gameWorld.getCellMap()[2][1], gameWorld.eventHandler, null);
        Ghost ghost1 = new Ghost(gameWorld.getCellMap()[2][2], gameWorld.eventHandler, 1, null,
                Color.yellow, gameWorld.soundManager);
        ghost1.stopTimer();

        pacman.changeDirection(Direction.DOWN);
        pacman.moveTimerActionPerformed();

        Assert.assertTrue(ghost1.getSpeed() > ghost1.getDefaultSpeed());
        potatoElement.performEnd();
        Assert.assertTrue(ghost1.getSpeed() <= ghost1.getDefaultSpeed());

    }

    @Test
    public void testFishElement() {

        Pacman pacman = new Pacman(gameWorld.getCellMap()[1][1], gameWorld.eventHandler, 1, null);
        pacman.stopTimer();

        new FishElement(gameWorld.getCellMap()[2][1]);

        pacman.changeDirection(Direction.DOWN);
        pacman.moveTimerActionPerformed();

        Assert.assertTrue(pacman.getSpeed() < pacman.getDefaultSpeed());

    }

}
