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
import javax.swing.JTextField;

public class TestUI extends JFrame {
	
	private JFrame switchFrame;

	private JButton captureButton;
	private JButton loadButton;
	private JButton saveButton;
	private JButton evalButton;
	private JButton switchButton;
	private JButton quitButton;

	private JButton momentsButton;
	private JButton calcClassifierButton;
	private JButton testDataButton;
	
	private JTextField designTextField;
	private JTextField testTextField;

	private JLabel imageLabel;

	private ImagePanel cameraPanel;	// custom panel for cameras
	
	private CaptureCamera camera;	// custom camera object
	
	private BufferedImage currentImage;

	private String savedDirectoryPath;
	
	// Constructor: create the interface
	public TestUI() {
		// Set title and layout
		super("Object Recognition -- Design");

		JPanel picturePanel;
		JPanel imagePanel;
		JPanel processPanel;
		JPanel capturePanel;
		JPanel savePanel;
		JPanel evalPanel;
		JPanel loadPanel;
		JPanel switchPanel;
		JPanel quitPanel;
		JPanel bottomPanel;
		JPanel controlPanel;
		JPanel dataPanel;
		JPanel designTextPanel;
		JPanel testTextPanel;
		
		JPanel leftPanel;
		JPanel rightPanel;
		
		// handler for the input buttons
		ButtonHandler buttonHandler = new ButtonHandler();

		// design the layout: two
		setLayout(new GridLayout(1, 2, 5, 5));

		picturePanel = new JPanel(new GridLayout(2, 1, 0, 0));
		controlPanel = new JPanel(new GridLayout(5, 1, 10, 10));
		imagePanel = new JPanel(new GridLayout(1, 2));
		savePanel = new JPanel(new FlowLayout());
		evalPanel = new JPanel(new FlowLayout());
		loadPanel = new JPanel(new FlowLayout());
		capturePanel = new JPanel(new FlowLayout());
		switchPanel = new JPanel(new FlowLayout());
		quitPanel = new JPanel(new FlowLayout());
		bottomPanel = new JPanel(new GridLayout(1, 2));
		leftPanel = new JPanel(new GridLayout(2, 1));
		rightPanel = new JPanel(new GridLayout(2, 2));

		// video camera capture
		cameraPanel = new ImagePanel();
		camera = new CaptureCamera();
		camera.addToPanel(cameraPanel);
		leftPanel.add(cameraPanel);		
		
		imageLabel = new JLabel();
		leftPanel.add(imageLabel);
		
		add(leftPanel);

		// image processing buttons
		dataPanel = new JPanel(new GridLayout(4,1));
		designTextPanel = new JPanel(new FlowLayout());
		designTextField = new JTextField(10);
		designTextPanel.add(new JLabel("Design: "));
		designTextPanel.add(designTextField);
		dataPanel.add(designTextPanel);
		testTextPanel = new JPanel(new FlowLayout());
		testTextField = new JTextField(10);
		testTextPanel.add(new JLabel("Test: "));
		testTextPanel.add(testTextField);
		dataPanel.add(testTextPanel);
		calcClassifierButton = new JButton("Calculate Classifier Parameters");
		dataPanel.add(calcClassifierButton);
		testDataButton = new JButton("Test Data With Current Classifier");
		dataPanel.add(testDataButton);

		rightPanel.add(dataPanel);
		
		// Confusion Matrix Output
		rightPanel.add(new JPanel().add(new JLabel("Confusion Matrix")));
		
		// switch UI button
		switchButton = new JButton("Switch to Design");
		switchPanel.add(switchButton);
		switchButton.addActionListener(buttonHandler);
		controlPanel.add(switchPanel);
		
		// capture button
		captureButton = new JButton("Capture Image");
		capturePanel.add(captureButton);
		captureButton.addActionListener(buttonHandler);
		controlPanel.add(capturePanel);
		
		// eval button
		evalButton = new JButton("Evaluate Image");
		evalPanel.add(evalButton);
		evalButton.addActionListener(buttonHandler);
		controlPanel.add(evalPanel);
		
		// save button
		saveButton = new JButton("Save Image");
		savePanel.add(saveButton);
		saveButton.addActionListener(buttonHandler);
		controlPanel.add(savePanel);
		
		// quit button
		quitButton = new JButton("Quit");
		quitPanel.add(quitButton);
		quitButton.addActionListener(buttonHandler);
		controlPanel.add(quitPanel);
		
		rightPanel.add(controlPanel);
		
		// Confusion Matrix Output
		rightPanel.add(new JPanel().add(new JLabel("feature selection / moment size")));
		
		add(rightPanel);

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
				switchFrame.setBounds(TestUI.this.getBounds());
			} else if (event.getSource() == captureButton) {
				System.out.print("capture\n");
				setImage(camera.captureImage());
			} else if (event.getSource() == saveButton) {
				String strDesignSet = designTextField.getText();
				String strTestSet = testTextField.getText();
				// hard code design for now
				String strFileName = String.format("c:/ordata/%s-%s/c%s-%d.jpg", "design", strDesignSet, "1", 0);
				image.csu.fullerton.edu.Image.saveImage(currentImage, strFileName, "JPG");
			} else if (event.getSource() == momentsButton) {
				image.csu.fullerton.edu.Image.calculateMoments(currentImage);
			} else if (event.getSource() == calcClassifierButton) {
				System.out.printf("calculate classifier!\n");
			} else if (event.getSource() == testDataButton) {
				System.out.printf("test data!\n");
			} else {
				// ignore
			}
		}
	}

}
