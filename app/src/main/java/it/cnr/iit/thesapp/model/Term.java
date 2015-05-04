package it.cnr.iit.thesapp.model;

import com.google.gson.annotations.Expose;

import java.util.ArrayList;
import java.util.List;

public class Term {

	@Expose
	private String descriptor;
	@Expose
	private String scopeNote;
	@Expose
	private String domain;
	@Expose
	private String language;
	@Expose
	private List<Term> categories    = new ArrayList<Term>();
	@Expose
	private List<Term> relatedTerms  = new ArrayList<Term>();
	@Expose
	private List<Term> narrowerTerms = new ArrayList<Term>();
	@Expose
	private List<Term> broaderTerms  = new ArrayList<Term>();
	@Expose
	private List<Term> useFor        = new ArrayList<Term>();
	@Expose
	private Term usedFor;
	@Expose
	private List<Term> localizations = new ArrayList<Term>();

	public Term(String termDescriptor, String termDomain, String termLanguage) {
		this.descriptor = termDescriptor;
		this.domain = termDomain;
		this.language = termLanguage;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		Term term = (Term) o;

		if (descriptor != null ? !descriptor.equals(term.descriptor) : term.descriptor != null)
			return false;
		if (domain != null ? !domain.equals(term.domain) : term.domain != null) return false;
		return !(language != null ? !language.equals(term.language) : term.language != null);
	}

	@Override
	public int hashCode() {
		int result = descriptor != null ? descriptor.hashCode() : 0;
		result = 31 * result + (domain != null ? domain.hashCode() : 0);
		result = 31 * result + (language != null ? language.hashCode() : 0);
		return result;
	}

	/**
	 * @return The descriptor
	 */
	public String getDescriptor() {
		return descriptor;
	}

	/**
	 * @param descriptor The descriptor
	 */
	public void setDescriptor(String descriptor) {
		this.descriptor = descriptor;
	}

	/**
	 * @return The scopeNote
	 */
	public String getScopeNote() {
		return scopeNote;
	}

	/**
	 * @param scopeNote The scopeNote
	 */
	public void setScopeNote(String scopeNote) {
		this.scopeNote = scopeNote;
	}

	/**
	 * @return The domain
	 */
	public String getDomain() {
		return domain;
	}

	/**
	 * @param domain The domain
	 */
	public void setDomain(String domain) {
		this.domain = domain;
	}

	/**
	 * @return The language
	 */
	public String getLanguage() {
		return language;
	}

	/**
	 * @param language The language
	 */
	public void setLanguage(String language) {
		this.language = language;
	}

	/**
	 * @return The categories
	 */
	public List<Term> getCategories() {
		return categories;
	}

	/**
	 * @param categories The categories
	 */
	public void setCategories(List<Term> categories) {
		this.categories = categories;
	}

	/**
	 * @return The relatedTerms
	 */
	public List<Term> getRelatedTerms() {
		return relatedTerms;
	}

	/**
	 * @param relatedTerms The relatedTerms
	 */
	public void setRelatedTerms(List<Term> relatedTerms) {
		this.relatedTerms = relatedTerms;
	}

	/**
	 * @return The narrowerTerms
	 */
	public List<Term> getNarrowerTerms() {
		return narrowerTerms;
	}

	/**
	 * @param narrowerTerms The narrowerTerms
	 */
	public void setNarrowerTerms(List<Term> narrowerTerms) {
		this.narrowerTerms = narrowerTerms;
	}

	/**
	 * @return The broaderTerms
	 */
	public List<Term> getBroaderTerms() {
		return broaderTerms;
	}

	/**
	 * @param broaderTerms The broaderTerms
	 */
	public void setBroaderTerms(List<Term> broaderTerms) {
		this.broaderTerms = broaderTerms;
	}

	/**
	 * @return The useFor
	 */
	public List<Term> getUseFor() {
		return useFor;
	}

	/**
	 * @param useFor The useFor
	 */
	public void setUseFor(List<Term> useFor) {
		this.useFor = useFor;
	}

	/**
	 * @return The usedFor
	 */
	public Term getUsedFor() {
		return usedFor;
	}

	/**
	 * @param usedFor The usedFor
	 */
	public void setUsedFor(Term usedFor) {
		this.usedFor = usedFor;
	}

	/**
	 * @return The localizations
	 */
	public List<Term> getLocalizations() {
		return localizations;
	}

	/**
	 * @param localizations The localizations
	 */
	public void setLocalizations(List<Term> localizations) {
		this.localizations = localizations;
	}
}