public class InsertionSort {
	private static void insertionSort(int[] list) {
		int i,j;
		for (j = 1; j < list.length; j++) {
			int key = list[j];
			i = j - 1;
			while (i >= 0 && list[i] > key) {
				list[i + 1] = list[i];
				i--;
			}
			list[i + 1] = key;
		}
	}

	private static void print(int[] list) {
		for (int num : list) {
			System.out.print(num + " ");
		}
		System.out.println();
	}

	public static void main(String[] args) {
		int[] list = new int[] {5, 2, 4, 6, 1, 3};
		System.out.println("Given List:");
		print(list);

		insertionSort(list);
		System.out.println("Sorted List:");
		print(list);
	}
}
