package dice.eu.fleximonkey;

import com.jcraft.jsch.*;

import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class VMblockExternalTraffic {

	public void blockfirewall(String host,String vmpassword, String sshkeypath) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		System.setOut(new PrintStream(baos));
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
			 if (sshkeypath.equals("-no")) {
				 session.setPassword(vmpassword);
			  }
			  else if (vmpassword.equals("-no"))
			  {
					 jsch.addIdentity(sshkeypath);
			  }

			java.util.Properties config = new java.util.Properties();
			config.put("StrictHostKeyChecking", "no");
			session.setConfig(config);
			session.connect();
			LoggerWrapper.myLogger.info( "Attempting to SSH to VM with ip " + host);

			String command = "dpkg-query -W -f='${Status}' ufw ";

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
					System.out.print(" Stress Status : " + info);
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
			channel.disconnect();
			String command2 = null;

			// Currently only runs for Ubuntu
			//Will need check for other OS/commands 
			if (info == null) {

				command2 = "sudo apt-get install ufw";
				LoggerWrapper.myLogger.info("ufm not found..Installing......");
			}

			// check not getting called with error

			else if (info.equals("install ok installed")) {
				command2 = "echo y | sudo ufw enable";
				LoggerWrapper.myLogger.info( "ufw  found..setting firewall and disabling external connections......");
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
			channel2.disconnect();
			session.disconnect();
			LoggerWrapper.myLogger.info( baos.toString());

		} catch (Exception e) {
			LoggerWrapper.myLogger.severe("Unable to SSH to VM " + e.toString());
		}
	}
}
