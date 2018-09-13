package q6.Synchronized;

import java.util.concurrent.*;
import java.util.*;

public class PIncrement implements Runnable{
	static volatile int num;
	static int incr;
	
    public static int parallelIncrement(int c, int numThreads){
        // your implementation goes here.
    	num = c;
    	incr = 1200000 / numThreads;
    	
    	// This seems to run faster...
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

    	return num;
    	
    	
//    	ExecutorService execService = Executors.newFixedThreadPool(numThreads);
//    	List<Future<Runnable>> futures = new ArrayList<Future<Runnable>>();
//    	
//    	for (int i = 0; i < numThreads; i ++){
//    		Future f = execService.submit(new PIncrement());
//    		futures.add(f);
//    	}
//    	for (Future future : futures){
//    		try {
//				future.get();
//			} catch (InterruptedException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			} catch (ExecutionException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//    	}
    }  
    
    static synchronized void incrementNum(){
    	num ++;
    }
    
	@Override
	public void run() {	
		for (int i = 0; i < incr; i ++){
			incrementNum();
		}
	}

}
