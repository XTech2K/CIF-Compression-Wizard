package app;

public class Pixel implements Comparable<Pixel> {
    public final int x;
    public final int y;

    Pixel(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * compares this pixel to a supplied pixel
     *
     * @param other the pixel to be comared with
     * @return the sum of the absolute value of the difference between the x components and y components respectively.
     */
    public int compareTo(Pixel other) {
        return Math.abs(this.x - other.x) + Math.abs(this.y - other.y);
    }

}
