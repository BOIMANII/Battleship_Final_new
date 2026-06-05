
/**
 * @author Anthony
 * @date 2026-05-26
 * Description: Window to launch diffrent difficlys from
 */
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import javax.swing.*;

public class GameLauncher implements ActionListener {

	// Window Settings And Constants
	private int frameX = 960;
	private int frameY = 540;
	private Dimension buttonSize = new Dimension(180, 40);

	// Main GUI Panels
	private JFrame frame;
	private JPanel westPanel;
	private JPanel northPanel;
	private JPanel displaceTitle;

	// Image Icon Items
	private JLabel background;
	private JLabel title;
	private JLabel logo;
	private ImageIcon backgroundI;
	private ImageIcon titleI;
	private ImageIcon logoI;

	// Interactive Items
	private JTextField nameField;
	private JButton launchEasyButton;
	private JButton launchHardButton;
	private JButton loadButton;
	private JButton infoButton;
	private JButton scoreButton;

	// Tracking
	private Board board;
	private String name;
	private Boolean difficultyNormal;

	public static void main(String[] args) {
		GameLauncher engine = new GameLauncher();
	}

	public GameLauncher() {
		SoundPlayer.playMusic("musicLauncher.wav");

		frame = new JFrame("Star Wars Battleship");
		
		logoI = new ImageIcon("imageLogo.png");
		backgroundI = new ImageIcon("launchBackground.png");
		titleI = new ImageIcon("titleText.png");
		background = new JLabel(backgroundI);
		title = new JLabel(titleI);
		logo = new JLabel(logoI);

		westPanel = new JPanel();
		northPanel = new JPanel();
		displaceTitle = new JPanel();

		nameField = new JTextField("Enter Name");
		launchEasyButton = new JButton("New Normal");
		launchHardButton = new JButton("New Hard");
		loadButton = new JButton("Load Previous");
		infoButton = new JButton("How to play");
		scoreButton = new JButton("Scoreboard");

		frame.setSize(frameX, frameY);
		frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		background.setBounds(0, 0, frameX, frameY);
		background.setLayout(new BorderLayout());

		westPanel.setPreferredSize(new Dimension(300, 540));
		westPanel.setOpaque(false);
		westPanel.setBorder(BorderFactory.createEmptyBorder(0, 40, 0, 0));

		northPanel.setPreferredSize(new Dimension(960, 200));
		northPanel.setOpaque(false);
		northPanel.setBackground(Color.black);

		displaceTitle.setPreferredSize(new Dimension(275, 50));
		displaceTitle.setOpaque(false);
		
		logo.setText("Space Wizards");
		logo.setVerticalTextPosition(JLabel.BOTTOM);
		logo.setHorizontalTextPosition(JLabel.CENTER);
		logo.setForeground(Color.yellow);

		nameField.setHorizontalAlignment(JTextField.CENTER);
		nameField.setPreferredSize(buttonSize);

		configureLauncherButton(launchEasyButton);
		configureLauncherButton(launchHardButton);
		configureLauncherButton(loadButton);
		configureLauncherButton(infoButton);
		configureLauncherButton(scoreButton);

		frame.add(background);
		background.add(westPanel, BorderLayout.WEST);
		background.add(northPanel, BorderLayout.NORTH);

		westPanel.add(nameField);
		westPanel.add(launchEasyButton);
		westPanel.add(launchHardButton);
		westPanel.add(loadButton);
		westPanel.add(infoButton);
		westPanel.add(scoreButton);

		northPanel.add(title);
		northPanel.add(displaceTitle);
		northPanel.add(logo);

		frame.setVisible(true);
	}

	/**
	 * Helper method to make buttons All buttons are the same ecxept text
	 * 
	 * @param JButton
	 */
	private void configureLauncherButton(JButton button) {
		button.addActionListener(this);
		button.setPreferredSize(buttonSize);
		button.setBackground(Color.BLACK);
		button.setForeground(Color.YELLOW);
		button.setFocusable(false);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == launchHardButton) {
			SoundPlayer.playSound("sfxButton.wav");
			SoundPlayer.playSound("sfxJump.wav");
			difficultyNormal = false;
			name = nameField.getText();
			try {
				board = new Board(name, true, false);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
			frame.dispose();
			new InfoFrame();

		}

		if (e.getSource() == launchEasyButton) {
			SoundPlayer.playSound("sfxButton.wav");
			SoundPlayer.playSound("sfxJump.wav");
			difficultyNormal = true;
			name = nameField.getText();
			try {
				board = new Board(name, false, false);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
			frame.dispose();
			new InfoFrame();

		}

		if (e.getSource() == loadButton) {
			SoundPlayer.playSound("sfxButton.wav");
			SoundPlayer.playSound("sfxJump.wav");
			name = nameField.getText();
			try {
				frame.dispose();
				new Board(name, false, true);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
		}

		if (e.getSource() == infoButton) {
			SoundPlayer.playSound("sfxButton.wav");
			new InfoFrame();
		}
		if (e.getSource() == scoreButton) {
			SoundPlayer.playSound("sfxButton.wav");
			try {
				new ScoreBoard();
			} catch (FileNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	}
}