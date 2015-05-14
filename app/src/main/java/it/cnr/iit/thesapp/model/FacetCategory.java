package it.cnr.iit.thesapp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FacetCategory {
	@SerializedName("descriptor")
	@Expose
	private String descriptor;

	@SerializedName("count")
	@Expose
	private int count;

	@SerializedName("language")
	@Expose
	private String language;

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

	/**
	 * @return The count
	 */
	public int getCount() {
		return count;
	}

	/**
	 * @param count The count
	 */
	public void setCount(int count) {
		this.count = count;
	}

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
}
