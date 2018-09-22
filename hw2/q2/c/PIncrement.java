package q2.c;

import java.util.concurrent.atomic.AtomicInteger;

public class PIncrement implements Runnable {
	static int incr, nThreads;
	static volatile int N;
	static volatile boolean[] available;
	static AtomicInteger tailSlot = new AtomicInteger(0);
	
	private ThreadLocal<Integer> mySlot = new ThreadLocal<Integer>();
	
    public static int parallelIncrement(int c, int numThreads) {
    	N = c;
    	incr = 120000 / numThreads;
    	//incr = 100;
    	nThreads = numThreads;
    	
    	available = new boolean[numThreads];
    	for (int j = 1; j < numThreads; j ++) available[j] = false;
    	available[0] = true;
    	
    	Thread[] threads = new Thread[numThreads];
    	for (int i = 0; i < numThreads; i ++){
    		Thread t = new Thread(new PIncrement());
    		t.start();
    		threads[i] = t;
    	}

    	for (Thread t : threads){
    		try {
				t.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
    	}
    	
        return N;
    }
    
    void print_avail(){
    	for (boolean a : available) System.out.print(a + " ");
    	System.out.println();
    }
    
	@Override
	public void run() {
		mySlot.set(0);
		
		for (int i = 0; i < incr; i ++){
			mySlot.set(tailSlot.getAndIncrement() % nThreads);
			while (!available[mySlot.get()]);
			
			N ++;
			
			available[mySlot.get()] = false;
			available[(mySlot.get() + 1) % nThreads] = true;
		}
		
	}

}
