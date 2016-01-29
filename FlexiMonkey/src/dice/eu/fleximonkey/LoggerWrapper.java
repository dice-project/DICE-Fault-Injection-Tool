package dice.eu.fleximonkey;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class LoggerWrapper {
	  
public final static Logger myLogger = Logger.getLogger("Test");
 
private static LoggerWrapper instance = null;
    
   public static LoggerWrapper getInstance() throws SecurityException, IOException {
      if(instance == null) {
    	  prepareLogger();
         instance = new LoggerWrapper ();
      }
      return instance;
   }
  
private static void prepareLogger() throws SecurityException, IOException {
		SimpleDateFormat format = new SimpleDateFormat("M-d_HHmmss");
    	FileHandler myFileHandler = new FileHandler("log"+format.format(Calendar.getInstance().getTime()) + ".log");
        myFileHandler.setFormatter(new SimpleFormatter());
        myLogger.addHandler(myFileHandler);
        myLogger.setUseParentHandlers(false);
        myLogger.setLevel(Level.FINEST);
	} 
}
