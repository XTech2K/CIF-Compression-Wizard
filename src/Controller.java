import javax.imageio.ImageIO;
import javax.imageio.stream.FileImageOutputStream;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;

public class Controller
{
	private ImageRegions ir;
	private Compressor compressor;
	private LinkedList<BufferedImage> animation;

	public Controller()
	{
		this.ir = null;
		this.compressor = null;
		this.animation = null;
	}

	/**
	 * Takes a File from the Menu that the user selected, reads it in with ImageIO.read() into a BufferedImage,
	 * and sets the Controller's baseImage to that image.
	 */
	public boolean setImageFile(File f)
	{
		try
		{
			ir = new ImageRegions(f);
			compressor = new Compressor(ir);
			return true;
		}
		catch(IOException E)
		{
			return false;
		}
	}

	/**
	 * Chooses a number of regions to compress to based on a percent of the current number of regions in the image.
	 */
	public boolean compressToPercent(int percent, boolean animate)
	{
		if(ir == null) return false;

		int K = ir.getMaxSize() * (100 - percent) / 100;
		if(K == 0)
		{
			K = 1;
		}

		if(animate)
		{
			return compressWithAnimation(K);
		}
		else
		{
			return compress(K);
		}
	}

	/**
	 * Compresses to the number of regions specified by the caller.
	 */
	public boolean compressToRegions(int K, boolean animate)
	{
		if(ir == null) return false;
		if(K == 0) K = 1;

		if(animate)
			return compressWithAnimation(K);
		else
			return compress(K);
	}

	/**
	 * Calls the Compressor on the base image, given the options from the Menu.
	 * Need to think about what failure conditions might be.
	 */
	private boolean compress(int K)
	{

		try
		{
			compressor.compress(K);
			return true;
		}
		catch(IllegalArgumentException E)
		{
			return false;
		}
	}

	/**
	 * Create a list of images with gradually increasing compressions that can be combined to show an animation.
	 */
	private boolean compressWithAnimation(int K)
	{
		double currK = ir.getMaxSize();
		double ratio = Math.pow(K / currK, 1.0 / 60);

		animation = new LinkedList<>();

		while(currK > K)
		{

			if(!compress((int) Math.ceil(currK))) return false;

			animation.add(ir.getCompressed());
			currK *= ratio;

		}

		return compress(K);

	}

	/**
	 * Saves the compressed image as a PNG to the disk at the location specified by the user.
	 */
	public boolean saveImageAsPNG(File f)
	{
		if(ir == null) return false;

		BufferedImage image = ir.getCompressed();

		try
		{
			ImageIO.write(image, "png", f);
			return true;

		}
		catch(IOException E)
		{
			return false;
		}

	}

	/**
	 * Saves an animated GIF of the last compression call with animation enabled.
	 */
	public boolean saveAnimationAsGIF(File f)
	{
		if(animation == null) return false;

		try
		{
			FileImageOutputStream out = new FileImageOutputStream(f);
			GifSequenceWriter g = new GifSequenceWriter(out, ir.getImage().getType(), 100, false);
			for(BufferedImage frame : animation)
			{
				g.writeToSequence(frame);
			}
			g.close();
		}
		catch(IOException E)
		{
			return false;
		}
		return true;
	}

	/**
	 * Returns the base image that the controller is working with.
	 */
	public BufferedImage getBaseImage()
	{
		return ir != null ? ir.getImage() : null;
	}

	/**
	 * Returns the most recently compressed image.
	 */
	public BufferedImage getCompressedImage()
	{
		return ir != null ? ir.getCompressed() : null;
	}
}
