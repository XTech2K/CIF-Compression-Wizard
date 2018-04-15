import java.awt.*;
import java.util.Iterator;
import java.util.LinkedList;

public class Region implements Iterable<Pixel> {
    private LinkedList<Pixel> pixels;
    private long redVal;
    private long greenVal;
    private long blueVal;
    private int size;


    Region(Pixel p, Color c) {
        this.pixels = new LinkedList<>();
        this.pixels.add(p);
        this.redVal = c.getRed();
        this.greenVal = c.getGreen();
        this.blueVal = c.getBlue();
        this.size = 1;
    }

    /**
     * creates a color from the supplied inputs
     * @param redVal the running total of red supplied
     * @param greenVal the running total of green supplied
     * @param blueVal the running total of blue supplied
     * @param size number of colors supplied
     * @return the resultant color after averaging each color with its size.
     */
    private static Color colorFromVals(long redVal, long greenVal, long blueVal, int size) {

        int r = (int) (redVal / size);
        int g = (int) (greenVal / size);
        int b = (int) (blueVal / size);

        return new Color(r, g, b);

    }
 
    /**
     * creates an average color from the supplied regions
     * @param r1 the first region to be averaged
     * @param r2 the second region to be averaged with
     * @return the resultant color after averaging the color of each region
     */
    public static Color avgColor(Region r1, Region r2) {

        long redVal = r1.redVal + r2.redVal;
        long greenVal = r1.greenVal + r2.greenVal;
        long blueVal = r1.blueVal + r2.blueVal;
        int size = r1.size + r2.size;

        return colorFromVals(redVal, greenVal, blueVal, size);

    }

    /**
     * unions this region with a second region
     * @param other the region to be union with
     */
    public void union(Region other) {

        this.pixels.addAll(other.pixels);

        this.redVal += other.redVal;
        this.greenVal += other.greenVal;
        this.blueVal += other.blueVal;

        this.size += other.size;

    }
 
    /**
     * calculates the color difference between two regions
     * @param other the region to be compared to
     * @return the resultant sum of absolute differences between the reg, green, and blue colors of each region
     */
    public long getDistance(Region other) {
        long r = this.redVal - other.redVal;
        long g = this.greenVal - other.greenVal;
        long b = this.blueVal - other.blueVal;

        return Math.abs(r) + Math.abs(g) + Math.abs(b);
    }

    /**
     * fetches the size of this region
     * @return the size of this region
     */
    public int getSize() {
        return this.size;
    }
 
    /**
     * calculates the color of this region
     * @return the color of this region
     */
    public Color getColor() {
        return colorFromVals(redVal, greenVal, blueVal, size);
    }
 
    /**
     * fetches the root of this region
     * @return the root of this region
     */
    public Pixel getRoot() {
        return this.pixels.get(0);
    }
 
    /**
     * creates an iterator of pixels
     * @return an iterator of pixels
     */
    public Iterator<Pixel> iterator() {
        return pixels.iterator();
    }

}
