package q6.Tournament;

import java.util.HashMap;

public class PIncrement implements Runnable{
	static volatile int num;
	static int incr;
	static TournamentLock t_lock;
	static HashMap<Long, Integer> id_map = new HashMap<Long, Integer>();

	public static int parallelIncrement(int c, int numThreads){
		t_lock = new TournamentLock(numThreads);
		num = c;
		incr = 1200000 / numThreads;
		//incr = 1000;
		
		Thread[] threads = new Thread[numThreads];
		for (int i = 0; i < numThreads; i++) {
			Thread t = new Thread(new PIncrement());
			id_map.put(t.getId(), i);
			t.start();
			threads[i] = t;
		}
		//System.out.println(id_map);
		for (Thread t : threads) {
			try {
				t.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		return num;
	}

	static void incrementNum() {
		num ++;
	}
	
	static int getId(long threadId){
		return id_map.get(threadId);
	}

	public void run() {
		int id = getId(Thread.currentThread().getId());
		for (int i = 0; i < incr; i ++) {
			t_lock.lock(id);
			num ++;
			t_lock.unlock(id);
		}
		//System.out.println(id + " terminating");
		//System.out.println(num);
	}

}
