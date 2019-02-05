import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.StdDraw;

public class KdTree {
    private static final boolean VERTICAL = true;
    private static final boolean HORIZONTAL = false;

    private Node root;    // the root of KdTree
    private int size;     // the number of points in the KdTree

    // KdTree helper node data type
    private static class Node {
        private Point2D p;            // the point
        private Node lb;              // the left/bottom subtree
        private Node rt;              // the right/top subtree 
        private boolean divide;       // true->vertical, false->horizontal
        
        public Node(Point2D p) {
            this.p = p;
        }
    }

    // construct an empty set of points 
    public KdTree() {
        size = 0;
        root = null;
    }   

    // is the set empty?                            
    public boolean isEmpty() {
        return root == null;
    } 

    // number of points in the set                      
    public int size() {
        return size;
    } 

    // add the point to the set (if it is not already in the set)                      
    public void insert(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException("argument to insert() is null\n");
        }

        root = insert(root, p, VERTICAL);
    }                         

    private Node insert(Node h, Point2D p, boolean divide) {
        if (h == null) {
            Node tmp = new Node(p);
            tmp.divide = divide;
            size++;
            return tmp;
        }

        double x = p.x();
        double y = p.y();
        double hx = h.p.x();
        double hy = h.p.y();
        if (h.divide == VERTICAL) {      
            if (x > hx) h.rt = insert(h.rt, p, !h.divide);
            else if (x < hx) h.lb = insert(h.lb, p, !h.divide);
            // go right if x equals hx and do nothing if y also equals hy
            else if (y != hy) h.rt = insert(h.rt, p, !h.divide);
        }
        if (h.divide == HORIZONTAL) {
            if (y > hy) h.rt = insert(h.rt, p, !h.divide);
            else if (y < hy) h.lb = insert(h.lb, p, !h.divide);
            // go top if y equals hy and do nothing if x also equals hx
            else if (x != hx) h.rt = insert(h.rt, p, !h.divide);
        }

        return h;
    }

    // does the set contain point p? 
    public boolean contains(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException("argument to contains() is null\n");
        }
        return contains(root, p);
    }

    private boolean contains(Node h, Point2D p) {
        while (h != null) {
            if (h.divide == VERTICAL) {
                if (p.x() > h.p.x()) h = h.rt;
                else if (p.x() < h.p.x()) h = h.lb;
                else if (p.y() != h.p.y()) h = h.rt;
                else return true;
            }
            else if (h.divide == HORIZONTAL) {
                if (p.y() > h.p.y()) h = h.rt;
                else if (p.y() < h.p.y()) h = h.lb;
                else if (p.x() != h.p.x()) h = h.rt;
                else return true;
            }
        }
        return false;
    }

    // draw all points to standard draw
    public void draw() { 
        draw(root, 0.0, 0.0, 1.0, 1.0);
    }   

    private void draw(Node h, double xmin, double ymin, double xmax, double ymax) {
        if (h == null) return;

        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.setPenRadius(0.01);
        h.p.draw();

        if (h.divide == VERTICAL) {
            StdDraw.setPenColor(StdDraw.RED);
            StdDraw.setPenRadius();
            // draw red vertical line
            RectHV rect = new RectHV(h.p.x(), ymin, h.p.x(), ymax);
            rect.draw();
            draw(h.rt, h.p.x(), ymin, xmax, ymax);
            draw(h.lb, xmin, ymin, h.p.x(), ymax);
        }

        if (h.divide == HORIZONTAL) {
            StdDraw.setPenColor(StdDraw.BLUE);
            StdDraw.setPenRadius();
            // draw blue horizontal line
            RectHV rect = new RectHV(xmin, h.p.y(), xmax, h.p.y());
            rect.draw();
            draw(h.rt, xmin, h.p.y(), xmax, ymax);
            draw(h.lb, xmin, ymin, xmax, h.p.y());
        }
    }

    // all points that are inside the rectangle (or on the boundary)                  
    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) {
            throw new IllegalArgumentException("the argument to range() is null\n");
        }

        Stack<Point2D> pointsInRect = new Stack<Point2D>();
        // axis-aligned rectangle corresponding to the root
        RectHV rootRect = new RectHV(0.0, 0.0, 1.0, 1.0);
        range(root, rootRect, rect, pointsInRect);
        return pointsInRect;
    } 

    private void range(Node h, RectHV hRect, RectHV queryRect, Stack<Point2D> pointsInRect) {
        if (h == null) return;
        if (!hRect.intersects(queryRect)) return;

        if (queryRect.contains(h.p)) pointsInRect.push(h.p);

        if (h.divide == VERTICAL) {
            double ymin = hRect.ymin();
            double ymax = hRect.ymax();

            double xmin = h.p.x();
            double xmax = hRect.xmax();
            range(h.rt, new RectHV(xmin, ymin, xmax, ymax), queryRect, pointsInRect);

            xmin = hRect.xmin();
            xmax = h.p.x();
            range(h.lb, new RectHV(xmin, ymin, xmax, ymax), queryRect, pointsInRect);
        }

        else if (h.divide == HORIZONTAL) {
            double xmin = hRect.xmin();
            double xmax = hRect.xmax();

            double ymin = h.p.y();
            double ymax = hRect.ymax();
            range(h.rt, new RectHV(xmin, ymin, xmax, ymax), queryRect, pointsInRect);

            ymin = hRect.ymin();
            ymax = h.p.y();
            range(h.lb, new RectHV(xmin, ymin, xmax, ymax), queryRect, pointsInRect);
        }
    }

    // a nearest neighbor in the set to point p; null if the set is empty           
    public Point2D nearest(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException("the argument to nearest() is null\n");
        }
        if (isEmpty()) return null;

        Node nearestN = new Node(root.p);
        nearestN.lb = root.lb;
        nearestN.rt = root.rt;
        nearestN.divide = root.divide;
        // axis-aligned rectangle corresponding to the root
        RectHV rootRect = new RectHV(0.0, 0.0, 1.0, 1.0);
        nearest(root, rootRect, nearestN, p);
        return nearestN.p;
    }       
    
    private void nearest(Node h, RectHV hRect, Node nearestN, Point2D queryP) {
        if (h == null) return;

        if (queryP.distanceSquaredTo(h.p) < queryP.distanceSquaredTo(nearestN.p)) {
            nearestN.p = h.p;
        }

        double hx = h.p.x();
        double hy = h.p.y();
        double x = queryP.x();
        double y = queryP.y();
        double xmin, xmax, ymin, ymax;
        if (h.divide == VERTICAL) {
            ymin = hRect.ymin();
            ymax = hRect.ymax();

            xmin = hx;
            xmax = hRect.xmax();
            RectHV rtRect = new RectHV(xmin, ymin, xmax, ymax);

            xmin = hRect.xmin();
            xmax = hx;
            RectHV lbRect = new RectHV(xmin, ymin, xmax, ymax);

            if (x >= hx) {
                nearest(h.rt, rtRect, nearestN, queryP);
                if (lbRect.distanceSquaredTo(queryP) < queryP.distanceSquaredTo(nearestN.p)) {
                    nearest(h.lb, lbRect, nearestN, queryP);
                }
            } else {
                nearest(h.lb, lbRect, nearestN, queryP);
                if (rtRect.distanceSquaredTo(queryP) < queryP.distanceSquaredTo(nearestN.p)) {
                    nearest(h.rt, rtRect, nearestN, queryP);
                }
            }
        } else {
            xmin = hRect.xmin();
            xmax = hRect.xmax();

            ymin = hy;
            ymax = hRect.ymax();
            RectHV rtRect = new RectHV(xmin, ymin, xmax, ymax);

            ymin = hRect.ymin();
            ymax = hy;
            RectHV lbRect = new RectHV(xmin, ymin, xmax, ymax);

            if (y >= hy) {
                nearest(h.rt, rtRect, nearestN, queryP);
                if (lbRect.distanceSquaredTo(queryP) < queryP.distanceSquaredTo(nearestN.p)) {
                    nearest(h.lb, lbRect, nearestN, queryP);
                }
            } else {
                nearest(h.lb, lbRect, nearestN, queryP);
                if (rtRect.distanceSquaredTo(queryP) < queryP.distanceSquaredTo(nearestN.p)) {
                    nearest(h.rt, rtRect, nearestN, queryP);
                }
            }
        }   
    }
 
    // unit testing of the methods (optional)
    public static void main(String[] args) { }                   
 }