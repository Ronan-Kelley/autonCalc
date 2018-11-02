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
	private ArrayList<String> robotExec;
	
	public void run(Graphics g, String commands) {
		buildCommands(commands);
		buildMarkers();
		
		draw(g);
	}
	
	//should be private void, currently public String for testing
	public String buildCommands(String commands) {
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
		
		System.out.println("begin output of finalSplit");
		
		for (String curStr : finalSplit) {
			System.out.println(curStr);
		}
		
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
