package image.csu.fullerton.edu;

import java.awt.BorderLayout;
import java.awt.Component;
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

public class CaptureCamera {
	
    private Player player;

	CaptureCamera() {
		player = null;
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
            cameraPanel.add(comp, BorderLayout.CENTER);
        } else {
        	System.out.printf("Unable to obtain visual component for camera!\n");
        }
	}

	public BufferedImage captureImage() {
	    Buffer BUF;
	    BufferedImage img = null;
	    BufferToImage BtoI;
	    
        FrameGrabbingControl fgc = (FrameGrabbingControl) player.getControl("javax.media.control.FrameGrabbingControl");
        if (fgc != null) {
        	BUF = fgc.grabFrame();
            // Convert it to an image
            BtoI = new BufferToImage((VideoFormat) BUF.getFormat());
            img = (BufferedImage)BtoI.createImage(BUF);
        } else {
        	System.out.printf("Failed to obtain frame grabber!\n");
        }

		return img;
	}

}
