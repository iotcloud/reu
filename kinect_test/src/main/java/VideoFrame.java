import org.openkinect.freenect.FrameMode;

import java.nio.ByteBuffer;

public class VideoFrame {
    public FrameMode mode;

    public ByteBuffer frame;

    public int timeStamp;

    public VideoFrame(FrameMode mode, ByteBuffer frame, int timeStamp) {
        this.mode = mode;
        this.frame = frame;
        this.timeStamp = timeStamp;
    }
}
