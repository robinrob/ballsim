package ballSim;
import java.util.Random;

import sim.*;


/**
 * BallSim is the main class of the BallSim project. It runs a graphical
 * simulation of some balls bouncing down a set of steps. Many parameters can be
 * adjusted by the user via a GUI.
 * 
 * @author Robin Smith
 * @version 2 (17/11/2010)
 * 
 * Based on a simple project called 'BallDemo' originally written by
 * Michael Kolling and David J. Barnes, for the book 'Objects First with Java 
 * A practical Introduction Using BlueJ' (23Jan2002).
 */

public class BallSim implements Runnable
{	
	private BallSimGUI gui;
    private Ball[] balls;
    private static final String[] BALL_COLORS = {"green", "cyan", "red",
                                                 "yellow", "blue", "white",
                                                 "pink"};
    private static final String[] PLATFORM_COLORS = {"cyan", "green", "red",
                                                     "yellow", "blue", "white",
                                                     "pink"};
    private Platform[] platforms;
    private DataAnalyser data;
    private Random random;
    private static final int SEED = 50;
	/* time step in simulation */
    private static double DELTA_T = 0.2;
    private static final double DEFAULT_TIME_STEP = 8.0;
	/* array which stores the platform positions */
    private int[] platformPositions;
    boolean isRunning = false;
    
	//simulation parameters
    private IntSimParameter simWidth;
    private IntSimParameter simHeight;
	private IntSimParameter nBalls;
	private IntSimParameter nPlatforms;
	private IntSimParameter ballDiameter;
	/* hysteresis of ball on bouncing */
	private DoubleSimParameter ballHyst;
	private DoubleSimParameter gravity;
	/* air resistance */
	private DoubleSimParameter airRes;
	/* platform surface resistance */
	private DoubleSimParameter rollRes;
	/* rightwards wind strength*/
	private DoubleSimParameter rightWind;
	private ColorSimParameter ballColor;
	private ColorSimParameter platColor;
	private DoubleSimParameter simSpeed;
	/* amount of spread in initial ball positions */
	private IntSimParameter ballSpread;
	/* amount of spread in initial ball velocities */
	private IntSimParameter ballVelSpread;

	
	private final int N_PARAMS = 15;
	private Parameter[] params;

