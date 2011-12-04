package uk.co.mrrobinsmith.ballsim.sim;
import java.awt.*;
import uk.co.mrrobinsmith.ballsim.base.Canvas;

/**
 * Class Platform - a platform object for BallSim.
 * 
 * @author Robin Smith
 * @version 1 (19/10/2010)
 */

public class Platform
{
	private final int LENGTH; //Length of platform
    private final int XPOS; //x-coordinate of left end of platform
    private final int YPOS; //y-coordinate of left end of platform
    
    private final Canvas canvas; /* canvas onto which the platform is drawn */

    /**
     * Constructor for a Platform object.
     * @param x x-coordinate of left-hand end of platform
     * @param y y-coordinate of platform
     * @param length length of the platform
     * @param canvas canvas to be drawn onto
     */
    public Platform(int x, int y, int length, Canvas canvas)
    {
        XPOS = x;
        YPOS = y;
        LENGTH = length;
        this.canvas = canvas;
    }
    
    /**
     * Draw the platform onto the canvas in the specified colour.
     * @param colour
     */
    public void draw(Color colour){
        canvas.setForegroundColor((Color) colour);
        canvas.drawLine(XPOS, YPOS, XPOS + LENGTH, YPOS);
    }
        
}
