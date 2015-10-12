package dice.eu.fleximonkey;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.io.FileUtils;

public class WhiteListVMs {
	private static final Logger log = Logger.getLogger(WhiteListVMs.class
			.getName());

	public void whitelistvms(String cloudusername, String cloudpassword,
			String cloudapiurl, String cloudUUID, String filepath) {
		log.log(Level.INFO, "Starting Whitelist");

		// New Random Generator used for random selection of VM UUID from provided list
		Random randomGenerator;
		randomGenerator = new Random();
		// Takes provided file path and attempts to read file and place each line into a list
		File file = new File(filepath);
		List<String> list = null;
		try {
			list = FileUtils.readLines(file);
		} catch (IOException e) {
			log.log(Level.SEVERE,
					"Unable to read file from proivded path, Error: " + e);
		}
		//Selects random from proivded list and passes to Stop command within FCO
		int index = randomGenerator.nextInt(list.size());
		String vmuuid = list.get(index);
		log.log(Level.INFO, "Server random from provided list " + vmuuid);

		FCOstopVM stopvm = new FCOstopVM();
		stopvm.stopvm(vmuuid, cloudusername, cloudpassword, cloudapiurl,
				cloudUUID);
	}
}
