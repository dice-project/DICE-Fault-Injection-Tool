package dice.eu.fleximonkey;

import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

public class CommandLineParameters {
	public static void main(String[] args) {

		CommandLineParser parser = new BasicParser();
		Options options = new Options();
		Option vmShutdown = new Option("r", "randomVM", true,
				"Shutdown random VM within FCO.");
		// Set option r to take maximum of 4 arguments
		vmShutdown.setArgs(4);
		vmShutdown
				.setArgName("cloudusername, cloudpassword, cloudUUID, cloudapiurl");
		options.addOption(vmShutdown);
		options.addOption("b", "bcd", true, "Second Parameter");
		options.addOption("f", "file", true, "Load from properties file");
		options.addOption("h", "help", false, "Shows help");

		try {
			CommandLine commandLine = parser.parse(options, args);

			if (commandLine.hasOption("r")) {

				String[] argument = commandLine.getOptionValues("r");
				String cloudusername = argument[0];
				String cloudpassword = argument[1];
				String cloudUUID = argument[2];
				String cloudapiurl = argument[3];
				// Logger to be added
				FCOListVMs listvms = new FCOListVMs();
				listvms.listvms(cloudusername, cloudpassword, cloudapiurl,
						cloudUUID);
				System.out.println("Executing stop random VM");

			}

			if (commandLine.hasOption("b")) {
				String argument = (String) commandLine
						.getParsedOptionValue("b");
				System.out.println(argument);
			}

			if (commandLine.hasOption("h")) {
				HelpFormatter formatter = new HelpFormatter();
				formatter.printHelp("CommandLineParameters", options);
			}

		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
