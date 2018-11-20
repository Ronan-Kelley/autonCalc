/*
 * SequencerReader is an object dedicated to taking java commands
 * written to be executed on the robot, then executing them. The pre-season
 * code for this will be based on the commands for last year's
 * code.
 */
package autonCalc;

import java.util.ArrayList;
import java.util.Arrays;

public class SequencerReader {
	private ArrayList<UserMarker> markers = new ArrayList<UserMarker>();
	private ArrayList<String> initialSplit;
	private ArrayList<String> finalSplit = new ArrayList<String>();
	private ArrayList<Double[]> commandVals = new ArrayList<Double[]>();

	//
	// constructors
	//

	public SequencerReader() {
		/*
		 * in the future, this should probably take a starting position and
		 * initialize the first UserMarker based on that, instead of just
		 * arbitrarily setting it to the point 50, 50.
		 */
	}

	//
	// methods
	//

	public void run(String commands) {
			markers.add(new UserMarker(50, 50, true));
			buildCommands(commands);
			buildMarkerValues();
			buildMarkers();
	}

	private void buildCommands(String commands) {
		/*
		 * this is split up into two arraylists so that comments are handled properly,
		 * as comments on the same lines as code was breaking the reader
		 */

		finalSplit.clear();
		try {
			initialSplit.clear();
		} catch (Exception NullPointerException) {
		}

		commandVals.clear();

		commands = commands
				//remove all tab characters
				//				.replaceAll("(?:\\t+|\\s+)", "")
				//add newlines after all semicolons
				.replace(";", ";\n")
				/*
				 * remove all lines (case insensitive) including:
				 * - package
				 * - addParallel
				 * - import
				 * - public
				 * - requires
				 * - *
				 * - cube
				 * - robot
				 * - check
				 * - toggle
				 */
				.replaceAll("(?mi)^package.*|addParallel.*|import.*|public.*|requires.*|elevator.*|robot.*|check.*|toggle.*|cube.*|\\*.*", "")
				//remove all curly brackets
				.replaceAll("(?:\\{|\\})", "");

		initialSplit = new ArrayList<String>(Arrays.asList(commands.split(";|\n")));

		for (String initStr : initialSplit) {
			String curStr = 
					initStr
					//remove all entries containing //
					.replaceAll("(?m)//.*", "")
					//remove all addSequential prefixes to commands
					.replaceAll("(?i:addSequential\\(new)", "")
					//change all double close paranthesis to single close parenthesis
					.replace("))", ")")
					//remove whitespace/semicolons (github replaces all unix/macos style line endings w/carriage returns)
					.replaceAll("(?:\\n+|\\r+|\\t+|\\s+)", "");

			if (curStr.length() > 2) {
				finalSplit.add(curStr);
			}
		}

	}

	private void buildMarkerValues() {
		/*
		 * this method should take the commands in FinalSplit and convert them to
		 * arrays of decimals
		 */

		for (String curStr : finalSplit) {
			/*
			 * this is messy, if anyone has a better way of doing it, feel free!
			 */

			double commandType = -1;

			/*
			 * this if statement here works similarly to how file headers to -
			 * depending on the value of the first set of switches (in this case,
			 * the value of the first decimal), the data is read differently. In
			 * binary files, this helps the OS executing them know if the program
			 * is 32 bit or 64 bit, as well as what type of binary it is (I.E., 
			 * binaries compiled on linux are ELF binaries)
			 */
			if (curStr.toLowerCase().indexOf("driveto") != -1) {
				//commandType 0 = a driveTo command
				commandType = 0;
			} else if (curStr.toLowerCase().indexOf("rotatedegree") != -1) {
				//commandType 1 = a rotateDegree command
				commandType = 1;
			}

			//remove all text from the string, keep numbers and commas
			curStr = curStr.replaceAll("(?i:driveto\\(|rotatedegree\\(|\\))", "");

			//create a temporary array of strings, each string should just be the
			//a decimal number
			String[] temp = curStr.split(",");
			//not sure how else to add the arrays as complete arrays
			Double[] tempD = new Double[temp.length+1];
			//add in the header
			tempD[0] = commandType;
			//start at 1 instead of 0 since the header takes up the first position
			int i = 1;

			for (String tempString : temp) {
				try {
					tempD[i] = Double.parseDouble(tempString);	
				} catch(Exception NumberFormatException) {

				} finally {
					i++;
				}
			}

			commandVals.add(tempD);

		}

		for (Double[] commandVal : commandVals) {
			System.out.println("new Double array");
			for (Double cmdVal : commandVal) {
				System.out.println(cmdVal);
			}
		}

	}

	private void buildMarkers() {
		/*
		 * buildmarkers just checks the first number in the decimal array,
		 * then passes it to the corresponding function. Those functions
		 * could have just been a part of buildMarkers, but personally 
		 * I find it much easier to read when they are seperated as they are right
		 * now.
		 */
		for (Double[] decimalArray : commandVals) {

			if (decimalArray[0] == 0.0) {
				markers.add(driveToMarker(decimalArray));
			} else if (decimalArray[0] == 1.0) {
				markers.add(rotateDegreeMarker(decimalArray));
			}

		}

	}

	private UserMarker driveToMarker(Double[] decimalArray) {
		/*
		 * create a driveToMarker based on the commands given and the last position.
		 */
		double oldX = markers.get(markers.size()-1).getCenterX();
		double oldY = markers.get(markers.size()-1).getCenterY();
		double distance = decimalArray[1];
		
		/*
		 * add 5 to allow for roughly straight lines, add 90
		 * to correct for degree 0 going to the right on computers
		 */
		double angle = Math.toRadians(Math.floor(decimalArray[2] + 5 + 90));
		//decimalArray[3] contains a value for speed - which is useless in this context
		//decimalArray[4] contains a value for facing - which is useless in this context

		double newX = 0;
		double newY = 0;

		newX = oldX + distance * Math.cos(angle);
		newY = oldY + distance * Math.sin(angle);

		UserMarker marker = new UserMarker((int) Math.floor(newX), (int) Math.floor(newY), true, markers.get(markers.size()-1));

		return marker;
	}

	private UserMarker rotateDegreeMarker(Double[] decimalArray) {
		double oldX = markers.get(markers.size()-1).getCenterX();
		double oldY = markers.get(markers.size()-1).getCenterY();
		double pivotX = oldX + decimalArray[1];
		double pivotY = oldY + decimalArray[2];
		double degree = decimalArray[3];
		//decimalArray[4] contains a value for speed - which is useless in this context
		double newX, newY, radius;

		radius = Math.sqrt(((pivotX - oldX)*(pivotX-oldX)) + ((pivotY-oldY)*(pivotY-oldY)));

		newX = pivotX + (radius * Math.cos(degree));
		newY = pivotY + (radius * Math.sin(degree));

		UserMarker marker = new UserMarker((int) newX, (int) newY, true, markers.get(markers.size()-1), decimalArray[1], decimalArray[2], radius);

		return marker;
	}
	
	public void clearMem() {
		markers.clear();
		initialSplit.clear();
		finalSplit.clear();
		commandVals.clear();
	}

	//
	// getters and setters
	//
	
	public ArrayList<UserMarker> getMarkers() {
		return markers;
	}
}
