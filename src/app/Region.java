package app;

import java.awt.*;
import java.util.Iterator;
import java.util.LinkedList;

public class Region implements Iterable<Pixel> {
    private LinkedList<Pixel> pixels;
    private long redVal;
    private long greenVal;
    private long blueVal;
    private long alphaVal;
    private int size;


    Region(Pixel p, Color c) {
        this.pixels = new LinkedList<>();
        this.pixels.add(p);
        this.redVal = c.getRed();
        this.greenVal = c.getGreen();
        this.blueVal = c.getBlue();
        this.alphaVal = c.getAlpha();
        this.size = 1;
    }

    Region(Region other) {
        this.pixels = new LinkedList<>();
        for (Pixel p : other.pixels) {
            this.pixels.add(p);
        }
        this.redVal = other.redVal;
        this.greenVal = other.greenVal;
        this.blueVal = other.blueVal;
        this.alphaVal = other.alphaVal;
        this.size = other.size;
    }

    public static Region[] copy(Region[] old) {
        Region[] res = new Region[old.length];
        for (int i = 0; i < old.length; i++) {
            res[i] = new Region(old[i]);
        }
        return res;
    }

    /**
     * creates a color from the supplied inputs
     *
     * @param redVal   the running total of red supplied
     * @param greenVal the running total of green supplied
     * @param blueVal  the running total of blue supplied
     * @param alphaVal the running total of alpha supplied
     * @param size     number of colors supplied
     * @return the resultant color after averaging each color with its size.
     */
    private static Color colorFromVals(long redVal, long greenVal, long blueVal, long alphaVal, int size) {

        int r = (int) (redVal / size);
        int g = (int) (greenVal / size);
        int b = (int) (blueVal / size);
        int a = (int) (alphaVal / size);

        return new Color(r, g, b, a);

    }

    /**
     * creates an average color from the supplied regions
     *
     * @param r1 the first region to be averaged
     * @param r2 the second region to be averaged with
     * @return the resultant color after averaging the color of each region
     */
    public static Color avgColor(Region r1, Region r2) {

        long redVal = r1.redVal + r2.redVal;
        long greenVal = r1.greenVal + r2.greenVal;
        long blueVal = r1.blueVal + r2.blueVal;
        long alphaVal = r1.alphaVal + r2.alphaVal;
        int size = r1.size + r2.size;

        return colorFromVals(redVal, greenVal, blueVal, alphaVal, size);

    }

    /**
     * unions this region with a second region
     *
     * @param other the region to be union with
     */
    public void union(Region other) {

        this.pixels.addAll(other.pixels);

        this.redVal += other.redVal;
        this.greenVal += other.greenVal;
        this.blueVal += other.blueVal;
        this.alphaVal += other.alphaVal;

        this.size += other.size;

    }

    /**
     * fetches the size of this region
     *
     * @return the size of this region
     */
    public int getSize() {
        return this.size;
    }

    /**
     * calculates the color of this region
     *
     * @return the color of this region
     */
    public Color getColor() {
        return colorFromVals(redVal, greenVal, blueVal, alphaVal, size);
    }

    /**
     * fetches the root of this region
     *
     * @return the root of this region
     */
    public Pixel getRoot() {
        return this.pixels.get(0);
    }

    /**
     * creates an iterator of pixels
     *
     * @return an iterator of pixels
     */
    public Iterator<Pixel> iterator() {
        return pixels.iterator();
    }

}
