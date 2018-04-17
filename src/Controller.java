import javax.imageio.ImageIO;
import javax.imageio.stream.FileImageOutputStream;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;

public class Controller {
    private ImageRegions ir;
    private Compressor compressor;
    private LinkedList<BufferedImage> animation;

    public Controller() {
        this.ir = null;
        this.compressor = null;
        this.animation = null;
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

    public boolean compressImage(int percent, boolean animate) {
        if (ir == null) return false;

        int K = ir.maxSize() * (100 - percent) / 100;

        if (animate) {
            return compressWithAnimation(K);
        } else {
            return compress(K);
        }
    }

    /**
     * Calls the Compressor on the base image, given the options from the Menu.
     * Need to think about what failure conditions might be.
     */
    // TODO: make private when testing done
    public boolean compress(int K) {

        try {
            compressor.compress(K);
            return true;
        } catch (IllegalArgumentException E) {
            return false;
        }
    }

    //TODO: make private when testing done
    public boolean compressWithAnimation(int K) {
        int currK = ir.maxSize();

        animation = new LinkedList<>();

        while (currK > K) {

            if (!compress(currK)) return false;

            animation.add(ir.getCompressed());
            currK = (int) Math.ceil(currK * 0.9);

        }

        return compress(K);

    }

    /**
     * Saves the compressed image as a PNG to the disk at the location specified by the user.
     */
    public boolean saveImageAsPNG(File f) {
        if (ir == null) return false;

        BufferedImage image = ir.getCompressed();

        try {
            ImageIO.write(image, "png", f);
            return true;

        } catch (IOException E) {
            return false;
        }

    }

    public boolean saveAnimationAsGIF(File f) {
        if (animation == null) return false;

        try {
            FileImageOutputStream out = new FileImageOutputStream(f);
            GifSequenceWriter g = new GifSequenceWriter(out, ir.getImage().getType(), 250, true);
            for (BufferedImage frame : animation) {
                g.writeToSequence(frame);
            }
            g.close();
        } catch (IOException E) {
            return false;
        }
        return true;
    }

    /**
     * Returns the base image that the controller is working with.
     */
    public BufferedImage getBaseImage() {
        return ir != null ? ir.getImage() : null;
    }

    public BufferedImage getCompressedImage() {
        return ir != null ? ir.getCompressed() : null;
    }
/*
import java.awt.*;
import java.io.File;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;

public class Controller
{
	private Image baseImage;
	private ImageRegions compressedImage;
    private Compressor m_compressor;
	public Controller()
	{
	}

	/**
	 * Takes a File from the Menu that the user selected, reads it in with ImageIO.read() into a BufferedImage,
	 * and sets the Controller's baseImage to that image.
	 
	public boolean setImageFile(File f)
	{
            BufferedImage reading_Img;
            try{
                reading_Img = ImageIO.read(f);
                baseImage = reading_Img;
                return true;
            }catch(Exception e){
                baseImage = null;
                return false;
            }	    
	}

	/**
	 * Returns the base image that the controller is working with.
	 */
	/*public Image getBaseImage()
	{
            if (baseImage!= null)
            {
                return baseImage;
            }else
            {
                return null;
            }
	}

        public void getCompressImage()
        {
            compressedImage = m_compressor.ir;
        }
	/**
	 * Calls the Compressor on the base image, given the options from the Menu.
	 * Need to think about what failure conditions might be.
	 
	public boolean compressImage(int percent, boolean animate)
	{
            try{
                compressedImage = new ImageRegions((BufferedImage)baseImage);
                m_compressor = new Compressor(compressedImage);
                m_compressor.segment(percent);
                return true;
            }catch(Exception e){}
		return false;
	}

	/**
	 * Saves the compressed image as a PNG to the disk at the location specified by the user.
	 
	public boolean saveImageAsPNG(File f)
	{
            try{
                ImageIO.write(compressedImage.image, "png", f);
                return true;
            }catch(Exception e)
            {  }
            return false;
	}

	/**
	 * Saves the compressed image as a CIF to the disk at the location specified by the user.
	 
	public boolean saveImageAsCIF(File f)
	{
	    try{
                ImageIO.write(compressedImage.image, "cif", f);
                return true;
            }catch(Exception e)
            {  }
            return false;
	}
*/
}
