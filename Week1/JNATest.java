//taken from http://openkinect.org/wiki/Java_JNA_Wrapper
import org.openkinect.freenect.*;
 
public class JNATest {
 
    public static void main(String[] args) throws InterruptedException {
 
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
	        dev.setTiltAngle(20);
	        Thread.sleep(4000);
	        dev.setTiltAngle(-20);
	        Thread.sleep(4000);
	        dev.setTiltAngle(0);
 
	    // SHUT DOWN
	        if (ctx != null)
	            if (dev != null) {
	                dev.close();
	            }
	        ctx.shutdown();
    }
 
}
