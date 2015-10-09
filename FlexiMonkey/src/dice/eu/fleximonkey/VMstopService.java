package dice.eu.fleximonkey;

import com.jcraft.jsch.*;

import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class VMstopService {
	private static final Logger log = Logger.getLogger( VMstopService.class.getName() );

	public void stopservice(String host, String vmpassword,String service,String sshkeypath) {
		try {

			String info = null;

			// String privateKey = ".ssh/id_rsa";

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
			log.log( Level.INFO, "Attempting to SSH to VM with ip " + host);

		
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
			channel.disconnect();
			session.disconnect();
		} catch (Exception e) {
			log.log( Level.SEVERE, "Unable to SSH to VM", e.toString());
		}
	}
}
