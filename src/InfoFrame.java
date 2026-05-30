import java.awt.Color;

import javax.swing.*;

public class InfoFrame extends JFrame{
	
	private JLabel background;
	private ImageIcon backgroundImage;
	InfoFrame(){
		backgroundImage = new ImageIcon("infoBackground.jpg");
		background = new JLabel("TEXT STUFF ABOUT BATTLESHIP NGL IM KINDA LAZY RN AND DONT WANNA WRITE STUFF SO THIS IS THE TEMP PLACEHOLDER AND ITS GOTTA BE A BIT BIG SO IM JUST SPITBALLING RANDOM WORDS OK I THINK THIS SHOULD BE LONG ENOUGH I DONT EVEN KNOW IF ANYONE WILL READ THIS BUT IF SOMEONE IS HELLO");
		
		background.setIcon(backgroundImage);
		background.setForeground(Color.white);
		background.setHorizontalTextPosition(JLabel.CENTER);
		this.setSize(1183,2560);
		
		
		this.add(background);
		this.setDefaultCloseOperation(3);
		this.setVisible(true);
	}
}
