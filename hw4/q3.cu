/*
Please use "inp.txt" as input file and output/write your results of each question to a separate file named as "q1a.txt", "q1b.txt" etc. The output file should have the same format as the input file. 
You only need to submit three source code files, e.g. q1.cu, q2.cu and q3.cu and the input file "inp.txt". Don't submit any other files.
*/
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#define NUMBLOCKS 10

__global__ void convertTo1and0(int *array, int *b){
	b[blockIdx.x] = array[blockIdx.x] % 2;
}

__global__ void PPRead(int * b, int * c, int d){
	if (blockIdx.x >= d) c[blockIdx.x] = b[blockIdx.x - d];
}


__global__ void PPWrite(int * b, int * c, int d){
	if (blockIdx.x >= d) b[blockIdx.x] += c[blockIdx.x];
}

__global__ void parallelPrefix(int * b, int *c, int n){
	for (int d = 1; d < n; d *= 2){
//		PPRead<<<n, 1>>>(b, c, d);
//		PPRead<<<n, 1>>>(b, c, d);
	}	
}


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
	fclose(stream);

	int array[numcomma+1];

	stream = fopen("inp.txt", "r");
	int i;
	for (i = 0; i <= numcomma; i ++){
		fscanf(stream, "%d,", &array[i]);
	}
	fclose(stream);


	int *d_array;
	int size = sizeof(array);
	int *d_b;
	int *d_c;
	int *d = (int *) malloc(size);
	int *d_d;

	int *b = (int *) malloc(size);

	cudaMalloc((void **)&d_b, size);
	cudaMalloc((void **)&d_c, size);
	cudaMalloc((void **)&d_array, size);
	cudaMalloc((void **)&d_d, size);
	cudaMemcpy(d_array, &array, size, cudaMemcpyHostToDevice);

	convertTo1and0<<<(numcomma+1), 1>>>(d_array, d_b);
	
	for (int d = 1; d <= numcomma; d *= 2){
		PPRead<<<(numcomma+1), 1>>>(d_b, d_c, d);
		PPWrite<<<(numcomma+1), 1>>>(d_b, d_c, d);
	}	
//	parallelPrefix<<<(numcomma+1), 1>>>(d_b, d_c);		

	cudaMemcpy(b, d_b, size, cudaMemcpyDeviceToHost);
	for (i = 0; i <= numcomma; i ++) printf("%d ", b[i]);

	cudaFree(d_array); cudaFree(d_d);

	FILE *q3 = fopen("q3.txt", "w+");
	
	free(d);
}




