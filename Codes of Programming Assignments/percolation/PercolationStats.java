import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdStats;
import edu.princeton.cs.algs4.StdRandom;

public class PercolationStats {
    private static final double FRACTILE = 1.96;    // 95% confidence interval

    private int size;
    private int times;
    private double mean;
    private double stddev;
    private double[] data;
    
     // perform trials independent experiments on an n-by-n grid
     public PercolationStats(int n, int trials) {
        if (n <= 0 || trials <= 0) {
            throw new IllegalArgumentException("Error: n or T <= 0");
        }

        size = n;
        times = trials;
        data = new double[trials];
        for (int i = 0; i < trials; i++) {
            Percolation perc = new Percolation(n);
            while (!perc.percolates()) {
                int row = getRandom();
                int col = getRandom();
                perc.open(row, col);
            }
            data[i] = ((double) perc.numberOfOpenSites()) / (size * size);
        }
        mean = StdStats.mean(data);
        stddev = StdStats.stddev(data);
    }    

    private int getRandom() {
        return StdRandom.uniform(0, size) + 1;
    }

    // sample mean of percolation threshold
    public double mean() {
        return mean;
    }          
    
    // sample standard deviation of percolation threshold
    public double stddev() {
        return stddev;
    }                     

     // low  endpoint of 95% confidence interval
    public double confidenceLo() {
        double tmp = FRACTILE * stddev / Math.sqrt(times);
        return mean - tmp;
    }   
    
   // high endpoint of 95% confidence interval
    public double confidenceHi() {
        double tmp = FRACTILE * stddev / Math.sqrt(times);
        return mean + tmp;
    }                  
 
    // test client (described below)
    public static void main(String[] args) {
        int n = Integer.parseInt(args[0]);
        int trials = Integer.parseInt(args[1]);
        PercolationStats percStats = new PercolationStats(n, trials);
        StdOut.println("mean                    = " + percStats.mean());
        StdOut.println("stddev                  = " + percStats.stddev());
        StdOut.println("95% confidence interval = [" + percStats.confidenceLo()
            + ", " + percStats.confidenceHi() + "]");
    }       
 }