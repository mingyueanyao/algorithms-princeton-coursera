import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.SET;

public class PointSET {
    private SET<Point2D> points;

    // construct an empty set of points 
    public PointSET() {
        points = new SET<Point2D>();
    }     
    
    // is the set empty? 
    public boolean isEmpty() {
        return points.isEmpty();
    }            
    
    // number of points in the set 
    public int size() {
        return points.size();
    }   
    
    // add the point to the set (if it is not already in the set)
    public void insert(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException("argument to insert() is null\n");
        }

        if (!points.contains(p)) {
            points.add(p);
        }   
    }       
    
    // does the set contain point p? 
    public boolean contains(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException("argument to contains() is null\n");
        }

        return points.contains(p);
    }   
    
    // draw all points to standard draw 
    public void draw() { 
        for (Point2D p : points)
            p.draw();
    } 
    
     // all points that are inside the rectangle (or on the boundary) 
    public Iterable<Point2D> range(RectHV rect) { 
        if (rect == null) {
            throw new IllegalArgumentException("argument to range() is null\n");
        }

        double xmin = rect.xmin();
        double ymin = rect.ymin();
        double xmax = rect.xmax();
        double ymax = rect.ymax();
        Stack<Point2D> pointsInRect = new Stack<Point2D>();
        for (Point2D p : points) {
            double x = p.x();
            double y = p.y();
            if (x >= xmin && x <= xmax && y >= ymin && y <= ymax) {
                pointsInRect.push(p);
            }
        }
        return pointsInRect;
    }  
    
    // a nearest neighbor in the set to point p; null if the set is empty 
    public Point2D nearest(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException("argument to nearest() is null\n");
        }

        if (points.isEmpty()) {
            return null;
        }

        double dist = 0xffff;
        Point2D nearestP = null;
        for (Point2D tmp : points) {
            if (tmp.distanceSquaredTo(p) < dist) {
                dist = tmp.distanceSquaredTo(p);
                nearestP = tmp;
            }
        }
        return nearestP;
    }             
 
    // unit testing of the methods (optional) 
    public static void main(String[] args)  { }                
 }