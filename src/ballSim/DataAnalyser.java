package ballSim;
import sim.*;

/**
 * DataAnalyser stores and returns data for BallSim.
 * 
 * @author Robin Smith
 * @version 2 (11/11/2010)
 */

public class DataAnalyser
{

	private IntDataParameter simTime;
    private IntDataParameter stoppedBalls;
    private IntDataParameter ballsOffScreen;
    private IntDataParameter ballsOnScreen;
    private final int N_PARAMS = 4;
    private Parameter[] data = new Parameter[N_PARAMS];

    
    /**
     * Creates a new DataAnalyser object with default initial data values.
     */
    public DataAnalyser()
    {
    	int i = 0;
    	
    	simTime = new IntDataParameter("Sim time", 0, false);
    	data[i++] = simTime;
    	
    	stoppedBalls = new IntDataParameter("Balls stopped", 0, false);
    	data[i++] = stoppedBalls;
    	
    	ballsOffScreen = new IntDataParameter("Balls off screen", 0, false);
    	data[i++] = ballsOffScreen;
    	
    	ballsOnScreen = new IntDataParameter("Balls on screen", 0, false);
    	data[i++] = ballsOnScreen;
    }
    
    /**
     * Gets the current simulation time step.
     * @return simTime the int time step.
     */
    public int getSimTime()
    {
    	return simTime.getValue();
    }
    
    /**
     * Gets the number of balls in the simulation that have stopped moving.
     * @return the number of stopped balls.
     */
    public int getStoppedBalls()
    {
        return stoppedBalls.getValue();
    }
    
    /**
     * Gets the set of data parameters from DataAnalyser.
     * @return the array of Parameters.
     */
    public Parameter[] getParams()
    {
    	return data;
    }

    /**
     * Increments simTime by one.
     */
    public void incSimTime()
    {
    	simTime.incValue();
    }
    
    /**
     * Increments the number of stopped balls by one.
     */
    public void incStoppedBalls()
    {
        stoppedBalls.incValue();
    }
    
    /**
     * Increments the number of off-screen balls by one.
     */
    public void incBallsOffScreen()
    {
    	ballsOffScreen.incValue();
    }
    
    /**
     * Decrements the number of off-screen balls by one.
     */
    public void decBallsOffScreen()
    {
    	ballsOffScreen.decValue();
    }
    
    /**
     * Increments the number of on-screen balls by one.
     */
    public void incBallsOnScreen()
    {
    	ballsOnScreen.incValue();
    }
    
    /**
     * Decrements the number of off-screen balls by one.
     */
    public void decBallsOnScreen()
    {
    	ballsOnScreen.decValue();
    }
    
    /**
     * Resets all of the data values to defaults.
     */
    public void reset()
    {
    	for (Parameter param : data) {
    		param.setDefault();
    	}
    }
    
}