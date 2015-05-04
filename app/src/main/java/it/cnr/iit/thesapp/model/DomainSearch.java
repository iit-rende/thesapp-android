package it.cnr.iit.thesapp.model;

import com.google.gson.annotations.Expose;

import java.util.ArrayList;
import java.util.List;

public class DomainSearch {

	@Expose
	private List<Domain> domains = new ArrayList<Domain>();

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
}