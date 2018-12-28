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

        /* 
        // solution that found in the Internet
        Point[] aux = dup.clone();
        for (int i = 0; i < length - 3; i++) {
            System.arraycopy(dup, i, aux, i, length - i);
            Arrays.sort(aux, 0, i, aux[i].slopeOrder());
            Arrays.sort(aux, i + 1, length, aux[i].slopeOrder());

            int head = i + 1;
            int tail = head + 1;
            while (tail < length) {
                double headSlope = aux[i].slopeTo(aux[head]);
                while (tail < length && headSlope == aux[i].slopeTo(aux[tail])) {
                    tail++;
                }

                if (tail - head >= 3) {
                    int lo = 0;
                    int hi = i;
                    boolean flag = true;
                    // BS
                    while (lo <= hi) {
                        int mid = lo + (hi - lo) / 2;
                        if (aux[i].slopeTo(aux[mid]) > headSlope) hi = mid - 1;
                        else if (aux[i].slopeTo(aux[mid]) < headSlope) lo = mid + 1;
                        else flag = false;
                    }

                    if (flag) {
                        LineSegment tmpLine = new LineSegment(aux[i], aux[tail - 1]);
                        lineQueue.add(tmpLine);
                        number++;
                    }
                }

                head = tail;
                tail++;
            }
        }
        */

        // my solution that two tests overtime
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

                    /*
                    int len = tail - head + 1;
                    Point[] tmp = new Point[len];
                    System.arraycopy(dup, head, tmp, 0, len - 1);
                    tmp[len - 1] = points[i];
                    Arrays.sort(tmp);
                    if (points[i] == tmp[0]) {
                        LineSegment tmpLine = new LineSegment(tmp[0], tmp[len - 1]);
                        lineQueue.add(tmpLine);
                        number++;
                    }
                    */
                }

                head = tail;
                tail++;
            }

            /*
            for (int j = 1; j < length - 2; j++) {
                int k = j + 1;
                while (points[i].slopeTo(dup[j]) == points[i].slopeTo(dup[k])) {
                    k++;
                    if (k == length) break;
                }

                if (k - j >= 3) {
                    Point[] tmp = new Point[k - j + 1];
                    int len = tmp.length;
                    System.arraycopy(dup, j, tmp, 0, len - 1);
                    tmp[len - 1] = points[i];
                    Arrays.sort(tmp);
                    if (points[i].slopeTo(tmp[0]) == Double.NEGATIVE_INFINITY) {
                        LineSegment tmpLine = new LineSegment(tmp[0], tmp[len - 1]);
                        lineQueue.add(tmpLine);
                        number++;
                    }
                }
                j = k - 1;
            }  
            */ 
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