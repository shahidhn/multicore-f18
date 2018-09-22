package q2.a;

import java.util.HashMap;

public class PIncrement implements Runnable {
	static int N, incr, delta;
	static volatile int turn;
	static HashMap<Long, Integer> id_map = new HashMap<Long, Integer>();
	
	
    public static int parallelIncrement(int c, int numThreads) {
        N = c;
        incr = 120000 / numThreads;
        delta = 10*1000; // in nanoseconds
        turn = -1;

    	Thread[] threads = new Thread[numThreads];
    	for (int i = 0; i < numThreads; i ++){
    		Thread t = new Thread(new PIncrement());
    		id_map.put(t.getId(), i);
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

	@Override
	public void run() {
		int id = id_map.get(Thread.currentThread().getId());
		
		for (int i = 0; i < incr; i ++){
			while (true){
				while (turn != -1);
				turn = id;
				try {
					Thread.sleep(0, delta);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				if (turn == id) break;
			}
			
			N ++;
			turn = -1;
		}
		
	}

}
