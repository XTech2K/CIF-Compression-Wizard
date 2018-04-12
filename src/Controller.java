import java.awt.*;
import java.io.File;

public class Controller
{
	private Image baseImage;
	private ImageRegions compressedImage;

	public Controller()
	{
	}

	/**
	 * Takes a File from the Menu that the user selected, reads it in with ImageIO.read() into a BufferedImage,
	 * and sets the Controller's baseImage to that image.
	 */
	public boolean setImageFile(File f)
	{
		return false;
	}

	/**
	 * Returns the base image that the controller is working with.
	 */
	public Image getBaseImage()
	{
		return null;
	}

	/**
	 * Calls the Compressor on the base image, given the options from the Menu.
	 * Need to think about what failure conditions might be.
	 */
	public boolean compressImage(int percent, boolean animate)
	{
		return false;
	}

	/**
	 * Saves the compressed image as a PNG to the disk at the location specified by the user.
	 */
	public boolean saveImageAsPNG(File f)
	{
		return false;
	}

	/**
	 * Saves the compressed image as a CIF to the disk at the location specified by the user.
	 */
	public boolean saveImageAsCIF(File f)
	{
		return false;
	}
}
