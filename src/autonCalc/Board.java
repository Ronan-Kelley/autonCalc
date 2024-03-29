/*
 * board is where all the displaying of elements happens and where
 * the graphical portion of the piece begins. Board also contains
 * the keylistener, timer, and actual initializer code.
 */

package autonCalc;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Label;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.image.BufferedImage;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;
import javax.swing.Timer;

import autonCalc.util.GraphicsUtil;
import autonCalc.util.ArcInfo;

@SuppressWarnings("serial")
public class Board extends JPanel implements ActionListener {

	/**
	 * swing objs
	 */

	// distance/angle outputs
	public JTextArea output = new JTextArea(16, 58);
	public JScrollPane scroll = new JScrollPane(output);

	// current usermarker's coordinates
	public Label coords = new Label();

	// necessary to restore focus to the board when lost
	public JButton focusButton = new JButton("restore focus");

	/**
	 * keyboard control aid
	 */

	// used in the keybinds to toggle freeze on controls
	public boolean allowMove = true;

	// used to make control a modifier key
	public boolean modifierControlDown = false;

	/**
	 * curve drawing
	 */
	public int curveStage = 0;
	public ArcInfo arcBuilder = new ArcInfo();
	private Point arcBuilderCorrectedPoint2 = new Point();

	/**
	 * mouse pos information
	 */
	public static double mouseXpos;
	public static double mouseYpos;
	public static int SCROLLVAL = 0;

	/**
	 * local graphical objects
	 */
	private BufferedImage backgroundImage;

	// this array list is public so that it can be used in the class that calculates
	// results
	public ArrayList<UserMarker> UMList = new ArrayList<UserMarker>();
	public ArrayList<ArcInfo> arcList = new ArrayList<ArcInfo>();

	// milliseconds, event handler
	Timer timer = new Timer(5, this);

	// color storage
	public Color[] FColorArray = new Color[] { Color.GREEN, Color.BLUE, Color.ORANGE };
	public Color[] EColorArray = new Color[] { Color.RED, Color.LIGHT_GRAY, Color.BLACK };

	public int colorIndexF = 0;
	public int colorIndexE = 0;

	/*
	 *
	 * begin methods
	 *
	 */

	public Board() {
		initBoard();
		timer.start();
	}

