import java.io.FileReader;
import java.util.Properties;
import java.net.Socket;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Hashtable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Properties;
import java.util.StringTokenizer;

public abstract class Client {
	static int port;
	static String hostname;

	static StringTokenizer st;


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
			// System.out.println("[debug] Opening socket for " + hostname + ":" + port);
			s = new Socket(hostname, port);

			output = s.getOutputStream();
			input = s.getInputStream();
			// System.out.println("[debug] about to write:" + args);
			//output.write(args.getBytes());
			for (char c : args.toCharArray()) {
				output.write(c);
			}
			output.write(-1);

			// System.out.println("[debug] About to wait for a response");

			byte[] response = input.readAllBytes();

			String returnString = "";
			for (byte b : response) {
				returnString += (char) b;
			}

			// System.out.println("[debug] Received the response! " + returnString);
			if (returnString.length() == 0) {
				returnString = "9 ";	// 9 = did not receive response
			}
			return new String(returnString);

		} catch (Exception e) {
			e.printStackTrace();
			return("error:failure");

		} finally {
			// Always try to close everything
			try {
				output.close();
			} catch (Exception e1) {}
			try {
				input.close();
			} catch (Exception e1) {}
			if (!s.isClosed()) {
				try {
					s.close();
				} catch (IOException e1) {}
			}
		}
	}

	// Helper function for shell functionality, tokenizes a string input
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
