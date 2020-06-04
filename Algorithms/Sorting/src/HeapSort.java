public class HeapSort {
	public static void heapSort(int[] list) {
		buildMaxHeap(list);
		int size = list.length;
		for (int idx = list.length - 1; idx > 0; idx--) {
			exchangeVal(list, 0, idx);
			size--;
			maxHeapify(list, size, 0);
		}
	}

	public static void buildMaxHeap(int[] list) {
		for (int idx = (list.length - 1) / 2; idx >= 0; idx--) {
			maxHeapify(list, list.length, idx);
		}
	}

	public static void maxHeapify(int[] list, int size, int idx) {
		int largestIdx = idx;
		int lIdx = 2 * idx + 1;
		int rIdx = 2 * idx + 2;

		if (lIdx < size && list[lIdx] > list[idx]) {
			largestIdx = lIdx;
		}

		if (rIdx < size && list[rIdx] > list[largestIdx]) {
			largestIdx = rIdx;
		}

		if (largestIdx != idx) {
			exchangeVal(list, idx, largestIdx);
			maxHeapify(list, size, largestIdx);
		}
	}

	public static void exchangeVal(int[] list, int idx1, int idx2) {
		int temp = list[idx1];
		list[idx1] = list[idx2];
		list[idx2] = temp;
	}

	public static void print(int[] list) {
		for (int num : list) {
			System.out.print(num + " ");
		}
		System.out.println();
	}

	public static void main(String[] args) {
		int[] list = new int[] {4, 1, 3, 2, 16, 9, 10, 14, 8, 7};
		print(list);

		heapSort(list);
		print(list);
	}
}
