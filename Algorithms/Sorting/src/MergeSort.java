import java.lang.Math;

public class MergeSort {
	private static void merge(int[] list, int idxStr, int idxMid, int idxEnd) {
		int sizeL = idxMid - idxStr + 1;
		int sizeR = idxEnd - idxMid;

		int[] leftList = new int[sizeL];
		int[] rightList = new int[sizeR];

		for (int i = 0; i < sizeL; i++) {
			leftList[i] = list[idxStr + i];
		}

		for (int i = 0; i < sizeR; i++) {
			rightList[i] = list[idxMid + i + 1];
		}
		int i = 0;
		int j = 0;
		int k = idxStr;
		while (i < sizeL || j < sizeR) {
			if (i == sizeL) {
				list[k] = rightList[j];
				j++;
			} else if (j == sizeR) {
				list[k] = leftList[i];
				i++;
			} else if (leftList[i] <= rightList[j]) {
				list[k] = leftList[i];
				i++;
			} else {
				list[k] = rightList[j];
				j++;
			}

			k++;
		}
	}

	private static void mergeSort(int[] list, int idxStr, int idxEnd) {
		if (idxStr < idxEnd) {
			int idxMid = (idxStr + idxEnd) / 2;
			mergeSort(list, idxStr, idxMid);
			mergeSort(list, idxMid + 1, idxEnd);
			merge(list, idxStr, idxMid, idxEnd);
		}
	}

	private static void print(int[] list) {
		for (int num : list) {
			System.out.print(num + " ");
		}
		System.out.println();
	}

	public static void main(String[] args) {
		int[] list = new int[] {5, 2, 4, 7, 1, 3, 2, 6};
		
		System.out.println("Given List:");
		print(list);

		mergeSort(list, 0, list.length - 1);
		System.out.println("Sorted List:");
		print(list);
	}
}
