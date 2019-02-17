package autonCalc;

import java.awt.geom.Arc2D;

public class ArcInfo {
    private int x, x1, x2;
    private int y, y1, y2;
    private int width;
    private int height;
    private double startAng;
    private double endAng;
    private double type = Arc2D.PIE;

    public ArcInfo() {

    }

    public ArcInfo(ArcInfo oldObj) {
        this.x = oldObj.getX();
        this.x1 = oldObj.getX1();
        this.x2 = oldObj.getX2();
        this.y = oldObj.getY();
        this.y1 = oldObj.getY1();
        this.y2 = oldObj.getY2();
        this.width = oldObj.getWidth();
        this.height = oldObj.getHeight();
        this.startAng = oldObj.getStartAng();
        this.endAng = oldObj.getEndAng();
        this.type = oldObj.getType();
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

    public int getY() {
        return y;
    }

    public int getY1() {
        return y1;
    }

    public int getY2() {
        return y2;
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

    public Arc2D generateArc2D() {
        return new Arc2D.Double(x, y, width, height, startAng, endAng, (int) type);
    }

    public ArcInfo copy() {
        return new ArcInfo(this);
    }

    public void checkX2Y2() {
        double ang2 = Math.toDegrees(Math.acos((x2 - x) / calcDistance(x, y, x2, y2)));
        double dist2 = (x2 - x) / (Math.toDegrees(Math.cos(ang2)));

        double newX2, newY2;
        if (dist2 > width) {
            newX2 = x + width * Math.cos(ang2);
            newY2 = y + width * Math.sin(ang2);
            this.x2 = (int) newX2; 
            this.y2 = (int) newY2;
        }
    }

    public void genAngs() {
        // calc starting degrees (variation of newx = oldx + distance * cos(ang))
        double firstStageAng = Math.toDegrees(Math.acos((x1 - x) / calcDistance(x, y, x1, y1)));
        double hypLength = calcDistance(x2, y2, x1, y1);
        double secondStageAng = Math.toDegrees(Math.acos(((Math.pow(hypLength, 2) - Math.pow(width, 2) - Math.pow(width, 2)) / (-2 * width * width))));
        
        if (y1 > y) {
            if (x1 < x) {
                firstStageAng += 90;
            } else {
                firstStageAng += 180;
            }
        }
        // if (y2 > y) {
        //     if (x2 < x) {
        //         secondStageAng += 90;
        //     } else {
        //         secondStageAng += 180;
        //     }
        // }

        firstStageAng %= 360;

        // if (firstStageAng > secondStageAng) {
        //     this.startAng = secondStageAng;
        //     this.endAng = firstStageAng;
        // } else {
        //     this.startAng = firstStageAng;
        //     this.endAng = secondStageAng;
        // }

        this.startAng = firstStageAng;
        this.endAng = secondStageAng;

        System.out.println("firstStageAng/secondStageAng: " + firstStageAng + "/" + secondStageAng);

    }

    public double calcDistance(double x, double y, double x1, double y1) {
		// returns distance formula output
		return Math.sqrt(((x1 - x) * (x1 - x)) + ((y1 - y) * (y1 - y)));
    }
    
    public void calcRadius() {
        this.width = (int) calcDistance(x, y, x1, y1);
        this.height = this.width;
    }

    public void reset() {
        this.x = 0;
        this.x1 = 0;
        this.x2 = 0;
        this.y = 0;
        this.y1 = 0;
        this.y2 = 0;
        this.height = 0;
        this.width = 0;
        this.startAng = 0;
        this.endAng = 0;
        this.type = Arc2D.PIE;
    }
}