import static org.junit.Assert.*;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import org.junit.Test;

public class Controller_Test {

	@Test
	public void testCompressToPercentOverHundred() {
		Controller controller = new Controller();
		File file = new File("hedgehog.png");
		//test setImageFile(File) function with a valid file
		assertTrue(controller.setImageFile(file));
		
		// test compressToPercent with percent = 120 and animation = true
		assertFalse(controller.compressToPercent(120, true));
		
	
				
	}
	@Test
	public void testCompressToPercentBelowHundred() {
		Controller controller = new Controller();
		File file = new File("hedgehog.png");
		//test setImageFile(File) function with a valid file
		assertTrue(controller.setImageFile(file));
		
		// test compressToPercent with percent = 90 and animation = true 
		// also it gives error
		assertTrue(controller.compressToPercent(90, true));	
	}
	@Test
	public void testCompressToRegions() {
		Controller controller = new Controller();
		File file = new File("hedgehog.png");
		//test setImageFile(File) function with a valid file
		assertTrue(controller.setImageFile(file));
		// create image regions object with image file
		ImageRegions ir = null;
		try {
			ir = new ImageRegions(file);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// get K with percent = 50
		int percent = 50;
		int K = ir.getMaxSize() * (100 - percent) / 100;
		// test compressToRegions with percent = 50 and animation = true 
		// also it gives error
		assertTrue(controller.compressToRegions(K, true));	
	}
	
	@Test
	public void testSaveImage() {
		Controller controller = new Controller();
		File file = new File("hedgehog.png");
		//test setImageFile(File) function with a valid file
		assertTrue(controller.setImageFile(file));
		
	
		int percent = 129;
		// test compressToPercent with percent = 90 and animation = true 
		// also it gives error
		assertFalse(controller.compressToPercent(percent, true));
		
		// 
		assertTrue(controller.saveImageAsPNG(new File("output.png")));
	}
	@Test
	public void testSaveAnimation() {
		Controller controller = new Controller();
		File file = new File("hedgehog.png");
		//test setImageFile(File) function with a valid file
		assertTrue(controller.setImageFile(file));
		
	
		int percent = 129;
		// test compressToPercent with percent = 129 and animation = true 
		// also it gives error
		assertFalse(controller.compressToPercent(percent, true));
		
		// 
		assertTrue(controller.saveAnimationAsGIF(new File("outputAnimation.gif")));
	}
}
