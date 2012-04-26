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
		DesignUI ui = new DesignUI();
		ui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		ui.setSize(appWidth,appHeight);
		ui.setVisible(true);
	}
}
