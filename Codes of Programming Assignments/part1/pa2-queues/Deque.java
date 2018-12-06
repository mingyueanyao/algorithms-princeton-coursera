import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class Deque<Item> implements Iterable<Item> {
    private int n;                // number of elements on deque
    private Node<Item> header;    // sentinel node in the beginning
    private Node<Item> trailer;   // sentinel node in the end
    
    // helper linked list class
    private static class Node<Item> {
        Item item;
        Node<Item> prev;
        Node<Item> next;
    }

    // construct an empty deque
    public Deque() {
        n = 0;
        header = new Node<Item>();
        trailer = new Node<Item>();

        header.item = null;
        trailer.item = null;
        header.prev = null;
        trailer.prev = header;
        header.next = trailer;
        trailer.next = null;
    }     
    
    // is the deque empty?
    public boolean isEmpty() {
        return n == 0;
    }     
    
    // return the number of items on the deque
    public int size() {
        return n;
    }   
    
    // add the item to the front
    public void addFirst(Item item) {
        if (item == null) throw new IllegalArgumentException("can not add null");
        Node<Item> oldfirst = header.next;
        Node<Item> first = new Node<Item>();
        first.item = item;
        first.prev = header;
        first.next = oldfirst;
        header.next = first;
        oldfirst.prev = first;
        n++;
    }       
    
    // add the item to the end
    public void addLast(Item item) {
        if (item == null) throw new IllegalArgumentException("can not add null");
        Node<Item> oldlast = trailer.prev;
        Node<Item> last = new Node<Item>();
        last.item = item;
        last.prev = oldlast;
        last.next = trailer;
        trailer.prev = last;
        oldlast.next = last;
        n++;
    }  

    // remove and return the item from the front       
    public Item removeFirst() {
        if (isEmpty()) throw new NoSuchElementException("Deque underflow");
        Node<Item> first = header.next;
        Node<Item> newfirst = first.next;
        Item item = first.item;
        header.next = newfirst;
        newfirst.prev = header;
        n--;
        return item;
    }     
    
    
    // remove and return the item from the end
    public Item removeLast() {
        if (isEmpty()) throw new NoSuchElementException("Deque underflow");
        Node<Item> last = trailer.prev;
        Node<Item> newlast = last.prev;
        Item item = last.item;
        trailer.prev = newlast;
        newlast.next = trailer;
        n--;
        return item;
    }
    
    // return an iterator over items in order from front to end
    public Iterator<Item> iterator() {
        return new ListIterator<Item>(header);
    }

    private class ListIterator<Item> implements Iterator<Item> {
        private Node<Item> current;

        public ListIterator(Node<Item> header) {
            current = header.next;
        }

        public boolean hasNext() {
            return current != trailer;
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }

        public Item next() {
            if (!hasNext()) throw new NoSuchElementException();
            Item item = current.item;
            current = current.next;
            return item;
        }
    }

    // unit testing (optional)
    public static void main(String[] args) {
        Deque<String> deque = new Deque<String>();
        while (!StdIn.isEmpty()) {
            String item = StdIn.readString();
            deque.addFirst(item);
        }
        StdOut.println("size of deque = " + deque.size());
        for (String s : deque) {
            StdOut.println(s);
        }
    }

 }