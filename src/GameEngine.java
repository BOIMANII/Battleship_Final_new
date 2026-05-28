
/**
 * 
 */

/**
 * 
 */
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

public class GameEngine implements ActionListener {

//	private Board board;
	private JFrame frame;
	private JButton launchEasyButton;
	private JButton launchHardButton;


	public GameEngine() {
		frame = new JFrame("Star wars Battleship");
		launchEasyButton = new JButton("Easy");
		launchHardButton = new JButton("Hard");
	}

	public void launch() {

		frame.setSize(960, 540);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(3);
	}

	public void isEnd() {

	}

	@Override
	public void actionPerformed(ActionEvent e) {

	}
	
	
	public static void main(String[] args) {
		GameEngine engine = new GameEngine();
		engine.launch();
	}
}


