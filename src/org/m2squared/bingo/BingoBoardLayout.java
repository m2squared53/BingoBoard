package org.m2squared.bingo;

import java.awt.Color;
import java.awt.ComponentOrientation;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.Popup;
import javax.swing.PopupFactory;
import javax.swing.border.Border;

public class BingoBoardLayout {
	final static boolean shouldFill = true;
    final static boolean shouldWeightX = true;
    static String[][] LABELS = new String[5][16];
    static String[] ROW_HEADERS = {"B", "I", "N", "G", "O"};
    static BingoLabel[] labels = new BingoLabel[75];
    static int[] currentlabels = new int[75];
	static int lastEntered = 0;
	static int currentCounter = 0;
	static JFrame frame = new JFrame("St. Theresa Turkey Bingo");

    
    static {
    	for (int i = 0; i < 5; i++) {
			LABELS[i][0] = ROW_HEADERS[i];
    		for (int j = 1; j < 16; j++) {
    			LABELS[i][j] = new Integer(15 * i + j).toString();
    		}
    	}
    	for (int i = 0; i < 75; i++) {
    		labels[i] = null;
    		currentlabels[i] = 0;
    	}
    }

    public static void addComponentsToPane(Container pane) {

		final JButton submitButton = new JButton("Submit");
		final JButton clearLastButton = new JButton("Clear Last");
		final JButton resetButton = new JButton("Reset");
		BingoLabel label = null;
		pane.setLayout(new GridBagLayout());
		pane.setBackground(Color.BLACK);
		GridBagConstraints c = new GridBagConstraints();
		if (shouldFill) {
			// natural height, maximum width
			c.fill = GridBagConstraints.BOTH;
		}
		
		/*
		 * Create and initialize the Bingo labels (BINGO and 1-75)
		 */
		int labelCounter = 0;
		for (int i = 0; i < 5; i++) {
			for (int j = 0; j < 16; j++) {
				label = new BingoLabel(LABELS[i][j]);
				Border border = BorderFactory.createLineBorder(Color.WHITE, 1);
				label.setBorder(border);
				if (shouldWeightX) {
					c.weightx = 0.5;
				}
				c.fill = GridBagConstraints.BOTH;
				c.gridx = j;
				c.gridy = i;
				if (j == 0) {
					// These are headers (B-I-N-G-O) so show them
					label.showValue();
				} else {
					// These are the numbers (1-75), start out hidden
					labels[labelCounter++] = label;
				}
				pane.add(label, c);
			}
		}
		
		// Create an input to add numbers (i.e. show) to the board
		JTextField input = new JTextField("", 7);
		Border border = BorderFactory.createLineBorder(Color.BLACK, 1);
		input.setBorder(border);
		if (shouldWeightX) {
			c.weightx = 0.5;
		}
		c.fill = GridBagConstraints.BOTH;
		c.gridx = 0;
		c.gridy = 6;

		pane.add(input, c);
		
		/*
		 * This button takes the value from input add adds it to the board
		 */
		submitButton.setBorder(border);
		if (shouldWeightX) {
			c.weightx = 0.5;
		}
		c.fill = GridBagConstraints.BOTH;
		c.gridx = 1;
		c.gridy = 6;

		submitButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				String value = input.getText().trim();
				boolean valid = false;
				boolean canAdd = true;
				Integer num = 0;
				try {
					num = new Integer(value);
					if (num > 0 && num < 76) {
						valid = true;
					}
				} catch (NumberFormatException nfe) {
					// Pass thru: valid not set
				}
				
				// if we have a valid entry check to see if it's already entered.
				if (valid) {
					lastEntered = num - 1;
					for (int i = 0; i < currentCounter; i++) {
						if (currentlabels[i] == lastEntered) {
							canAdd = false;
							break;
						}
					}
				}
				
				/*
				 * Case valid and canAdd: display and add to array
				 * Case valid and !canAdd: Pop appropriate error dialog
				 * Case !valid : Pop appropriate error dialog
				 */
				if (valid && canAdd) {
					if (currentCounter > 0) {
						labels[currentlabels[currentCounter - 1]].flash(false);
					}
					labels[lastEntered].flash(true);
					currentlabels[currentCounter++] = lastEntered;
					clearLastButton.setEnabled(true);
					resetButton.setEnabled(true);
				} else if (valid) {
					Popup pop = ErrorDialogFactory.getDialogWithDismiss(
							"Number is already selected", frame, frame.getX() + frame.getWidth() / 2,
							frame.getY() + frame.getHeight() / 2);
					pop.show();
				} else {
					Popup pop = ErrorDialogFactory.getDialogWithDismiss(
							"Must enter a valid number between 1 and 75",
							frame, frame.getX() + frame.getWidth() / 2, frame.getY() + frame.getHeight() / 2);
					pop.show();
				}
				input.setText("");
				input.requestFocus();
			}
		});
		pane.add(submitButton, c);
    
		/*
		 * This button removes the last entered value from the board
		 */
		clearLastButton.setBorder(border);
		clearLastButton.setEnabled(false);

		if (shouldWeightX) {
			c.weightx = 0.5;
		}
		c.fill = GridBagConstraints.BOTH;
		c.gridx = 2;
		c.gridy = 6;

		clearLastButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				int lastEntered = currentCounter - 1;
				if (lastEntered >= 0 && currentlabels[lastEntered] >= 0) {
					labels[currentlabels[lastEntered]].flash(false);
					labels[currentlabels[lastEntered]].hideValue();
					currentlabels[lastEntered] = 0;
					currentCounter--;
						
					if (currentCounter == 0) {
						clearLastButton.setEnabled(false);
						resetButton.setEnabled(false);
						currentCounter = 0;
					}
				}
				input.setText("");
				input.requestFocus();
			}
		});
		pane.add(clearLastButton, c);
		
		/*
		 * This button resets (clears) the board
		 */
		resetButton.setBorder(border);
		resetButton.setEnabled(false);

		if (shouldWeightX) {
			c.weightx = 0.5;
		}
		c.fill = GridBagConstraints.BOTH;
		c.gridx = 3;
		c.gridy = 6;

		resetButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				for (int i = 0; i < labels.length; i++) {
					labels[i].flash(false);
					labels[i].hideValue();
				}
				
				for (int i = 0; i < currentlabels.length; i++) {
					currentlabels[i] = 0;
				}
				
				currentCounter = 0;
				lastEntered = 0;
				clearLastButton.setEnabled(false);
				resetButton.setEnabled(false);
				input.setText("");
				input.requestFocus();
			}
		});
		pane.add(resetButton, c);
}


    /**
     * Create the GUI and show it.  For thread safety,
     * this method should be invoked from the
     * event-dispatching thread.
     */
    private static void createAndShowGUI() {
    	Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
    	System.out.println("Screen Size: width: " + screen.width + " height: " + screen.height);
        //Create and set up the window.
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //Set up the content pane.
        addComponentsToPane(frame.getContentPane());

        //Display the window.
        frame.pack();
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        //Schedule a job for the event-dispatching thread:
        //creating and showing this application's GUI.
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });
    }
}
