package q5;

import java.util.concurrent.locks.ReentrantLock;

public class CoarseGrainedListSet implements ListSet {
// you are free to add members
	private final ReentrantLock lock;
	Node head;
	
  public CoarseGrainedListSet() {
	this.lock = new ReentrantLock();
	this.head = new Node(null);
  }
  
  public boolean add(int value) {
	lock.lock();
	
	// Insert at head
	if (head.value == null){
		head.value = value;
		
		lock.unlock();
		return true;
	}
	
	Node current = head;
	Node prev = current;
	
	while (current.next != null && current.value < value){
		prev = current;
		current = current.next;
	}
	// Exit while loop if current.next == null OR current.value >= value
	
	if (current.value == value){
		lock.unlock();
		return false;
	} else if (current.value > value){
		Node n = new Node(value);
		prev.next = n;
		n.next = current;
		
		lock.unlock();
		return true;
	} else { // current.value < value AND current.next == null
		Node n = new Node(value);
		current.next = n;
		
		lock.unlock();
		return true;
	}
	
  }
  
  public boolean remove(int value) {
	lock.lock();
	
	// Check head
	if (head.value == null){
		lock.unlock();
		return false;
	}
	
	Node current = head;
	Node prev = current;
	
	while (current.next != null && current.value < value){
		prev = current;
		current = current.next;
	}
	// Exit while loop if current.next == null OR current.value >= value
	
	if (current.value == value){
		prev.next = current.next;
		
		lock.unlock();
		return true;
	} else { // (current.value > value) || (current.value < value && current.next == null)
		lock.unlock();
		return false;
	}
    
  }
  
  public boolean contains(int value) {
	lock.lock();
	if (head.value == null){
		lock.unlock();
		return false;
	}
	
	Node current = head;
	while (current.value < value && current.next != null){
		current = current.next;
	}
	
	lock.unlock();
	return current.value == value;
	
  }
  
  protected class Node {
	  public Integer value;
	  public Node next;
		    
	  public Node(Integer x) {
		  value = x;
		  next = null;
	  }
  }

  /*
  return the string of list, if: 1 -> 2 -> 3, then return "1,2,3,"
  check simpleTest for more info
  */
  public String toString() {
    String s = "";
    lock.lock();
    if (head.value == null){
    	lock.unlock();
    	return s;
    }
    
    Node current = head;
    while (current != null){
    	s = s + current.value + ",";
    	current = current.next;
    }
    
    lock.unlock();
    return s;
    
  }
}
