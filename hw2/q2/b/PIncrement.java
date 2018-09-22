package q2.b;

import java.util.HashMap;

public class PIncrement implements Runnable {
	static int N, incr, nThreads;
	static HashMap<Long, Integer> id_map = new HashMap<Long, Integer>();
	static volatile int x, y;
	static volatile boolean[] flags;
	/*
	 * true => flag up
	 * false => flag down
	 */
	
    public static int parallelIncrement(int c, int numThreads) {
    	N = c;
    	incr = 120000 / numThreads;
    	nThreads = numThreads;
    	x = -1;
    	y = -1;
    	flags = new boolean[numThreads];
    	
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
			/* Request CS */
			while (true){
				flags[id] = true;
				x = id;
				
				if (y != -1){
					flags[id] = false;
					while (y != -1);
					continue;
				} else {
					y = id;
					if (x == id) break; // Success via fast path
					else {
						flags[id] = false;
						for (int j = 0; j < nThreads; j ++){
							while (flags[j] == true);
						}
						if (y == id) break; // Success via slow path
						else {
							while (y != -1);
							continue;
						}
					}
				}
			}
			
			/* CS */
			N ++;
			
			/* Release CS */
			y = -1;
			flags[id] = false;
			
		}
		
	}

}