    /**
     * Constructor for BallSim. Initialises all the parameters and creates the
     * simulation objects.
     * @param gui the parent GUI isRunning the simulation
     */
    public BallSim(BallSimGUI gui)
    {
    	this.gui = gui;
    	data = new DataAnalyser();
    	params = new Parameter[N_PARAMS];
    	random = new Random(SEED);
    	
    	int i = 0;
    	
    	simWidth = new IntSimParameter("Simulation width", 900, 200, 2000,
    	                               false);
    	params[i++] = simWidth;
    	
    	simHeight = new IntSimParameter("Simulation height", 600, 200, 2000,
    	                                false);
    	params[i++] = simHeight;
    	
    	nBalls = new IntSimParameter("No. of balls", 20, 1, 500, false);
    	params[i++] = (nBalls);
    	
    	nPlatforms = new IntSimParameter("No. of platforms", 5, 1, 100,
    	                                 false);
    	params[i++] = (nPlatforms);
    	
    	ballDiameter = new IntSimParameter("Ball Diameter", 10, 1, 50, false);
    	params[i++] = (ballDiameter);
    	
    	ballHyst = new DoubleSimParameter("Ball hysteresis", 0.3, 0.0, 1.0,
    	                                  true);
    	params[i++] = (ballHyst);
    	
    	gravity = new DoubleSimParameter("Gravity", 6.0, 0.0, 20.0, true);
    	params[i++] = (gravity);
    	
    	airRes = new DoubleSimParameter("Air resistance", 0.001, 0.0, 1.0,
    	                                true);
    	params[i++] = (airRes);
    	
    	rollRes = new DoubleSimParameter("Rolling resistance", 0.01, 0.0, 1.0,
    	                                 true);
    	params[i++] = rollRes;
    	
    	rightWind = new DoubleSimParameter("Right wind", 0.0, 0.0, 100.0, true);
    	params[i++] = rightWind;
    	
    	ballColor = new ColorSimParameter("Ball colour", BALL_COLORS, true);
    	params[i++] = (ballColor);
    	
    	platColor = new ColorSimParameter("Platform colour", PLATFORM_COLORS,
    	                                  true);
    	params[i++] = (platColor);
    	
    	simSpeed = new DoubleSimParameter("Simulation speed", 1.0, 0.125,
    	                                  DEFAULT_TIME_STEP, true);
    	params[i++] = simSpeed;
    	
    	ballSpread = new IntSimParameter("Ball spread", 1000, 1, 10000, false);
    	params[i++] = ballSpread;
    	
    	ballVelSpread = new IntSimParameter("Ball vel. spread", 1000, 1, 10000,
    	                                 false);
    	params[i++] = ballVelSpread;
    	
    	createPlatforms();
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
     * Gets the array of setup Parameters for the simulation.
     * @return an array of type Parameter.
     */
    public Parameter[] getSimParams()
    {
    	return params;
    }
    
    
    /**
     * Gets the dynamic data parameters of the simulation.
     * @return an array of type Parameter.
     */
    public Parameter[] getDataParams()
    {
    	return data.getParams();
    }
    
    /**
     * 
     * @return the width of the simulation space
     */
    public int getSimWidth()
    {
    	return simWidth.getValue();
    }
    
    /**
     * 
     * @return the height of the simulation space
     */
    public int getSimHeight()
    {
    	return simHeight.getValue();
    }
    
    /**
     * Returns a boolean value to indicate whether or not the simulation is
     * currently isRunning.
     * @return true or false
     */
    public boolean isRunning() {
    	return isRunning;
    }
    
    /**
     * Creates all the platforms and balls in the simulation.
     */
    public void createObjects()
    {
    	createPlatforms();
    	createBalls();
    }
    
	/**
	 * Creates the platforms and the int[] platformPositions array and fills it
	 * with the heights of the platforms.
	 */
    public void createPlatforms()
    {
    	int n = nPlatforms.getValue();
        platforms = new Platform[n];
        int length = simWidth.getValue() / n;
    	platformPositions = new int[simWidth.getValue()];
        /* vertShift is the vertical distance between platforms */
        int vertShift = (int) ((0.5 * simHeight.getValue())/(double) n);
        
        for (int i = 0; i < n; ++i) {
            int xPos = i * length;
            int yPos = (int) (0.5 * simHeight.getValue()) + (i * vertShift);      
            
            platforms[i] = new Platform(xPos, yPos, length, gui.getCanvas()); 
            
            /* add the remainder onto last platform */
            if (i == (n - 1)) {
        		length += (simWidth.getValue() % n); 
        	}
            /* this loop records the platform positions in the simulation */
            for (int j = xPos; j < (xPos + length); ++j) {
            	platformPositions[j] = yPos - ballDiameter.getValue();
            }
        }
    }

    /**
     * Creates the balls in the simulation.
     */
    private void createBalls()
    {
    	int n = nBalls.getValue();
        balls = new Ball[n];
        for (int i = 0; i < n; ++i){
            balls[i] =
            	new Ball(100 + 0.025 * random.nextInt(ballSpread.getValue()),
            	         20 + 0.02 * random.nextInt(ballSpread.getValue()),
            	         8 + 0.01 * random.nextInt(ballVelSpread.getValue()),
            	         0.01 * random.nextInt(ballVelSpread.getValue()),
            	         ballDiameter,
            	         gravity,
            	         ballHyst,
            	         airRes,
            	         rollRes,
            	         rightWind,
            	         gui.getCanvas(),
            	         platformPositions,
            	         data);
        }
    }
    
    /**
     * Draws the platforms in the simulation onto the canvas.
     */
    public void drawPlatforms()
    {
        for (Platform platform : platforms) {
        	platform.draw(platColor.getValue());
        }
    }
    
    /**
     * Draws all the balls in the simulation onto the canvas.
     */
    public void drawBalls()
    {
    	for (Ball ball : balls) {
    		ball.draw(ballColor.getValue());
    	}
    }
    
    /**
     * Causes the run() method to return.
     */
    public void stop()
    {
    	isRunning = false;
    }
    
    /**
     * Resets the BallSim's DataAnalyser.
     */
    public void resetData()
    {
    	data.reset();
    }
        
    /**
     * Run the simulation.
     */
    public void run()
    {
    	isRunning = true;
    	/* start the simulation */
    	while(isRunning) {
    		for (Ball ball : balls) {
    			ball.move(DELTA_T);
    		}
    		/* ball drawing is done AFTER move() because of balls erasing other
    		 * balls in shared space when drawn/erased sequentially */
    		drawBalls();
    		drawPlatforms();
    		if (data.getStoppedBalls() == nBalls.getValue()) { 
    			isRunning = false;
    		}
    		data.incSimTime();
    		gui.wait((int) (DEFAULT_TIME_STEP / (simSpeed.getValue())));
    	}
    	gui.simFinished();
    }
    
}