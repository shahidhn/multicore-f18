#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#define THREADNUM 16



__global__ void parta(int *array, int *B, int n){
	int chunk_size = n/blockDim.x;
	int i;

	if (threadIdx.x < blockDim.x-1){
		for (i = threadIdx.x * chunk_size; i < threadIdx.x * chunk_size + chunk_size; i++){
			//B[array[i] / 100] += 1;
			atomicAdd(&B[array[i] / 100], 1);
		}
	}
	else{
		for (i = threadIdx.x * chunk_size; i < n; i++){
			//B[array[i] / 100] += 1;
			atomicAdd(&B[array[i] / 100], 1);
		}
	}
	__syncthreads();


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


	int *d_array;
	int B[10];
	int *d_B;    			
	int size = sizeof(array);
	// Allocate space for device copies of array
	cudaMalloc((void **)&d_array, size);
	cudaMalloc((void **)&d_B, sizeof(int)*10);
	cudaMemcpy(d_array, &array, size, cudaMemcpyHostToDevice);

	parta<<<1,THREADNUM>>>(d_array, d_B, numcomma+1);
	cudaMemcpy(&B, d_B, sizeof(int)*10, cudaMemcpyDeviceToHost);
	cudaFree(d_B); cudaFree(d_array);
	printf("b0: %d\n", B[0]);
	printf("b1: %d\n", B[1]);
	printf("b5: %d\n", B[5]);
	printf("total count: %d\n", B[0]+B[1]+B[2]+B[3]+B[4]+B[5]+B[6]+B[7]+B[8]+B[9]);
}
