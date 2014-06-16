import org.xerial.snappy.Snappy;
import java.io.*;

public class Sample {
    public static void main(String[] args) {
	try { 
	    try {
		String input = "Hello snappy-java! Snappy-java is a JNI-based wrapper of "
		    + "Snappy, a fast compresser/decompresser.";
		byte[] compressed = Snappy.compress(input.getBytes("UTF-8"));
		byte[] uncompressed = Snappy.uncompress(compressed);
		
		FileOutputStream outOld = new FileOutputStream("Traw_depth.txt");
		outOld.write(input.getBytes("UTF-8"));
		outOld.close();
		FileOutputStream outComp = new FileOutputStream("compressed_depth.txt");
		outComp.write(compressed);
		outComp.close();

		String result = new String(uncompressed, "UTF-8");
		System.out.println(result);
	    } catch(UnsupportedEncodingException e) {
		System.exit(0);
	    } 
	} catch(IOException e) {
	    System.exit(0);
	} 
    }
}
