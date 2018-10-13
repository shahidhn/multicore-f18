package queue;

import org.junit.Assert;
import org.junit.Test;

import q5.CoarseGrainedListSet;






public class SimpleTest {
	
    @Test
    public void testLockQueue() {
        LockQueue queue = new LockQueue();
        makeThread(queue);
        checkNode(0, 3000, queue);
    }
	
	
    private void makeThread(MyQueue queue) {
        Thread[] threads = new Thread[3];
        threads[0] = new Thread(new MyThread(0, 2000, queue));
        threads[1] = new Thread(new MyThread(0, 3000, queue));
        threads[2] = new Thread(new MyThread(1000, 3000, queue));
        threads[1].start(); threads[0].start(); threads[2].start();

        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }	
	
    private void checkNode(int start, int end, MyQueue queue) {
        StringBuilder sb = new StringBuilder();
        for (int i = start; i <= end; ++i) {
            sb.append(i).append(",");
        }
        System.out.println(queue.toString());
        Assert.assertEquals(queue.toString(), sb.toString());
    }
	
	private class MyThread implements Runnable {

        int begin;
        int end;
        MyQueue queue;

        MyThread(int begin, int end, MyQueue queue) {
            this.begin = begin;
            this.end = end;
            this.queue = queue;
        }

        @Override
        public void run() {
            for (int i = begin; i <= end; ++i) {
            	queue.enq(i);
            }
        }
	}

}
