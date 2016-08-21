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

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Path;

public class XmlScriptDefinitionBo extends AbxXmlScriptBo {
	@Element(name = "SCRIPT_CONTEXT")
	@Path("fields")
	private String scriptContext;

	@Element(name = "BUS_UNIT_CTXT")
	@Path("fields")
	private String busUnitContext;

	@Element(name = "TARGET_CLASS_ID")
	@Path("fields")
	private String targetClassId;

	@Element(name = "TARGET_INSTANCE_TYPE")
	@Path("fields")
	private String targetInstanceType;

	@Element(name = "TARGET")
	@Path("fields")
	private String target;

	@Element(name = "SCRIPT_VERSION")
	@Path("fields")
	private String scriptVersion;

	public String getScriptContext() {
		return scriptContext;
	}

	public void setScriptContext(String scriptContext) {
		this.scriptContext = scriptContext;
	}

	public String getBusUnitContext() {
		return busUnitContext;
	}

	public void setBusUnitContext(String busUnitContext) {
		String[] ctxInfo = busUnitContext.split("\\$");
		this.busUnitContext = ctxInfo[3];
	}

	public String getTargetClassId() {
		return targetClassId;
	}

	public void setTargetClassId(String targetClassId) {
		this.targetClassId = targetClassId;
	}

	public String getTargetInstanceType() {
		return targetInstanceType;
	}

	public void setTargetInstanceType(String targetInstanceType) {
		this.targetInstanceType = targetInstanceType;
	}

	public String getTarget() {
		return target;
	}

	public void setTarget(String target) {
		this.target = target;
	}

	public String getScriptVersion() {
		return scriptVersion;
	}

	public void setScriptVersion(String scriptVersion) {
		this.scriptVersion = scriptVersion;
	}
}
