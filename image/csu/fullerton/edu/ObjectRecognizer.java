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
	public static void main (String[] args)
	{
		final int appWidth = 750;
		final int appHeight = 450;
		DesignUI uiDesign = new DesignUI();
		uiDesign.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		uiDesign.setSize(appWidth,appHeight);
		uiDesign.setVisible(true);
		TestUI uiTest = new TestUI();
		uiTest.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		uiTest.setSize(appWidth,appHeight);
		uiTest.setVisible(false);
		uiDesign.setSwitcher(uiTest);
		uiTest.setSwitcher(uiDesign);
	}
}
