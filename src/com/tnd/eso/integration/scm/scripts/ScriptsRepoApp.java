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
package com.tnd.eso.integration.scm.scripts;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.Properties;

import org.simpleframework.xml.core.InstantiationException;

import com.tnd.eso.integration.scm.scripts.init.TemplateGenerator;
import com.tnd.eso.integration.scm.scripts.repository.ReposioryParserFactory;
import com.tnd.eso.integration.scm.scripts.repository.RepositoryParser;
import com.tnd.eso.integration.scm.scripts.transporter.Transporter;
import com.tnd.eso.integration.scm.scripts.transporter.TransporterFactory;

public class ScriptsRepoApp {
	public static final String OMA_IMPORT_FILE = "allscripts.oma";
	public static final String RESOURCES_DIR = ".resources" + File.separator;
	public static final String TEMPLATE_FILENAME = "ZTND_SCRIPT_DEF_TEMPLATE.xml";
	private static String CONFIG_FILENAME = "config.properties";
	private static Properties prop = new Properties();
	private final String path = ScriptsRepo.class.getProtectionDomain().getCodeSource().getLocation().getPath();

	public static Properties getProps() {
		return prop;
	}

	protected void runImport() {
		try {
			if (!initCheck())
				return;
			generateConfig();
			init();
			importScripts();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	protected void runDeploy() {
		try {
			loadProps();
			if (!writeConfigDefaults()) {
				process();
			} else {
				System.out.println("WARNING - config.properties file was updated!");
				System.out.println("Please check and re-execute --deploy.");
				System.out.println();
			}
		} catch (IOException e) {
			System.out.println("Could not load config.properties file. Aborting.");
			e.printStackTrace();
		}

	}

	private void loadProps() throws IOException {
		InputStream input = null;
		try {
			input = new FileInputStream(path + CONFIG_FILENAME);
			prop.load(input);
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	private void process() {
		RepositoryParser repoParser = ReposioryParserFactory.getParser();
		System.out.println("Initialised git repository.");

		Transporter transporter = TransporterFactory.getTransporter();
		System.out.println("Initialised file transporter.");
		System.out.println();

		try {
			ScriptsRepoProcessor scriptXmlParser = new ScriptsRepoProcessor(path + RESOURCES_DIR + TEMPLATE_FILENAME);
			scriptXmlParser.process();
		} catch (InstantiationException ie) {
			System.out.println("ERROR: Cannot load scripts. Please execute --import");
			System.out.println();
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			repoParser.close();
			transporter.close();
		}
	}

	private boolean writeConfigDefaults() throws IOException {
		File f = new File(path + CONFIG_FILENAME);
		boolean configUpdated = setConfigDefaults();
		if (configUpdated) {
			PrintWriter writer = new PrintWriter(f);
			prop.store(writer, null);
			writer.close();
		}
		return configUpdated;
	}

	private void generateConfig() throws IOException {
		File f = new File(path + CONFIG_FILENAME);
		if (f.exists())
			loadProps();

		writeConfigDefaults();
	}

	private boolean setConfigDefaults() {
		boolean changed = false;

		if (prop.getProperty("REPOSITORY_TYPE") == null) {
			prop.put("REPOSITORY_TYPE", "GIT");
			changed = true;
		}

		if (prop.getProperty("REPOSITORY_DIR") == null) {
			prop.put("REPOSITORY_DIR", "C:/Users/DummyUser/Projects/CLMX/Scripts/");
			changed = true;
		}

		if (prop.getProperty("REPOSITORY_FILE_ID") == null) {
			prop.put("REPOSITORY_FILE_ID", "EXTERNAL_ID");
			changed = true;
		}

		if (prop.getProperty("DATA_FILE_EXTENSION") == null) {
			prop.put("DATA_FILE_EXTENSION", ".java");
			changed = true;
		}

		if (prop.getProperty("ESO_DATA_DIR") == null) {
			prop.put("ESO_DATA_DIR", "/usr/sap/server/clm/scriptsrepo/Import/Data/");
			changed = true;
		}

		if (prop.getProperty("ESO_UPLOAD_DIR") == null) {
			prop.put("ESO_UPLOAD_DIR", "/usr/sap/server/clm/scriptsrepo/Import/Upload/");
			changed = true;
		}

		if (prop.getProperty("TRANSPORT_PROTOCOL") == null) {
			prop.put("TRANSPORT_PROTOCOL", "DUMMY");
			changed = true;
		}

		if (prop.getProperty("HOST") == null) {
			prop.put("HOST", "ssh.corporate.com");
			changed = true;
		}

		if (prop.getProperty("PORT") == null) {
			prop.put("PORT", "22");
			changed = true;
		}

		if (prop.getProperty("USER") == null) {
			prop.put("USER", "username");
			changed = true;
		}

		if (prop.getProperty("PASS") == null) {
			prop.put("PASS", "password");
			changed = true;
		}

		if (prop.getProperty("ESO_VERSION") == null) {
			prop.put("ESO_VERSION", "9");
			changed = true;
		}

		if (prop.getProperty("DEPLOY_INACTIVE") == null) {
			prop.put("DEPLOY_INACTIVE", "false");
		}
		return changed;
	}

	private boolean initCheck() {
		File f = new File(path + OMA_IMPORT_FILE);
		if (!f.exists()) {
			System.out.println("Import oma file " + OMA_IMPORT_FILE + " not found!");
			return false;
		}
		return true;
	}

	private void init() {
		File resDir = new File(path + RESOURCES_DIR);
		if (!resDir.exists()) {
			System.out.println("Creating directory " + RESOURCES_DIR);
			resDir.mkdir();
		}
	}

	private void importScripts() {
		TemplateGenerator templateGenerator = new TemplateGenerator(path);
		templateGenerator.process();
	}
}
