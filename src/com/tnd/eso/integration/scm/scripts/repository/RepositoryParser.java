package com.tnd.eso.integration.scm.scripts.repository;

import java.io.File;

public interface RepositoryParser {
	public String getLastCommitRevision(String fileName);

	public File getFileContents(String fileName);

	public void close();
}