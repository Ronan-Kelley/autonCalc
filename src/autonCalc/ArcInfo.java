package autonCalc;

import java.awt.geom.Arc2D;

public class ArcInfo {
    private int x, x1, x2;
    private double x3, xi, xc = -1;
    private int y, y1, y2;
    private double y3, yi, yc = -1;
    private double deltaX, deltaY;
    private double radius;
    private double startAng;
    private double endAng;
    private double type = Arc2D.PIE;
    private Arc2D arc;
    private String arcInfo;

    public ArcInfo() {

    }

    public ArcInfo(ArcInfo oldObj) {
        this.x = oldObj.getX();
        this.x1 = oldObj.getX1();
        this.x2 = oldObj.getX2();
        this.y = oldObj.getY();
        this.y1 = oldObj.getY1();
        this.y2 = oldObj.getY2();
        this.radius = oldObj.getRadius();
        this.startAng = oldObj.getStartAng();
        this.endAng = oldObj.getEndAng();
        this.type = oldObj.getType();
        this.arc = oldObj.getArc();
    }

    public int getX() {
        return x;
    }

    public int getX1() {
        return x1;
    }

    public int getX2() {
        return x2;
    }

    public double getX3() {
        return x3;
    }

    public double getXi() {
        return xi;
    }

    public double getXC() {
        return xc;
    }

    public int getY() {
        return y;
    }

    public int getY1() {
        return y1;
    }

    public int getY2() {
        return y2;
    }

    public double getY3() {
        return y3;
    }

    public double getYi() {
        return yi;
    }

    public double getYC() {
        return yc;
    }

    public double getRadius() {
        return radius;
    }

    public double getStartAng() {
        return startAng;
    }

    public double getEndAng() {
        return endAng;
    }

    public double getType() {
        return type;
    }

    public String toString() {
        return arcInfo;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setX1(int x1) {
        this.x1 = x1;
    }

    public void setX2(int x2) {
        this.x2 = x2;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void setY1(int y1) {
        this.y1 = y1;
    }

    public void setY2(int y2) {
        this.y2 = y2;
    }

    public void setRadius(double radius) {
        this.radius = radius;
    }

    public void setStartAng(double startAng) {
        this.startAng = startAng;
    }

    public void setEndAng(double endAng) {
        this.endAng = endAng;
    }

    public void setType(double type) {
        this.type = type;
    }

    public ArcInfo copy() {
        return new ArcInfo(this);
    }

    public Arc2D getArc() {
        return arc;
    }

    //
    // build steps
    //



    //
    // actually build
    //

    // public void build() {
    //     deltaX = x2 - x1;
    //     deltaY = y2 - y1;
    //     double lineSolution = (x1 - x2) * ((y2 - y1) / (x2 - x1)) + y1;
    //     double lineLength = Math.sqrt(sqr(deltaX) + sqr(deltaY));
    //     double midLength = lineLength / 2;
    //     double perpLineSlope = -(deltaX/deltaY);
    //     double CP = Board.SCROLLVAL;
    //     double CD = (sqr(midLength)/CP)+CP;
    //     double R = 0.5 * (sqr(midLength)/CP)+CP;
    //     double alphaAng = Math.atan(deltaY / deltaX);
    //     xi = x1 + midLength * Math.cos(Math.toDegrees(alphaAng));
    //     yi = y1 + midLength * Math.sin(Math.toDegrees(alphaAng));
    //     double betaAng = alphaAng + Math.toRadians(90);
    //     x3 = xi + CP * Math.cos(Math.toDegrees(betaAng));
    //     y3 = yi + CP * Math.sin(Math.toDegrees(betaAng));
    //     xc = x3 - R * Math.cos(Math.toDegrees(betaAng));
    //     yc = y3 - R * Math.sin(Math.toDegrees(betaAng));
    //     double startAng = Math.toDegrees(Math.atan((y1 - yc) / (x1 - xc)));
    //     double endAng = Math.toDegrees(Math.atan((y2 - yc) / (x2 - xc)));
    //     if (startAng < 0) {
    //         startAng += 180;
    //     }
    //     if (endAng < 0) {
    //         endAng += 180;
    //     }
    //     double deltaAng = startAng - endAng;

    //     arc = new Arc2D.Double(xc-R/2, yc-R/2, R, R, startAng, deltaAng, Arc2D.PIE);

    //     arcInfo = "x1/y1: " + x1 + "/" + y1 + "\n" +
    //                 "x2/y2: " + x2 + "/" + y2 + "\n" +
    //                 "xi/yi: " + xi + "/" + yi + "\n" +
    //                 "x3/y3: " + x3 + "/" + y3 + "\n" +
    //                 "xc/yc: " + xc + "/" + yc + "\n" +
    //                 "startAng/endAng/deltaAng: " + startAng + "/" + endAng + "/" + deltaAng;

    // }

    public void build() {
        double deltaX = x2 - x1;
        double deltaY = y2 - y1;
        double ang1 = Math.atan2(y1 - yc, x1 - xc);
        double ang2 = Math.atan2(y2 - yc, x2 - xc);
        double deltaAng = ang2 - ang1;
        double radius = dist0(x1, y1);
        this.xc = x1 + deltaX;
        this.yc = y1 - deltaY;
        this.startAng = ang1;
        this.endAng = deltaAng;
        System.out.println("xc/yc: " + xc + "/" + yc);
        System.out.println("deltax/deltay: " + deltaX + "/" + deltaY);
        // System.out.println("x2/y2 - x1/y1: " + x2 + "/" + y2 + " - " + x1 + "/" + y1);

        this.arc = new Arc2D.Double(xc - radius/2, xc - radius/2, radius, radius, startAng, endAng, (int) this.type);
    }

    public double calcDistance(double x, double y, double x1, double y1) {
		// returns distance formula output
		return Math.sqrt(sqr(x1 - x) + sqr(y1 - y));
    }

    public double angle0(double x, double y) {
        // return angle of args from center
        return Math.toDegrees(Math.atan2(y - yc, x - xc));
    }

    public double dist0(double x, double y) {
        // return distance of args from center
        return Math.sqrt(sqr(x - xc) + sqr(y - yc));
    }

    public double angleDiff(double a, double b) {
        // returns difference in angles
        double d = b - a;
        while (d >= 180) {
            d -= 360;
        }
        while (d < -180) {
                d += 360;
        }
        return d;
    }

    public double angleDiff2(double a, double b) {
        // returns difference in standard angles
        a %= 360;
        if (a < 0) {
            a += 360;
        }

        b %= 360;
        if (b < 0) {
            b += 360;
        }

        double d = Math.abs(b - a);

        return d;
    }

    public double sqr(double arg) {
        // squares input
        return arg*arg;
    }

    public void reset() {
        this.x = 0;
        this.x1 = 0;
        this.x2 = 0;
        this.y = 0;
        this.y1 = 0;
        this.y2 = 0;
        this.radius = 0;
        this.startAng = 0;
        this.endAng = 0;
        this.type = Arc2D.PIE;
    }
}