package it.cnr.iit.thesapp.model;

import android.content.Context;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class DomainSearch extends TimelineElement {

	public static final String DEFAULT_DESCRIPTOR = "DOMAIN_SEARCH";

	@SerializedName("domains")
	@Expose
	private List<Domain> domains = new ArrayList<Domain>();

	public DomainSearch(Context context) {
		super();
		setElementKind(KIND_DOMAIN_CONTAINER);
		setDescriptor(DEFAULT_DESCRIPTOR);
		setDomain(Domain.getDefault(context));
	}

	public DomainSearch(Domain domain, String termLanguage) {
		super(DEFAULT_DESCRIPTOR, domain.getDescriptor(), termLanguage, KIND_DOMAIN_CONTAINER);
		setDomain(domain);
	}

	/**
	 * @return The domains
	 */
	public List<Domain> getDomains() {
		return domains;
	}

	/**
	 * @param domains The domains
	 */
	public void setDomains(List<Domain> domains) {
		this.domains = domains;
	}

	@Override
	public void copy(TimelineElement element) {
		if (element instanceof DomainSearch) {
			DomainSearch domain = (DomainSearch) element;
			setDescriptor(domain.getDescriptor());
			setDomains(domain.getDomains());
			setLanguage(domain.getLanguage());
			setDomain(domain.getDomain());
			setCompletelyFetched(domain.isCompletelyFetched());
			setElementKind(domain.getElementKind());
		}
	}

	@Override
	public void fillMissingInfo() {
		if (getDomains() != null) for (Domain domain : getDomains()) {
			domain.setLanguage(getLanguage());
		}
	}
}