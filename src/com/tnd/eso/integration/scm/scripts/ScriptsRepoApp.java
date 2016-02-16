package com.tnd.eso.integration.scm.scripts;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.Properties;

import com.tnd.eso.integration.scm.scripts.repository.ReposioryParserFactory;
import com.tnd.eso.integration.scm.scripts.repository.RepositoryParser;
import com.tnd.eso.integration.scm.scripts.transporter.Transporter;
import com.tnd.eso.integration.scm.scripts.transporter.TransporterFactory;

public class ScriptsRepoApp {
	private static Properties prop = new Properties();
	private static String CONFIG_FILENAME = "config.properties";
	private final String path = ScriptsRepo.class.getProtectionDomain().getCodeSource().getLocation().getPath();

	public static Properties getProperties() {
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

	protected void process() {
		RepositoryParser repoParser = ReposioryParserFactory.getParser();
		System.out.println("Initialised git repository.");

		Transporter transporter = TransporterFactory.getTransporter();
		System.out.println("Initialised file transporter.");

		try {
			ScriptsRepoProcessor scriptXmlParser = new ScriptsRepoProcessor(prop.getProperty("XML_IMPORT_TEMPLATE"));
			scriptXmlParser.process();
		} finally {
			repoParser.close();
			transporter.close();
		}

		System.out.println("DONE");
		System.out.println();
	}

	protected void generateConfig() throws Exception {
		File f = new File(path + CONFIG_FILENAME);
		if (f.exists())
			return;

		PrintWriter writer = new PrintWriter(f);
		writer.println("XML_IMPORT_TEMPLATE=resources/ZTND_SCRIPT_DEF_TEMPLATE.xml");
		writer.println("REPOSITORY_TYPE=GIT");
		writer.println("REPOSITORY_DIR=C:/Users/DummyUser/Projects/CLMX/Scripts/");
		writer.println("DATA_FILE_EXTENSION=.java");
		writer.println("ESO_DATA_DIR=/usr/sap/server/clm/scriptsrepo/Import/Data/");
		writer.println("ESO_UPLOAD_DIR=/usr/sap/server/clm/scriptsrepo/Import/Upload/");
		writer.println("TRANSPORT_PROTOCOL=SFTP");
		writer.println("HOST=ssh.corporate.com");
		writer.println("PORT=22");
		writer.println("USER=username");
		writer.println("PASS=password");
		writer.close();
	}
}
