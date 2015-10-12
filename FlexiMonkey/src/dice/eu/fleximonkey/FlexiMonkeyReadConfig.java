package dice.eu.fleximonkey;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FlexiMonkeyReadConfig {
	private static final Logger log = Logger.getLogger( FlexiMonkeyReadConfig.class.getName() );

	//Set required inputs by default
	String result = "";
	String action = "";
	String cores = "";
	String stresstime = "";
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
	InputStream inputStream;

	public String getPropValues() throws IOException {

		try {
			 log.log( Level.INFO, "Loading values from config.properties file");

			Properties prop = new Properties();
			String propFileName = "config.properties";

			inputStream = getClass().getClassLoader().getResourceAsStream(
					propFileName);

			if (inputStream != null) {
				prop.load(inputStream);
			} else {
				 log.log( Level.SEVERE, "Exception in reading proprties file ");

				throw new FileNotFoundException("property file '"
						+ propFileName + "' not found in the classpath");
			}
			 log.log( Level.INFO, "Getting values from config file");

			// get the property values from the config file
			action = prop.getProperty("action");
			cores = prop.getProperty("cores");
			stresstime = prop.getProperty("time");
			vmpassword = prop.getProperty("password");
			host = prop.getProperty("host");
			cloudapiurl = prop.getProperty("cloudapiurl");
			cloudusername = prop.getProperty("cloudusername");
			cloudpassword = prop.getProperty("cloudpassword");
			cloudUUID = prop.getProperty("cloudUUID");
			memorytesterloops = prop.getProperty("memorytesterloops");
			service = prop.getProperty(service);
			filepath = prop.getProperty(filepath);
			sshkeypath = prop.getProperty(sshkeypath);
		} catch (Exception e) {
			 log.log( Level.SEVERE, "Exception in reading proprties: " + e);
		} finally {
			//Close input
			inputStream.close();
		}
		//return the values from the file
		return result;
	}
}
