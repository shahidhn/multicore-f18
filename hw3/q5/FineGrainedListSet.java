package q5;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.ReentrantLock;

public class FineGrainedListSet implements ListSet {
	Node head;

	public FineGrainedListSet() {
		this.head = new Node(null);
	}

	public boolean add(int value) {
		if (contains(value)) return false;

		if (head.value == null){
			head.lock.lock();
			head.value = value;
			head.lock.unlock();
			return true;
		}

		while (true){
			Node current = head;
			Node prev = current;

			while (current.value < value && current.next != null){
				prev = current;
				current = current.next;
			}
			// Exit while loop if current.next == null OR current.value >= value

			if (current.value == value){
				//				current.lock.lock();
				//				if (current.isDeleted.get()){
				//					current.isDeleted.set(false);
				//					current.lock.unlock();
				//					return true;
				//				} else {
				//					current.lock.unlock();
				//					return false;
				//				}
				return false;
			} else if (current.value > value){
					prev.lock.lock();
					current.lock.lock();

					if ((prev.isDeleted.get() || current.isDeleted.get()) || prev.next != current){
						prev.lock.unlock();
						current.lock.unlock();
						continue;
					}

					Node n = new Node(value);
					n.next = current;
					prev.next = n;
					prev.lock.unlock();
					current.lock.unlock();
					return true;
				} else { // current.value < value AND current.next == null
					current.lock.lock();
					if (current.isDeleted.get()){
						current.lock.unlock();
						continue;
					}

					Node n = new Node(value);
					current.next = n;
					current.lock.unlock();
					return true;

				}
		}

	}

	public boolean remove(int value) {
		if (!contains(value)) return false;

		while (true){
			Node current = head;
			Node prev = current;

			while (current.next != null && current.value < value){
				prev = current;
				current = current.next;
			}

			if (current.value == value){
				current.lock.lock();
				prev.lock.lock();

				if ((prev.isDeleted.get() || current.isDeleted.get()) || prev.next != current){
					prev.lock.unlock();
					current.lock.unlock();
					continue;
				}

				current.isDeleted.set(true);
				prev.next = current.next;
				current.lock.unlock();
				prev.lock.unlock();
				return true;

			} else {
				return false;
			}
		}

	}

	public boolean contains(int value) {
		if (head.value == null) return false;

		Node current = head;
		while (current.value < value && current.next != null){
			current = current.next;
		}
		return current.value == value && !current.isDeleted.get();
	}

	protected class Node {
		public Integer value;
		public Node next;
		ReentrantLock lock;
		AtomicBoolean isDeleted;


		public Node(Integer x) {
			value = x;
			next = null;
			isDeleted = new AtomicBoolean();
			isDeleted.set(false);
			lock = new ReentrantLock();
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
			current.lock.lock();
			if (!current.isDeleted.get()){
				s = s + current.value + ",";
			}
			current.lock.unlock();
			current = current.next;
		}
		return s;
	}
}
