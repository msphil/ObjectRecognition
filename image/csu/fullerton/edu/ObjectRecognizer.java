package image.csu.fullerton.edu;
// Michael Phillips
// msphil@gmail.com
// CPSC483
// Final Project
// 
// Object Recognition via Image Moments
// 2012/05/10

import javax.swing.JFrame;

// ObjectRecognizer: class which instantiates the Object Recognition UI
public class ObjectRecognizer
{
	private static CaptureCamera camera;

	public static void main (String[] args)
	{
		final int appWidth = 1000;
		final int appHeight = 700;
		camera = new CaptureCamera();
		DesignUI uiDesign = new DesignUI(camera);
		uiDesign.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		uiDesign.setSize(appWidth,appHeight);
		uiDesign.setVisible(false);
		TestUI uiTest = new TestUI(camera);
		uiTest.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		uiTest.setSize(appWidth,appHeight);
		uiTest.setVisible(true);
		uiDesign.setSwitcher(uiTest);
		uiTest.setSwitcher(uiDesign);
	}
}
