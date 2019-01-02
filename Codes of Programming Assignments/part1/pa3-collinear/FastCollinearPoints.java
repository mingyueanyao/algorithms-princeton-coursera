import edu.princeton.cs.algs4.StdOut;

import java.util.Queue;
import java.util.Arrays;
import java.util.LinkedList;

public class FastCollinearPoints {
    private int number;            // the number of line segments
    private LineSegment[] line;    // the line segments

    // finds all line segments containing 4 or more points
    public FastCollinearPoints(Point[] points) {
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
            if (dup[i].compareTo(dup[i + 1]) == 0) {
                throw new IllegalArgumentException("points[" + i + "] == points[" + (i + 1) + "]");
            }   
        }

        number = 0; 
        Queue<LineSegment> lineQueue = new LinkedList<LineSegment>();
        for (int i = 0; i < length; i++) {
            Arrays.sort(dup, points[i].slopeOrder());
            int head = 1;
            int tail = head + 1;
            while (tail < length) {
                double headSlope = points[i].slopeTo(dup[head]);
                while (tail < length && headSlope == points[i].slopeTo(dup[tail])) {
                    tail++;
                }
                if (tail - head >= 3) {
                    boolean flag = true;
                    Point max = new Point(-32767, -32767);
                    for (int j = head; j < tail; j++) {
                        if (points[i].compareTo(dup[j]) > 0) {
                            flag = false;
                            break;
                        }
                        if (dup[j].compareTo(max) > 0) {
                            max = dup[j];
                        }
                    }
                    if (flag) {
                        LineSegment tmpLine = new LineSegment(points[i], max);
                        lineQueue.add(tmpLine);
                        number++;
                    }
                }

                head = tail;
                tail++;
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