	public void initBoard() {
		/**
		 * not the most professional, beatiful, or well made gui ever designed, but it works.
		 */

		//
		//initialize board's settings
		//
		setBorder(BorderFactory.createEmptyBorder(450, 0, 0, 0));

		setSize(Toolkit.getDefaultToolkit().getScreenSize());
		setPreferredSize(Toolkit.getDefaultToolkit().getScreenSize());

		setFocusable(true);
		setDoubleBuffered(true);

		//
		// initialize objects on board
		//

		//coords
		coords.setLocation(70, 20);
		coords.setText("labelPos");

		//output textarea
		output.setEditable(false);
		scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

		//
		// add event listeners
		//

		focusButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				requestFocus();
			}
		});

		//
		// add objects to board
		//

		add(coords);
		add(output);
		add(focusButton);

		//
		// set up arena/markers
		//

		loadArenaImage(); //load image FIRST so that it has lowest z index
		initObjs(); //create the first UserMarker, add it to UMList
		updateCoords(); //initial drawing of coords

		//
		// mouse listener
		//

		addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent me) {
				if (me.getButton() == MouseEvent.BUTTON1) {
					setMark(true);
				} else if (me.getButton() == MouseEvent.BUTTON3) {
					setMark(false);
				} else if (me.getButton() == MouseEvent.BUTTON2) {
					drawCurve(me.getX(), me.getY());
				}
			}
		});

		//
		// mouse movement listener
		//
		addMouseMotionListener(new MouseAdapter() {
			@Override
			public void mouseMoved(MouseEvent me) {
				if (allowMove) {
					UMList.get(UMList.size() - 1).mouseMove(me.getX(), me.getY());
				}

				mouseXpos = me.getX();
				mouseYpos = me.getY();
				// System.out.println("x/y: " + me.getX() + "/" + me.getY());
				updateCoords();
			}
		});

		//
		// mouse wheel listener
		//
		addMouseWheelListener(new MouseAdapter() {
			@Override
			public void mouseWheelMoved(MouseWheelEvent me) {
				// up returns -1, down return 1
				// System.out.println(me.getWheelRotation());
				Board.SCROLLVAL += me.getWheelRotation();
				if (arcList.size() >= 1) {
					arcList.get(arcList.size() - 1).setScrollVal(Board.SCROLLVAL);
				}
			}
		});

		//
		// key listener
		//

		addKeyListener(new KeyListener() {

			@Override
			public void keyPressed(KeyEvent e) {
				/*
				 * all movement here affects only the most recent addition to the UMList
				 * arraylist
				 */
				int keycode = e.getKeyCode();
				String dir = "up";
				Boolean move = false;
				switch (keycode) {
				case (KeyEvent.VK_UP):
					dir = "up";
				move = true;
				break;
				case (KeyEvent.VK_DOWN):
					dir = "down";
				move = true;
				break;
				case (KeyEvent.VK_LEFT):
					dir = "left";
				move = true;
				break;
				case (KeyEvent.VK_RIGHT):
					dir = "right";
				move = true;
				break;
				case (KeyEvent.VK_CONTROL):
					modifierControlDown = true;
				break;
				}
				if (move && allowMove) {
					UMList.get(UMList.size() - 1).moveKeyDown(dir);
				}
			}

			@Override
			public void keyReleased(KeyEvent e) {
				/*
				 * all movement here affects only the most recent addition to the UMList
				 * arraylist
				 */
				int keycode = e.getKeyCode();
				String dir = "up";
				Boolean move = false;
				switch (keycode) {
				case (KeyEvent.VK_UP):
					dir = "up";
				move = true;
				break;
				case (KeyEvent.VK_DOWN):
					dir = "down";
				move = true;
				break;
				case (KeyEvent.VK_LEFT):
					dir = "left";
				move = true;
				break;
				case (KeyEvent.VK_RIGHT):
					dir = "right";
				move = true;
				break;
				case (KeyEvent.VK_CONTROL):
					modifierControlDown = false;
				break;
				case (KeyEvent.VK_ENTER):
					setMark(true); // create and swap control to a new UserMarker that continues the current line
				break;
				case (KeyEvent.VK_0):
					//pressing 0 should output all important information from the different markers
					UMList.get(UMList.size()-1).calcAngleDistance();
				outputInformation();
				break;
				case (KeyEvent.VK_BACK_SPACE):
					undoMarker();
				break;
				case (KeyEvent.VK_OPEN_BRACKET):
					UMList.get(UMList.size()-1).rotate(15, false);
				updateCoords();
				break;
				case (KeyEvent.VK_CLOSE_BRACKET):
					UMList.get(UMList.size()-1).rotate(15, true);
				updateCoords();
				break;
				//
				// begin coach-related binds
				//
				case (KeyEvent.VK_BACK_SLASH):
					setMark(false); // create and swap control to a new userMarker that starts its own line.
				break;
				case (KeyEvent.VK_X):
					cycleColor(true); // true is for enemy colors
				break;
				case (KeyEvent.VK_C):
					if (modifierControlDown == true) { //require the control key to be pressed to activate
						//clears on ctrl + c
						UMList.clear();
						arcList.clear();
						curveStage = 0;
						initObjs();
					} else if (modifierControlDown == false) {
						cycleColor(false); // false is for friendly colors
					}
				break;
				//
				// end coach related binds
				//
				case (KeyEvent.VK_ESCAPE):
					allowMove = !allowMove;
				break;
				case (KeyEvent.VK_9):
					System.out.println(arcList.get(arcList.size() - 1).toString());
				break;
				}
				if (move && allowMove) {
					UMList.get(UMList.size() - 1).moveKeyUp(dir);
					updateCoords();
				}
			}

			@Override
			public void keyTyped(KeyEvent e) {
				/*
				 * yet to have a use for this, but afaik it has to be here.
				 */

			}
		});
	} // end of initBoard


	//
	// event handling
	//

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == timer) {
			repaint(); //redraw screen every time timer fires
		}
	}

	//
	// methods related to initialization
	//

	public void loadArenaImage() {
		/*
		 * load the field image, taken from FIRST
		 */
		boolean failed = true;
		short curAttempt = 0;
		while (failed && curAttempt < 5) {
			try {
				Image bgImage = ImageIO.read(this.getClass().getResourceAsStream("../resource/backgroundImage2019.png"));
				backgroundImage = GraphicsUtil.imgToBufferedImage(bgImage);
				failed = false;
			} catch (Exception e) {
				failed = true;
			} finally {
				curAttempt++;
			}
		}


	}

	public void initObjs() {
		// add the first UserMarker
		UMList.add(new UserMarker(50, 50, true));
	}

	//
	// methods for manipulating UserMarkers
	//

	public void setMark(Boolean sameLine) {
		/*
		 * add a new marker (currently bound to enter). sameLine just dictates whether
		 * or not the new marker is attached to the one behind it; or, more simply put,
		 * if sameLine is true the marker will be treated as the first marker in a line.
		 */

		//make sure that the necessary information is calculated, but only when it actually can be.
		if (UMList.size() >= 2) {
			UMList.get(UMList.size()-1).calcAngleDistance();
		}

		UMList.add(new UserMarker(UMList.get(UMList.size()-1), sameLine));
	}

	public void setMark(Boolean sameLine, boolean curved, int x, int y) {
		// overload for setting arcIndex

		if (curved) {
			UMList.get(UMList.size()-1).setArcIndex(arcList.size()-1);
			UMList.get(UMList.size()-1).setXPos(x - UMList.get(UMList.size()-1).getWidth() / 2);
			UMList.get(UMList.size()-1).setYPos(y - UMList.get(UMList.size()-1).getHeight() / 2);
			UMList.get(UMList.size()-1).calcCenter();
		}

		//make sure that the necessary information is calculated, but only when it actually can be.
		if (UMList.size() >= 2) {
			UMList.get(UMList.size()-1).calcAngleDistance();
		}

		UMList.add(new UserMarker(UMList.get(UMList.size()-1), sameLine));
	}

	/**
	 * draws a CurveDrive move
	 */
	public void drawCurve(int x, int y) {

		if (curveStage == 0) {
			arcBuilder.setPoint1(x, y);
			setMark(false);

			curveStage = 1;

		} else if (curveStage == 1) {

			arcBuilder.setCenter(x, y);

			curveStage = 2;

		} else if (curveStage == 2) {

			arcBuilder.setPoint2((int) arcBuilderCorrectedPoint2.getX(), (int) arcBuilderCorrectedPoint2.getY());

			arcList.add(arcBuilder.copy());

			setMark(true, true, (int) arcBuilderCorrectedPoint2.getX(), (int) arcBuilderCorrectedPoint2.getY());

			// arcList.set(arcList.size() - 1, arcBuilder.copy());
			curveStage = 0;
			Board.SCROLLVAL = 0;
		}
	}

	public void undoMarker() {
		//simply remove the most recent userMarker to re-establish control of a previous one
		if (UMList.size() > 1) {
			if (UMList.get(UMList.size()-1).getArcIndex() != -1) {
				arcList.remove(UMList.get(UMList.size()-1).getArcIndex());
			}
			UMList.remove(UMList.size()-1);
		}
	}

	//
	// methods related to outputting information to the user
	//

	public void updateCoords() {
		/*
		 * output the X, Y, and Rotation of the current UserMarker
		 */

		//create DecimalFormat object to round degrees to 2 decimal places
		DecimalFormat df = new DecimalFormat("#.##");
		//make sure the rounding is done in the way a high school student would expect
		df.setRoundingMode(RoundingMode.HALF_UP);

		double degrees = UMList.get(UMList.size() - 1).getRotation();

		//round degrees to two decimal places, then store in a string for printing
		String degreesString = df.format(degrees);

		int X = UMList.get(UMList.size() - 1).getXPos();
		int Y = UMList.get(UMList.size() - 1).getYPos();

		coords.setText(
				"current coords: " + X + ", " + Y + "; " + degreesString + " [x,y; rotation]");
	}

	public void outputInformation() {
		/*
		 * ideally this function should pull info from the instance variables
		 * in UserMarker objects, then display important information about them in
		 * the "output" JTextArea.
		 */

		//used in the loop to round decimals to 2 places
		DecimalFormat df = new DecimalFormat("#.##");
		df.setRoundingMode(RoundingMode.HALF_UP);

		String OUTPUT = "";

		for (UserMarker marker : UMList) {
			if (marker.getLastMarker() != null) {
				double X = marker.getCenterX();
				double Y = marker.getCenterY();
				/*
				 * sometimes markers (specifically the second in the list?)
				 * don't calculate a lastAngle or lastDistance when this
				 * function is called, this if statements seems to fix that
				 */
				if (marker.getLastDistance() == 0 || marker.getLastDistance() == -1|| marker.getLastAngle() == 0 || marker.getLastAngle() == -1) {
					marker.calcAngleDistance();
				}

				double lastAngle, lastDistance, radius;
				if (marker.getArcIndex() == -1) {
					lastAngle = Math.toDegrees(marker.getLastAngle());
					//this could be a potential issue, I don't know if conversionRatio will mess up angle calculations
					lastDistance = marker.getLastDistance();
					OUTPUT += "X: " + X + ", Y: " + Y + ", lastAngle: " + df.format(lastAngle) + ", lastDistance: " + df.format(lastDistance) + "\n";
				} else {
					double xOffset = UserMarker.inchesPerPixel * (arcList.get(marker.getArcIndex()).getMidpoint().getX() - arcList.get(marker.getArcIndex()).getPoint1().getX());
					double yOffset = UserMarker.inchesPerPixel * (arcList.get(marker.getArcIndex()).getMidpoint().getY() - arcList.get(marker.getArcIndex()).getPoint1().getY());
					lastAngle = arcList.get(marker.getArcIndex()).getArc().getAngleExtent();
					radius = arcList.get(marker.getArcIndex()).getRadius() * UserMarker.inchesPerPixel;
					OUTPUT += "arc degrees: " + df.format(lastAngle) + ", radius: " + df.format(radius) + ", x offset: " + xOffset + ", y offset: " + -yOffset + "\n";
				}
			}
		}
		output.setText(OUTPUT);
	}

	//
	// methods related to graphics
	//

	@Override
	public void paintComponent(Graphics g) {
		// call paint component super like a good boy
		super.paintComponent(g);

		// might be a little weird to do things this way? Some of the graphics have
		// their own functions just for readability.
		doDrawing(g);
		drawLines(g);
		drawSemiCircle(g);
	}

	public void doDrawing(Graphics g) {
		/*
		 * draws the background image before drawing all e xisting markers over it.
		 */
		Graphics2D g2d = (Graphics2D) g.create();
		g2d.drawImage(backgroundImage, 0, 0, null); // MUST BE THE FIRST THING DRAWN
		for (int i = 0; i < UMList.size(); i++) {
			UMList.get(i).draw(g);
		}
	}

	public void drawLines(Graphics g) {
		/*
		 * self explanatory, draws the lines between each UserMarker displayed on screen
		 */
		if (UMList.size() > 1) {
			g.setColor(Color.black);
			for (int i = 0; UMList.size() - 1 > i; i++) {
				if (UMList.get(i + 1).getSameLine() == true && UMList.get(i +1).getCircle() == false) {
					int x = (int) UMList.get(i).getCenterX();
					int y = (int) UMList.get(i).getCenterY();
					int x1 = (int) UMList.get(i + 1).getCenterX();
					int y1 = (int) UMList.get(i + 1).getCenterY();
					g.drawLine(x, y, x1, y1);
				} else if (UMList.get(i + 1).getSameLine() && UMList.get(i +1).getCircle()) {
					//try to draw circles, currently not in the right spot due to lack of conversion ratio in SequencerReader
					int circleCenterX = (int) (UMList.get(i).getCenterX() + UMList.get(i + 1).getCircleX());
					int circleCenterY = (int) (UMList.get(i).getCenterY() + UMList.get(i + 1).getCircleY());
					int radius = (int) UMList.get(i + 1).getRadius();

					g.drawOval(circleCenterX, circleCenterY, radius, radius);
				}
			}
		}

		if (curveStage == 1) {
			g.drawLine((int) arcBuilder.getPoint1().getX(), (int) arcBuilder.getPoint1().getY(), (int) mouseXpos,(int) mouseYpos);
		}

		if (curveStage == 2) {
			int xc, x2, yc, y2;
			double radius, mouseAng;
			xc = (int) arcBuilder.getCenterPoint().getX();
			yc = (int) arcBuilder.getCenterPoint().getY();

			radius = Math.sqrt(Math.pow(arcBuilder.getPoint1().getX() - xc, 2) + Math.pow(arcBuilder.getPoint1().getY() - yc, 2));
			mouseAng = Math.atan2(mouseYpos - yc, mouseXpos - xc);

			x2 = (int) (xc + radius * Math.cos(mouseAng));
			y2 = (int) (yc + radius * Math.sin(mouseAng));

			this.arcBuilderCorrectedPoint2.setLocation(x2, y2);

			g.drawLine(xc, yc, x2, y2);
		}
	}

	public void drawSemiCircle(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		for (ArcInfo arc : arcList) {
			g2.setColor(Color.BLUE);
			g2.setStroke(new BasicStroke(3));
			g2.draw(arc.getArc());
			g2.setStroke(new BasicStroke(1));
			g2.setColor(Color.ORANGE);
			g2.fillOval((int) arc.getPoint1().getX(), (int) arc.getPoint1().getY(), 5, 5);
			g2.fillOval((int) arc.getPoint2().getX(), (int) arc.getPoint2().getY(), 5, 5);
			g2.setColor(Color.GREEN);
			g2.fillOval((int) arc.getCenterPoint().getX(), (int) arc.getCenterPoint().getY(), 5, 5);
			g2.setColor(Color.RED);
			g2.fillOval((int) arc.getMidpoint().getX(), (int) arc.getMidpoint().getY(), 5, 5);
		}
	}

	public void cycleColor(Boolean team) {
		/*
		 * coaching feature - if set to true, cycles through enemy colors, if set to
		 * false, cycles through friendly colors.
		 */
		Color color;
		if (team) {
			if (colorIndexE < 3) {
				color = EColorArray[colorIndexE];
				UMList.get(UMList.size() - 1).setColor(color);
				colorIndexE++;
			} else {
				colorIndexE = 0;
				color = EColorArray[colorIndexE];
				UMList.get(UMList.size() - 1).setColor(color);
				colorIndexE++;
			}
		} else if (!team) {
			if (colorIndexF < 3) {
				color = FColorArray[colorIndexF];
				UMList.get(UMList.size() - 1).setColor(color);
				colorIndexF++;
			} else {
				colorIndexF = 0;
				color = FColorArray[colorIndexF];
				UMList.get(UMList.size() - 1).setColor(color);
				colorIndexF++;
			}
		}
	}

} // end of class
