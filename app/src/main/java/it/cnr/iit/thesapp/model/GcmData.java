package it.cnr.iit.thesapp.model;

import android.support.annotation.Nullable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class GcmData {

	@SerializedName("domain")
	@Expose
	private String domain;
	@SerializedName("localizations")
	@Expose
	private List<Localization> localizations = new ArrayList<Localization>();

	/**
	 * @return The domain
	 */
	@Nullable
	public String getDomain() {
		return domain;
	}

	/**
	 * @param domain The domain
	 */
	public void setDomain(String domain) {
		this.domain = domain;
	}

	/**
	 * @return The localizations
	 */
	@Nullable
	public List<Localization> getLocalizations() {
		return localizations;
	}

	/**
	 * @param localizations The localizations
	 */
	public void setLocalizations(List<Localization> localizations) {
		this.localizations = localizations;
	}

	public class Localization {

		@SerializedName("language")
		@Expose
		private String language;
		@SerializedName("text")
		@Expose
		private String text;

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
		 * @return The text
		 */
		public String getText() {
			return text;
		}

		/**
		 * @param text The text
		 */
		public void setText(String text) {
			this.text = text;
		}
	}
}