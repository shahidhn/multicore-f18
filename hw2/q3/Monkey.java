package q3;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;



public class Monkey {

	static volatile int numOnRope;
	static volatile int currentDirection;
	static volatile boolean kongWant;
	static volatile boolean kongHave;

	public Monkey() {
		final Lock lock = new ReentrantLock();
		final Condition notFull  = lock.newCondition();
		final Condition kongDone  = lock.newCondition();
		final Condition empty = lock.newCondition();
		final Condition switchDir = lock.newCondition();

	}

	public void ClimbRope(int direction) throws InterruptedException {
		lock.lock();
		try { 
			if (direction == -1){
				kongWant = true;
				while(numOnRope != 0){
					empty.await();
				}
				kongHave = true;
				kongWant = false;
				numOnRope ++;
			}
			else{
				while (direction != currentDirection){
					switchDir.await();
				}
				while(numOnRope == 3){
					notFull.await();
				}
				while (kongWant || kongHave){
					kongDone.await();
				}
				numOnRope ++;	
			}
		} finally {
			lock.unlock();
		}
	}

	public void LeaveRope() {
		lock.lock();
		try { 
			if (kongHave){
				numOnRope--;
				kongHave = false;
				kongDone.signal();
			} else{				
				numOnRope--;
				if (numOnRope == 0){
					currentDirection = 1 - currentDirection;
					switchDir.signal();
				}
				notFull.signal();
			}	
		} finally {
			lock.unlock();
		}
	}

	/**
	 * Returns the number of monkeys on the rope currently for test purpose.
	 *
	 * @return the number of monkeys on the rope
	 *
	 * Positive Test Cases:
	 * case 1: when normal monkey (0 and 1) is on the rope, this value should <= 3, >= 0
	 * case 2: when Kong is on the rope, this value should be 1
	 */
	public int getNumMonkeysOnRope() {
		return numOnRope;
	}

}
