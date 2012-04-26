package image.csu.fullerton.edu;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;
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

	private JButton captureButton;
	private JButton loadButton;
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
	private JPanel loadPanel;
	private JPanel quitPanel;
	private JPanel controlPanel;

	private BufferedImage currentImage;

	private String savedDirectoryPath;

	// Constructor: create the interface
	public DesignUI() {
		// Set title and layout
		super("Object Recognition -- Design");

		// handler for the input buttons
		ButtonHandler buttonHandler = new ButtonHandler();

		// design the layout: two
		setLayout(new GridLayout(2, 1, 5, 5));

		controlPanel = new JPanel(new GridLayout(1, 3, 10, 10));
		imagePanel = new JPanel(new GridLayout(1, 2));
		loadPanel = new JPanel(new FlowLayout());
		capturePanel = new JPanel(new FlowLayout());
		quitPanel = new JPanel(new FlowLayout());

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

		// quit button
		quitButton = new JButton("Quit");
		quitPanel.add(quitButton);
		quitButton.addActionListener(buttonHandler);
		controlPanel.add(quitPanel);
		add(controlPanel);

	}

	private void setImage(BufferedImage newImage) {
		ImageIcon newImageIcon = new ImageIcon(newImage);
		imageLabel.setIcon(newImageIcon);
		imageLabel.setVisible(true);
		currentImage = newImage;
	}

	private void desaturateImage() {
		System.out.printf("desaturateImage\n");
		if (currentImage != null) {
			BufferedImage newImage = new BufferedImage(currentImage.getWidth(),
					currentImage.getHeight(), BufferedImage.TYPE_INT_RGB);
			for (int y = 0; y < currentImage.getHeight(); y++) {
				for (int x = 0; x < currentImage.getWidth(); x++) {
					Color pixel = new Color(currentImage.getRGB(x, y));
					int b = pixel.getBlue();
					int g = pixel.getGreen();
					int r = pixel.getRed();
					int a = pixel.getAlpha();
					/* well-known formula, documented in many places */
					double grayValue = 0.3 * r + 0.59 * g + 0.11 * b;
					int gray = (0x000000FF & (int) grayValue);
					int finalGray = gray | gray << 8 | gray << 16;
					int newPixel = (a << 24) | finalGray;
					newImage.setRGB(x, y, newPixel);
				}
			}
			setImage(newImage);
		}
	}

	private void downscaleImage(int newWidth, int newHeight) {
		System.out.printf("downscaleImage\n");
	}

	private void edgeDetectImage() {
		/*
		 * http://processing.org/learning/topics/edgedetection.html
		 * modified in that it does an inaccurate grayscale on a color image
		 */
		System.out.printf("edgeDetectImage\n");
		if (currentImage != null) {
			BufferedImage newImage = new BufferedImage(currentImage.getWidth(), currentImage.getHeight(), BufferedImage.TYPE_INT_RGB);
			float filter[][] = { { -1, -1, -1 }, { -1, 9, -1 }, { -1, -1, -1 } };
			for (int y = 1; y < currentImage.getHeight() - 1; y++) {
				for (int x = 1; x < currentImage.getWidth() - 1; x++) {
					float sum = 0;
					for (int dy = -1; dy <= 1; dy++) {
						for (int dx = -1; dx <= 1; dx++) {
							int avgVal;
							int r, g, b;
							if (false) {
								Color pixel = new Color(currentImage.getRGB(x, y));
								b = pixel.getBlue();
								g = pixel.getGreen();
								r = pixel.getRed();
							} else {
								int pixel = currentImage.getRGB(x, y);
								b = (pixel & 0x00FF0000) >> 16;
								g = (pixel & 0x0000FF00) >> 8;
								r = pixel & 0x000000FF;
							}
							avgVal = (b + g + r) / 3;
							float val = (float) avgVal;
							sum += filter[dy + 1][dx + 1] * val;
						}
					}
					int newPixel = ((int) sum) & 0x000000FF;
					int finalPixel = 0xFF000000 | newPixel | newPixel << 8
							| newPixel << 16;
					newImage.setRGB(x, y, finalPixel);
				}
			}
			setImage(newImage);
		}
	}
	
	private void cannyEdgeDetectImage() {
		System.out.printf("cannyEdgeDetectImage\n");
		if (currentImage != null) {
			BufferedImage newImage = new BufferedImage(currentImage.getWidth(),
					currentImage.getHeight(), BufferedImage.TYPE_INT_RGB);
			CannyEdgeDetector detector = new CannyEdgeDetector();
			detector.setLowThreshold(0.5f);
			detector.setHighThreshold(6f);
			detector.setSourceImage(currentImage);
			detector.process();
			newImage = detector.getEdgesImage();
			setImage(newImage);
		}
	}

	private void thresholdImage() {
		System.out.printf("thresholdImage\n");
		if (currentImage != null) {
			BufferedImage newImage = new BufferedImage(currentImage.getWidth(),
					currentImage.getHeight(), BufferedImage.TYPE_INT_RGB);
			for (int y = 0; y < currentImage.getHeight(); y++) {
				for (int x = 0; x < currentImage.getWidth(); x++) {
					int avgVal;
					int a, r, g, b;
					int finalPixel;
					if (false) {
						Color pixel = new Color(currentImage.getRGB(x, y));
						b = pixel.getBlue();
						g = pixel.getGreen();
						r = pixel.getRed();
						a = pixel.getAlpha();
					} else {
						int pixel = currentImage.getRGB(x, y);
						b = (pixel & 0x00FF0000) >> 16;
						g = (pixel & 0x0000FF00) >> 8;
						r = pixel & 0x000000FF;
						a = (pixel & 0xFF000000) >> 24;
					}
					avgVal = (b + g + r) / 3;
					/*
					 * arbitrary value, could possibly make this settable or
					 * dynamic
					 */
					if (avgVal >= 192) {
						finalPixel = 0x00FFFFFF | a << 24;
					} else {
						finalPixel = 0x00000000 | a << 24;
					}
					newImage.setRGB(x, y, finalPixel);
				}
			}
			setImage(newImage);
		}
	}

	private void calculateMoments() {
		System.out.printf("calculateMoments\n");
		ImageMoments moments = new ImageMoments(currentImage);
		for (int i=1; i <= 7 ; i++) {
			System.out.printf("  Hu[%d]: %2.2f\n", i, moments.getMoment(i));
		}
		System.out.printf("  Flusser/Suk: %2.2f\n", moments.getMoment(8));
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
			// ending in one of the extentions appear in the window.
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

	// ButtonHandler: on Calculate, calculate pay. On Clear, clear
	// fields.
	private class ButtonHandler implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			if (event.getSource() == quitButton) {
				System.out.print("quit\n");
				DesignUI.this.dispose(); // clean up UI
				System.exit(0);
			} else if (event.getSource() == loadButton) {
				System.out.print("load\n");
				JFileChooser chooser = new JFileChooser();
				// Note: source for ExampleFileFilter can be found in
				// FileChooserDemo,
				// under the demo/jfc directory in the Java 2 SDK, Standard
				// Edition.
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
			} else if (event.getSource() == desaturateButton) {
				desaturateImage();
			} else if (event.getSource() == downscaleButton) {
				downscaleImage(64, 64);
			} else if (event.getSource() == edgedetectButton) {
				edgeDetectImage();
			} else if (event.getSource() == cannyEdgeDetectButton) {
				cannyEdgeDetectImage();
			} else if (event.getSource() == thresholdButton) {
				thresholdImage();
			} else if (event.getSource() == momentsButton) {
				calculateMoments();
			} else {
				// ignore
			}
		}
	}

}
