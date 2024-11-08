package org.m2squared.bingo;

import javax.swing.JLabel;

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
	
	private String text;

	public BingoLabel(String text) {
		super(String.format(HTML, BG_BLACK, text));
		this.text = text;
		this.setHorizontalAlignment(CENTER);
		this.setVerticalAlignment(CENTER);
	}
	
	public void showValue() {
		this.setText(String.format(HTML, BG_WHITE, this.text));
	}
	
	public void hideValue() {
		this.setText(String.format(HTML, BG_BLACK, this.text));
	}

}
