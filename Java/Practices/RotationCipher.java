
public class RotationCipher {
	public static String rotationCipher(String str, int rotation) {
		StringBuilder builder = new StringBuilder("");
		for (char c : str.toCharArray()) {
			if (c >= '0' && c <= '9') {
				c = rotate(c, '0', rotation, 10);
			} else if (c >= 'a' && c <= 'z') {
				c = rotate(c, 'a', rotation, 26);
			} else if (c >= 'A' && c <= 'Z') {
				c = rotate(c, 'A', rotation, 26);
			}

			builder.append(c);
		}

		return builder.toString();
	}

	public static char rotate(char c, char base, int rotation, int mod) {
		int num = c - base;
		num += rotation;
		num %= mod;
		return (char) (num + base);
	}

	public static void main(String args[]) {
		String res = rotationCipher("abcdZXYzxy-999.@", 200);
		System.out.println(res);
	}
}
