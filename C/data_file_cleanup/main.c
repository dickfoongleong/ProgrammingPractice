#include <string.h>
#include <stdlib.h>
#include <stdio.h>

int main(int argc, char **argv) {
	system("reset");

	char *file_path;
	if (argc == 2) {
		file_path = argv[1];
	} else {
		file_path = (char *) malloc(2048 * sizeof(char));
		printf("Please drag and drop the file to clean:\n");
		fgets(file_path, 2048, stdin);
	}

	while (file_path[strlen(file_path) - 1] == '\n' || file_path[strlen(file_path) - 1] == '\t' || file_path[strlen(file_path) - 1] == ' ') {
		file_path[strlen(file_path) - 1] = '\0';
	}

	printf("File Enter: >>%s<<\n", file_path);

	return 0;
}
