package autonCalc.util;

public class Angle {
    private double degs;
    private double rads;

    public static enum AngleType {
        DEG, RAD
    };

    public Angle() {}

    public Angle(AngleType angleType, double ang) {
        if (angleType == AngleType.DEG) {
            setDegs(ang);
        } else {
            setRads(ang);
        }
    }

    public Angle(int x1, int y1, int x2, int y2) {
        setRads(Math.atan2(y2 - y1, x2 - x1));
    }

    public double getRads() {
        return this.rads;
    }

    public double getDegs() {
        return this.degs;
    }

    public double getDeltaAng(AngleType angleType, Angle ang) {
        if (angleType == AngleType.DEG) {
            return this.degs - ang.getDegs();
        } else {
            return this.rads - ang.getRads();
        }
    }

    public double getDeltaAng(AngleType angleType, double ang) {
        if (angleType == AngleType.DEG) {
            return this.degs - ang;
        } else {
            return this.rads - ang;
        }
    }

    public void setRads(double radians) {
        this.rads = radians;
        this.degs = Math.toDegrees(radians);
    }

    public void setDegs(double degrees) {
        this.rads = Math.toRadians(degrees);
        this.degs = degrees;
    }
}