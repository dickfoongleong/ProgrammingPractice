#include <stdio.h>

/* Print Celsius-Fahrenheit table
   for C = -100, -90, ..., 100 */

#define LOWER -100
#define UPPER 100
#define STEP 10

int main() {
	printf(" C\t   F\n");

	int f, c;
	
	c = LOWER;
	while (c <= UPPER) {
		f = (c * 9 / 5 ) + 32;
		printf("%4d\t%7d\n", c, f);
		c += STEP;
	}
	return 0;
}
