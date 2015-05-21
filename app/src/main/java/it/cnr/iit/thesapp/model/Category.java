package it.cnr.iit.thesapp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

import it.cnr.iit.thesapp.utils.Logs;

public class Category extends TimelineElement {

	@SerializedName("terms")
	@Expose
	private List<Term> terms = new ArrayList<Term>();

	public Category() {
		super();
		setElementKind(KIND_CATEGORY);
	}

	public Category(String termDescriptor, String termDomain, String termLanguage) {
		super(termDescriptor, termDomain, termLanguage, KIND_CATEGORY);
	}


	/**
	 * @return The terms
	 */
	public List<Term> getTerms() {
		return terms;
	}

	/**
	 * @param terms The terms
	 */
	public void setTerms(List<Term> terms) {
		this.terms = terms;
	}

	@Override
	public void copy(TimelineElement element) {
		if (element instanceof Category) {
			Category category = (Category) element;
			Logs.cache("Copying category: " + category);
			setDescriptor(category.getDescriptor());
			setDomain(category.getDomain());
			setDomainDescriptor(category.getDomainDescriptor());
			setLanguage(category.getLanguage());
			setTerms(category.getTerms());
			setCompletelyFetched(category.isCompletelyFetched());
		}
	}

	@Override
	public void fillMissingInfo() {
		setCompletelyFetched(true);
		setDomainDescriptor(getDomain().getDescriptor());
		if (terms != null) for (Term term : terms) {
			//term.setLanguage(getLanguage());
			term.setDomain(getDomain());
		}
	}

	@Override
	public String toString() {
		return "Category{" +
		       "descriptor='" + getDescriptor() + '\'' +
		       ", domainDescriptor='" + getDomainDescriptor() + '\'' +
		       ", language='" + getLanguage() + '\'' +
		       '}';
	}

	private String toStringComplete() {
		return "Category{" +
		       "descriptor='" + getDescriptor() + '\'' +
		       ", domainDescriptor='" + getDomainDescriptor() + '\'' +
		       ", domain='" + getDomain() + '\'' +
		       ", language='" + getLanguage() + '\'' +
		       ", terms=" + terms.size() +
		       ", completelyFetched=" + isCompletelyFetched() +
		       '}';
	}
}