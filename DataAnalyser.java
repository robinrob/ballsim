/**
 * Class DataAnalyser - calculates, stores and returns data for BallSim.
 * 
 * @author Robin Smith
 * @version 1 (19/10/2010)
 */

public class DataAnalyser
{

	private int time = 0;
    private int stopped = 0;
    
    /**
     * Constructor for DataAnalyser.
     */
    public DataAnalyser()
    {
    }
    
    /**
     * Increment time by one.
     */
    public void addTime()
    {
    	++time;
    }
    
    /**
     * 
     * @return time
     */
    public int getTime() {
    	return time;
    }
    
    /**
     * Increment the number of stopped balls by one.
     */
    public void addStopped() {
        ++stopped;
    }
    
    /**
     * @return the number of balls in the simulation that have stopped moving
     */
    public int getStopped() {
        return stopped;
    }
    
}