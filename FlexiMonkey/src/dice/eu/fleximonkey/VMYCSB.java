package dice.eu.fleximonkey;

import com.jcraft.jsch.*;

import java.io.*;

public class VMYCSB {

	public void vmycsbclass(String host, String vmpassword,String workloadname,String threads,String sshkeypath) {
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

			// String privateKey = ".ssh/id_rsa";

			// Add check before attempting SSH. ping?
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
			LoggerWrapper.myLogger.info("Attempting to SSH to VM with ip " + host);

			// Look at ways to get number of CPU's to run test.
			
			String command = "cd /home/"+user+"/ycsb-0.5.0";

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
					System.out.print(" YCSB Status : " + info);
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
			if (info.equals("No such file or directory")) {
command2 = "curl -O --location https://github.com/brianfrankcooper/YCSB/releases/download/0.5.0/ycsb-0.5.0.tar.gz"
		+ " && tar xfvz ycsb-0.5.0.tar.gz &&"
		+ " cd /home/"+user+"/ycsb-0.5.0/ "
				+ "./bin/ycsb load mongodb -s -P workloads/"+workloadname+" -p recordcount=10000000 -threads 2";
				LoggerWrapper.myLogger.info("YSCA tool not found..Installing......");

			}

			// check not getting called with error

			else {
				command2 = "cd /home/"+user+"/ycsb-0.5.0/ && ./bin/ycsb run mongodb -s -P workloads/"+ workloadname+" -p operationcount=10000000 -threads 2";

				LoggerWrapper.myLogger.info("YSCA tool found..running test......");
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
