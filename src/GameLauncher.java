
/**
 * @author Anthony
 * @date 2026-05-27
 * Description: The class that launches the Board and deals with winning screen
 */

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;

import javax.swing.*;

public class GameLauncher implements ActionListener {

	// GUI Components
	private JFrame frame;
	private JPanel westPanel;
	private JPanel northPanel;
	private JPanel displaceTitle;

	private JLabel background;
	private JLabel title;
	private ImageIcon backgroundI;
	private ImageIcon titleI;

	private JTextField nameField;
	private JButton launchEasyButton;
	private JButton launchHardButton;
	private JButton loadButton;
	private JButton infoButton;

	private int frameX = 960;
	private int frameY = 540;
	private Dimension buttonSize = new Dimension(180, 50);

	// Variables
	private Board board;
	private String name;
	private Boolean difficultyNormal;

	public static void main(String[] args) {

		GameLauncher engine = new GameLauncher();
	}

	/**
	 * Constructor for GameLauncher
	 * 
	 * Holds the graphics for the launcher
	 * 
	 * Buttons hold the logic and code to launch board
	 * 
	 */
	public GameLauncher() {
		frame = new JFrame("Star Wars Battleship");
		launchEasyButton = new JButton("New Normal");
		launchHardButton = new JButton("New Hard");
		nameField = new JTextField("Enter Name");
		backgroundI = new ImageIcon("launchBackground.png");
		titleI = new ImageIcon("titleText.png");
		loadButton = new JButton("Load Previous");
		infoButton = new JButton("How to play");
		buttonSize = new Dimension(180, 50);
		background = new JLabel(backgroundI);
		westPanel = new JPanel();
		northPanel = new JPanel();
		title = new JLabel(titleI);
		displaceTitle = new JPanel();// To offset the title and to potentialy hold other buttons

		frame.setSize(frameX, frameY);

		background.setBounds(0, 0, frameX, frameY);
		background.setLayout(new BorderLayout());

		launchEasyButton.addActionListener(this);
		launchEasyButton.setPreferredSize(buttonSize);
		launchEasyButton.setBackground(Color.black);
		launchEasyButton.setForeground(Color.yellow);
		launchEasyButton.setFocusable(false);

		launchHardButton.addActionListener(this);
		launchHardButton.setPreferredSize(buttonSize);
		launchHardButton.setBackground(Color.black);
		launchHardButton.setForeground(Color.yellow);
		launchHardButton.setFocusable(false);

		nameField.setHorizontalAlignment(JTextField.CENTER);
		nameField.setPreferredSize(buttonSize);

		westPanel.setPreferredSize(new Dimension(300, 540));
		westPanel.setOpaque(false);
		westPanel.setBorder(BorderFactory.createEmptyBorder(0, 40, 0, 0));

		northPanel.setPreferredSize(new Dimension(960, 200));
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

		displaceTitle.setPreferredSize(new Dimension(370, 50));
		displaceTitle.setOpaque(false);

		frame.add(background);
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
		SoundPlayer.playMusic("musicLauncher .wav");
	}


	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == launchHardButton) {
			System.out.println("Launch Hard");
			difficultyNormal = false;
			name = nameField.getText();
			try {
				board = new Board(name, true, false);
			} catch (FileNotFoundException | InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			frame.dispose();
		}
		if (e.getSource() == launchEasyButton) {
			System.out.println("Launch Normal");
			difficultyNormal = true;
			name = nameField.getText();
			try {
				board = new Board(name, false, false);
			} catch (FileNotFoundException | InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			frame.dispose();
		}
		if (e.getSource() == loadButton) {
			// Save file stuff
			System.out.println("Gonna load something");
			name = nameField.getText();
			try {
				board = new Board(name, false, true);
				new Board(name, false, true);
			} catch (FileNotFoundException | InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		if (e.getSource() == infoButton) {
			System.out.println("Dumping info");
			new InfoFrame();
		}
		System.out.println(name);
	}

}
