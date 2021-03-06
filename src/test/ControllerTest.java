package test;

import app.Controller;

import java.io.File;

public class ControllerTest implements Testable
{
	Controller c;

	public ControllerTest()
	{
		c = new Controller();
	}

	public void runAll()
	{
		testSetImageFile();
		testCompressToPercent();
		testCompressToRegions();
	}

	/**
	 * Tests 3 cases - invalid file, png file, and jpg file.
	 */
	public void testSetImageFile()
	{
		//fail if setImageFile accepts bad file
		System.out.print("setImageFile test 1: ");
		String s = c.setImageFile(new File("res/text.txt")) ? "FAIL" : "pass";
		System.out.println(s);

		//fail if setImageFile doesn't accept png
		System.out.print("setImageFile test 2: ");
		s = c.setImageFile(new File("res/hedgehog.png")) ? "pass" : "FAIL";
		System.out.println(s);

		//fail if setImageFile doesn't accept jpg
		System.out.print("setImageFile test 3: ");
		s = c.setImageFile(new File("res/pikachu.jpg")) ? "pass" : "FAIL";
		System.out.println(s);
	}

	/**
	 * Tests 2 cases - png file, and jpg file.
	 */
	public void testCompressToPercent()
	{
		//setImageFile would fail on a bad file, so compressToPercent doesn't need to check for it

		//fail if compressToPercent doesn't compress png
		System.out.print("compressToPercent test 1: ");
		c.setImageFile(new File("res/hedgehog.png"));
		String s = c.compressToPercent(25, false) ? "pass" : "FAIL";
		System.out.println(s);

		//fail if compressToPercent doesn't compress jpg
		System.out.print("compressToPercent test 2: ");
		c.setImageFile(new File("res/pikachu.jpg"));
		s = c.compressToPercent(25, false) ? "pass" : "FAIL";
		System.out.println(s);

		//Bad numerical inputs cannot make it past the Menu since the slider only allows 0-99.
	}

	/**
	 * Tests 3 cases - invalid file, png file, and jpg file.
	 */
	public void testCompressToRegions()
	{
		//setImageFile would fail on a bad file, so compressToPercent doesn't need to check for it

		//fail if compressToRegions doesn't compress png
		System.out.print("compressToRegions test 1: ");
		c.setImageFile(new File("res/hedgehog.png"));
		String s = c.compressToRegions(100000, false) ? "pass" : "FAIL";
		System.out.println(s);

		//fail if compressToRegions doesn't compress jpg
		System.out.print("compressToRegions test 2: ");
		c.setImageFile(new File("res/pikachu.jpg"));
		s = c.compressToRegions(25000, false) ? "pass" : "FAIL";
		System.out.println(s);

		//Bad numerical inputs CAN make it to the Controller for compressToRegions, so test some boundaries

		//compressToRegions should turn input of 0 into input of 1 - pass on true, fail on false
		System.out.print("compressToRegions test 3: ");
		c.setImageFile(new File("res/hedgehog.png"));	//pikachu was taking too long on this test (over a minute)
		s = c.compressToRegions(0, false) ? "pass" : "FAIL";
		System.out.println(s);

		//compressToRegions should fail on negative numbers
		System.out.print("compressToRegions test 4: ");
		s = c.compressToRegions(-10, false) ? "FAIL" : "pass";
		System.out.println(s);

		//compressToRegions should fail on numbers greater than the number of regions in the base image
		//just use a very big number - the pikachu image is certain to have less than 500,000 regions of color
		System.out.print("compressToRegions test 5: ");
		s = c.compressToRegions(500000, false) ? "FAIL" : "pass";
		System.out.println(s);
	}

	//compress() does not need to be tested - it is tested by virtue of the methods that call it
}
