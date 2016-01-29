package dice.eu.fleximonkey;

import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import java.io.IOException;

@SuppressWarnings("deprecation")
public class CommandLineParameters {

	public static void main(String[] args) {
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

		
		//new command line parser. Used to generate command line options
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
		
		Option networkstress = new Option("n", "stressnetwork", true, "Stress network on VM");
		// Set maximum of 5 arguments
		networkstress.setArgs(5);
		networkstress.setArgName("host,vmpassword,iperfserver,time,sshkeypath");
		options.addOption(networkstress);
		
		Option diskstress = new Option("d", "diskstress", true, "Stress Disk on VM");
		// Set maximum of 5 arguments
		diskstress.setArgs(5);
		diskstress.setArgName("host,vmpassword,memeorytotal,loops,sshkeypath");
		options.addOption(diskstress);
		
		//test loading
		//options.addOption("f", "file", true, "Load from properties file");
		Option loadfile = new Option("f", "file", true, "Load from properties file");
		// Set maximum of 1 arguments
		loadfile.setArgs(1);
		loadfile.setArgName("filepath");
		options.addOption(loadfile);
		
		options.addOption("h", "help", false, "Shows help");

		//Checks what user has entered as first parameter and checks available options
		try {
			CommandLine commandLine = parser.parse(options, args);
			LoggerWrapper.myLogger.info("Looking for command line option");

			if (commandLine.hasOption("r")) {
				//Executes random Stop VM on FCO platform
				String[] argument = commandLine.getOptionValues("r");
				String cloudusername = argument[0];
				String cloudpassword = argument[1];
				String cloudUUID = argument[2];
				String cloudapiurl = argument[3];
				FCOListVMs listvms = new FCOListVMs();
				listvms.listvms(cloudusername, cloudpassword, cloudapiurl,
						cloudUUID);
				LoggerWrapper.myLogger.info( "Executing stop random VM on FCO platform");

			}

			if (commandLine.hasOption("f")) {
				String[] argument = commandLine.getOptionValues("f");
				String filepath = argument[0];
				//Reads config file for options
				ReadConfig readconfigfile = new ReadConfig();
				readconfigfile.readconfig(filepath);
				LoggerWrapper.myLogger.info( "Reading config file");


			}
			if (commandLine.hasOption("b")) {
				//Blocks external connection on VM
				String[] argument = commandLine.getOptionValues("b");
				String host = argument[0];
				String vmpassword = argument[1];
				String sshkeypath = argument[2];
				VMblockExternalTraffic blockfirewall = new VMblockExternalTraffic();
				blockfirewall.blockfirewall(host, vmpassword,sshkeypath);
				LoggerWrapper.myLogger.info( "Setting Firewall on VM");


			}
			if (commandLine.hasOption("s")) {
				//Executes high CPU usage on VM
				String[] argument = commandLine.getOptionValues("s");
				String cores = argument[0];
				String stresstime = argument[1];
				String host = argument[2];
				String vmpassword = argument[3];
				String sshkeypath = argument[4];
				VMcpuStress cpustress = new VMcpuStress();
				cpustress.stresscpu(cores,stresstime,vmpassword,host,sshkeypath);
				LoggerWrapper.myLogger.info( "Executing CPU stress on VM");

			}		

			if (commandLine.hasOption("m")) {
				//Causes high Memory usage on VM
				String[] argument = commandLine.getOptionValues("m");
				String memorytesterloops = argument[0];
				String memeorytotal = argument[1];
				String host = argument[2];
				String vmpassword = argument[3];
				String sshkeypath = argument[4];
				VMmemoryStress vmmemstress = new VMmemoryStress();
				vmmemstress.stressmemory(host,vmpassword,memorytesterloops,memeorytotal,sshkeypath);
				LoggerWrapper.myLogger.info( "Executing Memory stress on VM");

			}
			if (commandLine.hasOption("k")) {
				//Kills service on VM
				String[] argument = commandLine.getOptionValues("k");
				String host = argument[0];
				String vmpassword = argument[1];
				String service = argument[2];
				String sshkeypath = argument[3];
				VMstopService vmstopservice = new VMstopService();
				vmstopservice.stopservice(host, vmpassword, service,sshkeypath);
				LoggerWrapper.myLogger.info( "Executing Stop service on VM" );

			}

			if (commandLine.hasOption("h")) {
				//Displays help from command line
				HelpFormatter formatter = new HelpFormatter();
				formatter.printHelp("CommandLineParameters", options);
				LoggerWrapper.myLogger.info( "Opening commandline help" );

			}
			if (commandLine.hasOption("w")) {
				//Whitelist VM stop
				String[] argument = commandLine.getOptionValues("w");
				String cloudusername = argument[0];
				String cloudpassword = argument[1];
				String cloudUUID = argument[2];
				String cloudapiurl = argument[3];
				String filepath = argument[4];
				WhiteListVMs whitelist = new WhiteListVMs();
				whitelist.whitelistvms(cloudusername, cloudpassword, cloudapiurl,cloudUUID, filepath);
				LoggerWrapper.myLogger.info( "Executing stop random VM from whitelist" );

			}

			if (commandLine.hasOption("n")) {
				//Causes high bandwith useage on VM
				String[] argument = commandLine.getOptionValues("n");
				String host = argument[0];
				String vmpassword = argument[1];
				String iperfserver = argument[2];
				String time = argument[3];
				String sshkeypath = argument[4];
				NetworkBandwidthStress networkstresstest = new NetworkBandwidthStress();
				networkstresstest.networkbandwidthstress(host, vmpassword, iperfserver, time, sshkeypath);
				LoggerWrapper.myLogger.info( "Executing High bandwith usage on VM" );

			}
			
			if (commandLine.hasOption("d")) {
				//Causes high disk useage on VM
				String[] argument = commandLine.getOptionValues("d");
				String host = argument[0];
				String vmpassword = argument[1];
				String memeorytotal = argument[2];
				String loops = argument[3];
				String sshkeypath = argument[4];
				VMDiskStress diskstresstest = new VMDiskStress();
				diskstresstest.stressdisk(host, vmpassword, memeorytotal, loops, sshkeypath);
				LoggerWrapper.myLogger.info( "Executing High bandwith usage on VM" );

			}

		} catch (ParseException e) {
			LoggerWrapper.myLogger.severe( "Unable to find selected option " + e.toString());

		}

	}
}
