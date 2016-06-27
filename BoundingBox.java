package cs2110.collision;
import java.util.Iterator;

/** An instance is a 2D bounding box. */
public class BoundingBox {
	/** The corner of the bounding box with the smaller x,y coordinates. */
	public Vector2D lower; // (minX,minY)

	/** The corner of the bounding box with the larger x,y coordinates.	 */
	public Vector2D upper; // (maxX,maxY)

	/** Constructor: an instance is a copy of bounding box b.*/
	public BoundingBox(BoundingBox b) {
		lower = new Vector2D(b.lower);
		upper = new Vector2D(b.upper);
	}

	/** Constructor: An instance with lower as smaller coordinates and
	 * upper as larger coordinates.
	 * @param lower  Corner with smaller coordinates.
	 * @param upper  Corner with larger coordinates.
	 */
	public BoundingBox(Vector2D lower, Vector2D upper) {
		if (upper.x < lower.x)
			throw new IllegalArgumentException("invalid bbox");
		if (upper.y < lower.y)
			throw new IllegalArgumentException("invalid bbox");

		this.lower = lower;
		this.upper = upper;
	}

	/** Return the width of this bounding box (along x-dimension).
	 * @return Width of this bounding box.
	 */
	public double getWidth() {
		return upper.x - lower.x;
	}

	/** Return the height of this bounding box (along y-dimension).
	 * @return Height of this bounding box.
	 */
	public double getHeight() {
		return upper.y - lower.y;
	}

	/** Return the larger of the width and height of this bounding box.
	 * @return Returns the dimension (width or height) of maximum length.
	 */
	public double getLength() {
		double lengthX = upper.x - lower.x;
		double lengthY = upper.y - lower.y;
		if (lengthX > lengthY){
			return lengthX;
		}else{return lengthY;}
	}

	/** Return the center of this bounding box.
	 * @return The center of this bounding box.
	 */
	public Vector2D getCenter() {
		double lengthX = (upper.x +lower.x)/2;
		double lengthY = (upper.y + lower.y)/2;
		Vector2D myCenter = new Vector2D(lengthX,lengthY);
		return myCenter;
	}

	/** Return the result of displacing this bounding box by d.
	 * @param d
	 *            A displacement vector.
	 * @return The result of displacing this bounding box by vector d.
	 */
	public BoundingBox displaced(Vector2D d) {
		BoundingBox myBoundingBox = new BoundingBox(lower.add(d),upper.add(d));
		return myBoundingBox;
	}

	/** Return true iff this bounding box contains p.
	 * @param p A point.
	 * @return True iff this bounding box contains point p.
	 */
	public boolean contains(Vector2D p) {
		boolean inX = lower.x <= p.x && p.x <= upper.x;
		boolean inY = lower.y <= p.y && p.y <= upper.y;
		return inX && inY;
	}

	/** Return the area of this bounding box.
	 * @return The area of this bounding box.
	 */
	public double getArea() {
		return getWidth()*getHeight();
	}

	/** Return true iff this bounding box overlaps with box.
	 * @param box  A bounding box.
	 * @return True iff this bounding box overlaps with box.
	 */
	public boolean overlaps(BoundingBox box) {
		if(box.upper.x < lower.x){
			return false;
		}if(box.upper.y < lower.y){
			return false;
		}if(box.lower.x > upper.x){
			return false;
		}if(box.lower.y > upper.y){
			return false;
		}
		return true;
	}

	/** Return the bounding box of blocks given by iter.
	 * @param iter   An iterator of blocks.
	 * @return The bounding box of the blocks given by the iterator.
	 */
	public static BoundingBox findBBox(Iterator<Block> iter) {
		// Do not modify the following "if" statement.
		if (!iter.hasNext())
			throw new IllegalArgumentException("empty iterator");

		double myLowerX = Double.POSITIVE_INFINITY;
		double myLowerY = Double.POSITIVE_INFINITY;
		double myUpperX = Double.NEGATIVE_INFINITY;
		double myUpperY = Double.NEGATIVE_INFINITY;
		while (iter.hasNext()){
			Block myBlock = iter.next();
			if(myLowerX > myBlock.position.x - myBlock.halfwidth){
				myLowerX = myBlock.position.x - myBlock.halfwidth;
			}if(myLowerY > myBlock.position.y - myBlock.halfwidth){
				myLowerY = myBlock.position.y - myBlock.halfwidth;
			}if(myUpperX < myBlock.position.x + myBlock.halfwidth){
				myUpperX = myBlock.position.x + myBlock.halfwidth;
			}if(myUpperY < myBlock.position.y + myBlock.halfwidth){
				myUpperY = myBlock.position.y + myBlock.halfwidth;
			}
		}
		Vector2D myBoundingBoxLower = new Vector2D(myLowerX,myLowerY);
		Vector2D myBoundingBoxUpper = new Vector2D(myUpperX,myUpperY);
		BoundingBox myBoundingBox = new BoundingBox(myBoundingBoxLower,myBoundingBoxUpper);
		return myBoundingBox;
	}
	/** Return a representation of this bounding box. */
	public String toString() {
		return lower + " -- " + upper;
	}
}
