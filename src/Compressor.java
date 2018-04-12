import java.awt.*;
import java.util.PriorityQueue;

public class Compressor {

	private ImageRegions ir;

	public Compressor(ImageRegions ir) {
		this.ir = ir;
	}

	public void compress(int K) {

		if (K < 1 || K > ir.maxSize()) {
			throw new IllegalArgumentException("oof");
		}

		if (K > ir.getSize()) {
			ir = ir.reset();
		}

		// Create PriorityQueue and add all adjacent pixels' similaritites to it
		PriorityQueue<Similarity> pQueue = new PriorityQueue<>();
		for (int i = 0; i < ir.getSize(); i++)
			for (int j : ir.getAdjacent(i))
				pQueue.add(new Similarity(ir.get(i), ir.get(j)));

		// Loop until we have the number of regions left that we want
		while (ir.getSize() > K) {

			// Get Similarity with the smallest distance and determine pixels and roots
			Similarity toUnion = pQueue.remove();
			int p1 = ir.getID(toUnion.r1.getRoot());
			int p2 = ir.getID(toUnion.r1.getRoot());
			int root1 = ir.find(p1);
			int root2 = ir.find(p2);

			// Ignore pairs of pixels from the same set
			if (root1 == root2) continue;

			// If pixels are not roots, union only if color difference is 0 - otherwise add roots back into PQ
			if (p1 != root1 || p2 != root2) {
				if (toUnion.distance > 0) {
					pQueue.add(new Similarity(ir.get(root1), ir.get(root2)));
				} else {
					ir.union(root1, root2);
				}

				// If pixels are both roots, union
			} else if (new Similarity(ir.get(root1), ir.get(root2)).compareTo(toUnion) == 0){
				int R = ir.union(root1, root2);

				// Add neighboring sets back into PQ only if distance is greater than 0 and doing so is necessary
				if (toUnion.distance > 0) {
					for (int n : ir.getAdjacent(R)) {
						pQueue.add(new Similarity(ir.get(R), ir.get(n)));
					}
				}

			}

		}

	}

	//this class represents the similarity between the colors of two adjacent regions
	private class Similarity implements Comparable<Similarity> {

		//a pair of adjacent regions
		Region r1;
		Region r2;

		//distance between the color of two pixels or two regions,
		//smaller distance indicates higher similarity
		int distance;

		Similarity(Region r1, Region r2) {
			this.r1 = r1;
			this.r2 = r2;
			this.distance = calcDistance();
		}

		private int getDifference(Color c1, Color c2)
		{
			int r = (c1.getRed()-c2.getRed());
			int g = (c1.getGreen()-c2.getGreen());
			int b = (c1.getBlue()-c2.getBlue());

			return r*r+g*g+b*b;
		}

		private int calcDistance() {

			Color avgColor = Region.avgColor(r1, r2);
			int d1 = getDifference(r1.getColor(), avgColor);
			int d2 = getDifference(r2.getColor(), avgColor);

			return d1 * r1.getSize() + d2 * r2.getSize();

		}

		public int compareTo(Similarity other) {
			int diff=this.distance - other.distance;
			if(diff!=0) return diff;
			diff=this.r1.getRoot().compareTo(other.r1.getRoot());
			if(diff!=0) return diff;
			return this.r2.getRoot().compareTo(other.r2.getRoot());
		}

	}

}
