package it.cnr.iit.thesapp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import it.cnr.iit.thesapp.utils.Logs;

public class CategoryList extends TimelineElement {

	private static final String DEFAULT_DESCRIPTOR = "CATEGORY_LIST_DESCRIPTOR";
	@SerializedName("categories")
	@Expose
	private List<Category> categories;

	public CategoryList() {
		setDescriptor(DEFAULT_DESCRIPTOR);
		setElementKind(KIND_CATEGORY_LIST);
	}

	public CategoryList(String domain, String language) {
		super();
		setDescriptor(DEFAULT_DESCRIPTOR);
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
		if (element instanceof CategoryList) {
			CategoryList categoryList = (CategoryList) element;
			Logs.cache("Copying categoryList: " + categoryList);
			setDescriptor(categoryList.getDescriptor());
			setDomain(categoryList.getDomain());
			setDomainDescriptor(categoryList.getDomainDescriptor());
			setLanguage(categoryList.getLanguage());
			setCategories(categoryList.getCategories());
			setCompletelyFetched(categoryList.isCompletelyFetched());
		}
	}

	@Override
	public void fillMissingInfo() {
		setCompletelyFetched(true);
		setDomainDescriptor(getDomain().getDescriptor());
		if (categories != null) for (Category category : categories) {
			category.setDomain(getDomain());
		}
	}

	@Override
	public String toString() {
		return "CategoryList{" +
		       "descriptor='" + getDescriptor() + '\'' +
		       ", domainDescriptor='" + getDomainDescriptor() + '\'' +
		       ", language='" + getLanguage() + '\'' +
		       '}';
	}

	public List<Category> getCategories() {
		return categories;
	}

	public void setCategories(List<Category> categories) {
		this.categories = categories;
	}
}
