package dice.eu.fleximonkey;

import com.jcraft.jsch.*;

import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class VMstopService {

	public void stopservice(String host, String vmpassword,String service,String sshkeypath) {
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
			
			//Formats host information from user
			String user = host.substring(0, host.indexOf('@'));
			host = host.substring(host.indexOf('@') + 1);
			//Creates new SSH session
			Session session = jsch.getSession(user, host, 22);
			//Checks if password or SSH key path has been provided
			 if (sshkeypath.equals("-no")) {
				 session.setPassword(vmpassword);
			  }
			  else if (vmpassword.equals("-no"))
			  {
					 jsch.addIdentity(sshkeypath);
			  }
			//Configures session and attempts SSH
			java.util.Properties config = new java.util.Properties();
			config.put("StrictHostKeyChecking", "no");
			session.setConfig(config);
			session.connect();
			LoggerWrapper.myLogger.info( "Attempting to SSH to VM with ip " + host);
			//Executes command on VM
			String command = "sudo service " + service + " stop";
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
			//Disconnects all session to VM
			channel.disconnect();
			session.disconnect();
			LoggerWrapper.myLogger.info( baos.toString());

		} catch (Exception e) {
			LoggerWrapper.myLogger.severe("Unable to SSH to VM " +  e.toString());
		}
	}
}
