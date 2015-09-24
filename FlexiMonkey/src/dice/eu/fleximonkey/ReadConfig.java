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
			e.printStackTrace();
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
        
       
        default:
			 log.log( Level.SEVERE, "Error no option selected!!");

        break;	
	  }

	}
	
	public enum VMState{
		stresscpu,
		stressmem,
		stopVM
	}

}
