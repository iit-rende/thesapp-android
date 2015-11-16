package it.cnr.iit.thesapp.model;

import android.content.Context;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

import it.cnr.iit.thesapp.R;
import it.cnr.iit.thesapp.utils.PrefUtils;

public class Domain {
	public static final String DEFAULT_DOMAIN_DESCRIPTOR = "default_domain";
	@SerializedName("descriptor")
	@Expose
	private String descriptor;
	@SerializedName("icon")
	@Expose
	private String icon;
	@SerializedName("color")
	@Expose
	private String color;
	@SerializedName("localization")
	@Expose
	private String localization;
	@SerializedName("localizations")
	@Expose
	private List<DomainLocalization> localizations = new ArrayList<DomainLocalization>();
	private String language;

	public Domain() {
	}

	public Domain(String descriptor) {
		this.descriptor = descriptor;
	}

	public static Domain getDefault(Context context) {
		Domain domain = new Domain();
		domain.setDescriptor(DEFAULT_DOMAIN_DESCRIPTOR);
		domain.setLanguage(PrefUtils.loadLanguage(context));
		domain.setLocalization(DEFAULT_DOMAIN_DESCRIPTOR);
		domain.setColor(context.getResources()
		                       .getString(R.string.primary));
		return domain;
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

	@Override
	public String toString() {
		return "Domain{" +
			   "descriptor='" + descriptor + '\'' +
			   ", icon='" + icon + '\'' +
			   ", color='" + color + '\'' +
			   ", localizations=" + localizations.size() +
			   '}';
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public String getLocalization() {
		return localization;
	}

	public void setLocalization(String localization) {
		this.localization = localization;
	}
}