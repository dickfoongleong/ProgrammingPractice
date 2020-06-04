public class QuickSort {
	public static void quickSort(int[] list, int strIdx, int endIdx) {
		if (strIdx < endIdx) {
			int midIdx = partition(list, strIdx, endIdx);
			quickSort(list, strIdx, midIdx - 1);
			quickSort(list, midIdx + 1, endIdx);
		}
	}

	public static int partition(int[] list, int strIdx, int endIdx) {
		int lastVal = list[endIdx];
		int prvIdx = strIdx - 1;

		for (int curIdx = strIdx; curIdx < endIdx; curIdx++) {
			if (list[curIdx] <= lastVal) {
				prvIdx++;
				exchangeVal(list, prvIdx, curIdx);
			}
		}
		exchangeVal(list, prvIdx + 1, endIdx);
		return prvIdx + 1;
	}

	public static void exchangeVal(int[] list, int idx1, int idx2) {
		int temp = list[idx1];
		list[idx1] = list[idx2];
		list[idx2] = temp;
	}

	public static void print(int[] list) {
		for (int num : list) {
			System.out.print(" " + num);
		}
		System.out.println();
	}

	public static void main(String[] args) {
		int[] list = new int[] {2, 8, 7, 1, 3, 5, 6, 4};
		print(list);

		quickSort(list, 0, list.length - 1);
		print(list);
	}
}
