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

	final double t_gamma[] = new double[1024];
	for(int p=0; p<1024; p++) {
	    t_gamma[p]=100 * 0.1236 * Math.tan(p / 2842.5 + 1.1863);
	}

        // DISPLAY DEPTH VIDEO
	dev.startDepth(new DepthHandler() {
		double avRawLen=0,p=0,avCompLen=0;
		byte[] data = null;
		@Override
		    public void onFrameReceived(FrameMode mode, ByteBuffer frame, int timestamp) {
		    BufferedImage im = new BufferedImage(mode.getWidth(), mode.getHeight(), BufferedImage.TYPE_INT_RGB);
		    data = new byte[614400];
		    for(int i=0; i<614399; i++) data[i] = frame.get(i);
		    
		    avRawLen = avRawLen + data.length;

		    //frame.get(data); //bulk get causes error
		    
		    /*try {
			FileOutputStream outRaw = new FileOutputStream("raw_depth.txt");
			outRaw.write(data);
			outRaw.close();
		    } catch (IOException e) {
			System.exit(0);
			}*/
		    
		    // compress data
		    try {
			byte[] compressed = Snappy.compress(data);
			byte[] restored = Snappy.uncompress(compressed);
			avCompLen = avCompLen + compressed.length;
			p++;

			/*try {
			    FileOutputStream outComp = new FileOutputStream("compressed_depth.txt");
			    outComp.write(compressed);
			outComp.close();
			} catch (IOException e) {
			    System.exit(0);
			}*/
			
			for(int i=0; i<614400; i++) frame.put(i, restored[i]);
		    } catch (IOException e) {
			System.exit(0);
		    } 
		    //frame.put(restored); //as with bulk put, causes error
		    
		    
		    // COLOR DISTANCE DATA
		    int r=0, b=0, g=0, x, y;
		    //for(int y=0; y<mode.height; y++) {
		    //	for(int x=0; x<mode.width; x++) {
		    for(int i=0; i<614400; i+=2) {		    
			    int lo = frame.get(i) & 0xFF;
			    int hi = frame.get(i+1) & 0xFF;
			    int disp = hi << 8 | lo;
			    double dist = t_gamma[disp];
			    if (dist >= 40 && dist<150) { //values real far away go negative
				b = 255;
				r = 0;
				g = (int)(255-((dist-40)/109*255));
			    } else if (dist >= 150 && dist <= 250) {
				dist = ((dist-150)/100*255);
				b = (int)(255-dist);
				r = (int)(dist);
				g = 0;
			    } else if (dist > 250 && dist <= 500){
				dist = (dist-251)/249*255;
				b = 0;
				r = (int)(255-dist);
				g = (int)(dist);
			    } else if (disp==1023){
				b = 0;
				r = 0;
				g = 0;
			    } else {
				dist = (dist-501)/t_gamma[1022]*255;
				b = 20;
				r = 0;
				g = (int)(255-dist);
			    }
			    
			    y=(int)Math.floor((double)i/2/640);
			    x=i/2-640*y;
			    int pixel = (0xFF) << 24
				| (b & 0xFF) << 16
				| (g & 0xFF) << 8
				| (r & 0xFF) << 0;
			    im.setRGB(x, y, pixel);
		    }
		    //}
		    ui.setImage(im);
		    ui.repaint();
		    //System.exit(0);
		    System.out.println("avRawLen: " + avRawLen/p + " avCompLen: " + avCompLen/p);
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
