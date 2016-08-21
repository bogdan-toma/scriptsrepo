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
package com.tnd.eso.integration.scm.scripts.init;

import java.io.File;

import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.sun.org.apache.xerces.internal.parsers.DOMParser;
import com.tnd.eso.integration.scm.scripts.ScriptsRepoApp;
import com.tnd.eso.integration.scm.scripts.model.XmlDataStore;
import com.tnd.eso.integration.scm.scripts.model.XmlScriptDefinitionBo;
import com.tnd.eso.integration.scm.scripts.model.XmlScriptExplicitBo;
import com.tnd.eso.integration.scm.scripts.model.XmlScriptIface;
import com.tnd.eso.integration.scm.scripts.util.XmlHelper;
import com.tnd.eso.integration.scm.scripts.util.ZipHelper;

public class TemplateGenerator {
	private final String path;
	private final String ZIP_TEMP_DIR = "allscripts" + File.separator;
	private final String DUMMY_TEXT = "DUMMY";
	private XmlHelper helper;

	public TemplateGenerator(String currentPath) {
		path = currentPath;
		helper = new XmlHelper();
	}

	public void process() {
		preProcess();
		unzip();
		generateTemplate();
		cleanup(new File(path + ScriptsRepoApp.RESOURCES_DIR + ZIP_TEMP_DIR));
		cleanup(new File(path + ScriptsRepoApp.OMA_IMPORT_FILE));
	}

	private void unzip() {
		ZipHelper zipHelper = new ZipHelper();
		zipHelper.unzip(path + ScriptsRepoApp.OMA_IMPORT_FILE, path + ScriptsRepoApp.RESOURCES_DIR + ZIP_TEMP_DIR);
	}

	private void generateTemplate() {
		DOMParser domParser = new DOMParser();

		try {
			domParser.parse(path + ScriptsRepoApp.RESOURCES_DIR + ZIP_TEMP_DIR + "exported_objects.0.xml");
			Document doc = domParser.getDocument();
			NodeList root = doc.getChildNodes();

			Node fciObjects = helper.getNode("fciObjects", root);
			NodeList objectList = fciObjects.getChildNodes();

			XmlDataStore xmlDataStore = new XmlDataStore();

			for (int i = 0; i < objectList.getLength(); i++) {
				Node object = objectList.item(i);
				if (!"object".equals(object.getNodeName()))
					continue;

				String objectType = helper.getNodeAttr("class", object);
				Node fields = helper.getNode("fields", object.getChildNodes());

				XmlScriptIface script;
				switch (objectType) {
				case "doccommon.scripting.script_definition":
					script = generateScriptDefinitionBo(fields, objectType);
					xmlDataStore.getScripts().add(script);
					System.out.println("Processed import: " + script.getDisplayName());
					break;
				case "odp.doccommon.scripting.callable_script":
					// Import Explicitly Called Scripts only for ESO V10+
					if (Integer.valueOf(ScriptsRepoApp.getProps().getProperty("ESO_VERSION")).compareTo(10) >= 0) {
						script = generateExplicitScriptBo(fields, objectType);
						xmlDataStore.getScripts().add(script);
						System.out.println("Processed import: " + script.getDisplayName());
					}
					break;
				default:
					continue;
				}
			}

			serialize(xmlDataStore);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void cleanup(File f) {
		if (f.isDirectory()) {
			for (File c : f.listFiles())
				cleanup(c);
		}
		if (!f.delete())
			System.out.println("Failed to delete: " + f);
	}

	private void preProcess() {
		File oldTemplate = new File(path + ScriptsRepoApp.RESOURCES_DIR + ScriptsRepoApp.getProps().get("XML_IMPORT_TEMPLATE"));
		if (oldTemplate.exists())
			oldTemplate.delete();
	}

	private XmlScriptIface generateScriptDefinitionBo(Node node, String type) {
		XmlScriptDefinitionBo xmlImportScript = new XmlScriptDefinitionBo();
		xmlImportScript.setType(type);

		NodeList fieldList = node.getChildNodes();

		for (int j = 0; j < fieldList.getLength(); j++) {
			Node field = fieldList.item(j);
			if (!"field".equals(field.getNodeName()))
				continue;

			String fieldType = helper.getNodeAttr("name", field);

			switch (fieldType) {
			case "EXTERNAL_ID":
				xmlImportScript.setExternalId(helper.getNodeValue(field));
				break;
			case "DISPLAY_NAME":
				xmlImportScript.setDisplayName(helper.getNodeValue(field));
				break;
			case "SCRIPT_CONTEXT":
				xmlImportScript.setScriptContext(helper.getNodeValue(field));
				break;
			case "BUS_UNIT_CTXT":
				xmlImportScript.setBusUnitContext(helper.getNodeValue(field));
				break;
			case "TARGET_CLASS_ID":
				xmlImportScript.setTargetClassId(helper.getNodeValue(field));
				break;
			case "TARGET_INSTANCE_TYPE":
				xmlImportScript.setTargetInstanceType(helper.getNodeValue(field));
				break;
			case "TARGET":
				xmlImportScript.setTarget(helper.getNodeValue(field));
				break;
			case "DOCUMENT_DESCRIPTION":
				xmlImportScript.setDocumentDescription(helper.getNodeValue(field));
				break;
			case "INACTIVE":
				xmlImportScript.setInactive(helper.getNodeValue(field));
				break;
			default:
				break;
			}
		}
		xmlImportScript.setScriptVersion(DUMMY_TEXT);
		xmlImportScript.setScript(DUMMY_TEXT);

		return xmlImportScript;
	}

	private XmlScriptIface generateExplicitScriptBo(Node node, String type) {
		XmlScriptExplicitBo xmlImportScript = new XmlScriptExplicitBo();
		xmlImportScript.setType(type);

		NodeList fieldList = node.getChildNodes();

		for (int j = 0; j < fieldList.getLength(); j++) {
			Node field = fieldList.item(j);
			if (!"field".equals(field.getNodeName()))
				continue;

			String fieldType = helper.getNodeAttr("name", field);

			switch (fieldType) {
			case "EXTERNAL_ID":
				xmlImportScript.setExternalId(helper.getNodeValue(field));
				break;
			case "DISPLAY_NAME":
				xmlImportScript.setDisplayName(helper.getNodeValue(field));
				break;
			case "DOCUMENT_DESCRIPTION":
				xmlImportScript.setDocumentDescription(helper.getNodeValue(field));
				break;
			case "INACTIVE":
				xmlImportScript.setInactive(helper.getNodeValue(field));
				break;
			default:
				break;
			}
		}
		xmlImportScript.setDocumentDescription(DUMMY_TEXT);
		xmlImportScript.setScript(DUMMY_TEXT);

		return xmlImportScript;
	}

	private void serialize(XmlDataStore xmlDataStore) throws Exception {
		Serializer serializer = new Persister();
		File result = new File(path + ScriptsRepoApp.RESOURCES_DIR + File.separator + ScriptsRepoApp.TEMPLATE_FILENAME);
		serializer.write(xmlDataStore, result);
	}
}
