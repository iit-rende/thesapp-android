package it.cnr.iit.thesapp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class Term {

	@SerializedName("descriptor")
	@Expose
	private String  descriptor;
	@SerializedName("scopeNote")
	@Expose
	private String  scopeNote;
	@SerializedName("domain")
	@Expose
	private Domain  domain;
	private String  domainDescriptor;
	@SerializedName("language")
	@Expose
	private String  language;
	@SerializedName("semantic")
	@Expose
	private boolean semantic;
	@SerializedName("categories")
	@Expose
	private List<Term> categories    = new ArrayList<Term>();
	@SerializedName("relatedTerms")
	@Expose
	private List<Term> relatedTerms  = new ArrayList<Term>();
	@SerializedName("narrowerTerms")
	@Expose
	private List<Term> narrowerTerms = new ArrayList<Term>();
	@SerializedName("broaderTerms")
	@Expose
	private List<Term> broaderTerms  = new ArrayList<Term>();
	@SerializedName("useFor")
	@Expose
	private List<Term> useFor        = new ArrayList<Term>();
	@SerializedName("usedFor")
	@Expose
	private Term usedFor;
	@SerializedName("localizations")
	@Expose
	private List<Term> localizations     = new ArrayList<Term>();
	@SerializedName("hierarchy")
	@Expose
	private List<Term> hierarchy         = new ArrayList<Term>();
	private boolean    completelyFetched = false;

	public Term(String termDescriptor, String termDomain, String termLanguage) {
		this.descriptor = termDescriptor;
		this.domainDescriptor = termDomain;
		this.language = termLanguage;
	}

	@Override
	public String toString() {
		return "Term{" +
			   "descriptor='" + descriptor + '\'' +
			   ", domainDescriptor='" + domainDescriptor + '\'' +
			   ", language='" + language + '\'' +
			   '}';
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		Term term = (Term) o;

		if (descriptor != null ? !descriptor.equals(term.descriptor) : term.descriptor != null)
			return false;
		if (domainDescriptor != null ? !domainDescriptor.equals(term.domainDescriptor) :
			term.domainDescriptor != null) return false;
		return !(language != null ? !language.equals(term.language) : term.language != null);
	}

	@Override
	public int hashCode() {
		int result = descriptor != null ? descriptor.hashCode() : 0;
		result = 31 * result + (domainDescriptor != null ? domainDescriptor.hashCode() : 0);
		result = 31 * result + (language != null ? language.hashCode() : 0);
		return result;
	}

	public void copy(Term term) {
		setDescriptor(term.getDescriptor());
		setScopeNote(term.getScopeNote());
		setDomain(term.getDomain());
		setDomainDescriptor(term.getDomainDescriptor());
		setLanguage(term.getLanguage());
		setCategories(term.getCategories());
		setRelatedTerms(term.getRelatedTerms());
		setNarrowerTerms(term.getNarrowerTerms());
		setBroaderTerms(term.getBroaderTerms());
		setUseFor(term.getUseFor());
		setUsedFor(term.getUsedFor());
		setLocalizations(term.getLocalizations());
		setHierarchy(term.getHierarchy());
	}

	public void fillMissingInfo() {
		setDomainDescriptor(getDomain().getDescriptor());
		if (categories != null) for (Term term : categories) {
			term.setLanguage(getLanguage());
			term.setDomain(getDomain());
		}
		if (relatedTerms != null) for (Term term : relatedTerms) {
			term.setLanguage(getLanguage());
			term.setDomain(getDomain());
		}
		if (narrowerTerms != null) for (Term term : narrowerTerms) {
			term.setLanguage(getLanguage());
			term.setDomain(getDomain());
		}
		if (broaderTerms != null) for (Term term : broaderTerms) {
			term.setLanguage(getLanguage());
			term.setDomain(getDomain());
		}
		if (useFor != null) for (Term term : useFor) {
			term.setLanguage(getLanguage());
			term.setDomain(getDomain());
		}
		if (hierarchy != null) for (Term term : hierarchy) {
			term.setLanguage(getLanguage());
			term.setDomain(getDomain());
		}
		if (usedFor != null) {
			usedFor.setLanguage(getLanguage());
			usedFor.setDomain(getDomain());
		}
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
	 * @return The domainDescriptor
	 */
	public String getDomainDescriptor() {
		return domainDescriptor;
	}

	/**
	 * @param domainDescriptor The domainDescriptor
	 */
	public void setDomainDescriptor(String domainDescriptor) {
		this.domainDescriptor = domainDescriptor;
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

	public boolean isSemantic() {
		return semantic;
	}

	public void setSemantic(boolean semantic) {
		this.semantic = semantic;
	}

	public boolean isCompletelyFetched() {
		return completelyFetched;
	}

	public void setCompletelyFetched(boolean completelyFetched) {
		this.completelyFetched = completelyFetched;
	}

	public List<Term> getHierarchy() {
		return hierarchy;
	}

	public void setHierarchy(List<Term> hierarchy) {
		this.hierarchy = hierarchy;
	}

	public Domain getDomain() {
		return domain;
	}

	public void setDomain(Domain domain) {
		this.domain = domain;
		setDomainDescriptor(domain.getDescriptor());
	}
}