package autonCalc;

import java.awt.geom.Arc2D;

public class ArcInfo {
    private int x0, xa, xd;
    private int y0, ya, yd;
    private int endX, endY;
    private int width;
    private int height;
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
        this.width = oldObj.getWidth();
        this.height = oldObj.getHeight();
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

    public int getEndX() {
        return endX;
    }

    public int getYa() {
        return ya;
    }

    public int getYd() {
        return yd;
    }

    public int getEndY() {
        return endY;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
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

    public void setWidth(int width) {
        this.width = width;
    }

    public void setHeight(int height) {
        this.height = height;
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

    public void checkXdYd() {
        double ang2 = Math.toDegrees(Math.acos((xd - x0) / calcDistance(x0, y0, xd, yd)));
        double dist2 = (xd - x0) / (Math.toDegrees(Math.cos(ang2)));

        double newXd, newYd;
        if (dist2 > width) {
            newXd = x0 + width * Math.cos(ang2);
            newYd = y0 + width * Math.sin(ang2);
            this.xd = (int) newXd; 
            this.yd = (int) newYd;
        } else if (dist2 < width) {
            newXd = x0 + width * Math.cos(ang2);
            newYd = y0 + width * Math.sin(ang2);
            this.xd = (int) newXd;
            this.yd = (int) newYd;
        }
    }

    public void build() {
        // get radii of anchor/det point
        double ra = dist0(xa, ya);
        double rd = dist0(xd, yd);

        // if either is 0, something is wrong
        if (ra == 0 || rd == 0) {
            startAng = 0;
            endAng = 0;
        }

        // get the angles from center to points
        double aa = angle0(xa, ya);
        double ad = angle0(xd, yd);

        // build the arc
        arc = new Arc2D.Double(x0 - ra, y0 - ra, 2 * ra, 2 * ra, aa, angleDiff(aa, ad), (int) this.type);
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
    
    public void calcRadius() {
        this.width = (int) calcDistance(x0, y0, xa, ya);
        this.height = this.width;
    }

    public void calcXY0() {
        if (yd - ya >= 0) {
            x0 = xa;
        } else {
            x0 = xd;
        }

        if (xd - xa >= 0) {
            y0 = ya;
        } else {
            y0 = yd;
        }
    }

    public void reset() {
        this.x0 = 0;
        this.xa = 0;
        this.xd = 0;
        this.y0 = 0;
        this.ya = 0;
        this.yd = 0;
        this.height = 0;
        this.width = 0;
        this.startAng = 0;
        this.endAng = 0;
        this.type = Arc2D.PIE;
    }
}