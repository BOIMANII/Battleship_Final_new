import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;
public class EndScreen extends JFrame implements ActionListener{
	
	JButton playAgain;
	JButton quit;
	JPanel westPanel;
	JLabel title;
	JPanel northPanel;
	JPanel displaceTitle;
	
	JLabel background;
	ImageIcon backgroundI;
	ImageIcon titleI;

	
	public EndScreen() {
		
		backgroundI = new ImageIcon("backgroundEnding.png");
		playAgain = new JButton();
		quit = new JButton();
		background = new JLabel(backgroundI);
		westPanel = new JPanel();
		titleI = new ImageIcon("titleText.png");
		title = new JLabel(titleI);
		northPanel = new JPanel();
		displaceTitle = new JPanel();


		
		background.setLayout(new BorderLayout());
		
		westPanel.setPreferredSize(new Dimension(300, 540));
		westPanel.setOpaque(false);
		westPanel.setBorder(BorderFactory.createEmptyBorder(0, 40, 0, 0));
		
		northPanel.setPreferredSize(new Dimension(960, 200));
		northPanel.setOpaque(false);
		
		displaceTitle.setPreferredSize(new Dimension(370, 50));
		displaceTitle.setOpaque(false);
		
		playAgain.setPreferredSize(new Dimension(200,100));
		playAgain.setText("Play Again");
		playAgain.addActionListener(this);
		playAgain.setBackground(Color.black);
		playAgain.setForeground(Color.yellow);
		
		quit.setPreferredSize(new Dimension(200,100));
		quit.setText("Play Again");
		quit.addActionListener(this);
		quit.setBackground(Color.black);
		quit.setForeground(Color.yellow);
		
		this.setSize(backgroundI.getIconWidth(), backgroundI.getIconHeight());
		this.setDefaultCloseOperation(3);
		
		this.add(background);
		background.add(westPanel, BorderLayout.WEST);
		background.add(northPanel, BorderLayout.NORTH);
		westPanel.add(playAgain);
		westPanel.add(quit);
		northPanel.add(title);
		northPanel.add(displaceTitle);
		
		this.setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
	}
}
