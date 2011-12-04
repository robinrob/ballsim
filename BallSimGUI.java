import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.*;;

/**
 * Class BallSimGUI  a GUI for the BallSim project
 * 
 * @author Robin Smith
 * @version 1 (19/10/2010)
 * 
 * Based on a simple project originally written by Michael Kolling
 * and David J. Barnes, for the book 'Objects First with Java  A practical
 * Introduction Using BlueJ' (23Jan2002).
 */

public class BallSimGUI {

	private static final long serialVersionUID = 1217566099994443321L;
	//GUI details
	private static final String VERSION = "Version 1";
    private static final String ICON_IMAGE = "icon.jpg";
    private static final String DATE = "19/10/2010";
    private static final String AUTHOR = "Robin Smith";
	
    /* vertical position of input form for user input */
    private final int INPUT_PANEL_POS = 100; 
    private final int FRAME_WIDTH;
    private final int FRAME_HEIGHT;
    
    //canvas display parameters
    private final int CANVAS_WIDTH;
    private final int CANVAS_HEIGHT;
	private final Color BG_COLOUR = Color.black;
	private final Color TEXT_COLOUR = Color.white;
	private final Color PLATFORM_COLOUR = Color.cyan;
	
    //user input prompts to be used with InputPanel
    private String[] prompts = {"Number of balls:", "Number of platforms:",
    		"Ball diameter:", "Ball colour:", "Ball hysteresis:", "Gravity:",
    		"Air resistance:", "Rolling resistance:", "Ball scatter:", 
    		"Ball dispersion (x):", "Ball dispersion (y):"};
	
	//GUI components
	private JFrame frame;
	private Container contentPane;
    private Canvas canvas; //drawing canvas on which the simulation is drawn
	private JButton runButton;
	private JButton stopButton;
	private JButton resetButton;
    
	//simulation objects
    private BallSim sim;
    private Thread simThread; //separate thread for the simulation
    private Platform[] platforms;
    
	//simulation parameters determined by the user
	private int nBalls;
	private int nPlatforms;
	private int ballDiameter;
	private Color ballColour;
	private double gravity;
	private double ballHyst; //energy loss of ball on bouncing
	private double airRes;
	private double surfRes; //platform surface resistance
	private int scatter; //spread in initial positions of balls
	private int dispersionX; //spread in initial horizontal speeds of balls
	private int dispersionY; //spread in initial vertical speeds of balls
	private String[] params; //an array to hold the parameters in string form

	
	/**
	 * Constructor for BallSimGUI.
	 * @param sim the BallSim object running the simulation
	 * @param simWidth width of simulation space
	 * @param simHeight height of simulation space
	 * @param defaultParams default parameters for the simulation
	 */
    public BallSimGUI(BallSim sim, int simWidth, int simHeight,
    		String[] defaultParams)
    {
    	FRAME_WIDTH = simWidth;
    	FRAME_HEIGHT = simHeight + 50; //50 extra pixels for menu bar etc.
    	CANVAS_WIDTH = simWidth;
    	CANVAS_HEIGHT = simHeight;
    	
    	this.sim = sim;
    	canvas = new Canvas(CANVAS_WIDTH, CANVAS_HEIGHT, BG_COLOUR);
    	params = sim.getDefaultParams();
    	
    	makeFrame(FRAME_WIDTH, FRAME_HEIGHT);
    	addInputPanel(params);
    	frame.pack();
    	frame.setVisible(true);
    }
    
    /**
     * 
     * @return the canvas' contentPane
     */
    public Canvas getCanvas()
    {
    	return canvas;
    }
    
    /**
     * Pause the GUI thread for 'time' milliseconds.
     * @param time
     */
    public void wait (int time)
    {
    	canvas.wait(time);
    }

    //GUI construction methods
    
