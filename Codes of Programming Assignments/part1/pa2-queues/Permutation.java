import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

public class Permutation {
    public static void main(String[] args) {
        int k = Integer.parseInt(args[0]);
        RandomizedQueue<String> q = new RandomizedQueue<String>();
        for (int i = 0; i < k; i++) {
            q.enqueue(StdIn.readString());
        }
        int n = k;
        while (!StdIn.isEmpty()) {
            String item = StdIn.readString();
            if (StdRandom.uniform(++n) < k) {
                q.dequeue();
                q.enqueue(item);
            }
        }
        for (String s : q) {
            StdOut.println(s);
        }
    }
    
 }