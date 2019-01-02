import java.util.Queue;
import java.util.Arrays;
import java.util.LinkedList;

public class BruteCollinearPoints {
    private int number;            // the number of line segments
    private LineSegment[] line;    // the line segments

    // finds all line segments containing 4 points
    public BruteCollinearPoints(Point[] points)  
    {
        if (points == null) {
            throw new IllegalArgumentException("points == null");
        }

        int length = points.length;
        for (int i = 0; i < length; i++) {
            if (points[i] == null) {
                throw new IllegalArgumentException("points[" + i + "] == null");
            }
        }
        
        Point[] dup = points.clone();
        Arrays.sort(dup);
        for (int i = 0; i < length - 1; i++) {
            if (dup[i].slopeTo(dup[i + 1]) == Double.NEGATIVE_INFINITY) {
                throw new IllegalArgumentException("points[" + i + "] == points[" + (i + 1) + "]");
            }
        }
        
        number = 0; 
        Queue<LineSegment> lineQueue = new LinkedList<LineSegment>();
        for (int i = 0; i < (length - 3); i++) {
            for (int j = i + 1; j < (length - 2); j++) {
                for (int m = j + 1; m < (length - 1); m++) {
                    if (dup[i].slopeTo(dup[j]) != dup[i].slopeTo(dup[m])) {
                        continue;
                    }
                    for (int n = m + 1; n < length; n++) {
                        if (dup[i].slopeTo(dup[j]) == dup[i].slopeTo(dup[n])) {
                            number++;
                            LineSegment tmpLine = new LineSegment(dup[i], dup[n]);
                            lineQueue.add(tmpLine);
                        }
                    }
                }
            }
        }

        line = new LineSegment[number];
        for (int i = 0; i < number; i++) {
            line[i] = lineQueue.remove();
        }
    }  

    // the number of line segments
    public int numberOfSegments() {
        return number;
    }      
    
    // the line segments
    public LineSegment[] segments() {
        return line.clone();
    }    

 }