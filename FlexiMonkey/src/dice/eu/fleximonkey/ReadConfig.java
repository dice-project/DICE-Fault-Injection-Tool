package dice.eu.fleximonkey;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ReadConfig {

	public void readconfig(){
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
		FlexiMonkeyReadConfig properties = new FlexiMonkeyReadConfig();
		try {
			properties.getPropValues();
		} catch (IOException e) {
			LoggerWrapper.myLogger.severe("Error getting file values " + e.toString());
		}
		
		VMState vmstate = VMState.valueOf(properties.action);
		
		//Switch case to all require actions
		switch (vmstate) {
		case stresscpu:
			//Start Stress execute code
			LoggerWrapper.myLogger.info("Executing CPU stress on VM");
			VMcpuStress cpustress = new VMcpuStress();
			cpustress.stresscpu(properties.cores, properties.time, properties.vmpassword, properties.host,properties.sshkeypath);
		break;	
		
        case stopVM:
        	FCOListVMs listvms = new FCOListVMs();
        	listvms.listvms(properties.cloudusername, properties.cloudpassword, properties.cloudapiurl, properties.cloudUUID);
			LoggerWrapper.myLogger.info("Executing Stop FCO VM");
        break;
        case stressmem:
			LoggerWrapper.myLogger.info("Executing Memory stress on VM");
			VMmemoryStress vmmemstress = new VMmemoryStress();
			vmmemstress.stressmemory(properties.host,properties.vmpassword, properties.memorytesterloops, properties.memeorytotal,properties.sshkeypath);	
        break;
        
        case blockfirewall:
			LoggerWrapper.myLogger.info("Executing blockfirwall on VM");
			VMblockExternalTraffic blockfirewall = new VMblockExternalTraffic();
			blockfirewall.blockfirewall(properties.host,properties.vmpassword, properties.sshkeypath);	
        break;
        case stopservice:
			LoggerWrapper.myLogger.info("Executing stop service on VM");
			VMstopService vmstopservice = new VMstopService();
			vmstopservice.stopservice(properties.host,properties.vmpassword, properties.service, properties.sshkeypath);	
        break; 
        
        case whitelistStop:
        	WhiteListVMs whitelist = new WhiteListVMs();
			whitelist.whitelistvms(properties.cloudusername, properties.cloudpassword, properties.cloudapiurl,properties.cloudUUID, properties.filepath);
			LoggerWrapper.myLogger.info("Executing Stop FCO VM from whitelist");
        break;
        
        case stressnetwork:
        	NetworkBandwidthStress networkstresstest = new NetworkBandwidthStress();
			networkstresstest.networkbandwidthstress(properties.host, properties.vmpassword, properties.iperfserver, properties.time, properties.sshkeypath);
			LoggerWrapper.myLogger.info("Executing Stop FCO VM from whitelist");
        break;
       
        default:
			LoggerWrapper.myLogger.severe("Error no option selected!!");
        break;	
	  }

	}
	
	public enum VMState{
		stresscpu,
		stressmem,
		stopVM,
		blockfirewall,
		stopservice,
		whitelistStop,
		stressnetwork
	}

}
