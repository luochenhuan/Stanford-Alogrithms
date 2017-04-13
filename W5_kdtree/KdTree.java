import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;
import java.util.*;

public class KdTree {
    
    private static class KdNode {
        private Point2D p;      // the point
        private RectHV rect;    // the axis-aligned rectangle corresponding to this node
        private KdNode left;        // the left/bottom subtree
        private KdNode right;        // the right/top subtree
        private boolean isVertical; // if the division line is vertical or horizontal
        
        public KdNode(Point2D p, RectHV rect, boolean isVertical) {
            this.p = p;
            this.rect = rect;
            this.isVertical = isVertical;
            left = null;
            right = null;
        }
    }
    
    private KdNode root;
    private int size;
    
    public         KdTree() {                              // construct an empty set of points 
        root = null;
        size = 0;
    }
    
    public           boolean isEmpty() {
        return size == 0;
    } 
    
    public               int size() {
        return size;
    }
    
    public              void insert(Point2D p) {
    // add the point to the set (if it is not already in the set)
        if(p == null) throw new java.lang.NullPointerException();
        if (root == null) {
            root = new KdNode(p, new RectHV(0, 0, 1, 1), true);
            size++;
        }
        else
            root = insert(root, p, root.rect, root.isVertical);
    }
    
    private KdNode insert(KdNode n, Point2D p, RectHV rect, boolean isVertical) {
        if (n == null) {
            n = new KdNode(p, rect, isVertical);
            size++;
            return n;
        }
        // already in tree
        if (n.p.equals(p)) return n;
        RectHV rectVar;
        if (n.isVertical) {
            if (Point2D.X_ORDER.compare(p, n.p) < 0) { 
            // left child
                if (n.left == null) {
                    rectVar = new RectHV(n.rect.xmin(), n.rect.ymin(), n.p.x(), n.rect.ymax());
                } else {
                    rectVar = n.left.rect;
                }
                n.left = insert(n.left, p, rectVar, !isVertical);
            } else {
                // right child
                if (n.right == null) {
                    rectVar = new RectHV(n.p.x(), n.rect.ymin(), n.rect.xmax(), n.rect.ymax());
                } else {
                    rectVar = n.right.rect;
                }
                n.right = insert(n.right, p, rectVar, !isVertical);
            }   
        } else {
            if (Point2D.Y_ORDER.compare(p, n.p) < 0) { 
            // left child
                if (n.left == null) {
                    rectVar = new RectHV(n.rect.xmin(), n.rect.ymin(), n.rect.xmax(), n.p.y());
                } else {
                    rectVar = n.left.rect;
                }
                n.left = insert(n.left, p, rectVar, !isVertical);
            } else {
                // right child
                if (n.right == null) {
                    rectVar = new RectHV(n.rect.xmin(), n.p.y(), n.rect.xmax(), n.rect.ymax());
                } else {
                    rectVar = n.right.rect;
                }
                n.right = insert(n.right, p, rectVar, !isVertical);
            } 
        }
        return n;
    }
    
    public           boolean contains(Point2D p) {
    // does the set contain point p? 
        if(p == null) throw new java.lang.NullPointerException();
        return contains(root, p);
    }
    
    private boolean contains(KdNode n, Point2D p) {
        if (n == null) return false;
        if (n.p.equals(p))
            return true;
        
        if (n.isVertical && Point2D.X_ORDER.compare(p, n.p) < 0 || !n.isVertical && Point2D.Y_ORDER.compare(p, n.p) < 0) {
            //Check left child
            return contains(n.left, p);
        } else {
            return contains(n.right, p);
        }
    }
    
    public              void draw() {
        // draw all points to standard draw
        draw(root);
    }
    private void draw(KdNode n) {
        if (n == null) return;
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.setPenRadius(0.01);
        StdDraw.point(n.p.x(), n.p.y());
        
        StdDraw.setPenRadius();
        if (n.isVertical) {
            StdDraw.setPenColor(StdDraw.RED);
            StdDraw.line(n.p.x(), n.rect.ymin(), n.p.x(), n.rect.ymax());
        } else {
            StdDraw.setPenColor(StdDraw.BLUE);
            StdDraw.line(n.rect.xmin(), n.p.y(), n.rect.xmax(), n.p.y());
        }
        draw(n.left);
        draw(n.right);
    }
    
    public Iterable<Point2D> range(RectHV rect) {
    // all points that are inside the rectangle
        if(rect == null) throw new java.lang.NullPointerException();
        List<Point2D> insidePts = new LinkedList<>();
        range(rect, root, insidePts);
        return insidePts;
    }

    private void range(RectHV rect, KdNode n, List<Point2D> pts) {
        if (n == null)
            return;
        if (rect.contains(n.p))
            pts.add(n.p);
        if (n.left != null && rect.intersects(n.left.rect))
            range(rect, n.left, pts);
        if (n.right != null && rect.intersects(n.right.rect))
            range(rect, n.right, pts);
    }
    
    public           Point2D nearest(Point2D p) {
    // a nearest neighbor in the set to point p; null if the set is empty 
        if(p == null) throw new java.lang.NullPointerException();
        if (root == null) return null;
        return nearest(p, root, root.p);
    }
    
    private Point2D nearest(Point2D p, KdNode n, Point2D prevMinPt) {
        if (n == null)  return prevMinPt;
        Point2D curMinPt = prevMinPt;
        if (p.distanceSquaredTo(n.p) < p.distanceSquaredTo(curMinPt))
            curMinPt = n.p;
        
        if (n.isVertical && Point2D.X_ORDER.compare(p, n.p) < 0 || !n.isVertical && Point2D.Y_ORDER.compare(p, n.p) < 0) {
        // left child could contain a closer point
            curMinPt = nearest(p, n.left, curMinPt);
            if (n.right != null && n.right.rect.distanceSquaredTo(p) < curMinPt.distanceSquaredTo(p)) {
                curMinPt = nearest(p, n.right, curMinPt);
            }
        } else {
            // right child could contain a closer point
            curMinPt = nearest(p, n.right, curMinPt);
            if (n.left != null && n.left.rect.distanceSquaredTo(p) < curMinPt.distanceSquaredTo(p)) {
                curMinPt = nearest(p, n.left, curMinPt);
            }
        }
        return curMinPt;
    }

    public static void main(String[] args) {}        
}
