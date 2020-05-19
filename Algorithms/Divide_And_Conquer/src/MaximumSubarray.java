public class MaximumSubarray {
	private static int[] findMaxSubarray(int[] list, int idxStr, int idxEnd) {
		if (idxStr == idxEnd) {
			return new int[] {idxStr, idxEnd, list[idxStr]};
		} else {
			int idxMid = (idxStr + idxEnd) / 2;

			int[] leftMaxSub = findMaxSubarray(list, idxStr, idxMid);
			int leftSum = leftMaxSub[2];

			int[] rightMaxSub = findMaxSubarray(list, idxMid + 1, idxEnd);
			int rightSum = rightMaxSub[2];

			int[] crossMaxSub = findMaxCrossSubarray(list, idxStr, idxMid, idxEnd);
			int crossSum = crossMaxSub[2];

			if (leftSum >= rightSum && leftSum >= crossSum) {
				return leftMaxSub;
			} else if (rightSum >= leftSum && rightSum >= crossSum) {
				return rightMaxSub;
			} else {
				return crossMaxSub;
			}
		}
	}

	private static int[] findMaxCrossSubarray(int[] list, int idxStr, int idxMid, int idxEnd) {
		int leftIdx, rightIdx, maxSum;
		leftIdx = rightIdx = -1;

		int leftSum = Integer.MIN_VALUE;
		int sum = 0;
		for (int i = idxMid; i >= idxStr; i--) {
			sum += list[i];
			if (sum > leftSum) {
				leftSum = sum;
				leftIdx = i;
			}
		}

		int rightSum = Integer.MIN_VALUE;
		sum = 0;
		for (int i = idxMid + 1; i <= idxEnd; i++) {
			sum += list[i];
			if (sum > rightSum) {
				rightSum = sum;
				rightIdx = i;
			}
		}
		
		maxSum = leftSum + rightSum;

		return new int[] {leftIdx, rightIdx, maxSum};
	}

	private static void print(int[] list) {
		for (int num : list) {
			System.out.print(num + " ");
		}
		System.out.println();
	}

	public static void main(String[] args) {
		int[] list = new int[] {13, -3, -25, 20, -3, -16, -23, 18, 20, -7, 12, -5, -22, 15, -4, 7};
		System.out.println("Given List");
		print(list);

		int[] maxSub = findMaxSubarray(list, 0, list.length - 1);
		System.out.println("Maximum Subarray: BegindIdx EndIdx Sum");
		print(maxSub);
	}
}
