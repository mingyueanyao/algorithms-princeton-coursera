import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.StdOut;

import java.util.Comparator;

public class Solver {
    private int moves;              // min number of moves to solve initial board
    private boolean solvable;       // is the initial board solvable?
    private Stack<Board> solution;  // sequence of boards in a shortest solution

    // search node
    private static class Node {
        Board board;
        int moves;
        int priority;
        Node pred;
    }

    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial) {
        if (initial == null) {
            throw new IllegalArgumentException("initial == null\n");
        }

        moves = -1;
        solvable = true;
        solution = new Stack<Board>(); 
        MinPQ<Node> pq = init(initial);
        MinPQ<Node> twinpq = init(initial.twin());
 
        while (true) {
            Node searchNode = pq.delMin();
            Node predNode = searchNode.pred;
            Board searchBoard = searchNode.board;

            if (searchBoard.isGoal()) {
                solvable = true;
                moves = searchNode.moves;
                solution = findRoot(searchNode);
                break;
            }

            for (Board b : searchBoard.neighbors()) {
                if (predNode == null) {
                    Node temp = new Node();
                    temp.pred = searchNode;
                    temp.board = b;
                    temp.moves = searchNode.moves + 1;
                    temp.priority = temp.moves + b.manhattan();
                    pq.insert(temp);
                } else {
                    if (!predNode.board.equals(b)) {
                        Node temp = new Node();
                        temp.pred = searchNode;
                        temp.board = b;
                        temp.moves = searchNode.moves + 1;
                        temp.priority = temp.moves + b.manhattan();
                        pq.insert(temp);
                    }
                }
            }

            Node twinSearchNode = twinpq.delMin();
            Node twinPredNode = twinSearchNode.pred;
            Board twinSearchBoard = twinSearchNode.board;

            if (twinSearchBoard.isGoal()) {
                solvable = false;
                break;
            }

            for (Board b : twinSearchBoard.neighbors()) {
                if (twinPredNode == null) {
                    Node temp = new Node();
                    temp.pred = twinSearchNode;
                    temp.board = b;
                    temp.moves = twinSearchNode.moves + 1;
                    temp.priority = temp.moves + b.manhattan();
                    twinpq.insert(temp);
                } else {
                    if (!twinPredNode.board.equals(b)) {
                        Node temp = new Node();
                        temp.pred = twinSearchNode;
                        temp.board = b;
                        temp.moves = twinSearchNode.moves + 1;
                        temp.priority = temp.moves + b.manhattan();
                        twinpq.insert(temp);
                    }
                }
            }
        }
    }          

    // is the initial board solvable?
    public boolean isSolvable() {
        return solvable;
    }           

    // min number of moves to solve initial board; -1 if unsolvable
    public int moves() {
        return moves;
    }                     

    // sequence of boards in a shortest solution; null if unsolvable
    public Iterable<Board> solution() {
        if (!solvable) return null;
        return solution;
    } 
    
    private Comparator<Node> priority() {
        return new ByPriority();
    }

    private class ByPriority implements Comparator<Node> {
        public int compare(Node n1, Node n2) {
            if (n1.priority > n2.priority) return +1;
            if (n1.priority < n2.priority) return -1;
            return 0;
        }
    }
    
    private MinPQ<Node> init(Board root) {
        Node rootNode = new Node();
        rootNode.board = root;
        rootNode.moves = 0;
        rootNode.priority = root.manhattan();
        rootNode.pred = null;
        MinPQ<Node> pq = new MinPQ<Node>(priority());
        pq.insert(rootNode); 
        return pq;
    }

    private Stack<Board> findRoot(Node leaf) {
        Stack<Board> path = new Stack<Board>();
        Node temp = leaf;
        do {
            path.push(temp.board);
            temp = temp.pred;
        } while (temp != null);
        return path;
    }

    // solve a slider puzzle (given below)
    public static void main(String[] args) {
        // create initial board from file
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] blocks = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                blocks[i][j] = in.readInt();
        Board initial = new Board(blocks);

        // solve the puzzle
        Solver solver = new Solver(initial);

        // print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
                StdOut.println(board);
        } 
    } 
}