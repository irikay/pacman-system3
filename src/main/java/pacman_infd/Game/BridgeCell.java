package pacman_infd.Game;

import pacman_infd.Elements.AExtensionElement;
import pacman_infd.Elements.GameElement;
import pacman_infd.Elements.MovingGameElement;
import pacman_infd.Elements.Wall;
import pacman_infd.Enums.Direction;

import java.awt.*;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

/**
 * by BOOSKO Sam
 * Class to manage when game elements are under or above the bridge.
 */
public class BridgeCell extends Cell{

    private static final String[] IMAGE_NAMES = {"bridge"};

    /**
     * List of moving game elements under the bridge.
     */
    private List<MovingGameElement> underBridge;

    /**
     * List of moving game elements above the bridge.
     */
    private List<MovingGameElement> aboveBridge;

    /**
     * the image of the bridge.
     */
    private Image bridgeImage;

    public BridgeCell(int x, int y, int size) throws IOException {
        super(x, y, size);

        this.underBridge = new LinkedList<>();
        this.aboveBridge = new LinkedList<>();

        this.bridgeImage = AExtensionElement.loadImage(IMAGE_NAMES[0]);
    }

    /**
     * Draw all elements contained by this cell.
     *
     * @param g Graphics object
     */
    @Override
    protected void drawElements(Graphics g) {
        g.drawImage(this.bridgeImage,
                this.getXpos() * this.getSize(),
                this.getYPos() * this.getSize(),
                null);

        if (staticElement != null) {
            staticElement.draw(g);
        }
        if (this.aboveBridge != null) {
            for (GameElement element : this.aboveBridge) {
                element.draw(g);
            }
        }
    }

    /**
     *
     * @param movingGameElement add this element under the bridge.
     */
    public void addMovingGameElementUnder(MovingGameElement movingGameElement){
        this.underBridge.add(movingGameElement);
    }

    /**
     *
     * @param movingGameElement remove this element under the bridge.
     */
    public void removeMovingGameElementUnder(MovingGameElement movingGameElement){
        this.underBridge.remove(movingGameElement);
    }

    /**
     *
     * @param movingGameElement add this element above the bridge.
     */
    public void addMovingGameElementAbove(MovingGameElement movingGameElement){
        this.aboveBridge.add(movingGameElement);
    }

    /**
     *
     * @param movingGameElement remove this element above the bridge.
     */
    public void removeMovingGameElementAbove(MovingGameElement movingGameElement){
        this.aboveBridge.remove(movingGameElement);
    }

    /**
     *
     * @return a list of all GameElements on this cell depending of where is the moving game element.
     */
    @Override
    public List<MovingGameElement> getMovingElements(MovingGameElement accessor) {
        if(this.isUnderTheBridgeByDirection(accessor.getCurrentDirection())){
            return this.underBridge;
        }
        return this.aboveBridge;
    }

    /**
     *
     * @param accessor the accessor.
     * @return staticElement of this cell.
     */
    @Override
    public GameElement getStaticElement(MovingGameElement accessor){
        if(this.isAboveTheBridgeByDirection(accessor.getCurrentDirection())){
            return super.getStaticElement();
        }
        return null;
    }

    /**
     * Add a GameElement to this cell.
     *
     * @param e GameElement
     */
    @Override
    public void addMovingElement(MovingGameElement e) {
        super.movingElements.add(e);

        Direction currentDirection = e.getCurrentDirection();
        if(this.isUnderTheBridgeByDirection(currentDirection)){
            this.addMovingGameElementUnder(e);
        }else{
            this.addMovingGameElementAbove(e);
        }
    }

    /**
     * Remove a GameElement from this cell.
     *
     * @param e GameElement
     */
    @Override
    public void removeMovingElement(MovingGameElement e) {
        super.movingElements.remove(e);

        Direction currentDirection = e.getCurrentDirection();
        if(this.isUnderTheBridgeByDirection(currentDirection)){
            this.removeMovingGameElementUnder(e);
        }else{
            this.removeMovingGameElementAbove(e);
        }
    }

    /**
     *
     * @param direction the direction of the element.
     * @return true if the moving game element is under the bridge.
     */
    public boolean isUnderTheBridgeByDirection(Direction direction){
        return direction == Direction.UP ||direction == Direction.DOWN;
    }

    /**
     *
     * @param direction the direction of the element.
     * @return true if the moving game element is above the bridge.
     */
    public boolean isAboveTheBridgeByDirection(Direction direction){
        return direction == Direction.LEFT ||direction == Direction.RIGHT;
    }

    /**
     *
     * @param tryDir the direction tried.
     * @return Cell neighbor of this cell in the direction specified.
     */
    @Override
    public Cell getNeighbor(MovingGameElement movingGameElement, Direction tryDir) {
        Cell neighbor = null;
        Direction elementDirection = movingGameElement.getCurrentDirection();

        if(this.isUnderTheBridgeByDirection(elementDirection)){
            if(this.isUnderTheBridgeByDirection(tryDir)){
                neighbor = neighbors.get(tryDir);
            }
        }else if(this.isAboveTheBridgeByDirection(elementDirection)){
            if(this.isAboveTheBridgeByDirection(tryDir)){
                neighbor = neighbors.get(tryDir);
            }
        }

        if(neighbor == null){
            neighbor = new Cell(0, 0, 0);
            neighbor.setStaticElement(new Wall());
        }

        return neighbor;
    }

    /**
     *
     * @param collision the checker between two game elements.
     * @param collider the game element who collides others.
     * @param gameEventListener the game event listener provided to collision.
     */
    @Override
    public void checkCollision(ICollision collision, MovingGameElement collider, GameEventListener gameEventListener){
        synchronized (super.o) {
            if (this.isAboveTheBridgeByDirection(collider.getCurrentDirection())) {
                if (this.getStaticElement() != null) {
                    collision.onCollision(collider, this.getStaticElement(), gameEventListener);
                }

                for (MovingGameElement movingGameElement : this.aboveBridge) {
                    collision.onCollision(collider, movingGameElement, gameEventListener);
                }

            } else {
                for (MovingGameElement movingGameElement : this.underBridge) {
                    collision.onCollision(collider, movingGameElement, gameEventListener);
                }
            }
        }
    }

}
