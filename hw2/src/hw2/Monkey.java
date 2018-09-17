package hw2;

import java.util.concurrent.locks.ReentrantLock;


public class Monkey {
	// declare the variables here
	// A monkey calls the method when it arrives at the river bank and wants to climb
	// the rope in the specified direction (0 or 1); Kong’s direction is -1.
	// The method blocks a monkey until it is allowed to climb the rope.
	public void ClimbRope(int direction) throws InterruptedException {
	}
	// After crossing the river, every monkey calls this method which
	// allows other monkeys to climb the rope.
	public void LeaveRope() {
	}

}
