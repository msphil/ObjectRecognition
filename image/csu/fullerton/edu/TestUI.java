package image.csu.fullerton.edu;

import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

public class TestUI extends JFrame {
	
	private JFrame switchFrame;

	private JButton captureButton;
	private JButton loadButton;
	private JButton saveButton;
	private JButton evalButton;
	private JButton switchButton;
	private JButton quitButton;

	private JRadioButton designRadioButton;
	private JRadioButton testRadioButton;
	private ButtonGroup groupDesignTest;
	
	private JRadioButton sobelRadioButton;
	private JRadioButton sobelThresholdRadioButton;
	private JRadioButton cannyRadioButton;
	private ButtonGroup groupFeatureSet;
	
	private JRadioButton hu6RadioButton;
	private JRadioButton hu7RadioButton;
	private JRadioButton hu8RadioButton;
	private ButtonGroup groupFeatureVector;
	
	private JButton calcClassifierButton;
	private JButton testDataButton;
	
	private JTextField designTextField;
	private JTextField testTextField;
	private JTextField classTextField;

	private JLabel imageLabel;

	private ImagePanel cameraPanel;	// custom panel for cameras
	
	private CaptureCamera camera;	// custom camera object
	
	private BufferedImage currentImage;

	private String savedDirectoryPath;
	
	private KNearestNeighbor knn;
	
	// Constructor: create the interface
	public TestUI() {
		// Set title and layout
		super("Object Recognition -- Design");

		JPanel capturePanel;
		JPanel savePanel;
		JPanel evalPanel;
		JPanel loadPanel;
		JPanel switchPanel;
		JPanel quitPanel;
		JPanel controlPanel;
		JPanel dataPanel;
		JPanel designTextPanel;
		JPanel testTextPanel;
		JPanel classTextPanel;
		JPanel pickDesignTestPanel;
		JPanel classAndFeaturePanel;
		
		JPanel leftPanel;
		JPanel rightPanel;
		
		// handler for the input buttons
		ButtonHandler buttonHandler = new ButtonHandler();

		// design the layout: two
		setLayout(new GridLayout(1, 2, 5, 5));

		savePanel = new JPanel(new FlowLayout());
		evalPanel = new JPanel(new FlowLayout());
		loadPanel = new JPanel(new FlowLayout());
		capturePanel = new JPanel(new FlowLayout());
		switchPanel = new JPanel(new FlowLayout());
		quitPanel = new JPanel(new FlowLayout());
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
		dataPanel = new JPanel();
		dataPanel.setLayout(new BoxLayout(dataPanel, BoxLayout.PAGE_AXIS));
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
		calcClassifierButton.addActionListener(buttonHandler);
		dataPanel.add(calcClassifierButton);
		testDataButton = new JButton("Test Data With Current Classifier");
		testDataButton.addActionListener(buttonHandler);
		dataPanel.add(testDataButton);

		rightPanel.add(dataPanel);
		
		// Confusion Matrix Output
		rightPanel.add(new JPanel().add(new JLabel("Confusion Matrix")));
		
		// control panel (capture/eval/save/switch/quit)
		controlPanel = new JPanel();
		controlPanel.setLayout(new BoxLayout(controlPanel, BoxLayout.PAGE_AXIS));
		
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
		classTextPanel = new JPanel(new FlowLayout());
		classTextField = new JTextField(4);
		classTextPanel.add(new JLabel("Class: "));
		classTextPanel.add(classTextField);
		savePanel.add(classTextPanel);
		pickDesignTestPanel = new JPanel(new GridLayout(1,2));
		designRadioButton = new JRadioButton("Design");
		designRadioButton.setMnemonic(KeyEvent.VK_D);
		designRadioButton.addActionListener(buttonHandler);
		testRadioButton = new JRadioButton("Test");
		testRadioButton.setMnemonic(KeyEvent.VK_T);
		testRadioButton.addActionListener(buttonHandler);
		groupDesignTest = new ButtonGroup();
		groupDesignTest.add(designRadioButton);
		groupDesignTest.add(testRadioButton);
		designRadioButton.setSelected(true);
		pickDesignTestPanel.add(designRadioButton);
		pickDesignTestPanel.add(testRadioButton);
		savePanel.add(pickDesignTestPanel);
		controlPanel.add(savePanel);
		
		// quit button
		quitButton = new JButton("Quit");
		quitPanel.add(quitButton);
		quitButton.addActionListener(buttonHandler);
		controlPanel.add(quitPanel);
		
		rightPanel.add(controlPanel);
		
		// pre-processing selector
		classAndFeaturePanel = new JPanel();
		classAndFeaturePanel.setLayout(new BoxLayout(classAndFeaturePanel, BoxLayout.PAGE_AXIS));
		classAndFeaturePanel.add(new JPanel().add(new JLabel("feature selection:")));
		
		sobelRadioButton = new JRadioButton("Sobel Edge Detection");
		sobelRadioButton.addActionListener(buttonHandler);
		sobelThresholdRadioButton = new JRadioButton("Sobel + Threshold");
		sobelThresholdRadioButton.addActionListener(buttonHandler);
		cannyRadioButton = new JRadioButton("Canny Edge Detection");
		cannyRadioButton.addActionListener(buttonHandler);
		groupFeatureSet = new ButtonGroup();
		groupFeatureSet.add(sobelRadioButton);
		groupFeatureSet.add(sobelThresholdRadioButton);
		groupFeatureSet.add(cannyRadioButton);
		sobelRadioButton.setSelected(true);
		classAndFeaturePanel.add(sobelRadioButton);
		classAndFeaturePanel.add(sobelThresholdRadioButton);
		classAndFeaturePanel.add(cannyRadioButton);
		
		classAndFeaturePanel.add(new JPanel().add(new JLabel("moment size selection:")));
		
		hu7RadioButton = new JRadioButton("Classic Hu (7)");
		hu7RadioButton.addActionListener(buttonHandler);
		hu8RadioButton = new JRadioButton("Hu + Flusser (8)");
		hu8RadioButton.addActionListener(buttonHandler);
		hu6RadioButton = new JRadioButton("minimal Hu + Flusser (6)");
		hu6RadioButton.addActionListener(buttonHandler);
		groupFeatureVector = new ButtonGroup();
		groupFeatureVector.add(hu7RadioButton);
		groupFeatureVector.add(hu8RadioButton);
		groupFeatureVector.add(hu6RadioButton);
		hu8RadioButton.setSelected(true);
		classAndFeaturePanel.add(hu7RadioButton);
		classAndFeaturePanel.add(hu8RadioButton);
		classAndFeaturePanel.add(hu6RadioButton);
		
		classAndFeaturePanel.add(new JPanel().add(new JLabel("classifier selection:")));
		
		rightPanel.add(classAndFeaturePanel);
		
		add(rightPanel);

	}
	
