package com.tnd.eso.integration.scm.scripts.repository;

import java.io.File;
import java.text.SimpleDateFormat;

public class LocalParser extends AbsParser implements RepositoryParser {

	@Override
	public String getLastCommitRevision(String fileName) {
		File file = new File(dir + fileName);

		SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss");
		return sdf.format(file.lastModified());
	}

	@Override
	public void close() {
	}
}