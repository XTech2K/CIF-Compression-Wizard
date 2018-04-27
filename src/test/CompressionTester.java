package test;

import app.Controller;

import java.io.File;
import java.io.IOException;

public class CompressionTester {

    public static void main(String[] args) throws IOException {

        File inFile = new File(args[0]);
        File outFile = new File(args[1]);
        File out2 = new File(args[3]);

        Controller c = new Controller();
        System.out.println(c.setImageFile(inFile));

        System.out.println(c.compressToRegions(Integer.parseInt(args[2]), true));

        System.out.println(c.saveImageAsPNG(outFile));

        System.out.println(c.saveAnimationAsGIF(out2));

    }

}