	private void setImage(BufferedImage newImage) {
		ImageIcon newImageIcon = new ImageIcon(newImage);
		imageLabel.setIcon(newImageIcon);
		imageLabel.setVisible(true);
		imageLabel.paint(imageLabel.getGraphics());
		currentImage = newImage;
	}
	
	void setSwitcher(JFrame newFrame) {
		switchFrame = newFrame;
	}
	
	double[] getFeatureVector(ImageMoments im) {
		double[] all = im.getAllMoments();
		double[] ret = null;
		if (hu6RadioButton.isSelected()) {
			ret = new double[6];
			ret[0] = all[0];
			ret[1] = all[3];
			ret[2] = all[4];
			ret[3] = all[5];
			ret[4] = all[6];
			ret[5] = all[7];
		} else if (hu7RadioButton.isSelected()) {
			ret = new double[7];
			ret[0] = all[0];
			ret[1] = all[1];
			ret[2] = all[2];
			ret[3] = all[3];
			ret[4] = all[4];
			ret[5] = all[5];
			ret[6] = all[6];
		} else {
			ret = all;
		}
		return ret;
	}
	
	private int getNextFileName(String strFileName) {
		// we're going to start at 1, for no particular reason
		int nextFile = 1;
		for (; ; nextFile++) {
			String strNewFileName = String.format("%s%d.jpg", strFileName, nextFile);
			File f = new File(strNewFileName);
			if (!f.exists())
				break;
		}
		return nextFile;
	}

	private boolean classHasFiles(String strPath, String strClass) {
		String strNewFileName = String.format("%s/c%s-1.jpg", strPath, strClass);
		File f = new File(strNewFileName);
		return f.exists();
	}

	private int preProcessingType() {
		if (sobelRadioButton.isSelected()) {
			return 1;
		} else if (sobelThresholdRadioButton.isSelected()) {
			return 2;
		} else if (cannyRadioButton.isSelected()) {
			return 3;
		} else 
			return 0;
	}
	
