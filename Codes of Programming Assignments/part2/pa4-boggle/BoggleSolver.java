import edu.princeton.cs.algs4.In; 
import edu.princeton.cs.algs4.SET;

import edu.princeton.cs.algs4.StdOut;    // for testing

public class BoggleSolver
{
    private SET<String> dictionary;

    // Initializes the data structure using the given array of strings as the dictionary.
    // (You can assume each word in the dictionary contains only the uppercase letters A through Z.)
    public BoggleSolver(String[] dictionary) {
        if (dictionary == null)
           throw new IllegalArgumentException("the argument to BoggleSolver() is null\n");

        this.dictionary = new SET<String>();
        for (int i = 0; i < dictionary.length; i++)
            this.dictionary.add(dictionary[i]);
    }

    private void DFS(BoggleBoard board) {
        for (int i = 0; i < boards.rows(); i++)
            for (int j = 0; j < boards.cols(); j++)
                DFS(board, i, j);
    }

    private void DFS(BoggleBoard board, int i, int j) {
         
    }

    /*
    // TODO
    // Returns the set of all valid words in the given Boggle board, as an Iterable.
    public Iterable<String> getAllValidWords(BoggleBoard board) {
        SET<String> allValidWords = new SET<String>();
        return allValidWords;
    }

    // Returns the score of the given word if it is in the dictionary, zero otherwise.
    // (You can assume the word contains only the uppercase letters A through Z.)
    public int scoreOf(String word)
    */

    // test
    public void testDictionary() {
        StdOut.println("the sixe of dictionary: " + dictionary.size());
        StdOut.println(dictionary.toString());
    }

    public static void main(String[] args) {
        In in = new In(args[0]);
        String[] dictionary = in.readAllStrings();
        BoggleSolver solver = new BoggleSolver(dictionary);
        solver.testDictionary();
    }
}