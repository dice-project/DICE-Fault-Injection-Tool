package dice.eu.fleximonkey;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class WhiteListVMs {
	private static final Logger log = Logger.getLogger( WhiteListVMs.class.getName() );

	public void whitelistvms(String cloudusername, String cloudpassword,
			String cloudapiurl, String cloudUUID, String filepath) {
	Scanner s = null;
	Random randomGenerator;
	randomGenerator = new Random();
	try {
		s = new Scanner(new File(filepath));
	} catch (FileNotFoundException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	ArrayList<String> list = new ArrayList<String>();
	while (s.hasNextLine()){
		list.add(s.next());
	}
	s.close();
	
	int index = randomGenerator.nextInt(list.size());
	String vmuuid = list.get(index);
	 log.log( Level.INFO, "Server random from provided list " + vmuuid);

	FCOstopVM stopvm = new FCOstopVM();
	stopvm.stopvm(vmuuid, cloudusername, cloudpassword, cloudapiurl,
			cloudUUID);
	}
}
