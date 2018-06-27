import edu.princeton.cs.algs4.Picture;

public class SeamCarver {
    private int width;
    private int height;
    private Picture pictureCopy;
    
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
        return new Picture(pictureCopy);
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
       validateColumnIndex(x);
       validateRowIndex(y);

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

    // sequence of indices for vertical seam
    public int[] findVerticalSeam() {
        double[][] energy = new double[width][height];
        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                energy[col][row] = energy(col, row);
            }
        }

        int[][] edgeTo = new int[width][height];
        double[][] distTo = new double[width][height];
        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                distTo[col][row] = Double.POSITIVE_INFINITY;
                if (row == 0) distTo[col][row] = energy[col][row];
            }
        }

        // construct disTo[][] and edgeTo[][]
        // there is a downward edge from pixel (x, y) 
        // to pixels (x - 1, y + 1), (x, y + 1), and (x + 1, y + 1)
        for (int row = 0; row < height - 1; row++) {
            for (int col = 0; col < width; col++) {
                if (distTo[col][row + 1] > distTo[col][row] + energy[col][row + 1]) {
                    distTo[col][row + 1] = distTo[col][row] + energy[col][row + 1];
                    edgeTo[col][row + 1] = col;
                }
                if (col - 1 > 0) {
                    if (distTo[col - 1][row + 1] > distTo[col][row] + energy[col - 1][row + 1]) {
                        distTo[col - 1][row + 1] = distTo[col][row] + energy[col - 1][row + 1];
                        edgeTo[col - 1][row + 1] = col;
                    }
                }
                if (col + 1 < width) {
                    if (distTo[col + 1][row + 1] > distTo[col][row] + energy[col + 1][row + 1]) {
                        distTo[col + 1][row + 1] = distTo[col][row] + energy[col + 1][row + 1];
                        edgeTo[col + 1][row + 1] = col;
                    }
                }
            }
        }

        // find column number with minimum energy in bottom
        int minCol = 0;
        double minEnergy = Double.POSITIVE_INFINITY;
        for (int col = 0; col < width; col++) {
            if (minEnergy > distTo[col][height - 1]) {
                minEnergy = distTo[col][height - 1];
                minCol = col;
            }
        }
        
        // construct VerticalSeam
        int[] vSeam = new int[height];
        int minRow = height - 1;
        while (minRow >= 0) {
            vSeam[minRow] = minCol;
            minCol = edgeTo[minCol][minRow--];
        }

        return vSeam;
    }        
    
    // sequence of indices for horizontal seam
    public int[] findHorizontalSeam() {
        transpose();
        int[] hSeam = findVerticalSeam();
        transpose();
        return hSeam;
    }   

    // remove vertical seam from current picture
    public void removeVerticalSeam(int[] seam) {
        if (seam == null) {
            throw new IllegalArgumentException("the argument to removeVerticalSeam() is null\n"); 
        }
        if (seam.length != height) {
            throw new IllegalArgumentException("the length of seam not equal height\n");
        }
        validateSeam(seam);
        if (width <= 1) {
            throw new IllegalArgumentException("the width of the picture is less than or equal to 1\n");
        }

        Picture tmpPicture = new Picture(width - 1, height);
        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width - 1; col++) {
                validateColumnIndex(seam[row]);
                if (col < seam[row]) {
                    tmpPicture.setRGB(col, row, pictureCopy.getRGB(col, row));
                } else {
                    tmpPicture.setRGB(col, row, pictureCopy.getRGB(col + 1, row));
                }
            }
        }
        pictureCopy = tmpPicture;
        width--;
    }    

    // remove horizontal seam from current picture
    public void removeHorizontalSeam(int[] seam) {
        if (seam == null) {
            throw new IllegalArgumentException("the argument to removeHorizontalSeam() is null\n"); 
        }
        if (seam.length != width) {
            throw new IllegalArgumentException("the length of seam not equal width\n");
        }
        validateSeam(seam);
        if (height <= 1) {
            throw new IllegalArgumentException("the height of the picture is less than or equal to 1\n");
        }

        transpose();
        removeVerticalSeam(seam);
        transpose();
    }  

    // transpose the current pictureCopy
    private void transpose() {
        Picture tmpPicture = new Picture(height, width);
        for (int row = 0; row < width; row++) {
            for (int col = 0; col < height; col++) {
                tmpPicture.setRGB(col, row, pictureCopy.getRGB(row, col));
            }
        } 
        pictureCopy = tmpPicture;
        int tmp = height;
        height = width;
        width = tmp;
     }

    // make sure column index is bewteen 0 and width - 1
    private void validateColumnIndex(int col) {
        if (col < 0 || col > width -1) {
            throw new IllegalArgumentException("colmun index is outside its prescribed range\n"); 
        }
    }

    // make sure row index is bewteen 0 and height - 1
    private void validateRowIndex(int row) {
        if (row < 0 || row > height -1) {
            throw new IllegalArgumentException("row index is outside its prescribed range\n"); 
        }
    }

    // make sure two adjacent entries differ within 1
    private void validateSeam(int[] seam) {
        for (int i = 0; i < seam.length - 1; i++) {
            if (Math.abs(seam[i] - seam[i + 1]) > 1) {
                throw new IllegalArgumentException("two adjacent entries differ by more than 1 in seam\n"); 
            }
        }
    }
}
