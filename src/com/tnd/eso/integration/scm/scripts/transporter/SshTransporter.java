package com.tnd.eso.integration.scm.scripts.transporter;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;
import com.tnd.eso.integration.scm.scripts.ScriptsRepoApp;

public class SshTransporter implements Transporter {

	private static String HOST = ScriptsRepoApp.getProperties().getProperty("HOST");
	private static int PORT = Integer.parseInt(ScriptsRepoApp.getProperties().getProperty("PORT"));
	private static String USER = ScriptsRepoApp.getProperties().getProperty("USER");
	private static String PASS = ScriptsRepoApp.getProperties().getProperty("PASS");
	private static Session session;
	private static Channel channel;
	private static ChannelSftp channelSftp;

	protected SshTransporter() {
		try {
			JSch jsch = new JSch();
			session = jsch.getSession(USER, HOST, PORT);
			session.setPassword(new String(PASS));
			java.util.Properties config = new java.util.Properties();
			config.put("StrictHostKeyChecking", "no");
			session.setConfig(config);
			session.connect();
			channel = session.openChannel("sftp");
			channel.connect();
			channelSftp = (ChannelSftp) channel;
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	@Override
	public void setWorkingDir(String SFTPWORKINGDIR) {
		try {
			channelSftp.cd(SFTPWORKINGDIR);
		} catch (SftpException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void transport(File f) {
		try {
			InputStream stream = new FileInputStream(f);
			channelSftp.put(stream, f.getName());
			int chmodInt = Integer.parseInt("777", 8);
			channelSftp.chmod(chmodInt, channelSftp.pwd() + "/" + f.getName());
			stream.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void close() {
		channelSftp.disconnect();
		session.disconnect();
	}
}