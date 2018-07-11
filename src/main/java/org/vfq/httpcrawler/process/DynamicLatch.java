package org.vfq.httpcrawler.process;

/**
 * Synchronization aid which allows a thread to wait until a dynamically changing amount of actions is finished.
 * Waiting threads are notified when amount of active actions reaches 0.
 */
class DynamicLatch {

    private volatile int count = 0;

    private volatile int totalAmount = 0;
    private volatile int doneAmount = 0;

    private final Object mutex = new Object();

    void await() {
        synchronized(mutex) {
            if (count == 0) {
                return;
            }
            try {
                mutex.wait();
            } catch (InterruptedException e) { }
        }
    }

    /**
     * Invoked by a new action.
     */
    void acquire() {
        synchronized(mutex) {
            count++;
            totalAmount++;
            print();
        }
    }

    /**
     * Invoked by a finished action.
     */
    void release() {
        synchronized(mutex) {
            count--;
            doneAmount++;
            print();
            if (count == 0) {
                mutex.notifyAll();
            }
        }
    }

    private void print() {
        if (count == 0) System.out.println("\rprocessing finished");
        else System.out.printf("\rprocessed %d of %d", doneAmount, totalAmount);
    }
}
