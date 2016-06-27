package cs2110.collision;
import java.util.ArrayList;

/** An instance is a non-empty collection of points organized in a hierarchical
 * binary tree structure. */
public class BlockTree {

	private BoundingBox box; // bounding box of the blocks contained in this tree.

	private int numBlocks; // Number of blocks contained in this tree.

	private BlockTree left; // left subtree --null if this is a leaf

	private BlockTree right; // right subtree --null iff this is a leaf

	private Block block; //The block of a leaf node (null if not a leaf)

	// REMARK:
	// Leaf node: left, right == null && block != null
	// Intermediate node: left, right != null && block == null

	/** Constructor: a binary tree containing blocks.
	 * Precondition: The tree has no be non-empty,
	 * i.e. it must contain at least one block.	 */
	public BlockTree(ArrayList<Block> blocks) { // Leave the following two "if"
		// statements as they are.
		if (blocks == null)
			throw new IllegalArgumentException("blocks null");
		if (blocks.size() == 0)
			throw new IllegalArgumentException("no blocks");

		numBlocks = blocks.size();
		box = BoundingBox.findBBox(blocks.iterator());
		
		if (blocks.size() == 1){//leaf base case
			block = blocks.get(0);
			return;
		}else{
			ArrayList<Block> leftArrayList = new ArrayList<Block>();
			ArrayList<Block> rightArrayList = new ArrayList<Block>();
			if (box.getHeight() > box.getWidth())	{
				for(Block b:blocks){
					if(b.position.y < box.getCenter().y){
						leftArrayList.add(b);
					}else{rightArrayList.add(b);}
				}
			}else{
				for(Block b:blocks){
					if(b.position.x < box.getCenter().x){
						leftArrayList.add(b);
					}else{rightArrayList.add(b);}
				}
			}
			left = new BlockTree(leftArrayList);
			right =  new BlockTree(rightArrayList);
		}
	}
	/** Return the bounding box of the collection of blocks.
	 * @return The bounding box of this collection of blocks.
	 */
	public BoundingBox getBox() {
		return box;
	}

	/** Return true iff this is a leaf node.
	 * @return true iff this is a leaf node.
	 */
	public boolean isLeaf() {
		return (block != null);
	}

	/** Return true iff this is an intermediate node.
	 * @return true iff this is an intermediate node.
	 */
	public boolean isIntermediate() {
		return !isLeaf();
	}

	/** Return the number of blocks contained in this tree.
	 * @return Number of blocks contained in this tree.
	 */
	public int getNumBlocks() {
		return numBlocks;
	}

	/** Return true iff this collection of blocks contains  point p.
	 * @return True iff this collection of blocks contains  point p.
	 */
	public boolean contains(Vector2D p) {
		if (isLeaf()){
			return box.contains(p);
		}else{
			return left.contains(p) || right.contains(p);
		}
	}

	/** Return true iff (this tree displaced by thisD) and (tree t 
	 * displaced by d) overlap.
	 * @param thisD
	 *            Displacement of this tree.
	 * @param t
	 *            A tree of blocks.
	 * @param d
	 *            Displacement of tree t.
	 * @return True iff this tree and tree t overlap (account for
	 *         displacements).
	 */
	public boolean overlaps(Vector2D thisD, BlockTree t, Vector2D d) {
		BoundingBox myBoxthis = box.displaced(thisD);
		BoundingBox myBoxt = t.box.displaced(d);
		if (!myBoxthis.overlaps(myBoxt)){
			return false;
		}else{
			if (t.isLeaf() && this.isLeaf()){
				return true;
			}
			if (this.isLeaf()){
				return overlaps(thisD, t.left,d) || overlaps(thisD, t.right,d);
			}
			if (t.isLeaf()){
				return t.overlaps(thisD, this.left,d) || t.overlaps(thisD, this.right,d);
			}else{
				if (box.getLength() > t.box.getLength()){
					return left.overlaps(thisD, t,d) || right.overlaps(thisD, t,d);
				}else{
					return this.overlaps(thisD, t.left,d) || this.overlaps(thisD, t.right,d);
				}
			}
		}
	}

	/** Return a representation of this instance. */
	public String toString() {
		return toString(new Vector2D(0, 0));
	}

	/** Return a represenation of d
	 * 
	 * @param d  Displacement vector.
	 * @return String representation of this tree (displaced by d).
	 */
	public String toString(Vector2D d) {
		return toStringAux(d, "");
	}

	/** Useful for creating appropriate indentation for function toString.  */
	private static final String indentation = "   ";

	/** Return a representation of this instance displaced by d, with
	 * indentation indent.
	 * @param d Displacement vector.
	 * @param indent  Indentation.
	 * @return String representation of this tree (displaced by d).
	 */
	private String toStringAux(Vector2D d, String indent) {
		String str = indent + "Box: ";
		str += "(" + (box.lower.x + d.x) + "," + (box.lower.y + d.y) + ")";
		str += " -- ";
		str += "(" + (box.upper.x + d.x) + "," + (box.upper.y + d.y) + ")";
		str += "\n";

		if (isLeaf()) {
			String vStr = "(" + (block.position.x + d.x) + "," + (block.position.y + d.y)
					+ ")" + block.halfwidth;
			str += indent + "Leaf: " + vStr + "\n";
		} else {
			String newIndent = indent + indentation;
			str += left.toStringAux(d, newIndent);
			str += right.toStringAux(d, newIndent);
		}

		return str;
	}

}
