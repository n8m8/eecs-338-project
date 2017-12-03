import java.net.ServerSocket;
import java.net.Socket;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Hashtable;
import java.util.ArrayList;
import java.util.Enumeration;



public class Server implements Runnable{
    protected int          serverPortVal   = 8080;
    protected ServerSocket serverSocketVal = null;
    protected boolean      hasStopped    = false;
    protected Thread       movingThread= null;
    private Hashtable<String,CustomerAccount> userPermissions = new Hashtable<String,CustomerAccount>();

    public void ServerMultithreaded(int port){
        this.serverPortVal = port;
    }

    public void run(){
        synchronized(this){
            this.movingThread = Thread.currentThread();
        }
        opnSvrSocket();
        while(! hasStopped()){
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
            new Thread(
                new ClientConnection(
                    clntSocket, "Please return user ID:")
            ).start();
        }
        System.out.println("Server has Stopped...Please check") ;
    }
    private synchronized boolean hasStopped() {
        return this.hasStopped;
    }
    public synchronized void stop(){
        this.hasStopped = true;
        try {
            this.serverSocketVal.close();
        } catch (IOException e) {
            throw new RuntimeException("Server can not be closed - Please check error", e);
        }
    }
    private void opnSvrSocket() {
        try {
            this.serverSocketVal = new serverSocketVal(this.serverPortVal);
        } catch (IOException e) {
            throw new RuntimeException("Not able to open the port 8080", e);
        }
    }

public class ClientConnection implements Runnable{
    protected Socket clntSocket = null;
    protected String txtFrmSrvr   = null;
 	protected int permissions;
 	protected String methodName, userRequesting, destination, userName;
 	protected Float amount;
 	protected CustomerAccount userAccount;

    public ClientConnection(Socket clntSocket, String txtFrmSrvr) {
        this.clntSocket = clntSocket;
        this.txtFrmSrvr   = txtFrmSrvr;
    }
    public void run() {
        try {
            OutputStream outputstrm = clntSocket.getOutputStream();
            long timetaken = System.currentTimeMillis();
            outputstrm.write(txtFrmSrvr.getBytes());

            BufferedReader bis = new BufferedReader(clntSocket.getInputStream());
 			ArrayList<String> request = new ArrayList<String>;
  			while ((inputLine = bis.readLine()) != null)
  			{
     			 request.add(inputLine) 
  			}
  			//processing request
  			if(userPermissions.containsKey(userRequesting=request.get(0))){
  				permissions = userPermissions.get(user);

  				methodName = request.get(1);
  				//determine if user is real, to be added, or erroneous
  				if(methodName.equals("createUser")){
  					if(userPermissions.containsKey(userName)){
  						outputstrm.write("Sorry, user already exists".getBytes());
  					}
  					else{
  						userPermissions.add(userName, new CustomerAccount(request.get(4), request.get(3))
  						outputstrm.write("Success".getBytes());
  					}
  				}
  				else if(userPermissions.containsKey(userName= request.get(2))){
  					userAccount = userPermissions.get(userName);
  					//get balance event
  					if (methodName.equals("getBalance"){
  						if(userName.equals(userRequesting)|| permissions==0 || permissions==2){
  							outputstrm.write(userPermission.get(userName).getBalance.getBytes());
  						}
  						else{
  							outputstrm.write("permission denied".getBytes());
  						}
  					}
  					//make payment event
  					else if(methodName.equals("makePayment")&&(userName.equals(userRequesting) || permissions== 2)){
  						Float transactionAmount= Float.parseFloat(request.get(3));
  						if(userPermissions.containsKey(request.get(4))){
  							CustomerAccount user2 = userPermissions.get(reqest.get(4));
  							//check if user has funds available
  							if(userAccount.getBalance>= transactionAmount){
  								user2.deposit(transactionAmount);
  								userAccount.withdraw(transactionAmount);
  								outputstrm.write("Success".getBytes());
  							}
  							else
  								outputstrm.write("insufficient funds".getBytes());
  						}
  						else
  							outputstrm.write("second user does not exist".getBytes());
  					}
  					// withdrawl
  					else if(methodName.equals("withdrawl") && (userName.equals(userRequesting)|| permissions==0 || permissions== 2)){
  						Float transactionAmount= Float.parseFloat(request.get(3))
  						if(userAccount.getBalance>= transactionAmount){
  								user2.deposit(transactionAmount);
  								userAccount.withdraw(transactionAmount);
  								outputstrm.write("Success".getBytes());
  							}
  							else
  								outputstrm.write("insufficient funds".getBytes());
  					}
  					//deposit
  					else if(methodName.equals("makePayment")){
  						Float transactionAmount= Float.parseFloat(request.get(3));
  						userAccount.depost(transactionAmount);
  					}
  					
  				}
  				
  				else
  					outputstrm.write("Sorry invalid userName".getBytes());

  			}

  			else
  				outputstrm.write("Sorry invalid request name".getBytes());
            outputstrm.close();
            inputstrm.close();
            System.out.println("Your request has processed in time : " + timetaken);
        } catch (IOException e) {           
            e.printStackTrace();
        }
    }
}


}
