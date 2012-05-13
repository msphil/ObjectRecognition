package image.csu.fullerton.edu;

import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;

public class ImageDialog extends JDialog {
	private JButton dismissButton;

	public ImageDialog(BufferedImage image, int c) {
		this.setTitle("Image Evaluation");
		setSize(image.getWidth() + 50, image.getHeight() + 150);
		setLocation(0, 0);
		Container contentPane = this.getContentPane();
		//contentPane.setBackground(white);
		// set up vertical arrangement
		contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.PAGE_AXIS));

		dismissButton = new JButton("Dismiss");
		dismissButton.setToolTipText("Dismiss this dialog");

		// add components:
		JLabel imageLabel = new JLabel();
		ImageIcon newImageIcon = new ImageIcon(image);
		imageLabel.setIcon(newImageIcon);
		imageLabel.setVisible(true);
		
		contentPane.add(new JLabel(String.format("Evaluated as: %d",c)));
		contentPane.add(Box.createVerticalGlue());
		contentPane.add(imageLabel);
		contentPane.add(Box.createVerticalGlue());
		contentPane.add(dismissButton);
		contentPane.add(Box.createVerticalGlue());
		// hook up About box listeners
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				dismiss();
			}// end WindowClosing
		}// end anonymous class
		);// end addWindowListener line
		dismissButton.addActionListener(new ActionListener() {
			/**
			 * close down the About box when user clicks Dismiss
			 */
			public void actionPerformed(ActionEvent event) {
				Object object = event.getSource();
				if (object == dismissButton) {
					dismiss();
				}// end if
			}// end actionPerformed
		}// end anonymous class
				);// end addActionListener line
		this.validate();
		dismissButton.requestFocus();
		dismissButton.setFocusPainted(false);
		this.setVisible(true);
	}
	
	void dismiss()
    {
		this.dispose();
    }
	
}
