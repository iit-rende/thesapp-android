package it.cnr.iit.thesapp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public abstract class TimelineElement {
	public static final int KIND_TERM             = 0;
	public static final int KIND_CATEGORY         = 1;
	public static final int KIND_CATEGORY_LIST    = 2;
	public static final int KIND_DOMAIN_CONTAINER = 3;

	@SerializedName("language")
	@Expose
	private String language;
	@SerializedName("descriptor")
	@Expose
	private String descriptor;
	@SerializedName("domain")
	@Expose
	private Domain domain;
	private String domainDescriptor;
	private int    elementKind;
	private boolean completelyFetched = false;

	public TimelineElement() {}

	public TimelineElement(String termDescriptor, String termDomain, String termLanguage,
						   int elementKind) {
		this.descriptor = termDescriptor;
		this.domainDescriptor = termDomain;
		this.language = termLanguage;
		this.elementKind = elementKind;
	}

	public abstract void copy(TimelineElement element);

	public abstract void fillMissingInfo();

	public String getDescriptor() {
		return descriptor;
	}

	public void setDescriptor(String descriptor) {
		this.descriptor = descriptor;
	}

	public String getDomainDescriptor() {
		return domainDescriptor;
	}

	public void setDomainDescriptor(String domain) {
		this.domainDescriptor = domain;
	}

	public int getElementKind() {
		return elementKind;
	}

	public void setElementKind(int elementKind) {
		this.elementKind = elementKind;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public Domain getDomain() {
		return domain;
	}

	public void setDomain(Domain domain) {
		this.domain = domain;
		setDomainDescriptor(domain.getDescriptor());
	}

	public boolean isCompletelyFetched() {
		return completelyFetched;
	}

	public void setCompletelyFetched(boolean completelyFetched) {
		this.completelyFetched = completelyFetched;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		TimelineElement that = (TimelineElement) o;

		if (elementKind != that.elementKind) return false;
		if (descriptor != null ? !descriptor.equals(that.descriptor) : that.descriptor != null)
			return false;
		if (domainDescriptor != null ? !domainDescriptor.equals(that.domainDescriptor) :
			that.domainDescriptor != null) return false;
		return !(language != null ? !language.equals(that.language) : that.language != null);
	}

	@Override
	public int hashCode() {
		int result = descriptor != null ? descriptor.hashCode() : 0;
		result = 31 * result + (domainDescriptor != null ? domainDescriptor.hashCode() : 0);
		result = 31 * result + (language != null ? language.hashCode() : 0);
		result = 31 * result + elementKind;
		return result;
	}

	@Override
	public String toString() {
		return "TLElement{" +
			   "descriptor='" + descriptor + '\'' +
			   ", domainDescriptor='" + domainDescriptor + '\'' +
			   ", language='" + language + '\'' +
			   '}';
	}
}
