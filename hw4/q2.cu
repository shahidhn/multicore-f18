#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#define THREADS_PER_BLOCK 1024



__global__ void parta(int *array, int *B, int n){
	int index = threadIdx.x + blockIdx.x * blockDim.x;
	if (index < n){
		atomicAdd(&B[array[index] / 100], 1);
	}	

	__syncthreads();

}


__global__ void partb(int *array, int *B, int n){
	__shared__ int localB[10];
	int i;
		
	int index = threadIdx.x + blockIdx.x * blockDim.x;
	if (index < n){
		atomicAdd(&localB[array[index] / 100], 1);
	}

	__syncthreads();

	if (threadIdx.x == 0){
		for (i = 0; i < 10; i++){
			atomicAdd(&B[i], localB[i]);
		}
	}

}

__global__ void partc(int *B, int n){
	int d, val;
	
	for (d = 1; d < n; d = d*2){
		if (threadIdx.x >= d)
			val = B[threadIdx.x-d];
		__syncthreads();
		if (threadIdx.x >= d)
			B[threadIdx.x] += val;
		__syncthreads();	
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
	int array_len = numcomma + 1;

	int *d_array;
	int B[10];
	int *d_B;    			
	int size = sizeof(array);
	// Allocate space for device copies of array
	cudaMalloc((void **)&d_array, size);
	cudaMalloc((void **)&d_B, sizeof(int)*10);
	cudaMemcpy(d_array, &array, size, cudaMemcpyHostToDevice);

	parta<<<(array_len + THREADS_PER_BLOCK - 1)/THREADS_PER_BLOCK,THREADS_PER_BLOCK>>>(d_array, d_B, array_len);
	cudaMemcpy(&B, d_B, sizeof(int)*10, cudaMemcpyDeviceToHost);
	cudaFree(d_B); cudaFree(d_array);

	FILE *q2a = fopen("q2a.txt", "w+");
	for (i = 0; i <= 9; i++){
		fprintf(q2a, "%d", B[i]);
		if (i < 9) fprintf(q2a, ", ");
	}
	fclose(q2a);

	
	
	// Q2b
	
	cudaMalloc((void **)&d_array, size);
	cudaMalloc((void **)&d_B, sizeof(int)*10);
	cudaMemcpy(d_array, &array, size, cudaMemcpyHostToDevice);

	partb<<<(array_len + THREADS_PER_BLOCK - 1)/THREADS_PER_BLOCK,THREADS_PER_BLOCK>>>(d_array, d_B, array_len);
	cudaMemcpy(&B, d_B, sizeof(int)*10, cudaMemcpyDeviceToHost);
	cudaFree(d_B); cudaFree(d_array);
	
	FILE *q2b = fopen("q2b.txt", "w+");
	for (i = 0; i <= 9; i++){
		fprintf(q2b, "%d", B[i]);
		if (i < 9) fprintf(q2b, ", ");
	}
	fclose(q2b);


	// Q2c


	cudaMalloc((void **)&d_B, sizeof(int)*10);
	cudaMemcpy(d_B, &B, sizeof(int)*10, cudaMemcpyHostToDevice);


	partc<<<1,10>>>(d_B, array_len);
	cudaMemcpy(&B, d_B, sizeof(int)*10, cudaMemcpyDeviceToHost);
	cudaFree(d_B);
	
	FILE *q2c = fopen("q2c.txt", "w+");
	for (i = 0; i <= 9; i++){
		fprintf(q2c, "%d", B[i]);
		if (i < 9) fprintf(q2c, ", ");
	}
	fclose(q2c);









}
