package image.csu.fullerton.edu;

import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Panel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class DesignUI extends JFrame {

	private TestUI switchFrame;
	
	private JButton captureButton;
	private JButton loadButton;
	private JButton saveButton;
	private JButton switchButton;
	private JButton quitButton;

	private JButton desaturateButton;
	private JButton downscaleButton;
	private JButton edgedetectButton;
	private JButton sobelButton;
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
	private ImagePanel cameraPanel;	// custom panel for cameras
	
	private CaptureCamera camera;	// custom camera object
	
	private BufferedImage currentImage;

	private String savedDirectoryPath;

	// Constructor: create the interface
	public DesignUI(CaptureCamera camera) {
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
		desaturateButton = new JButton("Desaturate");
		desaturateButton.addActionListener(buttonHandler);
		processPanel.add(desaturateButton);
		downscaleButton = new JButton("Downscale");
		downscaleButton.addActionListener(buttonHandler);
		processPanel.add(downscaleButton);
		edgedetectButton = new JButton("Edge Detect");
		edgedetectButton.addActionListener(buttonHandler);
		processPanel.add(edgedetectButton);
		sobelButton = new JButton("Sobel Edge Detect");
		sobelButton.addActionListener(buttonHandler);
		processPanel.add(sobelButton);
		cannyEdgeDetectButton = new JButton("Canny Edge Detect");
		cannyEdgeDetectButton.addActionListener(buttonHandler);
		processPanel.add(cannyEdgeDetectButton);
		thresholdButton = new JButton("Threshold");
		thresholdButton.addActionListener(buttonHandler);
		processPanel.add(thresholdButton);
		momentsButton = new JButton("Calculate Moments");
		momentsButton.addActionListener(buttonHandler);
		processPanel.add(momentsButton);
		imagePanel.add(processPanel);

		add(imagePanel);
		
		// video camera capture
		cameraPanel = new ImagePanel();
		this.camera = camera;
		camera.addToPanel(cameraPanel);
		bottomPanel.add(cameraPanel);		

		// save button
		saveButton = new JButton("Save");
		savePanel.add(saveButton);
		saveButton.addActionListener(buttonHandler);
		controlPanel.add(savePanel);

		// load button
		loadButton = new JButton("Load");
		loadPanel.add(loadButton);
		loadButton.addActionListener(buttonHandler);
		controlPanel.add(loadPanel);

		// capture button
		captureButton = new JButton("Capture");
		capturePanel.add(captureButton);
		captureButton.addActionListener(buttonHandler);
		controlPanel.add(capturePanel);

		// switch UI button
		switchButton = new JButton("Switch to Test");
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

	public void bondCamera() {
		camera.addToPanel(cameraPanel);
	}
	
	private void setImage(BufferedImage newImage) {
		ImageIcon newImageIcon = new ImageIcon(newImage);
		imageLabel.setIcon(newImageIcon);
		imageLabel.setVisible(true);
		currentImage = newImage;
	}
	
	void setSwitcher(JFrame newFrame) {
		switchFrame = (TestUI)newFrame;
	}

	private class ImageFileFilter extends javax.swing.filechooser.FileFilter {
		protected String description;

		protected ArrayList exts = new ArrayList();

		public void addExtension(String s) {
			exts.add(s);
		}

		/** Return true if the given file is accepted by this filter. */
		public boolean accept(File f) {
			// Little trick: if you don't do this, only directory names
			// ending in one of the extensions appear in the window.
			if (f.isDirectory()) {
				return true;

			} else if (f.isFile()) {
				Iterator it = exts.iterator();
				while (it.hasNext()) {
					if (f.getName().endsWith((String) it.next()))
						return true;
				}
			}

			// A file that didn't match, or a weirdo (e.g. UNIX device file?).
			return false;
		}

		/** Set the printable description of this filter. */
		public void setDescription(String s) {
			description = s;
		}

		/** Return the printable description of this filter. */
		public String getDescription() {
			return description;
		}
	}

	// ButtonHandler:
	private class ButtonHandler implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			if (event.getSource() == quitButton) {
				System.out.print("quit\n");
				DesignUI.this.dispose(); // clean up UI
				System.exit(0);
			} else if (event.getSource() == switchButton) {
				DesignUI.this.setVisible(false);
				switchFrame.setVisible(true);
				switchFrame.setBounds(DesignUI.this.getBounds());
				switchFrame.bondCamera();
			} else if (event.getSource() == loadButton) {
				System.out.print("load\n");
				JFileChooser chooser = new JFileChooser();
				// Note: source for ExampleFileFilter can be found in FileChooserDemo,
				// under the demo/jfc directory in the Java 2 SDK, Standard Edition.
				ImageFileFilter filter = new ImageFileFilter();
				filter.addExtension("jpg");
				filter.addExtension("png");
				filter.setDescription("Supported Images (*.jpg, *.png)");
				if (savedDirectoryPath != null) {
					File previousDirectory = new File(savedDirectoryPath);
					chooser.setCurrentDirectory(previousDirectory);
				}
				chooser.setFileFilter(filter);
				int returnVal = chooser.showOpenDialog(getParent());
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					System.out.printf("You chose to open this file: '%s'\n",
							chooser.getSelectedFile().getName());
					savedDirectoryPath = chooser.getSelectedFile().getPath();
					try {
						BufferedImage newImage = ImageIO.read(new File(chooser
								.getSelectedFile().getAbsolutePath()));
						setImage(newImage);
					} catch (IOException e) {
						System.out.printf("exception: %s\n", e.getMessage());
					}
				}
			} else if (event.getSource() == captureButton) {
				System.out.print("capture\n");
				setImage(camera.captureImage());
			} else if (event.getSource() == saveButton) {
				image.csu.fullerton.edu.Image.saveImage(currentImage, "c:/ordata/test/test.jpg", "JPG");
			} else if (event.getSource() == desaturateButton) {
				setImage(image.csu.fullerton.edu.Image.desaturateImage(currentImage));
			} else if (event.getSource() == downscaleButton) {
				setImage(image.csu.fullerton.edu.Image.downscaleImage(currentImage, 64, 64));
			} else if (event.getSource() == edgedetectButton) {
				setImage(image.csu.fullerton.edu.Image.edgeDetectImage(currentImage));
			} else if (event.getSource() == sobelButton) {
				setImage(image.csu.fullerton.edu.Image.sobelEdgeDetectImage(currentImage));
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
