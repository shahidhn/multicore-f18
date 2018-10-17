package stack;

import java.util.concurrent.atomic.AtomicReference;

public class LockFreeStack implements MyStack {
	AtomicReference<Node> top;
	
	public LockFreeStack() {
		top = new AtomicReference<Node>(null);
	}
	
  public boolean push(Integer value) {
	  Node node = new Node(value);
	  while(true) {
		  Node oldTop = top.get();
		  node.next = oldTop;
		  if (top.compareAndSet(oldTop, node)) return true;
		  		else Thread.yield();
	   }
  }
  
  public Integer pop() throws EmptyStack {
	  while(true) {
		  Node oldTop = top.get();
		  if(oldTop == null) throw new EmptyStack();
		  Integer val = oldTop.value;
		  Node newTop = oldTop.next;
		  if(top.compareAndSet(oldTop, newTop)) return val;
		   	else Thread.yield();
		  }
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
