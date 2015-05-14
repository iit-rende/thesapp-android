package it.cnr.iit.thesapp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class Facet {
	@SerializedName("facets")
	@Expose
	private List<FacetCategory> categories = new ArrayList<FacetCategory>();

	/**
	 * @return The categories
	 */
	public List<FacetCategory> getCategories() {
		return categories;
	}

	/**
	 * @param categories The categories
	 */
	public void setCategories(List<FacetCategory> categories) {
		this.categories = categories;
	}
}

