package com.tnd.eso.integration.scm.scripts.model;

import java.util.ArrayList;
import java.util.List;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

@Root(name = "sapesourcing")
public class XmlDataStore {
	@ElementList(name = "objects")
	private List<XmlScriptIface> scripts = new ArrayList<>();

	public List<XmlScriptIface> getScripts() {
		return scripts;
	}
}
