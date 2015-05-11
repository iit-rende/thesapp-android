package it.cnr.iit.thesapp.views;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.List;

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
				hideKeyboard();
				final Term term = mTermSearchAdapter.getItem(pos);
				if (mListener != null) mListener.onTermSelected(term.getDescriptor(),
						term.getDomainDescriptor(), term.getLanguage());
			}
		});
		searchTextView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
			@Override
			public boolean onEditorAction(TextView textView, int arg, KeyEvent keyEvent) {
				if (arg == EditorInfo.IME_ACTION_SEARCH) {
					openSearchDropdown();
				}
				return false;
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

	private void hideKeyboard() {
		InputMethodManager imm = (InputMethodManager) getContext().getSystemService(
				Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(searchTextView.getWindowToken(), 0);
	}

	private void openSearchDropdown() {
		searchTextView.showDropDown();
	}

	public void setTermSearchAdapter(TermSearchAdapter termSearchAdapter) {
		Logs.ui("Setting term search adapter");
		this.mTermSearchAdapter = termSearchAdapter;
		searchTextView.setAdapter(mTermSearchAdapter);
		onLanguageSelected(PrefUtils.loadLanguage(getContext()));
	}

	public void setDomainSpinnerAdapter(DomainSpinnerAdapter domainSpinnerAdapter) {
		this.mDomainSpinnerAdapter = domainSpinnerAdapter;
		domainSpinner.setAdapter(mDomainSpinnerAdapter);
		if (selectedDomain == null) {
			int wantedPosition = mDomainSpinnerAdapter.getPositionFromDescriptor(
					PrefUtils.loadDomain(getContext()));
			if (wantedPosition >= 0) domainSpinner.setSelection(wantedPosition);
		}
	}

	public void setDomains(List<Domain> domains) {
		mDomainSpinnerAdapter.setDomains(domains);
		if (selectedDomain == null) {
			int wantedPosition = mDomainSpinnerAdapter.getPositionFromDescriptor(
					PrefUtils.loadDomain(getContext()));
			if (wantedPosition >= 0) domainSpinner.setSelection(wantedPosition);
		}
	}

	private void onDomainSelected(Domain domain) {
		PrefUtils.saveDomain(getContext(), domain.getDescriptor());
		selectedDomain = domain;
		mTermSearchAdapter.setDomain(domain);
		searchTextView.performFiltering(searchTextView.getText(), 0);
	}

	private void onLanguageSelected(String language) {
		mTermSearchAdapter.setLanguage(language);
	}


	public void setSearchBoxListener(SearchBoxListener listener) {
		this.mListener = listener;
	}

	public void setSearchInterval(long searchInterval) {
		searchTextView.setAutoCompleteDelay(searchInterval);
	}

	public interface SearchBoxListener {
		void onTermSelected(String descriptor, String domain, String language);
	}
}
