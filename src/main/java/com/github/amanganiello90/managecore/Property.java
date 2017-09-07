package com.github.amanganiello90.managecore;

public class Property {

	/**
	 * The property that defines the version of the artifact to use.
	 *
	 */
	private String name;

	/**
	 * The version property value .
	 *
	 */
	private String version;

	public Property(String name) {
		this.name = name;
		this.version = null;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

}
