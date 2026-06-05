import java.awt.Color;
import java.awt.Font;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;

public class ScoreBoard extends JFrame {
	
	JLabel background;
	ImageIcon backgroundImage;
	
	File file;
	
	ScoreBoard() throws FileNotFoundException{
		backgroundImage = new ImageIcon("infoBackground.jpg");
		background = new JLabel();
		file = new File("scoreboard.txt");
		Scanner scan = new Scanner(file);
		
		String boardData = "<html><div style='text-align: center;'>";// Html writen by ai becuase i dont know html
		while (scan.hasNextLine()) {
			boardData += scan.nextLine() + "<br>";
		}
		boardData += "</div></html>";
		scan.close();
				
		background.setIcon(backgroundImage);
		background.setText(boardData);
		background.setForeground(Color.white);
		background.setFont(new Font("SansSerif", Font.BOLD, 24));
		background.setHorizontalTextPosition(JLabel.CENTER);
		background.setVerticalTextPosition(JLabel.CENTER);
		
		this.setSize(1183,2560);
		
		this.add(background);
		this.setVisible(true);
		
	}
}