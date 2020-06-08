public class TreeNode {
	private int value;
	private TreeNode parent;
	private TreeNode leftLeaf;
	private TreeNode rightLeaf;

	public TreeNode(int value) {
		this.value = value;
		this.parent = null;
		this.leftLeaf = null;
		this.rightLeaf = null;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

	public TreeNode getParent() {
		return parent;
	}

	public void setParent(TreeNode parent) {
		this.parent = parent;
	}

	public TreeNode getLeftLeaf() {
		return leftLeaf;
	}

	public void setLeftLeaf(TreeNode leftLeaf) {
		this.leftLeaf = leftLeaf;
	}

	public TreeNode getRightLeaf() {
		return rightLeaf;
	}

	public void setRightLeaf(TreeNode rightLeaf) {
		this.rightLeaf = rightLeaf;
	}
}
