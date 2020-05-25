#include <stdio.h>
#include <stdlib.h>

int main(int argc, char **argv) {
	system("/usr/bin/osascript -e 'tell application \"System Events\" to tell process \"Terminal\" to keystroke \"k\" using command down'");
	printf("%d\n", argc);
	while (--argc >= 0) {
		printf("%s\n", *(argv++));
	}
}
