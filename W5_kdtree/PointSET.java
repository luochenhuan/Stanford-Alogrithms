

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import java.util.*;

public class PointSET {
    private TreeSet<Point2D> points;
    public         PointSET() {  
    // construct an empty set of points 
        points = new TreeSet<>();
    }
    
    public           boolean isEmpty() {
        return points.size() == 0;
    }
    
    public               int size() {
        return points.size();
    }                       
    
    public              void insert(Point2D p) {
    // add the point to the set (if it is not already in the set)
        if(p == null) throw new java.lang.NullPointerException();
        points.add(p);
    }
    
    public           boolean contains(Point2D p) {
    // does the set contain point p? 
        if(p == null) throw new java.lang.NullPointerException();
        return points.contains(p);
    }
    
    public              void draw() {
        // draw all points to standard draw
        for (Point2D p : points)
            p.draw();
    }
    
    public Iterable<Point2D> range(RectHV rect) {
    // all points that are inside the rectangle
        if(rect == null) throw new java.lang.NullPointerException();
        List<Point2D> inside = new LinkedList<>();
        for (Point2D p : points) {
            if (rect.contains(p))
                inside.add(p);
        }
        return inside;
    }
    
    public           Point2D nearest(Point2D p) {
    // a nearest neighbor in the set to point p; null if the set is empty 
        if(p == null) throw new java.lang.NullPointerException();
        if (isEmpty()) return null;
        double minDist = Double.MAX_VALUE;
        Point2D nearestPt = null;
        for (Point2D point : points) {
            if (p.distanceSquaredTo(point) < minDist) {
                nearestPt = point;
                minDist = p.distanceSquaredTo(point);
            }
        }
        return nearestPt;
    }

    public static void main(String[] args) {}                 
 }