#include <stdio.h>

/* Print Fahrenheit-Celsius table
   for F = 0, 10, ..., 300 */

#define LOWER 0
#define UPPER 300
#define STEP 10

int main() {
	printf(" F\t   C\n");

	float f, c;
	
	f = LOWER;
	while (f <= UPPER) {
		c = 5 * (f - 32) / 9;
		printf("%3.0f\t%7.2f\n", f, c);
		f += STEP;
	}
	return 0;
}
