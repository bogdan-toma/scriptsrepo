package com.tnd.eso.integration.scm.scripts.init;

public interface XmlImportScriptIface {

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