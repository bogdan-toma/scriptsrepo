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
package com.tnd.eso.integration.scm.scripts.model;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Path;

public class AbxXmlScriptBo implements XmlScriptIface {
	@Attribute(name = "classname")
	private String type;

	@Element(name = "EXTERNAL_ID")
	@Path("fields")
	private String externalId;

	@Element(name = "DISPLAY_NAME")
	@Path("fields")
	private String displayName;

	@Element(name = "DOCUMENT_DESCRIPTION")
	@Path("fields")
	private String documentDescription;

	@Element(name = "SCRIPT")
	@Path("fields")
	private String script;

	@Element(name = "INACTIVE")
	@Path("fields")
	private String inactive;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public AbxXmlScriptBo() {
		super();
	}

	public String getExternalId() {
		return externalId;
	}

	public void setExternalId(String externalId) {
		this.externalId = externalId;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public String getDocumentDescription() {
		return documentDescription;
	}

	public void setDocumentDescription(String documentDescription) {
		this.documentDescription = documentDescription;
	}

	public String getScript() {
		return script;
	}

	public void setScript(String script) {
		this.script = script;
	}

	public String getInactive() {
		return inactive;
	}

	public void setInactive(String inactive) {
		this.inactive = inactive;
	}
}