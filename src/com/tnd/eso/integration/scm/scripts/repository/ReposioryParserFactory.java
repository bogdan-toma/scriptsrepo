package com.tnd.eso.integration.scm.scripts.repository;

import com.tnd.eso.integration.scm.scripts.ScriptsRepoApp;

public class ReposioryParserFactory {
	private static RepositoryParser parser;

	public static RepositoryParser getParser() {
		if (parser == null) {
			String repoType = ScriptsRepoApp.getProperties().getProperty("REPOSITORY_TYPE");
			if ("GIT".equals(repoType)) {
				parser = new GitParser();
			} else if ("LOCAL".equals(repoType)) {
				parser = new LocalParser();
			}
		}
		return parser;
	}
}