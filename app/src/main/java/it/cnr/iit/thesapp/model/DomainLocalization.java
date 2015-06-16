package it.cnr.iit.thesapp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DomainLocalization {

	@SerializedName("language")
	@Expose
	private String language;
	@SerializedName("descriptor")
	@Expose
	private String descriptor;
	@SerializedName("description")
	@Expose
	private String description;
	@SerializedName("termCount")
	@Expose
	private int    termCount;

	/**
	 * @return The language
	 */
	public String getLanguage() {
		return language;
	}

	/**
	 * @param language The language
	 */
	public void setLanguage(String language) {
		this.language = language;
	}

	/**
	 * @return The descriptor
	 */
	public String getDescriptor() {
		return descriptor;
	}

	/**
	 * @param descriptor The descriptor
	 */
	public void setDescriptor(String descriptor) {
		this.descriptor = descriptor;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getTermCount() {
		return termCount;
	}

	public void setTermCount(int termCount) {
		this.termCount = termCount;
	}
}
