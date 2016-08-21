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
import java.util.Arrays;
import java.util.List;

import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;
import org.simpleframework.xml.strategy.Strategy;
import org.simpleframework.xml.strategy.Type;
import org.simpleframework.xml.strategy.Visitor;
import org.simpleframework.xml.strategy.VisitorStrategy;
import org.simpleframework.xml.stream.InputNode;
import org.simpleframework.xml.stream.NodeMap;
import org.simpleframework.xml.stream.OutputNode;

import com.tnd.eso.integration.scm.scripts.model.XmlDataStore;
import com.tnd.eso.integration.scm.scripts.model.XmlScriptDefinitionBo;
import com.tnd.eso.integration.scm.scripts.model.XmlScriptExplicitBo;
import com.tnd.eso.integration.scm.scripts.model.XmlScriptIface;
import com.tnd.eso.integration.scm.scripts.repository.ReposioryParserFactory;
import com.tnd.eso.integration.scm.scripts.repository.RepositoryParser;
import com.tnd.eso.integration.scm.scripts.transporter.Transporter;
import com.tnd.eso.integration.scm.scripts.transporter.TransporterFactory;

public class ScriptsRepoProcessor {
	private XmlDataStore xmlDataStore;
	private String ESO_DATA_DIR;
	private String ESO_UPLOAD_DIR;

	private Transporter transporter;
	private RepositoryParser repoParser;

	protected ScriptsRepoProcessor() {
	}

	public ScriptsRepoProcessor(String dataStoreLocation) throws Exception {
		ESO_DATA_DIR = ScriptsRepoApp.getProps().getProperty("ESO_DATA_DIR");
		ESO_UPLOAD_DIR = ScriptsRepoApp.getProps().getProperty("ESO_UPLOAD_DIR");
		repoParser = ReposioryParserFactory.getParser();

		transporter = TransporterFactory.getTransporter();
		transporter.setWorkingDir(ESO_DATA_DIR);

		Serializer serializer = new Persister();
		File dataStore = new File(dataStoreLocation);
		xmlDataStore = serializer.read(XmlDataStore.class, dataStore);
	}

	public void process() {
		List<XmlScriptIface> scripts = xmlDataStore.getScripts();
		try {
			for (int i = scripts.size() - 1; i >= 0; i--) {
				XmlScriptIface script = scripts.get(i);

				if (!Boolean.valueOf(ScriptsRepoApp.getProps().getProperty("DEPLOY_INACTIVE"))) {
					if (Boolean.valueOf(script.getInactive())) {
						scripts.remove(i);
						continue;
					}
				}

				String fileIdentifier = script.getExternalId();
				File scriptDataFile = repoParser.getFileContents(fileIdentifier);
				if (!scriptDataFile.exists()) {
					System.out.println("WARNING: File " + fileIdentifier + " does not exist in repository! Skipping.");
					scripts.remove(i);
					continue;
				}

				String version = repoParser.getLastCommitRevision(fileIdentifier + ScriptsRepoApp.getProps().getProperty("DATA_FILE_EXTENSION"));

				if ("doccommon.scripting.script_definition".equals(script.getType())) {
					((XmlScriptDefinitionBo) script).setScriptVersion(version);
				} else if ("odp.doccommon.scripting.callable_script".equals(script.getType())) {
					((XmlScriptExplicitBo) script).setDocumentDescription(version);
				}

				script.setScript(ScriptsRepoApp.getProps().getProperty("ESO_DATA_DIR") + fileIdentifier + ScriptsRepoApp.getProps().getProperty("DATA_FILE_EXTENSION"));
				transporter.transport(scriptDataFile);
				System.out.println("Processed: " + fileIdentifier);
			}
		} finally {
			repoParser.close();
		}

		transporter.setWorkingDir(ESO_UPLOAD_DIR);
		File xmlFile = export();
		System.out.println("XML generation complete.");
		transporter.transport(xmlFile);
		System.out.println("XML transport complete: " + xmlFile.getName());

		transporter.close();
	}

	private File export() {

		try {
			File temp = File.createTempFile("ZTND_SCRIPT_DEF_", ".xml");

			Strategy strategy = new VisitorStrategy(new Visitor() {
				List<String> list = Arrays.asList(new String[] { "objects", "object" });

				@Override
				public void read(Type arg0, NodeMap<InputNode> arg1) throws Exception {
				}

				@Override
				public void write(Type arg0, NodeMap<OutputNode> arg1) throws Exception {
					if (list.contains(arg1.getName())) {
						arg1.remove("class");
					}
				}
			});

			Serializer serializer = new Persister(strategy);
			serializer.write(xmlDataStore, temp);

			return temp;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
