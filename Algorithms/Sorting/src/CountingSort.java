import java.util.HashMap;
import java.util.Map;

public class CountingSort {
	public static void countingSort(int[] listIn, int[] listOut, int valRange) {
		Map<Integer, Integer> counterMap = new HashMap<Integer, Integer>();
		for (int val = 0; val <= valRange; val++) {
			counterMap.put(val, 0);
		}

		for (int idx = 0; idx < listIn.length; idx++) {
			counterMap.put(listIn[idx], counterMap.get(listIn[idx]) + 1);
		}

		for (int val = 1; val <= valRange; val++) {
			counterMap.put(val, counterMap.get(val) + counterMap.get(val - 1));
		}

		for (int idx = listIn.length - 1; idx >= 0; idx--) {
			listOut[counterMap.get(listIn[idx]) - 1] = listIn[idx];
			counterMap.put(listIn[idx], counterMap.get(listIn[idx]) - 1);
		}
	}

	public static void print(int[] list) {
		for (int num : list) {
			System.out.print(" " + num);
		}
		System.out.println();
	}

	public static void main(String[] args) {
		int[] listIn = new int[] {2, 5, 3, 0, 2, 3, 0, 3};
		int[] listOut = new int[listIn.length];
		print(listIn);

		countingSort(listIn, listOut, 5);
		print(listOut);
	}
}
