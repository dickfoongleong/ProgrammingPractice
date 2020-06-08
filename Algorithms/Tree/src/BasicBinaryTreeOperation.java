public class BasicBinaryTreeOperation {
	public static void printTree(TreeNode root) {
		if (root != null) {
			printTree(root.getLeftLeaf());
			System.out.println(root.getValue());
			printTree(root.getRightLeaf());
		}
	}

	public static TreeNode treeSearch(TreeNode root, int key) {
		while (root != null && key != root.getValue()) {
			if (key < root.getValue()) {
				root = root.getLeftLeaf();
			} else {
				root = root.getRightLeaf();
			}
		}
		return root;
	}

	public static TreeNode treeMin(TreeNode root) {
		if (root == null) {
			return root;
		}

		while (root.getLeftLeaf() != null) {
			root = root.getLeftLeaf();
		}
		return root;
	}

	public static TreeNode treeMax(TreeNode root) {
		if (root == null) {
			return root;
		}

		while (root.getRightLeaf() != null) {
			root = root.getRightLeaf();
		}
		return root;
	}

	public static TreeNode treeSuccessor(TreeNode node) {
		if (node.getRightLeaf() != null) {
			return treeMin(node.getRightLeaf());
		}

		TreeNode resultNode = node.getParent();
		while (resultNode != null && node.getValue() == resultNode.getRightLeaf().getValue()) {
			node = resultNode;
			resultNode = resultNode.getParent();
		}
		return resultNode;
	}

	public static TreeNode treeInsert(TreeNode tree, TreeNode leaf) {
		TreeNode trailingPointer = null;
		TreeNode root = tree;

		while (root != null) {
			trailingPointer = root;
			if (leaf.getValue() < root.getValue()) {
				root = root.getLeftLeaf();
			} else {
				root = root.getRightLeaf();
			}
		}
		leaf.setParent(trailingPointer);
		if (trailingPointer == null) {
			tree = leaf;
		} else if (leaf.getValue() < trailingPointer.getValue()) {
			trailingPointer.setLeftLeaf(leaf);
		} else {
			trailingPointer.setRightLeaf(leaf);
		}

		return tree;
	}

	public static void printNode(TreeNode node) {
		if (node == null) {
			System.out.println("Node Is NULL");
		} else {
			System.out.println(node.getValue());
		}
	}

	public static void main(String[] args) {
		TreeNode tree = null;
		int[] values = new int[] {15, 6, 3, 2, 4, 7, 13, 9, 18, 17, 20};

		for (int val : values) {
			tree = treeInsert(tree, new TreeNode(val));
		}
		printTree(tree);

		TreeNode result = treeSearch(tree, 7);
		System.out.print("Searched for value 7 and found: ");
		printNode(result);
		result = treeSearch(tree, 17);
		System.out.print("Searched for value 17 and found: ");
		printNode(result);
		result = treeSearch(tree, 31);
		System.out.print("Searched for value 31 and found: ");
		printNode(result);

		result = treeMin(tree);
		System.out.print("Searched for Min and found: ");
		printNode(result);
		result = treeMax(tree);
		System.out.print("Searched for Max and found: ");
		printNode(result);

		result = treeSearch(tree, 13);
		result = treeSuccessor(result);
		System.out.print("Searched for Successor of 13 and found: ");
		printNode(result);
		result = treeSearch(tree, 15);
		result = treeSuccessor(result);
		System.out.print("Searched for Successor of 15 and found: ");
		printNode(result);
		result = treeMax(tree);
		result = treeSuccessor(result);
		System.out.print("Searched for Successor of Max and found: ");
		printNode(result);
	}
}
