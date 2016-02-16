package com.tnd.eso.integration.scm.scripts.transporter;

import java.io.File;

public interface Transporter {
	public void transport(File f);

	public void setWorkingDir(String SFTPWORKINGDIR);

	public void close();
}
