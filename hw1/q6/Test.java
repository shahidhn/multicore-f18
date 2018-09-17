package q6;

public class Test {

	public static void main(String[] args) {
		long time, startTime, endTime;
		for (int i = 1; i <= 8; i++){
			startTime = System.nanoTime();
			q6.AtomicInteger.PIncrement.parallelIncrement(0, i);
			endTime = System.nanoTime();
			time = endTime - startTime;
			System.out.println("atomic" + i + "\t" + time/1000000);
		}
		for (int i = 1; i <= 8; i++){
			startTime = System.nanoTime();
			q6.ReentrantLock.PIncrement.parallelIncrement(0, i);
			endTime = System.nanoTime();
			time = endTime - startTime;
			System.out.println("reentrant" + i + "\t" + time/1000000);
		}
		for (int i = 1; i <= 8; i++){
			startTime = System.nanoTime();
			q6.Synchronized.PIncrement.parallelIncrement(0, i);
			endTime = System.nanoTime();
			time = endTime - startTime;
			System.out.println("synchronized" + i + "\t" + time/1000000);
		}
		for (int i = 1; i <= 8; i++){
			startTime = System.nanoTime();
			q6.Tournament.PIncrement.parallelIncrement(0, i);
			endTime = System.nanoTime();
			time = endTime - startTime;
			System.out.println("tournament" + i + "\t" + time/1000000);
			
		}
	}

}
