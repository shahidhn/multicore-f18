package queue;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class LockQueue implements MyQueue {
	Node head, tail;
	Lock enqlock = new ReentrantLock();
	Lock deqlock = new ReentrantLock();

	
  public LockQueue() {
	Node node = new Node(-1);
	head = node;
	tail = node;
  }
  
  public boolean enq(Integer value) {
	Node node = new Node(value);
	enqlock.lock();
	tail.next = node;
	tail = node;
	enqlock.unlock();
    return false;
  }
  
  public Integer deq() {
	deqlock.lock();
	if (head.next == null){
		deqlock.unlock();
		return null;
	}
	Integer i = head.next.value;
	head.next = head.next.next;
	deqlock.unlock();
    return i;
  }
  
  protected class Node {
	  public Integer value;
	  public Node next;
		    
	  public Node(Integer x) {
		  value = x;
		  next = null;
	  }
  }
}
