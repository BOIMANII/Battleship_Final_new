/**
 * 
 */

/**
 * 
 */

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

public class GameEngine implements ActionListener{

	//private Board board;
	private JFrame frame;
	private JButton launchEasyButton;
	private JButton launchHardButton;
	private JTextField nameField;
	private JLabel background;
	private ImageIcon backgroundI;
	private ImageIcon titleI;
	private JPanel southPanel;
	private JPanel northPanel;
	private JLabel title;
	private JButton playAgain;
	private JButton loadButton;
	private JButton infoButton;
	private Dimension buttonSize;
	
	private String name;
	private Boolean difficultyNormal;
	

	private int frameX = 960;
	private int frameY = 540;
	
	public static void main(String[] args) {
		GameEngine engine = new GameEngine();
		launch();
	}
	
	public GameEngine() {
		frame = new JFrame("Star Wars Battleship");
		launchEasyButton = new JButton("Start New Normal Match");
		launchHardButton = new JButton("Start New Hard Match");
		nameField = new JTextField("Enter Name");
		backgroundI = new ImageIcon("launchBackground.png");
		titleI = new ImageIcon("titleText.png");
		loadButton = new JButton("Load Previous Match");
		infoButton = new JButton("?");
		buttonSize = new Dimension(180,50);

		background = new JLabel(backgroundI);
		southPanel = new JPanel();
		northPanel = new JPanel();
		title = new JLabel(titleI);

		
		background.setBounds(0,0,frameX, frameY);
		
		launchEasyButton.addActionListener(this);
		launchHardButton.addActionListener(this);
		launchEasyButton.setPreferredSize(buttonSize);
		launchHardButton.setPreferredSize(buttonSize);
		launchEasyButton.setBackground(Color.black);
		launchEasyButton.setForeground(Color.yellow);
		launchHardButton.setBackground(Color.black);
		launchHardButton.setForeground(Color.yellow);
		launchEasyButton.setFocusable(false);
		launchHardButton.setFocusable(false);
		
		
		nameField.setHorizontalAlignment(JTextField.CENTER);
		nameField.setPreferredSize(buttonSize);
		
		southPanel.setPreferredSize(new Dimension(960,100));
		southPanel.setOpaque(false);
		
		northPanel.setPreferredSize(new Dimension(960,200));
		northPanel.setOpaque(false);
		
		loadButton.addActionListener(this);
		loadButton.setPreferredSize(buttonSize);
		loadButton.setBackground(Color.black);
		loadButton.setForeground(Color.yellow);
		loadButton.setFocusable(false);
		
		infoButton.addActionListener(this);
		infoButton.setPreferredSize(buttonSize);
		infoButton.setBackground(Color.black);
		infoButton.setForeground(Color.yellow);
		infoButton.setFocusable(false);
		
		frame.add(background);
		background.setLayout(new BorderLayout());
		frame.setSize(frameX,frameY);
		
		background.add(southPanel, BorderLayout.SOUTH);
		background.add(northPanel, BorderLayout.NORTH);
		
		//southPanel.add(infoButton);
		southPanel.add(nameField);
		southPanel.add(launchEasyButton);
		southPanel.add(launchHardButton);
		southPanel.add(loadButton);
		northPanel.add(title);
		
		frame.setResizable(false);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(3);
	}
	
	public static void launch() {
		System.out.println("Launch Test");
		
		
	}
	
	public void isEnd() {
		System.out.println("End test");

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == launchHardButton) {
			System.out.println("Launch Hard");
			difficultyNormal = false;
			name = nameField.getText();
			frame.dispose();
			launch();
		}
		if(e.getSource() == launchEasyButton) {
			System.out.println("Launch Normal");
			difficultyNormal = true;
			name = nameField.getText();
			frame.dispose();
			launch();
		}
		if(e.getSource() == loadButton) {
			System.out.println("Gonna load something");
		}
		if(e.getSource() == infoButton) {
			System.out.println("Dumping info");
			//Pop up window 
		}
		System.out.println(name);
	}
	
}
