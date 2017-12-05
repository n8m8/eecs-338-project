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
    protected static		int serverPortVal;
    protected				ServerSocket serverSocketVal = null;
    protected				boolean hasStopped = false;
    protected				Thread movingThread = null;
    private  static Hashtable<String,CustomerAccount> userPermissions = new Hashtable<String,CustomerAccount>();

    public static void main(String[] args) {
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

      System.out.println("[debug] Adding a base user named admin");
      userPermissions.put("admin", new CustomerAccount(2));
	userPermissions.get("admin").deposit(99999.9f);

    	System.out.println("[debug] Running the server.");
    	Server s = new Server();
    	Thread t = new Thread(s);
    	t.start();


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
            	System.out.println("[debug] Waiting for connection.");
                clntSocket = this.serverSocketVal.accept();
                System.out.println("[debug] Accepted connection!");
            } catch (IOException e) {
                if(hasStopped()) {
                    System.out.println("Server has Stopped...Please check") ;
                    return;
                }
                throw new RuntimeException(
                    "Client cannot be connected - Error", e);
            }
            System.out.println("[debug] Starting ClientConnection thread.");
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
    	System.out.println("[debug] ClientConnection thread started.");
    	InputStream in = null;
    	OutputStream outputstrm = null;
      String response = "";

        try {
        	in = clntSocket.getInputStream();
          outputstrm = clntSocket.getOutputStream();

          long timetaken = System.currentTimeMillis();
            // outputstrm.write(txtFrmSrvr.getBytes());

            // BufferedReader bis = new BufferedReader(clntSocket.getInputStream());



 			// ArrayList<String> request = new ArrayList<String>();
  			// while ((inputLine = bis.readLine()) != null) {
     		// 	 request.add(inputLine);
  			// }

 			//byte[] req = in.readAllBytes();

        	byte[] temp = new byte[32];
            //ArrayList<Integer> req = new ArrayList<Integer>();
          int read;
          String reqString = "";
          while ((read = in.read()) != 255) {
            	//req.add(read);
            	reqString += (char) read;
            	//System.out.println(reqString);
             // System.out.println(read);
          }


 			//String reqString = new String(temp);
 			// tricky line; splits the raw request string; Arrays.asList makes it an arrayList; This goes into the ArrayList constructor
 			    ArrayList<String> request = new ArrayList<String>(Arrays.asList(reqString.split("\n")));

 			    for (String r : request) {
 				   System.out.println("[debug] Read one header as " + r);
 			    }



           //processing request
         //get permissions for user requesting
          if (userPermissions.containsKey(userRequesting = request.get(0))) {
          permissions = userPermissions.get(userRequesting).getPermissions();
	 try{ userName = request.get(2);}catch(Exception e) { userName = "None Submitted";}

          methodName = request.get(1);
	  if (methodName.equals("login")){
		response = "0 ";
          }
	
	  //determine if user is real, to be added, or erroneous
          else if (methodName.equals("createUser")) {
	     
            if(userPermissions.containsKey(userName)) {
              response = "6 ";
            }
            else{
		if(permissions == 2){
              		userPermissions.put(userName, new CustomerAccount(Integer.parseInt(request.get(3))));
              		response = "0 ";
		}
		else
			response = "1 ";
            }
          }
          //user operation is requested for exists
          else if (userPermissions.containsKey(userName= request.get(2))) {
            userAccount = userPermissions.get(userName);

            //get balance event
            if (methodName.equals("getBalance")) {
              if(userName.equals(userRequesting)|| permissions==0 || permissions==2){
                // Java doesn't let you easily turn floats into bytes
                // So let's turn it into a string, then a byte[]
                response = "0 " +userPermissions.get(userName).getBalance().toString();
              }
              else{
                response ="1 ";
              }
            }

            /*
            *make payment
            *checks if user is user requesting or if user requesting has admin rights
            */

            else if (methodName.equals("makePayment")&&(userName.equals(userRequesting) || permissions== 2)) {
              Float transactionAmount= Float.parseFloat(request.get(3));
              //if second user exists
              if (userPermissions.containsKey(request.get(4))) {
                CustomerAccount user2 = userPermissions.get(request.get(4));
                //check if user has funds available
                if (userAccount.getBalance() >= transactionAmount) {
                  user2.deposit(transactionAmount);
                  userAccount.withdraw(transactionAmount);
                  response ="0 "+userPermissions.get(userName).getBalance().toString();
                }
                //insufficient funds
                else {
                  response = "3 ";
                }
              }
              //second user does not exist
              else {
                response = "4 ";
              }
            }

            /*
            * withdrawl method
            *checks if user requesting is user
            *if not checks that user requesting is an atm or admin
            */
            else if (methodName.equals("withdrawl") && (userName.equals(userRequesting) || permissions == 0 || permissions == 2)) {
              Float transactionAmount= Float.parseFloat(request.get(3));
              CustomerAccount user2 = userPermissions.get(request.get(4));
              //checks if user balance is over requested amount
              if (userAccount.getBalance() >= transactionAmount){
                  user2.deposit(transactionAmount);
                  userAccount.withdraw(transactionAmount);
                  response = "0 "+userPermissions.get(userName).getBalance().toString();
                }
                //insufficient funds
                else
                  outputstrm.write("3 ".getBytes());
            }
            //deposit
            else if (methodName.equals("deposit")) {
              Float transactionAmount= Float.parseFloat(request.get(3));
              userAccount.deposit(transactionAmount);
              response = "0 "+userPermissions.get(userName).getBalance().toString();

            }
            //methodName not found
            else{
              outputstrm.write("5 ".getBytes());

            }

          }
          //requesting user not found
          else {
            response = "2 ";
          }
        }
        outputstrm.write(response.getBytes());






        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
				        outputstrm.close();
			      } catch (Exception e) {}
            try {
				        in.close();
			      } catch (Exception e) {}
        	// Always close everything
        	  try {
        		    clntSocket.close();
        	} catch (Exception e) {}
        }


      }
    }
  }
