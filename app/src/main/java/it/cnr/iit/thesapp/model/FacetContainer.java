package it.cnr.iit.thesapp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class FacetContainer {

	@SerializedName("categories")
	@Expose
	private List<FacetCategory> categories;

	public List<FacetCategory> getCategories() {
		return categories;
	}

	public void setCategories(List<FacetCategory> categories) {
		this.categories = categories;
	}
}
