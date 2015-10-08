package dice.eu.fleximonkey;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.io.FileUtils;

public class WhiteListVMs {
	private static final Logger log = Logger.getLogger( WhiteListVMs.class.getName() );

	public void whitelistvms(String cloudusername, String cloudpassword,
			String cloudapiurl, String cloudUUID, String filepath) {
	Random randomGenerator;
	randomGenerator = new Random();
	File file = new File(filepath);
	List<String> list = null;
	try {
		list = FileUtils.readLines(file);
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
		for (String line : list) {
	  System.out.println(line);  
	}
	int index = randomGenerator.nextInt(list.size());
	String vmuuid = list.get(index);
	 log.log( Level.INFO, "Server random from provided list " + vmuuid);

	FCOstopVM stopvm = new FCOstopVM();
	stopvm.stopvm(vmuuid, cloudusername, cloudpassword, cloudapiurl,
			cloudUUID);
	}
}
