package pacman_infd.Game;

import pacman_infd.Elements.ExtensionElements.*;
import pacman_infd.Elements.GameElement;
import pacman_infd.Elements.Ghost;
import pacman_infd.Elements.Pacman;

import java.util.HashMap;
import java.util.Map;

/**
 * Basic map to perform collision between two game elements.
 */
public class GameCollisionsMap implements ICollision{

    private final Map<Class<? extends GameElement>, Map<Class<? extends GameElement>,
            ICollisionAction<? extends GameElement, ? extends GameElement>>> collisions;

    public GameCollisionsMap(){

        this.collisions = new HashMap<>();
        this.loadCollisionsMap();

    }

    /**
     *
     * @param c the Class extends GameElement key.
     * @return the map corresponding, creates the map if not exists.
     */
    private Map<Class<? extends GameElement>,
            ICollisionAction<? extends GameElement, ? extends GameElement>> getSubMap(Class<? extends GameElement> c){

        Map<Class<? extends GameElement>,
                ICollisionAction<? extends GameElement,
                        ? extends GameElement>> map = this.collisions.getOrDefault(c, null);

        if(map == null){
            map = new HashMap<>();
            this.collisions.put(c, map);
        }

        return map;
    }

    /**
     *
     * @param c1 the Class extends GameElement key.
     * @param c2 the Class extends GameElement key.
     * @param action the action to perform when collision between c1 and c2.
     */
    private <C1 extends GameElement, C2 extends  GameElement> void registerCollision(Class<C1> c1, Class<C2> c2,
                                   ICollisionAction<C1, C2> action){
        Map<Class<? extends GameElement>,
                ICollisionAction<? extends GameElement,
                        ? extends GameElement>> map = this.getSubMap(c1);

        map.put(c2, action);
    }

    /**
     * register all collision actions between gameElements.
     */
    private void loadCollisionsMap(){
        this.registerCollision(Pacman.class, TrapElement.class,
                (pacman, extensionElement, gameEventListener) -> {
                    extensionElement.onCollided(gameEventListener, pacman);
                });

        this.registerCollision(Ghost.class, TrapElement.class,
                (ghost, extensionElement, gameEventListener) -> {
                    extensionElement.onCollided(gameEventListener, ghost);
                });

        this.registerCollision(Pacman.class, TeleporterElement.class,
                (pacman, extensionElement, gameEventListener) -> {
                    extensionElement.onCollided(gameEventListener, pacman);
                });

        this.registerCollision(Pacman.class, GrenadeElement.class,
                (pacman, extensionElement, gameEventListener) -> {
                    extensionElement.onCollided(gameEventListener, pacman);
                });

        this.registerCollision(Pacman.class, PepperElement.class,
                (pacman, extensionElement, gameEventListener) -> {
                    extensionElement.onCollided(gameEventListener, pacman);
                });

        this.registerCollision(Pacman.class, TomatoElement.class,
                (pacman, extensionElement, gameEventListener) -> {
                    extensionElement.onCollided(gameEventListener, pacman);
                });

        this.registerCollision(Pacman.class, RedBeanElement.class,
                (pacman, extensionElement, gameEventListener) -> {
                    extensionElement.onCollided(gameEventListener, pacman);
                });

        this.registerCollision(RedBeanElement.Projectile.class, Ghost.class,
                (projectile, ghost, gameEventListener) -> {
                    projectile.onCollided(gameEventListener, ghost);
                });

        this.registerCollision(Pacman.class, PotatoElement.class,
                (pacman, extensionElement, gameEventListener) -> {
                    extensionElement.onCollided(gameEventListener, pacman);
                });

        this.registerCollision(Pacman.class, FishElement.class,
                (pacman, extensionElement, gameEventListener) -> {
                    extensionElement.onCollided(gameEventListener, pacman);
                });
    }

    /**
     *
     * @return the action between object1CLASS and object2CLASS.
     */
    private <C1 extends GameElement, C2 extends GameElement> ICollisionAction
        getCollisionAction(Class<C1> c1, Class<C2> c2) {

        Map<Class<? extends GameElement>, ICollisionAction<? extends GameElement, ? extends GameElement>> map =
                collisions.getOrDefault(c1, null);

        if(map == null){
            return null;
        }

        return map.getOrDefault(c2, null);

    }

    @Override
    public <C1 extends GameElement, C2 extends GameElement> void onCollision(C1 object1, C2 object2, GameEventListener gameEventListener) {
        if(object1 == object2 || object1 == null || object2 == null){
            return;
        }

        ICollisionAction<C1, C2> action = this.getCollisionAction(object1.getClass(), object2.getClass());

        if(action != null){
            action.onCollision(object1, object2, gameEventListener);
        }else{
            ICollisionAction<C2, C1> action2 = this.getCollisionAction(object2.getClass(), object1.getClass());

            if(action != null){
                action2.onCollision(object2, object1, gameEventListener);
            }
        }

    }

    private interface ICollisionAction<C1 extends GameElement, C2 extends GameElement>{

        void onCollision(C1 object1, C2 object2, GameEventListener gameEventListener);
    }
}
