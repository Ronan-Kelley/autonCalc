package autonCalc;

import java.awt.geom.Arc2D;

public class ArcInfo {
    private int x0, xa, xd;
    private int y0, ya, yd;
    private double deltaX, deltaY;
    private double radius;
    private double startAng;
    private double endAng;
    private double type = Arc2D.PIE;
    private Arc2D arc;

    public ArcInfo() {

    }

    public ArcInfo(ArcInfo oldObj) {
        this.x0 = oldObj.getX0();
        this.xa = oldObj.getXa();
        this.xd = oldObj.getXd();
        this.y0 = oldObj.getY0();
        this.ya = oldObj.getYa();
        this.yd = oldObj.getYd();
        this.radius = oldObj.getRadius();
        this.startAng = oldObj.getStartAng();
        this.endAng = oldObj.getEndAng();
        this.type = oldObj.getType();
        this.arc = oldObj.getArc();
    }

    public int getX0() {
        return x0;
    }

    public int getXa() {
        return xa;
    }

    public int getXd() {
        return xd;
    }

    public int getY0() {
        return y0;
    }

    public int getYa() {
        return ya;
    }

    public int getYd() {
        return yd;
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
        this.x0 = x0;
    }

    public void setXa(int xa) {
        this.xa = xa;
    }

    public void setXd(int xd) {
        this.xd = xd;
    }

    public void setY0(int y0) {
        this.y0 = y0;
    }

    public void setYa(int ya) {
        this.ya = ya;
    }

    public void setYd(int yd) {
        this.yd = yd;
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
        deltaX = xd - xa;
        deltaY = yd - ya;
        double lineSolution = (xa - xd) * ((yd - ya) / (xd - xa)) + ya;
        double lineLength = Math.sqrt(sqr(deltaX) + sqr(deltaY));
        double midLength = lineLength / 2;
        double perpLineSlope = -(deltaX/deltaY);
        double CP = Board.SCROLLVAL;
        double CD = (sqr(midLength)/CP)+CP;
        double R = 0.5 * (sqr(midLength)/CP)+CP;
        double alphaAng = Math.atan(deltaY / deltaX);
        double Xi = xa + midLength * Math.cos(alphaAng);
        double yi = ya + midLength * Math.sin(alphaAng);
        double betaAng = alphaAng + Math.toRadians(90);
        double x3 = Xi + CP * Math.cos(betaAng);
        double y3 = yi + CP * Math.sin(betaAng);
        double xc = x3 - R * Math.cos(betaAng);
        double yc = y3 - R * Math.sin(betaAng);
        double startAng = Math.toDegrees(Math.atan((ya - yc) / (xa - xc)));
        double endAng = Math.toDegrees(Math.atan((yd - yc) / (xd - xc)));
        if (xa < xc) {
            startAng += 90;
        }
        double deltaAng = startAng - endAng;

        arc = new Arc2D.Double(xc, yc, R, R, Math.abs(startAng), Math.abs(deltaAng), Arc2D.PIE);

        System.out.println("xc, yc, R, startAng, deltaAng: " + xc + ", " + yc + ", " + R + ", " + startAng + ", " + deltaAng);

    }

    public double calcDistance(double x, double y, double x1, double y1) {
		// returns distance formula output
		return Math.sqrt(sqr(x1 - x) + sqr(y1 - y));
    }

    public double angle0(double x, double y) {
        // return angle of args from center
        return Math.toDegrees(Math.atan2(y0 - y, x - x0));
    }

    public double dist0(double x, double y) {
        // return distance of args from center
        return Math.sqrt(sqr(x - x0) + sqr(y - y0));
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
        this.x0 = 0;
        this.xa = 0;
        this.xd = 0;
        this.y0 = 0;
        this.ya = 0;
        this.yd = 0;
        this.radius = 0;
        this.startAng = 0;
        this.endAng = 0;
        this.type = Arc2D.PIE;
    }
}