import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.StringTokenizer;

public abstract class Client {
	static int port = 8080;
	static String hostname = "localhost";
	
	private static StringTokenizer st;
	
	public static String sendData(String args) {
		try {
			Socket s = new Socket(hostname, port);
			
			OutputStream output = s.getOutputStream();
			InputStream input = s.getInputStream();
			output.write(args.getBytes());
			
			byte[] response = input.readAllBytes();
			
			return new String(response);
			
		} catch (Exception e) {
			e.printStackTrace();
			return("error:failure");
		}
		
	}
	
	public static String[] getToks(String input) {
		st = new StringTokenizer(input);
		ArrayList<String> list = new ArrayList<String>();
		while (st.hasMoreTokens()) {
			list.add(st.nextToken());
		}
		return list.toArray(new String[list.size()]);
	}
	
	public static boolean checkNumArgs(String[] input, int numArgs) {
		if (input.length == numArgs)
			return true;
		else
			return false;
	}
}
