package autonCalc;

import java.awt.Point;
import java.awt.geom.Arc2D;

import autonCalc.util.Angle;
import autonCalc.util.Angle.AngleType;

public class ArcInfo {
    private Point point1 = new Point();
    private Point point2 = new Point();
    private Point pointC = new Point();
    private Point originalPointC = new Point();
    private Point midpoint = new Point();
    private Angle ang1 = new Angle();
    private Angle ang2 = new Angle();
    private Arc2D arc;
    private double radius = 0.0;

    public ArcInfo() {}

    public ArcInfo copy() {
        ArcInfo copy = new ArcInfo();
        copy.setCenter((int) this.pointC.getX(), (int) this.pointC.getY());
        copy.setPoint1((int) this.point1.getX(), (int) this.point1.getY());
        copy.setPoint2((int) this.point2.getX(), (int) this.point2.getY());
        copy.buildArc();
        return copy;
    }

    public void setPoint1(int x, int y) {
        this.point1.setLocation(x, y);
    }

    public void setPoint2(int x, int y) {
        this.point2.setLocation(x, y);
    }

    public void setCenter(int x, int y) {
        this.pointC.setLocation(x, y);
        this.originalPointC.setLocation(x, y);
    }

    public Point getPoint1() {
        return point1;
    }

    public Point getPoint2() {
        return point2;
    }

    public Point getCenterPoint() {
        return pointC;
    }

    public Point getMidpoint() {
        calcMidpoint();
        return this.midpoint;
    }

    public double getRadius() {
        return this.radius;
    }

    public Arc2D getArc() {
        buildArc();
        return this.arc;
    }

    /**
     * calculates the radius, calls a helper function that calculates the arcs, and then creates the arc2d object.
     */
    private void buildArc() {
        radius = getDistance(point1, pointC);
        calcAngs();
        calcMidpoint();
        setNewCenter();
        this.arc = new Arc2D.Double(pointC.getX() - (radius), pointC.getY() - (radius), radius*2, radius*2, ang1.getDegs(), ang2.getDeltaAng(AngleType.DEG, ang1), Arc2D.PIE);
    }

    //
    // build helper methods
    //
    private void calcAngs() {
        double ang1 = Math.atan2(point1.getY() - pointC.getY(), point1.getX() - pointC.getX());
        double ang2 = Math.atan2(point2.getY() - pointC.getY(), point2.getX() - pointC.getX());

        ang1 *= -1;
        ang2 *= -1;

        this.ang1.setRads(ang1);
        this.ang2.setRads(ang2);   
    }

    /**
     * calcs midpoint between center of the first point and the center point
     */
    private void calcMidpoint() {
        double ang1to2 = getAng(point1, point2);
        double dist1to2 = getDistance(point1, point2);
        
        int midX = (int) (point1.x + (dist1to2 / 2) * Math.cos(ang1to2));
        int midY = (int) (point1.y + (dist1to2 / 2) * Math.sin(ang1to2));

        this.midpoint.setLocation(midX, midY);
    }

    /**
     * sets new center based on the user's original placement and the scrollwheel value in Board.java
     */
    private void setNewCenter() {
        double centerAng = getAng(pointC, midpoint);

        int newX = (int) (originalPointC.x + Board.SCROLLVAL * Math.cos(centerAng));
        int newY = (int) (originalPointC.y + Board.SCROLLVAL * Math.sin(centerAng));

        this.pointC.setLocation(newX, newY);
    }

    private double getDistance(Point pointA, Point pointB) {
        return Math.sqrt(Math.pow(pointB.x - pointA.x, 2) + Math.pow(pointB.y - pointA.y, 2));
    }

    private double getAng(Point pointA, Point pointB) {
        return Math.atan2(pointB.y - pointA.y, pointB.x - pointA.x);
    }
}