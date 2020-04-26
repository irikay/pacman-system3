package pacman_infd.Elements.ExtensionElements;

import pacman_infd.Elements.AExtensionElement;
import pacman_infd.Elements.Ghost;
import pacman_infd.Elements.MovingGameElement;
import pacman_infd.Elements.Pacman;
import pacman_infd.Enums.Direction;
import pacman_infd.Game.Cell;
import pacman_infd.Game.ElementEventListener;
import pacman_infd.Game.GameEventListener;
import pacman_infd.Game.SoundManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class RedBeanElement extends ATimeBasedElement{

    private final static String IMAGINE_NAME = "red_bean";
    private final static String PROJECTILE_IMAGE_NAME = "projectile";

    private final static int DEFAULT_TIME = 2000;
    private final static int OFFSET_TIME = 3000;

    private final static int FIRE_RATE = 3;
    private final static int PROJECTILE_SPEED = 10;

    private final static float SHOOTER_SPEED_MODIFIER = 0.5f;
    private final static Color SHOOTER_COLOR = new Color(70, 0, 0);

    private Pacman shooter;
    private Color previousColor;

    /**
     * timer of the fire.
     */
    private Timer fireTimer;

    /**
     * The image of the projectile.
     */
    private Map<Direction, Image> projectileImgs;

    public RedBeanElement(Cell cell, ElementEventListener evtl, SoundManager sMger) throws IOException {
        super(cell, evtl, sMger, IMAGINE_NAME, DEFAULT_TIME + new Random().nextInt(OFFSET_TIME));

        this.projectileImgs = this.loadProjectileImages(AExtensionElement.loadImage(PROJECTILE_IMAGE_NAME));
        this.fireTimer = new Timer(1000 / FIRE_RATE, new FireTask());
    }

    private Map<Direction, Image> loadProjectileImages(BufferedImage img){
        Map<Direction, Image> maps = new HashMap<>();

        maps.put(Direction.UP, img);

        double rotation = Math.toRadians(90);
        double locationX = img.getWidth(null) / 2.;
        double locationY = img.getHeight(null) / 2;

        AffineTransform tx = AffineTransform.getRotateInstance(rotation, locationX, locationY);
        AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_BILINEAR);

        Image img2 = op.filter((BufferedImage) img, null);
        Image img3 = op.filter((BufferedImage) img2, null);
        Image img4 = op.filter((BufferedImage) img3, null);

        maps.put(Direction.RIGHT, img2);
        maps.put(Direction.DOWN, img3);
        maps.put(Direction.LEFT, img4);

        return maps;
    }

    /**
     *
     * @param gameEventListener the game world listener.
     * @param pacman the pacman who touches this element.
     */
    public void onCollided(GameEventListener gameEventListener, Pacman pacman){
        cell.setStaticElement(null);

        this.shooter = pacman;
        this.previousColor = pacman.getPacmanColor();
        this.shooter.setSpeedModifier(SHOOTER_SPEED_MODIFIER);
        this.shooter.setPacmanColor(SHOOTER_COLOR);

        this.fireTimer.start();
        super.start();
    }

    @Override
    protected void endAction() {
        this.fireTimer.stop();

        this.shooter.setSpeedModifier(1f);
        this.shooter.setPacmanColor(previousColor);
    }

    /**
     * Create a projectile.
     */
    private void shoot(){
        new Projectile(
                this.shooter.getCell(),
                super.elementEventListener,
                this.shooter.getSpeed() + PROJECTILE_SPEED,
                super.soundManager,
                shooter.getCurrentDirection());
    }

    /**
     * The fire task
     */
    private class FireTask implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e) {
            shoot();
        }
    }

    /**
     * The projectile
     */
    public class Projectile extends MovingGameElement{

        public Projectile(Cell cell, ElementEventListener gameEventListener,
                          float speed, SoundManager sMger, Direction direction) {
            super(cell, gameEventListener, (int) speed, sMger);

            super.currentDirection = direction;
        }

        /**
         *
         * @param gameEventListener the game event listener.
         * @param ghost the ghost touched.
         */
        public void onCollided(GameEventListener gameEventListener, Ghost ghost){
            if(!ghost.isDead()) {
                ghost.dead();

                this.destroy();
            }
        }

        /**
         * Destroy the projectile
         */
        private void destroy(){
            cell.removeMovingElement(this);
            setCell(null);
            stopTimer();
        }

        @Override
        protected void move() {
            Cell moveTo = cell.getNeighbor(this, currentDirection);
            if (moveTo != null && !moveTo.hasWall()) {
                moveTo.addMovingElement(this);
                cell.removeMovingElement(this);
                setCell(moveTo);
            }else{
                this.destroy();
            }
        }

        @Override
        public void draw(Graphics g) {
            if(cell != null) {
                g.drawImage(projectileImgs.get(getCurrentDirection()),
                        (int) getPosition().getX(),
                        (int) getPosition().getY(),
                        null);
            }
        }
    }
}
