package com.tnd.eso.integration.scm.scripts.init;

public class AbxXmlImportScriptBo implements XmlImportScriptIface {
	private String type;
	private String externalId;
	private String displayName;
	private String documentDescription;
	private String script;
	private String inactive;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public AbxXmlImportScriptBo() {
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