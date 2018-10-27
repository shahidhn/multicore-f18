/*
Please use "inp.txt" as input file and output/write your results of each question to a separate file named as "q1a.txt", "q1b.txt" etc. The output file should have the same format as the input file. 
You only need to submit three source code files, e.g. q1.cu, q2.cu and q3.cu and the input file "inp.txt". Don't submit any other files.
*/
#include <stdio.h>
#include <stdlib.h>
#include <string.h>

int main(void) {
	int numcomma = 0;
	char c;
	FILE* stream = fopen("inp.txt", "r");
	while(1){
		c = fgetc(stream);
		if (c == EOF)
			break;
		if (c == ',')
			numcomma ++;
	}
	printf("%d\n", numcomma);
	fclose(stream);
	int array[numcomma+1];

	stream = fopen("inp.txt", "r");
	int i;
	for (i = 0; i <= numcomma; i ++){
		fscanf(stream, "%d,", &array[i]);
	}
	printf("%d\n", array[3]);
	fclose(stream);


}





		// int a[];	 			// host copies of a
		// int *d_a; 	     			// device copies of a
		// int size = sizeof(int);
		
		// // Allocate space for device copies of a, b, c
		// cudaMalloc((void **)&d_a, size);
