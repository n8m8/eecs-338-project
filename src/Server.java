import java.net.ServerSocket;
import java.net.Socket;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;


public class Server implements Runnable{
    protected int          serverPortVal   = 8080;
    protected ServerSocket serverSocketVal = null;
    protected boolean      hasStopped    = false;
    protected Thread       movingThread= null;

    public ServerMultithreaded(int port){
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
                new WrkrRunnable(
                    clntSocket, "This is a multithreaded Server")
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

public class WrkrRunnable implements Runnable{
    protected Socket clntSocket = null;
    protected String txtFrmSrvr   = null;
 
    public WrkrRunnable(Socket clntSocket, String txtFrmSrvr) {
        this.clntSocket = clntSocket;
        this.txtFrmSrvr   = txtFrmSrvr;
    }
    public void run() {
        try {
            InputStream inputstrm  = clntSocket.getInputStream();
            OutputStream outputstrm = clntSocket.getOutputStream();
            long timetaken = System.currentTimeMillis();
            outputstrm.write(("OK\n\nWrkrRunnable: " + this.txtFrmSrvr + " - " +timetaken +"").getBytes());
            outputstrm.close();
            inputstrm.close();
            System.out.println("Your request has processed in time : " + timetaken);
        } catch (IOException e) {           
            e.printStackTrace();
        }
    }
}


}
