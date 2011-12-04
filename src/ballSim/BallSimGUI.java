package ballSim;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.*;
import sim.*;

/**
 * BallSimGUI is the GUI and main class for the BallSim project.
 * 
 * @author Robin Smith
 * @version 2 (17/11/2010)
 */

public class BallSimGUI implements SimGUI
{
    private static final long serialVersionUID = 1217566099994443321L;
    //program details
    private static final String VERSION = "Version 2";
    private static final String ICON_IMAGE = "icons/icon.jpg";
    private static final String DATE = "17/11/2010";
    private static final String AUTHOR = "Robin Smith";
    
    //canvas display parameters
    private final Color BG_COLOR = Color.black;
    private final Color TEXT_COLOR = Color.white;
	
    //GUI components
    private static final int SHORTCUT_MASK = 
    	Toolkit.getDefaultToolkit().getMenuShortcutKeyMask();
    private JFrame mainFrame;
    private Container contentPane;
    private JFrame paramFrame;
    private JFrame dataFrame;
    private Canvas canvas; //drawing canvas on which the simulation is drawn
    private JButton runButton;
    private JButton stopButton;
    private JButton resetButton;
    
    //simulation objects
    private BallSim sim;
    private Thread simThread; //separate thread for the simulation
	
    /**
     * Constructor for BallSimGUI. Makes the buttons of the simulation view, the
     * main GUI frame, the Parameter frame and displays the initial setup panel.
     */
    public BallSimGUI()
    {
    	sim = new BallSim(this);
        makeSimButtons();
    	makeMainFrame();
    	makeParamFrame();
    	makeDataFrame();
    	showSetupView();
    }
    
    /**
     * Gets the drawing canvas of the GUI.
     * @return the GUI's Canvas
     */
    public Canvas getCanvas()
    {
    	return canvas;
    }
    
    /**
     * Pauses the GUI thread for a specified number of milliseconds.
     * @param time an integer number of milliseconds.
     */
    public void wait (int time)
    {
    	canvas.wait(time);
    }
    
    /**
     * Obtains an image with the relative path 'filename'.
     * @param filename the relative path of the image file
     * @return a BufferedImage object if file 'filename' exists, null otherwise
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
     * Makes the main GUI window.
     */
    private void makeMainFrame()
    {
    	mainFrame = new JFrame("Ball Simulation");
    	mainFrame.setResizable(false);
    	mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    	BufferedImage iconImage = loadImage(ICON_IMAGE);
        mainFrame.setIconImage(iconImage);
    	
    	makeMenuBar();
    	
        contentPane = mainFrame.getContentPane();
    	mainFrame.setLayout(new BorderLayout());
    }
    
    /**
     * Makes the menu bar and adds it to the main GUI window.
     */
    private void makeMenuBar()
    {
        JMenuBar menuBar = new JMenuBar();
        mainFrame.setJMenuBar(menuBar);
        
        makeFileMenu();
        makeViewMenu();
        makeHelpMenu();
    }
    
