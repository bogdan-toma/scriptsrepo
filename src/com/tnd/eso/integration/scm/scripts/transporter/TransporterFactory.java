package com.tnd.eso.integration.scm.scripts.transporter;

import com.tnd.eso.integration.scm.scripts.ScriptsRepoApp;

public class TransporterFactory {
	private static Transporter transporter;

	public static Transporter getTransporter() {
		if (transporter == null) {
			// check test parameter
			String transportProtocol = ScriptsRepoApp.getProperties().getProperty("TRANSPORT_PROTOCOL");
			if ("DUMMY".equals(transportProtocol)) {
				transporter = new ConsoleTransporter();
			} else if ("SFTP".equals(transportProtocol)) {
				transporter = new SshTransporter();
			} else {
				// TODO - invalid configuration management
			}
		}
		return transporter;
	}
}
