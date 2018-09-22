#include <stdio.h>
#include <omp.h>
#include <stdlib.h>
#include <string.h>

void MatrixMult(char file1[],char file2[],int T){
	FILE * fp1 = fopen(file1, "r");
	FILE * fp2 = fopen(file2, "r");
	int m1r, m1c, m2r, m2c;
	fscanf(fp1, "%d %d", &m1r, &m1c);
	fscanf(fp2, "%d %d", &m2r, &m2c);
	if (m1c != m2r)
		printf("incorrect matrix shape\n");
	//printf("%d %d %d %d", m1r, m1c, m2r, m2c);
	int mat1[m1r][m1c];
	int mat2[m2r][m2c];
	int i, j;
	for (i = 0; i < m1r; i++){
		for (j = 0; j < m1c; j ++){
			fscanf(fp1, "%d", &mat1[i][j]);
		}
	}
	for (i = 0; i < m2r; i++){
		for (j = 0; j < m2c; j ++){
			fscanf(fp2, "%d", &mat2[i][j]);
		}
	}
	
	int outmat[m1r][m2c];
	omp_set_num_threads(T);
	#pragma omp parallel
	{
	// multiply
	int r,c,inner,sum;

	//#pragma omp parallel shared(m1r,m2c,outmat) private(r,c,inner,sum)


	//#pragma omp for schedule (static, 5)
	#pragma omp for
	for (r = 0; r < m1r; r++){
		for (c = 0; c < m2c; c++){
			sum = 0;
			//#pragma omp parallel for reduction (+:sum) <- this way takes more time to run
			for (inner = 0; inner < m1c; inner ++){
				sum += (mat1[r][inner]*mat2[inner][c]);
			}
			//#pragma omp atomic
			outmat[r][c] = sum; 
			//printf("%d ", sum);
		}
		//printf("\n");
	}

	} // end of parallel

	for(i = 0; i < m1r; i ++){
		for(j = 0; j < m2c; j++){
			printf("%d ", outmat[i][j]);
		}
		printf("\n");
	}


	fclose(fp1);
	fclose(fp2);
}

void main(int argc, char *argv[])
{
  char *file1, *file2;
  file1=argv[1];
  file2=argv[2];
  int T=atoi(argv[3]);
  MatrixMult(file1,file2,T);
}


