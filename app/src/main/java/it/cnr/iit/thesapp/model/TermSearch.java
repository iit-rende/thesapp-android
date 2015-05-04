package it.cnr.iit.thesapp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class TermSearch {

	@SerializedName("query")
	@Expose
	private String query;
	@SerializedName("suggestions")
	@Expose
	private List<Term> suggestions = new ArrayList<Term>();

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
}