package dice.eu.fleximonkey;

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
	InputStream inputStream;

	public String getPropValues() throws IOException {
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
			String propFileName = "config.properties";

			inputStream = getClass().getClassLoader().getResourceAsStream(
					propFileName);

			if (inputStream != null) {
				prop.load(inputStream);
			} else {
				LoggerWrapper.myLogger.severe("Exception in reading proprties file ");

				throw new FileNotFoundException("property file '"
						+ propFileName + "' not found in the classpath");
			}
			LoggerWrapper.myLogger.info("Getting values from config file");

			// get the property values from the config file
			action = prop.getProperty("action");
			cores = prop.getProperty("cores");
			time = prop.getProperty("time");
			vmpassword = prop.getProperty("password");
			host = prop.getProperty("host");
			cloudapiurl = prop.getProperty("cloudapiurl");
			cloudusername = prop.getProperty("cloudusername");
			cloudpassword = prop.getProperty("cloudpassword");
			cloudUUID = prop.getProperty("cloudUUID");
			memorytesterloops = prop.getProperty("memorytesterloops");
			service = prop.getProperty("service");
			filepath = prop.getProperty("filepath");
			sshkeypath = prop.getProperty("sshkeypath");
			loops = prop.getProperty("loops");
		} catch (Exception e) {
			LoggerWrapper.myLogger.severe("Exception in reading proprties: " + e);
		} finally {
			//Close input
			inputStream.close();
		}
		//return the values from the file
		return result;
	}
}
