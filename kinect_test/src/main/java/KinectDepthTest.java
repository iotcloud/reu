import org.openkinect.freenect.*;

import java.lang.*;
import java.util.*;
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

	final double t_gamma[] = new double[1024];
	for(int p=0; p<1024; p++) {
	    t_gamma[p]=100 * 0.1236 * Math.tan(p / 2842.5 + 1.1863);
	}

        // DISPLAY DEPTH VIDEO
	dev.startDepth(new DepthHandler() {
		@Override
		    public void onFrameReceived(FrameMode mode, ByteBuffer frame, int timestamp) {
		    BufferedImage im = new BufferedImage(mode.getWidth(), mode.getHeight(), BufferedImage.TYPE_INT_RGB);
		    int r=0, b=0, g=0, i=0;
		    for(int y=0; y<mode.height; y++) {
			for(int x=0; x<mode.width; x++) {
			    int lo = frame.get(i) & 0xFF;
			    int hi = frame.get(i+1) & 0xFF;
			    int disp = hi << 8 | lo;
			    double dist = t_gamma[disp];
			    lo=0;
			    if (dist <= 150 && dist>=40) { //values real far away go negative
				b = 255;
				r = 0;
				g = 255-lo;
			    } else if (dist > 150 && dist <= 250) {
				b = 255-lo;
				r = lo;
				g = 0;
			    } else if (dist > 250 && dist <= 500){
				b = 0;
				r = 255-lo;
				g = 0;
			    } else if (disp==1023){
				b = 0;
				r = 0;
				g = 0;
			    } else {
				b = 255;
				r = 255;
				g = 255;
			    }
			    
			    int pixel = (0xFF) << 24
				| (b & 0xFF) << 16
				| (g & 0xFF) << 8
				| (r & 0xFF) << 0;
			    im.setRGB(x, y, pixel);
			    i+=2;
			}
		    }
		    //System.exit(0);	    
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

		    /*int t_gamma[] = new int[2048];
		    for(int i=0; i<2048; i++) {
			double v = i/2048.0;
			v = Math.pow(v,3) * 6.0;
			t_gamma[i] = (int)v*6*256;
			}*/



			    /*    int offset = 2*(y*mode.width+x);
			    int lo = frame.get(offset) & 0xFF;
			    int hi = frame.get(offset+1) & 0xFF;
			    int disp = hi << 8 | lo;
			    int pval = t_gamma[disp];
			    int lb = pval & 0xFF;
			    switch (pval>>8) {
			    case 0: // very far away, very close before error
				r = 255; //bluish
				b = 255-lb;
				g = 255-lb;
				break;
			    case 1: // next after case 0, seems correct
				r = 255; //purplish
				b = lb;
				g = 0;
				break;
			    case 2:
				r = 255-lb;// seems next
				b = 255;
				g = 0;
				break;
			    case 3: // seems next
				r = 0;
				b = 255;
				g = lb;
				break;
			    case 4: // depth shadows fall in this category
				r = 0;
				b = 255-lb;
				g = 255;
				break;
			    case 5:
				r = 0;
				b = 0;
				g = 255-lb;
				break;
			    default:
				r = 0;
				g = 0;
				b = 0;
				break;
			    }
			    if(disp==0) {
				r = 0;
				g= 0;
				b = 0;
			    }
			    int pixel = (0xFF) << 24
				| (b & 0xFF) << 16
				| (g & 0xFF) << 8
				| (r & 0xFF) << 0;
				im.setRGB(x, y, pixel);*/