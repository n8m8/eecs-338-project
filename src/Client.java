import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Properties;
import java.util.Scanner;
import java.util.StringTokenizer;

public abstract class Client {
	static int port;
	static String hostname;
	
	private static StringTokenizer st;
	
	// Client constructor to set values from config
	public Client() {
		try {
		FileReader reader = new FileReader("config.properties");
		
		Properties p = new Properties();
		p.load(reader);
		
		port = Integer.parseInt(p.getProperty("port"));
		hostname = p.getProperty("host");
		System.out.println("[debug] Set init values to " + hostname + ":" + port);
		} catch (Exception e) {
			System.out.println("Couldn't read config file! Hardcoding to localhost:8080.");
			port = 8080;
			hostname = "localhost";
		}
	}
	
	// Sends data to the server; takes the string to send
	// Returns the raw response from the server
	public static String sendData(String args) {
		Socket s = null;
		OutputStream output = null;
		InputStream input = null;
		try {
			System.out.println("[debug] Opening socket for " + hostname + ":" + port);
			s = new Socket(hostname, port);
			
			output = s.getOutputStream();
			input = s.getInputStream();
			output.write(args.getBytes());
			
			byte[] response = input.readAllBytes();
			
			return new String(response);
			
		} catch (Exception e) {
			e.printStackTrace();
			return("error:failure");
			
		} finally {
			// Always try to close everything
			if (!s.isClosed()) {
				try {
					s.close();
				} catch (IOException e1) {}
			}
			
			try {
				output.close();
			} catch (Exception e1) {}
		
			try {
				input.close();
			} catch (Exception e1) {}
		}
	}
	
	// Tokenizes a string input
	// Returns each token in a string array
	public static String[] getToks(String input) {
		st = new StringTokenizer(input);
		ArrayList<String> list = new ArrayList<String>();
		while (st.hasMoreTokens()) {
			list.add(st.nextToken());
		}
		return list.toArray(new String[list.size()]);
	}
	
	// Helper method to ensure input is valid
	public static boolean checkNumArgs(String[] input, int numArgs) {
		if (input.length == numArgs)
			return true;
		else
			return false;
	}
}
