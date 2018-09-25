#include <stdio.h>
#include <stdlib.h>
#include <omp.h>
#include <math.h>
#include <time.h>

double MonteCarlo(int s)
{
//Write your code here
	omp_set_num_threads(12);
	time_t t;
	srand((unsigned) time(&t));
	int i,x,y, c =0;
	#pragma omp parallel 
	{
		for (i = 0; i < s; i ++){
			//#pragma omp parallel
			//{
			x = ((rand()%2000)- 1000);
			y = ((rand()%2000) - 1000);
			//}
			if (x*x + y*y < 1000*1000)
				c ++;
		}
	}
	return (((double)c/(double)s) *4);
}

void main()
{
double pi;
clock_t begin = clock();
pi=MonteCarlo(100000000);
clock_t end = clock();
printf("Value of pi is: %lf\n",pi);
printf("time %lf\n", (double)(end - begin)/CLOCKS_PER_SEC);
}



