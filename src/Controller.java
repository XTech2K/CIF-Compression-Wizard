import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Controller {
    private ImageRegions ir;
    private Compressor compressor;

    public Controller() {
        this.ir = null;
        this.compressor = null;
    }

    /**
     * Takes a File from the Menu that the user selected, reads it in with ImageIO.read() into a BufferedImage,
     * and sets the Controller's baseImage to that image.
     */
    public boolean setImageFile(File f) {
        try {
            ir = new ImageRegions(f);
            compressor = new Compressor(ir);
            return true;
        } catch (IOException E) {
            return false;
        }
    }

    /**
     * Returns the base image that the controller is working with.
     */
    public Image getBaseImage() {
        return ir.getImage();
    }

    /**
     * Calls the Compressor on the base image, given the options from the Menu.
     * Need to think about what failure conditions might be.
     */
    public boolean compressImage(int K, boolean animate) {
        if (ir == null) {
            throw new RuntimeException("ImageRegions never initialized");
        }
        //int K = ir.maxSize() * percent / 100;
        try {
            compressor.compress(K);
            return true;
        } catch (IllegalArgumentException E) {
            return false;
        }
    }

    /**
     * Saves the compressed image as a PNG to the disk at the location specified by the user.
     */
    public boolean saveImageAsPNG(File f) {
        BufferedImage image = ir.getCompressed();
        try {
            ImageIO.write(image, "png", f);
            return true;
        } catch (IOException E) {
            return false;
        }
    }

    /**
     * Saves the compressed image as a CIF to the disk at the location specified by the user.
     */
    public boolean saveImageAsCIF(File f) {
        return false;
    }
 
}
