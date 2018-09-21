package q3;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;



public class Monkey {

    public Monkey() {
    	final Lock lock = new ReentrantLock();
    	final Condition notFull  = lock.newCondition();
    	//final Condition notEmpty = lock.newCondition();
    	
    	
    	static volatile int numOnRope;
    	static volatile int currentDirection;
    	static volatile boolean kongTime;
    	
    }

    public void ClimbRope(int direction) throws InterruptedException {
    	lock.lock();
		try { 
			if (direction == -1){
				kongTime = true;
				
			}
			else{
				while (((direction != currentDirection) || (numOnRope == 3)) || kongTime){
					notFull.await();
				}
				numOnRope ++;
				currentDirection = direction;
			}		
			
		} finally {
			lock.unlock();
    }

    public void LeaveRope() {
    	lock.lock();
		try { 
			notFull.signal();
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
        return -1;
    }

}
