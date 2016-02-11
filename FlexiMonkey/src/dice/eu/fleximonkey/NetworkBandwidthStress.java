package dice.eu.fleximonkey;

import com.jcraft.jsch.*;
import java.io.*;

public class NetworkBandwidthStress {

	public void networkbandwidthstress(String host, String vmpassword,String iperfserver,String time,String sshkeypath) {
		
		//Calls OS checker to determine if Ubuntu or Centos os
		OSChecker oscheck = new OSChecker();
		oscheck.oscheck(host, vmpassword, sshkeypath);
		String localOS = oscheck.OSVERSION;
		LoggerWrapper.myLogger.info(localOS);

		String command;
		
		if (localOS.equals("UBUNTU"))
		{
		command ="sudo apt-get -q -y install iperf";

		}
		else
		{
			//CENTOS will not accept first command so "dud" command sent
		command ="";
		}
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
				
			//Opens channel for sending first command.
			Channel channel = session.openChannel("exec");
			((ChannelExec) channel).setCommand(command);
			channel.setInputStream(null);

			((ChannelExec) channel).setErrStream(System.err);

			InputStream in = channel.getInputStream();

			channel.connect();

			byte[] tmp = new byte[1024];
			while (true) {
				while (in.available() > 0) {
					int i = in.read(tmp, 0, 1024);
					if (i < 0)
						break;
					System.out.print(new String(tmp, 0, i));
					info = new String(tmp, 0, i);
					//Outputs responce for ssh connection
					System.out.print(" Disk Stress Status : " + info);
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
			//Closes after first command is sent
			channel.disconnect();
			//Sets up for second command
			String command2 = null;
			//Different commands used if Centos or Ubuntu OS is used.
			if  (localOS.equals("CENTOS"))
			{
				command2 = "sudo yum install iperf; sudo iperf -c "+ iperfserver +" -m "+" -t " + time;
				LoggerWrapper.myLogger.info("Installing iperf tool if required then running command");

			}
			
			else if  (localOS.equals("UBUNTU"))
				
			{
		
			if (info == null) {
				
				command2 = "sudo apt-get -q -y install iperf";
				LoggerWrapper.myLogger.info("iperf tool not found..Installing......");
			}
			else if (info.equals("install ok installed")) {
			command2 = "sudo iperf -c "+ iperfserver +" -m "+" -t " + time;
			LoggerWrapper.myLogger.info("iperf running loop for: "
					+ time);			
				}
			}

			Channel channel2 = session.openChannel("exec");
			((ChannelExec) channel2).setCommand(command2);
			InputStream in1 = channel2.getInputStream();
			channel2.connect();
			while (true) {
				while (in1.available() > 0) {
					int i = in1.read(tmp, 0, 1024);
					if (i < 0)
						break;
					System.out.print(new String(tmp, 0, i));
					info = new String(tmp, 0, i);
					System.out.print(info);
				}
				if (channel2.isClosed()) {
					if (in.available() > 0)
						continue;
					System.out.println("exit-status: "
							+ channel2.getExitStatus());
					break;
				}
				try {
					Thread.sleep(1000);
				} catch (Exception ee) {
				}

			}
			in1.close();
			//Close after second command
			channel2.disconnect();
			//Close session after all commands are done
			session.disconnect();
			LoggerWrapper.myLogger.info( baos.toString());

		} catch (Exception e) {
			LoggerWrapper.myLogger.severe("Unable to SSH to VM " + e.toString());
		}
	}
}
