import org.openkinect.freenect.*;

public class JNATest2 {

    public static void main(String[] args) throws InterruptedException {

	// DECLARATIONS
	Context ctx = null;
	Device dev = null;
	LedStatus ledstat = LedStatus.BLINK_YELLOW;
	double accelstat[];
	double tiltang;

	// INITIALIZE DEVICE
	ctx = Freenect.createContext();
	if (ctx.numDevices() > 0) {
	    dev = ctx.openDevice(0);
	} else {
	    System.err.println("No kinects detected. Exiting.");
	    System.exit(0);
	}

	// MAKE THE KINECT DO STUFF
	dev.setLed(ledstat);
	Thread.sleep(1000);
	dev.setLed(ledstat.BLINK_YELLOW);
	Thread.sleep(1000);
	accelstat = dev.getAccel();
	for(int i=0; i<accelstat.length; i++) {
	    System.out.println(accelstat[i]);
	}
	tiltang = dev.getTiltAngle();
	System.out.println(tiltang);
	tiltang = Integer.parseInt(args[0]);
	dev.setTiltAngle(tiltang);
	tiltang = dev.getTiltAngle();
	System.out.println(tiltang);
	Thread.sleep(1000);
	dev.setTiltAngle(0);
	tiltang = dev.getTiltAngle();
	System.out.println(tiltang);

	// SHUT DOWN
	if (ctx != null)
	    if(dev != null) {
		dev.close();
	    }
	ctx.shutdown();
    }

}
