import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public abstract class Client {
	int port = 8080;
	String hostname = "localhost";
	public String sendData(String args) {
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
}
