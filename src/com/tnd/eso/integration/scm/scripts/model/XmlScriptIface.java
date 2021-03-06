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

import org.simpleframework.xml.Root;

@Root(name = "object")
public interface XmlScriptIface {

	String getExternalId();

	void setExternalId(String externalId);

	String getDisplayName();

	void setDisplayName(String displayName);

	String getDocumentDescription();

	void setDocumentDescription(String documentDescription);

	String getScript();

	void setScript(String script);

	String getInactive();

	void setInactive(String inactive);

	String getType();

	void setType(String type);
}