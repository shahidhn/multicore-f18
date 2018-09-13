package q6.AtomicInteger;

import java.util.concurrent.*;
import java.util.concurrent.atomic.*;

public class PIncrement implements Runnable{
	private static AtomicInteger num;
	static int incr;
	
    public static int parallelIncrement(int c, int numThreads){
    	num = new AtomicInteger(c);
    	incr = 1200000 / numThreads;
    	
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

    	return num.get();
    	
    }
	@Override
	public void run() {
		for (int i = 0; i < incr; i ++){
			num.getAndIncrement();
		}
	}
}
