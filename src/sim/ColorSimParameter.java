package sim;

/**
 * ColorSimParameter is a wrapper class for ColorParameter, to be used as a
 * simulation parameter.
 * 
 * @author Robin Smith
 * @version 1 (03/11/2010)
 */

public class ColorSimParameter extends ColorParameter
{
	/**
	 * Creates a ColorSimParameter given a name for the parameter, a String
	 * array of possible color names and a boolean value to indicate whether or
	 * not the parameter is adjustable during running of the simulation.
	 * @param name the name.
	 * @param possibleCols a String array of color names (corresponding exactly
	 * to the names of the static Colors held by the java.awt.Color class, e.g.
	 * Color.blue, Color.lightGray). The array can hold any number of color
	 * names. This ColorSimParameter's color will then be adjustable within this
	 * defined set of possible colors. If the ColorSimParameter is not to be
	 * adjustable, then the possibleCols array should contain just the one name
	 * of the color to be used.
	 * @param isAdjustable true or false.
	 */
	public ColorSimParameter(String name, String[] possibleCols,
	                         boolean isAdjustable)
	{
		super(name, possibleCols, isAdjustable);
	}
	
}
