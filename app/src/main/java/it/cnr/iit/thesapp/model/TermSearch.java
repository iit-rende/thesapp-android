package it.cnr.iit.thesapp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class TermSearch {

	@SerializedName("query")
	@Expose
	private String query;
	@SerializedName("domain")
	@Expose
	private String domain;
	@SerializedName("count")
	@Expose
	private int count;
	@SerializedName("suggestions")
	@Expose
	private List<Term> suggestions = new ArrayList<Term>();
	@SerializedName("facets")
	@Expose
	private FacetContainer facets;

	/**
	 * @return The query
	 */
	public String getQuery() {
		return query;
	}

	/**
	 * @param query The query
	 */
	public void setQuery(String query) {
		this.query = query;
	}

	/**
	 * @return The suggestions
	 */
	public List<Term> getSuggestions() {
		return suggestions;
	}

	/**
	 * @param suggestions The suggestions
	 */
	public void setSuggestions(List<Term> suggestions) {
		this.suggestions = suggestions;
	}

	public String getDomain() {
		return domain;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public FacetContainer getFacets() {
		return facets;
	}

	public void setFacets(FacetContainer facets) {
		this.facets = facets;
	}
}