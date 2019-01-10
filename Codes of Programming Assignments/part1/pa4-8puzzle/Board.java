import edu.princeton.cs.algs4.Stack;

public class Board {
    private int n;          // board dimension
    private int hd;         // Hamming distance
    private int md;         // Manhattan distance
    private int x, y;       // tiles[x][y] = 0
    private int[][] tiles;  // the copy of blocks

    // construct a board from an n-by-n array of blocks
    // (where blocks[i][j] = block in row i, column j)
    public Board(int[][] blocks) {
        if (blocks == null) {
            throw new IllegalArgumentException("bolcks == null");
        }    

        hd = 0;
        md = 0;
        n = blocks.length;
        tiles = new int[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n ; j++) {
                tiles[i][j] = blocks[i][j];
                if (tiles[i][j] == 0) {
                    x = i;
                    y = j;
                    continue;
                }
                if (tiles[i][j] != (i * n + j + 1)) hd++;
                int p = (tiles[i][j] - 1) / n;
                int q = (tiles[i][j] - 1) % n;
                md += (Math.abs(i - p) + Math.abs(j - q));
            }
        }
    }   
    
    // board dimension n
    public int dimension() {
        return n;
    }        
    
    // number of blocks out of place
    public int hamming() {
        return hd;
    }   
    
     // sum of Manhattan distances between blocks and goal
    public int manhattan() {
        return md;
    }      
    
    // is this board the goal board?
    public boolean isGoal() {
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (tiles[i][j] != (i * n + j + 1)) {
                    if (tiles[i][j] == 0) continue;
                    return false;
                }
            }
        }
        return true;
    } 

    // a board that is obtained by exchanging any pair of blocks     
    public Board twin() {
        int[][] copy = new int[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {      
                copy[i][j] = tiles[i][j];
            }
        }

        if (copy[0][0] == 0) swap(copy, 0, 1, 1, 1);
        else if (copy[0][1] == 0) swap(copy, 0, 0, 1, 0);
        else swap(copy, 0, 0, 0, 1);

        Board twinBoard = new Board(copy);
        return twinBoard;
    }  

     // does this board equal other?     
    public boolean equals(Object other) {
        if (other == this) return true;
        if (other == null) return false;
        if (other.getClass() != this.getClass()) return false;

        Board that = (Board) other;
        if (that.n != this.n) return false;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (that.tiles[i][j] != this.tiles[i][j]) {
                    return false;
                }
            }
        }
        return true;
    }      
      
    // all neighboring boards
    public Iterable<Board> neighbors() {
        Stack<Board> neighborBoards = new Stack<Board>();
        if (x > 0) neighborBoards.push(moveBlank(x - 1, y));
        if (x < n - 1) neighborBoards.push(moveBlank(x + 1, y));
        if (y > 0) neighborBoards.push(moveBlank(x, y - 1));
        if (y < n - 1) neighborBoards.push(moveBlank(x, y + 1));
        return neighborBoards;
    }   

    // string representation of this board 
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append(n + "\n");
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                s.append(String.format("%2d ", tiles[i][j]));
            }
            s.append("\n");
        }
        return s.toString();
    }               

    private void swap(int[][] arr, int i1, int j1, int i2, int j2) {
        int temp = arr[i1][j1];
        arr[i1][j1] = arr[i2][j2];
        arr[i2][j2] = temp;
    }

     private Board moveBlank(int p, int q) {
        int[][] copy = new int[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {      
                copy[i][j] = tiles[i][j];
            }
        }

        swap(copy, p, q, x, y);
        Board newBoard = new Board(copy);
        return newBoard;
    }  
    
    // unit tests (not graded)
    public static void main(String[] args) { }
}