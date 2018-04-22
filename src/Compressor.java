import java.awt.*;
import java.util.*;

public class Compressor {

    private ImageRegions ir;
    private PriorityQueue<Similarity> pQueue;

    public Compressor(ImageRegions ir) {
        this.ir = ir;
        setupQueue();
    }

    /**
     * Reset this Compressor and its imageRegions to their uncompressed states
     */
    private void reset() {
        ir.reset();
        setupQueue();
    }

    /**
     * Sets up the PriorityQueue which will be used in compression
     */
    private void setupQueue() {
        // Create PriorityQueue and add all adjacent pixels' similaritites to it
        pQueue = new PriorityQueue<>();
        for (int i = 0; i < ir.getSize(); i++) {
            if (ir.isRoot(i)) {
                for (int j : ir.getAdjacent(i, true)) {
                    pQueue.add(new Similarity(ir.get(i), ir.get(j)));
                }
            }
        }
    }

    /**
     * Reduces the number of colored regions to the requested number
     *
     * @param K the number of regions to reduce down to
     * @throws IllegalArgumentException if K is less than one or greater than the number of maximum regions
     */
    public void compress(int K) {
        //checks for valid input size
        if (K < 1 || K > ir.getMaxSize()) {
            throw new IllegalArgumentException("oof");
        }
        //resets the image and recompresses if the supplied K value is greater than the current K but less than the maximum acceptable value.
        if (K > ir.getSize()) {
            reset();
        }

        // Loop until we have the number of regions left that we want
        while (ir.getSize() > K) {

            // Get Similarity with the smallest distance and determine pixels and roots
            Similarity toUnion = pQueue.remove();
            int p1 = ir.getID(toUnion.r1.getRoot());
            int p2 = ir.getID(toUnion.r2.getRoot());
            int root1 = ir.find(p1);
            int root2 = ir.find(p2);

            // Ignore copies of the same region
            if (root1 == root2) continue;

            // If original pixels were not roots, only union if regions had identical colors
            if (p1 != root1 || p2 != root2) {
                if (toUnion.distance > 0) {
                    pQueue.add(new Similarity(ir.get(root1), ir.get(root2)));
                } else {
                    ir.union(root1, root2);
                }
            } else if (new Similarity(ir.get(root1), ir.get(root2)).compareTo(toUnion) == 0) {
                // If pixels were both roots, union and add surroundings to the PriorityQueue
                int R = ir.union(root1, root2);
                // Do not add surroundings if both colors were the same, as it would have no effect
                if (toUnion.distance > 0) {
                    for (int n : ir.getAdjacent(R, false)) {
                        pQueue.add(new Similarity(ir.get(R), ir.get(n)));
                    }
                }

            }

        }

    }

    /**
     * Calculates a numerical weight for the difference between two colors
     *
     * @param c1 the color of the first region
     * @param c2 the color of the second region
     * @return the sum of the squares of the differences between red, green, and blue components of each color.
     */
    private static int compareColors(Color c1, Color c2) {
        int r = (c1.getRed() - c2.getRed());
        int g = (c1.getGreen() - c2.getGreen());
        int b = (c1.getBlue() - c2.getBlue());
        int a = (c1.getAlpha() - c2.getAlpha());

        return r * r + g * g + b * b + a * a;
    }

    //Rhis class represents the similarity between the colors of two adjacent regions
    private class Similarity implements Comparable<Similarity> {

        Region r1;
        Region r2;

        // Distance between the color of two regions,
        int distance;

        Similarity(Region r1, Region r2) {
            this.r1 = r1;
            this.r2 = r2;

            Color avgColor = Region.avgColor(r1, r2);
            int d1 = compareColors(r1.getColor(), avgColor);
            int d2 = compareColors(r2.getColor(), avgColor);

            this.distance = d1 * r1.getSize() + d2 * r2.getSize();
        }

        /**
         * Compares this region to second, provided region
         *
         * @param other the provided region
         * @return an integer representing the distance between the two regions.
         */
        public int compareTo(Similarity other) {
            int diff = this.distance - other.distance;
            if (diff != 0) return diff;
            diff = this.r1.getRoot().compareTo(other.r1.getRoot());
            if (diff != 0) return diff;
            return this.r2.getRoot().compareTo(other.r2.getRoot());
        }

    }

}
