package q3;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;



public class Monkey {

	static volatile int numOnRope = 0;
	static volatile int currentDirection = 2; //empty
	static volatile boolean kongWant = false;
	static volatile boolean kongHave = false;
//	int numOnRope;
//	int currentDirection;
//	boolean kongWant;
//	boolean kongHave;
	
	final Lock lock = new ReentrantLock();
	final Condition notFull  = lock.newCondition();
	final Condition kongDone  = lock.newCondition();
	final Condition empty = lock.newCondition();
	final Condition switchDir = lock.newCondition();

	public Monkey() {
		
	}

	public void ClimbRope(int direction) throws InterruptedException {
		lock.lock();
		try { 
			if (direction == -1){
				kongWant = true;
				System.out.println("I am kong");
				while(numOnRope != 0){
					empty.await();
					System.out.println("hear me roar");
				}
				kongHave = true;
				currentDirection = direction;
				kongWant = false;
				numOnRope ++;
				System.out.println(numOnRope);
			}
			else{
				while (kongWant || kongHave){
					System.out.println("momma called the doctor and the doctor said");
					kongDone.await();
				}
				while ((direction != currentDirection) && (currentDirection != 2)){
					System.out.println("direction");
					switchDir.await();
					currentDirection = direction;
				}
				while(numOnRope == 3){
					System.out.println("no more monkeys jumping on the bed");
					notFull.await();
				}
				while (kongWant || kongHave){
					System.out.println("one fell off and broke his head");
					kongDone.await();
				}
				numOnRope ++;
				System.out.println(numOnRope);
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
				currentDirection = 2;
				kongDone.signalAll();
				System.out.println("kong sleepy");
			} else{				
				numOnRope--;
				if (numOnRope == 0){
					currentDirection = 2;
					if(kongWant)
						empty.signal();
					else{
						switchDir.signal();
						notFull.signal();
					}
				}
				System.out.println("the end");
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
