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