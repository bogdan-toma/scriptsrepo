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

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.tnd.eso.integration.scm.scripts.ScriptsRepoApp;

public class XmlTemplateGenerator {
	private Document doc;
	private Element objectsElement;
	private String path;

	public XmlTemplateGenerator(String currentPath) {
		path = currentPath;

		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
		try {
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

			doc = docBuilder.newDocument();
			Element rootElement = doc.createElement("sapesourcing");
			doc.appendChild(rootElement);

			objectsElement = doc.createElement("objects");
			rootElement.appendChild(objectsElement);

		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}
	}

	public void addScript(XmlImportScriptIface script) {
		if (Boolean.valueOf(script.getInactive())) {
			System.out.println("Skipped import - inactive script: " + script.getDisplayName());
			return;
		}

		Element object = doc.createElement("object");
		object.setAttribute("classname", script.getType());

		Element fields = doc.createElement("fields");
		object.appendChild(fields);

		Element externalId = doc.createElement("EXTERNAL_ID");
		externalId.appendChild(doc.createTextNode(script.getExternalId()));
		fields.appendChild(externalId);

		Element displayName = doc.createElement("DISPLAY_NAME");
		displayName.appendChild(doc.createTextNode(script.getDisplayName()));
		fields.appendChild(displayName);

		Element scriptFile = doc.createElement("SCRIPT");
		scriptFile.appendChild(doc.createTextNode(script.getScript()));
		fields.appendChild(scriptFile);

		Element inactive = doc.createElement("INACTIVE");
		inactive.appendChild(doc.createTextNode(script.getInactive()));
		fields.appendChild(inactive);

		Element documentDescription = doc.createElement("DOCUMENT_DESCRIPTION");
		documentDescription.appendChild(doc.createTextNode(script.getDocumentDescription()));
		fields.appendChild(documentDescription);

		if (script instanceof XmlImportScriptDefinitionBo) {
			XmlImportScriptDefinitionBo castScript = (XmlImportScriptDefinitionBo) script;

			Element scriptContext = doc.createElement("SCRIPT_CONTEXT");
			scriptContext.appendChild(doc.createTextNode(castScript.getScriptContext()));
			fields.appendChild(scriptContext);

			Element busUnitContext = doc.createElement("BUS_UNIT_CTXT");
			busUnitContext.appendChild(doc.createTextNode(castScript.getBusUnitContext()));
			fields.appendChild(busUnitContext);

			Element targetClassId = doc.createElement("TARGET_CLASS_ID");
			targetClassId.appendChild(doc.createTextNode(castScript.getTargetClassId()));
			fields.appendChild(targetClassId);

			Element targetInstanceType = doc.createElement("TARGET_INSTANCE_TYPE");
			targetInstanceType.appendChild(doc.createTextNode(castScript.getTargetInstanceType()));
			fields.appendChild(targetInstanceType);

			Element target = doc.createElement("TARGET");
			target.appendChild(doc.createTextNode(castScript.getTarget()));
			fields.appendChild(target);

			Element scriptVersion = doc.createElement("SCRIPT_VERSION");
			scriptVersion.appendChild(doc.createTextNode(castScript.getScriptVersion()));
			fields.appendChild(scriptVersion);
		}

		objectsElement.appendChild(object);
		System.out.println("Processed import: " + script.getDisplayName());
	}

	public void write() {
		try {
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(doc);
			StreamResult result = new StreamResult(new File(path + ScriptsRepoApp.RESOURCES_DIR + File.separator + ScriptsRepoApp.TEMPLATE_FILENAME));

			transformer.transform(source, result);
		} catch (TransformerException e) {
			e.printStackTrace();
		}

	}
}
