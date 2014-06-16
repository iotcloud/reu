import org.openkinect.freenect.*;
import org.xerial.snappy.Snappy;

import java.io.*;
import java.lang.*;
import java.util.*;
import java.awt.image.BufferedImage;
import java.nio.ByteBuffer;

public class CompressionTest {
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

        // DISPLAY DEPTH VIDEO
	dev.startDepth(new DepthHandler() {
		@Override
		    public void onFrameReceived(FrameMode mode, ByteBuffer frame, int timestamp) {
		    BufferedImage im = new BufferedImage(mode.getWidth(), mode.getHeight(), BufferedImage.TYPE_INT_RGB);

		    byte[] data = new byte[614400];
		    for(int i=0; i<614399; i++) data[i] = frame.get(i);
		    //frame.get(data); //bulk get causes error
		    
		    try {
			FileOutputStream outRaw = new FileOutputStream("raw_depth.txt");
			outRaw.write(data);
			outRaw.close();
		    } catch (IOException e) {
			System.exit(0);
		    }
		    
		    // compress data
		    try {
			byte[] compressed = Snappy.compress(data);
			byte[] restored = Snappy.uncompress(compressed);
			
			try {
			    FileOutputStream outComp = new FileOutputStream("compressed_depth.txt");
			    outComp.write(compressed);
			outComp.close();
			} catch (IOException e) {
			    System.exit(0);
			}
			
			for(int i=0; i<614400; i++) frame.put(i, restored[i]);
		    } catch (IOException e) {
			System.exit(0);
		    } 
		    //frame.put(restored); //as with bulk put, causes error


		    // COLOR DISTANCE DATA
		    int r=0, b=0, g=0;
		    for(int y=0; y<mode.height; y++) {
			for(int x=0; x<mode.width; x++) { 
			    int offset = 2*(y*mode.width+x);
			    int lo = frame.get(offset) & 0xFF;
			    int hi = frame.get(offset+1) & 0xFF;
			    int disp = hi << 8 | lo;
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
		    //System.exit(0);
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
