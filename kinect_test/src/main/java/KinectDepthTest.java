import org.openkinect.freenect.*;

import java.awt.image.BufferedImage;
import java.nio.ByteBuffer;

public class KinectDepthTest {
    public static void main(String[] args) throws InterruptedException {
        final ImageUI ui = new ImageUI();
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
		ui.show();
            }
        });
        t.start();

        // DECLARATIONS
        Context ctx = null;
        Device dev = null;

        // INITIALIZE DEVICE
        ctx = Freenect.createContext();
        if (ctx.numDevices() > 0) {
            dev = ctx.openDevice(0);
        } else {
            System.err.println("No kinects detected.  Exiting.");
            System.exit(0);
        }

        // TILT UP, DOWN, & RETURN
        //dev.setTiltAngle(20);
        //Thread.sleep(4000);
        //dev.setTiltAngle(-20);
        //Thread.sleep(4000);
        //dev.setTiltAngle(0);

        dev.startDepth(new DepthHandler() {
            @Override
            public void onFrameReceived(FrameMode mode, ByteBuffer frame, int timestamp) {
                BufferedImage im = new BufferedImage(mode.getWidth(), mode.getHeight(), BufferedImage.TYPE_INT_RGB);
		int r=0, b=0, g=0;
		for(int y=0; y<mode.height; y++) {
		    for(int x=0; x<mode.width; x++) { 
                        int offset = 2*(y*mode.width+x);
			int lo = frame.get(offset) & 0xFF;
			int hi = frame.get(offset+1) & 0xFF;
			int disp = hi << 8 | lo;
			//Why does ln below give a different value than the 3 precedeeing lines?  
			//int disparity = (frame.get(offset)) & 0xFFFF;
                        double dist = 100/(-0.00307 * disp + 3.33);
			int lb = (int) (dist) & 0xFF;	
			if (dist <= 150) {
			    b = 255;
			    r = 0;
			    g = 255;
			} else if (dist > 150 && dist < 300) {
			    b = 255;
			    r = 0;
			    g = 0;
			} else if (dist > 300){
			    b = 0;
			    r = 255;
			    g = 0;
			}
		
			int pixel = (0xFF) << 24
			    | (b & 0xFF) << 16
			    | (g & 0xFF) << 8
			    | (r & 0xFF) << 0;
			im.setRGB(x, y, pixel);
		    }
		}
		ui.setImage(im);
		ui.repaint();
	    }
	    });
        Thread.sleep(1000000);

        // SHUT DOWN
        if (ctx != null) {
            if (dev != null) {
                dev.close();
            }
        }
        ctx.shutdown();
    }
}