    /**
     * Makes the 'File' menu for the main frame's menubar.
     */
    private void makeFileMenu()
    {
    	JMenuBar menuBar = mainFrame.getJMenuBar();
        JMenu menu = new JMenu("File");
        menuBar.add(menu);

        JMenuItem item = new JMenuItem("Quit");
        item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q,
                                                   SHORTCUT_MASK));
        item.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) { System.exit(0); }
        	});
        menu.add(item);
    }
    
    /**
     * Makes the 'View' menu for the main frame's menubar.
     */
    private void makeViewMenu()
    {
    	JMenuBar menuBar = mainFrame.getJMenuBar();
        JMenu menu = new JMenu("View");
        menuBar.add(menu);
      
        JMenuItem item = new JMenuItem("Setup");
        item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q,
                                                   SHORTCUT_MASK));
        item.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) { resetGUI(); }
        	});
        menu.add(item);
        
        item = new JMenuItem("Parameters");
        item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q,
                                                        SHORTCUT_MASK));
        item.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) { 
        			showParamFrame();
        		}
        	});
        menu.add(item);
        
        item = new JMenuItem("Data");
        item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q,
                                                        SHORTCUT_MASK));
        item.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) { showDataFrame(); }
        	});
        menu.add(item);
    }
    
    /**
     * Makes the 'Help' menu for the main frame's menubar.
     */
    private void makeHelpMenu()
    {
    	JMenuBar menuBar = mainFrame.getJMenuBar();
        JMenu menu = new JMenu("Help");
        menuBar.add(menu);
        
        JMenuItem item = new JMenuItem("About BallSim ...");
        item.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) { showAbout(); }
            });
        menu.add(item);
    }
    
    /**
     * Makes all the buttons of the simulation display.
     */
    private void makeSimButtons()
    {
    	runButton = new JButton("Run");
        runButton.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) { runSim(); }
        	});
        
    	stopButton = new JButton("Stop");
    	stopButton.addActionListener(new ActionListener() { 
    		public void actionPerformed(ActionEvent e) { stopSim(); }
         	});
    	
    	resetButton = new JButton("Reset");
    	resetButton.addActionListener(new ActionListener() { 
    		public void actionPerformed(ActionEvent e) { resetGUI(); }
         	});    	
    }
    
    /**
     * Makes the window used for displaying the simulation parameters.
     */
    private void makeParamFrame()
    {
    	paramFrame = new JFrame("Parameters");
    	paramFrame.setResizable(false);
    	BufferedImage iconImage = loadImage(ICON_IMAGE);
        paramFrame.setIconImage(iconImage);
    }
    
    /**
     * Makes the window used for displaying the simulation data.
     */
    private void makeDataFrame()
    {
    	dataFrame = new JFrame("Data");
    	dataFrame.setResizable(false);
    	BufferedImage iconImage = loadImage(ICON_IMAGE);
        dataFrame.setIconImage(iconImage);
    }
        
    /**
     * Displays an 'About' window for the program.
     */
    private void showAbout()
    {
        JOptionPane.showMessageDialog(mainFrame, 
                    "BallSim\n" + VERSION + " (" + DATE + ")\n" + AUTHOR,
                    "About BallSim", 
                    JOptionPane.INFORMATION_MESSAGE);
    }
    
    /**
     * Displays the window showing the simulation parameters.
     */
    private void showParamFrame()
    {
    	AdjustableParamPanel paramPanel;
    	paramPanel = new AdjustableParamPanel(sim.getSimParams());
    	paramFrame.getContentPane().removeAll();
        paramFrame.add(paramPanel);
        paramFrame.pack();
        paramFrame.setVisible(true);
        FramePosition.positionFrame(paramFrame, FramePosition.WEST, mainFrame);
    }
    
    /**
     * Displays the window showing the simulation data.
     */
    private void showDataFrame()
    {
    	DataParamPanel dataPanel = new DataParamPanel(sim.getDataParams());
    	dataFrame.getContentPane().removeAll();
    	dataFrame.add(dataPanel);
    	dataFrame.pack();
    	dataFrame.setVisible(true);
        FramePosition.positionFrame(dataFrame, FramePosition.CENTER);
    }
    
    /**
     * Displays the simulation setup window.
     */
    private void showSetupView()
    {
    	paramFrame.setVisible(false);
    	dataFrame.setVisible(false);
    	SetupParamPanel setupPanel = new SetupParamPanel(sim.getSimParams(),
    	                                                 this);
    	contentPane.removeAll();
    	contentPane.add(setupPanel);
        mainFrame.pack();
        FramePosition.positionFrame(mainFrame, FramePosition.CENTER);
        mainFrame.setTitle("BallSim setup");
        mainFrame.setVisible(true);
    }
    
    /**
     * Shows the simulation view ready for the simulation to begin.
     */
    public void showSimView()
    {
    	canvas = new Canvas(sim.getSimWidth(), sim.getSimHeight(), BG_COLOR);
    	mainFrame.getContentPane().removeAll();
    	mainFrame.setLayout(new BorderLayout());
    	contentPane.add(canvas.getCanvasPane(), BorderLayout.CENTER);
        canvas.setVisible(true);
        contentPane.add(runButton, BorderLayout.NORTH);
        mainFrame.pack();
        
        sim.createObjects();
        sim.drawPlatforms();
        sim.drawBalls();
        drawStartMessage();

        FramePosition.positionFrame(mainFrame, FramePosition.CENTER);
        mainFrame.setTitle("BallSim");
        mainFrame.setVisible(true);
        showParamFrame();
        showDataFrame();
    }
    
    /**
     * Draws a start message on the simulation canvas.
     */
    private void drawStartMessage()
    {
    	canvas.setForegroundColor(TEXT_COLOR);
    	
    	int width = 26;
    	String message = Formatter.centerJustify("BallSim " + VERSION,
    	                                         width);
    	canvas.drawString(message,
    	                  (int) (sim.getSimWidth() * 0.42),
    	                  (int) (sim.getSimHeight() * 0.3));
    	message = Formatter.centerJustify("Press 'Run' to begin", width);
    	canvas.drawString(message,
    	                  (int) (sim.getSimWidth() * 0.42),
    	                  (int) (sim.getSimHeight() * 0.33));
    }
    
    /**
     * Erases the start message from the simulation canvas.
     */
    private void eraseStartMessage()
    {
    	 Rectangle fillArea = new Rectangle(
    	                                    (int) (sim.getSimWidth() * 0.42),
    	                                    (int) (sim.getSimHeight() * 0.28),
    	                                    130,
    	                                    (int) (sim.getSimHeight() * 0.055));
         canvas.setForegroundColor(BG_COLOR);
         canvas.fill(fillArea);
    }
        
    /**
     * Runs the simulation.
     */
    private void runSim()
    {
    	contentPane.remove(runButton);
    	contentPane.add(stopButton, BorderLayout.NORTH);
    	mainFrame.pack();
    	eraseStartMessage();
    	mainFrame.repaint();
    	simThread = new Thread(sim);
    	simThread.start();
    }
    
    /**
     * Stops the simulation.
     */
    private void stopSim()
    {
    	sim.stop();
    	contentPane.remove(stopButton);
    	contentPane.add(resetButton, BorderLayout.NORTH);
    	mainFrame.pack();
    }
    
    /**
     * Resets the GUI to the setup window.
     */
    public void resetGUI()
    {
    	if (sim.isRunning()) {
    		sim.stop();
    	}
    	sim.resetData();
    	showSetupView();
    }
    
    /**
     * Makes changes to the GUI relevant to the simulation finishing.
     */
    public void simFinished()
    {
    	contentPane.remove(stopButton);
    	contentPane.add(resetButton, BorderLayout.NORTH);
    	mainFrame.pack();
    }
    
    /**
     * Main method for the BallSim project.
     * @param args
     */
    public static void main(String[] args)
    {
    	new BallSimGUI();
    }

    
}
    