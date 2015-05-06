package it.cnr.iit.thesapp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class Domain {

	@SerializedName("descriptor")
	@Expose
	private String descriptor;
	@SerializedName("icon")
	@Expose
	private String icon;
	@SerializedName("color")
	@Expose
	private String color;
	@SerializedName("localizations")
	@Expose
	private List<DomainLocalization> localizations = new ArrayList<DomainLocalization>();

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

	/**
	 * @return The localizations
	 */
	public List<DomainLocalization> getLocalizations() {
		return localizations;
	}

	/**
	 * @param localizations The localizations
	 */
	public void setLocalizations(List<DomainLocalization> localizations) {
		this.localizations = localizations;
	}
}