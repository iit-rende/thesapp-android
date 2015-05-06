package it.cnr.iit.thesapp;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;

import it.cnr.iit.thesapp.adapters.DomainSpinnerAdapter;
import it.cnr.iit.thesapp.adapters.TermExplorerAdapter;
import it.cnr.iit.thesapp.fragments.TermFragment;
import it.cnr.iit.thesapp.model.DomainSearch;
import it.cnr.iit.thesapp.model.Term;
import it.cnr.iit.thesapp.utils.Logs;
import it.cnr.iit.thesapp.utils.PrefUtils;
import it.cnr.iit.thesapp.views.SearchBox;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;


public class MainActivity extends AppCompatActivity implements TermFragment.TermFragmentCallbacks {

	public static final String OPENED_TERM_DESCRIPTORS = "openedTermDescriptors";
	public static final String OPENED_TERM_DOMAINS     = "openedTermDomains";
	public static final String OPENED_TERM_LANGUAGES   = "openedTermLanguages";
	private TermExplorerAdapter termExplorerAdapter;
	private ViewPager           pager;
	private SearchBox searchBox;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		setSupportActionBar((android.support.v7.widget.Toolbar) findViewById(
				R.id.activity_toolbar));
		pager = (ViewPager) findViewById(R.id.pager);
		termExplorerAdapter = new TermExplorerAdapter(this, getSupportFragmentManager(), null,
				pager);
		pager.setOffscreenPageLimit(3);
		pager.setAdapter(termExplorerAdapter);

		if (savedInstanceState != null) {
			String[] openedTermDescriptors = savedInstanceState.getStringArray(
					OPENED_TERM_DESCRIPTORS);
			String[] openedTermDomains = savedInstanceState.getStringArray(OPENED_TERM_DOMAINS);
			String[] openedTermLanguages = savedInstanceState.getStringArray
					(OPENED_TERM_LANGUAGES);
			List<Term> terms = new ArrayList<>();
			for (int i = 0; i < openedTermDescriptors.length; i++) {
				terms.add(new Term(openedTermDescriptors[i], openedTermDomains[i],
						openedTermLanguages[i]));
			}
			termExplorerAdapter.setTerms(terms);
		}

		searchBox = (SearchBox) findViewById(R.id.search_container);
		searchBox.setSearchBoxListener(new SearchBox.SearchBoxListener() {
			@Override
			public void onTermSelected(String descriptor, String domain, String language) {
				onWordSelected(descriptor, domain, language, -1);
			}
		});
		DomainSpinnerAdapter domainSpinnerAdapter = new DomainSpinnerAdapter(MainActivity.this,
				null);
		searchBox.setDomainSpinnerAdapter(domainSpinnerAdapter);

		loadDomains();
		fetchDomains();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu_main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();

		//noinspection SimplifiableIfStatement
		if (id == R.id.action_settings) {
			return true;
		}

		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
		super.onSaveInstanceState(outState, outPersistentState);
		final List<Term> terms = termExplorerAdapter.getTerms();
		final int size = terms.size();
		String[] openedTermDescriptors = new String[size];
		String[] openedTermDomains = new String[size];
		String[] openedTermLanguages = new String[size];
		for (int i = 0; i < size; i++) {
			final Term term = terms.get(i);
			openedTermDescriptors[i] = term.getDescriptor();
			openedTermDomains[i] = term.getDomainDescriptor();
			openedTermLanguages[i] = term.getLanguage();
		}

		outState.putStringArray(OPENED_TERM_DESCRIPTORS, openedTermDescriptors);
		outState.putStringArray(OPENED_TERM_DOMAINS, openedTermDomains);
		outState.putStringArray(OPENED_TERM_LANGUAGES, openedTermLanguages);
	}


	private void loadDomains() {
		Logs.cache("Loading domains from cache");
		searchBox.setDomains(PrefUtils.loadDomains(this));
	}

	private void fetchDomains() {
		App.getApi().getService().domains(PrefUtils.loadLanguage(this),
				new Callback<DomainSearch>() {
					@Override
					public void success(DomainSearch domainSearch, Response response) {
						if (response.getStatus() == 200) {
							Logs.retrofit("Domain fetched: " + domainSearch.getDomains().size());
							searchBox.setDomains(domainSearch.getDomains());
							PrefUtils.saveDomains(MainActivity.this, domainSearch.getDomains());
						} else {
							Logs.retrofit("Domain fetch failed: " + response.getStatus() + " - " +
										  response.getReason());
						}
					}

					@Override
					public void failure(RetrofitError error) {
						error.printStackTrace();
					}
				});
	}

	public void onWordSelected(String termDescriptor, String termDomain, String termLanguage,
							   int clickedFromPage) {
		int pos = termExplorerAdapter.addTerm(termDescriptor, termDomain, termLanguage,
				clickedFromPage);
		Log.d("Pager", "Positon for word " + termDescriptor + ": " + pos);
		if (pos != -1) {
			pager.setCurrentItem(pos, true);
		}
	}

	@Override
	public Term getTerm(String termDescriptor, String termDomain, String termLanguage) {
		return termExplorerAdapter.getTerm(termDescriptor, termDomain, termLanguage);
	}

	@Override
	public void onTermClicked(String termDescriptor, String termDomain, String termLanguage,
							  int clickedFromPage) {
		onWordSelected(termDescriptor, termDomain, termLanguage, clickedFromPage);
	}

	@Override
	public void onTermFetched(Term term) {
		termExplorerAdapter.onTermFetched(term);
	}

	@Override
	public void onUpPressed() {
		if (pager.getCurrentItem() > 0) pager.setCurrentItem(pager.getCurrentItem() - 1, true);
	}
}
