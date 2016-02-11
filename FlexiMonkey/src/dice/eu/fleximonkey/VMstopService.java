package dice.eu.fleximonkey;

import com.jcraft.jsch.*;
import java.io.*;

public class VMstopService {

	public void stopservice(String host, String vmpassword,String service,String sshkeypath) {
		
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
			LoggerWrapper.myLogger.info("Attempting to SSH to VM with ip " + host);
			String command2 = null;
			//Different commands used if Centos or Ubuntu OS is used.
			if  (localOS.equals("CENTOS"))
			{
				command2 ="sudo /sbin/service " + service + " stop";
			}
			
			else if  (localOS.equals("UBUNTU"))
			{
				 command2 = "sudo service " + service + " stop";

			LoggerWrapper.myLogger.info("bonnie++ found..running test......");
			
				
			}
			byte[] tmp = new byte[1024];
			//Opens channel for sending  command.
			Channel channel = session.openChannel("exec");
			((ChannelExec) channel).setCommand(command2);
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
			//Closes after first command is sent
			channel.disconnect();
			//Close session after all commands are done
			session.disconnect();
			LoggerWrapper.myLogger.info( baos.toString());

		} catch (Exception e) {
			LoggerWrapper.myLogger.severe("Unable to SSH to VM " + e.toString());
		}
	}
}
