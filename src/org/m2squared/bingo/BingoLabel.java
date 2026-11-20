package org.m2squared.bingo;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.Timer;
import javax.swing.JPopupMenu;

public class BingoLabel extends JLabel {
	private static final long serialVersionUID = 5932065928529292206L;

	protected static final String HTML = "<html>"
			+   "<head>"
			+     "<style>"
			+       "h1 {font-size: 45px; background-color: #000000; color: #%s;}"
			+     "</style>"
			+   "</head>"
			+   "<h1>%s</h1>"
			+ "</html>";
	
	protected static final String BG_WHITE = "FFFFFF";
	protected static final String BG_BLACK = "000000";
	protected static final int FLASH_RATE = 500;
	
	protected Timer flasher;
	protected boolean isFlashing = false;
	protected boolean showToggle = true;
	protected boolean isVisible = false;
	protected JPopupMenu popupMenu = new JPopupMenu();
	protected JMenuItem deleteMenuItem = new JMenuItem("Delete");
	protected JMenuItem cancelMenuItem = new JMenuItem("Cancel");
	private String text;

	public BingoLabel(String text, BingoBoardContainer container) {
		super(String.format(HTML, BG_BLACK, text));
		this.text = text;
		this.setHorizontalAlignment(CENTER);
		this.setVerticalAlignment(CENTER);
		this.popupMenu.add(this.deleteMenuItem);
		this.deleteMenuItem.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				container.removeBingoLabel(BingoLabel.this);
			}
		});
		this.popupMenu.add(this.cancelMenuItem);
		
		
		this.flasher = new Timer(FLASH_RATE, new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if (isFlashing) {
					if (showToggle) {
						showValue();
					}
					else {
						hideValue();
					}
					showToggle = !showToggle;
				}
			}
		});
		this.flasher.setRepeats(true);
		this.flasher.start();
	}
	
	public void showValue() {
		this.setText(String.format(HTML, BG_WHITE, this.text));
		this.isVisible = true;
	}
	
	public void hideValue() {
		this.setText(String.format(HTML, BG_BLACK, this.text));
		this.isVisible = false;
	}
	
	public void flash(boolean flash) {
		if (flash) {
			this.isFlashing = true;
		}
		else {
			this.isFlashing = false;
			this.showToggle = true;
			this.showValue();
			this.setComponentPopupMenu(popupMenu);
		}
	}

	public String getTextValue() {
		return this.text;
	}
	
}
