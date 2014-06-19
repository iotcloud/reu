import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.MessageProperties;

import org.openkinect.freenect.*;
import org.xerial.snappy.Snappy;

import java.io.*;
import java.lang.*;
import java.util.*;
import java.awt.image.BufferedImage;
import java.nio.ByteBuffer;

public class SendFrame {

    private static final String TASK_QUEUE_NAME = "frame_queue";

    public static void main(String[] args) throws InterruptedException {
	try {

	ConnectionFactory factory = new ConnectionFactory();
	factory.setHost("localhost");
	Connection connection = factory.newConnection();
	final Channel channel = connection.createChannel();
	
	channel.queueDeclare(TASK_QUEUE_NAME, true, false, false, null);
	
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

		    byte[] data = new byte[614400];
		    for(int i=0; i<614399; i++) data[i] = frame.get(i);

		    byte[] compressed = null;
		    try {
			compressed = Snappy.compress(data);

			channel.basicPublish( "", TASK_QUEUE_NAME, MessageProperties.PERSISTENT_TEXT_PLAIN, compressed);
			} catch (IOException e) {
			System.exit(0);
		    } 
		}
	    });

	Thread.sleep(5000);

	dev.stopDepth();

	channel.close();
	connection.close();
	
	// SHUT DOWN
	if (ctx != null) {
	    if (dev != null) {
		dev.close();
	    }
	}
	ctx.shutdown();
	} catch (IOException e) {
	    System.exit(0);
	}
    }
}


