import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/**
 * Class InputPanel  an input form based on JPanel. Used by the user to set the
 * simulation parameters.
 * 
 * @author Robin Smith
 * @version 1 (19/10/2010)
 */

public class InputPanel extends JPanel
{
	private static final long serialVersionUID = 2407717884636741269L;
	
	//display settings
    private final int FIELD_HEIGHT = 20;
    private final int BUTTON_HEIGHT = 20;
    public static final int WIDTH = 300;
    private static final int TEXTLENGTH = 20;
    
    //instance variables
    private BallSimGUI gui;
    private int nValues;
    private String[] paramPrompts;
    private String[] paramDefaults;
    private String[] userInputs;
    private JTextField[] fields;
    private String[] colours = {"green", "yellow", "blue", "red", "white"};
    private JComboBox colourList;

    /**
     * Constructor for InputPanel.
     * @param gui the gui using this InputPanel
     * @param paramPrompts prompts for the parameters
     * @param paramDefaults default parameters
     */
    public InputPanel(BallSimGUI gui, String[] paramPrompts,
    		String[] paramDefaults)
    {
    	this.gui = gui;
    	this.paramPrompts = paramPrompts;
    	this.paramDefaults = paramDefaults;
    	nValues = paramPrompts.length;
        userInputs = new String[nValues];
        fields = new JTextField[nValues];
        colourList = new JComboBox(colours);
        
        setUpPanel();
    }
    
    /**
     * Sets up the InputPanel.
     */
    private void setUpPanel()
    {
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new GridLayout(0,1));
        
        /* this loop adds a line for each parameter containing a label showing
         * the parameter prompt plus a text field for setting the parameter.
         */
        for (int i = 0; i < nValues; ++i) {
            JPanel inputLine = new JPanel();
            inputLine.setPreferredSize(new Dimension(WIDTH, FIELD_HEIGHT));
            inputLine.setLayout(new BorderLayout());
            
            JLabel label = new JLabel(Formatter.leftJustify(paramPrompts[i], TEXTLENGTH));
            label.setFont(new Font("Courier", Font.PLAIN, 12));
            inputLine.add(label, BorderLayout.WEST);
            
            if (paramPrompts[i].equals("Ball colour:")) {
            	inputLine.add(colourList);
            } else {
            	fields[i] = new JTextField(30);
            	fields[i].addActionListener(new ActionListener() {
            		public void actionPerformed(ActionEvent e) { 
                                }
                        });
            	fields[i].setText(paramDefaults[i]);
            	inputLine.add(fields[i], BorderLayout.CENTER);
            }
            formPanel.add(inputLine);    
        	}
        
        /* add a 'submit' button to the form */
        JButton button;
        button = new JButton("Submit");
        button.setPreferredSize(new Dimension(WIDTH, BUTTON_HEIGHT));
        button.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) { submit(); }
        	});
        formPanel.add(button);
        
        this.add(formPanel);
    }

    /**
     * Pass the input parameters to the GUI using this InputPanel.
     */
    private void submit() {
    	for (int i = 0; i < nValues; ++i) {
    		if (paramPrompts[i].equals("Ball colour:")) {
    			userInputs[i] = colourList.getSelectedItem().toString();
    		} else {
    			userInputs[i] = fields[i].getText();
    		}
    	}
    	gui.setUpBallSim(userInputs);
    }

}