package it.cnr.iit.thesapp.model;

import android.content.Context;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import it.cnr.iit.thesapp.utils.Logs;

public class DomainSearch extends TimelineElement {

	public static final String DEFAULT_DESCRIPTOR = "DOMAIN_SEARCH";
	public static final String DEFAULT_DOMAIN_DESCRIPTOR = "DOMAIN_SEARCH";

	@SerializedName("domains")
	@Expose
	private List<Domain> domains = new ArrayList<Domain>();

	public DomainSearch() {
		setElementKind(KIND_DOMAIN_CONTAINER);
		setDescriptor(DEFAULT_DESCRIPTOR);
		setDomainDescriptor(DEFAULT_DOMAIN_DESCRIPTOR);
	}

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
			Logs.cache("Copying domainSearch: " + domain);
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
		setCompletelyFetched(true);
		if (getDomains() != null) {
			for (Domain domain : getDomains()) {
				domain.setLanguage(getLanguage());
			}

			Collections.sort(getDomains(), new Comparator<Domain>() {
				public int compare(Domain domain1, Domain domain2) {
					if (domain1.getLocalization() != null && domain2.getLocalization() != null) {
						return domain1.getLocalization().compareTo(domain2.getLocalization());
					} else {
						if (domain1.getDescriptor() != null && domain2.getDescriptor() != null) {
							return domain1.getDescriptor().compareTo(domain2.getDescriptor());
						}
					}

					return 0;
				}
			});
		}
	}

	@Override
	public String toString() {
		return "DomainSearch{" +
		       "descriptor='" + getDescriptor() + '\'' +
		       ", domainDescriptor='" + getDomainDescriptor() + '\'' +
		       ", language='" + getLanguage() + '\'' +
		       '}';
	}
}