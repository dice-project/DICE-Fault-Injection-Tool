package dice.eu.fleximonkey;

import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import java.util.logging.Level;
import java.util.logging.Logger;

public class CommandLineParameters {
	private static final Logger log = Logger.getLogger( CommandLineParameters.class.getName() );


	public static void main(String[] args) {

		CommandLineParser parser = new BasicParser();
		Options options = new Options();
		Option vmShutdown = new Option("r", "randomVM", true,
				"Shutdown random VM within FCO.");
		// Set option r to take maximum of 4 arguments
		vmShutdown.setArgs(4);
		vmShutdown.setArgName("cloudusername, cloudpassword,cloudUUID, cloudapiurl");

		options.addOption(vmShutdown);
		
		Option vmStressCPU = new Option("s", "stresscpu", true,
				"Stress VM CPU");
		// Set option s to take maximum of 5 arguments
		vmStressCPU.setArgs(5);
		vmStressCPU.setArgName("cores, stresstime, host, vmpassword, sshkeypath");
		vmStressCPU.setOptionalArg(true);
		options.addOption(vmStressCPU);
		
		Option vmStressMem = new Option("m", "stressmem", true,"Stress VM Memory");
		// Set option m to take maximum of 5 arguments
		vmStressMem.setArgs(5);
		vmStressMem.setArgName("memorytesterloops,memeorytotal,host,vmpassword,sshkeypath");
		options.addOption(vmStressMem);
		
		Option blockfirewallOption = new Option("b", "blockfirewall", true, "Block external communication from Firewall");
		// Set option 2 to take maximum of 3 arguments
		blockfirewallOption.setArgs(3);
		blockfirewallOption.setArgName("host,vmpassword,sshkeypath");
		options.addOption(blockfirewallOption);
		
		Option stopserviceOption = new Option("k", "killservice", true, "Stop service running on VM");
		// Set maximum of 3 arguments
		stopserviceOption.setArgs(4);
		stopserviceOption.setArgName("host,vmpassword,service,sshkeypath");
		options.addOption(stopserviceOption);
		
		Option whitelistVMstopOption = new Option("w", "whiteliststop", true, "Stop VM from whitelist");
		// Set maximum of 5 arguments
		whitelistVMstopOption.setArgs(5);
		whitelistVMstopOption.setArgName("cloudusername, cloudpassword, cloudUUID, cloudapiurl, filepath");
		options.addOption(whitelistVMstopOption);
		
		options.addOption("f", "file", true, "Load from properties file");
		options.addOption("h", "help", false, "Shows help");

		try {
			CommandLine commandLine = parser.parse(options, args);
			 log.log( Level.INFO, "Looking for command line option");

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
				 log.log( Level.INFO, "Executing stop random VM");

			}

			if (commandLine.hasOption("f")) {
				ReadConfig readconfigfile = new ReadConfig();
				readconfigfile.readconfig();
				 log.log( Level.INFO, "Reading Config File");

			}
			if (commandLine.hasOption("b")) {
				String[] argument = commandLine.getOptionValues("b");
				String host = argument[0];
				String vmpassword = argument[1];
				String sshkeypath = argument[2];
				VMblockExternalTraffic blockfirewall = new VMblockExternalTraffic();
				blockfirewall.blockfirewall(host, vmpassword,sshkeypath);
				 log.log( Level.INFO, "Setting firewall");

			}
			if (commandLine.hasOption("s")) {
				String[] argument = commandLine.getOptionValues("s");
				String cores = argument[0];
				String stresstime = argument[1];
				String host = argument[2];
				String vmpassword = argument[3];
				String sshkeypath = argument[4];
				VMcpuStress cpustress = new VMcpuStress();
				cpustress.stresscpu(cores,stresstime,vmpassword,host,sshkeypath);
				log.log( Level.INFO, "Executing CPU stress on VM");

			}		

			if (commandLine.hasOption("m")) {
				String[] argument = commandLine.getOptionValues("m");
				String memorytesterloops = argument[0];
				String memeorytotal = argument[2];
				String host = argument[3];
				String vmpassword = argument[4];
				String sshkeypath = argument[5];
				VMmemoryStress vmmemstress = new VMmemoryStress();
				vmmemstress.stressmemory(host,vmpassword,memorytesterloops,memeorytotal,sshkeypath);
				 log.log( Level.INFO, "Executing Memory stress on VM");

			}
			if (commandLine.hasOption("k")) {
				String[] argument = commandLine.getOptionValues("k");
				String host = argument[0];
				String vmpassword = argument[1];
				String service = argument[2];
				String sshkeypath = argument[3];

				VMstopService vmstopservice = new VMstopService();
				vmstopservice.stopservice(host, vmpassword, service,sshkeypath);
				log.log( Level.INFO, "Executing Stop service on VM");

			}

			if (commandLine.hasOption("h")) {
				HelpFormatter formatter = new HelpFormatter();
				formatter.printHelp("CommandLineParameters", options);
				 log.log( Level.INFO, "Opening comandline help");

			}
			if (commandLine.hasOption("w")) {

				String[] argument = commandLine.getOptionValues("w");
				String cloudusername = argument[0];
				String cloudpassword = argument[1];
				String cloudUUID = argument[2];
				String cloudapiurl = argument[3];
				String filepath = argument[4];
				// Logger to be added
				WhiteListVMs whitelist = new WhiteListVMs();
				whitelist.whitelistvms(cloudusername, cloudpassword, cloudapiurl,cloudUUID, filepath);
				 log.log( Level.INFO, "Executing stop random VM from whitelist");

			}

		} catch (ParseException e) {
			 log.log( Level.SEVERE, "Unable to find selected option", e.toString());
			
		}

	}
}
