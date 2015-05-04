package it.cnr.iit.thesapp.model;

import com.google.gson.annotations.Expose;

public class Domain {

	@Expose
	private String descriptor;
	@Expose
	private String localization;
	@Expose
	private String icon;
	@Expose
	private String color;

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
	 * @return The localization
	 */
	public String getLocalization() {
		return localization;
	}

	/**
	 * @param localization The localization
	 */
	public void setLocalization(String localization) {
		this.localization = localization;
	}

	/**
	 * @return The icon
	 */
	public String getIcon() {
		return icon;
	}

	/**
	 * @param icon The icon
	 */
	public void setIcon(String icon) {
		this.icon = icon;
	}

	/**
	 * @return The color
	 */
	public String getColor() {
		return color;
	}

	/**
	 * @param color The color
	 */
	public void setColor(String color) {
		this.color = color;
	}
}