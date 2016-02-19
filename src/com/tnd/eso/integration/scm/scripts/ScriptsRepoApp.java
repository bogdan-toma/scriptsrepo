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
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.Properties;

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

	protected void loadProps() throws IOException {
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

	protected void runImport() {
		try {
			if (!initCheck())
				return;
			generateConfig();
			init();
			importScripts();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	protected void runDeploy() {
		try {
			loadProps();
			process();
		} catch (IOException e) {
			System.out.println("Could not load config.properties file. Aborting.");
			e.printStackTrace();
		}

	}

	protected void process() {
		RepositoryParser repoParser = ReposioryParserFactory.getParser();
		System.out.println("Initialised git repository.");

		Transporter transporter = TransporterFactory.getTransporter();
		System.out.println("Initialised file transporter.");

		try {
			ScriptsRepoProcessor scriptXmlParser = new ScriptsRepoProcessor(path + RESOURCES_DIR + TEMPLATE_FILENAME);
			scriptXmlParser.process();
		} finally {
			repoParser.close();
			transporter.close();
		}
	}

	protected void generateConfig() throws FileNotFoundException {
		File f = new File(path + CONFIG_FILENAME);
		if (f.exists())
			return;

		PrintWriter writer = new PrintWriter(f);
		writer.println("REPOSITORY_TYPE=GIT");
		writer.println("REPOSITORY_DIR=C:/Users/DummyUser/Projects/CLMX/Scripts/");
		writer.println("REPOSITORY_FILE_ID=EXTERNAL_ID");
		writer.println("DATA_FILE_EXTENSION=.java");
		writer.println("ESO_DATA_DIR=/usr/sap/server/clm/scriptsrepo/Import/Data/");
		writer.println("ESO_UPLOAD_DIR=/usr/sap/server/clm/scriptsrepo/Import/Upload/");
		writer.println("TRANSPORT_PROTOCOL=DUMMY");
		writer.println("HOST=ssh.corporate.com");
		writer.println("PORT=22");
		writer.println("USER=username");
		writer.println("PASS=password");
		writer.close();
	}

	protected boolean initCheck() {
		File f = new File(path + OMA_IMPORT_FILE);
		if (!f.exists()) {
			System.out.println("Import oma file " + OMA_IMPORT_FILE + " not found!");
			return false;
		}
		return true;
	}

	protected void init() {
		File resDir = new File(path + RESOURCES_DIR);
		if (!resDir.exists()) {
			System.out.println("Creating directory " + RESOURCES_DIR);
			resDir.mkdir();
		}
	}

	protected void importScripts() {
		TemplateGenerator templateGenerator = new TemplateGenerator(path);
		templateGenerator.process();
	}
}
