package dice.eu.fleximonkey;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class FlexiMonkeyReadConfig {
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
	InputStream inputStream;

	public String getPropValues() throws IOException {

		try {
			Properties prop = new Properties();
			String propFileName = "config.properties";

			inputStream = getClass().getClassLoader().getResourceAsStream(
					propFileName);

			if (inputStream != null) {
				prop.load(inputStream);
			} else {
				throw new FileNotFoundException("property file '"
						+ propFileName + "' not found in the classpath");
			}

			// get the property values
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

		} catch (Exception e) {
			System.out.println("Exception: " + e);
		} finally {
			inputStream.close();
		}
		return result;
	}
}
