import java.awt.Color;
import java.awt.Font;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;

public class ScoreBoard extends JFrame {

	JLabel background;
	ImageIcon backgroundImage;
	File file;
	JLabel title;

	ScoreBoard() throws FileNotFoundException {
		backgroundImage = new ImageIcon("infoBackground.jpg");
		background = new JLabel();
		file = new File("scoreboard.txt");
		title = new JLabel();
		Scanner scan = new Scanner(file);

		String boardData = "<html><div style='text-align: center;'>";// Html writen by ai becuase i dont know html
		boardData += "Score - Name" + "<br>";
		ArrayList<String> scores = new ArrayList<>();
		while (scan.hasNextLine()) {
			scores.add(scan.nextLine());
		}
		scan.close();

		for (int i = 0; i < scores.size(); i++) {
			for (int j = i + 1; j < scores.size(); j++) {
				int scoreA = Integer.parseInt(scores.get(i).split(" - ")[0]);
				int scoreB = Integer.parseInt(scores.get(j).split(" - ")[0]);
				if (scoreB > scoreA) {
					String temp = scores.get(i);
					scores.set(i, scores.get(j));
					scores.set(j, temp);
				}
			}
		}
		
		boardData = "<html><div style='text-align: center;'>HIGH SCORES<br><br>";		
		int count = 0;
		while (count < scores.size() && count < 10) {
			boardData += scores.get(count) + "<br>";
			count++;
		}
		

		boardData += "</div></html>";
		


		background.setIcon(backgroundImage);
		background.setText(boardData);
		background.setForeground(Color.white);
		background.setFont(new Font("SansSerif", Font.BOLD, 24));
		background.setHorizontalTextPosition(JLabel.CENTER);
		background.setVerticalTextPosition(JLabel.CENTER);

		this.setSize(1183, 2560);
		this.add(background);
		this.setVisible(true);
	}
}