package com.tnd.eso.integration.scm.scripts;

import java.io.File;

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
import com.tnd.eso.integration.scm.scripts.xml.XmlHelper;

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
		ESO_DATA_DIR = ScriptsRepoApp.getProperties().getProperty(ESO_DATA_DIR);
		ESO_UPLOAD_DIR = ScriptsRepoApp.getProperties().getProperty(ESO_UPLOAD_DIR);
	}

	public ScriptsRepoProcessor(String fileName) {
		// Fetch repository parser
		repoParser = ReposioryParserFactory.getParser();

		// Fetch transporter
		transporter = TransporterFactory.getTransporter();
		transporter.setWorkingDir(ESO_DATA_DIR);

		// Initialise DOM parser
		domParser = new DOMParser();

		// Open XML template doc
		try {
			helper = new XmlHelper();
			domParser.parse(fileName);
			doc = domParser.getDocument();
			// Get the document's root XML node
			root = doc.getChildNodes();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void process() {
		try {
			// Navigate down the hierarchy to get to the fields node
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
				String externalId = helper.getNodeValue("EXTERNAL_ID", fieldList);

				// transport data file
				transporter.transport(repoParser.getFileContents(externalId));

				if ("odp.doccommon.scripting.callable_script".equals(objectType)) {
					helper.setNodeValue("DOCUMENT_DESCRIPTION", fieldList, repoParser.getLastCommitRevision(externalId + ".java"));
				} else {
					helper.setNodeValue("SCRIPT_VERSION", fieldList, repoParser.getLastCommitRevision(externalId + ".java"));
				}
				System.out.println("Processed: " + externalId);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			repoParser.close();
		}

		// ** Transport XML import file(s)
		transporter.setWorkingDir(ESO_UPLOAD_DIR);
		File xmlFile = export();
		System.out.println("XML generation complete.");
		transporter.transport(xmlFile);
		System.out.println("XML transport complete: " + xmlFile.getName());

		transporter.close();
	}

	private File export() {
		// write the content into xml file
		try {
			File temp = File.createTempFile("ZTND_SCRIPT_DEF_", ".xml");
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(doc);
			StreamResult result = new StreamResult(temp);
			// StreamResult result = new StreamResult(System.out);
			transformer.transform(source, result);
			return temp;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
