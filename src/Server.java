import java.net.ServerSocket;
import java.net.Socket;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Hashtable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.Properties;

public class Server implements Runnable {
    protected				int serverPortVal;
    protected				ServerSocket serverSocketVal = null;
    protected				boolean hasStopped = false;
    protected				Thread movingThread = null;
    private Hashtable<String,CustomerAccount> userPermissions = new Hashtable<String,CustomerAccount>();

    public void main(String[] args) {
    	
    	// Read properties for stuff like port and host
    	try {
			FileReader r = new FileReader("config.properties");
			
			Properties p = new Properties();
			p.load(r);
			
			serverPortVal = Integer.parseInt(p.getProperty("port"));
		} catch (Exception e) {
			System.out.println("Couldn't read config file. Setting port to 8080.");
			serverPortVal = 8080;
			e.printStackTrace();
		}
    }
    
    public void ServerMultithreaded(int port) {
        this.serverPortVal = port;
    }

    public void run() {
        synchronized(this) {
            this.movingThread = Thread.currentThread();
        }
        
        opnSvrSocket();
        
        while(!hasStopped()) {
            Socket clntSocket = null;
            try {
                clntSocket = this.serverSocketVal.accept();
            } catch (IOException e) {
                if(hasStopped()) {
                    System.out.println("Server has Stopped...Please check") ;
                    return;
                }
                throw new RuntimeException(
                    "Client cannot be connected - Error", e);
            }
            new Thread(new ClientConnection(
                       clntSocket, "Please return user ID:")
                      ).start();
        }
        
        System.out.println("Server has Stopped...Please check") ;
    }
    
    private synchronized boolean hasStopped() {
        return this.hasStopped;
    }
    
    public synchronized void stop() {
        this.hasStopped = true;
        try {
            this.serverSocketVal.close();
        } catch (IOException e) {
            throw new RuntimeException("Server can not be closed - Please check error", e);
        }
    }
    
    private void opnSvrSocket() {
        try {
            this.serverSocketVal = new ServerSocket(this.serverPortVal);
        } catch (IOException e) {
            throw new RuntimeException("Not able to open the port 8080", e);
        }
    }

public class ClientConnection implements Runnable {
    protected Socket clntSocket = null;
    protected String txtFrmSrvr = null;
 	protected int permissions;
 	protected String methodName, userRequesting, destination, userName;
 	protected Float amount;
 	protected CustomerAccount userAccount;

    public ClientConnection(Socket clntSocket, String txtFrmSrvr) {
        this.clntSocket = clntSocket;
        this.txtFrmSrvr = txtFrmSrvr;
    }
    
    public void run() {
        try {
        	InputStream in = clntSocket.getInputStream();
            OutputStream outputstrm = clntSocket.getOutputStream();
            
            long timetaken = System.currentTimeMillis();
            outputstrm.write(txtFrmSrvr.getBytes());

            // BufferedReader bis = new BufferedReader(clntSocket.getInputStream());
            
            
            
 			// ArrayList<String> request = new ArrayList<String>();
  			// while ((inputLine = bis.readLine()) != null) {
     		// 	 request.add(inputLine);
  			// }
  			
 			byte[] req = in.readAllBytes();
 			
 			String reqString = new String(req);
 			
 			// tricky line; splits the raw request string; Arrays.asList makes it an arrayList; This goes into the ArrayList constructor
 			ArrayList<String> request = new ArrayList<String>(Arrays.asList(reqString.split("\n")));
 			
  			//processing request
  			if (userPermissions.containsKey(userRequesting=request.get(0))) {
  				permissions = userPermissions.get(user);

  				methodName = request.get(1);
  				//determine if user is real, to be added, or erroneous
  				if (methodName.equals("createUser")) {
  					if(userPermissions.containsKey(userName)) {
  						outputstrm.write("Sorry, user already exists".getBytes());
  					}
  					else{
  						userPermissions.put(userName, new CustomerAccount(Integer.parseInt(request.get(4)), Integer.parseInt(request.get(3))));
  						outputstrm.write("Success".getBytes());
  					}
  				} else if (userPermissions.containsKey(userName= request.get(2))) {
  					userAccount = userPermissions.get(userName);
  					//get balance event
  					if (methodName.equals("getBalance")) {
  						if(userName.equals(userRequesting)|| permissions==0 || permissions==2){
  							// Java doesn't let you easily turn floats into bytes
  							// So let's turn it into a string, then a byte[]
  							outputstrm.write(userPermissions.get(userName).getBalance().toString().getBytes());
  						} else{
  							outputstrm.write("permission denied".getBytes());
  						}
  					}
  					
  					//make payment event
  					else if (methodName.equals("makePayment")&&(userName.equals(userRequesting) || permissions== 2)) {
  						Float transactionAmount= Float.parseFloat(request.get(3));
  						if (userPermissions.containsKey(request.get(4))) {
  							CustomerAccount user2 = userPermissions.get(request.get(4));
  							//check if user has funds available
  							if (userAccount.getBalance() >= transactionAmount) {
  								user2.deposit(transactionAmount);
  								userAccount.withdraw(transactionAmount);
  								outputstrm.write("Success".getBytes());
  							} else {
  								outputstrm.write("insufficient funds".getBytes());
  							}
  						} else {
  							outputstrm.write("second user does not exist".getBytes());
  						}
  					}
  					
  					// withdrawl
  					else if (methodName.equals("withdrawl") && (userName.equals(userRequesting) || permissions == 0 || permissions == 2)) {
  						Float transactionAmount= Float.parseFloat(request.get(3));
  						if (userAccount.getBalance() >= transactionAmount){
  								user2.deposit(transactionAmount);
  								userAccount.withdraw(transactionAmount);
  								outputstrm.write("Success".getBytes());
  							}
  							else
  								outputstrm.write("insufficient funds".getBytes());
  					}
  					//deposit
  					else if (methodName.equals("makePayment")) {
  						Float transactionAmount= Float.parseFloat(request.get(3));
  						userAccount.deposit(transactionAmount);
  					}
  					
  				} else {
  					outputstrm.write("Sorry invalid userName".getBytes());
  				}
  			} else {
  				outputstrm.write("Sorry invalid request name".getBytes());
  			}
  			
            outputstrm.close();
            in.close();
            System.out.println("Your request has processed in time : " + timetaken);
        } catch (IOException e) {           
            e.printStackTrace();
        }
    }
}


}
