package it.cnr.iit.thesapp.views;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
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
import it.cnr.iit.thesapp.utils.Logs;
import it.cnr.iit.thesapp.utils.PrefUtils;

public class SearchBox extends FrameLayout {
	//Adapters
	private TermSearchAdapter    mTermSearchAdapter;
	private DomainSpinnerAdapter mDomainSpinnerAdapter;

	//Widgets
	private DelayedAutoCompleteTextView searchTextView;
	private Spinner                     domainSpinner;
	private SearchBoxListener           mListener;

	//Variables
	private Domain selectedDomain;
	private String selectedLanguage;

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

	@TargetApi(Build.VERSION_CODES.LOLLIPOP)
	public SearchBox(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
		super(context, attrs, defStyleAttr, defStyleRes);
		init();
	}

	public void init() {
		Logs.ui("Inflating search box");
		inflate(getContext(), R.layout.panel_search_box, this);
	}

	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();

		searchTextView = (DelayedAutoCompleteTextView) findViewById(R.id.search_text);
		searchTextView.setLoadingIndicator((android.widget.ProgressBar) findViewById(
				R.id.pb_loading_indicator));
		searchTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapter, View view, int pos, long id) {
				final Term term = mTermSearchAdapter.getItem(pos);
				if (mListener != null) mListener.onTermSelected(term.getDescriptor(),
						term.getDomain(), term.getLanguage());
			}
		});

		setTermSearchAdapter(new TermSearchAdapter(getContext(), null));

		domainSpinner = (Spinner) findViewById(R.id.spinner);
		domainSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				onDomainSelected(mDomainSpinnerAdapter.getItem(position));
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {

			}
		});
	}

	public void setTermSearchAdapter(TermSearchAdapter termSearchAdapter) {
		this.mTermSearchAdapter = termSearchAdapter;
		searchTextView.setAdapter(mTermSearchAdapter);
		onLanguageSelected(PrefUtils.loadLanguage(getContext()));
	}

	public void setDomainSpinnerAdapter(DomainSpinnerAdapter domainSpinnerAdapter) {
		this.mDomainSpinnerAdapter = domainSpinnerAdapter;
		domainSpinner.setAdapter(mDomainSpinnerAdapter);
		if (selectedDomain == null) {
			int wantedPosition = domainSpinnerAdapter.getPositionFromDescriptor(
					PrefUtils.loadDomain(getContext()));
			domainSpinner.setSelection(wantedPosition);
		}
	}

	private void onDomainSelected(Domain domain) {
		PrefUtils.saveDomain(getContext(), domain.getDescriptor());
		selectedDomain = domain;
		mTermSearchAdapter.setDomain(domain);
	}

	private void onLanguageSelected(String language) {
		this.selectedLanguage = language;
		mTermSearchAdapter.setLanguage(selectedLanguage);
	}


	public void setSearchBoxListener(SearchBoxListener listener) {
		this.mListener = listener;
	}

	public interface SearchBoxListener {
		void onTermSelected(String descriptor, String domain, String language);
	}
}
