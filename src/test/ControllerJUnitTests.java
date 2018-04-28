package test;

import java.io.File;
import app.Controller;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ControllerJUnitTests
{

	@Test
	public void testCompressToPercentOverHundred() {
		Controller controller = new Controller();
		File file = new File("res/hedgehog.png");
		//test setImageFile(File) function with a valid file
		assertTrue(controller.setImageFile(file));
		
		// test compressToPercent with percent = 120 and animation = true
		assertFalse(controller.compressToPercent(120, true));
	}

	@Test
	public void testCompressToPercentBelowHundred() {
		Controller controller = new Controller();
		File file = new File("res/hedgehog.png");
		//test setImageFile(File) function with a valid file
		assertTrue(controller.setImageFile(file));
		
		// test compressToPercent with percent = 90 and animation = true 
		// also it gives error
		assertTrue(controller.compressToPercent(90, true));	
	}

	@Test
	public void testCompressToRegions() {
		Controller controller = new Controller();
		File file = new File("res/hedgehog.png");
		//test setImageFile(File) function with a valid file
		assertTrue(controller.setImageFile(file));
		// create image regions object with image file
		assertTrue(controller.compressToRegions(100, true));
	}
	
	@Test
	public void testSaveImage() {
		Controller controller = new Controller();
		File file = new File("res/hedgehog.png");
		//test setImageFile(File) function with a valid file
		assertTrue(controller.setImageFile(file));
		
	
		int percent = 129;
		// test compressToPercent with percent = 90 and animation = true 
		// also it gives error
		assertFalse(controller.compressToPercent(percent, true));
		
		// test saving compressed image
		assertTrue(controller.saveImageAsPNG(new File("res/output.png")));
	}

	@Test
	public void testSaveAnimation() {
		Controller controller = new Controller();
		File file = new File("res/hedgehog.png");
		//test setImageFile(File) function with a valid file
		assertTrue(controller.setImageFile(file));
		
	
		int percent = 129;
		// test compressToPercent with percent = 129 and animation = true 
		// also it gives error
		assertFalse(controller.compressToPercent(percent, true));
		
		// test saving compression animation
		assertTrue(controller.saveAnimationAsGIF(new File("res/outputAnimation.gif")));
	}
}
