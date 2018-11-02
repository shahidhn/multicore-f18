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
	printf("b0: %d\n", B[0]);
	printf("b1: %d\n", B[1]);
	printf("b5: %d\n", B[5]);
	printf("total count: %d\n", B[0]+B[1]+B[2]+B[3]+B[4]+B[5]+B[6]+B[7]+B[8]+B[9]);
	printf("now correct");
		
	// Q2b
	
	cudaMalloc((void **)&d_array, size);
	cudaMalloc((void **)&d_B, sizeof(int)*10);
	cudaMemcpy(d_array, &array, size, cudaMemcpyHostToDevice);

	partb<<<(array_len + THREADS_PER_BLOCK - 1)/THREADS_PER_BLOCK,THREADS_PER_BLOCK>>>(d_array, d_B, array_len);
	cudaMemcpy(&B, d_B, sizeof(int)*10, cudaMemcpyDeviceToHost);
	cudaFree(d_B); cudaFree(d_array);
	printf("partB");
	printf("b0: %d\n", B[0]);
	printf("b1: %d\n", B[1]);
	printf("b5: %d\n", B[5]);
	printf("total count: %d\n", B[0]+B[1]+B[2]+B[3]+B[4]+B[5]+B[6]+B[7]+B[8]+B[9]);
	







}
