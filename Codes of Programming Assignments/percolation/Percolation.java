import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    private WeightedQuickUnionUF backwashUF;
    private WeightedQuickUnionUF uf;
    private byte[][] grid;    // 0 means block , 1 means open
    private int count;       // the number of opened sites
    private int size;

    // create n-by-n grid, with all sites blocked
    public Percolation(int n) {
        if (n <= 0) {
            throw new IllegalArgumentException("Error: n <= 0");
        }
        backwashUF =  new WeightedQuickUnionUF(n * n + 1);
        uf = new WeightedQuickUnionUF(n * n + 2);
        grid = new byte[n][n];
        count = 0;
        size = n;
    }    
    
    private void validata(int n) {
        int tmp = size;
        if (n <= 0 || n > tmp) {
            throw new IllegalArgumentException("row or col is not between 1 and " + tmp);
        }
    }

    private int xyTo1D(int row, int col) {
        return (row - 1) * size + col;
    }

    // open site (row, col) if it is not open already
    public void open(int row, int col) {
        if (isOpen(row, col)) return;
        grid[row - 1][col - 1] = 1;    // mark the site open
        count++;
        
        // mark the site full 
        int p = xyTo1D(row, col);
        if (row == 1) {
            uf.union(p, 0);  
            backwashUF.union(p, 0);
        }     
        if (row == size) uf.union(p, size * size + 1);  

        int q;
        if (row > 1) {
            if (isOpen(row - 1, col)) {
                q = xyTo1D(row - 1, col);
                uf.union(p, q);
                backwashUF.union(p, q);
            }        
        }
        if (row < size) {
            if (isOpen(row + 1, col)) {
                q = xyTo1D(row + 1, col);
                uf.union(p, q);
                backwashUF.union(p, q);
            }        
        }
        if (col > 1) {
            if (isOpen(row, col - 1)) {
                q = xyTo1D(row, col - 1);
                uf.union(p, q);
                backwashUF.union(p, q);
            }        
        }
        if (col < size) {
            if (isOpen(row, col + 1)) {
                q = xyTo1D(row, col + 1);
                uf.union(p, q);
                backwashUF.union(p, q);
            }        
        }
    }  

    // is site (row, col) open?
    public boolean isOpen(int row, int col) {
        validata(row);
        validata(col);
        return grid[row - 1][col -1] == 1;
    }  

    // is site (row, col) full?
    public boolean isFull(int row, int col) {
        if (!isOpen(row, col)) return false;
        int p = xyTo1D(row, col);
        return uf.connected(p, 0) && backwashUF.connected(p, 0);
    }  

    // number of open sites
    public int numberOfOpenSites() {
        return count;
    }    

    // does the system percolate?  
    public boolean percolates() {
        return uf.connected(0, size * size + 1);
    }              
 
    // test client (optional)
    public static void main(String[] args) {

    }  
 }