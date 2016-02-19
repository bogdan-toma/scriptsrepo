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

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.sun.org.apache.xerces.internal.parsers.DOMParser;
import com.tnd.eso.integration.scm.scripts.repository.ReposioryParserFactory;
import com.tnd.eso.integration.scm.scripts.repository.RepositoryParser;
import com.tnd.eso.integration.scm.scripts.transporter.Transporter;
import com.tnd.eso.integration.scm.scripts.transporter.TransporterFactory;
import com.tnd.eso.integration.scm.scripts.util.XmlHelper;

public class ScriptsRepoProcessor {
	XmlHelper helper;
	DOMParser domParser;
	Document doc;
	NodeList root;
	private String ESO_DATA_DIR;
	private String ESO_UPLOAD_DIR;

	private Transporter transporter;
	private RepositoryParser repoParser;

	protected ScriptsRepoProcessor() {
		ESO_DATA_DIR = ScriptsRepoApp.getProps().getProperty(ESO_DATA_DIR);
		ESO_UPLOAD_DIR = ScriptsRepoApp.getProps().getProperty(ESO_UPLOAD_DIR);
	}

	public ScriptsRepoProcessor(String fileName) {
		repoParser = ReposioryParserFactory.getParser();

		transporter = TransporterFactory.getTransporter();
		transporter.setWorkingDir(ESO_DATA_DIR);

		domParser = new DOMParser();

		try {
			helper = new XmlHelper();
			domParser.parse(fileName);
			doc = domParser.getDocument();

			root = doc.getChildNodes();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void process() {
		try {
			Node sapesourcing = helper.getNode("sapesourcing", root);
			Node objects = helper.getNode("objects", sapesourcing.getChildNodes());
			NodeList objectList = objects.getChildNodes();

			for (int i = 0; i < objectList.getLength(); i++) {
				Node object = objectList.item(i);

				if (!"object".equals(object.getNodeName()))
					continue;

				Node fields = helper.getNode("fields", object.getChildNodes());
				NodeList fieldList = fields.getChildNodes();

				String objectType = helper.getNodeAttr("classname", object);
				String fileIdentifier = helper.getNodeValue(ScriptsRepoApp.getProps().getProperty("REPOSITORY_FILE_ID"), fieldList);

				if ("doccommon.scripting.script_definition".equals(objectType)) {
					helper.setNodeValue("SCRIPT_VERSION", fieldList, repoParser.getLastCommitRevision(fileIdentifier + ".java"));
				}
				// disabled due to broken SAP importer
				else if ("odp.doccommon.scripting.callable_script".equals(objectType)) {
					helper.setNodeValue("DOCUMENT_DESCRIPTION", fieldList, repoParser.getLastCommitRevision(fileIdentifier + ".java"));
				}

				helper.setNodeValue("SCRIPT", fieldList, ScriptsRepoApp.getProps().getProperty("ESO_DATA_DIR") + fileIdentifier + ScriptsRepoApp.getProps().getProperty("DATA_FILE_EXTENSION"));

				transporter.transport(repoParser.getFileContents(fileIdentifier));
				System.out.println("Processed: " + fileIdentifier);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
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
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
			DOMSource source = new DOMSource(doc);
			StreamResult result = new StreamResult(temp);
			transformer.transform(source, result);
			return temp;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
