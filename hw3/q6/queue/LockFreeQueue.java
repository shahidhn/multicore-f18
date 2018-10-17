package queue;

import java.util.concurrent.atomic.AtomicReference;

import queue.LockQueue.Node;

public class LockFreeQueue implements MyQueue {
	AtomicReference<Node> head, tail;

	public LockFreeQueue() {
		Node dummy = new Node(-1);
		head = new AtomicReference<>(dummy);
		tail = new AtomicReference<>(dummy);
  }

	public boolean enq(Integer value) {
		Node newNode = new Node(value);
		while(true){
			Node curTail = tail.get();
			Node curTailNext = curTail.next.get();
			
			if (curTail == tail.get()){
				if (curTailNext != null){
					tail.compareAndSet(curTail, curTailNext);
				}
				else{
					if (curTail.next.compareAndSet(null, newNode)){
						tail.compareAndSet(curTail, newNode);
						return true;
					}
				}
			}	
		}
  }
  
  public Integer deq() {
	  while (true){
		  Node curHead = head.get();
		  Node curTail = tail.get();
		  Node curHeadNext = curHead.next.get();
		  
		  if (curHead == head.get()){
			  if(curHead == curTail){
				  if (curHeadNext == null){
					  return null;
				  }
				  else{
					  tail.compareAndSet(curTail, curHeadNext);
				  }
			  }
			  else{ // head and tail not same
				  if (head.compareAndSet(curHead, curHeadNext)){
					  return curHeadNext.value;
				  }
			  }
		  }
	  }
	  
  }
  

  
  protected class Node {
	  public Integer value;
	  public AtomicReference<Node> next;
		    
	  public Node(Integer x) {
		  value = x;
		  next = new AtomicReference<Node>();
	  }
  }
}
