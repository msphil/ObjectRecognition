package image.csu.fullerton.edu;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Panel;

import javax.swing.JPanel;

public class ImagePanel extends Panel {
	public Image myimg = null;

	public ImagePanel() {
		setLayout(null);
		setSize(320, 240);
	}

	public void setImage(Image img) {
		this.myimg = img;
		repaint();
	}

	public void paint(Graphics g) {
		super.paint(g);
		g.drawImage(myimg, 0, 0, this);
	}
}
