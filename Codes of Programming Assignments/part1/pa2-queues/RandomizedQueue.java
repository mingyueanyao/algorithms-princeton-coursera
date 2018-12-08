import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class RandomizedQueue<Item> implements Iterable<Item> {
    private Item[] q;        // queue elements
    private int n;           // number of elements on queue

    // construct an empty randomized queue
    public RandomizedQueue() {
        q = (Item[]) new Object[2];
        n = 0;
    }    

    // is the randomized queue empty?            
    public boolean isEmpty() {
        return n == 0;
    }   

    // return the number of items on the randomized queue           
    public int size() {
        return n;
    }   

    private void resize(int capacity) {
        assert capacity >= n;
        Item[] temp = (Item[]) new Object[capacity];
        for (int i = 0; i < n; i++) {
            temp[i] = q[i];
        }
        q = temp;
    }

    // add the item                  
    public void enqueue(Item item) {
        if (item == null) throw new IllegalArgumentException("can not add null to queue");
        if (n == q.length) resize(2*q.length);
        q[n++] = item;
    }   

    // remove and return a random item       
    public Item dequeue() {
        if (isEmpty()) throw new NoSuchElementException("Queue underflow");
        int r = StdRandom.uniform(n);
        Item item = q[r];
        q[r] = q[n - 1];
        q[n - 1] = null;
        n--;
        if (n > 0 && n == q.length/4) resize(q.length/2);
        return item;
    }  

    // return a random item (but do not remove it)                 
    public Item sample() {
        if (isEmpty()) throw new NoSuchElementException("Queue underflow");
        int r = StdRandom.uniform(n);
        Item item = q[r];
        return item;
    }

    // return an independent iterator over items in random order                 
    public Iterator<Item> iterator() {
        return new RandomIterator();
    }  

    private class RandomIterator implements Iterator<Item> {
        private int current;
        private int[] randomIdx;

        public RandomIterator() {
            current = 0;
            randomIdx = new int[n];
            for (int i = 0; i < n; i++) {
                randomIdx[i] = i;
            }
            StdRandom.shuffle(randomIdx);
        }

        public boolean hasNext() {
            return current != n;
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }

        public Item next() {
            if (!hasNext()) throw new NoSuchElementException();
            return q[randomIdx[current++]];
        }
    }

    // unit testing (optional)      
    public static void main(String[] args) {
        RandomizedQueue<String> q = new RandomizedQueue<String>();
        while (!StdIn.isEmpty()) {
            String item = StdIn.readString();
            q.enqueue(item);
        }
        for (String s : q) {
            StdOut.print(s + " ");
        }
        StdOut.println();
        for (String s : q) {
            StdOut.print(s + " ");
        }
        StdOut.println();
        for (String s : q) {
            StdOut.print(s + " ");
        }
        StdOut.println();
    } 

 }