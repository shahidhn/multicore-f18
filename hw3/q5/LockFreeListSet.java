package q5;

import java.util.concurrent.atomic.AtomicMarkableReference;

public class LockFreeListSet implements ListSet {
	Node head;

	public LockFreeListSet() {
		this.head = new Node(null, null); 
	}

	public boolean add(int value) {
		if (head.value == null){
			head.value = value;
			return true;
		}
		
		while (true){
			if (contains(value)) return false;
			Node current = head;
			Node prev = current;
			
			while (current.value < value && current.next.getReference() != null){
				prev = current;
				current = current.next.getReference();
			}
			
			if (current.value == value){
				return false;
			} else if (current.value > value){
				Node n = new Node(value, current);
				
				if (prev.next.compareAndSet(current, n, false, false)) return true;
				else continue;
			} else { // current.value < value && current.next.getReference() == null
				Node n = new Node(value, null);

				if (current.next.compareAndSet(null, n, false, false)) return true;
				else continue;
			}
		}
	}

	public boolean remove(int value) {
		if (!contains(value)) return false;
		
		while (true){
			Node current = head;
			Node prev = current;
			
			while (current.value < value && current.next.getReference() != null){
				prev = current;
				current = current.next.getReference();
			}
			
			if (current.value == value){
				Node next = current.next.getReference();
				if (!current.next.compareAndSet(next, next, false, true)) continue;
				if (prev.next.compareAndSet(current, next, false, false)){
					return true;
				} else {
					continue;
				}
			} else {
				return false;
			}
		}
	}

	public boolean contains(int value) {
		if (head.value == null) return false;
		
		Node current = head;
		while (current.value < value && current.next.getReference() != null){
			current = current.next.getReference();
		}
		
		return current.value == value && !current.next.isMarked();
	}

	protected class Node {
		public Integer value;
		//public Node next;
		AtomicMarkableReference<Node> next;
		
		public Node(Integer x, Node n) {
			value = x;
			next = new AtomicMarkableReference<Node>(n, false);
		}
	}

	/*
  return the string of list, if: 1 -> 2 -> 3, then return "1,2,3,"
  check simpleTest for more info
	 */
	public String toString() {
		String s = "";
		if (head.value == null) return s;
		
		Node current = head;
		while (current != null){
			if (!current.next.isMarked()){
				s = s + current.value + ",";
			}
			current = current.next.getReference();
		}
		return s;
		
		
		
	}
}
