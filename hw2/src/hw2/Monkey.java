package hw2;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


public class Monkey {
	// declare the variables here
	final Lock lock = new ReentrantLock();
	final Condition notFull  = lock.newCondition(); 
	//final Condition notEmpty = lock.newCondition();
	
	
	static volatile int numOnRope;
	static volatile int currentDirection;
	static volatile boolean kongTime;
	
	// A monkey calls the method when it arrives at the river bank and wants to climb
	// the rope in the specified direction (0 or 1); Kong’s direction is -1.
	// The method blocks a monkey until it is allowed to climb the rope.
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
		
		
		
		
		
		
	}
	// After crossing the river, every monkey calls this method which
	// allows other monkeys to climb the rope.
	public void LeaveRope() {
		lock.lock();
		try { 
			notFull.signal();
		} finally {
			lock.unlock();
		}
	}

}
