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
	private ArrayList<String> robotExec;
	
	public void run(Graphics g, String commands) {
		buildCommands(commands);
		buildMarkers();
		
		draw(g);
	}
	
	//should be private void, currently public String for testing
	public String buildCommands(String commands) {
		commands = commands
				//remove all tab characters
				.replaceAll("(?:\\t+|\\s+)", "")
				//get rid of addSequential string (case insensitive)
				.replaceAll("(?i)AddSequential\\(new", "")
				//add newlines after all semicolons
				.replace(";", ";\n")
				/*
				 * remove all lines including
				 * - package
				 * - addParallel
				 * - import
				 * - public
				 * - requires
				 * - //
				 * - cubeclose
				 */
				.replaceAll("(?mi)^package.*|addParallel.*|import.*|public.*|requires.*|//.*|cubeclose.*|elevator.*", "")
				//remove semicolons
				.replace(";", "")
				//remove all curly brackets
				.replace("{", "").replace("}", "")
				//remove the trailing double paranthesis
				.replace("))", ")")
				//remove whitespace/semicolons (github replaces all unix/macos style line endings w/carriage returns)
				.replaceAll("(?:\\n+|\\r+|\\t+|\\s+)", "");
		//create an an arraylist out of each of the commands, seperated by ) characters
		robotExec = new ArrayList<String>(Arrays.asList(commands.split("\\)")));
		
		return commands;
	}
	
	private void buildMarkers() {
		/*
		 * this method should take the commands set in buildCommands and create
		 * UserMarker objects based on them
		 */
	}
	
	private void draw(Graphics g) {
		for (UserMarker mark : markers) {
			mark.draw(g);
		}
	}
}
