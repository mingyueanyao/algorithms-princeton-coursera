import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.Digraph;

public class SAP {
    private int length;               // length of the shortest path between V and W
    private int ancestor;             // the nearest ancestor of V and W
    private Digraph copyG;            // save the copy of associated digraph
    private int[] distTo1;            // distTo1[v] = length of shortest V->v path
    private int[] distTo2;            // distTo2[v] = length of shortest W->v path
    private boolean[] marked1;        // marked1[v] = is there an V->v path?
    private boolean[] marked2;        // marked2[v] = is there an W->v path?
    private Stack<Integer> stack1;    // store changed auxiliary array1 entries
    private Stack<Integer> stack2;    // store changed auxiliary array1 entries

    // constructor takes a digraph (not necessarily a DAG)
    public SAP(Digraph G) {
        if (G == null) {
            throw new IllegalArgumentException("argument to SAP() is null");
        }
        copyG = new Digraph(G);
        distTo1 = new int[G.V()];
        distTo2 = new int[G.V()];
        marked1 = new boolean[G.V()];
        marked2 = new boolean[G.V()];
        stack1 = new Stack<Integer>();
        stack2 = new Stack<Integer>();
    }

    // length of shortest ancestral path between v and w; -1 if no such path
    public int length(int v, int w) {
        validateVertex(v);
        validateVertex(w);
        compute(v, w);
        return length;   
    }

    // a common ancestor of v and w that participates in a shortest ancestral path;
    // -1 if no such path
    public int ancestor(int v, int w) {
        validateVertex(v);
        validateVertex(w);    
        compute(v, w);
        return ancestor;
    }

    // length of shortest ancestral path between any vertex in v and any vertex in
    // w; -1 if no such path
    public int length(Iterable<Integer> v, Iterable<Integer> w) {
        validateVertices(v);
        validateVertices(w);
        compute(v, w);
        return length;
    }

    // a common ancestor that participates in shortest ancestral path; -1 if no such
    // path
    public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
        validateVertices(v);
        validateVertices(w);
        compute(v, w);
        return ancestor;
    }

    // using two bfs lockstep from v and w to compute sap
    private void compute(int v, int w) { 
        length = -1;
        ancestor = -1;
        distTo1[v] = 0;
        distTo2[w] = 0;
        marked1[v] = true;
        marked2[w] = true;
        stack1.push(v);
        stack2.push(w);
        Queue<Integer> q1 = new Queue<Integer>();
        Queue<Integer> q2 = new Queue<Integer>();
        q1.enqueue(v);
        q2.enqueue(w); 
        bfs(q1, q2);
    }

    // using two bfs lockstep from sources v and sources w to compute sap
    private void compute(Iterable<Integer> v, Iterable<Integer> w) {
        length = -1;
        ancestor = -1;
        Queue<Integer> q1 = new Queue<Integer>();
        Queue<Integer> q2 = new Queue<Integer>();
        for (int v1 : v) {
            marked1[v1] = true;
            stack1.push(v1);
            distTo1[v1] = 0;
            q1.enqueue(v1);
        }
        for (int w1 : w) {
            marked2[w1] = true;
            stack2.push(w1);
            distTo2[w1] = 0;
            q2.enqueue(w1);
        }
        bfs(q1, q2);
    }

    // run two bfs alternating back and forth bewteen q1 and q2
    private void bfs(Queue<Integer> q1, Queue<Integer> q2) {
        while (!q1.isEmpty() || !q2.isEmpty()) {
            if (!q1.isEmpty()) {
                int v = q1.dequeue();
                if (marked2[v]) {
                    if (distTo1[v] + distTo2[v] < length || length == -1) {
                        ancestor = v;
                        length = distTo1[v] + distTo2[v];
                    }
                }
                // stop adding new vertex to queue if the distance exceeds the length
                if (distTo1[v] < length || length == -1) {
                    for (int w : copyG.adj(v)) {
                        if (!marked1[w]) {
                            distTo1[w] = distTo1[v] + 1;
                            marked1[w] = true;
                            stack1.push(w);
                            q1.enqueue(w);

                            // StdOut.println("push " + w + " into q1");
                        }
                    }
                }
            }
            if (!q2.isEmpty()) {
                int v = q2.dequeue();
                if (marked1[v]) {
                    if (distTo1[v] + distTo2[v] < length || length == -1) {
                        ancestor = v;
                        length = distTo1[v] + distTo2[v];
                    }
                }
                // stop adding new vertex to queue if the distance exceeds the length
                if (distTo2[v] < length || length == -1) {
                    for (int w : copyG.adj(v)) {
                        if (!marked2[w]) {
                            distTo2[w] = distTo2[v] + 1;
                            marked2[w] = true;
                            stack2.push(w);
                            q2.enqueue(w);

                            // StdOut.println("push " + w + " into q2");
                        }
                    }
                }
            }
        }
        init();    // reinitialize auxiliary array for next bfs
    }

    // init auxiliary array for bfs
    private void init() {
        while (!stack1.isEmpty()) {
            int v = stack1.pop();
            marked1[v] = false;
        }
        while (!stack2.isEmpty()) {
            int v = stack2.pop();
            marked2[v] = false;
        }
    }

    // throw an IllegalArgumentException unless {@code 0 <= v < V}
    private void validateVertex(int v) {
        int V = marked1.length;
        if (v < 0 || v >= V)
            throw new IllegalArgumentException("vertex " + v + " is not between 0 and " + (V - 1));
    }

    // throw an IllegalArgumentException unless {@code 0 <= v < V}
    private void validateVertices(Iterable<Integer> vertices) {
        if (vertices == null) {
            throw new IllegalArgumentException("argument is null");
        }
        int V = marked1.length;
        for (int v : vertices) {
            if (v < 0 || v >= V) {
                throw new IllegalArgumentException("vertex " + v + " is not between 0 and " + (V - 1));
            }
        }
    }

    // do unit testing of this class
    public static void main(String[] args) {
        In in = new In(args[0]);
        Digraph G = new Digraph(in);
        SAP sap = new SAP(G);
        while (!StdIn.isEmpty()) {
            int v = StdIn.readInt();
            int w = StdIn.readInt();
            int length = sap.length(v, w);
            int ancestor = sap.ancestor(v, w);
            StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);
        }
    }
}