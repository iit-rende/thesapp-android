package it.cnr.iit.thesapp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CategoryList extends TimelineElement {
	@SerializedName("categories")
	@Expose
	private List<Category> categories;

	@Override
	public void copy(TimelineElement element) {

	}

	@Override
	public void fillMissingInfo() {

	}
}
