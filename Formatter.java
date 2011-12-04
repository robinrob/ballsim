import java.io.Serializable;

/**
 * Class to format strings
 * 
 * @author Pablo Romero
 * @version 1.0
 */
public class Formatter implements Serializable
{
	private static final long serialVersionUID = 2447862773134764551L;


	/**
     * formats a number to take a certain number of left spaces
     * @param number  the number to justify
     * @param length  the lenght of the final string
     * @return the right justified string
     * 
     */
    public static String rightIntJustify(int number, int length)
    {
        Integer intNumber = new Integer(number);
        String numberString = intNumber.toString();
        int spaces = length - numberString.length();
        for ( int i = 0 ; i < spaces ; i++ )
            numberString = " " + numberString;
            
        return numberString;
    }
    
    public static String leftIntJustify(int number, int length)
    {
        Integer intNumber = new Integer(number);
        String numberString = intNumber.toString();
        int spaces = length - numberString.length();
        for ( int i = 0 ; i < spaces ; i++ )
            numberString = numberString + " ";
            
        return numberString;
    }

    /**
     * formats a string to take a certain number of left spaces
     * @param aString  the string to justify
     * @param length  the lenght of the final string
     * @return the right justified string
     * 
     */
    public static String rightJustify(String aString, int length)
    {
        int spaces = length - aString.length();
        for ( int i = 0 ; i < spaces ; i++ )
            aString = " " + aString;
        return aString;
    }

    /**
     * formats a string to take a certain number of right spaces
     * @param aString  the string to justify
     * @param length  the lenght of the final string
     * @return the left justified string
     * 
     */
    public static String leftJustify(String aString, int length)
    {
        int spaces = length - aString.length();
        for (int i = 0; i < spaces; ++i)
            aString += " ";
        return aString;
    }


    public static String toMoneyString(int total)
    {
        String totalString = " " + total/100 + "." + 
                              (total%100/10) + 
                              total%10 + "  ";
        return totalString;
    }   
    
}

