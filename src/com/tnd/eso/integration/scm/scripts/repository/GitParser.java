package com.tnd.eso.integration.scm.scripts.repository;

import java.io.File;
import java.io.IOException;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.LogCommand;
import org.eclipse.jgit.lib.Constants;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevWalk;

public class GitParser extends AbsParser implements RepositoryParser {

	private static File gitWorkDir;
	private static Git git;
	private static Repository repo;
	private static String head;

	protected GitParser() {
		try {
			gitWorkDir = new File(dir);
			git = Git.open(gitWorkDir);
			repo = git.getRepository();

			// get head commit hash
			ObjectId lastCommitId = repo.resolve(Constants.HEAD);
			RevWalk revWalk = new RevWalk(repo);
			RevCommit commit = revWalk.parseCommit(lastCommitId);
			head = commit.getName().substring(0, 7);
			revWalk.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public String getLastCommitRevision(String fileName) {
		try {
			LogCommand logCommand = git.log().add(repo.resolve(Constants.HEAD)).addPath(fileName);

			for (RevCommit revCommit : logCommand.call()) {
				return head + "-" + revCommit.getName().substring(0, 7);
			}
			return null;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public void close() {
		git.close();
	}
}