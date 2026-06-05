
/**
 * @author Anthony
 * @date 2026-05-27
 * Description: The class that launches the end screen when the game finishes.
 */

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import javax.swing.border.Border;

public class EndScreen implements ActionListener {

	// GUI Components
	private JFrame frame;
	private JPanel westPanel;
	private JPanel northPanel;
	private JPanel displaceTitle;
	private JPanel eastPanel;
	private JLabel scoreBoard;
	private Border yellowBorder;

	private JLabel background;
	private JLabel title;
	private ImageIcon backgroundI;
	private ImageIcon titleI;

	private JButton playAgainButton;
	private JButton quitButton;

	private int frameX = 960;
	private int frameY = 540;
	private Dimension buttonSize = new Dimension(180, 50);

	/**
	 * Constructor for EndScreen. Has the graphics for EndScreen and is almost
	 * idenitcal to GameLauncher.
	 */
	public EndScreen(int hit, int miss, int sunk, int aiSunk, boolean victory) {
		SoundPlayer.playMusic("musicEnding.wav");
		frame = new JFrame("Star Wars Battleship - Game Over");
		playAgainButton = new JButton("Play Again");
		quitButton = new JButton("Quit");
		eastPanel = new JPanel();
		scoreBoard = new JLabel();
		yellowBorder = BorderFactory.createLineBorder(Color.yellow, 3);

		if (victory == true) {
			backgroundI = new ImageIcon("backgroundEnding.Again.jpg");

		} else {
			backgroundI = new ImageIcon("backgroundLose.png");
		}
		titleI = new ImageIcon("titleText.png");

		background = new JLabel(backgroundI);
		westPanel = new JPanel();
		northPanel = new JPanel();
		title = new JLabel(titleI);
		displaceTitle = new JPanel(); // To offset the title

		frame.setSize(backgroundI.getIconWidth(), backgroundI.getIconHeight());

		background.setBounds(0, 0, frameX, frameY);
		background.setLayout(new BorderLayout());

		playAgainButton.addActionListener(this);
		playAgainButton.setPreferredSize(buttonSize);
		playAgainButton.setBackground(Color.black);
		playAgainButton.setForeground(Color.yellow);
		playAgainButton.setFocusable(false);

		quitButton.addActionListener(this);
		quitButton.setPreferredSize(buttonSize);
		quitButton.setBackground(Color.black);
		quitButton.setForeground(Color.yellow);
		quitButton.setFocusable(false);

		westPanel.setPreferredSize(new Dimension(300, 540));
		westPanel.setOpaque(false);
		westPanel.setBorder(BorderFactory.createEmptyBorder(0, 40, 0, 0));

		eastPanel.setPreferredSize(new Dimension(300, 540));
		eastPanel.setBackground(Color.black);
		eastPanel.setBorder(yellowBorder);
		
		// Gemini helped with html
		scoreBoard.setText("<html>FINAL SCORE<br>HITS: " + hit + "<br>MISSES: " + miss + "<br>SHIPS SUNK: " + sunk
				+ "<br>SHIPS LOST: " + aiSunk + "<br>FINAL SCORE: "
				+ ((100 * hit) - (25 * miss) + (1000 * sunk) - (250 * aiSunk)) + "</html>");
		scoreBoard.setFont(new java.awt.Font("SansSerif", java.awt.Font.BOLD, 24));
		scoreBoard.setForeground(Color.yellow);

		northPanel.setPreferredSize(new Dimension(960, 200));
		northPanel.setOpaque(false);

		displaceTitle.setPreferredSize(new Dimension(370, 50));
		displaceTitle.setOpaque(false);

		frame.add(background);
		background.add(westPanel, BorderLayout.WEST);
		background.add(northPanel, BorderLayout.NORTH);
		background.add(eastPanel, BorderLayout.EAST);

		eastPanel.add(scoreBoard);

		westPanel.add(playAgainButton);
		westPanel.add(quitButton);

		northPanel.add(title);
		northPanel.add(displaceTitle);

		frame.setResizable(false);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(3);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == playAgainButton) {
			frame.dispose();
			new GameLauncher();
		}
		if (e.getSource() == quitButton) {
			System.exit(0);
		}
	}
}