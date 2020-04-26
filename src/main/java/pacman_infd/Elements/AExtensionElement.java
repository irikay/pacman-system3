package pacman_infd.Elements;

import pacman_infd.Game.*;
import pacman_infd.Pacman_INFD;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * Classed add by BOOSKO Sam to easily implement new element features.
 * Because it's not my job to update previous implement to make it easily.
 * As for me, all element that pacman can eat should be an implement of the interface Eatable.
 * And an extends from an class AEatable with basic thing. Like this the EventHandler just call a method like isEating
 * when pacman eat an element. Then with this way, the action on the game when pacman eats something is managed on the
 * eatable element.
 */
public abstract class AExtensionElement extends GameElement{

    private static final String IMAGE_DEFAULT_PATH = "/Images/";
    private static final Map<String, BufferedImage> IMAGES_SAVED = new HashMap<>();

    private Image img;

    public AExtensionElement(Cell cell, ElementEventListener evtl, SoundManager sMger, String imageName)
            throws IOException {
        super(cell, evtl, sMger);

        this.img = loadImage(imageName);
    }

    public AExtensionElement(Cell cell){
        super(cell, null, null);
    }

    public static BufferedImage loadImage(String imageName) throws IOException{
        String path = IMAGE_DEFAULT_PATH + imageName + ".png";

        BufferedImage img = IMAGES_SAVED.getOrDefault(imageName, null);
        if(img == null){
            try(InputStream input = Pacman_INFD.class.getResourceAsStream(path)){
                if(input == null){
                    throw new IOException("Image resource found :" + path);
                }

                BufferedImage bufferedImage = ImageIO.read(input);
                img = bufferedImage;
                IMAGES_SAVED.put(imageName, img);
                return img;

            }
        }else{
            return img;
        }
    }

    @Override
    public void draw(Graphics g) {
        g.drawImage(this.img,
                (int)getPosition().getX(),
                (int)getPosition().getY(),
                null);
    }
}
