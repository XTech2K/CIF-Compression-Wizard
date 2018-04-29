package test;

import app.*;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ImageRegionsTest implements Testable {

    public void runAll()
    {
        testTXTFile();
        testPNGFile();
        testJPGFile();
    }

    /**
     * Tests 1 case - not accepting invalid (txt in this case) files
     */
    public void testTXTFile()
    {
        File f;
        String s;
        ImageRegions ir;

        //test that ImageRegions cannot be created from txt file
        System.out.print("TXT test 1: ");
        f = new File("res/text.txt");
        s = "FAIL";
        try {
            ir = new ImageRegions(f);
        } catch (Exception E) {
            s = "pass";
        }
        System.out.println(s);

        // Since TXT files should fail, there is no need to test the maximum size for them
    }

    /**
     * Tests 5 cases - Accepting PNG files, automatically doing lossless compression, correct max regions,
     * Proper base image storage, and proper compressed image output.
     */
    public void testPNGFile()
    {
        String s;
        File f;
        BufferedImage i;
        ImageRegions ir;

        //test that ImageRegions can be created from png file
        System.out.print("PNG test 1: ");
        f = new File("res/hedgehog.png");
        s = "pass";
        try {
            i = ImageIO.read(f);
            ir = new ImageRegions(f);
        } catch (Exception E) {
            s = "FAIL - cannot complete tests";
            return;
        }
        System.out.println(s);

        //fail if regions not automatically compressed losslessly for png file
        System.out.print("PNG test 2: ");
        s = ir.getMaxSize() > 0 && ir.getMaxSize() < ir.s.length ? "pass" : "FAIL";
        System.out.println(s);

        //fail if incorrect max size for png file
        System.out.print("PNG test 3: ");
        int sum = 0;
        for (int root : ir.s) {
            sum += root < 0 ? 1 : 0;
        }
        s = ir.getMaxSize() == sum ? "pass" : "FAIL";
        System.out.println(s);

        //fail if output base image not the same as was input
        System.out.print("PNG test 4: ");
        s = compareImages(i, ir.getImage()) ? "pass" : "FAIL";
        System.out.println(s);

        //fail if output compressed image not the same as was input
        System.out.print("PNG test 5: ");
        s = compareImages(i, ir.getCompressed()) ? "pass" : "FAIL";
        System.out.println(s);

    }

    /**
     * Tests 3 cases - Accepting JPG files, automatically doing lossless compression, and correct max regions.
     */
    public void testJPGFile() {
        String s;
        File f;
        ImageRegions ir;

        //test that ImageRegions can be created from jpg file
        System.out.print("JPG test 1: ");
        f = new File("res/hedgehog.png");
        s = "pass";
        try {
            ir = new ImageRegions(f);
        } catch (Exception E) {
            s = "FAIL - cannot complete tests";
            return;
        }
        System.out.println(s);

        //fail if regions not automatically compressed losslessly for jpg file
        System.out.print("JPG test 2: ");
        s = ir.getMaxSize() > 0 && ir.getMaxSize() < ir.s.length ? "pass" : "FAIL";
        System.out.println(s);

        //fail if incorrect max size for jpg file
        System.out.print("JPG test 3: ");
        int sum = 0;
        for (int root : ir.s) {
            sum += root < 0 ? 1 : 0;
        }
        s = ir.getMaxSize() == sum ? "pass" : "FAIL";
        System.out.println(s);

        //cannot test output images with jpg files because we output png files
    }

    private static boolean compareImages(BufferedImage imgA, BufferedImage imgB) {
        // The images must be the same size.
        if (imgA.getWidth() != imgB.getWidth() || imgA.getHeight() != imgB.getHeight()) {
            return false;
        }

        int width  = imgA.getWidth();
        int height = imgA.getHeight();

        // Loop over every pixel.
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                // Compare the pixels for equality.
                if (imgA.getRGB(x, y) != imgB.getRGB(x, y)) {
                    return false;
                }
            }
        }

        return true;
    }

}
