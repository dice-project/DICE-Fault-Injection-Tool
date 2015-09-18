package dice.eu.fleximonkey;
import java.io.IOException;

public class FlexiMonkeyMain {
	
	public static void main(String[] args) throws IOException {

		FlexiMonkeyReadConfig properties = new FlexiMonkeyReadConfig();
		properties.getPropValues();
		
		VMState vmstate = VMState.valueOf(properties.action);
		
		//Switch case to all require actions
		switch (vmstate) {
		case stresscpu:
			//Start Stress execute code
			System.out.println("Executing CPU stress on VM");
			VMcpuStress cpustress = new VMcpuStress();
			cpustress.stresscpu(properties.cores, properties.stresstime, properties.vmpassword, properties.host);
		break;	
		
        case stopVM:
        	FCOListVMs listvms = new FCOListVMs();
        	listvms.listvms(properties.cloudusername, properties.cloudpassword, properties.cloudapiurl, properties.cloudUUID);
        	System.out.println("Executing stop random VM");
        break;
        case stressmem:
        	System.out.println("Executing Mem stress on VM");
			VMmemoryStress vmmemstress = new VMmemoryStress();
			vmmemstress.stressmemory(properties.cores, properties.stresstime, properties.vmpassword, properties.host, properties.memorytesterloops);	
        break;
        
       
        default:
        	System.out.println("Error no option selected!!");
        break;	
	  }

	}
	
	public enum VMState{
		stresscpu,
		stressmem,
		stopVM
	}

}
