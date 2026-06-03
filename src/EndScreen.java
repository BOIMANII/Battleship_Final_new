/**
 * @author Anthony
 * @date 2026-05-27
 * Description: The class that launches the End Screen when the game finishes
 */

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

public class EndScreen implements ActionListener {

	// GUI Components
	private JFrame frame;
	private JPanel westPanel;
	private JPanel northPanel;
	private JPanel displaceTitle;

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
	 * Constructor for EndScreen
	 * * Holds the graphics for the game over screen.
	 * Formatted identically to GameLauncher.
	 */
	public EndScreen() {
		//SoundPlayer.playMusic("musicEnding.wav");
		frame = new JFrame("Star Wars Battleship - Game Over");
		playAgainButton = new JButton("Play Again");
		quitButton = new JButton("Quit");
		
		backgroundI = new ImageIcon("backgroundEnding.Again.jpg");
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

		northPanel.setPreferredSize(new Dimension(960, 200));
		northPanel.setOpaque(false);

		displaceTitle.setPreferredSize(new Dimension(370, 50));
		displaceTitle.setOpaque(false);

		frame.add(background);
		background.add(westPanel, BorderLayout.WEST);
		background.add(northPanel, BorderLayout.NORTH);
		
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