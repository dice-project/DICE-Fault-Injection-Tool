package dice.eu.fleximonkey;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Random;

import org.apache.commons.io.FileUtils;

public class WhiteListVMs {
	public void whitelistvms(String cloudusername, String cloudpassword,
			String cloudapiurl, String cloudUUID, String filepath) {
		
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
		 LoggerWrapper.myLogger.info( "Starting Whitelist" );
 
		// New Random Generator used for random selection of VM UUID from provided list
		Random randomGenerator;
		randomGenerator = new Random();
		// Takes provided file path and attempts to read file and place each line into a list
		File file = new File(filepath);
		List<String> list = null;
		try {
			list = FileUtils.readLines(file);
		} catch (IOException e) {
			 LoggerWrapper.myLogger.severe(" Unable to read file from proivded path, Error: " + e );

		}
		//Selects random from proivded list and passes to Stop command within FCO
		int index = randomGenerator.nextInt(list.size());
		String vmuuid = list.get(index);
		 LoggerWrapper.myLogger.info("Server random from provided list " + vmuuid);

		FCOstopVM stopvm = new FCOstopVM();
		stopvm.stopvm(vmuuid, cloudusername, cloudpassword, cloudapiurl,
				cloudUUID);
	}
}
