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

        Color up, down, left, right;
        up = pictureCopy.get(x, y - 1);
        down = pictureCopy.get(x, y + 1);
        left = pictureCopy.get(x - 1, y);
        right = pictureCopy.get(x + 1, y);
        double gradientY = gradient(up, down);
        double gradientX = gradient(left, right);

        return Math.sqrt(gradientX + gradientY);
    }
    
    private double gradient(Color p1, Color p2) {
        return Math.pow(p1.getRed() - p2.getRed(), 2) 
            + Math.pow(p1.getGreen() - p2.getGreen(), 2) 
            + Math.pow(p1.getBlue() - p2.getBlue(), 2);
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