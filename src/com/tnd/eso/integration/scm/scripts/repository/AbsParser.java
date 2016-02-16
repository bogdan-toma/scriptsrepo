package com.tnd.eso.integration.scm.scripts.repository;

import java.io.File;

import com.tnd.eso.integration.scm.scripts.ScriptsRepoApp;

public abstract class AbsParser {
	protected static String dir = ScriptsRepoApp.getProperties().getProperty("REPOSITORY_DIR");

	public File getFileContents(String fileName) {
		File f = new File(dir + fileName + ScriptsRepoApp.getProperties().getProperty("DATA_FILE_EXTENSION"));
		return f;
	}
}
