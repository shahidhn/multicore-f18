package q5;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import java.util.Arrays;

public class Frequency implements Callable<Integer> {
    
	int[] subA;
	int find;
	
	public Frequency(int[] subA, int find){
		this.subA = subA;
		this.find = find;
	}
	
	public static int parallelFreq(int x, int[] A, int numThreads){
    	int arrLen = A.length;
    	if (arrLen == 0 || numThreads == 0) return -1;
    	
    	int chunk = arrLen / numThreads;
    	int begin = 0;
    	int end = chunk;
    	FutureTask<Frequency>[] futureTasks = new FutureTask[numThreads];
    	
    	// Start first (numThreads - 1) threads
    	for (int i = 0; i < (numThreads-1); i ++){
    		int[] subArr = Arrays.copyOfRange(A, begin, end);
    		Frequency frequency = new Frequency (subArr, x);
    		futureTasks[i] = new FutureTask(frequency);
    		Thread t = new Thread(futureTasks[i]);
    		t.start();
    		
    		begin += chunk;
    		end += chunk;
    	}
    	
    	// For the last thread, check all remaining elements
    	int[] subArr = Arrays.copyOfRange(A, begin, arrLen);
		Frequency frequency = new Frequency (subArr, x);
		futureTasks[numThreads-1] = new FutureTask(frequency);
		Thread t = new Thread(futureTasks[numThreads-1]);
		t.start();
		
		int sum = 0;
		for (FutureTask futureTask : futureTasks){
			try {
				sum += (int) futureTask.get();
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (ExecutionException e) {
				e.printStackTrace();
			}
		}
		
		return sum;
    }

	@Override
	public Integer call() throws Exception {
		int sum = 0;
		for (int num : subA){
			if (num == find){
				sum += 1;
			}
		}
		return sum;
	}  
}
