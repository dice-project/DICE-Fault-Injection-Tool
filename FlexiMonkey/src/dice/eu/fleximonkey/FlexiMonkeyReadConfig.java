package dice.eu.fleximonkey;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class FlexiMonkeyReadConfig {
	//Set required inputs by default
	String result = "";
	String action = "";
	String cores = "";
	String time = "";
	String vmpassword = "";
	String host = "";
	String cloudapiurl = "";
	String cloudusername = "";
	String cloudpassword = "";
	String cloudUUID = "";
	String memorytesterloops = "";
	String memeorytotal = "";
	String service ="";
	String filepath="";
	String sshkeypath="";
	String iperfserver ="";
	String loops ="";


	public String getPropValues(String filepath) throws IOException {
		@SuppressWarnings("unused")
		LoggerWrapper loggerWrapper = null;
		try {
			loggerWrapper = LoggerWrapper.getInstance();
		} catch (SecurityException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			LoggerWrapper.myLogger.info("Loading values from config.properties file");
			Properties prop = new Properties();
			String propFileName = filepath;
			InputStream input = new FileInputStream(propFileName);
			
			prop.load(input);
		
			{
				prop.load(input);
				LoggerWrapper.myLogger.severe("Exception in reading proprties file ");

				throw new FileNotFoundException("property file '"
						+ propFileName + "' not found in the classpath");
			}
		} catch (Exception e) {
			LoggerWrapper.myLogger.severe("Exception in reading proprties: " + e);
		
		return result;
		}
	}
}
