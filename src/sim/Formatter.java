package sim;

import java.io.Serializable;

/**
 * Abstract class with static methods to format strings.
 * 
 * @author Pablo Romero, extended by Robin Smith (2009.01.18)
 * @version 1.0
 */
public class Formatter implements Serializable
{
	private static final long serialVersionUID = 2447862773134764551L;

	/**
     * Right-justifies the String representation of the given int, given the
     * required length of the justified String, by inserting left spaces.
     * @param number the int to justify.
     * @param length the requied length of the justified int.
     * @return a String containing the right-justified int.
     */
    public static String rightIntJustify(int number, int length)
    {
        Integer intNumber = new Integer(number);
        String numberString = intNumber.toString();
        int spaces = length - numberString.length();
        for (int i = 0; i < spaces; i++) {
            numberString = " " + numberString;
        }
        return numberString;
    }
    
	/**
     * Left-justifies the String representation of the given int, given the
     * required length of the justified String, by inserting right spaces.
     * @param number the number to justify.
     * @param length the required length of the justified String.
     * @return a String containing the left-justified int.
     */
    public static String leftIntJustify(int number, int length)
    {
        Integer intNumber = new Integer(number);
        String numberString = intNumber.toString();
        int spaces = length - numberString.length();
        for (int i = 0; i < spaces; i++) {
            numberString = numberString + " ";
        }  
        return numberString;
    }

	/**
     * Right-justifies the given String, given the required length of the
     * justified String, by inserting left spaces.
     * @param string the String to justify.
     * @param length the required length of the justified String.
     * @return the right-justified String.
     */
    public static String rightJustify(String string, int length)
    {
        int spaces = length - string.length();
        for (int i = 0; i < spaces; i++) {
            string = " " + string;
        }    
        return string;
    }

	/**
     * Left-justifies the given String, given the required length of the
     * justified String, by inserting right spaces.
     * @param string the String to justify.
     * @param length the required length of the justified String.
     * @return the left-justified String.
     */
    public static String leftJustify(String string, int length)
    {
        int spaces = length - string.length();
        for (int i = 0; i < spaces; i++) {
            string = string + " ";
        }
        return string;
    }
    
	/**
     * Center-justifies the given String, given the required length of the
     * justified String, by inserting left and right spaces alternately.
     * @param string the String to justify.
     * @param length the required length of the justified String.
     * @return the center-justified String.
     */
    public static String centerJustify(String string, int length)
    {
        int spaces = length - string.length();
        for (int i = 0; i < spaces; i++) {
        	if ((i % 2) == 0) {
        		string = " " + string;
        	}
        	else {
        		string = string + " ";
        	}
        }
        return string;
    }
    
}

