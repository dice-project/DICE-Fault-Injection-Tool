package dice.eu.fleximonkey;

import com.jcraft.jsch.*;

import java.io.*;

public class VMcpuStress {

	public void stresscpu(String cores, String time, String vmpassword,
			String host) {
		try {
			String info = null;
			// Import from config file

			// Add check before attempting SSH. ping?
			JSch jsch = new JSch();

			String user = host.substring(0, host.indexOf('@'));
			host = host.substring(host.indexOf('@') + 1);

			Session session = jsch.getSession(user, host, 22);

			// SSH key config - NOTE Needs tested/way to select between them
			/*
			 * String privateKey = ".ssh/id_rsa"; jsch.addIdentity(privateKey);
			 * System.out.println("identity added ");
			 */

			session.setPassword(vmpassword);
			java.util.Properties config = new java.util.Properties();
			config.put("StrictHostKeyChecking", "no");
			session.setConfig(config);
			session.connect();

			// Look at ways to get number of CPU's to run test.
			// String command="cat /proc/cpuinfo | grep processor | wc -l";
			String command = "dpkg-query -W -f='${Status}' stress ";

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

			// Checking all OS versions centos, Ubuntu Debian.
			if (info == null) {

				command2 = "sudo apt-get -q -y install stress";
				System.out.print("Stress tool not found..Installing......");
			}

			// check not getting called with error

			else if (info.equals("install ok installed")) {
				command2 = "stress -c " + cores + " -t " + time;
				System.out.print("Stress tool found..running test......");
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
			System.out.println(e);
		}
	}
}
