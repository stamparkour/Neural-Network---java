package NeuralNetwork;

import java.util.HexFormat;

public class HexReader {
	public static String bytesToHex(byte[] bytes) {
	    return HexFormat.of().formatHex(bytes);
	}
	
	public static byte[] hexToBytes(String s) {
		return HexFormat.of().parseHex(s);
	}
}
