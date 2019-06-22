package autonCalc.util;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;

public class GraphicsUtil {

    /**
     *
     * @param image image to make buffered
     * @return null on error, the given image as a buffered image if all goes well
     */
    public static BufferedImage imgToBufferedImage(Image image) {
        if (image == null) {
            return null;
        }

        BufferedImage bImg = null;

        Graphics2D gg;

        try {
            // create buffered image
            bImg = new BufferedImage(image.getWidth(null), image.getHeight(null),
                BufferedImage.TYPE_INT_ARGB); // use type ARGB so that alpha channel can exist
            gg = (Graphics2D) bImg.getGraphics();

            // draw the image to the buffered image
            gg.drawImage(image, 0, 0, null);
            gg.dispose(); // release the graphics2d object's contents immediately, no need to wait for gc
        } catch (Exception e) {
            bImg = null;
        }

        return bImg;
    }
}
