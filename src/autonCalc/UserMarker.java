/*
 * UserMarker is a class dedicated to the main 
 * visual portion of this program. It holds the
 * information pertaining to the marks made by the
 * user, as well as the parameters for the objects
 * being drawn. Most of its methods are setters and 
 * getters, but a few such as rotate perform
 * more interesting functions.
 * 
 * this is also where the drawing of the markers
 * is implemented (for the most part), so any
 * kind of code pertaining to the visuals of them
 * is likely to be found in here.
 */

package autonCalc;

import java.awt.Color;
import java.awt.Graphics;

public class UserMarker {
	private int xPos, yPos;
	private int height, width;
	private double centerX, centerY;
	private int xAccel, yAccel;
	private final int defaultAccel = 8;
	//rotation is in degrees, for I am but a puny human
	private double rotation;
	//lastAngle is in radians
	//TODO lastAngle is still starting the rotation 90 degrees off from what the front of the bot would be considered
	private double lastAngle, lastDistance; // angle and distance of this marker and the last one
	private Boolean sameLine = true;
	private Color color;
	private UserMarker lastMarker = null;

	public UserMarker(UserMarker lastMarker, Boolean sameLine) {
		setXPos(lastMarker.getXPos());
		setYPos(lastMarker.getYPos());
		setSameLine(sameLine);
		setHeight(30);
		setWidth(30);
		calcCenter();
		setRotation(0);
		setLastMarker(lastMarker);
	}

	public UserMarker(int x, int y, Boolean sameLine) {
		setXPos(x);
		setYPos(y);
		setHeight(30);
		setWidth(30);
		setRotation(0);
		setSameLine(sameLine);
		calcCenter();
	}
	
	public UserMarker(int x, int y, Boolean sameLine, UserMarker lastMarker) {
		setXPos(x);
		setYPos(y);
		setHeight(30);
		setWidth(30);
		setRotation(0);
		setSameLine(sameLine);
		calcCenter();
		setLastMarker(lastMarker);
	}

	public UserMarker(UserMarker marker) {
		//copy constructor
		setXPos(marker.getXPos());
		setYPos(marker.getYPos());
		setSameLine(marker.getSameLine());
		setHeight(marker.getHeight());
		setWidth(marker.getWidth());
		setRotation(marker.getRotation());
		setLastMarker(marker.getLastMarker());
		calcCenter();
	}

	public int getXPos() {
		return xPos;
	}

	public void setXPos(int x) {
		// make sure the xPos isn't negative or NaN
		if (x >= 0) {
			this.xPos = x;
		} else if (x < 0) {
			this.xPos = 0;
			System.out.println("can't set UserMarker xPos to a negative number!");
		} else {
			System.out.println("userMarker encountered an undefined X input!");
		}
	}

	public int getYPos() {
		return yPos;
	}

	public void setYPos(int y) {
		// make sure the yPos isn't negative or NaN
		if (y >= 0) {
			this.yPos = y;
		} else if (y < 0) {
			this.yPos = 0;
			System.out.println("can't set UserMarker yPos to a negative number!");
		} else {
			System.out.println("userMarker encountered an undefined Y input!");
		}
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		if (height >= 0) {
			this.height = height;
		} else if (height < 0) {
			this.height = 20;
			System.out.println("UserMarker was given a negative height!");
		} else {
			System.out.println("UserMarker encountered an undefined Height input!");
		}
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		if (width >= 0) {
			this.width = width;
		} else if (width < 0) {
			this.width = 20;
			System.out.println("userMarker was given a negative width!");
		} else {
			System.out.println("userMarker was given an undefined Width input!");
		}
	}

	public double getCenterX() {
		return centerX;
	}

	public double getCenterY() {
		return centerY;
	}

	public double getRotation() {
		return rotation;
	}

	public void setRotation(double rotation) {
		if (rotation <= 359 && rotation >= 0) {
			this.rotation = rotation;
		} else if (rotation > 360) {
			rotation = rotation % 360;
			this.rotation = rotation;
		} else if (rotation < 0) {
			while (rotation < 0) {
				rotation += 360;
			}
			this.rotation = rotation;
		} else if (rotation == 360) {
			this.rotation = 0;
		}
	}

