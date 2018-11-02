/*
Please use "inp.txt" as input file and output/write your results of each question to a separate file named as "q1a.txt", "q1b.txt" etc. The output file should have the same format as the input file. 
You only need to submit three source code files, e.g. q1.cu, q2.cu and q3.cu and the input file "inp.txt". Don't submit any other files.
*/
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#define THREADNUM 16
#define THREADS_PER_BLOCK 1024

__global__ void min(int *array, int *answer, int n){
	int index = (threadIdx.x + blockIdx.x * blockDim.x)*2;

	int d, val;
	for (d = n; d >= 1;  d = d/2){
		if (index < d){
			val = array[index];
			if (array[index+1] < val)
				val = array[index+1];
		}
		__syncthreads();
		if (index < d){
			array[index/2] = val;
		}
		__syncthreads();
	}
	*answer = array[0];

/*




	int chunk_size = n/blockDim.x;
	int i, localmin = 10000;
	__shared__ int min[THREADNUM];
	if (threadIdx.x < blockDim.x-1){
		for (i = threadIdx.x * chunk_size; i < threadIdx.x * chunk_size + chunk_size; i++){
			if(array[i] < localmin)
				localmin = array[i];
		}
	}
	else{
		for (i = threadIdx.x * chunk_size; i < n; i++){
			if(array[i] < localmin)
				localmin = array[i];
		}
	}
	min[threadIdx.x] = localmin;
	__syncthreads();
	if(threadIdx.x == 0){
		int globalmin = 10000;
		for(i = 0; i < blockDim.x; i ++)
			if(min[i] < globalmin)
				globalmin = min[i];
		*answer = globalmin;
	}
*/
}

__global__ void last_digit(int *array, int *b){
	b[blockIdx.x] = array[blockIdx.x] % 10;
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
	int answer;
	int *d_answer;    			
	int size = sizeof(array);
	int *b = (int *) malloc(size);
	int *d_b;

	// Allocate space for device copies of array
	cudaMalloc((void **)&d_array, size);
	cudaMalloc((void **)&d_answer, sizeof(int));
	cudaMalloc((void **)&d_b, size);

	cudaMemcpy(d_array, &array, size, cudaMemcpyHostToDevice);

	min<<<(numcomma + THREADS_PER_BLOCK)/THREADS_PER_BLOCK,THREADS_PER_BLOCK>>>(d_array, d_answer, numcomma+1);
	cudaMemcpy(&answer, d_answer, sizeof(int), cudaMemcpyDeviceToHost);

	last_digit<<<(numcomma+1), 1>>>(d_array, d_b);
	cudaMemcpy(b, d_b, size, cudaMemcpyDeviceToHost);

	cudaFree(d_answer); cudaFree(d_array); cudaFree(d_b);

	FILE *q1a = fopen("q1a.txt", "w+");
	fprintf(q1a, "Min: %d\n", answer);
	
	FILE *q1b = fopen("q1b.txt", "w+");
	for (i = 0; i <= numcomma; i ++){
		fprintf(q1b, "%d", b[i]);
		if (i < numcomma) fprintf(q1b, ", ");
	}
	free(b);
}




