import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

import edu.princeton.cs.algs4.SET;

public class BoggleSolver
{
    private static final int R = 26;      // A-Z

    private boolean[] marked;
    private char[] board;
    private int rows;
    private int cols;
    private Cube[] adj;
    private Node root;

    private static class Node {
        private boolean isWord;
        private Node[] next = new Node[R];
    } 

    
    private static class Cube {
        private int n = 0;
        private int[] neighbor = new int[8];
    }

    // Initializes the data structure using the given array of strings as the dictionary.
    // (You can assume each word in the dictionary contains only the uppercase letters A through Z.)
    public BoggleSolver(String[] dictionary) {
        if (dictionary == null)
           throw new IllegalArgumentException("the argument to BoggleSolver() is null\n");

        for (int i = 0; i < dictionary.length; i++) 
            addToTrie(dictionary[i]); 
    }

    private void addToTrie(String word) {
        if (word == null)
            throw new IllegalArgumentException("argument to addToTrie() is null");

        root = add(root, word, 0);
    }

    private Node add(Node x, String word, int d) {
        if (x == null) x = new Node();
        if (d == word.length()) x.isWord = true;
        else {
            char c = word.charAt(d);
            x.next[c - 'A'] = add(x.next[c - 'A'], word, d + 1);
        }  
        return x;
    }

    private boolean contains(String word) {
        if (word == null) 
            throw new IllegalArgumentException("argument to contains() is null");

        Node x = get(root, word, 0);
        if (x == null) return false;
        return x.isWord;
    }

    private Node get(Node x, String word, int d) {
        if (x == null) return null;
        if (d == word.length()) return x;
        char c = word.charAt(d);
        return get(x.next[c - 'A'], word, d + 1);
    }

    // Returns the set of all valid words in the given Boggle board, as an Iterable.
    public Iterable<String> getAllValidWords(BoggleBoard board) {
        if (board == null)
            throw new IllegalArgumentException("the argument to getAllValidWords() is null\n");
            
        if (rows != board.rows() || cols != board.cols()) {
            rows = board.rows();
            cols = board.cols();
            marked = new boolean[rows * cols]; 
            this.board = new char[rows * cols];
            precomputeAdj();
        }
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                int idx = i * cols + j;
                char c = board.getLetter(i, j);
                this.board[idx] = c;
            }
        }
         
        SET<String> allValidWords = DFS();
        return allValidWords;
    }

    // Returns the score of the given word if it is in the dictionary, zero otherwise.
    // (You can assume the word contains only the uppercase letters A through Z.)
    public int scoreOf(String word) {
        if (word == null)
            throw new IllegalArgumentException("the argument to scoreOf() is null\n");
            
        if (!contains(word)) return 0;
        else if (word.length() < 3) return 0;
        else if (word.length() < 5) return 1;
        else if (word.length() == 5) return 2;
        else if (word.length() == 6) return 3;
        else if (word.length()  == 7) return 5;
        else return 11;
    }

    private SET<String> DFS() {
        SET<String> words = new SET<String>();
        for (int i = 0; i < rows * cols; i++) 
            DFS(i, new StringBuilder(), words, root);
        return words;
    }

    private void DFS(int idx, StringBuilder pre, SET<String> words, Node n) {
        char c = board[idx];
        Node next = n.next[c - 'A'];
        if (c == 'Q' && next != null) 
            next = next.next['U' - 'A'];
        if (next == null) return;

        if (c == 'Q') pre.append("QU");
        else pre.append(c);
        String str = pre.toString();
        if (pre.length() > 2 && next.isWord) 
            words.add(str);
         
        marked[idx] = true;
        for (int k = 0; k < adj[idx].n; k++) {
            int nextIdx = adj[idx].neighbor[k];
            if (!marked[nextIdx])
                DFS(nextIdx, new StringBuilder(pre), words, next);
        }     
        marked[idx] = false;
    }

    private void precomputeAdj() {
        adj =  new Cube[rows * cols];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                int idx = i * cols + j;
                adj[idx] = new Cube();
                if (i > 0) {
                    // 正上
                    adj[idx].neighbor[adj[idx].n++] = (i - 1) * cols + j;
                    // 右上
                    if (j < cols - 1)
                        adj[idx].neighbor[adj[idx].n++] = (i - 1) * cols + j + 1;
                }
                if (i < rows - 1) {
                    // 正下
                    adj[idx].neighbor[adj[idx].n++] = (i + 1) * cols + j;
                    // 左下
                    if (j > 0)
                        adj[idx].neighbor[adj[idx].n++] = (i + 1) * cols + j - 1;                  
                }
                if (j > 0) {
                    // 左边
                    adj[idx].neighbor[adj[idx].n++] = i * cols + j - 1;
                    // 左上
                    if (i > 0) 
                        adj[idx].neighbor[adj[idx].n++] = (i - 1) * cols + j - 1;      
                }
                if (j < cols - 1) {
                    // 右边
                    adj[idx].neighbor[adj[idx].n++] = i * cols + j + 1;
                    // 右下
                    if (i < rows - 1)
                        adj[idx].neighbor[adj[idx].n++] = (i + 1) * cols + j + 1;      
                }
            }
        }
    }

    public static void main(String[] args) {
        In in = new In(args[0]);
        String[] dictionary = in.readAllStrings();
        BoggleSolver solver = new BoggleSolver(dictionary);
        BoggleBoard board = new BoggleBoard(args[1]);
        int score = 0;
        for (String word : solver.getAllValidWords(board)) {
            StdOut.println(word);
            score += solver.scoreOf(word);
        }
        StdOut.println("Score = " + score);  
    }  

}