import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.TreeSet;

public class ImageRegions extends DisjointSets {

	private BufferedImage image;
	private Region[] regions;


	public ImageRegions(BufferedImage image) {

		super(image.getHeight() * image.getWidth());
		this.image = image;
		this.regions = new Region[s.length];

		for (int y = 0; y < image.getHeight(); y++) {

			for (int x = 0; x < image.getWidth(); x++) {

				Pixel p = new Pixel(x, y);
				Color c = new Color(image.getRGB(x, y));
				regions[getID(p)] = new Region(p, c);

			}

		}

	}

	public ImageRegions(File file) throws IOException {
		this(ImageIO.read(file));
	}

	public int getID(Pixel p) {
		return p.y * image.getWidth() + p.x;
	}

	/**
	 * Union two disjoint sets using the height heuristic
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

	public Region get(int root) {
		assertIsRoot(root);
		return regions[root];
	}

	public TreeSet<Integer> getAdjacent(int root) {
		//8-neighbors of a given pixel

		TreeSet<Integer> result = new TreeSet<>();

		for (Pixel p : get(root)) {

			for (Pixel neighbor : getNeighbors(p)) {

				int neighborRoot = find(getID(neighbor));
				if (neighborRoot != -1 && neighborRoot != root) // Do not add the original root into the neighbor set
					result.add(neighborRoot);

			}
			
		}
		
		return result;
		
	}

	private ArrayList<Pixel> getNeighbors(Pixel pixel) {
		ArrayList<Pixel> neighbors = new ArrayList<Pixel>();

		for (int i = -1; i <= 1; i++) {

			int x = pixel.x + i;

			if (x < 0 || x == image.getWidth()) continue;

			for (int j = -1; j <= 1; j++) {

				int y = pixel.y + j;

				if (y < 0 || y == image.getHeight()) continue;
				if (i == 0 && j == 0) continue;

				neighbors.add(new Pixel(x, y));

			}//end for j

		}//end for i

		return neighbors;

	}

	public int maxSize() {
		return s.length;
	}

	public int getSize() {
		return this.size;
	}

	public Image getCompressed() {

		BufferedImage compressed = new BufferedImage(image.getWidth(), image.getHeight(), image.getType());

		for (Region r : regions) {
			Color c = r.getColor();
			for (Pixel p : r) {
				compressed.setRGB(p.x, p.y, c.getRGB());
			}
		}

		return compressed;

	}

	public ImageRegions reset() {
		return new ImageRegions(this.image);
	}

}
