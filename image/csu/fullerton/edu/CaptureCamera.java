package image.csu.fullerton.edu;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Image;
import java.awt.Panel;
import java.awt.image.BufferedImage;

import javax.media.Buffer;
import javax.media.CaptureDeviceInfo;
import javax.media.CaptureDeviceManager;
import javax.media.Manager;
import javax.media.MediaLocator;
import javax.media.Player;
import javax.media.control.FrameGrabbingControl;
import javax.media.format.VideoFormat;
import javax.media.util.BufferToImage;
import javax.swing.JPanel;

public class CaptureCamera {
	
    private Player player;

	CaptureCamera() {
		try {
		    CaptureDeviceInfo DI;
		    MediaLocator ML;
			String strCamera = "vfw:Microsoft WDM Image Capture (Win32):0";
			DI = CaptureDeviceManager.getDevice(strCamera);
	        ML = new MediaLocator("vfw://0");
            player = Manager.createRealizedPlayer(ML);
            player.start();
            Thread.sleep(3000);
		} catch (Exception e) {
            e.printStackTrace();
        }
	}
	
	void addToPanel(Panel cameraPanel) {
        Component comp;
        if ((comp = player.getVisualComponent()) != null) {
            cameraPanel.add(comp, BorderLayout.NORTH);
        }
	}

	public BufferedImage captureImage() {
	    Buffer BUF;
	    BufferedImage img;
	    BufferToImage BtoI;
	    
        FrameGrabbingControl fgc = (FrameGrabbingControl) player.getControl("javax.media.control.FrameGrabbingControl");
        BUF = fgc.grabFrame();

        // Convert it to an image
        BtoI = new BufferToImage((VideoFormat) BUF.getFormat());
        img = (BufferedImage)BtoI.createImage(BUF);
		
		return img;
	}

}
