/**
 * Starter code for MST Algorithms
 * @author Shiva Prasad Reddy Bitla (sxb180066)
 * @author Sudeep Maity (sdm170530)
 * @author Saurav Sharma (sxs179830)
 */

package sxs179830;

import java.util.Comparator;
import java.util.NoSuchElementException;
import java.util.PriorityQueue;

public class BinaryHeap<T extends Comparable<? super T>> {
    Comparable[] pq;
    int size;

    // Constructor for building an empty priority queue using natural ordering of T
    public BinaryHeap(int maxCapacity) {
        pq = new Comparable[maxCapacity];
        size = 0;
    }

    /**
     * Add new element to the priority queue, resizing pq if pq is full
     * @param x element to add to the queue
     * @return true if successfully inserted else returns false
     */
    public boolean add(T x) {
        if(size == pq.length) {
            resize();
        }
        pq[size] = x;
        percolateUp(size);
        size++;
        return true;
    }

    /**
     * Add new element to the priority queue, resizing pq if pq is full
     * @param x element to add to the queue
     * @return true if successfully inserted else returns false
     */
    public boolean offer(T x) {
        return add(x);
    }

    /**
     * Remove highest priority element from the priority queue
     * @return the element if queue is not empty else returns null
     * @throws NoSuchElementException when the queue is empty
     */
    public T remove() throws NoSuchElementException {
        T result = poll();
        if(result == null) {
            throw new NoSuchElementException("Priority queue is empty");
        } else {
            return result;
        }
    }

    /**
     * Remove highest priority element from the priority queue
     * @return the element if queue is not empty else returns null
     */
    public T poll() {
        if(size == 0) return null;
        Comparable<T> min = pq[0];
        if(size > 1) {
            move(0, pq[size-1]);
        }
        size--;
        percolateDown(0);
        return (T) min;
    }

    /**
     * Return the topmost element in the queue i.e. the minimum element
     * @return top most element else null if queue is empty
     */
    public T min() {
        return peek();
    }

    /**
     * Return the topmost element in the queue
     * @return top most element else null if queue is empty
     */
    public T peek() {
        if(size == 0)
            return null;
        else
            return (T) pq[0];
    }

    /**
     * return index of Parent of given node
     * @param i index of the node
     * @return index of Parent
     */
    int parent(int i) {
        return (i-1)/2;
    }

    /**
     * Return the index of left Child of given node
     * @param i index of the node
     * @return index of Left child
     */
    int leftChild(int i) {
        return 2*i + 1;
    }

    /**
     * pq[index] may violate heap order with parent
     * @param index index of the node till where to check for order constraint
     */
    void percolateUp(int index) {
        Comparable x = pq[index];
        while(index > 0 && pq[parent(index)].compareTo(x) > 0) {
            move(index, pq[parent(index)]);
            index = parent(index);
        }
        move(index, x);

    }

    /**
     * pq[i] may violate heap order with children
     * @param index index of the node from where to check for order constraint
     */
    void percolateDown(int index) {
        Comparable x = pq[index];
        int newIndex;
        Comparable smallerChild;
        order_constraint_not_satisfied:
        while(leftChild(index) < size) {
            newIndex = leftChild(index);
            smallerChild = pq[leftChild(index)];
            if(leftChild(index) < size-1 && smallerChild.compareTo(pq[leftChild(index)+1]) > 0) {
                smallerChild = pq[leftChild(index) + 1];
                newIndex++;
            }
            if(x.compareTo(smallerChild) <= 0) {
                break order_constraint_not_satisfied;
            }
            move(index, pq[newIndex]);
            index = newIndex;
        }
        move(index, x);
    }

    /**
     * Assign x to pq[i].  Indexed heap will override this method
     * @param dest index of the node where to replace
     * @param x value to replace with
     */
    void move(int dest, Comparable x) {
        pq[dest] = x;
    }

    int compare(Comparable a, Comparable b) {
        return ((T) a).compareTo((T) b);
    }

    /** Create a heap.  Precondition: none. */
    void buildHeap() {
        for(int i=parent(size-1); i>=0; i--) {
            percolateDown(i);
        }
    }

    public boolean isEmpty() {
        return size() == 0;
    }

    public int size() {
        return size;
    }

    /**
     * Resize array to double the current size
     */
    void resize() {
        Comparable[] newPQ = new Comparable[size*2];
        System.arraycopy(pq, 0, newPQ, 0, size);
        pq = newPQ;
    }

    public interface Index {
        public void putIndex(int index);
        public int getIndex();
    }

    public static class IndexedHeap<T extends Index & Comparable<? super T>> extends BinaryHeap<T> {
        /** Build a priority queue with a given array */
        IndexedHeap(int capacity) {
            super(capacity);
        }

        /** restore heap order property after the priority of x has decreased */
        void decreaseKey(T x) {
            int index = -1;
            for(int i = 0; i < size; i++) {
                if(pq[i].equals(x)) {
                    index = i;
                    break;
                }
            }
            if(index > -1)
                percolateUp(index);
        }

        @Override
        void move(int i, Comparable x) {
            super.move(i, x);
        }
    }

    public static void main(String[] args) {
        Integer[] arr = {0,9,7,5,3,1,8,6,4,2};
        BinaryHeap<Integer> h = new BinaryHeap(arr.length);

        System.out.print("Before:");
        for(Integer x: arr) {
            h.offer(x);
            System.out.print(" " + x);
        }
        System.out.println();

        for(int i=0; i<arr.length; i++) {
            arr[i] = h.poll();
        }

        System.out.print("After :");
        for(Integer x: arr) {
            System.out.print(" " + x);
        }
        System.out.println();
    }
}
