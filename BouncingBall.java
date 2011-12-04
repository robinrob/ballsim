import java.awt.*;

/**
 * Class BouncingBall - a physical ball object with a graphical representation.
 * The ball itself determines how it moves in response to external forces:
 * -gravity
 * -air resistance
 * -surface resistance
 * -ball hysteresis
 *
 * Movement is made by repeated calls to the move() method.
 * 
 * @author Robin Smith
 * @version 1 (19/10/2010)
 * 
 * Based on a simple implementation of a Ball object written by Bruce Quig,
 * Michael Kolling (mik) and David J. Barnes, for the book 
 * 'Objects First with Java - A practical Introduction Using BlueJ'
 * (23-Jan-2002).
 */

public class BouncingBall
{
    private final int BALL_DIAMETER;
    private final double GRAVITY;
    private final double BALL_HYST; //amount of rebound effect when bouncing
    private final double AIR_RES; //air resistance
    private final double SURF_RES; //platform surface resistance
    
    private final Canvas canvas;
    private final DataAnalyser data;
    /* vertical platform positions as a function of x */
    private final int[] platformPos;
    
    private double xPos; //initial horizontal position
    private double yPos; //initial vertical position
    private double xSpeed; //initial horizontal speed
    private double ySpeed; //initial vertical speed
    private boolean isStoppedX = false; //check for horizontal movement
    private boolean isStoppedY = false; //check for vertical movement
    private boolean isStopped = false; //check for no movement
    private boolean isRolling = false; //check for purely horizontal movement

    /**
     * Constructor for BouncingBall object.
     * @param xPos initial horizontal position of the ball
     * @param yPos initial vertical position of the ball
     * @param xSpeed initial horizontal speed of the ball
     * @param ySpeed initial vertical speed of the ball
     * @param diameter diameter of the ball
     * @param ballHysteresis amount of ball hysteresis on bouncing
     * @param gravity strength of gravity in simulation environment
     * @param airRes strength of air resistance
     * @param surfaceRes strength of surface rolling friction
     * @param canvas canvas to be drawn on
     * @param platformPos array of platform heights as a function of x-position
     * @param dataAnalyser DataAnalyser object to send data to
     */
    public BouncingBall(double xPos, double yPos, double xSpeed, double ySpeed, 
    		int diameter, double ballHysteresis, double gravity,
    		double airRes, double surfaceRes, Canvas canvas, int[] platformPos,
    		DataAnalyser dataAnalyser)
    {
    	this.xPos = xPos;
        this.yPos = yPos;
        this.xSpeed = xSpeed;
        this.ySpeed = ySpeed;
    	
        BALL_DIAMETER = diameter;
        GRAVITY = gravity;
        BALL_HYST = ballHysteresis;
        AIR_RES = airRes;
        SURF_RES = surfaceRes;
        
        this.canvas = canvas;
        this.platformPos = platformPos;
        data = dataAnalyser;
    }

    /**
     * 
     * @return the canvas on which this ball is drawn
     */
    public Canvas getCanvas()
    {
    	return canvas;
    }
    
    /**
     * Draw this ball at its current position onto the canvas.
     */
    public void draw(Color colour)
    {
    	if (ballInXBounds() && ballInYBounds()) {
    		canvas.setForegroundColor(colour);
        	canvas.fillCircle((int) xPos, (int) yPos, BALL_DIAMETER);
    	}
    }

    /**
     * Erase this ball at its current position.
     */
    public void erase()
    {
    	if (ballInXBounds() && ballInYBounds())
    		canvas.eraseCircle((int) xPos, (int) yPos, BALL_DIAMETER);
    }    

    /**
     * Erase this ball at its current position, then move it according to its
     * new speed determined by the external forces acting on it in time interval
     * delta_t.
     */
    public void move(double delta_t)
    {
    	erase();
    	
    	if (!isStopped) {		
    		if (!isStoppedX)
    			moveHorizontal(delta_t);
    		if (!isStoppedY)
    			moveVertical(delta_t);
    	}
    	if (isStoppedX && isStoppedY && !isStopped)
    		stopBall();
    }
    
    /**
     * Move the ball horizontally.
     * @param delta_t
     */
    private void moveHorizontal(double delta_t)
    {
		if (xSpeed >= 1.0) {
			xSpeed *= (1 - AIR_RES);
			if (isRolling)
				xSpeed *= (1 - SURF_RES);
		}
		else if (!isStoppedX)
			isStoppedX = true;
			
		if((xPos + xSpeed * delta_t + 2 * BALL_DIAMETER) > (platformPos.length - 1))
			loopBack(delta_t);
		else
			xPos += xSpeed * delta_t;
    }
    
    /**
     * Move the ball vertically.
     * @param delta_t
     */
    private void moveVertical(double delta_t)
    {
    	if (((yPos + (ySpeed + GRAVITY * delta_t) * delta_t) >= platformPos[(int) xPos]))
			bounce(delta_t);
		else {
			ySpeed += GRAVITY * delta_t;
			yPos += ySpeed * delta_t;
			if (isRolling)
				isRolling = false;
		}
    }
    
    /**
     * Bring the ball back round to the left-hand side of the simulation
     * bounds.
     * @param delta_t
     */
    private void loopBack(double delta_t)
    {
    	double oldXPos = xPos;
        xPos += xSpeed * delta_t - (platformPos.length - 1) + 2 * BALL_DIAMETER;
        yPos = platformPos[(int) xPos] - (platformPos[(int) oldXPos] - yPos);
    }
    
    /**
     * Bounce the ball off the platform below its position.
     * @param delta_t
     */
    private void bounce(double delta_t)
    {
    	ySpeed += GRAVITY * delta_t;
    	double oldYSpeed = ySpeed;
    	if (!isStopped) {
    		yPos = platformPos[(int) xPos];
    		ySpeed = -1.0 * oldYSpeed * (1.0 - BALL_HYST);
    	}
    	if (ySpeed > -1.0) {
    		isRolling = true;
        	if (isStoppedX)
        		isStoppedY = true;
    	}
    }
    
    /**
     * Check the ball is within the simulation's horizontal bounds.
     * @return
     */
    private boolean ballInXBounds()
    {
    	if (xPos >= 0.0 && xPos < platformPos.length)
    		return true;
    	else return false;
    }
    
    /**
     * Check the ball is within the simulation's vertical bounds.
     * @return
     */
    private boolean ballInYBounds()
    {
    	if (yPos >= 0.0)
    		return true;
    	else return false;
    }
    
    /**
     * Stop the ball and prevent future movement.
     */
    private void stopBall()
    {
    	xSpeed = 0.0;
    	ySpeed = 0.0;
		isStopped = true;
		data.addStopped();
    }
    
}
