/*
 * SequencerReader is an object dedicated to taking java commands
 * written to be executed on the robot, then executing them. The pre-season
 * code for this will be based on the commands for last year's
 * code.
 */
package autonCalc;

import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Arrays;

public class SequencerReader {
	private ArrayList<UserMarker> markers = new ArrayList<UserMarker>();
	private ArrayList<String> initialSplit;
	private ArrayList<String> finalSplit = new ArrayList<String>();
	private ArrayList<Double[]> commandVals = new ArrayList<Double[]>();
	
	public void run(Graphics g, String commands) {
		buildCommands(commands);
		buildMarkerValues();
		buildMarkers();
		
		draw(g);
	}
	
	private void buildCommands(String commands) {
		/*
		 * this is split up into two arraylists so that comments are handled properly,
		 * as comments on the same lines as code was breaking the reader
		 */
		finalSplit.clear();
		commands = commands
				//remove all tab characters
//				.replaceAll("(?:\\t+|\\s+)", "")
				//add newlines after all semicolons
				.replace(";", ";\n")
				/*
				 * remove all lines including
				 * - package
				 * - addParallel
				 * - import
				 * - public
				 * - requires
				 * - *
				 * - cube
				 * - robot
				 * - check
				 */
				.replaceAll("(?mi)^package.*|addParallel.*|import.*|public.*|requires.*|elevator.*|robot.*|check.*|cube.*|\\*.*", "")
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
				tempD[i] = Double.parseDouble(tempString);
				i++;
			}
			
			commandVals.add(tempD);
			
		}
		
		//print values
		for (Double[] commandVal : commandVals) {
			System.out.println("new Double array");
			for (Double cmdVal : commandVal) {
				System.out.println(cmdVal);
			}
		}
		
	}
	
	private void buildMarkers() {
		
	}
	
	private void draw(Graphics g) {
		for (UserMarker mark : markers) {
			mark.draw(g);
		}
	}
}
