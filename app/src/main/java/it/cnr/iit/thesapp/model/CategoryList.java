package it.cnr.iit.thesapp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CategoryList extends TimelineElement {
	private static final String DEFAULT_DESCRIPTOR = "CATEGORY_LIST_DESCRIPTOR";
	@SerializedName("categories")
	@Expose
	private List<Category> categories;

	public CategoryList(String domain, String language) {
		super();
		setElementKind(KIND_CATEGORY_LIST);
		setDomainDescriptor(domain);
		setLanguage(language);
	}

	public CategoryList(Domain domain) {
		super();
		setDescriptor(DEFAULT_DESCRIPTOR);
		setElementKind(KIND_CATEGORY_LIST);
		setDomain(domain);
		setLanguage(domain.getLanguage());
	}

	@Override
	public void copy(TimelineElement element) {

	}

	@Override
	public void fillMissingInfo() {

	}

	public List<Category> getCategories() {
		return categories;
	}

	public void setCategories(List<Category> categories) {
		this.categories = categories;
	}
}
