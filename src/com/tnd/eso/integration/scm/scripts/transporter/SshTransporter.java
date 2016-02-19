/**
 * ScriptsRepo - Automatic deploy tool for SAP Sourcing scripts
 * Copyright (C) 2016  Bogdan Toma
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
**/
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

	private static String HOST = ScriptsRepoApp.getProps().getProperty("HOST");
	private static int PORT = Integer.parseInt(ScriptsRepoApp.getProps().getProperty("PORT"));
	private static String USER = ScriptsRepoApp.getProps().getProperty("USER");
	private static String PASS = ScriptsRepoApp.getProps().getProperty("PASS");
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