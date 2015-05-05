package it.cnr.iit.thesapp.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.Spinner;

import it.cnr.iit.thesapp.R;
import it.cnr.iit.thesapp.adapters.DomainSpinnerAdapter;
import it.cnr.iit.thesapp.adapters.TermSearchAdapter;
import it.cnr.iit.thesapp.model.Domain;
import it.cnr.iit.thesapp.model.Term;

public class SearchBox extends FrameLayout {
	private TermSearchAdapter           termSearchAdapter;
	private DelayedAutoCompleteTextView searchText;
	private SearchBoxListener           mListener;
	private Spinner                     domainSpinner;
	private DomainSpinnerAdapter        domainSpinnerAdapter;
	private Domain                      selectedDomain;
	private String                      selectedLanguage;

	public SearchBox(Context context) {
		super(context);
		init();
	}

	public SearchBox(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public SearchBox(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init();
	}


	public void init() {
		inflate(getContext(), R.layout.panel_search_box, null);
	}

	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();

		searchText = (DelayedAutoCompleteTextView) findViewById(R.id.search_text);
		searchText.setLoadingIndicator((android.widget.ProgressBar) findViewById(
				R.id.pb_loading_indicator));
		searchText.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapter, View view, int pos, long id) {
				final Term term = termSearchAdapter.getItem(pos);
				if (mListener != null) mListener.onTermSelected(term.getDescriptor(),
						term.getDomain(), term.getLanguage());
			}
		});

		domainSpinner = (Spinner) findViewById(R.id.spinner);
		domainSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				onDomainSelected(domainSpinnerAdapter.getItem(position));
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {

			}
		});
	}

	private void onDomainSelected(Domain domain) {
		selectedDomain = domain;
		termSearchAdapter.setDomain(domain);
	}

	private void onLanguageSelected(String language) {
		this.selectedLanguage = language;
		termSearchAdapter.setLanguage(selectedLanguage);
	}

	public void setListener(SearchBoxListener listener) {
		this.mListener = listener;
	}

	public void setTermAdapter(TermSearchAdapter termSearchAdapter) {
		this.termSearchAdapter = termSearchAdapter;
		searchText.setAdapter(termSearchAdapter);
	}

	public void setDomainSpinnerAdapter(DomainSpinnerAdapter domainSpinnerAdapter) {
		this.domainSpinnerAdapter = domainSpinnerAdapter;
		domainSpinner.setAdapter(domainSpinnerAdapter);
	}

	public interface SearchBoxListener {
		void onTermSelected(String descriptor, String domain, String language);
	}
}
