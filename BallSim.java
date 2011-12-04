import java.awt.*;
//import java.awt.geom.*;
//import java.util.HashMap;
//import java.lang.Integer;

import java.util.Random;

/**
 * Class BallSim  runs a graphical simulation of some balls bouncing down a set
 * of steps. Many parameters can be adjusted by the user via a GUI.
 * 
 * @author Robin Smith
 * @version 1 (19/10/2010)
 * 
 * Based on a simple project called 'BallDemo' originally written by
 * Michael Kolling and David J. Barnes, for the book 'Objects First with Java 
 * A practical Introduction Using BlueJ' (23Jan2002).
 */

public class BallSim implements Runnable
{	
	//GUI settings
    private BallSimGUI gui;
	
	//simulation parameters set to defaults
    private int simWidth = 900;
    private int simHeight = 600;
	private int nBalls = 20;
	private int nPlatforms = 5;
	private int ballDiameter = 10;
	private Color ballColour = Color.yellow;
	private double ballHyst = 0.2; //hysteresis of ball on bouncing
	private double gravity = 10.0;
	private double airRes = 0.001; //air resistance
	private double surfRes = 0.01; //platform surface resistance
	private int scatter = 1000; //initial position spread of balls
	private int dispersionX = 1000; //initial horizontal speed dispersion
	private int dispersionY = 1000; //initial vertical speed dispersion
    private String[] defaultParams = {"" + nBalls,
    								  "" + nPlatforms,
    								  "" + ballDiameter,
    								  "" + ballColour.toString(),
    								  "" + ballHyst,
    								  "" + gravity,
    								  "" + airRes,
    								  "" + surfRes,
    								  "" + scatter,
    								  "" + dispersionX,
    								  "" + dispersionY};
    
    //simulation objects
    private BouncingBall[] balls;
    private Platform[] platforms;
    private Random random;
    private DataAnalyser data;
    
    //other variables
    
	/* height of first platform */
	private final int PLAT_1_HEIGHT = (int) (simHeight * 0.5);
	
	/* array which stores the platform heights as a function of x, and is also
	 * used to represent the simulation boundaries */
    private final int[] platformPositions = new int[simWidth]; 
    private double delta_t = 0.2; //time increment in simulation
	
    /**
     * Constructor of a BallSim object. Creates the GUI.
     */
    public BallSim()
    {
    	gui = new BallSimGUI(this, simWidth, simHeight, defaultParams);
   
    }
    
    /**
     * 
     * @return the DataAnalyser used in the simulation
     */
    public DataAnalyser getData()
    {
    	return data;
    }
    
    /**
     * 
     * @return the platforms used in the simulation
     */
    public Platform[] getPlatforms()
    {
    	return platforms;
    }
    
    /**
     * 
     * @return the default parameters for the simulation
     */
    public String[] getDefaultParams()
    {
    	return defaultParams;
    }
    
    /**
     * Set up the BallSim using input parameters.
     * @param nBalls
     * @param nPlatforms
     * @param ballDiameter
     * @param ballColour
     * @param gravity
     * @param ballHyst energy loss of balls when bouncing
     * @param airRes air resistance
     * @param surfRes platform resistance to rolling balls
     * @param scatter spread in initial position of balls
     * @param dispersionX spread in initial horizontal speeds of balls
     * @param dispersionY spread in initial vertical speeds of balls
     */
    public void setUpBallSim(int nBalls, int nPlatforms, int ballDiameter,
    		Color ballColour, double gravity, double ballHyst, double airRes,
    		double surfRes, int scatter, int dispersionX, int dispersionY)
    {
    	this.nBalls = nBalls;
    	this.nPlatforms = nPlatforms;
    	this.ballDiameter = ballDiameter;
    	this.ballColour = ballColour;
    	this.gravity = gravity;
    	this.ballHyst = ballHyst;
    	this.airRes = airRes;
    	this.surfRes = surfRes;
    	this.scatter = scatter;
    	this.dispersionX = dispersionX;
    	this.dispersionY = dispersionY;
        data = new DataAnalyser();
        
    	createObjects();
    }
    
	/**
	 * Create n platforms and record their heights in the simulation.
	 * @param n
	 */
    private void createPlatforms(int n)
    {
        platforms = new Platform[n];
        int length = (platformPositions.length)/n;
        /* vertShift is the vertical distance between platforms */
        int vertShift = (int) ((0.5 * simHeight)/n);
        
        for (int i = 0; i < n; ++i) {
            int xPos = i * length;
            int yPos = PLAT_1_HEIGHT + i * vertShift;
            
        	/* this statement stops platforms going off the bottom of the screen
        	 * when there is a large number of them.
        	 */
            if (yPos > (simHeight - 1 - ballDiameter)) {
            	yPos = simHeight - 1 - ballDiameter;
            }
            
            platforms[i] = new Platform(xPos, yPos, length, gui.getCanvas());
            
            /* this loop records the platform heights in the simulation */
            for (int x = xPos; x < (xPos + length); ++x) {
            	platformPositions[x] = yPos - ballDiameter;
            }
        }
    }

    /**
     * Create n Balls.
     * @param n
     */
    private void createBalls(int n)
    {
        balls = new BouncingBall[n];
        random = new Random(50);
        for (int i = 0; i < n; ++i){
            balls[i] = new BouncingBall(
            		100 + 0.025 * random.nextInt(scatter),
            		20 + 0.02 * random.nextInt(scatter),
            		0.02 * random.nextInt(dispersionX),
            		0.01 * random.nextInt(dispersionY),
            		ballDiameter, ballHyst, gravity, airRes, surfRes, 
            		gui.getCanvas(), platformPositions, data);
        }
    }
    
    /**
     * Create the platforms and balls.
     */
    private void createObjects()
    {
        createPlatforms(nPlatforms);
        createBalls(nBalls);
    }
    
    /**
     * Run the simulation.
     */
    public void run()
    {   
    	boolean running = true;
    	
    	/* start the simulation */
    	while(running) {
    		for (BouncingBall ball : balls) {
    			ball.move(delta_t);
    		}
    		/* ball drawing is done AFTER move() because of balls erasing other
    		 * balls in shared space when drawn/erased sequentially
    		 */
    		for (BouncingBall ball : balls) {
				ball.draw(ballColour);
    		}
    		if (data.getStopped() == nBalls) { 
    			running = false;
    		}
    		data.addTime();
    		gui.refresh();
    	}
    	gui.simFinished();
    }
    
    /**
     * Main function of the BallSim project.
     * @param args
     */
    public static void main(String[] args)
    {
    	new BallSim();
    }
    
}