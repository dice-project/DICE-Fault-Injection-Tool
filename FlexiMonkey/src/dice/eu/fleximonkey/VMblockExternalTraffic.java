package dice.eu.fleximonkey;

import com.jcraft.jsch.*;
import java.io.*;

public class VMblockExternalTraffic {

	public void blockfirewall(String host,String vmpassword, String sshkeypath) {		
		
		//Calls OS checker to determine if Ubuntu or Centos os

		OSChecker oscheck = new OSChecker();
		oscheck.oscheck(host, vmpassword, sshkeypath);
		String localOS = oscheck.OSVERSION;
		LoggerWrapper.myLogger.info(localOS);

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		System.setOut(new PrintStream(baos));
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

		
		try {
			
			String info = null;

			JSch jsch = new JSch();

			String user = host.substring(0, host.indexOf('@'));
			host = host.substring(host.indexOf('@') + 1);

			Session session = jsch.getSession(user, host, 22);
			 //Used to determine if ssh key or password is proivded with command 

			if (sshkeypath.equals("-no")) {
				 session.setPassword(vmpassword);
			  }
			  else if (vmpassword.equals("-no"))
			  {
					 jsch.addIdentity(sshkeypath);
			  }

			session.setPassword(vmpassword);
		
			java.util.Properties config = new java.util.Properties();
			config.put("StrictHostKeyChecking", "no");
			session.setConfig(config);
		
			session.connect();
			byte[] tmp = new byte[1024];
			
			String command = null;
			//Different commands used if Centos or Ubuntu OS is used.

			if  (localOS.equals("CENTOS"))
			{
				command = "sudo yum install epel-release; sudo systemctl start firewalld && sudo firewall-cmd --permanent --add-service=ssh && sudo firewall-cmd --reload ";
				LoggerWrapper.myLogger.info("Installing Firewall tool if required and running test..... ");

			}
			
			else if  (localOS.equals("UBUNTU"))
				
			{

				command = "sudo apt-get install ufw; echo y | sudo ufw enable";
				LoggerWrapper.myLogger.info( "ufw  found..setting firewall and disabling external connections......");
	
			}

			Channel channel = session.openChannel("exec");
			((ChannelExec) channel).setCommand(command);
			InputStream in = channel.getInputStream();
			channel.connect();
			while (true) {
				while (in.available() > 0) {
					int i = in.read(tmp, 0, 1024);
					if (i < 0)
						break;
					System.out.print(new String(tmp, 0, i));
					info = new String(tmp, 0, i);
					//Outputs responce for ssh connection
					System.out.print(info);
				}
				if (channel.isClosed()) {
					if (in.available() > 0)
						continue;
					System.out.println("exit-status: "
							+ channel.getExitStatus());
					break;
				}
				try {
					Thread.sleep(1000);
				} catch (Exception ee) {
				}

			}
			in.close();
			//Close after command sent
			channel.disconnect();
			//Close session after all commands are done
			session.disconnect();
			LoggerWrapper.myLogger.info( baos.toString());

		} catch (Exception e) {
			LoggerWrapper.myLogger.severe("Unable to SSH to VM " + e.toString());
		}
	}
}