    /**
     * Construct the GUI frame.
     */
    private void makeFrame(int width, int height)
    {
    	frame = new JFrame("Ball Simulation");    	
    	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    	frame.setPreferredSize(new Dimension(width, height));
    	BufferedImage iconImage = loadImage(ICON_IMAGE);
        frame.setIconImage(iconImage);
    	
        /* retrieve the frame's content pane for later use */
    	contentPane = frame.getContentPane();
    	contentPane.setLayout(new BorderLayout());
        
        makeMenuBar();
    }
    
    /**
     * Obtains an image with name 'filename'.
     * @param filename the path of the image
     * @return a BufferedImage object if file 'filename' exists
     */
    private BufferedImage loadImage(String filename)
    {
        BufferedImage returnImage = null;
        File file = new File(filename);
        
        if (file.exists()) {
            try {
                returnImage = ImageIO.read(file);
            } 
            catch (IOException e) {
            }

        }
        return returnImage;
    }
    
    /**
     * Creates the menu bar and adds it to the GUI's frame.
     */
    private void makeMenuBar()
    {
        final int SHORTCUT_MASK =
            Toolkit.getDefaultToolkit().getMenuShortcutKeyMask();

        JMenuBar menuBar = new JMenuBar();
        frame.setJMenuBar(menuBar);
        
        //create the File menu
        JMenu menu = new JMenu("File");
        menuBar.add(menu);

        JMenuItem item = new JMenuItem("Quit");
        item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, SHORTCUT_MASK));
        item.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) { System.exit(0); }
        	});
        menu.add(item);
        
        //create the Help menu
        menu = new JMenu("Help");
        menuBar.add(menu);
        
        item = new JMenuItem("About BallSim ...");
            item.addActionListener(new ActionListener() {
                               public void actionPerformed(ActionEvent e) { showAbout(); }
                           });
        menu.add(item);

    }

    /**
     * Displays an 'About' window for the program.
     */
    private void showAbout()
    {
        JOptionPane.showMessageDialog(frame, 
                    "BallSim\n" + VERSION + " (" + DATE + ")\n" + AUTHOR,
                    "About BallSim", 
                    JOptionPane.INFORMATION_MESSAGE);
    }
    
    /**
     * Adds an InputPanel form to the contentPane to get the simulation
     * parameters from the user, with a button to begin the simulation.
     */
    private void addInputPanel(String[] fieldValues)
    {
    	JPanel padding = new JPanel();
    	padding.setPreferredSize(new Dimension(FRAME_WIDTH, INPUT_PANEL_POS));
    	contentPane.add(padding, BorderLayout.NORTH);
    	
        contentPane.add(new InputPanel(this, prompts, fieldValues),
        		BorderLayout.CENTER);
        frame.pack();
    }
    
    /**
     * Make all the buttons used for controlling the simulation.
     */
    private void makeButtons()
    {
    	runButton = new JButton("Run");
        runButton.addActionListener(new ActionListener() { 
        	public void actionPerformed(ActionEvent e) { runSim(); }
        	});
        
    	stopButton = new JButton("Stop");
    	stopButton.addActionListener(new ActionListener() { 
    		public void actionPerformed(ActionEvent e) { 
        		stopSim(); 
        		}
         	});
    	
    	resetButton = new JButton("Reset");
    	resetButton.addActionListener(new ActionListener() { 
    		public void actionPerformed(ActionEvent e) { 
    			resetGUI();
        		}
         	});    	
    }
    
    /**
     * Draw the simulation's platforms onto the canvas.
     */
	private void drawPlatforms()
	{
        for (Platform platform : platforms)
        	platform.draw(PLATFORM_COLOUR);
    }
	
    /**
     * Draw a display on the canvas showing the input parameters
     * of the simulation.
     */
    private void drawParametersDisplay()
    {
    	/* Positions of text are defined as a fraction of the canvas size for
    	 * automatic adjustment to different canvas sizes.
    	 */
    	double xPos = 0.02;
        double yPos = 0.70;
        
    	canvas.setForegroundColor(TEXT_COLOUR);
    	canvas.drawString("number of balls:  " + nBalls,
    			(int) (CANVAS_WIDTH * xPos), (int) (CANVAS_HEIGHT * yPos));
    	canvas.drawString("number of platforms:  " + nPlatforms,
    			(int) (CANVAS_WIDTH * xPos),
    			(int) (CANVAS_HEIGHT * (yPos + 0.03)));
    	canvas.drawString("gravity:  " + gravity,
    			(int) (CANVAS_WIDTH * xPos),
    			(int) (CANVAS_HEIGHT * (yPos + 0.06)));	
    	canvas.drawString("ball hysteresis:  " + ballHyst,
    			(int) (CANVAS_WIDTH * xPos),
    			(int) (CANVAS_HEIGHT * (yPos + 0.09)));
    	canvas.drawString("air resistance:  " + airRes,
    			(int) (CANVAS_WIDTH * xPos),
    			(int) (CANVAS_HEIGHT * (yPos + 0.12)));
    	canvas.drawString("surface resistance:  " + surfRes,
    			(int) (CANVAS_WIDTH * xPos),
    			(int) (CANVAS_HEIGHT * (yPos + 0.15)));
    	canvas.drawString("scatter:  " + scatter,
    			(int) (CANVAS_WIDTH * xPos),
    			(int) (CANVAS_HEIGHT * (yPos + 0.18)));
    	canvas.drawString("dispersion (x):  " + dispersionX,
    			(int) (CANVAS_WIDTH * xPos),
    			(int) (CANVAS_HEIGHT * (yPos + 0.21)));
    	canvas.drawString("dispersion (y):  " + dispersionY,
    			(int) (CANVAS_WIDTH * xPos),
    			(int) (CANVAS_HEIGHT * (yPos + 0.24)));
    }
	
    /**
     * Draw some interesting real-time simulation data onto the canvas.
     */
    private void drawDataDisplay()
    {
    	double xPos = 0.25;
        double yPos = 0.84;
        
        canvas.setForegroundColor(TEXT_COLOUR);
        
        canvas.drawString("Balls stopped:", (int) (CANVAS_WIDTH * xPos),
        		(int) (CANVAS_HEIGHT * yPos));
        
        canvas.drawString("Simulator time:", (int) (CANVAS_WIDTH * xPos),
        		(int) (CANVAS_HEIGHT * (yPos + 0.05)));
        
        canvas.drawString("(Stops when all balls are stopped)",
        		(int) (CANVAS_WIDTH * xPos),
        		(int) (CANVAS_HEIGHT * (yPos + 0.10)));  
    }
    
    /**
     * Erase the data values from the data display on the canvas 
     * and redraws them with their new values.
     */
    private void refreshDataDisplay()
    {
        double xPos = 0.37;
        double yPos = 0.84;
        
        Rectangle fillArea = new Rectangle(
        		(int) (CANVAS_WIDTH * xPos),
        		(int) (CANVAS_HEIGHT * (yPos - 0.04)),
        		40, (int) (CANVAS_HEIGHT * 0.11));
        canvas.setForegroundColor(BG_COLOUR);
        canvas.fill(fillArea);
        
        canvas.setForegroundColor(TEXT_COLOUR);
        canvas.drawString("" + sim.getData().getStopped(), (int) (CANVAS_WIDTH * xPos),
        		(int) (CANVAS_HEIGHT * yPos));
        
        canvas.drawString("" + sim.getData().getTime(),
        		(int) (CANVAS_WIDTH * xPos),
        		(int) (CANVAS_HEIGHT * (yPos + 0.05)));
        
    }
    
    /**
     * Draw a start message on the canvas.
     */
    private void drawStartMessage()
    {
    	canvas.setForegroundColor(TEXT_COLOUR);
    	canvas.drawString("Press 'Run' to begin", 
    			(int) (CANVAS_WIDTH * 0.42),
    			(int) (CANVAS_HEIGHT * 0.3));
    }
    
    /**
     * Erase the start message.
     */
    private void eraseStartMessage()
    {
    	 Rectangle fillArea = new Rectangle(
         		(int) (CANVAS_WIDTH * 0.42), (int) (CANVAS_HEIGHT * 0.28),
         		120, (int) (CANVAS_HEIGHT * 0.025));
         canvas.setForegroundColor(BG_COLOUR);
         canvas.fill(fillArea);
    }
    
    
    /**
     * Returns a Color object corresponding to the input string.
     * @param s a string like "red", "blue", "green" etc.
     * @return a Color
     */
    private Color stringToColor(String s) {
    	if (s.equals("red")) {
    		return Color.red;
    	}
    	else if (s.equals("blue")) {
    		return Color.blue;
    	}
    	else if (s.equals("green")) {
    		return Color.green;
    	}
    	else if (s.equals("white")) {
    		return Color.white;
    	}
    	else if (s.equals("yellow")) {
    		return Color.yellow;
    	}
    	else {
    		return Color.yellow;
    	}
	}
        
    //Simulation runtime methods
    
    /**
     * Set up the BallSim using the input parameters from the user.
     */
    public void setUpBallSim(String[] params) {
    	this.params = params;
    	int i = 0;
    	nBalls = Integer.parseInt(params[i++].trim());
    	nPlatforms = Integer.parseInt(params[i++].trim());
    	ballDiameter = Integer.parseInt(params[i++].trim());
    	ballColour = stringToColor(params[i++].trim());
    	ballHyst = Double.parseDouble(params[i++].trim());
    	gravity = Double.parseDouble(params[i++].trim());
    	airRes = Double.parseDouble(params[i++].trim());
    	surfRes = Double.parseDouble(params[i++].trim());
    	scatter = Integer.parseInt(params[i++].trim());
    	dispersionX = Integer.parseInt(params[i++].trim());
    	dispersionY = Integer.parseInt(params[i++].trim());
    	
    	sim.setUpBallSim(nBalls, nPlatforms, ballDiameter, ballColour,
        		gravity, ballHyst, airRes, surfRes, scatter, dispersionX, 
        		dispersionY);
        platforms = sim.getPlatforms();
        
        setUpSimDisplay();
    }
    
    /**
     * Set up the GUI display ready for the simulation to begin.
     */
    private void setUpSimDisplay()
    {
    	contentPane.removeAll();
        contentPane.add(canvas.getCanvasPane(), BorderLayout.CENTER);
        canvas.setVisible(true);
        
    	drawPlatforms();
        drawParametersDisplay();
        drawDataDisplay();
        drawStartMessage();
        
        makeButtons(); //make the buttons for the GUI
        contentPane.add(runButton, BorderLayout.NORTH);
        
        frame.pack();
        frame.repaint();
    }
    
    /**
     * Refresh the GUI display.
     */
    public void refresh() {
    	refreshDataDisplay();
    	canvas.repaint();
    	wait(1);
    }
    
    /**
     * Run the simulation.
     */
    private void runSim()
    {
    	contentPane.remove(runButton);
    	contentPane.add(stopButton, BorderLayout.NORTH);
    	frame.pack();
    	eraseStartMessage();
    	simThread = new Thread(sim);
    	simThread.start();
    }
    
    /**
     * Stop the simulation.
     */
    private void stopSim()
    {
    	simThread.stop();
    	contentPane.remove(stopButton);
    	contentPane.add(resetButton, BorderLayout.NORTH);
    	frame.pack();
    }
    
    /**
     * Reset the GUI.
     */
    public void resetGUI()
    {
    	contentPane.remove(resetButton);
    	contentPane.remove(canvas.getCanvasPane());
    	canvas = new Canvas(CANVAS_WIDTH, CANVAS_HEIGHT, BG_COLOUR);
    	addInputPanel(params);
    }
    
    /**
     * Change the GUI when the simulation has finished.
     */
    public void simFinished()
    {
    	contentPane.remove(stopButton);
    	contentPane.add(resetButton, BorderLayout.NORTH);
    	frame.pack();
    }
    
}
    