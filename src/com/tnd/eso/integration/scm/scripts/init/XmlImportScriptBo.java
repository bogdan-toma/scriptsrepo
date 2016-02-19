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

public class XmlImportScriptBo {
	private String externalId;
	private String displayName;
	private String scriptContext;
	private String busUnitContext;
	private String targetClassId;
	private String targetInstanceType;
	private String target;
	private String documentDescription;
	private String inactive;
	private String scriptVersion;
	private String script;

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

	public String getDocumentDescription() {
		return documentDescription;
	}

	public void setDocumentDescription(String documentDescription) {
		this.documentDescription = documentDescription;
	}

	public String getInactive() {
		return inactive;
	}

	public void setInactive(String inactive) {
		this.inactive = inactive;
	}

	public String getScriptVersion() {
		return scriptVersion;
	}

	public void setScriptVersion(String scriptVersion) {
		this.scriptVersion = scriptVersion;
	}

	public String getScript() {
		return script;
	}

	public void setScript(String script) {
		this.script = script;
	}
}
