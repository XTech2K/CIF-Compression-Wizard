package app;

import javafx.util.Pair;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.TreeSet;

public class ImageRegions extends DisjointSets {

    private static final int[][] FORWARD_ADJACENT = {{1, 0}, {1, 1}, {0, 1}, {-1, 1}};
    private static final int[][] ALL_ADJACENT = {{1, 0}, {1, 1}, {0, 1}, {-1, 1}, {1, -1}, {0, -1}, {-1, -1}, {-1, 0}};

    private final BufferedImage image;
    private Region[] regions;
    private int maxRegions;
    private int[] sArch;
    private Region[] regionsArch;


    public ImageRegions(BufferedImage image) {

        super(image.getHeight() * image.getWidth());
        this.image = image;
        this.regions = new Region[s.length];

        for (int y = 0; y < image.getHeight(); y++) {

            for (int x = 0; x < image.getWidth(); x++) {

                Pixel p = new Pixel(x, y);
                Color c = new Color(image.getRGB(x, y), true);
                regions[getID(p)] = new Region(p, c);

            }

        }

        ArrayList<Pair<Integer, Integer>> toUnion = new ArrayList<>();

        for (int i = 0; i < size; i++) {
            for (int j : getAdjacent(i, false)) {
                Region r1 = get(i);
                Region r2 = get(j);

                if (r1.getColor().equals(r2.getColor())) {
                    toUnion.add(new Pair<>(i, j));
                }
            }
        }

        for (Pair<Integer, Integer> pair : toUnion) {
            int a = find(pair.getKey());
            int b = find(pair.getValue());
            if (a != b) {
                union(a, b);
            }
        }

        maxRegions = size;
        sArch = Arrays.copyOf(s, s.length);
        regionsArch = Region.copy(regions);

    }

    public ImageRegions(File file) throws IOException {
        this(ImageIO.read(file));
    }

    /**
     * Resets this ImageRegions to its original number of regions
     */
    public void reset() {

        size = maxRegions;
        s = Arrays.copyOf(sArch, s.length);
        regions = Region.copy(regionsArch);

    }

    /**
     * Grabs the ordinal number of the supplied pixel. Ex: pixel A is the 5th pixel.
     *
     * @param p the pixel to get the order of
     * @return the ordinal number of the pixel
     */
    public int getID(Pixel p) {
        return p.y * image.getWidth() + p.x;
    }

    /**
     * Union two disjoint sets using the union by rank heuristic covered in CS 310 from the textbook.
     *
     * @param root1 the root of set 1
     * @param root2 the root of set 2
     * @return the root of the resulting set
     * @throws IllegalArgumentException if root1 or root2 are not distinct
     */
    @Override
    public int union(int root1, int root2) {
        int finalRoot = super.union(root1, root2);
        int subRoot = finalRoot == root1 ? root2 : root1;

        regions[finalRoot].union(regions[subRoot]);

        return finalRoot;

    }

    /**
     * Finds the region in which a provided root index lies
     *
     * @param root the provided integer array index to a root
     * @return the region that the root makes up
     */
    public Region get(int root) {
        root = find(root);
        return regions[root];
    }

    /**
     * Returns the regions adjacent to the provided root of a region
     *
     * @param root        an index to look from
     * @param onlyForward True if we only want adjacent regions further ahead
     * @return a treeset of neighboring sets listed by their root
     */
    public TreeSet<Integer> getAdjacent(int root, boolean onlyForward) {

        TreeSet<Integer> result = new TreeSet<>();

        int[][] offsets = onlyForward ? FORWARD_ADJACENT : ALL_ADJACENT;

        for (Pixel p : get(root)) {

            for (Pixel neighbor : getNearbyPixels(p, offsets)) {

                int neighborRoot = find(getID(neighbor));
                if (neighborRoot != -1 && neighborRoot != root) // Do not add the original root into the neighbor set
                    result.add(neighborRoot);

            }

        }

        return result;

    }

    /**
     * Calculates nearby neighbors of a given pixel
     *
     * @param pixel   the pixel to find neighbors of
     * @param offsets a list of pairs of x and y offsets for the desired pixels
     * @return an ArrayList of pixels that hold its neighboring pixels
     */
    private ArrayList<Pixel> getNearbyPixels(Pixel pixel, int[][] offsets) {
        ArrayList<Pixel> nearby = new ArrayList<Pixel>();

        for (int[] offset : offsets) {

            int x = pixel.x + offset[0];
            if (x < 0 || x == image.getWidth()) continue;

            int y = pixel.y + offset[1];
            if (y < 0 || y == image.getHeight()) continue;

            nearby.add(new Pixel(x, y));

        }

        return nearby;
    }

    /**
     * Determines if a given root is actually a root
     *
     * @param r the root to check
     * @return whether or not int r is a root
     */
    public boolean isRoot(int r) {
        return s[r] < 0;
    }

    /**
     * Fetches the size of the array s
     *
     * @return the size of the array s
     */
    public int getMaxSize() {
        return maxRegions;
    }

    /**
     * Fetches size of this image region
     *
     * @return the size of this image region
     */
    public int getSize() {
        return this.size;
    }

    /**
     * Fetches the original uncompressed image
     *
     * @return the uncompressed image
     */
    public BufferedImage getImage() {
        return this.image;
    }

    /**
     * Creates a buffered image and sets the value of each pixel in the image to the appropriate new value
     *
     * @return the compressed image
     */
    public BufferedImage getCompressed() {

        BufferedImage compressed = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_ARGB);

        for (int i = 0; i < s.length; i++) {
            if (s[i] < 0) {
                Region r = regions[i];
                Color c = r.getColor();
                for (Pixel p : r) {
                    compressed.setRGB(p.x, p.y, c.getRGB());
                }
            }
        }

        return compressed;

    }

}