	public double getLastAngle() {
		return lastAngle;
	}

	public void setLastAngle(UserMarker lastMarker) {
		double lastX = lastMarker.getCenterX();
		double lastY = lastMarker.getCenterY();
		double currentX = getCenterX();
		double currentY = getCenterY();

		this.lastAngle = angleCalc(lastX, lastY, currentX, currentY);
	}

	public void setLastAngle(Boolean error) {
		if (error == true) {
			this.lastAngle = -1;
		} else {
			System.out.println("error in UserMarker.setLastAngle(Boolean overload)! Please do not call with a false or null boolean");
		}
	}

	public double getLastDistance() {
		return lastDistance;
	}

	public void setLastDistance(UserMarker lastMarker) {
		double lastX = lastMarker.getCenterX();
		double lastY = lastMarker.getCenterY();
		double currentX = getCenterX();
		double currentY = getCenterY();
		this.lastDistance = DistanceCalc(lastX, lastY, currentX, currentY);
	}

	public void setLastDistance(Boolean error) {
		if (error == true) {
			this.lastDistance = -1;
		} else {
			System.out.println("error in UserMarker.setLastDistance(Boolean overload)! Please do not call with a false or null boolean");
		}
	}

	public Boolean getSameLine() {
		return sameLine;
	}

	public void setSameLine(Boolean sameLine) {
		this.sameLine = sameLine;
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}

	public UserMarker getLastMarker() {
		return lastMarker;
	}

	public void setLastMarker(UserMarker lastMarker) {
		this.lastMarker = lastMarker;
	}

	public void calcCenter() {
		this.centerX = this.xPos + (this.width / 2);
		this.centerY = this.yPos + (this.height / 2);

	}

	public void calcAngleDistance() {
		if (getLastMarker() != null) {
			setLastDistance(getLastMarker());
			setLastAngle(getLastMarker());	
		} else if (getLastMarker() == null) {
			setLastDistance(true);
			setLastAngle(true);
		}
	}

	public void draw(Graphics g) {
		/*
		 * should maybe be passed to paint component? Not sure I understand well enough which one
		 * I should actually be using, so I'll stick to calling this with paint()
		 */

		g.setColor(color);
		g.fillRect(this.xPos, this.yPos, this.width, this.height);
	}

	public void moveKeyDown(String Direction) {
		/*
		 * when a key is pressed down, move. This has a sister function that takes the
		 * key being depressed, so that motion stops.
		 */

		switch (Direction) {
		case ("down"):
			this.yAccel = this.defaultAccel;
		break;
		case ("up"):
			this.yAccel = -this.defaultAccel;
		break;
		case ("left"):
			this.xAccel = -this.defaultAccel;
		break;
		case ("right"):
			this.xAccel = this.defaultAccel;
		break;
		}
		this.updatePos();
	}

	public void moveKeyUp(String Direction) {
		/*
		 * this is the sister function to moveKeyDown, all it does is check for that key
		 * that was pressed being depressed, and when that happens it zeros the accel
		 * value.
		 */
		switch (Direction) {
		case ("down"):
			this.yAccel = 0;
		break;
		case ("up"):
			this.yAccel = 0;
		break;
		case ("left"):
			this.xAccel = 0;
		break;
		case ("right"):
			this.xAccel = 0;
		break;
		}
		this.updatePos();
	}

	public void updatePos() {
		// all this does is re-calculate the position of the marker, it doesn't
		// even redraw it
		this.setXPos(this.xPos + this.xAccel);
		this.setYPos(this.yPos + this.yAccel);

		calcCenter();
	}

	public void rotate(double degrees, Boolean clockwise) {
		if (clockwise) {
			setRotation(getRotation()+degrees);
		} else if (!clockwise) {
			setRotation(getRotation()-degrees);
		}

		//		System.out.println(getRotation());
	}

	/*
	 * begin math functions
	 */

	public double DistanceCalc(double x, double y, double x1, double y1) {
		// returns distance formula output
		return Math.sqrt(((x1 - x) * (x1 - x)) + ((y1 - y) * (y1 - y)));
	}

	public double angleCalc(double x, double y, double x1, double y1) {
		// returns angle between two points in radians
		return Math.atan2(y1 - y, x1 - x);
	}

} // end of class