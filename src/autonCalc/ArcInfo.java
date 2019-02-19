package autonCalc;

import java.awt.geom.Arc2D;

public class ArcInfo {
    private int x, x1, x2;
    private double x3, xi, xc;
    private int y, y1, y2;
    private double y3, yi, yc;
    private double deltaX, deltaY;
    private double radius;
    private double startAng;
    private double endAng;
    private double type = Arc2D.PIE;
    private Arc2D arc;

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

    public void setX0(int x0) {
        this.x = x0;
    }

    public void setXa(int xa) {
        this.x1 = xa;
    }

    public void setXd(int xd) {
        this.x2 = xd;
    }

    public void setY0(int y0) {
        this.y = y0;
    }

    public void setYa(int ya) {
        this.y1 = ya;
    }

    public void setYd(int yd) {
        this.y2 = yd;
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

    public void build() {
        deltaX = x2 - x1;
        deltaY = y2 - y1;
        double lineSolution = (x1 - x2) * ((y2 - y1) / (x2 - x1)) + y1;
        double lineLength = Math.sqrt(sqr(deltaX) + sqr(deltaY));
        double midLength = lineLength / 2;
        double perpLineSlope = -(deltaX/deltaY);
        double CP = Board.SCROLLVAL;
        double CD = (sqr(midLength)/CP)+CP;
        double R = 0.5 * (sqr(midLength)/CP)+CP;
        double alphaAng = Math.atan(deltaY / deltaX);
        xi = x1 + midLength * Math.cos(alphaAng);
        yi = y1 + midLength * Math.sin(alphaAng);
        double betaAng = alphaAng + Math.toRadians(90);
        x3 = xi + CP * Math.cos(betaAng);
        y3 = yi + CP * Math.sin(betaAng);
        xc = x3 - R * Math.cos(betaAng);
        yc = y3 - R * Math.sin(betaAng);
        double startAng = Math.toDegrees(Math.atan((y1 - yc) / (x1 - xc)));
        double endAng = Math.toDegrees(Math.atan((y2 - yc) / (x2 - xc)));
        // if (x1 < xc) {
        //     startAng += 90;
        // }
        double deltaAng = startAng - endAng;

        arc = new Arc2D.Double(xc-R/2, yc-R/2, R, R, Math.abs(startAng), Math.abs(deltaAng), Arc2D.PIE);

        System.out.println("xc, yc, R, startAng, deltaAng: " + xc + ", " + yc + ", " + R + ", " + startAng + ", " + deltaAng);

    }

    public double calcDistance(double x, double y, double x1, double y1) {
		// returns distance formula output
		return Math.sqrt(sqr(x1 - x) + sqr(y1 - y));
    }

    public double angle0(double x, double y) {
        // return angle of args from center
        return Math.toDegrees(Math.atan2(y - y, x - x));
    }

    public double dist0(double x, double y) {
        // return distance of args from center
        return Math.sqrt(sqr(x - x) + sqr(y - y));
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