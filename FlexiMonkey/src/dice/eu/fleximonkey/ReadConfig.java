package dice.eu.fleximonkey;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ReadConfig {
	private static final Logger log = Logger.getLogger( ReadConfig.class.getName() );

	public void readconfig(){
		FlexiMonkeyReadConfig properties = new FlexiMonkeyReadConfig();
		try {
			properties.getPropValues();
		} catch (IOException e) {
			 log.log( Level.SEVERE, "Error getting file values", e.toString());
		}
		
		VMState vmstate = VMState.valueOf(properties.action);
		
		//Switch case to all require actions
		switch (vmstate) {
		case stresscpu:
			//Start Stress execute code
			log.log( Level.INFO, "Executing CPU stress on VM");
			VMcpuStress cpustress = new VMcpuStress();
			cpustress.stresscpu(properties.cores, properties.stresstime, properties.vmpassword, properties.host);
		break;	
		
        case stopVM:
        	FCOListVMs listvms = new FCOListVMs();
        	listvms.listvms(properties.cloudusername, properties.cloudpassword, properties.cloudapiurl, properties.cloudUUID);
			log.log( Level.INFO, "Executing Stop FCO VM");
        break;
        case stressmem:
			log.log( Level.INFO, "Executing Memory stress on VM");
			VMmemoryStress vmmemstress = new VMmemoryStress();
			vmmemstress.stressmemory(properties.host,properties.vmpassword, properties.memorytesterloops, properties.memeorytotal);	
        break;
        
        case blockfirewall:
			log.log( Level.INFO, "Executing blockfirwall on VM");
			VMblockExternalTraffic blockfirewall = new VMblockExternalTraffic();
			blockfirewall.blockfirewall(properties.host,properties.vmpassword);	
        break;
        case stopservice:
			log.log( Level.INFO, "Executing stop service on VM");
			VMstopService vmstopservice = new VMstopService();
			vmstopservice.stopservice(properties.host,properties.vmpassword, properties.service);	
        break; 
        
        case whitelistStop:
        	WhiteListVMs whitelist = new WhiteListVMs();
			whitelist.whitelistvms(properties.cloudusername, properties.cloudpassword, properties.cloudapiurl,properties.cloudUUID, properties.filepath);
			log.log( Level.INFO, "Executing Stop FCO VM from whitelist");
        break;
   
       
        default:
			 log.log( Level.SEVERE, "Error no option selected!!");
        break;	
	  }

	}
	
	public enum VMState{
		stresscpu,
		stressmem,
		stopVM,
		blockfirewall,
		stopservice,
		whitelistStop
	}

}
