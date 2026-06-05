
/**
 * @author Andy
 * @date 2026-05-26
 * Description: Im not sure what this does - Anthony
 */
import javax.swing.*;
import java.awt.*;

class BackgroundPanel extends JPanel {
	private Image img;

	public BackgroundPanel(ImageIcon icon) {
		this.img = icon.getImage();
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);

		int panelW = getWidth();
		int panelH = getHeight();
		int imgW = img.getWidth(null);
		int imgH = img.getHeight(null);

		// Fill background with black
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, panelW, panelH);

		// Compute proportional scale
		double scale = Math.min((double) panelW / imgW, (double) panelH / imgH);

		int newW = (int) (imgW * scale);
		int newH = (int) (imgH * scale);

		// Center the image
		int x = (panelW - newW) / 2;
		int y = (panelH - newH) / 2;

		g.drawImage(img, x, y, newW, newH, this);
	}
}
