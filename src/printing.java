import java.io.PrintWriter;
import java.io.File;
import java.io.IOException;
import java.util.Date;

public class printing {

  public static void main(String[] args) throws IOException {
    Date date = new Date();
    File file = new File (date.toString() + ".txt");
    PrintWriter printWriter = new PrintWriter (file);
    printWriter.println ("hello this is a test");
    printWriter.close ();       
  }
}
