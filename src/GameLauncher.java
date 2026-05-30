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

public class GameLauncher implements ActionListener{

	private Board board;
	private JFrame frame;
	private JButton launchEasyButton;
	private JButton launchHardButton;
	private JTextField nameField;
	private JLabel background;
	private ImageIcon backgroundI;
	private ImageIcon titleI;
	private JPanel westPanel;
	private JPanel northPanel;
	private JLabel title;
	private JButton playAgain;
	private JButton loadButton;
	private JButton infoButton;
	private Dimension buttonSize;
	private JPanel displaceTitle;
	
	private String name;
	private Boolean difficultyNormal;
	

	private int frameX = 960;
	private int frameY = 540;
	
	public static void main(String[] args) {
		GameLauncher engine = new GameLauncher();
		launch();
	}
	
	public GameLauncher() {
		frame = new JFrame("Star Wars Battleship");
		launchEasyButton = new JButton("New Normal");
		launchHardButton = new JButton("New Hard");
		nameField = new JTextField("Enter Name");
		backgroundI = new ImageIcon("launchBackground.png");
		titleI = new ImageIcon("titleText.png");
		loadButton = new JButton("Load Previous");
		infoButton = new JButton("?");
		buttonSize = new Dimension(180,50);
		background = new JLabel(backgroundI);
		westPanel = new JPanel();
		northPanel = new JPanel();
		title = new JLabel(titleI);
		displaceTitle = new JPanel();

		
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
		
		westPanel.setPreferredSize(new Dimension(300,540));
		westPanel.setOpaque(false);
		westPanel.setBorder(BorderFactory.createEmptyBorder(0,40,0,0));
		
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
		
		displaceTitle.setPreferredSize(new Dimension(370,50));
		displaceTitle.setOpaque(false);
		
		frame.add(background);
		background.setLayout(new BorderLayout());
		frame.setSize(frameX,frameY);
		
		background.add(westPanel, BorderLayout.WEST);
		background.add(northPanel, BorderLayout.NORTH);
		
		westPanel.add(nameField);
		westPanel.add(launchEasyButton);
		westPanel.add(launchHardButton);
		westPanel.add(loadButton);
		westPanel.add(infoButton);
		northPanel.add(title);
		northPanel.add(displaceTitle);
		
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
			new InfoFrame();
		}
		System.out.println(name);
	}
	
}
