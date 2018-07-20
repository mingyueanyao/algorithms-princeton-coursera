import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

import edu.princeton.cs.algs4.ST;
import edu.princeton.cs.algs4.SET;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.Topological;

public class WordNet {
    private ST<String, SET<Integer>> synsets;    // noun and ids contain the noun
    private ST<Integer, String> id_nouns;
    private Digraph hypernyms;
    private boolean[] outEdge;                   // is the vertex has a edge out?
    private int outSum;                          // the number of vertices having a out edge
    private int idSum;
    private SAP sap;
    
    // constructor takes the name of the two input files
    public WordNet(String synsets, String hypernyms) {
        if (synsets == null || hypernyms == null) {
            throw new IllegalArgumentException("arguments to WordNet() is null");
        }

        readSynsets(synsets);
        readHypernyms(hypernyms);     
    }
 
    // build ST<String, SET<Integer>> synsets
    private void readSynsets(String synsets) {
        this.synsets = new ST<String, SET<Integer>>();
        id_nouns = new ST<Integer, String>();
        In synset = new In(synsets);
        while (synset.hasNextLine()) {
            idSum++;
            String str = synset.readLine();
            String[] fields = str.split(",");
            int id = Integer.parseInt(fields[0]);
            id_nouns.put(id, fields[1]);
            String[] nouns = fields[1].split(" ");
            for (int i = 0; i < nouns.length; i++) {
                if (this.synsets.contains(nouns[i])) {
                    this.synsets.get(nouns[i]).add(id);
                }
                else {
                    SET<Integer> ids = new SET<Integer>();
                    ids.add(id);
                    this.synsets.put(nouns[i], ids);
                }
            }
        }
    }

    // build Digraph hypernyms
    private void readHypernyms(String hypernyms) {
        this.hypernyms = new Digraph(idSum);
        In hypernym = new In(hypernyms);
        outEdge = new boolean[idSum];
        while (hypernym.hasNextLine()) {
            String str = hypernym.readLine();
            String[] fields = str.split(",");
            int v = Integer.parseInt(fields[0]);
            for (int i = 1; i < fields.length; i++) {
                int w = Integer.parseInt(fields[i]);
                this.hypernyms.addEdge(v, w);
            }
            if (!outEdge[v] && fields.length != 1) {
                outSum++;
            }
            outEdge[v] = true;
        }
        isRootedDAG();
        sap = new SAP(this.hypernyms);
    }

    private void isRootedDAG() {
        if (idSum - outSum != 1) {
            throw new IllegalArgumentException("is not a one root digraph"); 
        }
        Topological topo = new Topological(hypernyms);
        if (!topo.hasOrder()) {
            throw new IllegalArgumentException("is not a DAG"); 
        }
    }

    // returns all WordNet nouns
    public Iterable<String> nouns() {
        return synsets.keys();
    }
 
    // is the word a WordNet noun?
    public boolean isNoun(String word) {
        if (word == null) {
            throw new IllegalArgumentException("arguments to isNoun() is null"); 
        }
        return synsets.contains(word);
    }
 
    // distance between nounA and nounB (defined below)
    public int distance(String nounA, String nounB) {
        if (nounA == null || nounB == null) {
            throw new IllegalArgumentException("arguments to distance() is null"); 
        }
        if (!isNoun(nounA) || !isNoun(nounB)) {
            throw new IllegalArgumentException("arguments to distance() is not a WordNet noun"); 
        }
        SET<Integer> setA = synsets.get(nounA);
        SET<Integer> setB = synsets.get(nounB);
        if (setA.size() == 1 && setB.size() == 1) {
            return sap.length(setA.max(), setB.max());
        } else {
            return sap.length(setA, setB);
        }
    }
 
    // a synset (second field of synsets.txt) that is the common ancestor of nounA and nounB
    // in a shortest ancestral path (defined below)
    public String sap(String nounA, String nounB) {
        if (nounA == null || nounB == null) {
            throw new IllegalArgumentException("arguments to sap() is null"); 
        }
        if (!isNoun(nounA) || !isNoun(nounB)) {
            throw new IllegalArgumentException("arguments to sap() is not a WordNet noun"); 
        }
        int id;
        SET<Integer> setA = synsets.get(nounA);
        SET<Integer> setB = synsets.get(nounB);
        if (setA.size() == 1 && setB.size() == 1) {
            id = sap.ancestor(setA.max(), setB.max());
        } else {
            id = sap.ancestor(setA, setB);
        }
        return id_nouns.get(id);
    }
 
    // do unit testing of this class
    public static void main(String[] args) {}
 }