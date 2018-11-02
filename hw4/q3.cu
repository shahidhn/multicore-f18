/*
Please use "inp.txt" as input file and output/write your results of each question to a separate file named as "q1a.txt", "q1b.txt" etc. The output file should have the same format as the input file. 
You only need to submit three source code files, e.g. q1.cu, q2.cu and q3.cu and the input file "inp.txt". Don't submit any other files.
*/
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#define NUMBLOCKS 10

__global__ void convertTo1and0(int *array, int *b, int * idx){
	b[blockIdx.x] = array[blockIdx.x] % 2;
	idx[blockIdx.x] = array[blockIdx.x] % 2;
}

__global__ void PPRead(int * b, int * c, int d){
	if (blockIdx.x >= d) c[blockIdx.x] = b[blockIdx.x - d];
}


__global__ void PPWrite(int * b, int * c, int d){
	if (blockIdx.x >= d) b[blockIdx.x] += c[blockIdx.x];
}

__global__ void findOdds(int * array, int * idx, int * b, int * d){
	if (idx[blockIdx.x]){
		d[b[blockIdx.x] - 1] = array[blockIdx.x];
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


	int *d_array; // Holds device copy of array
	int size = sizeof(array);
	int *d_b; // Holds parallel prefix sum of d_idx
	int *d_c;
	int *d_idx; // 1 for odd number in array, 0 for even number in array
	int *d = (int *) malloc(size);
	int *d_d; // Holds final answer

	int *b = (int *) malloc(size);

	cudaMalloc((void **)&d_b, size);
	cudaMalloc((void **)&d_c, size);
	cudaMalloc((void **)&d_array, size);
	cudaMalloc((void **)&d_d, size);
	cudaMalloc((void **)&d_idx, size);
	cudaMemcpy(d_array, &array, size, cudaMemcpyHostToDevice);

	convertTo1and0<<<(numcomma+1), 1>>>(d_array, d_b, d_idx);
	
	for (int d = 1; d <= numcomma; d *= 2){
		PPRead<<<(numcomma+1), 1>>>(d_b, d_c, d);
		PPWrite<<<(numcomma+1), 1>>>(d_b, d_c, d);
	}	

	cudaMemcpy(b, d_b, size, cudaMemcpyDeviceToHost);
	int final_size = b[numcomma];

	findOdds<<<(numcomma+1), 1>>>(d_array, d_idx, d_b, d_d);

	cudaMemcpy(d, d_d, size, cudaMemcpyDeviceToHost);
	cudaFree(d_array); cudaFree(d_d); cudaFree(d_b), cudaFree(d_c); cudaFree(d_idx);

	FILE *q3 = fopen("q3.txt", "w+");
	for (i = 0; i < final_size; i ++){
		fprintf(q3, "%d", d[i]);
		if (i < final_size-1) fprintf(q3, ", ");
	}	

	free(d); free(b);
}




