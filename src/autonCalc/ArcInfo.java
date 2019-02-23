package autonCalc;

import java.awt.Point;
import java.awt.geom.Arc2D;

import autonCalc.util.Angle;
import autonCalc.util.Angle.AngleType;

public class ArcInfo {
    private Point point1 = new Point();
    private Point point2 = new Point();
    private Point pointC = new Point();
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
        copy.calcAngs();
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
    }

    public void calcAngs() {
        this.ang1.setRads(Math.atan2(point1.getY() - pointC.getY(), point1.getX() - pointC.getX()));
        this.ang2.setRads(Math.atan2(point2.getY() - pointC.getY(), point2.getX() - pointC.getX()));
        
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

    public double getRadius() {
        return this.radius;
    }

    public Arc2D getArc() {
        buildArc();
        return this.arc;
    }

    private void buildArc() {
        radius = Math.sqrt(Math.pow(point1.getX() - pointC.getX(), 2) + Math.pow(point1.getY() - pointC.getY(), 2));
        calcAngs();
        this.arc = new Arc2D.Double(pointC.getX() - (radius / 2), pointC.getY() - (radius / 2), radius, radius, ang1.getDegs(), ang2.getDeltaAng(AngleType.DEG, ang1), Arc2D.PIE);
        System.out.println("ang1/ang2/deltaAng: " + ang1.getDegs() + "/" + ang2.getDegs() + "/" + ang2.getDeltaAng(AngleType.DEG, ang1));
        // System.out.println("x1/y1 - x2/y2: " + point1.getX() + "/" + point1.getY() + " - " + point2.getX() + "/" + point2.getY());
    }
}