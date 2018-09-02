import java.util.Arrays;
import java.util.Comparator;

public class CircularSuffixArray {
    private int length;
    private char[] value;
    private Integer[] index;
    
    // circular suffix array of s
    public CircularSuffixArray(String s) {
        if (s == null)
            throw new IllegalArgumentException("the argument to CircularSuffixArray() is null");
        
        length = s.length();
        value = new char[length];
        index = new Integer[length];
        for (int i = 0; i < length; i++) {
            index[i] = i;
            value[i] = s.charAt(i);
        }
        
        Arrays.sort(index, new Comparator<Integer>() {
            public int compare(Integer idx1, Integer idx2) {
                for (int i = 0; i < length; i++) {
                    char c1 = value[(i + idx1) % length];
                    char c2 = value[(i + idx2) % length];
                    if (c1 > c2) return 1;
                    if (c1 < c2) return -1;
                }
                return 0;
            } 
        });
    }

    // length of s
    public int length() {
        return length;
    }   
    
    // returns index of ith sorted suffix
    public int index(int i) {
        if (i < 0 || i >= length)
            throw new IllegalArgumentException("the argument to index() is outside 0 and n - 1");
        return index[i];
    }   
    
    // unit testing (required)
    public static void main(String[] args) {
        CircularSuffixArray csa = new CircularSuffixArray("ABRACADABRA!");
        for (int i = 0; i < csa.length(); i++)
            System.out.print(csa.index(i) + " ");
    }
 }