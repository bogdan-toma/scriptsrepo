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

import com.tnd.eso.integration.scm.scripts.ScriptsRepoApp;

public abstract class AbsParser {
	protected static String dir = ScriptsRepoApp.getProps().getProperty("REPOSITORY_DIR");

	public File getFileContents(String fileName) {
		File f = new File(dir + fileName + ScriptsRepoApp.getProps().getProperty("DATA_FILE_EXTENSION"));
		return f;
	}
}
