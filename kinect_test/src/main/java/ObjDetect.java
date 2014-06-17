import org.openkinect.freenect.*;

import java.nio.ByteBuffer;

public class ObjDetect {
    public static void main(String[] args) throws InterruptedException {

	// DECLARATIONS
	Context ctx = null;
	Device dev = null;

	// INITIALIZE DEVICE
	ctx = Freenect.createContext();
	if (ctx.numDevices() > 0) {
	    dev = ctx.openDevice(0);
	} else {
	    System.err.println("No kinect detected. Exiting.");
	    System.exit(0);
	}

	final double t_gamma[] = new double[1024];
	for(int p=0; p<1024; p++) {
	    t_gamma[p]=100 * 0.1236 * Math.tan(p / 2842.5 + 1.1863);
	}

	dev.startDepth(new DepthHandler() {
		@Override
		    public void onFrameReceived(FrameMode mode, ByteBuffer frame, int timestamp) {
		    for(int y=0; y<mode.height; y++) {
			for(int x=0; x<mode.width; x++) {
			    int offset = 2*(y*mode.width+x);
			    int lo = frame.get(offset) & 0xFF;
			    int hi = frame.get(offset+1) & 0xFF;
			    int disp = hi << 8 | lo;
			    double dist = t_gamma[disp];
			    if(dist<100 && dist>80) System.out.println("Object Detected");
			}
		    }
		}
	    });
	Thread.sleep(10000);

	// SHUT DOWN
	if (ctx != null) {
	    if(dev != null) {
		dev.close();
	    }
	}
	ctx.shutdown();
    }
}