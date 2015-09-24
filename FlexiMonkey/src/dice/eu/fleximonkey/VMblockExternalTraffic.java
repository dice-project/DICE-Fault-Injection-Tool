package dice.eu.fleximonkey;

import com.jcraft.jsch.*;

import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class VMblockExternalTraffic {
	private static final Logger log = Logger.getLogger( VMblockExternalTraffic.class.getName() );

	public void blockfirewall(String host,String vmpassword) {
		try {
			String info = null;
			// Import from config file

			// Add check before attempting SSH. ping?
			JSch jsch = new JSch();

			String user = host.substring(0, host.indexOf('@'));
			host = host.substring(host.indexOf('@') + 1);

			Session session = jsch.getSession(user, host, 22);

			// SSH key config - NOTE Needs tested/way to select between them
			//Also needed implemeted in comand line option
			/*
			 String privateKey = ".ssh/id_rsa"; jsch.addIdentity(privateKey);
			 * System.out.println("identity added ");
			 */

			session.setPassword(vmpassword);
			java.util.Properties config = new java.util.Properties();
			config.put("StrictHostKeyChecking", "no");
			session.setConfig(config);
			session.connect();
			log.log( Level.INFO, "Attempting to SSH to VM with ip " + host);

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
				log.log( Level.INFO, "ufm not found..Installing......");
			}

			// check not getting called with error

			else if (info.equals("install ok installed")) {
				command2 = "echo y | sudo ufw enable";
				log.log( Level.INFO, "ufw  found..setting firewall and disabling external connections......");
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
		} catch (Exception e) {
			log.log( Level.SEVERE, "Unable to SSH to VM", e.toString());
		}
	}
}
