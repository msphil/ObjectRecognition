package image.csu.fullerton.edu;

import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class TestUI extends JFrame {
	
	private JFrame switchFrame;

	private JButton captureButton;
	private JButton loadButton;
	private JButton saveButton;
	private JButton switchButton;
	private JButton quitButton;

	private JButton desaturateButton;
	private JButton downscaleButton;
	private JButton edgedetectButton;
	private JButton cannyEdgeDetectButton;
	private JButton thresholdButton;
	private JButton momentsButton;

	private JPanel imagePanel;
	private JLabel imageLabel;
	private JPanel processPanel;
	private JPanel capturePanel;
	private JPanel savePanel;
	private JPanel loadPanel;
	private JPanel switchPanel;
	private JPanel quitPanel;
	private JPanel bottomPanel;
	private JPanel controlPanel;
	
	private CaptureCamera camera;	// custom camera object
	
	private BufferedImage currentImage;

	private String savedDirectoryPath;
	
	// Constructor: create the interface
	public TestUI() {
		// Set title and layout
		super("Object Recognition -- Design");

		// handler for the input buttons
		ButtonHandler buttonHandler = new ButtonHandler();

		// design the layout: two
		setLayout(new GridLayout(2, 1, 5, 5));

		controlPanel = new JPanel(new GridLayout(5, 1, 10, 10));
		imagePanel = new JPanel(new GridLayout(1, 2));
		savePanel = new JPanel(new FlowLayout());
		loadPanel = new JPanel(new FlowLayout());
		capturePanel = new JPanel(new FlowLayout());
		switchPanel = new JPanel(new FlowLayout());
		quitPanel = new JPanel(new FlowLayout());
		bottomPanel = new JPanel(new GridLayout(1, 2));

		imageLabel = new JLabel();
		imagePanel.add(imageLabel);

		// image processing buttons
		processPanel = new JPanel(new FlowLayout());
		imagePanel.add(processPanel);

		add(imagePanel);
		
		// switch UI button
		switchButton = new JButton("Switch to Design");
		switchPanel.add(switchButton);
		switchButton.addActionListener(buttonHandler);
		controlPanel.add(switchPanel);
		
		// quit button
		quitButton = new JButton("Quit");
		quitPanel.add(quitButton);
		quitButton.addActionListener(buttonHandler);
		controlPanel.add(quitPanel);
		
		bottomPanel.add(controlPanel);
		add(bottomPanel);

	}
	
	private void setImage(BufferedImage newImage) {
		ImageIcon newImageIcon = new ImageIcon(newImage);
		imageLabel.setIcon(newImageIcon);
		imageLabel.setVisible(true);
		currentImage = newImage;
	}
	
	void setSwitcher(JFrame newFrame) {
		switchFrame = newFrame;
	}

	// ButtonHandler: on Calculate, calculate pay. On Clear, clear
	// fields.
	private class ButtonHandler implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			if (event.getSource() == quitButton) {
				System.out.print("quit\n");
				TestUI.this.dispose(); // clean up UI
				System.exit(0);
			} else if (event.getSource() == switchButton) {
				TestUI.this.setVisible(false);
				switchFrame.setVisible(true);
			} else if (event.getSource() == saveButton) {
				image.csu.fullerton.edu.Image.saveImage(currentImage, "c:/ordata/test/test.jpg", "JPG");
			} else if (event.getSource() == desaturateButton) {
				setImage(image.csu.fullerton.edu.Image.desaturateImage(currentImage));
			} else if (event.getSource() == downscaleButton) {
				setImage(image.csu.fullerton.edu.Image.downscaleImage(currentImage, 64, 64));
			} else if (event.getSource() == edgedetectButton) {
				setImage(image.csu.fullerton.edu.Image.edgeDetectImage(currentImage));
			} else if (event.getSource() == cannyEdgeDetectButton) {
				setImage(image.csu.fullerton.edu.Image.cannyEdgeDetectImage(currentImage));
			} else if (event.getSource() == thresholdButton) {
				setImage(image.csu.fullerton.edu.Image.thresholdImage(currentImage));
			} else if (event.getSource() == momentsButton) {
				image.csu.fullerton.edu.Image.calculateMoments(currentImage);
			} else {
				// ignore
			}
		}
	}

}
