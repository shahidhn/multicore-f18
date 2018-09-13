package q6.ReentrantLock;

import java.util.concurrent.locks.ReentrantLock;

public class PIncrement implements Runnable{
	private static ReentrantLock lock = new ReentrantLock();
	static volatile int num;
	static int incr;
	
    public static int parallelIncrement(int c, int numThreads){
        num = c;
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

    	return num;
    }

    static void incrementNum(){
    	num ++;
    }
    
	@Override
	public void run() {
		for (int i = 0; i < incr; i ++){
			lock.lock();
			try {
				incrementNum();
			} finally {
				lock.unlock();
			}
		}
	}
}
