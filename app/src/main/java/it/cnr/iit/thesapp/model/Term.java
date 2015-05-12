package it.cnr.iit.thesapp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

import it.cnr.iit.thesapp.utils.Logs;

public class Term extends TimelineElement {
	@SerializedName("scopeNote")
	@Expose
	private String  scopeNote;
	@SerializedName("semantic")
	@Expose
	private boolean semantic;
	@SerializedName("categories")
	@Expose
	private List<Category> categories    = new ArrayList<Category>();
	@SerializedName("relatedTerms")
	@Expose
	private List<Term>     relatedTerms  = new ArrayList<Term>();
	@SerializedName("narrowerTerms")
	@Expose
	private List<Term>     narrowerTerms = new ArrayList<Term>();
	@SerializedName("broaderTerms")
	@Expose
	private List<Term>     broaderTerms  = new ArrayList<Term>();
	@SerializedName("useFor")
	@Expose
	private List<Term>     useFor        = new ArrayList<Term>();
	@SerializedName("usedFor")
	@Expose
	private Term usedFor;
	@SerializedName("localizations")
	@Expose
	private List<Term> localizations = new ArrayList<Term>();
	@SerializedName("hierarchy")
	@Expose
	private List<Term> hierarchy     = new ArrayList<Term>();

	public Term() {
		super();
		this.setElementKind(KIND_TERM);
	}

	public Term(String termDescriptor, String termDomain, String termLanguage) {
		super(termDescriptor, termDomain, termLanguage, KIND_TERM);
	}

	@Override
	public void copy(TimelineElement element) {
		if (element instanceof Term) {
			Term term = (Term) element;
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
	}

	@Override
	public void fillMissingInfo() {
		Logs.thesaurus("Filling term: " + this.toStringComplete());
		setDomainDescriptor(getDomain().getDescriptor());
		if (categories != null) for (Category term : categories) {
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
	 * @return The categories
	 */
	public List<Category> getCategories() {
		return categories;
	}

	/**
	 * @param categories The categories
	 */
	public void setCategories(List<Category> categories) {
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



	public List<Term> getHierarchy() {
		return hierarchy;
	}

	public void setHierarchy(List<Term> hierarchy) {
		this.hierarchy = hierarchy;
	}


	public String toStringComplete() {
		return "Term{" +
			   "descriptor='" + getDescriptor() + '\'' +
			   ", domainDescriptor='" + getDomainDescriptor() + '\'' +
			   ", domain='" + getDomain() + '\'' +
			   ", language='" + getLanguage() + '\'' +
			   "scopeNote='" + scopeNote + '\'' +
			   ", semantic=" + semantic +
			   ", categories=" + categories.size() +
			   ", relatedTerms=" + relatedTerms.size() +
			   ", narrowerTerms=" + narrowerTerms.size() +
			   ", broaderTerms=" + broaderTerms.size() +
			   ", useFor=" + useFor.size() +
			   ", usedFor=" + usedFor +
			   ", localizations=" + localizations.size() +
			   ", hierarchy=" + hierarchy.size() +
			   ", completelyFetched=" + isCompletelyFetched() +
			   '}';
	}
}