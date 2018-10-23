package autonCalc;

import java.util.ArrayList;

public class ResultsCalc {
	/*
	 * this whole class has been in desperate need of a rewrite for a bit of a while
	 * now, so it's been all but deleted. I may be just replacing the functionality
	 * this attempted to provide with methods and memory held within the UserMarker
	 * object.
	 */

	public ArrayList<Integer> MarkerX = new ArrayList<Integer>();
	public ArrayList<Integer> MarkerY = new ArrayList<Integer>();
	public ArrayList<Integer> MarkerDistances = new ArrayList<Integer>(); // this array list will have one less index
																			// than the X/Y array lists
	public ArrayList<Integer> MarkerAngles = new ArrayList<Integer>(); // this array list will also have one less index
																		// than the X/Y array lists

	public ArrayList<UserMarker> UserMarkerMem;

	public ResultsCalc(ArrayList<UserMarker> UML) {
		// copies the passed ArrayList of User Markers to the instance variable here
		UserMarkerMem = new ArrayList<UserMarker>(UML);
	}

} // end of class