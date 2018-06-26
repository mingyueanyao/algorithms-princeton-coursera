import java.awt.Color;
import edu.princeton.cs.algs4.Picture;

public class SeamCarver {
    private Picture pictureCopy;
    private int width;
    private int height;

    // create a seam carver object based on the given picture
    public SeamCarver(Picture picture) {
        if (picture == null) {
            throw new IllegalArgumentException("argument to SeamCarver() is null\n"); 
        }

        pictureCopy = new Picture(picture);
        width = picture.width();
        height = picture.height();
    }               

    // current picture
    public Picture picture() {
        return pictureCopy;
    }  

    // width of current picture
    public int width() {
        return width;
    }  
    
    // height of current picture
    public int height() {
        return height;
    }    

    // energy of pixel at column x and row y
    public double energy(int x, int y) {
        if (x < 0 || x > width -1 || y < 0 || y > height -1) {
            throw new IllegalArgumentException("argument to energy() is outside its prescribed range\n"); 
        }

        // border pixels
        if (x == 0 || x == width -1 || y == 0 || y == height -1) {
            return 1000;
        }

        int up, down, left, right;
        up = pictureCopy.getRGB(x, y - 1);
        down = pictureCopy.getRGB(x, y + 1);
        left = pictureCopy.getRGB(x - 1, y);
        right = pictureCopy.getRGB(x + 1, y);
        double gradientY = gradient(up, down);
        double gradientX = gradient(left, right);

        return Math.sqrt(gradientX + gradientY);
    }
    
    private double gradient(int rgb1, int rgb2) {
        int r1 = (rgb1 >> 16) & 0xFF;
        int g1 = (rgb1 >>  8) & 0xFF;
        int b1 = (rgb1 >>  0) & 0xFF;
        int r2 = (rgb2 >> 16) & 0xFF;
        int g2 = (rgb2 >>  8) & 0xFF;
        int b2 = (rgb2 >>  0) & 0xFF;
        
        return Math.pow(r1 - r2, 2) + Math.pow(g1 - g2, 2) 
            + Math.pow(b1 - b2, 2);
    }
    
    /*
    / TODO
    //
    public   int[] findHorizontalSeam()               // sequence of indices for horizontal seam
    public   int[] findVerticalSeam()                 // sequence of indices for vertical seam
    public    void removeHorizontalSeam(int[] seam)   // remove horizontal seam from current picture
    public    void removeVerticalSeam(int[] seam)     // remove vertical seam from current picture
    */
 }