	private BufferedImage processImage(BufferedImage image) {
		BufferedImage processedImage = null;
		
		setImage(image);
		processedImage = Image.downscaleImage(image, 256, 160);
		setImage(processedImage);
		switch (preProcessingType()) {
		case 1:
			processedImage = Image.desaturateImage(processedImage);
			setImage(processedImage);
			processedImage = Image.sobelEdgeDetectImage(processedImage);
			setImage(processedImage);
			break;
		case 2:
			processedImage = Image.desaturateImage(processedImage);
			setImage(processedImage);
			processedImage = Image.sobelEdgeDetectImage(processedImage);
			setImage(processedImage);
			processedImage = Image.thresholdImage(processedImage);
			setImage(processedImage);
			break;
		case 3:
			processedImage = Image.cannyEdgeDetectImage(processedImage);
			setImage(processedImage);
			break;
		}
		
		return processedImage;
	}
	
	private BufferedImage processImage(String strFileName, int c) {
		// hard code pre-processing/moment size temporarily
		File f = new File(strFileName);
		BufferedImage processedImage = null;
		try {
			BufferedImage newImage = ImageIO.read(f);
			processedImage = processImage(newImage);

		} catch (IOException e) {
			e.printStackTrace();
		}
		return processedImage;
	}

	private boolean isDesign() {
		if (designRadioButton.isSelected()) 
			return true;
		return false;
	}
	
	private int getKValue() {
		return 3;
	}
	
	private int getClassValue() {
		String strClass = classTextField.getText();
		return Integer.parseInt(strClass);
	}
	
	private void processImageFileAndAdd(String strFileName, int c) {
		BufferedImage processedImage = processImage(strFileName, c);
		if (processedImage != null) {
			ImageMoments im = new ImageMoments(processedImage);
			knn.addDesign(getFeatureVector(im), c);
		}
	}

	private int processImageFileAndTest(String strFileName, int c) {
		int ret_c = 0;
		BufferedImage processedImage = processImage(strFileName, c);
		if (processedImage != null) {
			ImageMoments im = new ImageMoments(processedImage);
			ret_c = knn.testVector(getFeatureVector(im), getKValue());
		}
		return ret_c;
	}

	private int processImageForResult(BufferedImage image) {
		int ret_c = 0;
		BufferedImage processedImage = processImage(image);
		if (processedImage != null) {
			ImageMoments im = new ImageMoments(processedImage);
			ret_c = knn.testVector(getFeatureVector(im), getKValue());
		}
		return ret_c;
	}

	// ButtonHandler: 
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
				String strIntermediateFileName = String.format("c:/ordata/%s-%s/c%s-", isDesign() ? "design" : "test", isDesign() ? strDesignSet : strTestSet, getClassValue());
				int nextFile = getNextFileName(strIntermediateFileName);
				String strFileName = String.format("%s%d.jpg", strIntermediateFileName, nextFile);
				image.csu.fullerton.edu.Image.saveImage(currentImage, strFileName, "JPG");
			} else if (event.getSource() == evalButton) {
				System.out.printf("evaluate current image!\n");
				int c = processImageForResult(currentImage);
				System.out.printf("Current image evaluated to %d\n", c);
			} else if (event.getSource() == calcClassifierButton) {
				System.out.printf("calculate classifier!\n");
				knn = new KNearestNeighbor();
				int c = 1;
				while (classHasFiles(String.format("c:/ordata/design-%s",designTextField.getText()), String.format("%d",c))) {
					int i = 1;
					boolean done = false;
					while (!done) {
						String strFileName = String.format("c:/ordata/design-%s/c%d-%d.jpg",designTextField.getText(),c,i);
						File f = new File(strFileName);
						if (f.exists()) {
							System.out.printf("Processing '%s'\n", strFileName);
							processImageFileAndAdd(strFileName, c);
						} else {
							done = true;
						}
						i++;
					}
					c++;
				}
			} else if (event.getSource() == testDataButton) {
				System.out.printf("test data!\n");
				if (knn != null) {
					int c = 1;
					while (classHasFiles(String.format("c:/ordata/test-%s",testTextField.getText()), String.format("%d",c))) {
						int i = 1;
						boolean done = false;
						while (!done) {
							String strFileName = String.format("c:/ordata/test-%s/c%d-%d.jpg",testTextField.getText(),c,i);
							File f = new File(strFileName);
							if (f.exists()) {
								System.out.printf("Processing '%s'\n", strFileName);
								int eval_c = processImageFileAndTest(strFileName, c);
								if (eval_c == c) {
									System.out.printf("'%s' was classified correctly!\n", strFileName);
								} else {
									System.out.printf("'%s' (expected %d) was mis-classified as %d\n", strFileName, c, eval_c);
								}
							} else {
								done = true;
							}
							i++;
						}
						c++;
					}
				}
			} else {
				// ignore
				System.out.printf("Ignoring event\n");
			}
		}
	}

}
