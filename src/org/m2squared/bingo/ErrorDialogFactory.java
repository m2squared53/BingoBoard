package org.m2squared.bingo;

import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Popup;
import javax.swing.PopupFactory;

public class ErrorDialogFactory {

	// Static methods only
	private ErrorDialogFactory() {
		// TODO Auto-generated constructor stub
	}
	
	public static Popup getDialogWithDismiss(String message, Container pane, int x, int y) {
		PopupFactory pf = PopupFactory.getSharedInstance();
		JPanel errPanel = new JPanel();
		JLabel errMsg = new JLabel(message);
		errPanel.add(errMsg);
		JButton dismissButton = new JButton("Dissmiss");
		errPanel.add(dismissButton);
		Popup pop = pf.getPopup(pane, errPanel, x, y);

		dismissButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				pop.hide();
				
			}
		});
		
		return pop;
	}
}
